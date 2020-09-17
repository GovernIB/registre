package es.caib.regweb3.persistence.ejb;

import com.sun.jersey.api.client.ClientResponse;
import es.caib.plugins.arxiu.api.Document;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.utils.ClientUtils;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.ISO8601;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;


/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * @author anadal
 * @author anadal (Adaptació DocumentCustody 3.0.0)
 * Date: 6/03/13
 */
@Stateless(name = "AnexoEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AnexoBean extends BaseEjbJPA<Anexo, Long> implements AnexoLocal {

    protected final Logger log = Logger.getLogger(getClass());


    @Resource
    private javax.ejb.SessionContext ejbContext;

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaCambiarEstadoEJB/local")
    private RegistroEntradaCambiarEstadoLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaCambiarEstadoEJB/local")
    private RegistroSalidaCambiarEstadoLocal registroSalidaEjb;

    @Autowired
    ArxiuCaibUtils arxiuCaibUtils;


    @Override
    public Anexo getReference(Long id) throws Exception {

        return em.getReference(Anexo.class, id);
    }

    @Override
    public AnexoFull getAnexoFullLigero(Long anexoID, Long idEntidad) throws I18NException {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Descarga anexo";
        AnexoFull anexoFull = null;

        try {

            //Obtenemos el anexo de la tabla de anexos de regweb
            Anexo anexo = em.find(Anexo.class, anexoID);

            //Montamos un AnexoFull( Anexo + toda la parte de custodia)
            anexoFull = new AnexoFull(anexo);

            //Obtenemos el identificador de custodia
            String custodyID = anexo.getCustodiaID();

            if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

                // Los justificantes y los anexos se guardan en plugins diferentes,
                // por eso se carga el plugin en función de si es justificante o no
                IDocumentCustodyPlugin custody;
                if (anexo.isJustificante()) { // Si es justificante cargamos el plugin de custodia del justificante
                    custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
                    descripcion = "Descarga justificante";
                } else { //si no, cargamos el plugin de anexos que no son justificantes
                    custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
                }

                // Integracion
                peticion.append("clase: ").append(custody.getClass().getName()).append(System.getProperty("line.separator"));
                peticion.append("custodyID: ").append(custodyID).append(System.getProperty("line.separator"));
                peticion.append("anexoID: ").append(anexoID).append(System.getProperty("line.separator"));
                peticion.append("justificante: ").append(anexo.isJustificante()).append(System.getProperty("line.separator"));

                //Obtenemos la información(sin el contenido físico en bytes[]) del anexo guardados en custodia
                anexoFull.setDocumentoCustody(custody.getDocumentInfoOnly(custodyID)); //Documento asociado al anexo
                anexoFull.setDocumentoFileDelete(false);
                anexoFull.setSignatureCustody(custody.getSignatureInfoOnly(custodyID)); //Firma asociada al anexo
                anexoFull.setSignatureFileDelete(false);

            //Obtenemos las metadatas de escaneo del anexo si no es justificante
            if(!anexo.isJustificante()) {
                List<Metadata> metadataList = new ArrayList<>();
                //Profundidad color
                Metadata profundidadColor = custody.getOnlyOneMetadata(custodyID, MetadataConstants.EEMGDE_PROFUNDIDAD_COLOR);
                if (profundidadColor != null) {
                    metadataList.add(profundidadColor);
                }

                //Resolución
                Metadata resolucion = custody.getOnlyOneMetadata(custodyID, MetadataConstants.EEMGDE_RESOLUCION);
                if (resolucion != null) {
                    metadataList.add(resolucion);
                }

                anexoFull.setMetadatas(metadataList);
            }

            if (log.isDebugEnabled()) {
                log.debug("SIGNATURE " + custody.getSignatureInfoOnly(custodyID));
                log.debug("DOCUMENT " + custody.getDocumentInfoOnly(custodyID));
                log.debug("modoFirma " + anexo.getModoFirma());
            }

            }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

                // Cargamos el plugin de Arxiu
                arxiuCaibUtils.cargarPlugin(idEntidad);

                Document document = arxiuCaibUtils.getDocumento(custodyID,null,false,false);

                anexoFull.setDocument(document);
                anexoFull.setDocumentoFileDelete(false);
                anexoFull.setSignatureFileDelete(false);

            }

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CUSTODIA, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, idEntidad, "");

            return anexoFull;

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CUSTODIA, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, idEntidad, "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            throw new I18NException(e, "anexo.error.obteniendo",
                    new I18NArgumentString(String.valueOf(anexoID)),
                    new I18NArgumentString(e.getMessage()));

        }
    }


    @Override
    public AnexoFull getAnexoFull(Long anexoID, Long idEntidad) throws I18NException {

        try {
            //Obtenemos el anexo de la tabla de anexos de regweb
            Anexo anexo = em.find(Anexo.class, anexoID);

            //Obtenemos el identificador de custodia
            String custodyID = anexo.getCustodiaID();

            AnexoFull anexoFull = new AnexoFull(anexo);

            if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

                IDocumentCustodyPlugin custody;
                final boolean isJustificante = anexo.isJustificante();
                if (isJustificante) { // Si es justificante cargamos el plugin de custodia del justificante
                    custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
                } else {//si no, cargamos el plugin de anexos que no son justificantes
                    custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
                }

                //Obtenemos la información(con el contenido físico en bytes[]) del anexo guardados en custodia
                anexoFull.setDocumentoCustody(custody.getDocumentInfo(custodyID)); //Documento asociado al anexo
                anexoFull.setDocumentoFileDelete(false);

                anexoFull.setSignatureCustody(custody.getSignatureInfo(custodyID));//Firma asociada al anexo
                anexoFull.setSignatureFileDelete(false);

            } else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

                // Cargamos el plugin de Arxiu
                arxiuCaibUtils.cargarPlugin(idEntidad);

                Document document = arxiuCaibUtils.getDocumento(custodyID,null,true,true);

                anexoFull.setDocument(document);
                anexoFull.setDocumentoFileDelete(false);
                anexoFull.setSignatureFileDelete(false);
            }

            return anexoFull;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new I18NException(e, "anexo.error.obteniendo",
                    new I18NArgumentString(String.valueOf(anexoID)),
                    new I18NArgumentString(e.getMessage()));

        }
    }


    /**
     * Método que crea un anexo o un justificante a partir de lo que le llega en anexoFull
     */
    public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                Long registroID, Long tipoRegistro, String custodyID, Boolean validarAnexo) throws I18NException, I18NValidationException {

        IDocumentCustodyPlugin custody = null;
        boolean error = false;
        final boolean isNew = true;

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Nuevo anexo ";
        String numRegFormat = "";
        Entidad entidad = null;

        try {

            // Si el anexo nuevo, proviene de un Oficio y fue un Justificante creado con el ApiArxiu, lo transformamos
            if(anexoFull.getAnexo().getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){
                anexoFull.arxiuDocumentToCustody();
            }

            Anexo anexo = anexoFull.getAnexo();

            //Obtenemos el registro con sus anexos, interesados y tipo Asunto
            IRegistro registro = getIRegistro(registroID, tipoRegistro);
            anexo.setRegistroDetalle(registro.getRegistroDetalle());

            //Obtenemos la Entidad
            entidad = registro.getOficina().getOrganismoResponsable().getEntidad();

            // Obtenemos el Plugin de Custodia correspondiente
            if (anexo.isJustificante()) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
                descripcion = "Nuevo anexo justificante";
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_CUSTODIA);
            }

            // Validador
            validateAnexo(anexo, isNew);

            anexo.setFechaCaptura(new Date());

            // Integración
            peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registro.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("clase: ").append(custody.getClass().getName()).append(System.getProperty("line.separator"));
            peticion.append("custodyID: ").append(custodyID).append(System.getProperty("line.separator"));

            // Validar firma del Anexo
            //Solo validamos si no es justificante, no es fichero tecnico y nos indican que se debe validar
            if (!anexo.isJustificante() && validarAnexo && !RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO.equals(anexo.getTipoDocumento())) {
                final boolean force = false; //Indica si queremos forzar la excepción.
                if (anexo.getModoFirma() != RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) { // Si no tiene firma no se valida
                    signatureServerEjb.checkDocument(anexoFull, entidad.getId(),
                            new Locale("es"), force);
                }
            }

            //Si firmaValida es null, por defecto marcamos como false
            if (anexo.getFirmaValida() == null) {
                anexo.setFirmaValida(false);
            }
            //Si no és justificante, por defecto marcamos como false
            if (!anexo.isJustificante()) {
                anexo.setJustificante(false);
            }


            // Revisar si tipusdocumental està carregat
            Long id = anexo.getTipoDocumental().getId();
            TipoDocumental td = tipoDocumentalEjb.findById(id);
            if (td == null) {
                I18NException i18n = new I18NException("anexo.tipoDocumental.obligatorio");
                log.error("No trob tipoDocumental amb ID = ]" + id + "[");
                throw i18n;
            } else {
                anexo.setTipoDocumental(td);
            }


            // ---------- BBDD -------------
            // Guardamos el anexo per a que tengui ID
            anexo = this.persist(anexo);


            // ----------- CUSTODIA -----------------
            final Map<String, Object> custodyParameters;
            custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);

            //Reservamos el custodyID
            if (custodyID == null) {
                custodyID = custody.reserveCustodyID(custodyParameters);
            }
            anexo.setCustodiaID(custodyID);

            numRegFormat = registro.getNumeroRegistroFormateado();

            //Guardamos los documentos asociados al anexo en custodia
            updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
                    registro, isNew);

            // -----------  BBDD -----------------

            // Actualitzam anexo per a que tengui custodyID
            anexo = this.persist(anexo);

            //Creamos el histórico de las modificaciones del registro debido a los anexos
            if (!anexo.isJustificante()) {
                crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
            }

            // -----------------------------------

            anexoFull.setAnexo(anexo);

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CUSTODIA, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), numRegFormat);

            return anexoFull;

        } catch (I18NException i18n) {
            error = true;
            log.info("Error creant un anexe: " + i18n.getMessage(), i18n);
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CUSTODIA, descripcion, peticion.toString(), i18n, null, System.currentTimeMillis() - tiempo, entidad.getId(), numRegFormat);
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw i18n;
        } catch (Exception e) {
            error = true;
            log.info("Error creant un anexe: " + e.getMessage(), e);
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CUSTODIA, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), numRegFormat);
            } catch (Exception ex) {
                e.printStackTrace();
            }

            throw new I18NException(e, "anexo.error.guardando", new I18NArgumentString(e.getMessage()));
        } finally {
            if (error) {
                ejbContext.setRollbackOnly();
                if (custody != null && custodyID != null) {
                    try {
                        custody.deleteCustody(custodyID);
                    } catch (Throwable th) {
                        log.warn("Error borrant custodia: " + th.getMessage(), th);
                    }
                }
            }
        }
    }

    /**
     * @param anexo
     * @param isNou
     * @throws I18NValidationException
     * @throws I18NException
     */
    protected void validateAnexo(Anexo anexo, final boolean isNou)
            throws I18NValidationException, I18NException {
        AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();
        AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);
        pfbv.throwValidationExceptionIfErrors(anexo, isNou);
    }

    /**
     * Método que actualiza un anexo
     *
     * @param anexoFull
     * @param usuarioEntidad
     * @param registroID
     * @param tipoRegistro
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                     Long registroID, Long tipoRegistro, boolean isJustificante, boolean noWeb)
            throws I18NException, I18NValidationException {

        try {
            Anexo anexo = anexoFull.getAnexo();

            // Validador
            final boolean isNew = false;
            validateAnexo(anexo, isNew);

            anexo.setFechaCaptura(new Date());

            //Cargamos el plugin de custodia
            IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_CUSTODIA);

            //Obtenemos el registro con sus anexos, interesados y tipo Asunto
            IRegistro registro = getIRegistro(registroID, tipoRegistro);

            //Actualizamos custodia solo cuando no venimos via web
            if (noWeb) {
                final Map<String, Object> custodyParameters;
                custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);

                final String custodyID = anexo.getCustodiaID();
                //Guardamos los cambios en custodia
                updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
                        registro, isNew);
            }

            //Actualizamos los datos de anexo en BBDD
            anexo = this.merge(anexo);
            anexoFull.setAnexo(anexo);

            // Crea historico y lo enlaza con el RegistroDetalle
            crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
            return anexoFull;
        } catch (I18NException i18n) {
            ejbContext.setRollbackOnly();
            throw i18n;
        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            log.error("Error actualitzant un anexe: " + e.getMessage(), e);
            throw new I18NException(e, "anexo.error.guardando", new I18NArgumentString(e.getMessage()));
        }

    }

    /**
     * Clonamos el registro indicado por el identificador registroID
     *
     * @param registroID
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    protected IRegistro getIRegistro(Long registroID, Long tipoRegistro) throws Exception {
        IRegistro registro;
        IRegistro cloneRegistro;
        //Recuperamos el registro de la BD
        if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
            registro = registroEntradaEjb.findById(registroID);
        } else {
            registro = registroSalidaEjb.findById(registroID);

        }

        //Cargamos el tipo asunto y los interesados
        //Hibernate.initialize(registro.getRegistroDetalle().getTipoAsunto());
        //Hibernate.initialize(registro.getRegistroDetalle().getInteresados());

        //Creamos un registro a partir del cargado previamente
        if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
            cloneRegistro = new RegistroEntrada((RegistroEntrada) registro);
        } else {
            cloneRegistro = new RegistroSalida((RegistroSalida) registro);
        }

        //cargamos y clonamos los anexos del registro
        List<Anexo> anexos = Anexo.clone(registro.getRegistroDetalle().getAnexos());
        //Se los asignamos al registro clonado.
        cloneRegistro.getRegistroDetalle().setAnexos(anexos);


        return cloneRegistro;
    }

    /**
     * @param anexoFull
     * @param usuarioEntidad
     * @param registroID
     * @param tipoRegistro
     * @param isNew
     * @throws Exception
     * @throws I18NException
     */
    protected void crearHistorico(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                  Long registroID, Long tipoRegistro, boolean isNew) throws Exception, I18NException {

        if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
            RegistroEntrada registroEntrada = registroEntradaEjb.findById(registroID);
            Entidad entidadActiva = registroEntrada.getOficina().getOrganismoResponsable().getEntidad();
            // Dias que han pasado desde que se creó el registroEntrada
            Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

            if (isNew) {//NUEVO ANEXO
                // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
                // cambios y se cambia el estado del registroEntrada a pendiente visar
                if (dias >= entidadActiva.getDiasVisado()) {
                    registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

                    // Creamos el historico de registro de entrada
                    historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.anexos"), true);
                }

            } else {// MODIFICACION DE ANEXO

                if (dias >= entidadActiva.getDiasVisado()) { // Si han pasado más de los dias de visado cambiamos estado registro
                    registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                }

                // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.anexos"), true);
            }
            anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());

        } else {
            RegistroSalida registroSalida = registroSalidaEjb.findById(registroID);
            Entidad entidadActiva = registroSalida.getOficina().getOrganismoResponsable().getEntidad();
            // Dias que han pasado desde que se creó el registroEntrada
            Long dias = RegistroUtils.obtenerDiasRegistro(registroSalida.getFecha());

            if (isNew) {//NUEVO ANEXO
                // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
                // cambios y se cambia el estado del registroEntrada a pendiente visar
                if (dias >= entidadActiva.getDiasVisado()) {
                    registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

                    // Creamos el historico de registro de entrada
                    historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.anexos"), true);
                }

            } else {// MODIFICACION DE ANEXO

                if (dias >= entidadActiva.getDiasVisado()) { // Si han pasado más de los dias de visado cambiamos estado registro
                    registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                }
                // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
                historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.anexos"), true);
            }
            anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
        }
    }


    /**
     * Método que crea/actualiza la información de custodia del anexo indicado
     *
     * @param anexoFull
     * @param custodyParameters
     * @param custodyID
     * @param registro
     * @param isNou
     * @throws Exception
     * @throws I18NException
     */
    protected void updateCustodyInfoOfAnexo(AnexoFull anexoFull, IDocumentCustodyPlugin custody2,
                                            final Map<String, Object> custodyParameters, final String custodyID,
                                            IRegistro registro, boolean isNou) throws Exception, I18NException {

        // Validador: Sempre amb algun arxiu
        int modoFirma = anexoFull.getAnexo().getModoFirma();
        if (isNou) { //Creación
            if (anexoFull.getDocumentoCustody() == null && anexoFull.getSignatureCustody() == null) {
                //"No ha definit cap fitxer en aquest annex"
                throw new I18NException("anexo.error.sinfichero");
            }
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && anexoFull.getSignatureCustody() == null) {
                //Si la firma es adjunta, el documento con su firma adjunta debe venir en SignatureCustody
                throw new I18NException("anexo.error.sinfichero");
            }
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && (anexoFull.getDocumentoCustody() == null || anexoFull.getSignatureCustody() == null)) {
                //Si la firma es por separado, se esperan dos documentos (el original + la firma)
                throw new I18NException("anexo.error.faltadocumento");
            }
        } else {//Actualización
            //Controlamos que el anexo no quede sin archivo, hay que controlar con modofirma
            int total = 0;
            //Si no tenia documento, pero ahora envian uno nuevo, sumamos 1
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {
                if (custody2.getDocumentInfoOnly(custodyID) == null) {
                    // Afegim un
                    if (anexoFull.getDocumentoCustody() != null) {
                        total += 1;
                    }
                } else { // ya tenia, sumamos 1
                    total += 1;
                }
                log.info("TOTAL " + total);
                if (total <= 0) {
                    //La combinació elegida deixa aquest annex sense cap fitxer
                    throw new I18NException("anexo.error.quedarsesinfichero");
                }
            }
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                //Si no tenia firma, pero envian 1 nueva, sumamos 1
                if (custody2.getSignatureInfoOnly(custodyID) == null) {
                    // Afegim un
                    if (anexoFull.getSignatureCustody() != null) {
                        total += 1;
                    }
                } else { // si ya tenia, sumamos 1
                    total += 1;
                }
                log.info("TOTAL " + total);
             /* if (total <= 0) {
                  //La combinació elegida deixa aquest annex sense cap fitxer
                  throw new I18NException("anexo.error.quedarsesinfichero");
              }*/
                //PARCHE API ANTIGUA
                if (custody2.getDocumentInfoOnly(custodyID) == null) {
                    // Afegim un
                    if (anexoFull.getDocumentoCustody() != null) {
                        total += 1;
                    }
                } else { // ya tenia, sumamos 1
                    total += 1;
                }
                log.info("TOTAL " + total);
                if (total <= 0) {
                    //La combinació elegida deixa aquest annex sense cap fitxer
                    throw new I18NException("anexo.error.quedarsesinfichero");
                }
            }
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                if (custody2.getDocumentInfoOnly(custodyID) == null) {
                    // Afegim un
                    if (anexoFull.getDocumentoCustody() != null) {
                        total += 1;
                    }
                } else { // ya tenia, sumamos 1
                    total += 1;
                }

                //Si no tenia firma, pero envian 1 nueva, sumamos 1
                if (custody2.getSignatureInfoOnly(custodyID) == null) {
                    // Afegim un
                    if (anexoFull.getSignatureCustody() != null) {
                        total += 1;
                    }
                } else { // si ya tenia, sumamos 1
                    total += 1;
                }
                log.info("TOTAL " + total);
                if (total <= 1) {
                    //La combinació elegida deixa aquest annex sense cap fitxer
                    throw new I18NException("anexo.error.faltadocumento");
                }
            }

        }


        // TODO Falta Check DOC
        Anexo anexo = anexoFull.getAnexo();

        boolean updateDate = false;
        final DocumentCustody documentCustody;
        final SignatureCustody signatureCustody;

        String mimeFinal = null;
        //Actualización o creación de los documentos de los anexos en función del modo de firma
        //Si el anexo es nuevo o el modo de firma es detached, el comportamiento es el mismo
        if (isNou || modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            //Guardamos el documentCustody
            // XMAS SAVEALL DocumentCustody doc = guardarDocumentCustody(anexoFull.getDocumentoCustody(),
            //      custody, custodyID, custodyParameters, anexo,  mimeFinal);
            documentCustody = anexoFull.getDocumentoCustody();
            mimeFinal = arreglarDocumentCustody(documentCustody, anexo, mimeFinal);

            //Guardamos la signatureCustody
            // XMAS SAVEALL guardarSignatureCustody(anexoFull.getSignatureCustody(), doc, custody, custodyID, custodyParameters, anexo, updateDate, mimeFinal);
            signatureCustody = anexoFull.getSignatureCustody();
            mimeFinal = arreglarSignatureCustody(signatureCustody, documentCustody,
                    anexo, mimeFinal);

            updateDate = true;

        } else { //es modificación Tratamos todos los modos firma como corresponda
            DocumentCustody doc = anexoFull.getDocumentoCustody();
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

                //Guardamos el documentCustody
                // XMAS SAVEALL guardarDocumentCustody(anexoFull.getDocumentoCustody(), custody, custodyID, custodyParameters, anexo, updateDate, mimeFinal);
                documentCustody = anexoFull.getDocumentoCustody();
                mimeFinal = arreglarDocumentCustody(documentCustody,
                        anexo, mimeFinal);

                //Borrar lo que haya en signature custody
                // XMAS SAVEALL custody.deleteSignature(custodyID);
                signatureCustody = null;

                updateDate = true;
            } else if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                //obtenemos el document custody para crear bien el documento

                if (doc == null) {//CASO API NUEVA

                    documentCustody = null;
                    //Guardamos la signatureCustody. Los documentos con firma attached se guardan en SignatureCustody.

                    signatureCustody = anexoFull.getSignatureCustody();
                    mimeFinal = arreglarSignatureCustody(signatureCustody, documentCustody,
                            anexo, mimeFinal);

                    // XMAS SAVEALL guardarSignatureCustody(anexoFull.getSignatureCustody(), doc, custody, custodyID, custodyParameters, anexo, updateDate, mimeFinal);
                    //Borramos el documentcustody que habia por si venimos de otro modo de firma
                    // custody.deleteDocument(custodyID);

                    updateDate = true;
                } else { //PARCHE PARA API ANTIGUA
                    log.info("PARCHE DC " + anexoFull.getDocumentoCustody());
                    documentCustody = doc;
                    mimeFinal = arreglarDocumentCustody(documentCustody,
                            anexo, mimeFinal);

                    signatureCustody = null;

                    // XMAS SAVEALL guardarDocumentCustody(anexoFull.getDocumentoCustody(), custody, custodyID, custodyParameters, anexo, updateDate, mimeFinal);
                    updateDate = true;
                }

            } else {
                //el caso de modoFirma detached es igual que si fuese nuevo.

                // CASO:  no tocam res
                documentCustody = null;
                signatureCustody = null;
            }
        }

        if (documentCustody == null && signatureCustody == null) {
            // OK No feim res.
        } else {

            // Actualitzar Metadades
            final String lang = Configuracio.getDefaultLanguage();
            final Locale loc = new Locale(lang);
            List<Metadata> metadades = new ArrayList<Metadata>();

            // Metadades que venen de Scan
            List<Metadata> metasScan = anexoFull.getMetadatas();
           // List<org.fundaciobit.plugins.utils.Metadata> metadadesAntigues = new ArrayList<org.fundaciobit.plugins.utils.Metadata>();

            final boolean debug = log.isDebugEnabled();

            if (debug) {
                log.info("MESTAS SCAN = " + metasScan);
            }

            if (metasScan != null && metasScan.size() != 0) {

                if (debug) {
                    log.info("MESTAS SCAN SIZE = " + metasScan.size());
                    log.info("MESTAS ORIG SIZE PRE = " + metadades.size());
                }

                metadades.addAll(metasScan);

                //Convertim les dades rebudes de l'Scan al format antic
                /*TODO s'hauria de canvia també la versió del plugin de DocumentCustody per evitar emplear la classe antiga de Metadata.
                org.fundaciobit.plugins.utils.Metadata s'hauria d'apuntar a pluginsIB*/


                if (debug) {
                    log.info("MESTAS ORIG SIZE POST = " + metadades.size());
                }

            }
            //TODO REVISAR METADATAS

            // fechaDeEntradaEnElSistema
            if (updateDate) {

                //les metadates estan comentades perque no s'usan de moment fins que adaptem a la nova versió de documentCustody de plugins ib)

                 metadades.add(new Metadata("anexo.fechaCaptura", anexo.getFechaCaptura()));

                // Afegida Nova Metadada
                metadades.add(new Metadata(MetadataConstants.ENI_FECHA_INICIO,
                   ISO8601.dateToISO8601(anexo.getFechaCaptura())));

            }

            // String tipoDeDocumento; //  varchar(100)
            if (anexo.getTitulo() != null) {
                 metadades.add(new Metadata("anexo.titulo", anexo.getTitulo()));


                // Afegida Nova Metadada
                // MetadataConstants.ENI_DESCRIPCION = "eni:descripcion"
                  metadades.add(new Metadata(MetadataConstants.ENI_DESCRIPCION, anexo.getTitulo()));

            }

            //  String tipoDeDocumento; //  varchar(100)
            if (anexo.getTipoDocumento() != null) {
                metadades.add(new Metadata("anexo.tipoDocumento",
                   I18NLogicUtils.tradueix(loc, "tipoDocumento.0" + anexo.getTipoDocumento())));

            }

            if (registro.getOficina() != null && registro.getOficina().getNombreCompleto() != null) {

                metadades.add(new Metadata("oficina", registro.getOficina().getNombreCompleto()));
               // metadadesAntigues.add(new org.fundaciobit.plugins.utils.Metadata("oficina", registro.getOficina().getNombreCompleto()));

                // Afegida Nova Metadada
                // MetadataConstants.ENI_CODIGO_OFICINA_REGISTRO = "eni:codigo_oficina_registro"
                metadades.add(new Metadata(MetadataConstants.ENI_CODIGO_OFICINA_REGISTRO,
                   registro.getOficina().getCodigo()));

            }


            if (anexo.getOrigenCiudadanoAdmin() != null) {
                metadades.add(new Metadata("anexo.origen",
                   I18NLogicUtils.tradueix(loc, "anexo.origen." + anexo.getOrigenCiudadanoAdmin())));


                // Afegida Nova Metadada
                // MetadataConstants.ENI_ORIGEN = "eni:origen"
                metadades.add(new Metadata(MetadataConstants.ENI_ORIGEN,
                   anexo.getOrigenCiudadanoAdmin()));


            }

            /*
             * tipoValidezDocumento.1=Còpia
             * tipoValidezDocumento.2=Còpia Compulsada
             * tipoValidezDocumento.3=Còpia Original
             * tipoValidezDocumento.4=Original
             */
            if (anexo.getValidezDocumento() != null && anexo.getValidezDocumento() != -1) {
                metadades.add(new Metadata("anexo.validezDocumento",
                   I18NLogicUtils.tradueix(loc, "tipoValidezDocumento." + anexo.getValidezDocumento())));


                // Afegida Nova Metadada
                // MetadataConstants.ENI_ESTADO_ELABORACION = "eni:estado_elaboracion"
               metadades.add(new Metadata(MetadataConstants.ENI_ESTADO_ELABORACION,
                   RegwebConstantes.CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento())));

            }

            if (mimeFinal != null) {
                     metadades.add(new Metadata("anexo.formato", mimeFinal));

            }

            if (anexo.getTipoDocumental() != null &&
                    anexo.getTipoDocumental().getCodigoNTI() != null) {

                // Afegida Nova Metadada
                // MetadataConstants.ENI_TIPO_DOCUMENTAL = "eni:tipo_doc_ENI"
                metadades.add(new Metadata(MetadataConstants.ENI_TIPO_DOCUMENTAL,
                   anexo.getTipoDocumental().getCodigoNTI()));


                 metadades.add(new Metadata("anexo.tipoDocumental.codigo", anexo.getTipoDocumental().getCodigoNTI()));


                try {
                    metadades.add(new Metadata("anexo.tipoDocumental.descripcion",
                       ((TraduccionTipoDocumental) anexo.getTipoDocumental().getTraduccion(loc.getLanguage())).getNombre()));

                } catch (Throwable th) {
                    log.error("Error en la traduccion de tipo documental: " + th.getMessage(), th);
                }
            }
            if (anexo.getObservaciones() != null) {
                 metadades.add(new Metadata("anexo.observaciones", anexo.getObservaciones()));

            }

/*for (Metadata metadata: metadades){
    log.info("Key " + metadata.getKey() + "Value " + metadata.getValue());
}*/

             custody2.saveAll(custodyID, custodyParameters, documentCustody,
              signatureCustody, metadades.toArray(new Metadata[metadades.size()]));
        }

    }


    /**
     * Método que prepara para actualizar los datos de un DocumentCustody
     *
     * @param doc
     * @param anexo
     * @param mimeFinal
     * @return
     * @throws Exception
     */
    public String arreglarDocumentCustody(DocumentCustody doc,
                                          Anexo anexo, String mimeFinal) throws Exception {

        if (doc != null && doc.getData() != null) {// si nos envian documento

            //Asignamos los datos nuevos recibidos
            if (doc.getMime() == null) {
                doc.setMime("application/octet-stream");
            }
            mimeFinal = doc.getMime();

            doc.setName(checkFileName(doc.getName(), "file.bin"));

            anexo.setFechaCaptura(new Date());
            anexo.setHash(RegwebUtils.obtenerHash(doc.getData()));

        }
        return mimeFinal;
    }

    /**
     * Método que prepara para actualizar los datos de un SignatureCustody
     *
     * @param signature
     * @param doc
     * @param anexo
     * @param mimeFinal
     * @return
     * @throws Exception
     */
    private String arreglarSignatureCustody(SignatureCustody signature,
                                            DocumentCustody doc, Anexo anexo, String mimeFinal) throws Exception {

        if (signature != null && signature.getData() != null) {//Si nos envian firma

            //Preparamos todos los datos para guardar la firma en custodia.
            String signType = (doc == null) ? SignatureCustody.OTHER_SIGNATURE_WITH_ATTACHED_DOCUMENT : SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT;

            //Asignamos los datos nuevos recibidos
            signature.setName(checkFileName(signature.getName(), "signature.bin"));

            final String mime = signature.getMime();
            if (mime == null) {
                signature.setMime("application/octet-stream");
            } else {
                if ("application/pdf".equals(mime)) {
                    signType = SignatureCustody.PADES_SIGNATURE;
                } else if ("application/xml".equals(mime) || "text/xml".equals(mime)) {
                    signType = SignatureCustody.XADES_SIGNATURE;
                }
            }

            mimeFinal = signature.getMime(); // Sobreescriu Mime de doc

            signature.setSignatureType(signType);
            // TODO Fallarà en update
            signature.setAttachedDocument(doc == null ? true : false);

            if (doc == null) {
                anexo.setHash(RegwebUtils.obtenerHash(signature.getData()));
            }

        }

        return mimeFinal;
    }


    /**
     * Verifica el nombre del fichero
     *
     * @param name
     * @param defaultName
     * @return
     * @throws Exception
     */
    protected String checkFileName(String name, String defaultName) throws Exception {
        if (name == null || name.trim().length() == 0) {
            return defaultName;
        } else { //Si no, lo recorta
            return StringUtils.recortarNombre(name, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);
        }
    }


    public static class java_util_Date_PersistenceDelegate extends PersistenceDelegate {
        protected Expression instantiate(Object oldInstance, Encoder out) {
            Date date = (Date) oldInstance;
            return new Expression(date, date.getClass(), "new", new Object[]{date.getTime()});
        }
    }


    protected Map<String, Object> getCustodyParameters(IRegistro registro, Anexo anexo,
                                                       AnexoFull anexoFull, UsuarioEntidad usuarioEntidad) {

        Map<String, Object> custodyParameters = new HashMap<String, Object>();

        custodyParameters.put("registro", registro);
        custodyParameters.put("anexo", anexo);
        custodyParameters.put("anexoFull", anexoFull);
        custodyParameters.put("usuarioEntidad", usuarioEntidad);

        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);
        custodyParameters.put("ciudadano_nombre", interesado.getNombreCompleto());

        String documentAdministratiu = interesado.getDocumento();
        if (documentAdministratiu == null) {
            documentAdministratiu = interesado.getCodigoDir3();
        }

        custodyParameters.put("ciudadano_idadministrativo", documentAdministratiu);

        return custodyParameters;
    }


    @Override
    public Anexo findById(Long id) throws Exception {

        return em.find(Anexo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Anexo> getAll() throws Exception {

        return em.createQuery("Select anexo from Anexo as anexo order by anexo.id").getResultList();
    }


    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(anexo.id) from Anexo as anexo");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Anexo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select anexo from Anexo as anexo order by anexo.id");

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    /**
     * Método que devuelve todos los anexos de un registro de entrada sin el justificante
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public List<AnexoFull> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException {

        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosSinJustificante = new ArrayList<AnexoFull>();

        for (Anexo anexo : anexos) {
            if (!anexo.isJustificante()) {
                anexosSinJustificante.add(getAnexoFullLigero(anexo.getId(), idEntidad));
            }
        }
        return anexosSinJustificante;
    }

    /**
     * Método que devuelve todos los anexos de un registro de salida sin el justificante
     *
     * @param registroSalida
     * @return
     * @throws Exception
     */
    @Override
    public List<AnexoFull> getByRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException {

        Long idEntidad = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroSalida.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosSinJustificante = new ArrayList<AnexoFull>();

        for (Anexo anexo : anexos) {
            if (!anexo.isJustificante()) {
                anexosSinJustificante.add(getAnexoFullLigero(anexo.getId(), idEntidad));
            }
        }
        return anexosSinJustificante;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception {
        Query query = em.createQuery("Select anexo from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle order by anexo.id");
        query.setParameter("idRegistroDetalle", idRegistroDetalle);
        return query.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Anexo> getByRegistroDetalleLectura(Long idRegistroDetalle) throws Exception {
        Query query = em.createQuery("Select anexo.titulo, anexo.tipoDocumento from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle");
        query.setParameter("idRegistroDetalle", idRegistroDetalle);

        List<Anexo> anexos = new ArrayList<Anexo>();
        List<Object[]> result = query.getResultList();

        for (Object[] object : result) {
            anexos.add(new Anexo((String) object[0], (Long) object[1]));
        }
        return anexos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getIdJustificante(Long idRegistroDetalle) throws Exception {
        Query q = em.createQuery("Select anexo.id from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle and " +
                "anexo.justificante = true order by anexo.id");
        q.setParameter("idRegistroDetalle", idRegistroDetalle);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> justificante = q.getResultList();

        if (justificante.size() >= 1) {
            return justificante.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void purgarAnexosRegistrosAceptados(Long idEntidad, Integer numElementos) throws Exception, I18NException {


        //Obtenemos los anexos de los registros de entrada que han sido aceptados y que no han sido purgados
        Query q = em.createQuery("Select anexos from RegistroEntrada as re left join re.registroDetalle.anexos as anexos where re.estado=:aceptado and  re.usuario.entidad.id=:idEntidad and anexos.purgado =false and anexos.justificante=false");

        q.setParameter("aceptado", RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
        q.setParameter("idEntidad", idEntidad);
        if(numElementos !=null) {
            q.setMaxResults(numElementos);
        }

        List<Anexo> anexos = q.getResultList();

        //Obtenemos los anexos de los registros de salida que han sido aceptados y que no han sido purgados
        Query qs = em.createQuery("Select anexos from  RegistroSalida as rs left join rs.registroDetalle.anexos as anexos where rs.estado=:aceptado and  rs.usuario.entidad.id=:idEntidad and anexos.purgado =false and anexos.justificante=false");

        qs.setParameter("aceptado", RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
        qs.setParameter("idEntidad", idEntidad);
        if(numElementos !=null) {
            qs.setMaxResults(numElementos);
        }

        anexos.addAll(qs.getResultList());


        for (Anexo anexo : anexos) {
            //Eliminamos de custodia los archivos asociados al anexo
            eliminarCustodia(anexo.getCustodiaID(), anexo.isJustificante(), idEntidad);
            //Marcamos el anexo como purgado
            anexo.setPurgado(true);
            merge(anexo);
        }

    }

    @Override
    public void purgarAnexosRegistrosDistribuidos(Long idEntidad, Integer meses, Integer numElementos) throws Exception, I18NException{

        List<String> custodyIds = obtenerCustodyIdAnexosDistribuidos(meses, numElementos);

        for (String custodyId : custodyIds) {
            //Purgamos anexo a anexo
            purgarAnexo(custodyId, false, idEntidad);
        }

    }

    @Override
    public void purgarAnexo(String custodiaId, boolean isJustificante, Long idEntidad) throws Exception, I18NException {

        try {
            Query query = em.createQuery("select anexo from Anexo as anexo where anexo.custodiaID=:custodiaId");
            query.setParameter("custodiaId", custodiaId);

            Anexo anexo = (Anexo) query.getSingleResult();
            if (anexo != null && !isJustificante) {
                eliminarCustodia(custodiaId, isJustificante, idEntidad);
                anexo.setPurgado(true);
                merge(anexo);
            }

        } catch (I18NException e) {
            throw new I18NException("S'ha produit un error eliminant la custodia de l'annex");
        }

    }


    @Override
    public List<String> obtenerCustodyIdAnexosDistribuidos(Integer meses, Integer numElementos) throws Exception {
            Date fechaPurgo = DateUtils.addMonths(new Date(), -meses);

            // Obtenemos aquellos anexos que corresponden a registros Distribuidos y la fecha de distribución la cogemos de la trazabilidad.
            Query q = em.createQuery("select anexo.custodiaID from Anexo as anexo, Trazabilidad  as t " +
                    "where t.fecha<=:fechaPurgo and t.registroEntradaOrigen.registroDetalle.id = anexo.registroDetalle.id and " +
                    "t.tipo =:tipoDistribucion and t.registroEntradaOrigen.estado=:distribuido and anexo.justificante = false and anexo.purgado = false");
            q.setParameter("fechaPurgo", fechaPurgo);
            q.setParameter("distribuido", RegwebConstantes.REGISTRO_DISTRIBUIDO);
            q.setParameter("tipoDistribucion", RegwebConstantes.TRAZABILIDAD_DISTRIBUCION);
            q.setHint("org.hibernate.readOnly", true);
            if(numElementos!= null) {
                q.setMaxResults(numElementos);
            }

            return q.getResultList();

    }


    /**
     * Obtiene la info + contenido físico(byte[]) del fichero existente en el sistema de archivos
     *
     * @param anexo
     * @return
     */
    @Override
    public DocumentCustody getArchivo(Anexo anexo, Long idEntidad) throws I18NException, Exception {

        if (anexo.getCustodiaID() == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

            IDocumentCustodyPlugin custody = null;

            if (anexo.isJustificante()) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
            }
            return custody.getDocumentInfo(anexo.getCustodiaID());

        } else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {

            // Cargamos el plugin de Arxiu
            arxiuCaibUtils.cargarPlugin(idEntidad);

            //return arxiuCaibUtils.getDocumento(anexo.getCustodiaID(), null, true, true);
        }

        return null;

    }


    /**
     * Obtiene el contenido físico del documento como byte[]
     *
     * @param anexo
     * @param idEntidad
     * @return
     */
    @Override
    public byte[] getArchivoContent(Anexo anexo, Long idEntidad) throws I18NException, Exception {

        if (anexo.getCustodiaID() == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

            IDocumentCustodyPlugin custody = null;

            if (anexo.isJustificante()) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
            }
            return custody.getDocument(anexo.getCustodiaID());

        } else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {

            // Cargamos el plugin de Arxiu
            arxiuCaibUtils.cargarPlugin(idEntidad);

            Document document = arxiuCaibUtils.getDocumento(anexo.getCustodiaID(), null, true, true);

            return document.getContingut().getContingut();
        }

        return null;

    }


    /**
     * Obtiene solo la info del Documento (sin byte[])
     *
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public DocumentCustody getDocumentInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException {
        IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
        return custody.getDocumentInfoOnly(custodiaID);
    }

    /**
     * Obtiene solo la info de la firma (sin byte[])
     *
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public SignatureCustody getSignatureInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException {
        IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
        return custody.getSignatureInfoOnly(custodiaID);
    }

    @Override
    public SignatureCustody getSignatureInfoOnly(Anexo anexo, Long idEntidad) throws I18NException, Exception {

        IDocumentCustodyPlugin custody = null;

        if (anexo.getCustodiaID() == null) {
            log.warn("getSignatureInfoOnly :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if (anexo.isJustificante()) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        } else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getSignatureInfoOnly(anexo.getCustodiaID());
    }

    /**
     * Obtiene la info + contenido físico(byte[]) de la firma existente en el sistema de archivos
     *
     * @param anexo
     * @param idEntidad
     * @return
     */
    public SignatureCustody getFirma(Anexo anexo, Long idEntidad) throws I18NException, Exception {

        IDocumentCustodyPlugin custody;

        if (anexo.getCustodiaID() == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if (anexo.isJustificante()) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        } else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getSignatureInfo(anexo.getCustodiaID());
    }


    /**
     * Obtiene el contenido físico de la firma como byte[]
     *
     * @param custodiaID
     * @return
     */
    @Override
    public byte[] getFirmaContent(String custodiaID, boolean isJustificante, Long idEntidad) throws Exception, I18NException {

        IDocumentCustodyPlugin custody;

        if (custodiaID == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if (isJustificante) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        } else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getSignature(custodiaID);
    }


    /**
     * Elimina completamente una custodia ( = eliminacion completa de Anexo)
     *
     * @param custodiaID
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    public boolean eliminarCustodia(String custodiaID, boolean isJustificante, Long idEntidad) throws Exception, I18NException {

        if (custodiaID == null) {
            log.warn("eliminarCustodia :: CustodiaID vale null !!!!!", new Exception());
            return false;
        } else {
            IDocumentCustodyPlugin custody;
            if (isJustificante) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
            }

            custody.deleteCustody(custodiaID);

            return true;

        }

    }


    /**
     * Obtiene la url de validacion del documento. Si no soporta url, devuelve null
     *
     * @param anexo
     * @param idEntidad
     * @return
     */
    public String getUrlValidation(Anexo anexo, Long idEntidad) throws I18NException, Exception {

        if (anexo.getCustodiaID() == null) {
            log.warn("getUrlValidation :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

            IDocumentCustodyPlugin custody = null;

            if (anexo.isJustificante()) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA);
            }
            return custody.getOriginalFileUrl(anexo.getCustodiaID(), new HashMap<String, Object>());

        } else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {

            // Cargamos el plugin de Arxiu
            arxiuCaibUtils.cargarPlugin(idEntidad);

            return  arxiuCaibUtils.getUrlValidacion(anexo.getCustodiaID());
        }

        return null;
    }


    /**
     * Obtiene el SignatureCustody de un Anexo
     *
     * @param anexo
     * @param idEntidad
     * @return SignatureCustody
     */
    public AnexoSimple descargarFirmaDesdeUrlValidacion(Anexo anexo, Long idEntidad) throws I18NException, Exception {


        if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {

            // Si es justificante
            byte[] data = null;

            if (anexo.isJustificante()) {
                String url = null;
                try {
                    url = getUrlValidation(anexo, idEntidad);

                    // Si soporta url, davalla arxiu de la url
                    if (StringUtils.isNotEmpty(url)) {
                        BufferedInputStream in = null;
                        ByteArrayOutputStream fout = null;
                        try {
                            String username = PropiedadGlobalUtil.getUrlValidationUsername(idEntidad);
                            String password = PropiedadGlobalUtil.getUrlValidationPassword(idEntidad);

                            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                                ClientResponse cr = ClientUtils.commonCall(url, username, password);
                                in = new BufferedInputStream(cr.getEntityInputStream());

                            } else {
                                in = new BufferedInputStream(new URL(url).openStream());
                            }
                            fout = new ByteArrayOutputStream();

                            final byte buffer[] = new byte[1024];
                            int count;
                            while ((count = in.read(buffer, 0, 1024)) != -1) {
                                fout.write(buffer, 0, count);
                            }
                            data = fout.toByteArray();
                        } finally {
                            if (in != null) {
                                in.close();
                            }
                            if (fout != null) {
                                fout.close();
                            }
                        }

                        SignatureCustody sc = getSignatureInfoOnly(anexo, idEntidad);
                        sc.setData(data);
                        return new AnexoSimple(data, sc.getName());
                    }

                } catch (Exception e) {
                    log.error("Error descarregant justificant des de url de validació (" + url + ")", e);
                    data = null;
                }

            }

        }else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {

            Document documento = arxiuCaibUtils.getDocumento(anexo.getCustodiaID(),null, true, false);

            return new AnexoSimple(documento.getContingut().getContingut(), documento.getContingut().getArxiuNom());

        }


        // Si no ha soportat url, davalla l'arxiu original
        SignatureCustody sc = getFirma(anexo, idEntidad);

        return new AnexoSimple(sc.getData(), sc.getName());
    }

}
