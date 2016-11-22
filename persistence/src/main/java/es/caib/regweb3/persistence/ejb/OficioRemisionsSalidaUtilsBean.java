package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
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
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionSalidaUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionsSalidaUtilsBean implements OficioRemisionSalidaUtilsLocal {

    public final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

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
    public Long oficiosSalidaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<String> organismos) throws Exception {

        Query q;
        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.oficina.id = :idOficina and rs.libro in (:libros) and " +
                "rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and codigoDir3 not in (:organismos)) and " +
                " rs.id not in (select tra.registroSalida.id from Trazabilidad as tra where tra.oficioRemision.estado != :anulado)");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);
        q.setParameter("organismos", organismos);


        return (Long) q.getSingleResult();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organismosSalidaPendientesRemision(Long idOficina, List<Libro> libros, Set<String> organismos) throws Exception {

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q1;
        q1 = em.createQuery("Select rs.registroDetalle.id from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.oficina.id = :idOficina and rs.libro in (:libros) and " +
                "rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and codigoDir3 not in (:organismos)) and " +
                " rs.id not in (select tra.registroSalida.id from Trazabilidad as tra where tra.oficioRemision.tipoOficioRemision = :tipoOficioRemision and tra.oficioRemision.estado != :anulado)");

        // Parámetros
        q1.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q1.setParameter("idOficina", idOficina);
        q1.setParameter("libros", libros);
        q1.setParameter("tipoOficioRemision", RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        q1.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q1.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);
        q1.setParameter("organismos", organismos);

        List<Object> registros = q1.getResultList();

        // Obtenemos los Interesados  de tipo administración destinatarios de los Registros de Salida
        List<Organismo> organismosDestino =  new ArrayList<Organismo>();
        if(registros.size() > 0){

            // Obtenemos los destinos de los Interesados de los Registros Salida anteriores
            Query q2;
            q2 = em.createQuery("Select distinct(i.codigoDir3), i.razonSocial from Interesado as i where i.tipo = :administracion and " +
                    "i.registroDetalle.id in (:registros)");

            // Parámetros
            q2.setParameter("registros", registros);
            q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);

            List<Object[]> destinos = q2.getResultList();

            for (Object[] object : destinos) {
                organismosDestino.add(new Organismo(null, (String) object[0], (String) object[1]));
            }
        }

        return  organismosDestino;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Long idOficina, Long idLibro, String codigoOrganismo, Entidad entidadActiva) throws Exception {

        OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();
        Organismo organismo = organismoEjb.findByCodigoEntidad(codigoOrganismo, entidadActiva.getId());

        if(organismo != null){ // Destinatario organismo interno
            oficios.setOrganismo(organismo);
            oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

        }else{ // Destinatario organismo externo

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
                //Boolean isSir = (Boolean) em.createQuery("select e.sir from Entidad as e where e.id = :id").setParameter("id", entidadActiva.getId()).getSingleResult();
                if (entidadActiva.getSir()) {
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

                }else{
                    oficios.setSir(false);
                    oficios.setOficinasSIR(null);
                    log.info("Nuestra entidad no está en SIR, se creará un oficio de remision tradicional");
                }


            }else{
                log.info("Organismo externo extinguido");
            }
        }

        //Buscamos los Registros de Salida, pendientes de tramitar mediante un Oficio de Remision
        oficios.setPaginacion(oficiosSalidaByOrganismo(pageNumber, codigoOrganismo, any, idOficina, idLibro));

        return oficios;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosSalidaByOrganismo(Integer pageNumber, String codigoOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(rs.fecha) = :any and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select rs from RegistroSalida as rs where " + anyWhere +
                "rs.libro.id = :idLibro and rs.oficina.id = :idOficina and rs.estado = :valido and " +
                "rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and i.codigoDir3 = :codigoOrganismo) and " +
                "rs.id not in (select tra.registroSalida.id from Trazabilidad as tra where tra.oficioRemision.tipoOficioRemision = :tipoOficioRemision and tra.oficioRemision.estado != :anulado)");

        q2 = em.createQuery(query.toString().replaceAll("Select rs", "Select count(rs.id)"));
        query.append(" order by rs.fecha desc ");
        q = em.createQuery(query.toString());


        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("idLibro", idLibro);
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("tipoOficioRemision", RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("codigoOrganismo", codigoOrganismo);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        q2.setParameter("idLibro", idLibro);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("tipoOficioRemision", RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q2.setParameter("codigoOrganismo", codigoOrganismo);
        q2.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

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

    /**
     * Crea un OficioRemision con todos los ResgistroSalida seleccionados
     * Crea la trazabilidad para los RegistroSalida
     *
     * @param registrosSalida Listado de ResgistroSalida que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     */
    @Override
    public OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosSalida(registrosSalida);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                    RegwebConstantes.REGISTRO_OFICIO_INTERNO, RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        }

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroSalida seleccionados
     *
     * @param registrosSalida Listado de RegistrosSalida que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param organismoExterno
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
                                                     String organismoExternoDenominacion, Long idLibro)
            throws Exception, I18NException, I18NValidationException {


        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setIdentificadorIntercambioSir(null);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosSalida(registrosSalida);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExterno);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                    RegwebConstantes.REGISTRO_OFICIO_EXTERNO, RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        }

        return oficioRemision;

    }

    @Override
    public OficioRemision crearOficioRemisionSir(RegistroSalida registroSalida, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno, String organismoExternoDenominacion, Long idLibro, String identificadorIntercambio) throws Exception, I18NException, I18NValidationException {


        List<RegistroSalida> registros = new ArrayList<RegistroSalida>();
        registros.add(registroSalida);

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setIdentificadorIntercambioSir(identificadorIntercambio);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setRegistrosSalida(registros);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExterno);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                    RegwebConstantes.REGISTRO_OFICIO_EXTERNO, RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
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

        // Recorremos los RegistroSalida del Oficio y Libro de registro seleccionado
        for (int i = 0; i < oficios.size(); i++) {

            OficioPendienteLlegada oficio = oficios.get(i);

            RegistroSalida registroSalida = registroSalidaEjb.findById(oficio.getIdRegistro());
            Libro libro = libroEjb.findById(oficio.getIdLibro());

            RegistroEntrada nuevoRE = new RegistroEntrada();
            nuevoRE.setUsuario(usuario);
            nuevoRE.setDestino(new Organismo(oficio.getIdOrganismoDestinatario()));
            nuevoRE.setOficina(oficinaActiva);
            nuevoRE.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            nuevoRE.setLibro(libro);
            nuevoRE.setRegistroDetalle(registroSalida.getRegistroDetalle());

            synchronized (this) {
                nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE, usuario, null);
            }

            registros.add(nuevoRE);

            // ACTUALIZAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroSalida(oficioRemision.getId(), registroSalida.getId());
            trazabilidad.setRegistroEntradaDestino(nuevoRE);

            trazabilidadEjb.merge(trazabilidad);

        }

        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemisionEjb.merge(oficioRemision);

        return registros;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemision(Long idRegistro, Set<String> organismos) throws Exception {



        Query q;

        q = em.createQuery("Select rs.id from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.id = :idRegistro and " +
                "rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and codigoDir3 not in (:organismos)) and " +
                " rs.id not in (select tra.registroSalida.id from Trazabilidad as tra where tra.oficioRemision.tipoOficioRemision = :tipoOficioRemision and tra.oficioRemision.estado != :anulado)");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("organismos", organismos);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("tipoOficioRemision", RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        q.setParameter("anulado", RegwebConstantes.OFICIO_REMISION_ANULADO);

        return q.getResultList().size() > 0;
    }

}
