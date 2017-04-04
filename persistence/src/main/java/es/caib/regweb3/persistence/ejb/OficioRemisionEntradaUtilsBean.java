package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.Oficio;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
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
@Stateless(name = "OficioRemisionEntradaUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionEntradaUtilsBean implements OficioRemisionEntradaUtilsLocal {

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
    public List<Organismo> organismosEntradaPendientesRemision(Long idOficina, List<Libro> libros, Set<Long> organismos) throws Exception {

        List<Organismo> organismosDestino =  new ArrayList<Organismo>();

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = " and re.destino.id not in (:organismos) ";
        }

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct re.destino.codigo, re.destino.denominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino != null " + organismosWhere);

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }


        List<Object[]> internos = q.getResultList();
        for (Object[] object : internos){
            Organismo organismo = new Organismo(null,(String) object[0], (String) object[1]);

            organismosDestino.add(organismo);
        }


        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
        Query q1;
        q1 = em.createQuery("Select distinct re.destinoExternoCodigo, re.destinoExternoDenominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino is null ");

        // Parámetros
        q1.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q1.setParameter("idOficina", idOficina);
        q1.setParameter("libros", libros);

        List<Object[]> externos = q1.getResultList();

        for (Object[] object : externos){
            Organismo organismo = new Organismo(null,(String) object[0], (String) object[1]);

            organismosDestino.add(organismo);
        }

        return organismosDestino;

    }

    @Override
    public Long oficiosEntradaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<Long> organismos) throws Exception {

        Long total = null;

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "and re.destino.id not in (:organismos)";
        }

        Query q;
        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino != null " + organismosWhere);

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        total = (Long) q.getSingleResult();

        Query q1;
        q1 = em.createQuery("Select count(re.id) from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino is null ");

        q1.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q1.setParameter("idOficina", idOficina);
        q1.setParameter("libros", libros);

        total = total +  (Long) q1.getSingleResult();

        return total;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Integer pageNumber, final Integer resultsPerPage, Integer any, Long idOficina, Long idLibro, String codigoOrganismo, Set<Long> organismos, Entidad entidadActiva) throws Exception {

        OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();

        Organismo organismo = organismoEjb.findByCodigoEntidad(codigoOrganismo, entidadActiva.getId());

        if(organismo != null) { // Destinatario organismo interno
            oficios.setOrganismo(organismo);
            oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setPaginacion(oficiosRemisionByOrganismoInterno(pageNumber,resultsPerPage,organismo.getId(), any, idOficina, idLibro));

        }else { // Destinatario organismo externo

            oficios.setExterno(true);

            // Obtenemos el Organismo externo de Dir3Caib
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
            UnidadTF unidadTF = unidadesService.obtenerUnidad(codigoOrganismo,null,null);

            if(unidadTF != null){
                Organismo organismoExterno = new Organismo(null,codigoOrganismo,unidadTF.getDenominacion());
                organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                oficios.setVigente(true);
                oficios.setOrganismo(organismoExterno);

                // Comprueba si la Entidad Actual está en SIR
                //Boolean isSir = (Boolean) em.createQuery("select e.sir from Entidad as e where e.id = :id").setParameter("id", idEntidadActiva).getSingleResult();
                if (entidadActiva.getSir()) {
                    // Averiguamos si el Organismo Externo está en Sir o no
                    Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                    List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismoExterno.getCodigo());
                    if (oficinasSIR.size() > 0) {
                        oficios.setSir(true);
                        oficios.setOficinasSIR(oficinasSIR);
                        log.info("El organismo externo " + organismoExterno + " TIENE oficinas Sir: " + oficinasSIR.size());
                    } else {
                        oficios.setOficinasSIR(null);
                        log.info("El organismo externo " + organismoExterno + " no tiene oficinas Sir");
                    }

                }else {
                    oficios.setSir(false);
                    oficios.setOficinasSIR(null);
                    log.info("Nuestra entidad no esta en SIR, se creara un oficio de remision tradicional");
                }

                //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
                oficios.setPaginacion(oficiosRemisionByOrganismoExterno(pageNumber, resultsPerPage, organismoExterno.getCodigo(), any, idOficina, idLibro));
            }
        }

        return oficios;
    }


    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosRemisionByOrganismoInterno(Integer pageNumber,final Integer resultsPerPage, Long idOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select re from RegistroEntrada as re where " + anyWhere +
                " re.libro.id = :idLibro and re.oficina.id = :idOficina " +
                "and re.destino.id = :idOrganismo and re.estado = :valido ");

        q2 = em.createQuery(query.toString().replaceAll("Select re", "Select count(re.id)"));
        query.append(" order by re.fecha desc ");
        q = em.createQuery(query.toString());


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
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public Oficio isOficio(Long idRegistro, Set<Long> organismos) throws Exception{

        Oficio oficio = new Oficio();

        if(isOficioRemisionExterno(idRegistro)){ // Externo

            oficio.setOficioRemision(true);
            oficio.setInterno(false);

            List<OficinaTF> oficinasSIR = isOficioRemisionSir(idRegistro);

            if(oficinasSIR != null){
                oficio.setSir(true);
                oficio.setExterno(false);
            }else{
                oficio.setSir(false);
                oficio.setExterno(true);
            }

        }else{
            oficio.setExterno(false);
            oficio.setSir(false);

            Boolean interno = isOficioRemisionInterno(idRegistro, organismos);

            oficio.setOficioRemision(interno);
            oficio.setInterno(interno);
        }

        return oficio;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = "re.destino.id not in (:organismos)";
        }

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro  and " +
                "re.destino != null and " + organismosWhere);

        // Parámetros
        q.setParameter("idRegistro", idRegistro);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionExterno(Long idRegistro) throws Exception {

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);


        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSir(Long idRegistro) throws Exception {

        Query q;
        q = em.createQuery("Select re.destinoExternoCodigo from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);

        List<String> result = q.getResultList();

        if(result.size() > 0){

            String codigoDir3 = result.get(0);
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
            List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(codigoDir3);

            return oficinasSIR;
        }

        return null;
    }


    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosRemisionByOrganismoExterno(Integer pageNumber, final Integer resultsPerPage, String codigoOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        StringBuilder query = new StringBuilder("Select re from RegistroEntrada as re where " + anyWhere +
                " re.libro.id = :idLibro and re.oficina.id = :idOficina " +
                " and re.destino is null and re.destinoExternoCodigo = :codigoOrganismo and re.estado = :valido ");

        Query q;
        Query q2;

        q2 = em.createQuery(query.toString().replaceAll("Select re", "Select count(re.id)"));
        query.append(" order by re.fecha desc ");
        q = em.createQuery(query.toString());

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
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
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
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_INTERNO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_INTERNO);
        }

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
     *
     * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param organismoExternoCodigo
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExternoCodigo,
                                                     String organismoExternoDenominacion, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExternoCodigo);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);
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

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(oficio.getIdRegistro());
            Libro libro = libroEjb.findById(oficio.getIdLibro());

            RegistroEntrada nuevoRE = new RegistroEntrada();
            nuevoRE.setUsuario(usuario);
            nuevoRE.setDestino(organismoEjb.findByIdLigero(oficio.getIdOrganismoDestinatario()));
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

        oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemisionEjb.merge(oficioRemision);

        return registros;

    }


}
