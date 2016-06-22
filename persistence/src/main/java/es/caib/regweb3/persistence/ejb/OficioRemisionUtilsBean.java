package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.OficiosRemisionInternoOrganismo;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (Convertir en EJB)
 *         Date: 16/01/14
 */
@Stateless(name = "OficioRemisionUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionUtilsBean implements OficioRemisionUtilsLocal {

    public final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(name = "OficinaEJB")
    public OficinaLocal oficinaEjb;

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> oficiosPendientesRemisionInterna(Long idLibro, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos) and ";
        }

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct(re.destino.denominacion) from RegistroEntrada as re where " +
                "re.estado = :valido and re.libro.id = :idLibro and " +
                "re.destino != null and " + organismosWhere +
                " re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idLibro", idLibro);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficiosRemisionInternoOrganismo> oficiosPendientesRemisionInterna(Integer any, Long idOficina, Long idLibro, Set<Long> organismos) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos) and ";
        }

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct(re.destino) from RegistroEntrada as re where " + anyWhere +
                " re.estado = :valido and re.libro.id = :idLibro and re.oficina.id = :idOficina and " +
                "re.destino != null and " + organismosWhere +
                "re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idLibro", idLibro);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (any != null) {
            q.setParameter("any", any);
        }
        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        List<Organismo> organismosPropios = q.getResultList();

        // Buscamos los RegistroEntrada pendientes de tramitar de cada uno de los Organismos encontrados
        List<OficiosRemisionInternoOrganismo> oficiosRemisionOrganismo = new ArrayList<OficiosRemisionInternoOrganismo>();

        // Por cada organismo Propio, buscamos sus RegistrosEntrada
        for (Organismo organismo : organismosPropios) {

            OficiosRemisionInternoOrganismo oficios = new OficiosRemisionInternoOrganismo();

            oficios.setOrganismo(organismo);
            oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setOficiosRemision(oficiosRemisionByOrganismoPropio(organismo.getId(), any, idOficina, idLibro));

            //Los añadimos a la lista
            oficiosRemisionOrganismo.add(oficios);

        }

        return oficiosRemisionOrganismo;

    }

    @Override
    public Long oficiosPendientesRemisionInternaCount(List<Libro> libros, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos) and ";
        }

        Query q;
        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where " +
                "re.estado = :valido and re.libro in (:libros) and " +
                "re.destino != null and " + organismosWhere +
                " re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("libros", libros);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return (Long) q.getSingleResult();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos) and ";
        }

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.estado = :valido and " +
                "re.destino != null and " + organismosWhere +
                " re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> oficiosPendientesRemisionExterna(Long idLibro) throws Exception {

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar

        Query q;
        q = em.createQuery("Select distinct(registroEntrada.destinoExternoDenominacion) from RegistroEntrada as registroEntrada where " +
                "registroEntrada.estado = :valido and registroEntrada.libro.id = :idLibro and " +
                "registroEntrada.destino is null and " +
                "registroEntrada.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idLibro", idLibro);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficiosRemisionOrganismo> oficiosPendientesRemisionExterna(Integer any, Long idOficina, Long idLibro, Entidad entidadActiva) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(registroEntrada.fecha) = :any and ";
        }

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct(registroEntrada.destinoExternoCodigo) from RegistroEntrada as registroEntrada where " + anyWhere +
                " registroEntrada.estado = :valido and registroEntrada.libro.id = :idLibro and " +
                "registroEntrada.oficina.id = :idOficina and registroEntrada.destino is null and " +
                "registroEntrada.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idLibro", idLibro);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (any != null) {
            q.setParameter("any", any);
        }

        List<String> organismosExternos = q.getResultList();

        // Buscamos los RegistroEntrada pendientes de tramitar de cada uno de los Organismos encontrados
        List<OficiosRemisionOrganismo> oficiosRemisionOrganismo = new ArrayList<OficiosRemisionOrganismo>();


        // Por cada organismo Externo, buscamos sus RegistrosEntrada
        for (String organismo : organismosExternos) {

            OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();

            // Consulta en base de datos si la Entidad Actual está en SIR
            Boolean isSir = (Boolean) em.createQuery("select e.sir from Entidad as e where e.id = :id").setParameter("id", entidadActiva.getId()).getSingleResult();
            if (isSir) {

                // Averiguamos si el Organismos Externo está en Sir o no
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                if (!organismosExternos.isEmpty()) {
                    List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismo); //TODO: Revisar que la cerca d'Oficines SIR la fa correctament
                    if (oficinasSIR.size() > 0) {
                        oficios.setSir(true);
                        oficios.setOficinasSIR(oficinasSIR);
                        log.info("El organismo externo " + organismo + " TIENE oficinas Sir: " + oficinasSIR.size());
                    } else {
                        log.info("El organismo externo " + organismo + " no tiene oficinas Sir");
                    }
                }
            }
            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setOficiosRemision(oficiosRemisionByOrganismoExterno(organismo, any, idOficina, idLibro));

            //Los añadimos a la lista
            oficiosRemisionOrganismo.add(oficios);

        }

        return oficiosRemisionOrganismo;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long oficiosPendientesRemisionExternaCount(List<Libro> libros) throws Exception {


        Query q;
        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where " +
                "registroEntrada.estado = :valido  and registroEntrada.libro in (:libros) and " +
                "registroEntrada.destino is null and " +
                "registroEntrada.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("libros", libros);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        return (Long) q.getSingleResult();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> oficiosRemisionByOrganismoPropio(Long idOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(registroEntrada.fecha) = :any and ";
        }

        Query q;
        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where " + anyWhere +
                " registroEntrada.libro.id = :idLibro and registroEntrada.oficina.id = :idOficina " +
                "and registroEntrada.destino.id = :idOrganismo and registroEntrada.estado = :valido " +
                "order by registroEntrada.fecha desc");

        q.setParameter("idOrganismo", idOrganismo);
        if (any != null) {
            q.setParameter("any", any);
        }
        q.setParameter("idOficina", idOficina);
        q.setParameter("idLibro", idLibro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);


        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> oficiosRemisionByOrganismoExterno(String codigoOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(registroEntrada.fecha) = :any and ";
        }

        Query q;
        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where " + anyWhere +
                " registroEntrada.libro.id = :idLibro and registroEntrada.oficina.id = :idOficina " +
                "and registroEntrada.destinoExternoCodigo = :codigoOrganismo and registroEntrada.estado = :valido " +
                "order by registroEntrada.fecha desc");

        q.setParameter("codigoOrganismo", codigoOrganismo);
        if (any != null) {
            q.setParameter("any", any);
        }
        q.setParameter("idOficina", idOficina);
        q.setParameter("idLibro", idLibro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);


        return q.getResultList();
    }


    /**
     * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
     * Crea un RegistroSalida por cada uno de los RegistroEntrada que contenga el OficioRemision
     * Crea la trazabilidad para los RegistroEntrada y RegistroSalida
     *
     * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     */
    @Override
    public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                    RegwebConstantes.REGISTRO_OFICIO_INTERNO);
        }

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
     *
     * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param organismoExterno
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
                                                     String organismoExternoDenominacion, Long idLibro, String identificadorIntercambioSir)
            throws Exception, I18NException, I18NValidationException {

        //Organismo organismoDestino = organismoEjb.findById(idOrganismo);

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setIdentificadorIntercambioSir(identificadorIntercambioSir);

        if (identificadorIntercambioSir == null) { //todo: modificar el estado cuando se implemente SIR
            oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ENVIADO);
            oficioRemision.setFechaEstado(new Date());
        } else {
            oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ENVIADO);
            oficioRemision.setFechaEstado(new Date());
        }

        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExterno);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                    RegwebConstantes.REGISTRO_OFICIO_EXTERNO);
        }

        return oficioRemision;

    }

    /**
     * Procesa un OficioRemision pendiente de llegada, creando tantos Registros de Entrada,
     * como contenga el Oficio.
     *
     * @param oficioRemision
     * @throws Exception
     */
    @Override
    public List<RegistroEntrada> procesarOficioRemision(OficioRemision oficioRemision,
                                                        UsuarioEntidad usuario, Oficina oficinaActiva,
                                                        List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException {

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        // Recorremos los RegistroEntrada del Oficio y Libro de registro seleccionado
        for (int i = 0; i < oficios.size(); i++) {

            OficioPendienteLlegada oficio = oficios.get(i);

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(oficio.getIdRegistroEntrada());
            Libro libro = libroEjb.findById(oficio.getIdLibro());

            RegistroEntrada nuevoRE = new RegistroEntrada();
            nuevoRE.setUsuario(usuario);
            nuevoRE.setDestino(new Organismo(oficio.getIdOrganismoDestinatario()));
            nuevoRE.setOficina(oficinaActiva);
            nuevoRE.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            nuevoRE.setLibro(libro);
            nuevoRE.setRegistroDetalle(registroEntrada.getRegistroDetalle());

            synchronized (this) {
                nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE, usuario, null);
            }

            registros.add(nuevoRE);

            // ACTUALIZAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroEntrada(oficioRemision.getId(), registroEntrada.getId());
            trazabilidad.setRegistroEntradaDestino(nuevoRE);

            trazabilidadEjb.merge(trazabilidad);

        }

        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemision = oficioRemisionEjb.merge(oficioRemision);

        return registros;

    }


}
