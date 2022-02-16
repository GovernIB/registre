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
import net.java.xades.security.xml.XMLSignatureElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.utils.cxf.CXFUtils;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */

@Stateless(name = "RegistroSirEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RegistroSirBean extends BaseEjbJPA<RegistroSir, Long> implements RegistroSirLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private ArchivoLocal archivoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;


    @Override
    public List<Long> getUltimosPendientesProcesarERTE(EstadoRegistroSir estado, String oficinaSir, Date fechaInicio, Date fechaFin, String aplicacion, Integer total) throws Exception{

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
    public RegistroSir getReference(Long id) throws Exception {

        return em.getReference(RegistroSir.class, id);
    }

    @Override
    public RegistroSir findById(Long id) throws Exception {

        RegistroSir registroSir = em.find(RegistroSir.class, id);
        Hibernate.initialize(registroSir.getAnexos());
        Hibernate.initialize(registroSir.getInteresados());

        return registroSir;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getAll() throws Exception {

        return em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroSir.id) from RegistroSir as registroSir");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception {

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

    @Override
    public RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws Exception{

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
    public RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio, Entidad entidad) throws Exception{

        RegistroSir registroSir = transformarFicheroIntercambio(ficheroIntercambio, entidad.getId());
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
    public void eliminarRegistroSir(RegistroSir registroSir) throws Exception{

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
    public void marcarEliminado(RegistroSir registroSir, UsuarioEntidad usuario, String observaciones) throws Exception{

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
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSir registroSir, String oficinaSir, String estado, String entidad) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroSir from RegistroSir as registroSir ");

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(oficinaSir)){
            where.add(" (registroSir.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);
        }

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(entidad)){
            where.add(" (registroSir.entidad.codigoDir3 = :entidad) "); parametros.put("entidad",entidad);
        }

        if (registroSir.getResumen() != null && registroSir.getResumen().length() > 0) {
            where.add(DataBaseUtils.like("registroSir.resumen", "resumen", parametros, registroSir.getResumen()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getIdentificadorIntercambio())) {
            where.add(DataBaseUtils.like("registroSir.identificadorIntercambio", "identificadorIntercambio", parametros, registroSir.getIdentificadorIntercambio()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getNumeroRegistro())) {
            where.add(DataBaseUtils.like("registroSir.numeroRegistro", "numeroRegistro", parametros, registroSir.getNumeroRegistro()));
        }

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" registroSir.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
        }

        if (registroSir.getTipoRegistro() != null) {
            where.add(" registroSir.tipoRegistro = :tipoRegistro "); parametros.put("tipoRegistro", registroSir.getTipoRegistro());
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getAplicacion())) {
            where.add(DataBaseUtils.like("registroSir.aplicacion", "aplicacion", parametros, registroSir.getAplicacion()));
        }

        // Intervalo fechas
        where.add(" (registroSir.fechaRecepcion >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSir.fechaRecepcion <= :fechaFin) "); parametros.put("fechaFin", fechaFin);

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
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append(" order by registroSir.fechaRecepcion desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append("order by registroSir.fechaRecepcion desc");
            q = em.createQuery(query.toString());
        }


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

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public Paginacion getRegistrosEstado(Integer pageNumber, String oficinaSir, String estado) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroSir from RegistroSir as registroSir ");

        where.add(" (registroSir.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" registroSir.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
        }

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
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append(" order by registroSir.fechaRecepcion");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append("order by registroSir.fechaRecepcion");
            q = em.createQuery(query.toString());
        }


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

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws Exception{

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
    public Long getPendientesProcesarCount(String oficinaSir) throws Exception{

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
    public void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws Exception {

        Query q = em.createQuery("update RegistroSir set estado=:estado where id = :idRegistroSir");
        q.setParameter("estado", estado);
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

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
    public RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio, Long idEntidad)throws Exception{

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
                    registroSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), RegwebConstantes.OFICINA));
                }

                registroSir.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (StringUtils.isNotEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                } else {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), RegwebConstantes.UNIDAD));
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
                    registroSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),de_Destino.getCodigo_Entidad_Registral_Destino(), RegwebConstantes.OFICINA));
                }

                if (StringUtils.isNotEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    registroSir.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (StringUtils.isNotEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        registroSir.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        registroSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),de_Destino.getCodigo_Unidad_Tramitacion_Destino(), RegwebConstantes.UNIDAD));
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
                    registroSir.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad),de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), RegwebConstantes.OFICINA));
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
                            registroSir.getInteresados().add(crearInteresadoJuridico(ficheroIntercambio, idEntidad));

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

                        anexo.setNombreFichero(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosArxiu(de_Anexo.getNombre_Fichero_Anexado(),'_'));
                        anexo.setIdentificadorFichero(de_Anexo.getIdentificador_Fichero());
                        anexo.setIdentificadorDocumentoFirmado(de_Anexo.getIdentificador_Documento_Firmado());
                        anexo.setCertificado(Base64.encodeBase64String(de_Anexo.getCertificado()));
                        anexo.setFirma(Base64.encodeBase64String(de_Anexo.getFirma_Documento()));
                        anexo.setTimestamp(Base64.encodeBase64String(de_Anexo.getTimeStamp()));
                        anexo.setValidacionOCSPCertificado(Base64.encodeBase64String(de_Anexo.getValidacion_OCSP_Certificado()));
                        anexo.setHash(Base64.encodeBase64String(de_Anexo.getHash()));
                        //Si el tipo mime es null, se obtiene de la extensión del fichero
//                        if (de_Anexo.getTipo_MIME() == null || de_Anexo.getTipo_MIME().isEmpty()) {
//                            String mime = MimeTypeUtils.getMimeTypeFileName(de_Anexo.getNombre_Fichero_Anexado());
//                            if(mime.length() <= 20){
//                                anexo.setTipoMIME(mime);
//                            }
//                        } else {
//                            anexo.setTipoMIME(de_Anexo.getTipo_MIME());
//                        }

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
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException {

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
        registroSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle,registroEntrada.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

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
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException{

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
        registroSir.setCodigoUnidadTramitacionDestino(obtenerCodigoUnidadTramitacionDestino(registroDetalle));
        String destinoExternoDecodificacion = obtenerDenominacionUnidadTramitacionDestino(registroDetalle);
        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(destinoExternoDecodificacion)) {
            registroSir.setDecodificacionUnidadTramitacionDestino(destinoExternoDecodificacion);
        }else{
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
        registroSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

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
    public List<Long> getEnviadosSinAck(Long idEntidad) throws Exception{

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
    public List<Long> getEnviadosConError(Long idEntidad) throws Exception{

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
    public void reiniciarIntentos(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("update RegistroSir set numeroReintentos=0 where id = :idRegistroSir");
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }

    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesados
     * @return
     * @throws Exception
     */
    private List<InteresadoSir> procesarInteresadosSir(List<Interesado> interesados) throws Exception{
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
     * @throws Exception
     */
    private InteresadoSir transformarInteresadoSir(Interesado interesado, Interesado representante) throws Exception{

        InteresadoSir interesadoSir = new InteresadoSir();

        if (interesado.getTipoDocumentoIdentificacion() != null) {
            interesadoSir.setTipoDocumentoIdentificacionInteresado(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
        }

        if (StringUtils.isNotEmpty(interesado.getDocumento())) {
            interesadoSir.setDocumentoIdentificacionInteresado(interesado.getDocumento());
        }

        if (StringUtils.isNotEmpty(interesado.getRazonSocial())) {
            interesadoSir.setRazonSocialInteresado(interesado.getRazonSocial());
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
    private List<AnexoSir> transformarAnexosSir(List<AnexoFull> anexosFull, String identificadorIntercambio) throws Exception{

        List<AnexoSir> anexosSir = new ArrayList<AnexoSir>();
        int secuencia = 0;

        for(AnexoFull anexoFull:anexosFull){

            final int modoFirma = anexoFull.getAnexo().getModoFirma();
            Anexo anexo = anexoFull.getAnexo();
            AnexoSir anexoSir;

            switch (modoFirma){

                case MODO_FIRMA_ANEXO_ATTACHED:

                    SignatureCustody sc = anexoFull.getSignatureCustody();

                    String identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
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

                    identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, dc.getName());
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

                    String identificador_fichero_FIRMA = generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
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
        anexoSir.setNombreFichero(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosSIR(nombreFichero, '_'));

        anexoSir.setIdentificadorFichero(identificadorFichero);

        if(validezDocumento != null){
            anexoSir.setValidezDocumento(validezDocumento);
        }
        anexoSir.setTipoDocumento(tipoDocumento);
        if(certificado != null){
            anexoSir.setCertificado(Base64.encodeBase64String(certificado));
        }
        if(firma != null){
            anexoSir.setFirma(firma);
        }
        if(timeStamp != null){
            anexoSir.setTimestamp(Base64.encodeBase64String(timeStamp));
        }
        if(validacionOCSPCertificado != null){
            anexoSir.setValidacionOCSPCertificado(Base64.encodeBase64String(validacionOCSPCertificado));
        }
        anexoSir.setHash(Base64.encodeBase64String(hash));
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
     * Metodo que genera identificador de anxso según el patron
     * identificadorIntercambio_01_secuencia.extension
     * donde secuencia es cadena que repesenta secuencia en formato 0001 (leftpading con 0 y máximo de 4 caracteres)
     * donde extesion es la extension del anexo
     *
     * @param identificadorIntercambio
     * @param secuencia
     * @param fileName
     * @return
     */
    protected String generateIdentificadorFichero(String identificadorIntercambio, int secuencia, String fileName) {

        return identificadorIntercambio +
                "_01_" +
                StringUtils.leftPad(
                        String.valueOf(secuencia), 4, "0") +
                "." + getExtension(fileName);
    }

    /**
     * Obtiene la Extensión de un Fichero a partir de su nombre
     *
     * @param nombreFichero
     * @return extensión del fichero
     */
    private String getExtension(String nombreFichero) {
        String extension = "";

        int i = nombreFichero.lastIndexOf('.');
        if (i > 0) {
            extension = nombreFichero.substring(i + 1);
        }

        return extension;
    }

    /**
     * Obtiene el código Oficina de Origen dependiendo de si es interna o externa
     *
     * @param registroDetalle
     * @param codigoOficia
     * @return
     * @throws Exception
     */
    private String obtenerCodigoOficinaOrigen(RegistroDetalle registroDetalle, String codigoOficia) {
        String codOficinaOrigen;

        if ((registroDetalle.getOficinaOrigenExternoCodigo() == null) && (registroDetalle.getOficinaOrigen() == null)) {
            codOficinaOrigen = codigoOficia;
        } else if (registroDetalle.getOficinaOrigenExternoCodigo() != null) {
            codOficinaOrigen = registroDetalle.getOficinaOrigenExternoCodigo();
        } else {
            codOficinaOrigen = registroDetalle.getOficinaOrigen().getCodigo();
        }

        return codOficinaOrigen;
    }

    /**
     * Obtiene el denominación Oficina de Origen dependiendo de si es interna o externa
     *
     * @param registroDetalle
     * @param denominacionOficia
     * @return
     * @throws Exception
     */
    private String obtenerDenominacionOficinaOrigen(RegistroDetalle registroDetalle, String denominacionOficia) {
        String denominacionOficinaOrigen;

        if ((registroDetalle.getOficinaOrigenExternoCodigo() == null) && (registroDetalle.getOficinaOrigen() == null)) {
            denominacionOficinaOrigen = denominacionOficia;
        } else if (registroDetalle.getOficinaOrigenExternoCodigo() != null) {
            denominacionOficinaOrigen = registroDetalle.getOficinaOrigenExternoDenominacion();
        } else {
            denominacionOficinaOrigen = registroDetalle.getOficinaOrigen().getDenominacion();
        }

        return denominacionOficinaOrigen;
    }

    /**
     *
     * @param registroDetalle
     * @return
     */
    private String obtenerCodigoUnidadTramitacionDestino(RegistroDetalle registroDetalle){

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getCodigoDir3();
            }
        }

        return null;
    }

    /**
     *
     * @param registroDetalle
     * @return
     */
    private String obtenerDenominacionUnidadTramitacionDestino(RegistroDetalle registroDetalle){

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getRazonSocial();
            }
        }

        return null;
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
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroEntrada aceptarRegistroSirEntrada(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino)
            throws Exception, I18NException, I18NValidationException {

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
        List<AnexoFull> anexosFull = procesarAnexos(registroSir, camposNTIs);

        // Registramos el Registro Entrada
        registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario,interesados,anexosFull, true);


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
     * @param registroSir
     * @param idIdioma
     * @return
     * @throws Exception
     */
    private RegistroDetalle getRegistroDetalle(RegistroSir registroSir, Long idIdioma) throws Exception{

        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroDetalle.setRecibidoSir(true);
        registroDetalle.setPresencial(false);
        registroDetalle.setExtracto(registroSir.getResumen());
        registroDetalle.setTipoDocumentacionFisica(Long.valueOf(registroSir.getDocumentacionFisica()));
        registroDetalle.setIdioma(idIdioma);
       // registroDetalle.setTipoAsunto(new TipoAsunto(idTipoAsunto));
        registroDetalle.setCodigoAsunto(null);

        if(registroSir.getTipoTransporte() != null){
            registroDetalle.setTransporte(Long.valueOf(registroSir.getTipoTransporte()));
        }
        if(StringUtils.isNotEmpty(registroSir.getNumeroTransporte())){
            registroDetalle.setNumeroTransporte(registroSir.getNumeroTransporte());
        }
        if(StringUtils.isNotEmpty(registroSir.getObservacionesApunte())){
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
     * @throws Exception
     */
    private List<Interesado> procesarInteresados(List<InteresadoSir> interesadosSir) throws Exception{
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
     * @throws Exception
     */
    private Interesado transformarInteresado(InteresadoSir interesadoSir) throws Exception{

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
        if (StringUtils.isNotEmpty(interesadoSir.getDireccionElectronicaHabilitadaInteresado())) {
            interesado.setDireccionElectronica(interesadoSir.getDireccionElectronicaHabilitadaInteresado());
        }
        if (interesadoSir.getCanalPreferenteComunicacionInteresado() != null) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(interesadoSir.getCanalPreferenteComunicacionInteresado()));
        }
        if (StringUtils.isNotEmpty(interesadoSir.getObservaciones())) {
            interesado.setObservaciones(interesadoSir.getObservaciones());
        }

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
        if (StringUtils.isNotEmpty(representanteSir.getDireccionElectronicaHabilitadaRepresentante())) {
            representante.setDireccionElectronica(representanteSir.getDireccionElectronicaHabilitadaRepresentante());
        }
        if (representanteSir.getCanalPreferenteComunicacionRepresentante() != null) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(representanteSir.getCanalPreferenteComunicacionRepresentante()));
        }
        if (StringUtils.isNotEmpty(representanteSir.getObservaciones())) {
            representante.setObservaciones(representanteSir.getObservaciones());
        }

        return representante;

    }



    /**
     *
     * @param registroSir
     * @param camposNTIs representa la lista de anexos del RegistroSir en los que el usuario ha especificado
     *                          los valores de los campos NTI no informados por SICRES (validez Documento, origen, Tipo Documental)
     * @return
     * @throws Exception
     */
    private List<AnexoFull> procesarAnexos(RegistroSir registroSir, List<CamposNTI> camposNTIs) throws Exception {

        HashMap<String,AnexoFull> anexosProcesados = new HashMap<String, AnexoFull>();

        // Procesamos los Documentos con firma Attached o sin firma
        for (AnexoSir anexoSir : registroSir.getAnexos()) {
            for (CamposNTI cnti : camposNTIs) {
                if (anexoSir.getId().equals(cnti.getId())) {
                    transformarAnexoDocumento(anexoSir, registroSir.getEntidad().getId(), cnti, anexosProcesados,anexoSir.getRegistroSir().getAplicacion());
                }
            }
        }

        // Procesamos las Firma detached
        for (AnexoSir anexoSir : registroSir.getAnexos()) {
            transformarAnexoFirmaDetached(anexoSir, anexosProcesados,registroSir.getEntidad().getId(), anexoSir.getRegistroSir().getAplicacion());
        }

        // Eliminam duplicats
        Set<AnexoFull> set = new HashSet<AnexoFull>(anexosProcesados.values());

        return new ArrayList<AnexoFull>(set);
    }

    /**
     * Transforma un {@link AnexoSir} en un {@link AnexoFull}
     * A partir de la clase AnexoSir transformamos a un AnexoFull para poder guardarlo en regweb3.
     * La particularidad de este método, es que se necesita pasar una lista de los anexos que se han procesado anteriormente
     * del AnexoSir que nos envian, ya que puede haber anexos que son firma de uno anteriormente procesado y lo necesitamos
     * para acabar de montar el anexo ya que para regweb3 el anexo y su firma van en el mismo AnexoFull.
     * Además ahora se pasa una lista de anexosSirRecibidos ya que para cada anexo el usuario debe escoger 3 campos que
     * pueden no venir informados en SICRES y son obligatorios en NTI.
     * Los campos en concreto son (validezDocumento, origen, tipo Documental)
     * @param anexoSir
     * @param idEntidad
     * @return AnexoFull tipo {@link AnexoFull}
     */
    private void transformarAnexoDocumento(AnexoSir anexoSir, Long idEntidad, CamposNTI camposNTI, HashMap<String,AnexoFull> anexosProcesados, String aplicacion) throws Exception {

        // Solo procesamos Documentos no firmados o firmados attached, no las firmas detached
        if(StringUtils.isEmpty(anexoSir.getIdentificadorDocumentoFirmado()) ||
                anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero())){

            AnexoFull anexoFull = new AnexoFull();
            Anexo anexo = new Anexo(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

            //TODO Temporal hata que se acepten todos los registros sir pendientes con anexos que tienen caracteres prohibidos
            anexo.setTitulo(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosArxiu(anexoSir.getNombreFichero(), '_'));

            // Validez Documento
            if (anexoSir.getValidezDocumento() != null) {
                //Transformamos de copia compulsada a copia_original = autèntica
                if(Long.valueOf(anexoSir.getValidezDocumento()).equals(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA)){
                    anexo.setValidezDocumento(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL);
                }else {
                    anexo.setValidezDocumento(Long.valueOf(anexoSir.getValidezDocumento()));
                }
            } else {//Campo NTI Cogemos la validez de documento indicada por el usuario
                if (camposNTI.getIdValidezDocumento() != null) {
                    anexo.setValidezDocumento(Long.valueOf(camposNTI.getIdValidezDocumento()));

                }else{ //Si no hay valor, por defecto "Copia"
                    anexo.setValidezDocumento(TIPOVALIDEZDOCUMENTO_COPIA);
                }
            }

            // Tipo Documento
            if (anexoSir.getTipoDocumento() != null) {
                anexo.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento()));

                // Si es un Documento técnico, ponemos el Origen a ADMINSITRACIÓN
                if(anexoSir.getTipoDocumento().equals(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO_SICRES)){
                    anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
                }
            }
            anexo.setObservaciones(anexoSir.getObservaciones());

            //Campo NTI no informados, asignamos lo que indica el usuario
            if (camposNTI.getIdOrigen() != null) {
                anexo.setOrigenCiudadanoAdmin(camposNTI.getIdOrigen().intValue());

            }else{ // Si no está informado, por defecto Ciudadano
                anexo.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
            }

            // Si el usuario no especifica el tipo Documental, por defecto se pone TD99 - Otros
            if (camposNTI.getIdTipoDocumental() == null || camposNTI.getIdTipoDocumental().equals("")) {
                anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
            }else{
                anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad(camposNTI.getIdTipoDocumental(), idEntidad));
            }

            if(anexoSir.getCertificado()!= null) {
                anexo.setCertificado(anexoSir.getCertificado().getBytes());
            }

            if (anexoSir.getFirma() != null) {
                anexo.setFirma(anexoSir.getFirma());

            }
            if (anexoSir.getTimestamp() != null) {
                anexo.setTimestamp(anexoSir.getTimestamp().getBytes());
            }

            if (anexoSir.getValidacionOCSPCertificado() != null) {
                anexo.setValidacionOCSPCertificado(anexoSir.getValidacionOCSPCertificado().getBytes());
            }

            if(anexoSir.getHash()!= null){
                anexo.setHash(anexoSir.getHash().getBytes());
            }

            DocumentCustody dc;
            SignatureCustody sc;
            // Si el IdentificadorDocumentoFirmado está informado
            if (es.caib.regweb3.utils.StringUtils.isNotEmpty(anexoSir.getIdentificadorDocumentoFirmado())) {

                // Si el IdentificadorDocumentoFirmado es igual al IdentificadorFichero, es una Firma Attached
                if(anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero())){
                    /** PARCHE ESPU*/
                    if(RegwebConstantes.APLICACION_SIR_ESPU.equals(aplicacion)){
                        //log.info("Documento con firma attached aplicación ESPU: " + anexoSir.getIdentificadorFichero());
                        //En este caso se guarda como un no firmado
                        anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
                        dc = getDocumentCustody(anexoSir);
                        anexoFull.setAnexo(anexo);
                        anexoFull.setDocumentoCustody(dc);
                    }else{
                        //log.info("Documento con firma attached: " + anexoSir.getIdentificadorFichero());
                        //Caso Firma Attached caso 5, se guarda el documento en signatureCustody, como lo especifica el API DE CUSTODIA(II)
                        anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
                        sc = getSignatureCustody(anexoSir, null, anexo.getModoFirma());
                        anexoFull.setDocumentoCustody(null);
                        anexoFull.setSignatureCustody(sc);
                        anexoFull.setAnexo(anexo);
                    }

                }

            } else { // El anexo no es firma de nadie
                //log.info("Documento sin firma: " + anexoSir.getIdentificadorFichero());
                anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);

                /** PARCHE GREG PROBLEMA: El campo  firma que se informa es más grande que 255 y al intentar hacer el insert peta por superar longitud
                 * De momento cortamos el campo, pero se debe informar a MADRID de este caso concreto */
                if (anexoSir.getFirma() != null) { // Anexo con Firma CSV
                    if(anexoSir.getFirma().length() >= 255) {
                        anexo.setCsv(null);
                    }else{
                        anexo.setCsv(anexoSir.getFirma());
                    }
                    //TODO Metadada a custodia pel csv.
                }
                dc = getDocumentCustody(anexoSir);
                anexoFull.setAnexo(anexo);
                anexoFull.setDocumentoCustody(dc);
            }

            anexosProcesados.put(anexoSir.getIdentificadorFichero(), anexoFull);

        }
    }


    /**
     * Transforma un {@link AnexoSir} en un {@link AnexoFull}
     * A partir de la clase AnexoSir transformamos a un AnexoFull para poder guardarlo en regweb3.
     * La particularidad de este método, es que se necesita pasar una lista de los anexos que se han procesado anteriormente
     * del AnexoSir que nos envian, ya que puede haber anexos que son firma de uno anteriormente procesado y lo necesitamos
     * para acabar de montar el anexo ya que para regweb3 el anexo y su firma van en el mismo AnexoFull.
     * Además ahora se pasa una lista de anexosSirRecibidos ya que para cada anexo el usuario debe escoger 3 campos que
     * pueden no venir informados en SICRES y son obligatorios en NTI.
     * Los campos en concreto son (validezDocumento, origen, tipo Documental)
     * @param anexoSir
     * @param anexosProcesados Lista de anexos procesados anteriores.
     * @return AnexoFull tipo {@link AnexoFull}
     */
    private void transformarAnexoFirmaDetached(AnexoSir anexoSir, Map<String, AnexoFull> anexosProcesados, Long idEntidad, String aplicacion) throws Exception {

        // En este método solo se procesan las firmas detached(aquellas que nos informan
        // con el identificador de documento firmado, que son firma de otro segmento anexo
        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(anexoSir.getIdentificadorDocumentoFirmado()) &&
                !anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero())) {

            log.info("Firma detached del documento: " + anexoSir.getIdentificadorDocumentoFirmado());

            AnexoFull anexoFirmado = anexosProcesados.get(anexoSir.getIdentificadorDocumentoFirmado());//obtenemos el anexo firmado previamente procesado
            if(anexoFirmado.getAnexo().getModoFirma() != MODO_FIRMA_ANEXO_ATTACHED) { // si la firma detached es de un firma attached, la descartamos


                byte[] anexoSirData = FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId());

                // CASO ORVE BASE64 Decodificamos previamente porque vienen las firmas codificadas en base64
                if (Base64.isBase64(anexoSirData)) {
                    anexoSirData = Base64.decodeBase64(anexoSirData);
                    anexoSir.setAnexoData(anexoSirData);
                }

                if (CXFUtils.isXMLFormat(anexoSirData)) { //Miramos si es un formato XML
                    //A pesar de que por identificador de documento firmado nos indican que es una firma detached, debemos
                    // averiguar si es una firma attached o detached  y esto nos lo indica el contenido del xml que nos envian.

                    /*******  COMENTAMOS TEMPORALMENTE MIRAR EL FORMATO DEL XADES YA QUE EN TODOS LOS CASOS SE ELIMINA EL XSIG, LO HACEMOS A RAIZ DEL PROBLEMA DE FORMATO DE GEISER CON EL XML CON CAPA <AFIRMA></AFIRMA>  ********/
                /*String format= getXAdESFormat(anexoSirData);

                if(SIGNFORMAT_EXPLICIT_DETACHED.equals(format)){// XADES Detached
                    //Obtenemos el anexo original cuya firma es la que estamos tratando, que ha sido previamente procesado
                    AnexoFull anexoFull = anexosProcesados.get(anexoSir.getIdentificadorDocumentoFirmado());

                    if(anexoFull.getSignatureCustody() == null) { // Es un documento sin firma(documento plano)
                        //Descartamos la firma xsig y lanzamos mensaje.
                        log.warn("FIRMA d'un anexoSir(document pla) ja que es tipus Xades Detached no suportada per ARXIU: només guardam document ");

                        // Descomentar aquest codi quan arxiu deixi guardar xsig (propuesta de toni, pero esta mas ordenado en el codigo de marilen
                        //CASO XADES DETACHED DE DOCUMENT PLA = CADES (AFEGIT MARILEN, CODI OPTIMITZAT, descomentar quan ARXIU guardi Xsig)
                        //Caso Firma Detached, caso 4, se guarda 1 anexo, con el doc original en documentCustody y la firma en SignatureCustody
                        *//*anexoFull.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED); // asignamos el modo de firma
                        SignatureCustody sc = getSignatureCustody(anexoSir, anexoFull.getDocumentoCustody(), anexoFull.getAnexo().getModoFirma()); //Asignamos la firma
                        anexoFull.setSignatureCustody(sc);*//*

                    }else{ //Es un documento firmado que además nos envian el xsig como detached.

                        log.warn("Descartat FIRMA d'un anexoSir(documentsignat) ja que es tipus Xades Detached no suportada per ARXIU: només guardam document ");

                        // Descomentar aquest codi quan arxiu deixi guardar xsig

                        //NO SOPORTADO POR MODELO CUSTODIA
                        //Obtenemos el documento firmado que está en signatureCustody porque ha sido procesado
                        // anteriormente.
                        *//*SignatureCustody scAntic = anexoFull.getSignatureCustody();

                        // Apunta a una signatura (attached o detached): collim la signatura com a doc detached
                        //Creamos un nuevo documentCustody para guardar el documento firmado
                        DocumentCustody  dc = new DocumentCustody(scAntic.getName(), scAntic.getMime(), scAntic.getData());

                        *//**//*Montamos el nuevo anexo
                            -(DocumentCustody = documento firmado)
                            -(signatureCustody = firma detached que estamos tratando)
                        *//**//*
                        AnexoFull anexoFullnou = new AnexoFull();
                        anexoFullnou.setDocumentoCustody(dc);

                        SignatureCustody sc = new SignatureCustody();
                        sc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
                        sc.setMime(anexoSir.getTipoMIME());
                        sc.setName(anexoSir.getNombreFichero());
                        anexoFullnou.setSignatureCustody(sc);

                        anexosProcesados.put(anexoSir.getIdentificadorFichero(),anexoFullnou );*//*

                    }

                }else{
                    // XADES attached
                    // CAS ORVE:  es una XADES attached pero AnexoSIR apunta a un DETACHED

                    // nomes ens serveix el contingut que ja s'ha donat d'alta: no feim res per que ARXIU no funciomna
                    //log.warn("AnexoSir-Detached amb firma tipus Xades attached (CAS ORVE)");

                    // Descomentar aquest codi quan arxiu deixi guardar xsig i si es vol guardar dins arxiu la firma de comunicació

                    //En este caso, nos mandan un xml attached, como detached. Por tanto lo guardaremos como una firma attached aislada
                    //Aquí perdemos el vinculo al documento del que nos indican que es su firma detached(lo cual no es cierto)
                    *//*SignatureCustody sc = new SignatureCustody();

                    sc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
                    sc.setMime(anexoSir.getTipoMIME());
                    sc.setName(anexoSir.getNombreFichero());
                    sc.setAttachedDocument(true);

                    //Montamos el nuevo anexoFull como attached, ya que es un xades attached
                    AnexoFull anexoFullnou = new AnexoFull();
                    Anexo anexoNou = new Anexo();
                    anexoFullnou.setSignatureCustody(sc);
                    anexoFullnou.setDocumentoCustody(null);
                    anexoNou.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);

                    //Debemos construir el anexo completo, porque es nuevo.
                    anexoNou.setTitulo(anexoSir.getNombreFichero());
                    anexoNou.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
                    anexoNou.setObservaciones(anexoSir.getObservaciones());
                    anexoNou.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
                    anexoNou.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
                    if(anexoSir.getTipoDocumento()!=null) {
                        anexoNou.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento()));
                    }
                    if(anexoSir.getCertificado()!= null) {
                        anexoNou.setCertificado(anexoSir.getCertificado().getBytes());
                    }
                    if (anexoSir.getFirma() != null) {
                        anexoNou.setFirma(anexoSir.getFirma().getBytes());
                    }
                    if (anexoSir.getTimestamp() != null) {
                        anexoNou.setTimestamp(anexoSir.getTimestamp().getBytes());
                    }
                    if (anexoSir.getValidacionOCSPCertificado() != null) {
                        anexoNou.setValidacionOCSPCertificado(anexoSir.getValidacionOCSPCertificado().getBytes());
                    }
                    if(anexoSir.getHash()!= null){
                        anexoNou.setHash(anexoSir.getHash().getBytes());
                    }
                    anexoFullnou.setAnexo(anexoNou);


                    anexosProcesados.put(anexoSir.getIdentificadorFichero(),anexoFullnou );*//*

                }*/
                } else {

                    // CADES

                    //Caso Firma Detached, caso 4, se guarda 1 anexo, con el doc original en documentCustody y la firma en SignatureCustody
                    anexoFirmado.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED); // asignamos el modo de firma
                    SignatureCustody sc = getSignatureCustody(anexoSir, anexoFirmado.getDocumentoCustody(), anexoFirmado.getAnexo().getModoFirma()); //Asignamos la firma
                    anexoFirmado.setSignatureCustody(sc);

                }

            }

        }

    }


    /**
     *
     * @param anexoSir
     * @return
     * @throws Exception
     */
    private DocumentCustody getDocumentCustody(AnexoSir anexoSir) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }
        DocumentCustody dc = null;
        if (anexoSir.getAnexo() != null) {
            dc = new DocumentCustody();
            dc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            dc.setMime(anexoSir.getTipoMIME());
            dc.setName(anexoSir.getNombreFichero());
        }
        return dc;
    }

    /**
     *
     * @param anexoSir
     * @param dc
     * @param modoFirma
     * @return
     * @throws Exception
     */
    private SignatureCustody getSignatureCustody(AnexoSir anexoSir, DocumentCustody dc,
                                                 int modoFirma) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }

        SignatureCustody sc = null;
        if (anexoSir.getAnexo() == null) {

            if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    || modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                String msg = "L'usuari ens indica que hi ha una firma a "+anexoSir.getIdentificadorFichero()+" i no ve (modoFirma = " + modoFirma + ")";
                log.info(msg, new Exception());
                throw new Exception(msg);
            }

        } else {

            if (modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    && modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                String msg = "L'usuari ens indica que NO hi ha una a "+anexoSir.getIdentificadorFichero()+" firma pero n'envia una"
                        + " (modoFirma = " + modoFirma + ")";
                log.error(msg, new Exception());
                throw new Exception(msg);
            }



            sc = new SignatureCustody();

            //Averiguamos si está en Base64 para decodificarlo y luego que se valide bien la firma.
            byte[] anexoData = FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId());
            if(Base64.isBase64(anexoData)){
                log.info("Entramos en decodificar base64");
                anexoData=Base64.decodeBase64(anexoData);
            };
            sc.setData(anexoData);
            sc.setMime(anexoSir.getTipoMIME());
            sc.setName(anexoSir.getNombreFichero());


            if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                // Document amb firma adjunta
                sc.setAttachedDocument(null);

                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

            } else if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                // Firma en document separat CAS 4
                if (dc == null) {
                    throw new Exception("Aquesta firma "+ anexoSir.getIdentificadorFichero() +"  requereix el document original"
                            + " i no s'ha enviat");
                }

                sc.setAttachedDocument(false);
                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
            }
        }
        return sc;
    }




    /** La firma està continguda dins del document: PADES, ODT, OOXML */
    public static final String SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED = "implicit_enveloped/attached";

    /** La firma conté al document: Xades ATTACHED */
    public static final String SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED = "implicit_enveloping/attached";

    /**
     * El documetn està forà de la firma: xades detached i cades detached
     */
    public static final String SIGNFORMAT_EXPLICIT_DETACHED = "explicit/detached";

    /**
     * Cas específic de Xades externally detached
     */
    public static final String SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED = "explicit/externally_detached";


    /**
     * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
     * @param signature
     * @return
     */
    private  String getXAdESFormat(byte[] signature) throws Exception {

        DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
        dBFactory.setNamespaceAware(true);

        Document eSignature = dBFactory.newDocumentBuilder().parse(
           new ByteArrayInputStream(signature));

        XMLSignature xmlSignature;
        String rootName = eSignature.getDocumentElement().getNodeName();
        if (rootName.equalsIgnoreCase("ds:Signature") || rootName.equals("ROOT_COSIGNATURES")) {
            //  "XAdES Enveloping"
            return SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
        }
        NodeList signatureNodeLs = eSignature.getElementsByTagName("ds:Manifest");
        if (signatureNodeLs.getLength() > 0) {
            //  "XAdES Externally Detached
            return SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED;
        }
        NodeList signsList = eSignature.getElementsByTagNameNS(
           "http://www.w3.org/2000/09/xmldsig#", "Signature");
        if (signsList.getLength() == 0) {
            throw new Exception("No te firmes");
        }
        Node signatureNode = signsList.item(0);
        try {
            xmlSignature = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
        } catch (MarshalException e) {
            throw new Exception("marshal exception: " + e.getMessage(), e);
        }
        List<?> references = xmlSignature.getSignedInfo().getReferences();
        for (int i = 0; i < references.size(); ++i) {
            if (!"".equals(((Reference) references.get(i)).getURI()))
                continue;
            //  "XAdES Enveloped"
            return SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
        }
        //  "XAdES Detached"
        return SIGNFORMAT_EXPLICIT_DETACHED;
    }

    /**
     * Método que comprueba si el tipoMime es aceptado por SIR, si no lo es devuelve null
     * @param tipoMime
     * @return
     */
    private String tipoMimeAceptadoPorSir(String tipoMime){

        if(tipoMime.length() <= ANEXO_TIPOMIME_MAXLENGTH_SIR && Arrays.asList(TIPOS_MIME_ACEPTADO_SIR).contains(tipoMime)){
            return tipoMime;
        }else{
            return null;
        }

    }

}