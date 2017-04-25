package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.*;
import es.caib.regweb3.persistence.utils.ArchivoManager;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.sir.core.schema.*;
import es.caib.regweb3.sir.core.schema.types.Documentacion_FisicaType;
import es.caib.regweb3.sir.core.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.schema.types.Tipo_RegistroType;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.MessageDigest;
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

@Stateless(name = "AsientoRegistralSirEJB")
@SecurityDomain("seycon")
public class AsientoRegistralSirBean extends BaseEjbJPA<AsientoRegistralSir, Long> implements AsientoRegistralSirLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private ArchivoLocal archivoEjb;


    @Override
    public AsientoRegistralSir getReference(Long id) throws Exception {

        return em.getReference(AsientoRegistralSir.class, id);
    }

    @Override
    public AsientoRegistralSir findById(Long id) throws Exception {

        return em.find(AsientoRegistralSir.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<AsientoRegistralSir> getAll() throws Exception {

        return em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir order by asientoRegistralSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(asientoRegistralSir.id) from AsientoRegistralSir as asientoRegistralSir");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<AsientoRegistralSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir order by asientoRegistralSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception {

        Query q = em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir where " +
                "asientoRegistralSir.identificadorIntercambio = :identificadorIntercambio and asientoRegistralSir.codigoEntidadRegistralOrigen = :codigoEntidadRegistralDestino");

        q.setParameter("identificadorIntercambio",identificadorIntercambio);
        q.setParameter("codigoEntidadRegistralDestino",codigoEntidadRegistralDestino);

        List<AsientoRegistralSir> asientoRegistralSir = q.getResultList();
        if(asientoRegistralSir.size() == 1){
            return asientoRegistralSir.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio) throws Exception{

        Query q = em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir where " +
                "asientoRegistralSir.identificadorIntercambio = :identificadorIntercambio");

        q.setParameter("identificadorIntercambio",identificadorIntercambio);

        List<AsientoRegistralSir> asientoRegistralSir = q.getResultList();
        if(asientoRegistralSir.size() == 1){
            return asientoRegistralSir.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public AsientoRegistralSir getAsientoRegistralConAnexos(Long idAsientoRegistralsir) throws Exception{

        AsientoRegistralSir asientoRegistralSir = findById(idAsientoRegistralsir);

        List<AnexoSir> anexosFull = new ArrayList<AnexoSir>();
        for (AnexoSir anexoSir : asientoRegistralSir.getAnexos()) {

            anexoSir.setAnexoData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            anexosFull.add(anexoSir);
        }

        asientoRegistralSir.setAnexos(anexosFull);

        return asientoRegistralSir;

    }

    @Override
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception{

        try{

            // En caso de recepción, le asignamos la entidad a la que va dirigida
            if(asientoRegistralSir.getEntidad() == null){
                Entidad entidad = new Entidad(oficinaEjb.obtenerEntidad(asientoRegistralSir.getCodigoEntidadRegistralDestino()));
                asientoRegistralSir.setEntidad(entidad);
            }

            asientoRegistralSir = persist(asientoRegistralSir);

            // Guardamos los Interesados
            if(asientoRegistralSir.getInteresados() != null && asientoRegistralSir.getInteresados().size() > 0){
                for(InteresadoSir interesadoSir: asientoRegistralSir.getInteresados()){
                    interesadoSir.setIdAsientoRegistralSir(asientoRegistralSir);

                    interesadoSirEjb.persist(interesadoSir);
                }
            }

            // Guardamos los Anexos
            if(asientoRegistralSir.getAnexos() != null && asientoRegistralSir.getAnexos().size() > 0){
                for(AnexoSir anexoSir: asientoRegistralSir.getAnexos()){
                    anexoSir.setIdAsientoRegistralSir(asientoRegistralSir);

                    anexoSirEjb.persist(anexoSir);
                }
            }
            em.flush();

        }catch (Exception e){
            log.info("Error al crear el AsientoRegistralSir, eliminamos los posibles anexos creados");
            for(AnexoSir anexoSir: asientoRegistralSir.getAnexos()){
                ArchivoManager am = new ArchivoManager(anexoSir.getAnexo(),archivoEjb);
                am.processError();
            }
            e.printStackTrace();
            throw e;
        }

        return asientoRegistralSir;
    }

    @Override
    public Boolean tieneAsientoRegistralSir(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select count(asientoRegistralSir.id) from AsientoRegistralSir as asientoRegistralSir where " +
                "asientoRegistralSir.codigoEntidadRegistralDestino = :codigoOficinaActiva");

        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);

        return (Long) q.getSingleResult() > 0;
    }

    public Paginacion busqueda(Integer pageNumber, Integer any, AsientoRegistralSir asientoRegistralSir, Set<String> organismos, String estado) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select asr from AsientoRegistralSir as asr ");

        /*if (organismos != null && organismos.size() > 0) {
            where.add(" asr.codigoUnidadTramitacionDestino in (:organismos) "); parametros.put("organismos",organismos);
        }*/

        if (asientoRegistralSir.getResumen() != null && asientoRegistralSir.getResumen().length() > 0) {
            where.add(DataBaseUtils.like("asr.resumen", "resumen", parametros, asientoRegistralSir.getResumen()));
        }

        if (asientoRegistralSir.getNumeroRegistro() != null && asientoRegistralSir.getNumeroRegistro().length() > 0) {
            where.add(DataBaseUtils.like("asr.numeroRegistro", "numeroRegistro", parametros, asientoRegistralSir.getNumeroRegistro()));
        }

        if (!StringUtils.isEmpty(estado)) {
            where.add(" asr.estado = :estado "); parametros.put("estado",EstadoAsientoRegistralSir.valueOf(estado));
        }

        if(any!= null){where.add(" year(asr.fechaRegistro) = :any "); parametros.put("any",any);}

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
            q2 = em.createQuery(query.toString().replaceAll("Select asr from AsientoRegistralSir as asr ", "Select count(asr.id) from AsientoRegistralSir as asr "));
            query.append(" order by asr.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select asr from AsientoRegistralSir as asr ", "Select count(asr.id) from AsientoRegistralSir as asr "));
            query.append("order by asr.id desc");
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

    public List<AsientoRegistralSir> getUltimosPendientesProcesar(Set<String> organismos, Integer total) throws Exception{

        Query q = em.createQuery("Select ars from AsientoRegistralSir as ars " +
                "where ars.codigoUnidadTramitacionDestino in (:organismos) and ars.estado = :idEstadoPreRegistro " +
                "order by ars.id desc");

        q.setMaxResults(total);
        q.setParameter("organismos", organismos);
        q.setParameter("idEstadoPreRegistro", EstadoAsientoRegistralSir.RECIBIDO);

        return  q.getResultList();
    }

    @Override
    public void modificarEstado(Long idAsientoRegistralSir, EstadoAsientoRegistralSir estado) throws Exception {

        Query q = em.createQuery("update AsientoRegistralSir set estado=:estado where id = :idAsientoRegistralSir");
        q.setParameter("estado", estado);
        q.setParameter("idAsientoRegistralSir", idAsientoRegistralSir);
        q.executeUpdate();

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> result = em.createQuery("Select distinct(a.id) from AsientoRegistralSir as a where a.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = result.size();

        if(result.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (result.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from AsientoRegistralSir where id in (:result) ").setParameter("result", subList).executeUpdate();
                result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from AsientoRegistralSir where id in (:result) ").setParameter("result", result).executeUpdate();
        }

        return total;

    }

    /**
     * Crea un AsientoRegistralSir, a partir del FicheroIntercambio.
     *
     * @return Información del asientoRegistralSir registral.
     */
    @Override
    public AsientoRegistralSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio)throws Exception{

        final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

        AsientoRegistralSir asientoRegistralSir = null;

        if (ficheroIntercambio.getFicheroIntercambio() != null) {

            asientoRegistralSir = new AsientoRegistralSir();
            asientoRegistralSir.setFechaRecepcion(new Date());

            // Segmento De_Origen_o_Remitente
            De_Origen_o_Remitente de_Origen_o_Remitente = ficheroIntercambio.getFicheroIntercambio().getDe_Origen_o_Remitente();
            if (de_Origen_o_Remitente != null) {

                asientoRegistralSir.setCodigoEntidadRegistralOrigen(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), RegwebConstantes.OFICINA));
                }

                asientoRegistralSir.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                    asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                } else {
                    asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), RegwebConstantes.UNIDAD));
                }


                asientoRegistralSir.setNumeroRegistro(de_Origen_o_Remitente.getNumero_Registro_Entrada());
                asientoRegistralSir.setTimestampRegistro(de_Origen_o_Remitente.getTimestamp_Entrada());

                String fechaRegistro = de_Origen_o_Remitente.getFecha_Hora_Entrada();
                if (StringUtils.isNotBlank(fechaRegistro)) {
                    try {
                        asientoRegistralSir.setFechaRegistro(SDF.parse(fechaRegistro));
                    } catch (ParseException e) {
                        log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                        throw new ValidacionException(Errores.ERROR_0037, e);
                    }
                }
            }

            // Segmento De_Destino
            De_Destino de_Destino = ficheroIntercambio.getFicheroIntercambio().getDe_Destino();
            if (de_Destino != null) {

                asientoRegistralSir.setCodigoEntidadRegistralDestino(de_Destino.getCodigo_Entidad_Registral_Destino());
                if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Entidad_Registral_Destino())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralDestino(de_Destino.getDecodificacion_Entidad_Registral_Destino());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Entidad_Registral_Destino(), RegwebConstantes.OFICINA));
                }

                if (!StringUtils.isEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    asientoRegistralSir.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Unidad_Tramitacion_Destino(), RegwebConstantes.UNIDAD));
                    }
                }

            }

            // Segmento De_Asunto de_Asunto
            De_Asunto de_Asunto = ficheroIntercambio.getFicheroIntercambio().getDe_Asunto();
            if (de_Asunto != null) {
                asientoRegistralSir.setResumen(de_Asunto.getResumen());
                asientoRegistralSir.setCodigoAsunto(de_Asunto.getCodigo_Asunto_Segun_Destino());
                asientoRegistralSir.setReferenciaExterna(de_Asunto.getReferencia_Externa());
                asientoRegistralSir.setNumeroExpediente(de_Asunto.getNumero_Expediente());
            }

            // Segmento De_Internos_Control
            De_Internos_Control de_Internos_Control = ficheroIntercambio.getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {

                asientoRegistralSir.setIdentificadorIntercambio(de_Internos_Control.getIdentificador_Intercambio());
                asientoRegistralSir.setAplicacion(de_Internos_Control.getAplicacion_Version_Emisora());
                asientoRegistralSir.setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                asientoRegistralSir.setDecodificacionTipoAnotacion(de_Internos_Control.getDescripcion_Tipo_Anotacion());
                asientoRegistralSir.setNumeroTransporte(de_Internos_Control.getNumero_Transporte_Entrada());
                asientoRegistralSir.setNombreUsuario(de_Internos_Control.getNombre_Usuario());
                asientoRegistralSir.setContactoUsuario(de_Internos_Control.getContacto_Usuario());
                asientoRegistralSir.setObservacionesApunte(de_Internos_Control.getObservaciones_Apunte());

                asientoRegistralSir.setCodigoEntidadRegistralInicio(de_Internos_Control.getCodigo_Entidad_Registral_Inicio());
                if (!StringUtils.isEmpty(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralInicio(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), RegwebConstantes.OFICINA));
                }


                // Tipo de transporte
                String tipoTransporte = de_Internos_Control.getTipo_Transporte_Entrada();
                if (StringUtils.isNotBlank(tipoTransporte)) {
                    asientoRegistralSir.setTipoTransporte(TipoTransporte.getTipoTransporteValue(tipoTransporte));
                }

                // Tipo de registro
                Tipo_RegistroType tipo_Registro = de_Internos_Control.getTipo_Registro();
                if ((tipo_Registro != null) && StringUtils.isNotBlank(tipo_Registro.value())) {
                    asientoRegistralSir.setTipoRegistro(TipoRegistro.getTipoRegistro(tipo_Registro.value()));
                }

                // Documentación física
                Documentacion_FisicaType documentacion_Fisica = de_Internos_Control.getDocumentacion_Fisica();
                if ((documentacion_Fisica != null) && StringUtils.isNotBlank(documentacion_Fisica.value())) {
                    asientoRegistralSir.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaValue(documentacion_Fisica.value()));
                }

                // Indicador de prueba
                Indicador_PruebaType indicadorPrueba = de_Internos_Control.getIndicador_Prueba();
                if ((indicadorPrueba != null) && StringUtils.isNotBlank(indicadorPrueba.value())){
                    asientoRegistralSir.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.value()));
                }

            }

            // Segmento De_Formulario_Generico
            De_Formulario_Generico de_Formulario_Generico = ficheroIntercambio.getFicheroIntercambio().getDe_Formulario_Generico();
            if (de_Formulario_Generico != null) {
                asientoRegistralSir.setExpone(de_Formulario_Generico.getExpone());
                asientoRegistralSir.setSolicita(de_Formulario_Generico.getSolicita());
            }

            // Segmento De_Interesado
            De_Interesado[] de_Interesados = ficheroIntercambio.getFicheroIntercambio().getDe_Interesado();
            if (ArrayUtils.isNotEmpty(de_Interesados)) {
                for (De_Interesado de_Interesado : de_Interesados) {
                    if (de_Interesado != null) {

                        // Si se trata de una Salida y no tiene Interesados
                        if(ficheroIntercambio.getTipoRegistro().equals(TipoRegistro.SALIDA) &&
                                StringUtils.isBlank(de_Interesado.getRazon_Social_Interesado())
                                && (StringUtils.isBlank(de_Interesado.getNombre_Interesado()) && StringUtils.isBlank(de_Interesado.getPrimer_Apellido_Interesado()))){

                            // Creamos uno a partir de la Entidad destino
                            asientoRegistralSir.getInteresados().add(crearInteresadoJuridico(ficheroIntercambio));

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

                            asientoRegistralSir.getInteresados().add(interesado);
                        }

                    }
                }
            }

            // Segmento De_Anexos
            De_Anexo[] de_Anexos = ficheroIntercambio.getFicheroIntercambio().getDe_Anexo();
            if (ArrayUtils.isNotEmpty(de_Anexos)) {
                for (De_Anexo de_Anexo : de_Anexos) {
                    if (de_Anexo != null) {
                        AnexoSir anexo = new AnexoSir();

                        anexo.setNombreFichero(de_Anexo.getNombre_Fichero_Anexado());
                        anexo.setIdentificadorFichero(de_Anexo.getIdentificador_Fichero());
                        anexo.setIdentificadorDocumentoFirmado(de_Anexo.getIdentificador_Documento_Firmado());
                        anexo.setCertificado(de_Anexo.getCertificado());
                        anexo.setFirma(de_Anexo.getFirma_Documento());
                        anexo.setTimestamp(de_Anexo.getTimeStamp());
                        anexo.setValidacionOCSPCertificado(de_Anexo.getValidacion_OCSP_Certificado());
                        anexo.setHash(de_Anexo.getHash());
                        //Si el tipo mime es null, se obtiene del nombre del fichero
                        if (de_Anexo.getTipo_MIME() == null || de_Anexo.getTipo_MIME().isEmpty()) {
                            anexo.setTipoMIME(MimeTypeUtils.getMimeTypeFileName(de_Anexo.getNombre_Fichero_Anexado()));
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
                            throw new ServiceException(Errores.ERROR_0045,e);
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

                        asientoRegistralSir.getAnexos().add(anexo);
                    }
                }
            }
        }

        return asientoRegistralSir;

    }

    /**
     * Transforma un {@link es.caib.regweb3.model.RegistroEntrada} en un {@link AsientoRegistralSir}
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public AsientoRegistralSir transformarRegistroEntrada(RegistroEntrada registroEntrada)
            throws Exception, I18NException {

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        AsientoRegistralSir asientoRegistralSir = new AsientoRegistralSir();

        asientoRegistralSir.setIndicadorPrueba(IndicadorPrueba.NORMAL);
        asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.PENDIENTE_ENVIO);
        asientoRegistralSir.setEntidad(registroEntrada.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        asientoRegistralSir.setCodigoEntidadRegistralOrigen(registroEntrada.getOficina().getCodigo());
        asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(registroEntrada.getOficina().getDenominacion());
        asientoRegistralSir.setNumeroRegistro(registroEntrada.getNumeroRegistroFormateado());
        asientoRegistralSir.setFechaRegistro(registroEntrada.getFecha());
        asientoRegistralSir.setCodigoUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getCodigo());
        asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        asientoRegistralSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        asientoRegistralSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        asientoRegistralSir.setCodigoUnidadTramitacionDestino(registroEntrada.getDestinoExternoCodigo());
        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(registroEntrada.getDestinoExternoDenominacion());

        // Segmento De_Asunto
        asientoRegistralSir.setResumen(registroDetalle.getExtracto());
        if(registroEntrada.getDestino() != null){
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            asientoRegistralSir.setCodigoAsunto(tra.getNombre());
        }
        asientoRegistralSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        asientoRegistralSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        asientoRegistralSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        asientoRegistralSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        asientoRegistralSir.setNombreUsuario(registroEntrada.getUsuario().getNombreCompleto());
        asientoRegistralSir.setContactoUsuario(registroEntrada.getUsuario().getUsuario().getEmail());
        asientoRegistralSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        asientoRegistralSir.setAplicacion(registroDetalle.getAplicacion());
        asientoRegistralSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        asientoRegistralSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        asientoRegistralSir.setTipoRegistro(TipoRegistro.ENTRADA);
        asientoRegistralSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        asientoRegistralSir.setObservacionesApunte(registroDetalle.getObservaciones());
        asientoRegistralSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle,registroEntrada.getOficina().getCodigo()));
        asientoRegistralSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        asientoRegistralSir.setExpone(registroDetalle.getExpone());
        asientoRegistralSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados
        asientoRegistralSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos
        asientoRegistralSir.setAnexos(procesarAnexosSir(registroDetalle.getAnexosFull(), asientoRegistralSir.getIdentificadorIntercambio()));

        return asientoRegistralSir;
    }

    /**
     * Transforma un {@link es.caib.regweb3.model.RegistroSalida} en un {@link AsientoRegistralSir}
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public AsientoRegistralSir transformarRegistroSalida(RegistroSalida registroSalida)
            throws Exception, I18NException{

        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        AsientoRegistralSir asientoRegistralSir = new AsientoRegistralSir();

        asientoRegistralSir.setIndicadorPrueba(IndicadorPrueba.NORMAL); // todo Modificar cuando entremos en Producción
        asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.PENDIENTE_ENVIO);
        asientoRegistralSir.setEntidad(registroSalida.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        asientoRegistralSir.setCodigoEntidadRegistralOrigen(registroSalida.getOficina().getCodigo());
        asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(registroSalida.getOficina().getDenominacion());
        asientoRegistralSir.setNumeroRegistro(registroSalida.getNumeroRegistroFormateado());
        asientoRegistralSir.setFechaRegistro(registroSalida.getFecha());
        asientoRegistralSir.setCodigoUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getCodigo());
        asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        asientoRegistralSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        asientoRegistralSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        asientoRegistralSir.setCodigoUnidadTramitacionDestino(obtenerCodigoUnidadTramitacionDestino(registroDetalle));
        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(obtenerDenominacionUnidadTramitacionDestino(registroDetalle));

        // Segmento De_Asunto
        asientoRegistralSir.setResumen(registroDetalle.getExtracto());
        /*if(registroSalida.getDestino() != null){ //todo Revisar
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            asientoRegistralSir.setCodigoAsunto(tra.getNombre());
        }*/
        asientoRegistralSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        asientoRegistralSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        asientoRegistralSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        asientoRegistralSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        asientoRegistralSir.setNombreUsuario(registroSalida.getUsuario().getNombreCompleto());
        asientoRegistralSir.setContactoUsuario(registroSalida.getUsuario().getUsuario().getEmail());
        asientoRegistralSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        asientoRegistralSir.setAplicacion(registroDetalle.getAplicacion());
        asientoRegistralSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        asientoRegistralSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        asientoRegistralSir.setTipoRegistro(TipoRegistro.SALIDA);
        asientoRegistralSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        asientoRegistralSir.setObservacionesApunte(registroDetalle.getObservaciones());
        asientoRegistralSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
        asientoRegistralSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        asientoRegistralSir.setExpone(registroDetalle.getExpone());
        asientoRegistralSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados
        asientoRegistralSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos
        asientoRegistralSir.setAnexos(procesarAnexosSir(registroDetalle.getAnexosFull(), asientoRegistralSir.getIdentificadorIntercambio()));

        return asientoRegistralSir;
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
            InteresadoSir interesadoSir = null;

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

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getDocumento())) {
            interesadoSir.setDocumentoIdentificacionInteresado(interesado.getDocumento());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getRazonSocial())) {
            interesadoSir.setRazonSocialInteresado(interesado.getRazonSocial());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getNombre())) {
            interesadoSir.setNombreInteresado(interesado.getNombre());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getApellido1())) {
            interesadoSir.setPrimerApellidoInteresado(interesado.getApellido1());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getApellido2())) {
            interesadoSir.setSegundoApellidoInteresado(interesado.getApellido2());
        }

        if(interesado.getPais() != null){
            interesadoSir.setCodigoPaisInteresado(interesado.getPais().getCodigoPais().toString());
        }

        if(interesado.getProvincia() != null){
            interesadoSir.setCodigoProvinciaInteresado(interesado.getProvincia().getCodigoProvincia().toString());
        }

        if(interesado.getLocalidad() != null){
            interesadoSir.setCodigoMunicipioInteresado(interesado.getLocalidad().getCodigoLocalidad().toString());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getDireccion())) {
            interesadoSir.setDireccionInteresado(interesado.getDireccion());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getCp())) {
            interesadoSir.setCodigoPostalInteresado(interesado.getCp());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getEmail())) {
            interesadoSir.setCorreoElectronicoInteresado(interesado.getEmail());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getTelefono())) {
            interesadoSir.setTelefonoInteresado(interesado.getTelefono());
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getDireccionElectronica())) {
            interesadoSir.setDireccionElectronicaHabilitadaInteresado(interesado.getDireccionElectronica());
        }

        if (interesado.getCanal() != null) {
            interesadoSir.setCanalPreferenteComunicacionInteresado(CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }

        if (!es.caib.regweb3.utils.StringUtils.isEmpty(interesado.getObservaciones())) {
            interesadoSir.setObservaciones(interesado.getObservaciones());
        }

        // Si tiene representante, también lo transformamos
        if(representante != null){

            if (representante.getTipoDocumentoIdentificacion() != null) {
                interesadoSir.setTipoDocumentoIdentificacionRepresentante(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getDocumento())) {
                interesadoSir.setDocumentoIdentificacionRepresentante(representante.getDocumento());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getRazonSocial())) {
                interesadoSir.setRazonSocialRepresentante(representante.getRazonSocial());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getNombre())) {
                interesadoSir.setNombreRepresentante(representante.getNombre());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getApellido1())) {
                interesadoSir.setPrimerApellidoRepresentante(representante.getApellido1());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getApellido2())) {
                interesadoSir.setSegundoApellidoRepresentante(representante.getApellido2());
            }

            if(representante.getPais() != null){
                interesadoSir.setCodigoPaisRepresentante(representante.getPais().getCodigoPais().toString());
            }

            if(representante.getProvincia() != null){
                interesadoSir.setCodigoProvinciaRepresentante(representante.getProvincia().getCodigoProvincia().toString());
            }

            if(representante.getLocalidad() != null){
                interesadoSir.setCodigoMunicipioRepresentante(representante.getLocalidad().getCodigoLocalidad().toString());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getDireccion())) {
                interesadoSir.setDireccionRepresentante(representante.getDireccion());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getCp())) {
                interesadoSir.setCodigoPostalRepresentante(representante.getCp());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getEmail())) {
                interesadoSir.setCorreoElectronicoRepresentante(representante.getEmail());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getTelefono())) {
                interesadoSir.setTelefonoRepresentante(representante.getTelefono());
            }

            if (!es.caib.regweb3.utils.StringUtils.isEmpty(representante.getDireccionElectronica())) {
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
    private List<AnexoSir> procesarAnexosSir(List<AnexoFull> anexosFull, String identificadorIntercambio) throws Exception{

        List<AnexoSir> anexosSir = new ArrayList<AnexoSir>();
        int secuencia = 0;

        for(AnexoFull anexoFull:anexosFull){


            final int modoFirma = anexoFull.getAnexo().getModoFirma();
            Anexo anexo = anexoFull.getAnexo();

            switch (modoFirma){

                case MODO_FIRMA_ANEXO_ATTACHED:

                    SignatureCustody sc = anexoFull.getSignatureCustody();

                    String identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
                    secuencia++;

                    AnexoSir anexoSir = crearAnexoSir(sc.getName(),identificador_fichero,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),
                            anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),sc.getMime(),sc.getData(),identificador_fichero,
                            anexo.getObservaciones());

                    anexosSir.add(anexoSir);

                    break;

                case MODO_FIRMA_ANEXO_DETACHED:

                    // ================= SEGMENTO 1: DOCUMENT ==================
                    anexoSir = new AnexoSir();

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
                    anexoSir = new AnexoSir();

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
                                   byte[] firma, byte[] timeStamp, byte[] validacionOCSPCertificado, byte[] hash, String tipoMime,
                                   byte[] anexoData, String identificadorDocumentoFirmado, String observaciones){
        AnexoSir anexoSir = new AnexoSir();

        anexoSir.setNombreFichero(nombreFichero);
        anexoSir.setIdentificadorFichero(identificadorFichero);
        if(validezDocumento != null){
            anexoSir.setValidezDocumento(validezDocumento);
        }
        anexoSir.setTipoDocumento(tipoDocumento);
        if(certificado != null){
            anexoSir.setCertificado(certificado);
        }
        if(firma != null){
            anexoSir.setFirma(firma);
        }
        if(timeStamp != null){
            anexoSir.setTimestamp(timeStamp);
        }
        if(validacionOCSPCertificado != null){
            anexoSir.setValidacionOCSPCertificado(validacionOCSPCertificado);
        }

        anexoSir.setHash(hash);
        if(tipoMime != null){
            anexoSir.setTipoMIME(tipoMime);
        }
        if(anexoData != null){
            anexoSir.setAnexoData(anexoData);
        }
        if(identificadorDocumentoFirmado != null){
            anexoSir.setIdentificadorDocumentoFirmado(identificadorDocumentoFirmado);
        }

        anexoSir.setObservaciones(observaciones);

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

        String result = new StringBuffer()
                .append(identificadorIntercambio)
                .append("_01_")
                .append(org.apache.commons.lang.StringUtils.leftPad(
                        String.valueOf(secuencia), 4, "0"))
                .append(".").append(getExtension(fileName)).toString();

        return result;
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
     * Genera el Hash mediante SHA-256 del contenido del documento y lo codifica en base64
     *
     * @param documentoData
     * @return
     * @throws Exception
     */
    private byte[] obtenerHash(byte[] documentoData) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(documentoData);

        return Base64.encodeBase64(digest);

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
        String codOficinaOrigen = null;

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
        String denominacionOficinaOrigen = null;

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
    private InteresadoSir crearInteresadoJuridico(FicheroIntercambio ficheroIntercambio){

        InteresadoSir interesadoSalida = new InteresadoSir();

        if(StringUtils.isNotBlank(ficheroIntercambio.getCodigoUnidadTramitacionDestino())){

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(ficheroIntercambio.getCodigoUnidadTramitacionDestino());

            if(StringUtils.isNotBlank(ficheroIntercambio.getDescripcionUnidadTramitacionDestino())){
                interesadoSalida.setRazonSocialInteresado(ficheroIntercambio.getDescripcionUnidadTramitacionDestino());
            }else{
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(ficheroIntercambio.getCodigoUnidadTramitacionDestino(),RegwebConstantes.UNIDAD));

            }


        }else{
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();

                OficinaTF oficinaTF = oficinasService.obtenerOficina(ficheroIntercambio.getCodigoEntidadRegistralDestino(),null,null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(oficinaTF.getCodUoResponsable(),RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }

}