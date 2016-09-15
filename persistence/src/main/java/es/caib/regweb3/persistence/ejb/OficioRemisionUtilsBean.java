package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.OficiosRemisionExternoOrganismo;
import es.caib.regweb3.persistence.utils.OficiosRemisionInternoOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
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

    @EJB(name = "OficinaEJB")
    public OficinaLocal oficinaEjb;

    @EJB(name = "CatEstadoEntidadEJB")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organismosPendientesRemisionInterna(List<Libro> libros, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos) and ";
        }

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct re.destino.id, re.destino.denominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.libro in (:libros) and " +
                "re.destino != null and " + organismosWhere +
                " re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("libros", libros);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        List<Organismo> organismosDestino =  new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Organismo organismo = new Organismo((Long) object[0], (String) object[1]);

            organismosDestino.add(organismo);
        }

        return organismosDestino;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionInternoOrganismo oficiosPendientesRemisionInterna(Integer pageNumber, Integer any, Long idOficina, Long idLibro, Long idOrganismo, Set<Long> organismos) throws Exception {

        OficiosRemisionInternoOrganismo oficios = new OficiosRemisionInternoOrganismo();
        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

        oficios.setOrganismo(organismo);
        oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

        //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
        oficios.setPaginacion(oficiosRemisionByOrganismoInterno(pageNumber,organismo.getId(), any, idOficina, idLibro));

        return oficios;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosRemisionByOrganismoInterno(Integer pageNumber, Long idOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select re from RegistroEntrada as re where " + anyWhere +
                " re.libro.id = :idLibro and re.oficina.id = :idOficina " +
                "and re.destino.id = :idOrganismo and re.estado = :valido " +
                "order by re.fecha desc");

        q = em.createQuery(query.toString());
        q2 = em.createQuery(query.toString().replaceAll("Select re", "Select count(re.id)"));

        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idLibro", idLibro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("idOrganismo", idOrganismo);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(10);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
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

        // Parámetros
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

        // Parámetros
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
    public List<Organismo> organismosPendientesRemisionExterna(List<Libro> libros) throws Exception {

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct re.destinoExternoCodigo, re.destinoExternoDenominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.libro in (:libros) and " +
                "re.destino is null and " +
                "re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("libros", libros);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        List<Organismo> organismosDestino =  new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Organismo organismo = new Organismo(null,(String) object[0], (String) object[1]);

            organismosDestino.add(organismo);
        }

        return organismosDestino;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionExternoOrganismo oficiosPendientesRemisionExterna(Integer pageNumber,Integer any, Long idOficina, Long idLibro, String codigoOrganismo, Entidad entidadActiva) throws Exception {

        OficiosRemisionExternoOrganismo oficios = new OficiosRemisionExternoOrganismo();

        // Obtenemos el Organismo externo de Dir3Caib
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
        UnidadTF unidadTF = unidadesService.obtenerUnidad(codigoOrganismo,null,null);

        if(unidadTF != null){
            Organismo organismoExterno = new Organismo(null,codigoOrganismo,unidadTF.getDenominacion());
            organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setVigente(true);
            oficios.setOrganismo(organismoExterno);

            // Comprueba si la Entidad Actual está en SIR
            Boolean isSir = (Boolean) em.createQuery("select e.sir from Entidad as e where e.id = :id").setParameter("id", entidadActiva.getId()).getSingleResult();
            if (isSir) {
                // Averiguamos si el Organismo Externo está en Sir o no
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismoExterno.getCodigo()); //TODO: Revisar que la cerca d'Oficines SIR la fa correctament
                if (oficinasSIR.size() > 0) {
                    oficios.setSir(true);
                    oficios.setOficinasSIR(oficinasSIR);
                    log.info("El organismo externo " + organismoExterno + " TIENE oficinas Sir: " + oficinasSIR.size());
                } else {
                    log.info("El organismo externo " + organismoExterno + " no tiene oficinas Sir");
                }

            }

            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setPaginacion(oficiosRemisionByOrganismoExterno(pageNumber, organismoExterno.getCodigo(), any, idOficina, idLibro));
        }

        return oficios;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosRemisionByOrganismoExterno(Integer pageNumber, String codigoOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        StringBuilder query = new StringBuilder("Select re from RegistroEntrada as re where " + anyWhere +
                " re.libro.id = :idLibro and re.oficina.id = :idOficina " +
                " and re.destino is null and re.destinoExternoCodigo = :codigoOrganismo and re.estado = :valido " +
                " order by re.fecha desc");

        Query q;
        Query q2;

        q = em.createQuery(query.toString());
        q2 = em.createQuery(query.toString().replaceAll("Select re", "Select count(re.id)"));

        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("codigoOrganismo", codigoOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idLibro", idLibro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("codigoOrganismo", codigoOrganismo);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(10);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
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
            oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO);
            oficioRemision.setFechaEstado(new Date());
        } else {
            oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO);
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
