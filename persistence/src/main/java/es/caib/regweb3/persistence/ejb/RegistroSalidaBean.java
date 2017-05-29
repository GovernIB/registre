package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaEJB")
@SecurityDomain("seycon")
public class RegistroSalidaBean extends RegistroSalidaCambiarEstadoBean
        implements RegistroSalidaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/ContadorEJB/local")
    private ContadorLocal contadorEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    private InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;



    @Override
    public synchronized RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                                       UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
            registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
            registroSalida.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libro, oficina));

            // Si no ha introducido ninguna fecha de Origen
            if (registroSalida.getRegistroDetalle().getFechaOrigen() == null) {
                registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }

            // Guardamos el RegistroSalida
            registroSalida = persist(registroSalida);

            //Guardamos el HistorioRegistroSalida
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesadoEjb.guardarInteresados(interesados, registroSalida.getRegistroDetalle());
                registroSalida.getRegistroDetalle().setInteresados(interesados);
            }

            if (anexos != null && anexos.size() != 0) {
                final Long registroID = registroSalida.getId();
                for (AnexoFull anexoFull : anexos) {
                    anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "salida");
                }
            }

            postProcesoNuevoRegistro(registroSalida,usuarioEntidad.getEntidad().getId());

            return registroSalida;

        }catch (I18NException i18n){
            log.info("Error registrando la salida");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la salida");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la salida");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoOrigen, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroSalida from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat ";
        StringBuffer query = new StringBuffer(queryBase);

        // Numero registro
        if (StringUtils.isNotEmpty(registroSalida.getNumeroRegistroFormateado())) {
            where.add(" registroSalida.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + registroSalida.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (StringUtils.isNotEmpty(registroSalida.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.extracto", "extracto", parametros, new String(registroSalida.getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8")));
        }

        // Observaciones
        if (StringUtils.isNotEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (StringUtils.isNotEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroSalida.usuario.usuario.identificador", "usuario", parametros, usuario));
        }

        // Nombre interesado
        if (StringUtils.isNotEmpty(interesadoNom)) {
            where.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros, interesadoNom) +
                    ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros, interesadoNom) +
                    "))");
        }

        // Primer apellido interesado
        if (StringUtils.isNotEmpty(interesadoLli1)) {
            where.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros, interesadoLli1));
        }

        // Segundo apellido interesado
        if (StringUtils.isNotEmpty(interesadoLli2)) {
            where.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros, interesadoLli2));
        }

        // Documento interesado
        if (StringUtils.isNotEmpty(interesadoDoc)) {
            where.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
            parametros.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
        }

        // Organismo origen
        if (StringUtils.isNotEmpty((organoOrigen))) {
            Organismo organismo = organismoEjb.findByCodigoEntidad(organoOrigen, idEntidad);
            if (organismo == null) {
                where.add(" registroSalida.origenExternoCodigo = :organoOrigen ");
            } else {
                where.add(" registroSalida.origen.codigo = :organoOrigen ");
            }

            parametros.put("organoOrigen", organoOrigen);
        }

        // Estado registro
        if (registroSalida.getEstado() != null && registroSalida.getEstado() > 0) {
            where.add(" registroSalida.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", registroSalida.getEstado());
        }

        // Oficina Registro
        if (registroSalida.getOficina().getId() != null && registroSalida.getOficina().getId() > 0) {
            where.add(" registroSalida.oficina.id = :idOficina ");
            parametros.put("idOficina", registroSalida.getOficina().getId());
        }

        // Intervalo fechas
        where.add(" (registroSalida.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSalida.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroSalida.libro.id = :idLibro");
        parametros.put("idLibro", registroSalida.getLibro().getId());

        // Buscamos registros de sañida con anexos
        if (anexos) {
            where.add(" registroSalida.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

        // Añadimos los parámetros a la query
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroSalida.id) from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat "));
            query.append(" order by registroSalida.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroSalida.id) from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat "));
            query.append("order by registroSalida.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroSalida "
                + " from RegistroSalida as registroSalida"
                + " where registroSalida.numeroRegistro = :numero "
                + " AND  YEAR(registroSalida.fecha) = :anyo "
                + " AND  registroSalida.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    public RegistroSalida findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.numeroRegistroFormateado = :numeroRegistroFormateado ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception {

        Query q = em.createQuery("Select registroSalida.numeroRegistroFormateado "
                + " from RegistroSalida as registroSalida"
                + " where registroSalida.registroDetalle.id = :idRegistroDetalle "
        );

        q.setParameter("idRegistroDetalle", idRegistroDetalle);


        List<String> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoTrazabilidad(registroSalida, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad);
    }

    @Override
    public void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoTrazabilidad(registroSalida, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }


    @Override
    public void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoTrazabilidad(registroSalida, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

    }


    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result) {
            RegistroBasico registroBasico = new RegistroBasico((Long) object[0], (String) object[1], (Date) object[2], (String) object[3], (String) object[4], (String) object[5]);

            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<Object> registros = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }

    @Override
    public Long getLibro(Long idRegistroSalida) throws Exception {

        Query q;

        q = em.createQuery("Select registroSalida.libro.id from RegistroSalida as registroSalida where registroSalida.id = :idRegistroSalida ");

        q.setParameter("idRegistroSalida", idRegistroSalida);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroSalida as re where re.libro in (:libros) " +
                "and re.estado = :idEstado");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RegistroSalida> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroSalida as re where re.libro in (:libros) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();

    }

    @Override
    public Long getTotalByLibro(Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public void cambiarEstadoTrazabilidad(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {
        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroSalida.getId());
        q.executeUpdate();

        // Creamos el HistoricoRegistroSalida para la modificación de estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.estado" ), false);
    }


    @Override
    public RegistroSalida getConAnexosFullLigero(Long id) throws Exception, I18NException {

        RegistroSalida re = findById(id);
        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId());
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de salida.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }

    @Override
    public RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroSalida re = findById(id);
        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullCompleto(anexo.getId());
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de salida.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSalida rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida rectificado = null;

        try {
            RegistroSalida registroSalida = getConAnexosFull(idRegistro);
            List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroSalida);
            session.evict(registroSalida.getRegistroDetalle());

            // Nuevas propiedades
            registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroSalida.setFecha(new Date());

            // Set Id's a null
            registroSalida.setId(null);
            registroSalida.getRegistroDetalle().setId(null);

            for (Interesado interesado : interesados) {
                interesado.setId(null);
            }
            registroSalida.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroSalida.getRegistroDetalle().setAnexos(null);

            registroSalida.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroSalida.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarSalida(registroSalida, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA);
            /*trazabilidad.setRegistroEntradaOrigen(null);
            trazabilidad.setRegistroEntradaDestino(null);
            trazabilidad.setRegistroSir(null);
            trazabilidad.setOficioRemision(null);*/
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setRegistroSalidaRectificado(getReference(idRegistro));
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException e) {
            e.printStackTrace();
        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        return rectificado;
    }

    public void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroSalida(rs);
        }

    }

    public void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroSalida(rs);
        }
    }
}
