package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.TimeUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaEJB")
@SecurityDomain("seycon")
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean
        implements RegistroEntradaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB(name = "LibroEJB")
    private LibroLocal libroEjb;

    @EJB(name = "ContadorEJB")
    private ContadorLocal contadorEjb;

    @EJB(name = "OficinaEJB")
    private OficinaLocal oficinaEjb;

    @EJB(name = "HistoricoRegistroEntradaEJB")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

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

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    private UsuarioEntidadLocal usuarioEntidadEjb;



    @SuppressWarnings("unchecked")
    @Override
    public RegistroBasico findByIdLigero(Long idRegistroEntrada) throws Exception{

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.estado " +
                "from RegistroEntrada as re where re.id = :idRegistroEntrada ");


        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        List<Object[]> result = q.getResultList();

        if(result.size() == 1){
            Object[] object = result.get(0);

            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long)  object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            registroBasico.setEstado((Long) object[5]);

            return registroBasico;
        }

        return null;
    }

    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroEntrada.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroEntrada.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
            registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
            registroEntrada.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libro, oficina));

            // Si no ha introducido ninguna fecha de Origen
            if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null) {
                registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }

            // Guardar RegistroEntrada
            registroEntrada = persist(registroEntrada);

            // Guardar el HistorioRegistroEntrada
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesados = interesadoEjb.guardarInteresados(interesados, registroEntrada.getRegistroDetalle());
                registroEntrada.getRegistroDetalle().setInteresados(interesados);
            }

            // TODO Controlar custodyID y si hay fallo borrar todos los Custody
            if (anexosFull != null && anexosFull.size() != 0) {
                final Long registroID = registroEntrada.getId();
                for (AnexoFull anexoFull : anexosFull) {

                    anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "entrada", true);

                }
            }
            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroEntrada,usuarioEntidad.getEntidad().getId());

            return registroEntrada;

        }catch (I18NException i18n){
            log.info("Error registrando la entrada");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la entrada");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la entrada");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoDest, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat ";
        StringBuilder query = new StringBuilder(queryBase);

        // Numero registro
        if (StringUtils.isNotEmpty(registroEntrada.getNumeroRegistroFormateado())) {
            where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + registroEntrada.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (StringUtils.isNotEmpty(registroEntrada.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto", "extracto", parametros, new String(registroEntrada.getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8")));
        }

        // Observaciones
        if (StringUtils.isNotEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (StringUtils.isNotEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroEntrada.usuario.usuario.identificador", "usuario", parametros, usuario));
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

        // Organismo destinatario
        if (StringUtils.isNotEmpty((organoDest))) {
            Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero(organoDest, idEntidad);
            if (organismo == null) {
                where.add(" registroEntrada.destinoExternoCodigo = :organoDest ");
            } else {
                where.add(" registroEntrada.destino.codigo = :organoDest ");
            }

            parametros.put("organoDest", organoDest);
        }

        // Estado registro
        if (registroEntrada.getEstado() != null && registroEntrada.getEstado() > 0) {
            where.add(" registroEntrada.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", registroEntrada.getEstado());
        }

        // Oficina Registro
        if (registroEntrada.getOficina().getId() != null && registroEntrada.getOficina().getId() > 0) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina", registroEntrada.getOficina().getId());
        }

        // Intervalo fechas
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroEntrada.libro.id = :idLibro");
        parametros.put("idLibro", registroEntrada.getLibro().getId());

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
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
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroEntrada.id) from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat "));
            query.append(" order by registroEntrada.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select DISTINCT count(registroEntrada.id) from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat "));
            query.append("order by registroEntrada.id desc");
            q = em.createQuery(query.toString());
        }

        Paginacion paginacion;

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
    @SuppressWarnings(value = "unchecked")
    public List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws Exception {

        Query q;

        String s = "re.registroDetalle.extracto ";

        if (idEstado.equals(RegwebConstantes.REGISTRO_RESERVA)) {
            s = "re.registroDetalle.reserva ";
        }

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, " + s +
                "from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("idEstado", idEstado);

        return getRegistroBasicoList(q.getResultList());

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;
        Query q2;


        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado");

        q2.setParameter("idOficinaActiva", idOficinaActiva);
        q2.setParameter("idEstado", idEstado);


        Paginacion paginacion;

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
    public Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroEntrada as re where re.libro in (:libros) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.libro in (:libros) " +
                "and re.estado = :idEstado");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    public void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), false);
    }

    @Override
    public void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación de estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, observacionesAnulacion, false);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado, String codigoLibro) throws Exception {


        String conLibroWhere="";
        if(codigoLibro != null){
            conLibroWhere = "and re.libro.codigo=:codigoLibro";
        }


        Query q = em.createQuery("Select re from RegistroEntrada as re where re.numeroRegistroFormateado = :numeroRegistroFormateado " +
                "and re.usuario.entidad.codigoDir3 = :codigoEntidad "+conLibroWhere);

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);
        q.setParameter("codigoEntidad", codigoEntidad);

        if(codigoLibro != null){
            q.setParameter("codigoLibro", codigoLibro);
        }

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateadoConAnexos(String codigoEntidad, String numeroRegistroFormateado, String codigoLibro) throws Exception, I18NException {


       RegistroEntrada registroEntrada = findByNumeroRegistroFormateado(codigoEntidad,numeroRegistroFormateado,codigoLibro);
       if(registroEntrada != null){
           return cargarAnexosFull(registroEntrada);
       }else{
           return null;
       }


    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroEntrada "
                + " from RegistroEntrada as registroEntrada"
                + " where registroEntrada.numeroRegistro = :numero "
                + " AND  YEAR(registroEntrada.fecha) = :anyo "
                + " AND  registroEntrada.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception {

        Query q = em.createQuery("Select registroEntrada.numeroRegistroFormateado "
                + " from RegistroEntrada as registroEntrada"
                + " where registroEntrada.registroDetalle.id = :idRegistroDetalle "
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
    public void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad,
                                      String observacionesAnulacion) throws Exception {

        // Estado anulado
        cambiarEstadoAnuladoHistorico(registroEntrada, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);

    }

    @Override
    public void activarRegistroEntrada(RegistroEntrada registroEntrada,
                                       UsuarioEntidad usuarioEntidad) throws Exception {

        // Actualizamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }

    @Override
    public void visarRegistroEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

    }

    @Override
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada,
                                        UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException {

        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOficioRemision(null);
        trazabilidad.setFecha(new Date());
        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_DISTRIBUCION);
        trazabilidad.setRegistroEntradaOrigen(registroEntrada);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(null);
        trazabilidadEjb.persist(trazabilidad);

        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_DISTRIBUIDO, usuarioEntidad);

    }




    @Override
    public RegistroEntrada generarJustificanteRegistroEntrada(RegistroEntrada registroEntrada,
                                                              UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException {
        // Justificante: Si no tiene generado, lo hacemos
        if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
             registroEntrada = cargarAnexosFull(registroEntrada);
            // Creamos el anexo del justificante y se lo añadimos al registro
            AnexoFull anexoFull = justificanteEjb.crearJustificante(usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
            registroEntrada.getRegistroDetalle().getAnexosFull().add(anexoFull);

        }

        return registroEntrada;

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getLibro(Long idRegistroEntrada) throws Exception {

        Query q;

        q = em.createQuery("Select registroEntrada.libro.id from RegistroEntrada as registroEntrada where registroEntrada.id = :idRegistroEntrada ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isDistribuir(Long idRegistro, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = " and re.destino.id in (:organismos) ";
        }

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.estado = :valido and re.destino != null " + organismosWhere );

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList().size() > 0;
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
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long) object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            if (StringUtils.isEmpty((String) object[5])) {
                registroBasico.setExtracto((String) object[6]);
            } else {
                registroBasico.setExtracto((String) object[5]);
            }
            registros.add(registroBasico);

        }

        return registros;
    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoCompleto(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        log.info("Total registros: " + result.size());

        for (Object[] object : result) {
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long) object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            if (StringUtils.isEmpty((String) object[5])) {
                registroBasico.setExtracto((String) object[6]);
            } else {
                registroBasico.setExtracto((String) object[5]);
            }


            registroBasico.setIdOficina((Long) object[7]);
            registroBasico.setOficina((String) object[8]);
            if (StringUtils.isEmpty((String) object[9])) {
                registroBasico.setDestinatario((String) object[10]);
            } else {
                registroBasico.setDestinatario((String) object[9]);
            }
            registroBasico.setEstado((Long) object[11]);
            //registroBasico.setAnexos((Long)object[12]);
            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> registros = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }

    @Override
    public Long getTotalByLibro(Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception {
        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.id = :idRegistroEntrada and re.estado = :idEstado ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public RegistroEntrada getConAnexosFullLigero(Long id) throws Exception, I18NException {

        RegistroEntrada re = findById(id);
        Long idEntidad = re.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexo.getId(),idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }

    @Override
    public RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroEntrada re = findById(id);

        return cargarAnexosFull(re);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.registroDetalle.identificadorIntercambio = :identificadorIntercambio ");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);

        return (RegistroEntrada) q.getResultList().get(0);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        RegistroEntrada rectificado = null;

        try {
            RegistroEntrada registroEntrada = getConAnexosFull(idRegistro);
            List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroEntrada);
            session.evict(registroEntrada.getRegistroDetalle());
            session.evict(registroEntrada.getRegistroDetalle().getInteresados());

            // Nuevas propiedades
            registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroEntrada.setFecha(new Date());

            // Set Id's a null
            registroEntrada.setId(null);
            registroEntrada.getRegistroDetalle().setId(null);
            registroEntrada.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroEntrada.getRegistroDetalle().setAnexos(null);

            registroEntrada.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroEntrada.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarEntrada(registroEntrada, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA);
            trazabilidad.setRegistroEntradaOrigen(getReference(idRegistro));
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setRegistroSir(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException e) {
            e.printStackTrace();
        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        return rectificado;
    }




    @Override
    public void enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad, int maxReintentos) throws Exception, I18NException, I18NValidationException {

        try {
            //Creamos un elemento nuevo de la cola de distribución
            Cola cola = new Cola();
            cola.setNumeroReintentos(0);
            cola.setIdObjeto(re.getId());
            cola.setDescripcionObjeto(re.getNumeroRegistroFormateado());
            cola.setTipo(RegwebConstantes.COLA_DISTRIBUCION);
            cola.setUsuarioEntidad(usuarioEntidad);
            cola.setDenominacionOficina(re.getOficina().getDenominacion());

            colaEjb.persist(cola);

            log.info("RegistroEntrada: " + re.getNumeroRegistroFormateado() + " enviado a la Cola de Distribución");
            cambiarEstadoHistorico(re,RegwebConstantes.REGISTRO_DISTRIBUYENDO, usuarioEntidad);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * Inicia la distribución de X elementos de la cola
     * @param entidadId
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public void iniciarDistribucionLista(Long entidadId, List<UsuarioEntidad> administradores, IDistribucionPlugin plugin) throws Exception, I18NException, I18NValidationException{

        //Obtenemos plugin
        int maxReintentos=0;
        //Obtenemos el numero máximo de reintentos de la configuración del plugin
        if(plugin!= null) {
            maxReintentos = plugin.configurarDistribucion().getMaxReintentos();
        }
        //obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
        List<Cola> elementosADistribuir = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_DISTRIBUCION,entidadId,RegwebConstantes.NUMELEMENTOSDISTRIBUIR,maxReintentos);
        Cola elementoADistribuir1 = new Cola();

        //Construimos los mensajes para guardar la información de la integración
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Distribución desde Cola";
        String hora =  "<b>"+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</b>&nbsp;&nbsp;&nbsp;";


        try {
        if (elementosADistribuir.size() > 0) {

            log.info(plugin.getClass());
            if (plugin != null) {
                for (Cola elementoADistribuir : elementosADistribuir) {
                    peticion= new StringBuilder();
                    try {
                        elementoADistribuir1 = elementoADistribuir;
                        //Obtenemos el registro de entrada que se debe distribuir
                        RegistroEntrada registroEntrada = getConAnexosFull(elementoADistribuir1.getIdObjeto());

                        //Montamos la petición de la integración
                        peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
                        peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                        peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
                        peticion.append("clase: ").append(plugin.getClass().getName()).append(System.getProperty("line.separator"));
                        /*if (distribucionPlugin instanceof DistribucionRipeaPlugin){
                            for (AnexoFull anexoFull : registroEntrada.getRegistroDetalle().getAnexosFull()) {
                                signatureServerEjb.checkDocument(anexoFull, entidadId, new Locale("ca"), false);
                            }
                        }*/

                        log.info("DISTRIBUYENDO REGISTRO  " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                        //Si no tiene justificante lo generamos
                        AnexoFull justificante = null;
                        if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                            justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                            registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);
                        }

                        //Gestionamos los anexos sir antes de distribuir
                        registroEntrada = gestionAnexosByAplicacionSIR(registroEntrada);
                        //Invocamos al plugin para distribuir el registro
                        Boolean distribuidoOK = plugin.enviarDestinatarios(registroEntrada, null, "", new Locale("ca"));

                        if (distribuidoOK) { //Si la distribución ha ido bien
                            //Eliminamos el elemento de la cola
                            colaEjb.remove(elementoADistribuir1);
                            log.info("distribucion OK REGISTRO " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                            //Tramitamos el registro de entrada
                            tramitarRegistroEntrada(registroEntrada, registroEntrada.getUsuario());
                            //Añadimos la integración correcta.
                            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION,descripcion,peticion.toString(),System.currentTimeMillis() - tiempo,entidadId,registroEntrada.getNumeroRegistroFormateado());

                        } else { //No ha ido bien, el plugin nos dice que no ha ido bien
                            log.info( "Distribucion Error REGISTRO "+ registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                            try {
                                //Actualizamos los diferentes datos del elemento a distribuir(incremento de intentos, envio de mails, etc)
                                colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",null,administradores,maxReintentos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        log.info("Primer Exception ");
                        try {
                            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        e.printStackTrace();
                    } catch (I18NException e) {
                        log.info("Primer I18NException ");
                        try {
                            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos );
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        e.printStackTrace();
                    } catch (I18NValidationException e) {
                        log.info("Primer I18NValidationException");
                        try {
                            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        e.printStackTrace();
                    }catch(Throwable t){
                        log.info("Primer Throwable");
                        try {
                            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",t ,administradores,maxReintentos);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        t.printStackTrace();
                    }
                }
            }
        }
    } catch (Exception e) {
        log.info("Error distribuyendo el registro Exception");
        try {
            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e,administradores,maxReintentos );
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        e.printStackTrace();
    }
}

    @Override
    public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException {

        log.info("------------------------------------------------------------");
        log.info("Distribuyendo el registro: " + re.getNumeroRegistroFormateado());
        log.info("");

        //Información a guardar de la integración
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Distribución Registro";
        String numRegFormat = "";

        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();
        respuestaDistribucion.setHayPlugin(false);
        respuestaDistribucion.setDestinatarios(null);
        respuestaDistribucion.setEnviado(false);
        respuestaDistribucion.setEnviadoCola(false);

        //Obtenemos plugin
        try {
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_DISTRIBUCION);

            //Si han especificado plug-in
            if (distribucionPlugin != null) {
                peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));
                peticion.append("numeroRegistro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                numRegFormat = re.getNumeroRegistroFormateado();

                respuestaDistribucion.setHayPlugin(true);

                //Obtenemos la configuración de la distribución
                ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
                respuestaDistribucion.setListadoDestinatariosModificable(configuracionDistribucion.isListadoDestinatariosModificable());
                //Obtenemos los anexos en función de la configuración establecida
                //re = obtenerAnexosDistribucion(re, configuracionDistribucion.getConfiguracionAnexos());
                //Se gestionan los anexos a distribuir, en función de la aplicación SIR que los ha enviado
                re= gestionAnexosByAplicacionSIR(re);

                if (configuracionDistribucion.isListadoDestinatariosModificable()) {// Si es modificable, mostraremos pop-up
                    respuestaDistribucion.setDestinatarios(distribucionPlugin.distribuir(re)); // isListado = true , puede escoger a quien lo distribuye de la listas propuestas.
                } else { // Si no es modificable, obtendra los destinatarios del propio registro y nos saltamos una llamada al plugin

                    if(configuracionDistribucion.isEnvioCola()){ //Si esta configurado para enviarlo a la cola
                        enviarAColaDistribucion(re,usuarioEntidad, configuracionDistribucion.getMaxReintentos());
                        respuestaDistribucion.setEnviadoCola(true);
                    }else {
                        //Generamos Justificante
                        AnexoFull justificante = null;
                        if(!re.getRegistroDetalle().getTieneJustificante()) {
                            justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(),Configuracio.getDefaultLanguage());
                            re.getRegistroDetalle().getAnexosFull().add(justificante);
                        }

                        //Distribuimos directamente
                        Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);
                        respuestaDistribucion.setEnviado(distribucionPlugin.enviarDestinatarios(re, null, "", locale));

                        // Si ya ha sido enviado, lo marcamos como tramitado.
                        if(respuestaDistribucion.getEnviado()){
                            //En tramitar entrada creamos la trazabilidad de distribución y con esa fecha trabajamos para obtener los anexos a purgar
                            tramitarRegistroEntrada(re,usuarioEntidad);

                            // Integración
                            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(),numRegFormat);
                            log.info("");
                            log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
                            log.info("------------------------------------------------------------");
                        }

                    }

                }


            }else{ //No hay plugin, generamos justificante y marcamos el Registro como Tramitado
                //Validamos las firmas de los anexos
                // TODO (No se si hay que validar, porque aquí no distribuimos a ningun lado
               /* if(PropiedadGlobalUtil.validarFirmas()) {
                    for (AnexoFull anexoFull : re.getRegistroDetalle().getAnexosFull()) {
                        signatureServerEjb.checkDocument(anexoFull, usuarioEntidad.getEntidad().getId(), new Locale("ca"), false);
                    }
                }*/

                AnexoFull justificante = null;
                if(!re.getRegistroDetalle().getTieneJustificante()) {
                    justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                    re.getRegistroDetalle().getAnexosFull().add(justificante);
                }
                //En tramitar entrada creamos la trazabilidad de distribución y con esa fecha trabajamos para obtener los anexos a purgar
                tramitarRegistroEntrada(re,usuarioEntidad);
                //Integración
                integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);

                log.info("");
                log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
                log.info("------------------------------------------------------------");
            }

        } catch (I18NException i18ne) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18ne, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw i18ne;
        } catch (Exception e) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), e, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        } catch (I18NValidationException i18vn) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18vn, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw i18vn;
        }

        return respuestaDistribucion;
    }

    @Override
    public Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper,
        Long entidadId, String idioma) throws Exception, I18NException, I18NValidationException {

        // Información de la integración
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Distribución Registro Modificable";
        Boolean distribucionOk = false; //Estado de la distribución

        //Obtenemos plugin
        try {
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_DISTRIBUCION);
            if (distribucionPlugin != null) {
                ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
                Locale locale = new Locale(idioma);

                //Generamos el justificante porque antes no lo hemos hecho
                AnexoFull justificante = null;
                if(!re.getRegistroDetalle().getTieneJustificante()) {
                    justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                    re.getRegistroDetalle().getAnexosFull().add(justificante);
                }
                re= gestionAnexosByAplicacionSIR(re);

                distribucionOk = distribucionPlugin.enviarDestinatarios(re, wrapper.getDestinatarios(), wrapper.getObservaciones(), locale);
                //Integración
                if(distribucionOk){
                    //Montamos la petición de la integración
                    peticion.append("registro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                    peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));
                    //Tramitamos el registro de entrada
                    tramitarRegistroEntrada(re, re.getUsuario());
                    integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, entidadId, re.getNumeroRegistroFormateado());
                    log.info("");
                    log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
                    log.info("------------------------------------------------------------");
                }
            }
            return distribucionOk;
        } catch (I18NException i18ne) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18ne, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw i18ne;
        } catch (Exception e) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
            } catch (Exception ex) {
                e.printStackTrace();
            }
            throw e;
        } catch (I18NValidationException i18vn) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18vn, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw i18vn;
        }

    }

    @Override
    public void distribuirRegistrosEnCola(Long entidadId) throws Exception, I18NException, I18NValidationException{
        log.info("Entramos en distribuir registros en Cola " );
        //Obtenemos todos los administradores de la entidad
        List<UsuarioEntidad> administradores = usuarioEntidadEjb.findAdministradoresByEntidad(entidadId);
        //Obtenermos plugin distribución
        IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_DISTRIBUCION);
        //Iniciamos la distribución de la lista si existe el plugin
        if(distribucionPlugin != null) {
            log.info("Iniciamos la distribucion de la cola");
            iniciarDistribucionLista(entidadId, administradores,distribucionPlugin);
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber,Long idOficina) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :rechazado or re.estado = :reenviado order by re.fecha desc");

        q.setParameter("idOficina", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :rechazado or re.estado = :reenviado");

        q2.setParameter("idOficina", idOficina);
        q2.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q2.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);


        Paginacion paginacion;

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
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getSirRechazadosReenviados(Long idOficina, Integer total) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and (re.estado = :rechazado or re.estado = :reenviado) order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getSirRechazadosReenviadosCount(Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and (re.estado = :rechazado or re.estado = :reenviado)");

        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        return (Long) q.getSingleResult();
    }

    @Override
    public void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set destino = :idOrganismoSustituto where destino = :idOrganismoExtinguido and estado = :valido");
        q.setParameter("idOrganismoSustituto", idOrganismoSustituto);
        q.setParameter("idOrganismoExtinguido", idOrganismoExtinguido);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        q.executeUpdate();

    }

    /**
     * Carga los Anexos Completos al RegistroEntrada pasado por parámetro
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada) throws Exception, I18NException {
        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroEntrada;
    }

    /**
     * Método que prepara el registro de entrada para distribuirlo.
     * La variable confAnexos indica que datos se envian en el segmento de anexo del registro de entrada.
     * <p/>
     * 1 = custodiaId + metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
     * 2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
     * 3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
     *
     * @param original
     * @param confAnexos
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada obtenerAnexosDistribucion(RegistroEntrada original, int confAnexos) throws Exception, I18NException, I18NValidationException {


        // Miramos si debemos generar el justificante
        AnexoFull justificante = null;
        if(!original.getRegistroDetalle().getTieneJustificante()) {
            justificante = justificanteEjb.crearJustificante(original.getUsuario(), original, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
        }

        switch (confAnexos) {
            case 1: {//1.  Fitxer + firma + metadades + custodiaId
                cargarAnexosFull(original);
                if(justificante!= null){
                    original.getRegistroDetalle().getAnexosFull().add(justificante);
                }
                break;
            }
            case 2: {//2. custodiaId

                // Montamos una nueva lista de anexos solo con el custodiaID, sin metadatos ni nada
                List<Anexo> anexos = original.getRegistroDetalle().getAnexos();
                List<Anexo> nuevosAnexos = new ArrayList<Anexo>();
                for (Anexo anexo : anexos) {
                    Anexo nuevoAnexo = new Anexo();
                    nuevoAnexo.setId(anexo.getId());
                    nuevoAnexo.setJustificante(anexo.isJustificante());
                    nuevoAnexo.setCustodiaID(anexo.getCustodiaID());
                    nuevosAnexos.add(nuevoAnexo);
                }
                //Añadimos el justificante si lo acabamos de crear
                if(justificante != null){
                    Anexo anexoJust = new Anexo();
                    anexoJust.setId(justificante.getAnexo().getId());
                    anexoJust.setJustificante(justificante.getAnexo().isJustificante());
                    anexoJust.setCustodiaID(justificante.getAnexo().getCustodiaID());
                    nuevosAnexos.add(justificante.getAnexo());
                }
                original.getRegistroDetalle().setAnexos(nuevosAnexos);
                break;
            }
            case 3: {// 3. custodiaId + metadades (no se hace nada, es el caso por defecto)

                //añadimos el justificante si lo acabamos de crear
                if(justificante != null){
                    original.getRegistroDetalle().getAnexos().add(justificante.getAnexo());
                }
            }

        }
        return original;
    }

    public void postProcesoActualizarRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroEntrada(re);
        }

    }

    public void postProcesoNuevoRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroEntrada(re);
        }
    }


    /**
     *  Este método elimina los anexos que no se pueden enviar a Arxiu porque no estan soportados.
     *  Son ficheros xml de los cuales no puede hacer el upgrade de la firma y se ha decidido que no se distribuyan.
     *
     * @param original
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    private RegistroEntrada gestionAnexosByAplicacionSIR(RegistroEntrada original) throws Exception, I18NException, I18NValidationException {

        List<AnexoFull> anexosFullADistribuir = new ArrayList<AnexoFull>();
        //Obtenemos los anexos del registro para tratarlos
        List<AnexoFull> anexosFull =original.getRegistroDetalle().getAnexosFull();
        //Lista de anexos para el procesamiento intermedio
        List<AnexoFull> anexosFullIntermedio = new ArrayList<AnexoFull>();


        gestionarByAplicacionByNombreFichero( RegwebConstantes.FICHERO_REGISTROELECTRONICO, anexosFull, anexosFullIntermedio);
        gestionarByAplicacionByNombreFichero(RegwebConstantes.FICHERO_DEFENSORPUEBLO, anexosFullIntermedio, anexosFullADistribuir);

        original.getRegistroDetalle().setAnexosFull(anexosFullADistribuir);

        return original;
    }


    /**
     * Este método lo que hace es eliminar de la lista de anexos a distribuir, aquellos que el Arxiu no soporta que son
     * de formato xml y que no puede hacer el upgrade de la firma.
     * Lo que hace es comparar el nombre del Fichero y la aplicación de la que procede, para determinar si lo elimina o no.
     * @param nombreFichero
     * @param anexosFull
     * @param anexosFullADistribuir
     * @return
     * @throws Exception
     */
    private List<AnexoFull> gestionarByAplicacionByNombreFichero(String nombreFichero, List<AnexoFull> anexosFull, List<AnexoFull> anexosFullADistribuir) throws Exception {
        //para cada uno de los anexos miramos si es uno de los conflictivos.
        //Estan tipificados en Regweb3Constantes.
        for (AnexoFull anexoFull: anexosFull) {
            String nombreFicheroTemp = "";
            //Si es un documento sin firma, el nombre està en DocumentCustody
            if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA){
                nombreFicheroTemp = anexoFull.getDocumentoCustody().getName();
            }else { // Si tiene firma, el nombre está en SignatureCustody.
                nombreFicheroTemp = anexoFull.getSignatureCustody().getName();
            }

            //Si el nombre del fichero es distinto al que nos han indicado, se puede distribuir
            if(!nombreFichero.equals(nombreFicheroTemp)){
                anexosFullADistribuir.add(anexoFull);
            }
        }
        return anexosFullADistribuir;
    }



}
