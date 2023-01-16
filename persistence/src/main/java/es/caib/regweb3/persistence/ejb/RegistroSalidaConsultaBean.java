package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static es.caib.regweb3.persistence.ejb.BaseEjbJPA.RESULTADOS_PAGINACION;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 15/05/19
 */

@Stateless(name = "RegistroSalidaConsultaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class RegistroSalidaConsultaBean implements RegistroSalidaConsultaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private AnexoLocal anexoEjb;


    @Override
    @SuppressWarnings("unchecked")
    public RegistroBasico findByIdLigero(Long idRegistroSalida) throws I18NException{
        Query q;

        q = em.createQuery("Select rs.id, rs.numeroRegistroFormateado, rs.fecha, rs.libro.nombre, rs.usuario.usuario.identificador, rs.estado " +
                "from RegistroSalida as rs where rs.id = :idRegistroSalida ");


        q.setParameter("idRegistroSalida", idRegistroSalida);
        q.setHint("org.hibernate.readOnly", true);

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
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber,List<Long> organismos, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String observaciones, Long idUsuario, Long idEntidad) throws I18NException {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();
        boolean busquedaInteresados = busquedaInteresados(interesadoNom, interesadoLli1, interesadoLli2, interesadoDoc);

        StringBuilder queryBase = new StringBuilder("Select DISTINCT rs.id, rs.numeroRegistro, rs.numeroRegistroFormateado, rs.fecha, rs.oficina, rs.origen, rs.origenExternoDenominacion, rs.estado, rs.usuario, " +
                "rs.registroDetalle.extracto, rs.registroDetalle.tipoDocumentacionFisica, rs.registroDetalle.decodificacionTipoAnotacion, rs.registroDetalle.presencial from RegistroSalida as rs LEFT JOIN rs.origen origen  ");
        // Si la búsqueda incluye referencias al interesado, hacemos la left outer join
        if(busquedaInteresados){
            queryBase.append("left outer join rs.registroDetalle.interesados interessat ");
        }

        StringBuilder query = new StringBuilder(queryBase);

        // Entidad
        where.add(" rs.entidad.id =:idEntidad  ");
        parametros.put("idEntidad", idEntidad);

        // Organismo
        if(organismos.size() == 1){
            where.add(" rs.oficina.organismoResponsable.id = :idOrganismo ");
            parametros.put("idOrganismo", organismos.get(0));
        }else{
            where.add(" rs.oficina.organismoResponsable.id in (:organismos) ");
            parametros.put("organismos", organismos);
        }

        // Oficina Registro
        if (registroSalida.getOficina() != null && (registroSalida.getOficina().getId() != null && registroSalida.getOficina().getId() > 0)) {
            where.add(" rs.oficina.id = :idOficina ");
            parametros.put("idOficina", registroSalida.getOficina().getId());
        }

        // Estado registro
        if (registroSalida.getEstado() != null && registroSalida.getEstado() > 0) {
            where.add(" rs.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", registroSalida.getEstado());
        }

        // Numero registro
        if (StringUtils.isNotEmpty(registroSalida.getNumeroRegistroFormateado())) {
            where.add(" rs.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + registroSalida.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (StringUtils.isNotEmpty(registroSalida.getRegistroDetalle().getExtracto())) {
            try{
                String extracto = new String(registroSalida.getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8") ;
                where.add(DataBaseUtils.like("rs.registroDetalle.extracto", "extracto", parametros, extracto));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        // Observaciones
        if (StringUtils.isNotEmpty(observaciones)) {
            where.add(DataBaseUtils.like("rs.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (idUsuario != null) {
            where.add(" rs.usuario.id = :idUsuario ");
            parametros.put("idUsuario", idUsuario);
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

        // Tipo documentación física
        if (registroSalida.getRegistroDetalle().getTipoDocumentacionFisica() != null && registroSalida.getRegistroDetalle().getTipoDocumentacionFisica() > 0) {
            where.add(" rs.registroDetalle.tipoDocumentacionFisica = :tipoDocumentacion ");
            parametros.put("tipoDocumentacion", registroSalida.getRegistroDetalle().getTipoDocumentacionFisica());
        }

        // Intervalo fechas
        where.add(" (rs.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" rs.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        //Presencial
        if(registroSalida.getRegistroDetalle().getPresencial() != null){
            where.add(" rs.registroDetalle.presencial = :presencial ");
            parametros.put("presencial", registroSalida.getRegistroDetalle().getPresencial());
        }

        // Añadimos los parámetros a la query
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
        StringBuilder queryCount = new StringBuilder("Select count(DISTINCT rs.id) from RegistroSalida as rs ");
        if(busquedaInteresados){
            queryCount.append("left outer join rs.registroDetalle.interesados interessat ");
        }
        q2 = em.createQuery(query.toString().replaceAll(queryBase.toString(), queryCount.toString()));

        // añadimos el order by
        query.append(" order by rs.id desc");
        q = em.createQuery(query.toString());

        // Mapeamos los parámetros
        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
            q2.setParameter(param.getKey(), param.getValue());
        }

        // Ejecutamos las queries
        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> results = q.getResultList();
        List<RegistroSalida> registros = new ArrayList<>();

        for (Object[] result : results) {
            RegistroSalida registro =  new RegistroSalida();
            registro.setId((Long) result[0]);
            registro.setNumeroRegistro((Integer) result[1]);
            registro.setNumeroRegistroFormateado((String) result[2]);
            registro.setFecha((Date) result[3]);
            registro.setOficina((Oficina)result[4]);
            registro.setOrigen((Organismo) result[5]);
            registro.setOrigenExternoDenominacion((String) result[6]);
            registro.setEstado((Long) result[7]);
            registro.setUsuario((UsuarioEntidad) result[8]);
            registro.getRegistroDetalle().setExtracto((String) result[9]);
            registro.getRegistroDetalle().setTipoDocumentacionFisica((Long) result[10]);
            registro.getRegistroDetalle().setDecodificacionTipoAnotacion((String) result[11]);
            registro.getRegistroDetalle().setPresencial((Boolean) result[12]);

            registros.add(registro);
        }

        paginacion.setListado(registros);

        return paginacion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws I18NException {

        Query q = em.createQuery("Select registroSalida "
                + " from RegistroSalida as registroSalida"
                + " where registroSalida.numeroRegistro = :numero "
                + " AND  YEAR(registroSalida.fecha) = :anyo "
                + " AND  registroSalida.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegistroSalida findByNumeroRegistroFormateado(Long idEntidad, String numeroRegistroFormateado) throws I18NException {


        Query q = em.createQuery("Select rs from RegistroSalida as rs where rs.numeroRegistroFormateado = :numeroRegistroFormateado " +
                "and rs.entidad.id = :idEntidad ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    public RegistroSalida findByNumeroRegistroFormateadoCompleto(Long idEntidad, String numeroRegistroFormateado) throws I18NException {

        RegistroSalida registroSalida = findByNumeroRegistroFormateado(idEntidad,numeroRegistroFormateado);

        if(registroSalida!= null){
            Hibernate.initialize(registroSalida.getRegistroDetalle().getAnexos());
            Hibernate.initialize(registroSalida.getRegistroDetalle().getInteresados());
            return cargarAnexosFull(registroSalida);
        }else{
            return null;
        }
    }



    @Override
    @SuppressWarnings("unchecked")
    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws I18NException {

        Query q = em.createQuery("Select registroSalida.numeroRegistroFormateado "
                + " from RegistroSalida as registroSalida"
                + " where registroSalida.registroDetalle.id = :idRegistroDetalle "
        );

        q.setParameter("idRegistroDetalle", idRegistroDetalle);
        q.setHint("org.hibernate.readOnly", true);

        List<String> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public Long getLibro(Long idRegistroSalida) throws I18NException {

        Query q;

        q = em.createQuery("Select registroSalida.libro.id from RegistroSalida as registroSalida where registroSalida.id = :idRegistroSalida ");

        q.setParameter("idRegistroSalida", idRegistroSalida);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo getOrganismo(Long idRegistroSalida) throws I18NException {

        Query q;

        q = em.createQuery("Select rs.oficina.organismoResponsable.id, rs.oficina.organismoResponsable.codigo, rs.oficina.organismoResponsable.denominacion from RegistroSalida as rs where rs.id = :idRegistroSalida ");

        q.setParameter("idRegistroSalida", idRegistroSalida);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> organismos = q.getResultList();

        if (organismos.size() > 0) {
            return new Organismo((Long) organismos.get(0)[0], (String) organismos.get(0)[1], (String) organismos.get(0)[2]);
        } else {
            return null;
        }
    }

    @Override
    public Long getByLibrosEstadoCount(List<Organismo> organismos, Long idEstado) throws I18NException {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.oficina.organismoResponsable in (:organismos) " +
                "and rs.estado = :idEstado");

        q.setParameter("organismos", organismos);
        q.setParameter("idEstado", idEstado);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RegistroSalida> getByLibrosEstado(int inicio, List<Organismo> organismos, Long idEstado) throws I18NException {

        Query q;

        q = em.createQuery("Select rs from RegistroSalida as rs where rs.oficina.organismoResponsable in (:organismos) " +
                "and rs.estado = :idEstado order by rs.fecha desc");

        q.setParameter("organismos", organismos);
        q.setParameter("idEstado", idEstado);

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    public Long getTotalByLibro(Long idLibro) throws I18NException {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idEntidad, Long idOficina) throws I18NException {

        Query q;
        Query q2;

        q = em.createQuery("Select rs from RegistroSalida as rs where rs.entidad.id = :idEntidad and rs.oficina.id = :idOficina " +
                "and (rs.estado = :rechazado or rs.estado = :reenviado) order by rs.fecha desc");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idOficina", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        q2 = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.entidad.id = :idEntidad and rs.oficina.id = :idOficina " +
                "and (rs.estado = :rechazado or rs.estado = :reenviado)");

        q2.setParameter("idEntidad", idEntidad);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q2.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);


        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
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
    public List<RegistroSalida> getSirRechazadosReenviados(Long idEntidad, Long idOficina, Integer total) throws I18NException {

        Query q;

        q = em.createQuery("Select rs.id, rs.fecha, rs.registroDetalle.decodificacionEntidadRegistralDestino," +
                " rs.estado, rs.registroDetalle.decodificacionTipoAnotacion from RegistroSalida as rs where rs.entidad.id = :idEntidad and rs.oficina.id = :idOficinaActiva " +
                "and (rs.estado = :rechazado or rs.estado = :reenviado) order by rs.id desc");

        q.setMaxResults(total);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroSalida> registros = new ArrayList<RegistroSalida>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSalida registroSalida = new RegistroSalida();
            registroSalida.setId((Long) object[0]);
            registroSalida.setFecha((Date) object[1]);
            registroSalida.getRegistroDetalle().setDecodificacionEntidadRegistralDestino((String) object[2]);
            registroSalida.setEstado((Long) object[3]);
            registroSalida.getRegistroDetalle().setDecodificacionTipoAnotacion((String) object[4]);

            registros.add(registroSalida);

        }

        return registros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getSirRechazadosReenviadosCount(Long idOficina) throws I18NException{

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.oficina.id = :idOficinaActiva " +
                "and (rs.estado = :rechazado or rs.estado = :reenviado)");


        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getByDocumento(Long idEntidad, String documento) throws I18NException{

        Query q;

        q = em.createQuery("Select DISTINCT rs from RegistroSalida as rs left outer join rs.registroDetalle.interesados interessat " +
                "where (UPPER(interessat.documento) LIKE UPPER(:documento)) and rs.entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("documento", documento.trim());
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long queryCount(String query) throws I18NException {

        Query q;

        q = em.createQuery(query);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> ultimosOrganismosRegistro(UsuarioEntidad usuarioEntidad) throws I18NException {

        Query q;
        q = em.createQuery("Select distinct(rs.registroDetalle.id) from RegistroSalida as rs left outer join rs.registroDetalle.interesados interesado where " +
                " rs.usuario.id = :idUsuarioEntidad and rs.estado != :anulado and interesado.tipo = :administracion order by rs.registroDetalle.id desc");

        q.setParameter("idUsuarioEntidad", usuarioEntidad.getId());
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(PropiedadGlobalUtil.getTotalOrganismosSelect(usuarioEntidad.getEntidad().getId()));

        List<Long> registros = q.getResultList();

        if(registros.isEmpty()){
            return null;
        }

        Query q1 = em.createQuery("Select distinct(i.codigoDir3), i.razonSocial from Interesado as i where i.tipo = :administracion and i.registroDetalle.id in (:registros)");

        // Parámetros
        q1.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q1.setParameter("registros", registros);
        q1.setHint("org.hibernate.readOnly", true);

        List<Object[]> destinos = q1.getResultList();
        List<Organismo> organismos = new ArrayList<>();

        for (Object[] object : destinos) {
            organismos.add(new Organismo(null, (String) object[0], (String) object[1]));
        }

        return organismos;
    }


    /**
     * Carga los Anexos Completos al RegistroSalida pasado por parámetro
     * @param registroSalida
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    private RegistroSalida cargarAnexosFull(RegistroSalida registroSalida) throws I18NException {

        Long idEntidad = registroSalida.getEntidad().getId();
        List<Anexo> anexos = registroSalida.getRegistroDetalle().getAnexos();

        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de salida.
        registroSalida.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroSalida;
    }

    /**
     * Comprueba si alguno de los valores de búsqueda referentes al Interesado se ha rellenado
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @return
     */
    private boolean busquedaInteresados(String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc) {

        return StringUtils.isNotEmpty(interesadoNom) || StringUtils.isNotEmpty(interesadoLli1) || StringUtils.isNotEmpty(interesadoLli2) || StringUtils.isNotEmpty(interesadoDoc);
    }
}
