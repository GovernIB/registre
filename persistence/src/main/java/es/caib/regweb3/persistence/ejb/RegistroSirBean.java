package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.*;
import es.caib.regweb3.model.utils.*;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.schema.*;
import es.caib.regweb3.sir.core.schema.types.Documentacion_FisicaType;
import es.caib.regweb3.sir.core.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.schema.types.Tipo_RegistroType;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.IConsultaService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static es.caib.regweb3.utils.RegwebConstantes.*;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */

@Stateless(name = "RegistroSirEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RegistroSirBean extends BaseEjbJPA<RegistroSir, Long> implements RegistroSirLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private ArchivoLocal archivoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private MetadatoRegistroSirLocal metadatoRegistroSirEjb;
    @EJB private MetadatoAnexoSirLocal metadatoAnexoSirEjb;

    @Autowired IConsultaService consultaService;


    @Override
    public List<Long> getUltimosPendientesProcesarERTE(EstadoRegistroSir estado, String oficinaSir, Date fechaInicio, Date fechaFin, String aplicacion, Integer total) throws I18NException {

        Query q = em.createQuery("Select r.id from RegistroSir as r " +
                "where r.codigoEntidadRegistral = :oficinaSir and r.estado = :idEstado " +
                "and  (r.fechaRecepcion >= :fechaInicio and r.fechaRecepcion <= :fechaFin) " +
                "and r.aplicacion LIKE :aplicacion " +
                "order by r.fechaRecepcion");

        q.setMaxResults(total);
        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("aplicacion", "%"+aplicacion+"%");
        q.setParameter("idEstado", estado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    public RegistroSir getReference(Long id) throws I18NException {

        return em.getReference(RegistroSir.class, id);
    }

    @Override
    public RegistroSir findById(Long id) throws I18NException {

        RegistroSir registroSir = em.find(RegistroSir.class, id);
        Hibernate.initialize(registroSir.getAnexos());
        Hibernate.initialize(registroSir.getInteresados());

        return registroSir;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getAll() throws I18NException {

        return em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(registroSir.id) from RegistroSir as registroSir");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir where " +
                "registroSir.identificadorIntercambio = :identificadorIntercambio and registroSir.codigoEntidadRegistral = :codigoEntidadRegistralDestino " +
                "and registroSir.estado != :eliminado");

        q.setParameter("identificadorIntercambio",identificadorIntercambio);
        q.setParameter("codigoEntidadRegistralDestino",codigoEntidadRegistralDestino);
        q.setParameter("eliminado",EstadoRegistroSir.ELIMINADO);

        List<RegistroSir> registroSir = q.getResultList();
        if(registroSir.size() >= 1){
            return registroSir.get(0);
        }else{
            return null;
        }
    }

    public RegistroSir getByIdIntercambio(String identificadorIntercambio) throws I18NException {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir where " +
                "registroSir.identificadorIntercambio = :identificadorIntercambio " +
                "and registroSir.estado != :eliminado");

        q.setParameter("identificadorIntercambio",identificadorIntercambio);
        q.setParameter("eliminado",EstadoRegistroSir.ELIMINADO);

        List<RegistroSir> registroSir = q.getResultList();
        if(registroSir.size() >= 1){
            return registroSir.get(0);
        }else{
            return null;
        }
    }

    @Override
    public EstadoRegistroSir getEstado(Long idRegistroSir) throws I18NException {

        Query q = em.createQuery("Select registroSir.estado from RegistroSir as registroSir where registroSir.id = :idRegistroSir");
        q.setParameter("idRegistroSir",idRegistroSir);

        return (EstadoRegistroSir) q.getSingleResult();
    }

    @Override
    public RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws I18NException{

        RegistroSir registroSir = findById(idRegistroSir);

        List<AnexoSir> anexosFull = new ArrayList<AnexoSir>();
        for (AnexoSir anexoSir : registroSir.getAnexos()) {

            anexoSir.setAnexoData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            anexosFull.add(anexoSir);
        }

        registroSir.setAnexos(anexosFull);

        return registroSir;

    }

    @Override
    public RegistroSir getRegistroSirConMetadatos(Long idRegistroSir) throws Exception {

        RegistroSir registroSir = findById(idRegistroSir);

        Hibernate.initialize(registroSir.getMetadatosRegistroSir());

        return registroSir;

    }

    @Override
    public List<AnexoSir> getAnexos(RegistroSir registroSir) throws I18NException{

        List<AnexoSir> anexosFull = new ArrayList<AnexoSir>();
        for (AnexoSir anexoSir : registroSir.getAnexos()) {

            anexoSir.setAnexoData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            anexosFull.add(anexoSir);
        }

        return anexosFull;
    }

    @Override
    public RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio, Entidad entidad) throws I18NException{

        RegistroSir registroSir = transformarFicheroIntercambio(ficheroIntercambio, entidad);
        registroSir.setEstado(EstadoRegistroSir.RECIBIDO);

        try{

            // En caso de recepción, le asignamos la entidad a la que va dirigida
            if(registroSir.getEntidad() == null){

                registroSir.setEntidad(entidad);
            }

            registroSir = persist(registroSir);

            // Guardamos los Interesados
            if(registroSir.getInteresados() != null && registroSir.getInteresados().size() > 0){
                for(InteresadoSir interesadoSir: registroSir.getInteresados()){
                    interesadoSir.setRegistroSir(registroSir);

                    interesadoSirEjb.guardarInteresadoSir(interesadoSir);
                }
            }

            // Guardamos los Anexos
            if(registroSir.getAnexos() != null && registroSir.getAnexos().size() > 0){
                for(AnexoSir anexoSir: registroSir.getAnexos()){
                    anexoSir.setRegistroSir(registroSir);

                    anexoSirEjb.persist(anexoSir);
                }
            }
            em.flush();

            // Creamos la TrazabilidadSir
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
            trazabilidadSir.setAplicacion(registroSir.getAplicacion());
            trazabilidadSir.setNombreUsuario(registroSir.getNombreUsuario());
            trazabilidadSir.setContactoUsuario(registroSir.getContactoUsuario());
            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
            trazabilidadSirEjb.persist(trazabilidadSir);

        }catch (Exception e){
            log.info("Error al crear el RegistroSir:");
            e.printStackTrace();
            for(AnexoSir anexoSir: registroSir.getAnexos()){
                ArchivoManager am = new ArchivoManager(anexoSir.getAnexo(),archivoEjb);
                am.processErrorArchivosWithoutThrowException();
                log.info("Eliminamos los posibles archivos creados: " + anexoSir.getAnexo().getId());
            }
            throw e;
        }

        return registroSir;
    }

    @Override
    public void eliminarRegistroSir(RegistroSir registroSir) throws I18NException{

        List<TrazabilidadSir> trazabilidades = trazabilidadSirEjb.getByRegistroSir(registroSir.getId());

        // Eliminamos sus trazabilidades
        for (TrazabilidadSir trazabilidadSir :trazabilidades) {
            trazabilidadSirEjb.remove(trazabilidadSir);
        }

        // Copiamos los id de los anexos sir a eliminar
        List<Long> idsAnexosSir = new ArrayList<>();
        for(AnexoSir anexoSir: registroSir.getAnexos()){
            idsAnexosSir.add(anexoSir.getAnexo().getId());
        }

        // Eliminamos el Anexo Sir
        remove(registroSir);

        // Eliminamos los archivos
        for(Long idAnexoSir: idsAnexosSir){
            FileSystemManager.eliminarArchivo(idAnexoSir);
        }

    }

    @Override
    public void marcarEliminado(RegistroSir registroSir, UsuarioEntidad usuario, String observaciones) throws I18NException{

        // Creamos la TrazabilidadSir
        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_ELIMINAR);
        trazabilidadSir.setRegistroSir(registroSir);
        trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
        trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
        trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
        trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
        trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
        trazabilidadSir.setObservaciones(observaciones);
        trazabilidadSirEjb.persist(trazabilidadSir);

        modificarEstado(registroSir.getId(),EstadoRegistroSir.ELIMINADO);
    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSir registroSir, String oficinaSir, String estado, String entidad) throws I18NException{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder queryBase = new StringBuilder("Select rs.id, rs.decodificacionEntidadRegistralOrigen, rs.fechaRecepcion, rs.identificadorIntercambio, rs.numeroRegistro, rs.resumen, " +
                "rs.estado, rs.tipoRegistro, rs.codigoEntidadRegistralOrigen, rs.decodificacionEntidadRegistralOrigen, rs.codigoEntidadRegistralDestino, rs.decodificacionEntidadRegistralDestino, rs.aplicacion, rs.documentacionFisica, rs.numeroReintentos from RegistroSir as rs ");

        StringBuilder query = new StringBuilder(queryBase);

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(oficinaSir)){
            where.add(" (rs.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);
        }

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(entidad)){
            where.add(" (rs.entidad.codigoDir3 = :entidad) "); parametros.put("entidad",entidad);
        }

        if (registroSir.getResumen() != null && registroSir.getResumen().length() > 0) {
            where.add(DataBaseUtils.like("rs.resumen", "resumen", parametros, registroSir.getResumen()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getIdentificadorIntercambio())) {
            where.add(DataBaseUtils.like("rs.identificadorIntercambio", "identificadorIntercambio", parametros, registroSir.getIdentificadorIntercambio()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getNumeroRegistro())) {
            where.add(DataBaseUtils.like("rs.numeroRegistro", "numeroRegistro", parametros, registroSir.getNumeroRegistro()));
        }

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" rs.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
        }

        if (registroSir.getTipoRegistro() != null) {
            where.add(" rs.tipoRegistro = :tipoRegistro "); parametros.put("tipoRegistro", registroSir.getTipoRegistro());
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getAplicacion())) {
            where.add(DataBaseUtils.like("rs.aplicacion", "aplicacion", parametros, registroSir.getAplicacion()));
        }

        // Intervalo fechas
        where.add(" (rs.fechaRecepcion >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" rs.fechaRecepcion <= :fechaFin) "); parametros.put("fechaFin", fechaFin);

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
        StringBuilder queryCount = new StringBuilder("Select count(rs.id) from RegistroSir as rs ");
        q2 = em.createQuery(query.toString().replaceAll(queryBase.toString(), queryCount.toString()));

        // añadimos el order by
        query.append(" order by rs.fechaRecepcion desc");
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
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> results = q.getResultList();
        List<RegistroSir> registros = new ArrayList<>();

        for (Object[] result : results) {
            RegistroSir registro =  new RegistroSir();
            registro.setId((Long) result[0]);
            registro.setDecodificacionEntidadRegistralOrigen((String) result[1]);
            registro.setFechaRecepcion((Date) result[2]);
            registro.setIdentificadorIntercambio((String) result[3]);
            registro.setNumeroRegistro((String)result[4]);
            registro.setResumen((String) result[5]);
            registro.setEstado((EstadoRegistroSir) result[6]);
            registro.setTipoRegistro((TipoRegistro) result[7]);
            registro.setCodigoEntidadRegistralOrigen((String) result[8]);
            registro.setDecodificacionEntidadRegistralOrigen((String) result[9]);
            registro.setCodigoEntidadRegistralDestino((String) result[10]);
            registro.setDecodificacionEntidadRegistralDestino((String) result[11]);
            registro.setAplicacion((String) result[12]);
            registro.setDocumentacionFisica((String) result[13]);
            registro.setNumeroReintentos((Integer) result[14]);

            registros.add(registro);
        }

        paginacion.setListado(registros);

        return paginacion;
    }

    @Override
    public Paginacion getRegistrosEstado(Integer pageNumber, String oficinaSir, String estado) throws I18NException{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder queryBase = new StringBuilder("Select rs.id, rs.decodificacionEntidadRegistralOrigen, rs.decodificacionEntidadRegistralDestino, rs.fechaRecepcion, rs.identificadorIntercambio, rs.numeroRegistro, " +
                "rs.resumen, rs.estado, rs.documentacionFisica, rs.tipoRegistro from RegistroSir as rs ");

        StringBuilder query = new StringBuilder(queryBase);

        where.add(" (rs.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" rs.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
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
        StringBuilder queryCount = new StringBuilder("Select count(rs.id) from RegistroSir as rs ");
        q2 = em.createQuery(query.toString().replaceAll(queryBase.toString(), queryCount.toString()));

        // añadimos el order by
        query.append(" order by rs.fechaRecepcion");
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
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> results = q.getResultList();
        List<RegistroSir> registros = new ArrayList<>();

        for (Object[] result : results) {
            RegistroSir registro =  new RegistroSir();
            registro.setId((Long) result[0]);
            registro.setDecodificacionEntidadRegistralOrigen((String) result[1]);
            registro.setDecodificacionEntidadRegistralDestino((String) result[2]);
            registro.setFechaRecepcion((Date) result[3]);
            registro.setIdentificadorIntercambio((String) result[4]);
            registro.setNumeroRegistro((String)result[5]);
            registro.setResumen((String) result[6]);
            registro.setEstado((EstadoRegistroSir) result[7]);
            registro.setDocumentacionFisica((String) result[8]);
            registro.setTipoRegistro((TipoRegistro) result[9]);
            registros.add(registro);
        }

        paginacion.setListado(registros);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws I18NException{

        Query q = em.createQuery("Select r.id, r.decodificacionEntidadRegistralOrigen, r.fechaRecepcion, r.resumen, r.documentacionFisica from RegistroSir as r " +
                "where r.codigoEntidadRegistral = :oficinaSir and r.estado = :idEstado " +
                "order by r.fechaRecepcion");

        q.setMaxResults(total);
        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("idEstado", EstadoRegistroSir.RECIBIDO);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroSir> registros = new ArrayList<RegistroSir>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSir registroSir = new RegistroSir();
            registroSir.setId((Long) object[0]);
            registroSir.setDecodificacionEntidadRegistralOrigen((String) object[1]);
            registroSir.setFechaRecepcion((Date) object[2]);
            registroSir.setResumen((String) object[3]);
            registroSir.setDocumentacionFisica((String) object[4]);

            registros.add(registroSir);
        }

        return registros;
    }



    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getPendientesProcesarCount(String oficinaSir) throws I18NException{

        Query q = em.createQuery("Select count(registroSir.id) from RegistroSir as registroSir " +
                "where registroSir.codigoEntidadRegistral = :oficinaSir and registroSir.estado = :idEstado " +
                "and registroSir.documentacionFisica = :no_acompanya");

        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("idEstado", EstadoRegistroSir.RECIBIDO);
        q.setParameter("no_acompanya", RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC.toString());
        q.setHint("org.hibernate.readOnly", true);

        return  (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws I18NException {

        Query q = em.createQuery("update RegistroSir set estado=:estado, fechaEstado=:fechaEstado where id = :idRegistroSir");
        q.setParameter("estado", estado);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void modificarEstadoNuevaTransaccion(Long idRegistroSir, EstadoRegistroSir estado) throws I18NException {

        Query q = em.createQuery("update RegistroSir set estado=:estado, fechaEstado=:fechaEstado where id = :idRegistroSir");
        q.setParameter("estado", estado);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void incrementarReintentos(Long idRegistroSir, Integer reintentos) throws I18NException {

        Query q = em.createQuery("update RegistroSir set numeroReintentos=:reintentos where id = :idRegistroSir");
        q.setParameter("reintentos", reintentos);
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void modificarEstadoError(Long idRegistroSir, EstadoRegistroSir estado, String codigoError, String descripcionError) throws I18NException {

        Query q = em.createQuery("update RegistroSir set estado=:estado, codigoError=:codigoError, descripcionError=:descripcionError, fechaEstado=:fechaEstado where id = :idRegistroSir");
        q.setParameter("estado", estado);
        q.setParameter("codigoError", codigoError);
        q.setParameter("descripcionError", descripcionError);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException{

        List<RegistroSir> registros = em.createQuery("Select a from RegistroSir as a where a.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (RegistroSir registroSir : registros) {
            List<AnexoSir> anexos = registroSir.getAnexos();
            remove(registroSir);

            for (AnexoSir anexoSir : anexos) {
                FileSystemManager.eliminarArchivo(anexoSir.getAnexo().getId());
            }

        }
        em.flush();

        return registros.size();

    }

    /**
     * Crea un RegistroSir, a partir del FicheroIntercambio.
     *
     * @return Información del registroSir registral.
     */
    @Override
    public RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio, Entidad entidad)throws I18NException{

        final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

        RegistroSir registroSir = null;

        if (ficheroIntercambio.getFicheroIntercambio() != null) {

            Fichero_Intercambio_SICRES_3 fichero_intercambio_sicres_3 = ficheroIntercambio.getFicheroIntercambio();

            registroSir = new RegistroSir();
            registroSir.setFechaRecepcion(new Date());

            // Segmento De_Origen_o_Remitente
            De_Origen_o_Remitente de_Origen_o_Remitente = fichero_intercambio_sicres_3.getDe_Origen_o_Remitente();
            if (de_Origen_o_Remitente != null) {

                registroSir.setCodigoEntidadRegistralOrigen(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());

                if (StringUtils.isNotEmpty(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen())) {
                    registroSir.setDecodificacionEntidadRegistralOrigen(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen());
                } else {
                    registroSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()),de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), RegwebConstantes.OFICINA));
                }

                registroSir.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (StringUtils.isNotEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                } else {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()),de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), RegwebConstantes.UNIDAD));
                }


                registroSir.setNumeroRegistro(de_Origen_o_Remitente.getNumero_Registro_Entrada());
                registroSir.setTimestampRegistro(Base64.encodeBase64String(de_Origen_o_Remitente.getTimestamp_Entrada()));

                String fechaRegistro = de_Origen_o_Remitente.getFecha_Hora_Entrada();
                if (StringUtils.isNotBlank(fechaRegistro)) {
                    try {
                        registroSir.setFechaRegistro(SDF.parse(fechaRegistro));
                    } catch (ParseException e) {
                        log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                        throw new ValidacionException(Errores.ERROR_0037,"Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                    }
                }
            }

            // Segmento De_Destino
            De_Destino de_Destino = fichero_intercambio_sicres_3.getDe_Destino();
            if (de_Destino != null) {

                registroSir.setCodigoEntidadRegistral(de_Destino.getCodigo_Entidad_Registral_Destino());

                registroSir.setCodigoEntidadRegistralDestino(de_Destino.getCodigo_Entidad_Registral_Destino());
                if (StringUtils.isNotEmpty(de_Destino.getDecodificacion_Entidad_Registral_Destino())) {
                    registroSir.setDecodificacionEntidadRegistralDestino(de_Destino.getDecodificacion_Entidad_Registral_Destino());
                } else {
                    registroSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()),de_Destino.getCodigo_Entidad_Registral_Destino(), RegwebConstantes.OFICINA));
                }

                if (StringUtils.isNotEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    registroSir.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (StringUtils.isNotEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        registroSir.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        registroSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()),de_Destino.getCodigo_Unidad_Tramitacion_Destino(), RegwebConstantes.UNIDAD));
                    }
                }

            }

            // Segmento De_Asunto de_Asunto
            De_Asunto de_Asunto = fichero_intercambio_sicres_3.getDe_Asunto();
            if (de_Asunto != null) {
                registroSir.setResumen(de_Asunto.getResumen());
                registroSir.setCodigoAsunto(de_Asunto.getCodigo_Asunto_Segun_Destino());
                registroSir.setReferenciaExterna(de_Asunto.getReferencia_Externa());
                registroSir.setNumeroExpediente(de_Asunto.getNumero_Expediente());
            }

            // Segmento De_Internos_Control
            De_Internos_Control de_Internos_Control = fichero_intercambio_sicres_3.getDe_Internos_Control();
            if (de_Internos_Control != null) {

                registroSir.setIdentificadorIntercambio(de_Internos_Control.getIdentificador_Intercambio());
                registroSir.setAplicacion(de_Internos_Control.getAplicacion_Version_Emisora());
                registroSir.setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                registroSir.setDecodificacionTipoAnotacion(de_Internos_Control.getDescripcion_Tipo_Anotacion());
                registroSir.setNumeroTransporte(de_Internos_Control.getNumero_Transporte_Entrada());
                registroSir.setNombreUsuario(de_Internos_Control.getNombre_Usuario());
                registroSir.setContactoUsuario(de_Internos_Control.getContacto_Usuario());
                registroSir.setObservacionesApunte(de_Internos_Control.getObservaciones_Apunte());

                registroSir.setCodigoEntidadRegistralInicio(de_Internos_Control.getCodigo_Entidad_Registral_Inicio());
                if (StringUtils.isNotEmpty(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio())) {
                    registroSir.setDecodificacionEntidadRegistralInicio(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio());
                } else {
                    registroSir.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()),de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), RegwebConstantes.OFICINA));
                }


                // Tipo de transporte
                String tipoTransporte = de_Internos_Control.getTipo_Transporte_Entrada();
                if (StringUtils.isNotBlank(tipoTransporte)) {
                    registroSir.setTipoTransporte(TipoTransporte.getTipoTransporteValue(tipoTransporte));
                }

                // Tipo de registro
                Tipo_RegistroType tipo_Registro = de_Internos_Control.getTipo_Registro();
                if ((tipo_Registro != null) && StringUtils.isNotBlank(tipo_Registro.value())) {
                    registroSir.setTipoRegistro(TipoRegistro.getTipoRegistro(tipo_Registro.value()));
                }

                // Documentación física
                Documentacion_FisicaType documentacion_Fisica = de_Internos_Control.getDocumentacion_Fisica();
                if ((documentacion_Fisica != null) && StringUtils.isNotBlank(documentacion_Fisica.value())) {
                    registroSir.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaValue(documentacion_Fisica.value()));
                }

                // Indicador de prueba
                Indicador_PruebaType indicadorPrueba = de_Internos_Control.getIndicador_Prueba();
                if ((indicadorPrueba != null) && StringUtils.isNotBlank(indicadorPrueba.value())){
                    registroSir.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.value()));
                }

            }

            // Segmento De_Formulario_Generico
            De_Formulario_Generico de_Formulario_Generico = fichero_intercambio_sicres_3.getDe_Formulario_Generico();
            if (de_Formulario_Generico != null) {
                registroSir.setExpone(de_Formulario_Generico.getExpone());
                registroSir.setSolicita(de_Formulario_Generico.getSolicita());
            }

            // Segmento De_Interesado
            De_Interesado[] de_Interesados = fichero_intercambio_sicres_3.getDe_Interesado();
            if (ArrayUtils.isNotEmpty(de_Interesados)) {
                for (De_Interesado de_Interesado : de_Interesados) {
                    if (de_Interesado != null) {

                        // Si se trata de una Salida y no tiene Interesados
                        if(ficheroIntercambio.getTipoRegistro().equals(TipoRegistro.SALIDA) &&
                                StringUtils.isBlank(de_Interesado.getRazon_Social_Interesado())
                                && (StringUtils.isBlank(de_Interesado.getNombre_Interesado()) && StringUtils.isBlank(de_Interesado.getPrimer_Apellido_Interesado()))){

                            // Creamos uno a partir de la Entidad destino
                            registroSir.getInteresados().add(crearInteresadoJuridico(ficheroIntercambio, entidad.getId()));

                        }else{

                            InteresadoSir interesado = new InteresadoSir();

                            // Información del interesado
                            interesado.setDocumentoIdentificacionInteresado(de_Interesado.getDocumento_Identificacion_Interesado());
                            interesado.setRazonSocialInteresado(de_Interesado.getRazon_Social_Interesado());
                            interesado.setNombreInteresado(de_Interesado.getNombre_Interesado());
                            interesado.setPrimerApellidoInteresado(de_Interesado.getPrimer_Apellido_Interesado());
                            interesado.setSegundoApellidoInteresado(de_Interesado.getSegundo_Apellido_Interesado());
                            interesado.setCodigoPaisInteresado(de_Interesado.getPais_Interesado());
                            interesado.setCodigoProvinciaInteresado(de_Interesado.getProvincia_Interesado());
                            interesado.setCodigoMunicipioInteresado(de_Interesado.getMunicipio_Interesado());
                            interesado.setDireccionInteresado(de_Interesado.getDireccion_Interesado());
                            interesado.setCodigoPostalInteresado(de_Interesado.getCodigo_Postal_Interesado());
                            interesado.setCorreoElectronicoInteresado(de_Interesado.getCorreo_Electronico_Interesado());
                            interesado.setTelefonoInteresado(de_Interesado.getTelefono_Contacto_Interesado());
                            interesado.setDireccionElectronicaHabilitadaInteresado(de_Interesado.getDireccion_Electronica_Habilitada_Interesado());

                            String tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Interesado();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            String canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Interesado();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionInteresado(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            // Información del representante
                            interesado.setDocumentoIdentificacionRepresentante(de_Interesado.getDocumento_Identificacion_Representante());
                            interesado.setRazonSocialRepresentante(de_Interesado.getRazon_Social_Representante());
                            interesado.setNombreRepresentante(de_Interesado.getNombre_Representante());
                            interesado.setPrimerApellidoRepresentante(de_Interesado.getPrimer_Apellido_Representante());
                            interesado.setSegundoApellidoRepresentante(de_Interesado.getSegundo_Apellido_Representante());
                            interesado.setCodigoPaisRepresentante(de_Interesado.getPais_Representante());
                            interesado.setCodigoProvinciaRepresentante(de_Interesado.getProvincia_Representante());
                            interesado.setCodigoMunicipioRepresentante(de_Interesado.getMunicipio_Representante());
                            interesado.setDireccionRepresentante(de_Interesado.getDireccion_Representante());
                            interesado.setCodigoPostalRepresentante(de_Interesado.getCodigo_Postal_Representante());
                            interesado.setCorreoElectronicoRepresentante(de_Interesado.getCorreo_Electronico_Representante());
                            interesado.setTelefonoRepresentante(de_Interesado.getTelefono_Contacto_Representante());
                            interesado.setDireccionElectronicaHabilitadaRepresentante(de_Interesado.getDireccion_Electronica_Habilitada_Representante());

                            tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Representante();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionRepresentante(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Representante();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionRepresentante(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            interesado.setObservaciones(de_Interesado.getObservaciones());

                            registroSir.getInteresados().add(interesado);
                        }

                    }
                }
            }

            // Segmento De_Anexos
            De_Anexo[] de_Anexos = fichero_intercambio_sicres_3.getDe_Anexo();
            if (ArrayUtils.isNotEmpty(de_Anexos)) {
                for (De_Anexo de_Anexo : de_Anexos) {
                    if (de_Anexo != null) {
                        AnexoSir anexo = new AnexoSir();

                        anexo.setNombreFichero(es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu(de_Anexo.getNombre_Fichero_Anexado()));
                        anexo.setIdentificadorFichero(de_Anexo.getIdentificador_Fichero());
                        anexo.setIdentificadorDocumentoFirmado(de_Anexo.getIdentificador_Documento_Firmado());
                        anexo.setCertificado(Base64.encodeBase64String(de_Anexo.getCertificado()));
                        anexo.setFirma(Base64.encodeBase64String(de_Anexo.getFirma_Documento()));
                        anexo.setTimestamp(Base64.encodeBase64String(de_Anexo.getTimeStamp()));
                        anexo.setValidacionOCSPCertificado(Base64.encodeBase64String(de_Anexo.getValidacion_OCSP_Certificado()));
                        anexo.setHash(Base64.encodeBase64String(de_Anexo.getHash()));
                        //Si el tipo mime es null, se obtiene de la extensión del fichero
                        if (StringUtils.isEmpty(de_Anexo.getTipo_MIME())) {
                            //String mime = MimeTypeUtils.getMimeTypeFileName(de_Anexo.getNombre_Fichero_Anexado());
                            //if(mime.length() <= 20){
                            //    anexo.setTipoMIME(mime);
                            //}
                        } else {
                            anexo.setTipoMIME(de_Anexo.getTipo_MIME());
                        }

                        ArchivoManager am = null;
                        try {
                            am = new ArchivoManager(archivoEjb, de_Anexo.getNombre_Fichero_Anexado(), anexo.getTipoMIME(), de_Anexo.getAnexo());
                            anexo.setAnexo(am.prePersist());
                        } catch (Exception e) {
                            log.info("Error al crear el Anexo en el sistema de archivos", e);
                            am.processError();
                            throw new ServiceException(Errores.ERROR_0065,e);
                        }

                        anexo.setObservaciones(de_Anexo.getObservaciones());

                        String validezDocumento = de_Anexo.getValidez_Documento();
                        if (StringUtils.isNotBlank(validezDocumento)) {
                            anexo.setValidezDocumento(ValidezDocumento.getValidezDocumentoValue(validezDocumento));
                        }

                        String tipoDocumento = de_Anexo.getTipo_Documento();
                        if (StringUtils.isNotBlank(tipoDocumento)) {
                            anexo.setTipoDocumento(TipoDocumento.getTipoDocumentoValue(tipoDocumento));
                        }

                        registroSir.getAnexos().add(anexo);
                    }
                }
            }
        }

        return registroSir;

    }

    /**
     * Transforma un {@link RegistroEntrada} en un {@link RegistroSir}
     * @param registroEntrada
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException {

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        RegistroSir registroSir = new RegistroSir();

        registroSir.setIndicadorPrueba(IndicadorPrueba.NORMAL);
        registroSir.setEntidad(registroEntrada.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        registroSir.setCodigoEntidadRegistralOrigen(registroEntrada.getOficina().getCodigo());
        registroSir.setDecodificacionEntidadRegistralOrigen(registroEntrada.getOficina().getDenominacion());
        registroSir.setNumeroRegistro(registroEntrada.getNumeroRegistroFormateado());
        registroSir.setFechaRegistro(registroEntrada.getFecha());
        registroSir.setCodigoUnidadTramitacionOrigen(null);
        registroSir.setDecodificacionUnidadTramitacionOrigen(null);
        //registroSir.setCodigoUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getCodigo());
        //registroSir.setDecodificacionUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        registroSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        registroSir.setCodigoUnidadTramitacionDestino(registroEntrada.getDestinoExternoCodigo());
        registroSir.setDecodificacionUnidadTramitacionDestino(registroEntrada.getDestinoExternoDenominacion());

        // Segmento De_Asunto
        registroSir.setResumen(registroDetalle.getExtracto());
        if(registroEntrada.getDestino() != null){
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            registroSir.setCodigoAsunto(tra.getNombre());
        }
        registroSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        registroSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        registroSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        registroSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        registroSir.setNombreUsuario(registroEntrada.getUsuario().getNombreCompleto());
        registroSir.setContactoUsuario(registroEntrada.getUsuario().getUsuario().getEmail());
        registroSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        registroSir.setAplicacion(registroDetalle.getAplicacion());
        registroSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        registroSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        registroSir.setTipoRegistro(TipoRegistro.ENTRADA);
        registroSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        registroSir.setObservacionesApunte(registroDetalle.getObservaciones());
        registroSir.setCodigoEntidadRegistralInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroEntrada.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        registroSir.setExpone(registroDetalle.getExpone());
        registroSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados
        registroSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos

        // Firmar anexos
        Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroEntrada.getUsuario().getUsuario().getIdioma()));

        List<AnexoFull> anexosfirmados = signatureServerEjb.firmarAnexosEnvioSir(registroDetalle.getAnexosFull(),registroEntrada.getUsuario().getEntidad().getId(),locale,true, registroEntrada.getNumeroRegistroFormateado());

        registroSir.setAnexos(transformarAnexosSir(anexosfirmados, registroSir.getIdentificadorIntercambio()));

        return registroSir;
    }

    /**
     * Transforma un {@link RegistroSalida} en un {@link RegistroSir}
     * @param registroSalida
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroSalida(RegistroSalida registroSalida) throws I18NException{

        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        RegistroSir registroSir = new RegistroSir();

        registroSir.setIndicadorPrueba(IndicadorPrueba.NORMAL); // todo Modificar cuando entremos en Producción
        registroSir.setEntidad(registroSalida.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        registroSir.setCodigoEntidadRegistralOrigen(registroSalida.getOficina().getCodigo());
        registroSir.setDecodificacionEntidadRegistralOrigen(registroSalida.getOficina().getDenominacion());
        registroSir.setNumeroRegistro(registroSalida.getNumeroRegistroFormateado());
        registroSir.setFechaRegistro(registroSalida.getFecha());
        registroSir.setCodigoUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getCodigo());
        registroSir.setDecodificacionUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        registroSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        registroSir.setCodigoUnidadTramitacionDestino(LibSirUtils.obtenerCodigoUnidadTramitacionDestino(registroDetalle));
        String destinoExternoDecodificacion = LibSirUtils.obtenerDenominacionUnidadTramitacionDestino(registroDetalle);
        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(destinoExternoDecodificacion)) {
            registroSir.setDecodificacionUnidadTramitacionDestino(destinoExternoDecodificacion);
        } else {
            registroSir.setDecodificacionUnidadTramitacionDestino(null);
        }

        // Segmento De_Asunto
        registroSir.setResumen(registroDetalle.getExtracto());
        /*if(registroSalida.getDestino() != null){ //todo Revisar
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            registroSir.setCodigoAsunto(tra.getNombre());
        }*/
        registroSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        registroSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        registroSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        registroSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        registroSir.setNombreUsuario(registroSalida.getUsuario().getNombreCompleto());
        registroSir.setContactoUsuario(registroSalida.getUsuario().getUsuario().getEmail());
        registroSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        registroSir.setAplicacion(registroDetalle.getAplicacion());
        registroSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        registroSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        registroSir.setTipoRegistro(TipoRegistro.SALIDA);
        registroSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        registroSir.setObservacionesApunte(registroDetalle.getObservaciones());
        registroSir.setCodigoEntidadRegistralInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        registroSir.setExpone(registroDetalle.getExpone());
        registroSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados: Irá siempre vacio, porque el destinatario va informado en el segmento DeDestino
        //registroSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos

        // Firmar anexos
        Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroSalida.getUsuario().getUsuario().getIdioma()));

        List<AnexoFull> anexosfirmados = signatureServerEjb.firmarAnexosEnvioSir(registroDetalle.getAnexosFull(),registroSalida.getUsuario().getEntidad().getId(),locale,true, registroSalida.getNumeroRegistroFormateado());

        registroSir.setAnexos(transformarAnexosSir(anexosfirmados, registroSir.getIdentificadorIntercambio()));

        return registroSir;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getEnviadosSinAck(Long idEntidad) throws I18NException{

        Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir " +
                "where registroSir.entidad.id = :idEntidad and (registroSir.estado = :reenviado or registroSir.estado = :rechazado) " +
                "and registroSir.numeroReintentos < :maxReintentos order by id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("reenviado", EstadoRegistroSir.REENVIADO);
        q.setParameter("rechazado", EstadoRegistroSir.RECHAZADO);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(20);

        return  q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getEnviadosConError(Long idEntidad) throws I18NException{

        Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir " +
                "where registroSir.entidad.id = :idEntidad and registroSir.estado = :reenviado or registroSir.estado = :rechazado " +
                "and (registroSir.codigoError = '0039' or registroSir.codigoError = '0046' or registroSir.codigoError = '0057' or  registroSir.codigoError = '0058') " +
                "and registroSir.numeroReintentos < :maxReintentos order by id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("reenviado", EstadoRegistroSir.REENVIADO_Y_ERROR);
        q.setParameter("rechazado", EstadoRegistroSir.RECHAZADO_Y_ERROR);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(20);

        return  q.getResultList();

    }

    @Override
    public void reiniciarIntentos(Long idRegistroSir) throws I18NException {

        Query q = em.createQuery("update RegistroSir set numeroReintentos=0 where id = :idRegistroSir");
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }

    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesados
     * @return
     * @throws I18NException
     */
    private List<InteresadoSir> procesarInteresadosSir(List<Interesado> interesados) throws I18NException{
        List<InteresadoSir> interesadosSir = new ArrayList<InteresadoSir>();

        for (Interesado interesado : interesados) {
            InteresadoSir interesadoSir;

            if (!interesado.getIsRepresentante()){
                interesadoSir = transformarInteresadoSir(interesado, interesado.getRepresentante());
                interesadosSir.add(interesadoSir);
            }


        }
        return interesadosSir;
    }

    /**
     * Transforma un {@link InteresadoSir} en un {@link Interesado}
     * @param interesado
     * @param representante
     * @return Interesado de tipo {@link Interesado}
     * @throws I18NException
     */
    private InteresadoSir transformarInteresadoSir(Interesado interesado, Interesado representante) throws I18NException{

        InteresadoSir interesadoSir = new InteresadoSir();

        if (interesado.getTipoDocumentoIdentificacion() != null) {
            interesadoSir.setTipoDocumentoIdentificacionInteresado(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
        }

        if (StringUtils.isNotEmpty(interesado.getDocumento())) {
            interesadoSir.setDocumentoIdentificacionInteresado(interesado.getDocumento());
        }

        if (StringUtils.isNotEmpty(interesado.getRazonSocial())) {
            interesadoSir.setRazonSocialInteresado(es.caib.regweb3.utils.StringUtils.recortarCadena(interesado.getRazonSocial(),80));
        }

        if (StringUtils.isNotEmpty(interesado.getNombre())) {
            interesadoSir.setNombreInteresado(interesado.getNombre());
        }

        if (StringUtils.isNotEmpty(interesado.getApellido1())) {
            interesadoSir.setPrimerApellidoInteresado(interesado.getApellido1());
        }

        if (StringUtils.isNotEmpty(interesado.getApellido2())) {
            interesadoSir.setSegundoApellidoInteresado(interesado.getApellido2());
        }

        if(interesado.getPais() != null){
            String codigoPais = interesado.getPais().getCodigoPais().toString();

            if(codigoPais.length() == 1){
                codigoPais = "00"+codigoPais;
            }else if(codigoPais.length() == 2){
                codigoPais = "0"+codigoPais;
            }

            interesadoSir.setCodigoPaisInteresado(codigoPais);
        }

        if(interesado.getProvincia() != null){
            String codigoProvincia = interesado.getProvincia().getCodigoProvincia().toString();

            if(codigoProvincia.length() == 1){ // Le añadimos '0' delante si el código es del 1-9
                codigoProvincia = "0"+codigoProvincia;
            }
            interesadoSir.setCodigoProvinciaInteresado(codigoProvincia);
        }

        if(interesado.getLocalidad() != null){
            String codigoMunicipio = interesado.getLocalidad().getCodigoLocalidad().toString();

            if(codigoMunicipio.length() == 1){
                codigoMunicipio = "000"+codigoMunicipio;
            }else if(codigoMunicipio.length() == 2){
                codigoMunicipio = "00"+codigoMunicipio;
            }else if(codigoMunicipio.length() == 3){
                codigoMunicipio = "0"+codigoMunicipio;
            }

            interesadoSir.setCodigoMunicipioInteresado(codigoMunicipio);
        }

        if (StringUtils.isNotEmpty(interesado.getDireccion())) {
            interesadoSir.setDireccionInteresado(interesado.getDireccion());
        }

        if (StringUtils.isNotEmpty(interesado.getCp())) {
            interesadoSir.setCodigoPostalInteresado(interesado.getCp());
        }

        if (StringUtils.isNotEmpty(interesado.getEmail())) {
            interesadoSir.setCorreoElectronicoInteresado(interesado.getEmail());
        }

        if (StringUtils.isNotEmpty(interesado.getTelefono())) {
            interesadoSir.setTelefonoInteresado(interesado.getTelefono());
        }

        if (StringUtils.isNotEmpty(interesado.getDireccionElectronica())) {
            interesadoSir.setDireccionElectronicaHabilitadaInteresado(interesado.getDireccionElectronica());
        }

        if (interesado.getCanal() != null) {
            interesadoSir.setCanalPreferenteComunicacionInteresado(CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }

        if (StringUtils.isNotEmpty(interesado.getObservaciones())) {
            interesadoSir.setObservaciones(interesado.getObservaciones());
        }

        // Si tiene representante, también lo transformamos
        if(representante != null){

            if (representante.getTipoDocumentoIdentificacion() != null) {
                interesadoSir.setTipoDocumentoIdentificacionRepresentante(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
            }

            if (StringUtils.isNotEmpty(representante.getDocumento())) {
                interesadoSir.setDocumentoIdentificacionRepresentante(representante.getDocumento());
            }

            if (StringUtils.isNotEmpty(representante.getRazonSocial())) {
                interesadoSir.setRazonSocialRepresentante(representante.getRazonSocial());
            }

            if (StringUtils.isNotEmpty(representante.getNombre())) {
                interesadoSir.setNombreRepresentante(representante.getNombre());
            }

            if (StringUtils.isNotEmpty(representante.getApellido1())) {
                interesadoSir.setPrimerApellidoRepresentante(representante.getApellido1());
            }

            if (StringUtils.isNotEmpty(representante.getApellido2())) {
                interesadoSir.setSegundoApellidoRepresentante(representante.getApellido2());
            }

            if(representante.getPais() != null){
                String codigoPais = representante.getPais().getCodigoPais().toString();

                if(codigoPais.length() == 1){ // Le añadimos '00' delante si el código es del 1-9
                    codigoPais = "00"+codigoPais;
                }else if(codigoPais.length() == 2){ // Le añadimos '0' delante si el código es hasta 99
                    codigoPais = "0"+codigoPais;
                }

                interesadoSir.setCodigoPaisRepresentante(codigoPais);
            }

            if(representante.getProvincia() != null){

                String codigoProvincia = representante.getProvincia().getCodigoProvincia().toString();

                if(codigoProvincia.length() == 1){
                    codigoProvincia = "0"+codigoProvincia;  // Le añadimos '0' delante si el código es del 1-9
                }

                interesadoSir.setCodigoProvinciaRepresentante(codigoProvincia);
            }

            if(representante.getLocalidad() != null){
                String codigoMunicipio = representante.getLocalidad().getCodigoLocalidad().toString();

                if(codigoMunicipio.length() == 1){
                    codigoMunicipio = "000"+codigoMunicipio; // Le añadimos '000' delante si el código es hasta 1-9
                }else if(codigoMunicipio.length() == 2){ // Le añadimos '00' delante si el código es hasta 99
                    codigoMunicipio = "00"+codigoMunicipio;
                }else if(codigoMunicipio.length() == 3){
                    codigoMunicipio = "0"+codigoMunicipio; // Le añadimos '0' delante si el código es hasta 999
                }

                interesadoSir.setCodigoMunicipioRepresentante(codigoMunicipio);
            }

            if (StringUtils.isNotEmpty(representante.getDireccion())) {
                interesadoSir.setDireccionRepresentante(representante.getDireccion());
            }

            if (StringUtils.isNotEmpty(representante.getCp())) {
                interesadoSir.setCodigoPostalRepresentante(representante.getCp());
            }

            if (StringUtils.isNotEmpty(representante.getEmail())) {
                interesadoSir.setCorreoElectronicoRepresentante(representante.getEmail());
            }

            if (StringUtils.isNotEmpty(representante.getTelefono())) {
                interesadoSir.setTelefonoRepresentante(representante.getTelefono());
            }

            if (StringUtils.isNotEmpty(representante.getDireccionElectronica())) {
                interesadoSir.setDireccionElectronicaHabilitadaRepresentante(representante.getDireccionElectronica());
            }

            if (representante.getCanal() != null) {
                interesadoSir.setCanalPreferenteComunicacionRepresentante(CODIGO_BY_CANALNOTIFICACION.get(representante.getCanal()));
            }

        }

        return interesadoSir;

    }

    /**
     *
     * @param anexosFull
     * @param identificadorIntercambio
     * @return
     */
    private List<AnexoSir> transformarAnexosSir(List<AnexoFull> anexosFull, String identificadorIntercambio) throws I18NException{

        List<AnexoSir> anexosSir = new ArrayList<AnexoSir>();
        int secuencia = 0;

        for(AnexoFull anexoFull:anexosFull){

            final int modoFirma = anexoFull.getAnexo().getModoFirma();
            Anexo anexo = anexoFull.getAnexo();
            AnexoSir anexoSir;

            switch (modoFirma){

                case MODO_FIRMA_ANEXO_ATTACHED:

                    SignatureCustody sc = anexoFull.getSignatureCustody();

                    String identificador_fichero = RegistroUtils.generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
                    secuencia++;

                    anexoSir = crearAnexoSir(sc.getName(),identificador_fichero,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),
                            anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),sc.getMime(),sc.getData(),identificador_fichero,
                            anexo.getObservaciones());

                    anexosSir.add(anexoSir);

                    break;

                case MODO_FIRMA_ANEXO_DETACHED:

                    // ================= SEGMENTO 1: DOCUMENT ==================

                    DocumentCustody dc = anexoFull.getDocumentoCustody();

                    identificador_fichero = RegistroUtils.generateIdentificadorFichero(identificadorIntercambio, secuencia, dc.getName());
                    secuencia++;

                    anexoSir = crearAnexoSir(dc.getName(),identificador_fichero,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),
                            anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),dc.getMime(),dc.getData(),null,
                            anexo.getObservaciones());

                    anexosSir.add(anexoSir);

                    // ================= SEGMENTO 2: FIRMA ==================

                    sc = anexoFull.getSignatureCustody();

                    String identificador_fichero_FIRMA = RegistroUtils.generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
                    secuencia++;

                    anexoSir = crearAnexoSir(sc.getName(),identificador_fichero_FIRMA,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FICHERO_TECNICO), null,
                            null,anexo.getTimestamp(), null,
                            anexo.getHash(),sc.getMime(),sc.getData(),identificador_fichero,
                            anexo.getObservaciones());

                    anexosSir.add(anexoSir);

                    break;
            }


        }

        return anexosSir;
    }

    /**
     *
     * @param nombreFichero
     * @param identificadorFichero
     * @param validezDocumento
     * @param tipoDocumento
     * @param certificado
     * @param firma
     * @param timeStamp
     * @param validacionOCSPCertificado
     * @param hash
     * @param tipoMime
     * @param anexoData
     * @param identificadorDocumentoFirmado
     * @param observaciones
     * @return
     */
    private AnexoSir crearAnexoSir(String nombreFichero, String identificadorFichero, String validezDocumento, String tipoDocumento, byte[] certificado,
                                   String firma, byte[] timeStamp, byte[] validacionOCSPCertificado, byte[] hash, String tipoMime,
                                   byte[] anexoData, String identificadorDocumentoFirmado, String observaciones){
        AnexoSir anexoSir = new AnexoSir();



        //Controlamos que el nombre fichero no supere el maxlength de SIR
        if (nombreFichero!=null && nombreFichero.length() >= RegwebConstantes.ANEXO_NOMBREFICHERO_MAXLENGTH_SIR) {
            String nombreFicheroSinExtension = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
            String extension = nombreFichero.substring(nombreFichero.lastIndexOf("."), nombreFichero.length());
            nombreFicheroSinExtension = nombreFicheroSinExtension.substring(0, RegwebConstantes.ANEXO_NOMBREFICHERO_MAXLENGTH_SIR-5);
            nombreFichero = nombreFicheroSinExtension + extension;
        }

        // Eliminamos los caracteres no permitidos en SIR y además los no permitidos en Arxiu, lo hacemos porque algunas aplicaciones no implementan bien las normas SICRES y nos retornan envíos con caracteres permitidos
        nombreFichero = es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosSIR(nombreFichero, '_');
        anexoSir.setNombreFichero(es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu(nombreFichero));

        anexoSir.setIdentificadorFichero(identificadorFichero);
        anexoSir.setTipoDocumento(tipoDocumento);

        if(tipoMime != null){
            if(tipoMime.equals("text/xml")){ //SICRES3 obliga a que el mime de un xml sea application/xml
                anexoSir.setTipoMIME("application/xml");
            }else{
                if(tipoMimeAceptadoPorSir(tipoMime)!=null) {
                    anexoSir.setTipoMIME(tipoMime);
                }
            }
        }
        if(anexoData != null){
            anexoSir.setAnexoData(anexoData);
        }
        if(identificadorDocumentoFirmado != null){
            anexoSir.setIdentificadorDocumentoFirmado(identificadorDocumentoFirmado);
        }

        if(observaciones!= null && observaciones.length()>= RegwebConstantes.ANEXO_OBSERVACIONES_MAXLENGTH_SIR) {
            anexoSir.setObservaciones(observaciones.substring(0, RegwebConstantes.ANEXO_OBSERVACIONES_MAXLENGTH_SIR));
        }else{
            anexoSir.setObservaciones(observaciones);
        }

        return anexoSir;
    }

    /**
     * Crea un Interesado tipo Persona Juridica a partir del Código Unidad De Gestión de destino o si no está informado,
     * a partir del Código Entidad Registral de destino
     * @return
     */
    private InteresadoSir crearInteresadoJuridico(FicheroIntercambio ficheroIntercambio, Long idEntidad){

        InteresadoSir interesadoSalida = new InteresadoSir();

        if(StringUtils.isNotBlank(ficheroIntercambio.getCodigoUnidadTramitacionDestino())){

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(ficheroIntercambio.getCodigoUnidadTramitacionDestino());

            if(StringUtils.isNotBlank(ficheroIntercambio.getDescripcionUnidadTramitacionDestino())){
                interesadoSalida.setRazonSocialInteresado(ficheroIntercambio.getDescripcionUnidadTramitacionDestino());
            }else{
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),ficheroIntercambio.getCodigoUnidadTramitacionDestino(),RegwebConstantes.UNIDAD));

            }


        }else{
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

                OficinaTF oficinaTF = oficinasService.obtenerOficina(ficheroIntercambio.getCodigoEntidadRegistralDestino(),null,null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), oficinaTF.getCodUoResponsable(),RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }

    /**
     * Transforma un {@link RegistroSir} en un {@link RegistroEntrada} y lo registra
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroEntrada aceptarRegistroSirEntrada(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idOrganismoDestino)
            throws I18NException, I18NValidationException, ParseException, InterException {

        Libro libro = libroEjb.findById(idLibro);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setUsuario(usuario);
        registroEntrada.setOficina(oficinaActiva);
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroEntrada.setLibro(libro);

        // Obtenemos el Organismo destino indicado
        Organismo organismoDestino = organismoEjb.findByIdLigero(idOrganismoDestino);

        registroEntrada.setDestino(organismoDestino);
        registroEntrada.setDestinoExternoCodigo(null);
        registroEntrada.setDestinoExternoDenominacion(null);

        // RegistroDetalle
        registroEntrada.setRegistroDetalle(getRegistroDetalle(registroSir, idIdioma));

        // Interesados
        List<Interesado> interesados = procesarInteresados(registroSir.getInteresados());

        // Anexos
        List<AnexoFull> anexosFull = procesarAnexosRFU(registroSir);

        //METADATOS
        Set<MetadatoRegistroEntrada> metadatos = registroSir.getMetadatosRegistroSir().stream()
                .map(metadato -> new MetadatoRegistroEntrada(metadato.getTipo(), metadato.getCampo(), metadato.getValor()) {
                }).collect(Collectors.toSet());

        registroEntrada.setMetadatosRegistroEntrada(metadatos);

        // Registramos el Registro Entrada
        registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, entidad, usuario, interesados, anexosFull, true);


        // Creamos la TrazabilidadSir
        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO);
        trazabilidadSir.setRegistroSir(registroSir);
        trazabilidadSir.setRegistroEntrada(registroEntrada);
        trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
        trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
        trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
        trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
        trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
        trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
        trazabilidadSirEjb.persist(trazabilidadSir);

        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        trazabilidad.setRegistroSir(registroSir);
        trazabilidad.setRegistroEntradaOrigen(null);
        trazabilidad.setOficioRemision(null);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(registroEntrada);
        trazabilidad.setFecha(new Date());
        trazabilidadEjb.persist(trazabilidad);

        // Modificamos el estado del RegistroSir
        modificarEstado(registroSir.getId(), EstadoRegistroSir.ACEPTADO);

        return registroEntrada;
    }

    /**
     * Obtiene un {@link RegistroDetalle} a partir de los datos de un RegistroSir
     *
     * @param registroSir
     * @param idIdioma
     * @return
     * @throws I18NException
     */
    private RegistroDetalle getRegistroDetalle(RegistroSir registroSir, Long idIdioma) throws I18NException {

        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroDetalle.setRecibidoSir(true);
        if (registroSir.getModoRegistro().equals(ModoRegistro.getModoRegistroValue("PRESENCIAL"))) {
            registroDetalle.setPresencial(true);
        } else {
            registroDetalle.setPresencial(false);
        }
        registroDetalle.setExtracto(registroSir.getResumen());
        registroDetalle.setTipoDocumentacionFisica(Long.valueOf(registroSir.getDocumentacionFisica()));
        registroDetalle.setIdioma(idIdioma);
        registroDetalle.setCodigoSia(registroSir.getCodigoSia());// ahora con LIBSIR viene informado. Cogerlo de RegistroSIR

        registroDetalle.setCodigoAsunto(null);

        if (registroSir.getTipoTransporte() != null) {
            registroDetalle.setTransporte(Long.valueOf(registroSir.getTipoTransporte()));
        }
        if (StringUtils.isNotEmpty(registroSir.getNumeroTransporte())) {
            registroDetalle.setNumeroTransporte(registroSir.getNumeroTransporte());
        }
        if (StringUtils.isNotEmpty(registroSir.getObservacionesApunte())) {
            registroDetalle.setObservaciones(registroSir.getObservacionesApunte());
        }
        if(StringUtils.isNotEmpty(registroSir.getReferenciaExterna())){
            registroDetalle.setReferenciaExterna(registroSir.getReferenciaExterna());
        }
        if(StringUtils.isNotEmpty(registroSir.getNumeroExpediente())){
            registroDetalle.setExpediente(registroSir.getNumeroExpediente());
        }
        if(StringUtils.isNotEmpty(registroSir.getExpone())){
            registroDetalle.setExpone(registroSir.getExpone());
        }
        if(StringUtils.isNotEmpty(registroSir.getSolicita())){
            registroDetalle.setSolicita(registroSir.getSolicita());
        }

        registroDetalle.setOficinaOrigen(null);
        registroDetalle.setOficinaOrigenExternoCodigo(registroSir.getCodigoEntidadRegistralInicio());
        registroDetalle.setOficinaOrigenExternoDenominacion(registroSir.getDecodificacionEntidadRegistralInicio());

        registroDetalle.setNumeroRegistroOrigen(registroSir.getNumeroRegistro());
        registroDetalle.setFechaOrigen(registroSir.getFechaRegistro());

        // Interesados
        registroDetalle.setInteresados(null);

        // Anexos
        //registroDetalle.setAnexos(null);

        return registroDetalle;
    }




    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesadosSir
     * @return
     * @throws I18NException
     */
    private List<Interesado> procesarInteresados(List<InteresadoSir> interesadosSir) throws I18NException{
        List<Interesado> interesados = new ArrayList<Interesado>();

        for (InteresadoSir interesadoSir : interesadosSir) {
            Interesado interesado = transformarInteresado(interesadoSir);

            if (interesadoSir.getRepresentante()) {

                Interesado representante = transformarRepresentante(interesadoSir);
                representante.setIsRepresentante(true);
                representante.setRepresentado(interesado);
                interesado.setRepresentante(representante);

                interesados.add(interesado);
                interesados.add(representante);
            }else{
                interesados.add(interesado);
            }


        }
        return interesados;
    }

    /**
     * Transforma un {@link InteresadoSir} en un {@link Interesado}
     * @param interesadoSir
     * @return Interesado de tipo {@link Interesado}
     * @throws I18NException
     */
    private Interesado transformarInteresado(InteresadoSir interesadoSir) throws I18NException{

        Interesado interesado = new Interesado();
        interesado.setId((long) (Math.random() * 10000));
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if (StringUtils.isEmpty(interesadoSir.getRazonSocialInteresado())) {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotEmpty(interesadoSir.getRazonSocialInteresado())) {
            interesado.setRazonSocial(interesadoSir.getRazonSocialInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getNombreInteresado())) {
            interesado.setNombre(interesadoSir.getNombreInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getPrimerApellidoInteresado())) {
            interesado.setApellido1(interesadoSir.getPrimerApellidoInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getSegundoApellidoInteresado())) {
            interesado.setApellido2(interesadoSir.getSegundoApellidoInteresado());
        }
        if (interesadoSir.getTipoDocumentoIdentificacionInteresado() != null) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesadoSir.getTipoDocumentoIdentificacionInteresado().charAt(0)));
        }
        if (StringUtils.isNotEmpty(interesadoSir.getDocumentoIdentificacionInteresado())) {
            interesado.setDocumento(interesadoSir.getDocumentoIdentificacionInteresado());
        }

        if (StringUtils.isNotEmpty(interesadoSir.getCodigoPaisInteresado())) {
            try {
                interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoPaisInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoProvinciaInteresado())) {
            try {
                interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoMunicipioInteresado())) {
            try {
                interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(interesadoSir.getCodigoMunicipioInteresado()), Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getDireccionInteresado())) {
            interesado.setDireccion(interesadoSir.getDireccionInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoPostalInteresado())) {
            interesado.setCp(interesadoSir.getCodigoPostalInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCorreoElectronicoInteresado())) {
            interesado.setEmail(interesadoSir.getCorreoElectronicoInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getTelefonoInteresado())) {
            interesado.setTelefono(interesadoSir.getTelefonoInteresado());
        }

        if (interesadoSir.getCanalPreferenteComunicacionInteresado() != null) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(interesadoSir.getCanalPreferenteComunicacionInteresado()));
        }
        if (StringUtils.isNotEmpty(interesadoSir.getObservaciones())) {
            interesado.setObservaciones(interesadoSir.getObservaciones());
        }

        //SICRES4 campos nuevos
        interesado.setAvisoNotificacionSMS(interesadoSir.getAvisoNotificacionSMSInteresado());
        interesado.setAvisoCorreoElectronico(interesadoSir.getAvisoCorreoElectronicoInteresado());
        interesado.setCodDirectoriosUnificados(interesadoSir.getCodigoDirectorioUnificadosInteresado());
        interesado.setReceptorNotificaciones(interesadoSir.getReceptorNotificacionesInteresado());
        interesado.setTelefonoMovil(interesadoSir.getTelefonoMovilInteresado());

        return interesado;

    }


    /** Transforma un {@link InteresadoSir} en un {@link Interesado}
     *
     * @param representanteSir
     * @return Interesado de tipo {@link Interesado}
     */
    private Interesado transformarRepresentante(InteresadoSir representanteSir) {

        Interesado representante = new Interesado();
        representante.setId((long) (Math.random() * 10000));
        representante.setIsRepresentante(true);

        // Averiguamos que tipo es el Representante
        if (StringUtils.isEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setRazonSocial(representanteSir.getRazonSocialRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getNombreRepresentante())) {
            representante.setNombre(representanteSir.getNombreRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getPrimerApellidoRepresentante())) {
            representante.setApellido1(representanteSir.getPrimerApellidoRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getSegundoApellidoRepresentante())) {
            representante.setApellido2(representanteSir.getSegundoApellidoRepresentante());
        }
        if (representanteSir.getTipoDocumentoIdentificacionRepresentante() != null) {
            representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(representanteSir.getTipoDocumentoIdentificacionRepresentante().charAt(0)));
        }
        if (StringUtils.isNotEmpty(representanteSir.getDocumentoIdentificacionRepresentante())) {
            representante.setDocumento(representanteSir.getDocumentoIdentificacionRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoPaisRepresentante())) {
            try {
                representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoPaisRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoProvinciaRepresentante())) {
            try {
                representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoMunicipioRepresentante())) {
            try {
                representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(representanteSir.getCodigoMunicipioRepresentante()), Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getDireccionRepresentante())) {
            representante.setDireccion(representanteSir.getDireccionRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoPostalRepresentante())) {
            representante.setCp(representanteSir.getCodigoPostalRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCorreoElectronicoRepresentante())) {
            representante.setEmail(representanteSir.getCorreoElectronicoRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getTelefonoRepresentante())) {
            representante.setTelefono(representanteSir.getTelefonoRepresentante());
        }
        if (representanteSir.getCanalPreferenteComunicacionRepresentante() != null) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(representanteSir.getCanalPreferenteComunicacionRepresentante()));
        }
        if (StringUtils.isNotEmpty(representanteSir.getObservaciones())) {
            representante.setObservaciones(representanteSir.getObservaciones());
        }

        //SICRES4 campos nuevos
        representante.setAvisoNotificacionSMS(representanteSir.getAvisoNotificacionSMSRepresentante());
        representante.setAvisoCorreoElectronico(representanteSir.getAvisoCorreoElectronicoRepresentante());
        representante.setCodDirectoriosUnificados(representanteSir.getCodigoDirectorioUnificadosRepresentante());
        representante.setReceptorNotificaciones(representanteSir.getReceptorNotificacionesRepresentante());
        representante.setTelefonoMovil(representanteSir.getTelefonoMovilRepresentante());

        return representante;

    }


    /**
     * Método que comprueba si el tipoMime es aceptado por SIR, si no lo es devuelve null
     *
     * @param tipoMime
     * @return
     */
    private String tipoMimeAceptadoPorSir(String tipoMime) {

        if (tipoMime.length() <= ANEXO_TIPOMIME_MAXLENGTH_SIR && Arrays.asList(TIPOS_MIME_ACEPTADO_SIR).contains(tipoMime)) {
            return tipoMime;
        } else {
            return null;
        }

    }


    //PARTE NUEVA MARILEN ASIENTOBEAN
    @Override
    public RegistroSir crearRegistroSir(AsientoBean asientoBean, Entidad entidad) throws Exception {

        RegistroSir registroSir = LibSirUtils.transformarAsientoBean(asientoBean, entidad);
        registroSir.setEstado(EstadoRegistroSir.RECIBIDO);

        try {

            // En caso de recepción, le asignamos la entidad a la que va dirigida
            if (registroSir.getEntidad() == null) {
                registroSir.setEntidad(entidad);
            }

            registroSir = persist(registroSir);

            //Guardamos los Metadatos
            Set<MetadatoRegistroSir> metadatosRegistroSir = registroSir.getMetadatosRegistroSir();
            if (metadatosRegistroSir != null && metadatosRegistroSir.size() > 0) {
                for (MetadatoRegistroSir metadatoRegistroSir : metadatosRegistroSir) {
                    metadatoRegistroSir.setRegistroSir(registroSir);
                    metadatoRegistroSirEjb.persist(metadatoRegistroSir);
                }
            }

            // Guardamos los Interesados
            List<InteresadoSir> interesadosSir = registroSir.getInteresados();
            if (interesadosSir != null && interesadosSir.size() > 0) {
                for (InteresadoSir interesadoSir : interesadosSir) {
                    interesadoSir.setRegistroSir(registroSir);
                    interesadoSirEjb.guardarInteresadoSir(interesadoSir);
                }
            }

            // Guardamos los Anexos
            List<AnexoSir> anexosSir = registroSir.getAnexos();
            if (anexosSir != null && anexosSir.size() > 0) {
                for (AnexoSir anexoSir : anexosSir) {
                    anexoSir.setRegistroSir(registroSir);
                    anexoSirEjb.persist(anexoSir);

                    //guardamos los metadatos del anexo SIR
                    Set<MetadatoAnexoSir> metadatosAnexoSir = anexoSir.getMetadatosAnexos();
                    log.info("METADATOS ANEXOS SIR" + anexoSir.getMetadatosAnexos().size());
                    if (metadatosAnexoSir != null && metadatosAnexoSir.size() > 0) {
                        for (MetadatoAnexoSir metadatoAnexoSir : metadatosAnexoSir) {
                            metadatoAnexoSir.setAnexoSir(anexoSir);
                            metadatoAnexoSirEjb.persist(metadatoAnexoSir);
                        }
                    }
                }
            }
            em.flush();

            // Creamos la TrazabilidadSir
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
            trazabilidadSir.setAplicacion(registroSir.getAplicacion());
            trazabilidadSir.setNombreUsuario(registroSir.getNombreUsuario());
            trazabilidadSir.setContactoUsuario(registroSir.getContactoUsuario());
            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
            trazabilidadSirEjb.persist(trazabilidadSir);

        } catch (Exception e) {
            log.info("Error al crear el RegistroSir:");
            e.printStackTrace();
            /*for (AnexoSir anexoSir : registroSir.getAnexos()) {
                ArchivoManager am = new ArchivoManager(anexoSir.getAnexo(), archivoEjb);
                am.processErrorArchivosWithoutThrowException();
                log.info("Eliminamos los posibles archivos creados: " + anexoSir.getAnexo().getId());
            }*/
            throw e;
        }

        return registroSir;
    }




    /**
     * @param registroSir
     * @return
     * @throws I18NException
     */
    private List<AnexoFull> procesarAnexosRFU(RegistroSir registroSir) throws I18NException, ParseException, InterException {

        List<AnexoFull> anexos = new ArrayList<>();

        for (AnexoSir anexoSir : registroSir.getAnexos()) {
            anexos.add(transformarAnexoRFU(anexoSir, registroSir.getEntidad().getId(), anexoSir.getRegistroSir().getAplicacion(), registroSir.getIdentificadorIntercambio()));
        }

        return anexos;
    }

    private AnexoFull transformarAnexoRFU(AnexoSir anexoSir, Long idEntidad, String aplicacion, String idIntercambio) throws I18NException, ParseException, InterException {

        AnexoFull anexoFull = new AnexoFull();
        Anexo anexo = new Anexo(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

        //TODO Temporal hasta que se acepten todos los registros sir pendientes con anexos que tienen caracteres prohibidos
        anexo.setTitulo(es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu(anexoSir.getNombreFichero()));

        // Tipo Documento
        if (anexoSir.getTipoDocumento() != null) {
            anexo.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento()));

            // Si es un Documento técnico, ponemos el Origen a ADMINSITRACIÓN
            if (anexoSir.getTipoDocumento().equals(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO_SICRES)) {
                anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
            }
        }
        anexo.setObservaciones(anexoSir.getObservaciones());


        // SICRES 4 Campos NTI vienen informados como metadatos
        for (MetadatoAnexoSir metadatoAnexoSir : anexoSir.getMetadatosAnexos()) {
            if (metadatoAnexoSir.getCampo().equals("origenCiudadanoAdministracion")) {
                anexo.setOrigenCiudadanoAdmin(Integer.valueOf(metadatoAnexoSir.getValor()));
            }
            if (metadatoAnexoSir.getCampo().equals("tipoDocumental")) {
                anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad(metadatoAnexoSir.getValor(), idEntidad));
            }
            if (metadatoAnexoSir.getCampo().equals("fechaCaptura")) {
                SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_SICRES4);
                anexo.setFechaCaptura(formatter.parse(metadatoAnexoSir.getValor()));
            }
            //TODO VERSIONNTI

        }

        //SICRES4 campos nuevos
        anexo.setResumen(anexoSir.getResumen());
        anexo.setCodigoFormulario(anexoSir.getCodigoFormulario());
        anexo.setEndpointRFU(anexoSir.getUrlRepositorio());

        //METADATOS
        Set<MetadatoAnexo> metadatos = anexoSir.getMetadatosAnexos().stream()
                .map(metadato -> new MetadatoAnexo(metadato.getTipo(), metadato.getCampo(), metadato.getValor()) {
                }).collect(Collectors.toSet());

        anexo.setMetadatosAnexos(metadatos);

        //LIBSIR
        if (anexoSir.getTipoFirma() != null) {
            anexo.setModoFirma(MODO_FIRMA_ANEXO_ATTACHED);
            SignatureCustody sc = getSignatureCustodyRFU(anexoSir, idIntercambio);
            anexoFull.setDocumentoCustody(null);
            anexoFull.setSignatureCustody(sc);
            anexoFull.setAnexo(anexo);
        } else {
            anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
            DocumentCustody dc = getDocumentCustodyRFU(anexoSir, idIntercambio);
            anexoFull.setAnexo(anexo);
            anexoFull.setDocumentoCustody(dc);
        }

        return anexoFull;

    }

    /**
     * @param anexoSir
     * @return
     * @throws I18NException
     */
    private DocumentCustody getDocumentCustodyRFU(AnexoSir anexoSir, String idIntercambio) throws InterException {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }

        DocumentCustody dc = null;
        if (anexoSir.getAnexo() != null) {
            dc = new DocumentCustody();
            dc.setData(consultaService.getDocEniDescargadoIdFicheroYCdIntercambio(anexoSir.getIdentificadorFichero(), idIntercambio));
            dc.setMime(anexoSir.getTipoMIME());
            dc.setName(anexoSir.getNombreFichero());
        }
        return dc;
    }


    /**
     * @param anexoSir
     * @return
     * @throws I18NException
     */
    private SignatureCustody getSignatureCustodyRFU(AnexoSir anexoSir, String idIntercambio) throws InterException {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }

        SignatureCustody sc = null;
        sc = new SignatureCustody();

        //obtenemos el contenido a través del método de LIBSIR
        byte[] anexoData = consultaService.getDocEniDescargadoIdFicheroYCdIntercambio(anexoSir.getIdentificadorFichero(), idIntercambio);
        sc.setData(anexoData);

        if (Base64.isBase64(anexoData)) {
            log.info("Entramos en decodificar base64");
            anexoData = Base64.decodeBase64(anexoData);
        }
        sc.setData(anexoData);
        sc.setMime(anexoSir.getTipoMIME());
        sc.setName(anexoSir.getNombreFichero());
        sc.setAttachedDocument(null);
        sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

        return sc;
    }


}