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
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada re, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoDest, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat ";
        StringBuilder query = new StringBuilder(queryBase);

        // Numero registro
        if (StringUtils.isNotEmpty(re.getNumeroRegistroFormateado())) {
            where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + re.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (StringUtils.isNotEmpty(re.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto", "extracto", parametros, new String(re.getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8")));
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
        if (re.getEstado() != null && re.getEstado() > 0) {
            where.add(" registroEntrada.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", re.getEstado());
        }

        // Tipo documentación física
        if (re.getRegistroDetalle().getTipoDocumentacionFisica() != null && re.getRegistroDetalle().getTipoDocumentacionFisica() > 0) {
            where.add(" registroEntrada.registroDetalle.tipoDocumentacionFisica = :tipoDocumentacion ");
            parametros.put("tipoDocumentacion", re.getRegistroDetalle().getTipoDocumentacionFisica());
        }

        // Oficina Registro
        if (re.getOficina() != null && (re.getOficina().getId() != null && re.getOficina().getId() > 0)) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina", re.getOficina().getId());
        }

        // Intervalo fechas
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        if(re.getLibro().getId() !=  null && re.getLibro().getId() > 0){
            where.add(" registroEntrada.libro.id = :idLibro");
            parametros.put("idLibro", re.getLibro().getId());
        }

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

        //Presencial
        if(re.getRegistroDetalle().getPresencial() != null){
            where.add(" registroEntrada.registroDetalle.presencial = :presencial ");
            parametros.put("presencial", re.getRegistroDetalle().getPresencial());
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
    @SuppressWarnings(value = "unchecked")
    public Paginacion pendientesDistribuir(Integer pageNumber, Long idOficinaActiva) throws Exception{

        Query q;
        Query q2;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado and re.registroDetalle.tipoDocumentacionFisica = :tipoDoc order by re.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("tipoDoc", RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado and re.registroDetalle.tipoDocumentacionFisica = :tipoDoc");

        q2.setParameter("idOficinaActiva", idOficinaActiva);
        q2.setParameter("idEstado", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("tipoDoc", RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);


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
    public RegistroEntrada findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select re from RegistroEntrada as re where re.numeroRegistroFormateado = :numeroRegistroFormateado " +
                "and re.usuario.entidad.codigoDir3 = :codigoEntidad ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);
        q.setParameter("codigoEntidad", codigoEntidad);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateadoConAnexos(String codigoEntidad, String numeroRegistroFormateado) throws Exception, I18NException {


       RegistroEntrada registroEntrada = findByNumeroRegistroFormateado(codigoEntidad,numeroRegistroFormateado);
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


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByDocumento(Long idEntidad, String documento) throws Exception {

        Query q;

        q = em.createQuery("Select DISTINCT re from RegistroEntrada as re left outer join re.registroDetalle.interesados interessat " +
                "where (UPPER(interessat.documento) LIKE UPPER(:documento)) and re.usuario.entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("documento", documento.trim());

        return q.getResultList();
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



}
