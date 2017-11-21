package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataConstants;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.util.*;



/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * @author anadal
 * @author anadal (Adaptació DocumentCustody 3.0.0)
 *         Date: 6/03/13
 */
@Stateless(name = "AnexoEJB")
@SecurityDomain("seycon")
public class AnexoBean extends BaseEjbJPA<Anexo, Long> implements AnexoLocal {

    protected final Logger log = Logger.getLogger(getClass());


    @Resource
    private javax.ejb.SessionContext ejbContext;

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaCambiarEstadoEJB/local")
    private RegistroEntradaCambiarEstadoLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaCambiarEstadoEJB/local")
    private RegistroSalidaCambiarEstadoLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/SignatureServerEJB/local")
    private SignatureServerLocal signatureServerEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


    @Override
    public Anexo getReference(Long id) throws Exception {

        return em.getReference(Anexo.class, id);
    }

    @Override
    public AnexoFull getAnexoFullLigero(Long anexoID) throws I18NException {

        try {
            Anexo anexo = em.find(Anexo.class, anexoID);

            String custodyID = anexo.getCustodiaID();

            AnexoFull anexoFull = new AnexoFull(anexo);

            IDocumentCustodyPlugin custody = null;
            if(anexo.isJustificante()){
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            }else{
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
            }

            anexoFull.setDocumentoCustody(custody.getDocumentInfoOnly(custodyID));
            anexoFull.setDocumentoFileDelete(false);
            anexoFull.setSignatureCustody(custody.getSignatureInfoOnly(custodyID));
            anexoFull.setSignatureFileDelete(false);

            if (log.isDebugEnabled()) {
              log.debug("SIGNATURE " + custody.getSignatureInfoOnly(custodyID));
              log.debug("DOCUMENT " + custody.getDocumentInfoOnly(custodyID));
              log.debug("modoFirma " + anexo.getModoFirma());
            }


            return anexoFull;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new I18NException(e, "anexo.error.obteniendo",
                    new I18NArgumentString(String.valueOf(anexoID)),
                    new I18NArgumentString(e.getMessage()));

        }
    }


    @Override
    public AnexoFull getAnexoFull(Long anexoID) throws I18NException {

        try {
            Anexo anexo = em.find(Anexo.class, anexoID);

            String custodyID = anexo.getCustodiaID();

            AnexoFull anexoFull = new AnexoFull(anexo);

            IDocumentCustodyPlugin custody = null;
            if(anexo.isJustificante()){
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            }else{
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
            }

            anexoFull.setDocumentoCustody(custody.getDocumentInfo(custodyID));

            anexoFull.setDocumentoFileDelete(false);

            anexoFull.setSignatureCustody(custody.getSignatureInfo(custodyID));

            anexoFull.setSignatureFileDelete(false);

            return anexoFull;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new I18NException(e, "anexo.error.obteniendo",
                    new I18NArgumentString(String.valueOf(anexoID)),
                    new I18NArgumentString(e.getMessage()));

        }
    }


    /**
     * Método que crea un anexo
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
    public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                Long registroID, String tipoRegistro) throws I18NException, I18NValidationException {

        return crearJustificanteAnexo(anexoFull, usuarioEntidad, registroID, tipoRegistro, null);

    }

        public AnexoFull crearJustificanteAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                Long registroID, String tipoRegistro, String custodyID) throws I18NException, I18NValidationException{

        IDocumentCustodyPlugin custody = null;
        boolean error = false;
        final boolean isNew = true;
        try {

            Anexo anexo = anexoFull.getAnexo();

            // Validador
            validateAnexo(anexo, isNew);

            anexo.setFechaCaptura(new Date());

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

            if (anexo.isJustificante()) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            }else{
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
            }


            //Obtenemos el registro con sus anexos, interesados y tipo Asunto
            IRegistro registro = getIRegistro(registroID, tipoRegistro, anexo, isNew);
            anexo.setRegistroDetalle(registro.getRegistroDetalle());
            
            
            // ---------- BBDD -------------
            // Guardamos el anexo per a que tengui ID
            anexo = this.persist(anexo);
            

            // ----------- CUSTODIA -----------------
            
            final Map<String, Object> custodyParameters;
            custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);

            //Reservamos el custodyID
            if(custodyID==null) {
                custodyID = custody.reserveCustodyID(custodyParameters);
                log.info("reserveCustodyID=" + custodyID);
            }
            anexo.setCustodiaID(custodyID);

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

            return anexoFull;

        } catch (I18NException i18n) {
            error = true;
            throw i18n;
        } catch (Exception e) {
            error = true;
            log.error("Error creant un anexe: " + e.getMessage(), e);
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
     *
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
            Long registroID, String tipoRegistro, boolean isJustificante,boolean noWeb)
                throws I18NException, I18NValidationException {

        try {


            Anexo anexo = anexoFull.getAnexo();

            // Validador
            final boolean isNew = false;
            validateAnexo(anexo, isNew);

            anexo.setFechaCaptura(new Date());


            IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);

            //Obtenemos el registro con sus anexos, interesados y tipo Asunto
            IRegistro registro = getIRegistro(registroID, tipoRegistro, anexo, isNew);

            //Actualizamos custodia solo cuando no venimos via web
            if(noWeb) {
                final Map<String, Object> custodyParameters;
                custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);

                final String custodyID = anexo.getCustodiaID();
                //Guardamos los cambios en custodia
                updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
                        registro, isNew);
            }

            //Actualizamos los datos de anexo
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
     *
     * @param registroID
     * @param tipoRegistro
     * @param anexo
     * @param isNou
     * @return
     * @throws Exception
     */
    protected IRegistro getIRegistro(Long registroID, String tipoRegistro, Anexo anexo, boolean isNou) throws Exception {
        IRegistro registro;
        IRegistro cloneRegistro;
        if ("entrada".equals(tipoRegistro)) {
            registro = registroEntradaEjb.findById(registroID);

        } else {
            registro = registroSalidaEjb.findById(registroID);

        }

        Hibernate.initialize(registro.getRegistroDetalle().getTipoAsunto());
        Hibernate.initialize(registro.getRegistroDetalle().getInteresados());

        if ("entrada".equals(tipoRegistro)) {
            cloneRegistro = new RegistroEntrada((RegistroEntrada) registro);
        } else {
            cloneRegistro = new RegistroSalida((RegistroSalida) registro);
        }


        List<Anexo> anexos = Anexo.clone(registro.getRegistroDetalle().getAnexos());

        cloneRegistro.getRegistroDetalle().setAnexos(anexos);


        return cloneRegistro;
    }

    /**
     *
     * @param anexoFull
     * @param usuarioEntidad
     * @param registroID
     * @param tipoRegistro
     * @param isNew
     * @throws Exception
     * @throws I18NException
     */
    protected void crearHistorico(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                  Long registroID, String tipoRegistro, boolean isNew) throws Exception, I18NException {
        Entidad entidadActiva = usuarioEntidad.getEntidad();
        if ("entrada".equals(tipoRegistro)) {
            RegistroEntrada registroEntrada = registroEntradaEjb.findById(registroID);
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
     * Método que crea/actualiza un anexo en función de lo que recibe en anexoFull
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
                throw new I18NException("anexo.error.sinfichero");
            }
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && (anexoFull.getDocumentoCustody() == null || anexoFull.getSignatureCustody() == null)) {
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
            mimeFinal = arreglarDocumentCustody(documentCustody, custodyID, anexo, mimeFinal);

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
                mimeFinal = arreglarDocumentCustody(documentCustody, custodyID, 
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
                    mimeFinal = arreglarDocumentCustody(documentCustody, custodyID, 
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
  
              if (debug) {
                log.info("MESTAS ORIG SIZE POST = " + metadades.size());
              }
  
          }
  
          // fechaDeEntradaEnElSistema
          if (updateDate) {

              metadades.add(new Metadata("anexo.fechaCaptura", anexo.getFechaCaptura()));
            
              // Afegida Nova Metadada
              metadades.add(new Metadata(MetadataConstants.ENI_FECHA_INICIO, 
                  org.fundaciobit.plugins.utils.ISO8601.dateToISO8601(anexo.getFechaCaptura())));
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
            // TODO A quin tipus ENI es correspon AIXÒ !!!!
            metadades.add(new Metadata("anexo.tipoDocumento",
                      I18NLogicUtils.tradueix(loc, "tipoDocumento.0" + anexo.getTipoDocumento())));
          }
  
          if (registro.getOficina() != null && registro.getOficina().getNombreCompleto() != null) {
            
            metadades.add(new Metadata("oficina", registro.getOficina().getNombreCompleto()));
            
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
  
          /**
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
  
          // XMAS SAVEALL custody.updateMetadata(custodyID, metadades.toArray(new Metadata[metadades.size()]), custodyParameters);
          custody2.saveAll(custodyID, custodyParameters, documentCustody,
              signatureCustody, metadades.toArray(new Metadata[metadades.size()]));
        }

    }


    /**
     *
     * @param doc
     * @param custodyID
     * @param anexo
     * @param mimeFinal
     * @return
     * @throws Exception
     */
    public String arreglarDocumentCustody(DocumentCustody doc,
         String custodyID, Anexo anexo,  String mimeFinal) throws Exception {

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
     *
     * @param signature
     * @param doc
     * @param anexo
     * @param mimeFinal
     * @return
     * @throws Exception
     */
    public String arreglarSignatureCustody(SignatureCustody signature,
        DocumentCustody doc,  Anexo anexo, String mimeFinal) throws Exception {
      //Obtenemos la firma que nos envian

      if (signature != null && signature.getData() != null) {//Si nos envian firma
      
       
        //Preparamos todos los datos para guardar la firma en custodia.
        String signType = (doc == null) ? SignatureCustody.OTHER_SIGNATURE_WITH_ATTACHED_DOCUMENT : SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT;
        
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


    protected String checkFileName(String name, String defaultName) throws Exception {
        if (name == null || name.trim().length() == 0) {
            return defaultName;
        } else {
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
        AnexoFull anexoFull, UsuarioEntidad usuarioEntidad)  {

        
        Map<String,Object> custodyParameters = new HashMap<String, Object>();

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
    public List<Anexo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select anexo from Anexo as anexo order by anexo.id");

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    /**
     * Método que devuelve todos los anexos de un registro de entrada sin el justificante
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public List<AnexoFull> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException {

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosSinJustificante = new ArrayList<AnexoFull>();

        for(Anexo anexo:anexos){
            if(!anexo.isJustificante()){
                anexosSinJustificante.add(getAnexoFullLigero(anexo.getId()));
            }
        }
        return anexosSinJustificante;
    }

    @Override
    public List<AnexoFull> getByRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException {

        List<Anexo> anexos = registroSalida.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosSinJustificante = new ArrayList<AnexoFull>();

        for(Anexo anexo:anexos){
            if(!anexo.isJustificante()){
                anexosSinJustificante.add(getAnexoFullLigero(anexo.getId()));
            }
        }
        return anexosSinJustificante;

    }

    @Override
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception {
        Query query = em.createQuery("Select anexo from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle order by anexo.id");
        query.setParameter("idRegistroDetalle", idRegistroDetalle);
        return query.getResultList();

    }

    @Override
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
    public Long getIdJustificante(Long idRegistroDetalle) throws Exception {
        Query query = em.createQuery("Select anexo.id from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle and " +
                "anexo.justificante = true");
        query.setParameter("idRegistroDetalle", idRegistroDetalle);

        List<Long> justificante = query.getResultList();

        if (justificante.size() == 1) {
            return justificante.get(0);
        } else {
            return null;
        }

    }


    /* METODOS DEL AnnexDocumentCustodyManager.java hecho por marilen del TODO DE TONI*/


    /**
     * Obtiene la info del fichero existente en el sistema de archivos
     * (No obtiene el array de bytes)
     *
     * @param custodiaID
     * @return
     */
    @Override
    public DocumentCustody getArchivo(String custodiaID, boolean isJustificante) throws I18NException, Exception {

        IDocumentCustodyPlugin custody;

        if (custodiaID == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if(isJustificante){
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
        } else{
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getDocumentInfo(custodiaID);

    }


    /**
     * Obtiene la info del fichero existente en el sistema de archivos
     * (No obtiene el array de bytes)
     *
     * @param custodiaID
     * @return
     */
    @Override
    public byte[] getArchivoContent(String custodiaID, boolean isJustificante) throws I18NException, Exception {

        IDocumentCustodyPlugin custody = null;

        if (custodiaID == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if(isJustificante) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
        } else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getDocument(custodiaID);

    }


    @Override
    public DocumentCustody getDocumentInfoOnly(String custodiaID) throws Exception, I18NException {
        IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        return custody.getDocumentInfoOnly(custodiaID);
    }

    @Override
    public SignatureCustody getSignatureInfoOnly(String custodiaID) throws Exception, I18NException {
        IDocumentCustodyPlugin custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        return custody.getSignatureInfoOnly(custodiaID);
    }

    /**
     * Obtiene la info de la firma existente en el sistema de archivos
     * (No obtiene el array de bytes)
     *
     * @param custodiaID
     * @return
     */
    public SignatureCustody getFirma(String custodiaID, boolean isJustificante) throws I18NException, Exception {

        IDocumentCustodyPlugin custody = null;

        if (custodiaID == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if(isJustificante) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        } else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getSignatureInfo(custodiaID);
    }


    @Override
    public byte[] getFirmaContent(String custodiaID, boolean isJustificante) throws Exception, I18NException {

        IDocumentCustodyPlugin custody = null;

        if (custodiaID == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }
        if(isJustificante) {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        }else {
            custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
        }

        return custody.getSignature(custodiaID);
    }


    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    public boolean eliminarCustodia(String custodiaID, boolean isJustificante) throws Exception, I18NException {

        if (custodiaID == null) {
            log.warn("eliminarCustodia :: CustodiaID vale null !!!!!", new Exception());
            return false;
        } else {
            IDocumentCustodyPlugin custody;
            if(isJustificante) {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            } else {
                custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA);
            }

            custody.deleteCustody(custodiaID);

            return true;

        }

    }


    /**
     * Crea o actualiza un anexos en el sistema de custodia
     * TODO PENDENT D'EMPLEAR PER LES PROVES RECEPCIO SIR, pero hauria de retornar
     *
     * @param name
     * @param file
     * @param signatureName
     * @param signature
     * @param signatureMode
     * @param custodyID         Si vale null significa que creamos el archivo. Otherwise actualizamos el fichero.
     * @param custodyParameters JSON del registre
     * @return Identificador de custodia
     * @throws Exception
     */
    // TODO mime de doc i firma
    /*
    public String crearArchivo(String name, byte[] file, String signatureName,
                               byte[] signature, int signatureMode, String custodyID,
                               Map<String, Object> custodyParameters, boolean isJustificante) throws Exception {

        IDocumentCustodyPlugin instance = getInstance(isJustificante);

        if (custodyID == null) {
            custodyID = instance.reserveCustodyID(custodyParameters);
        }


        if (signatureMode == RegwebConstantes.TIPO_FIRMA_CSV) {
            // CSV == Referencia a DocumentCustody
            throw new Exception("Modo de firma(signatureMode) RegwebConstantes.TIPO_FIRMA_CSV "
                    + " no suportat. Es suportarà a partir de la posada en marxa de l'Arxiu Electrònic");
        }


        if (name != null && file != null) {
            DocumentCustody document = new DocumentCustody(name, file);
            instance.saveDocument(custodyID, custodyParameters, document);
        }


        if (signatureName != null && signature != null) {

            SignatureCustody docSignature = new SignatureCustody();
            docSignature.setName(signatureName);
            docSignature.setData(signature);

            final long signType = (long) signatureMode;

            // Cases en doc. doc/AnalisiGestioDocumentsSIRDocumentCustodyAPI2Regweb.odt

            if (signType == RegwebConstantes.TIPO_FIRMA_XADES_DETACHED_SIGNATURE) {
                // Cas 4
                docSignature.setSignatureType(SignatureCustody.XADES_SIGNATURE);
                docSignature.setAttachedDocument(false);
            } else if (signType == RegwebConstantes.TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE) {
                // CAS 3
                docSignature.setSignatureType(SignatureCustody.XADES_SIGNATURE);
                docSignature.setAttachedDocument(true);
            } else if (signType == RegwebConstantes.TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE) {
                // CAS 4
                docSignature.setSignatureType(SignatureCustody.CADES_SIGNATURE);
                docSignature.setAttachedDocument(false);
            } else if (signType == RegwebConstantes.TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE) {
                // CAS 3
                docSignature.setSignatureType(SignatureCustody.CADES_SIGNATURE);
                docSignature.setAttachedDocument(true);
            } else if (signType == RegwebConstantes.TIPO_FIRMA_PADES) {
                // CAS 5
                docSignature.setSignatureType(SignatureCustody.PADES_SIGNATURE);
                docSignature.setAttachedDocument(null);
            } else { //    default:
                String msg = "No es suporta signatureMode amb valor  " + signatureMode;
                log.error(msg, new Exception());
                throw new Exception(msg);
            }


            instance.saveSignature(custodyID, custodyParameters, docSignature);
        }

        //log.info("Creamos el file: " + getArchivosPath()+dstId.toString());

        return custodyID;
    }
*/

    /**
     * Crea un Jusitificante, lo firma y lo crea como anexo al registro
     *
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws Exception
     */
    @Override
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad,
        IRegistro registro, String tipoRegistro, String idioma) 
                throws I18NException, I18NValidationException {
      try {
          final Long idEntidad = usuarioEntidad.getEntidad().getId();

          // Carregam el plugin del Justificant
          IJustificantePlugin justificantePlugin = (IJustificantePlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_JUSTIFICANTE);

          // Comprova que existeix el plugin de justificant
          if(justificantePlugin == null) {
              // No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
              throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.1"));
          }

          // Carregam el plugin del Custodia del Justificante
          IDocumentCustodyPlugin documentCustodyPlugin = (IDocumentCustodyPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

          // Comprova que existeix el plugin de Custodia del Justificante
          if(documentCustodyPlugin == null) {
              // No s´ha definit cap plugin de Custòdia-Justificant. Consulti amb el seu Administrador.
              throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.7"));
          }

          // Cerca el Plugin de Justificant definit a les Propietats Globals
          ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

          // Comprova que existeix el plugin de justificant
          if(signaturePlugin == null) {
              // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
              throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
          }


          // Mensajes traducidos
          Locale locale = new Locale(idioma);
          String fileName  = I18NLogicUtils.tradueix(locale, "justificante.fichero") + "_" + registro.getNumeroRegistroFormateado() + ".pdf";
          String nombreFichero = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
          String tituloAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.titulo");
          String observacionesAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.observaciones");
          
          // Crea el anexo del justificante firmado
          AnexoFull anexoFull = new AnexoFull();
          Anexo anexo = anexoFull.getAnexo();
          anexo.setTitulo(tituloAnexo);
          anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
          anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
          anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
          anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
          anexo.setObservaciones(observacionesAnexo);
          anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
          anexo.setJustificante(true);

          // Generam la Custòdia per tenir el CSV
          Map<String,Object> custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);

          String custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
          Metadata mcsv = documentCustodyPlugin.getOnlyOneMetadata(custodyID, MetadataConstants.ENI_CSV);

          String csv = null;
          if(mcsv != null) {
              csv = mcsv.getValue();
          }
          anexo.setCsv(csv);
          
          String url = documentCustodyPlugin.getValidationUrl(custodyID, custodyParameters);
          String specialValue = documentCustodyPlugin.getSpecialValue(custodyID,custodyParameters);

          // Obtenim el ByteArray per generar el pdf
          byte[] pdfSignat;
          if (registro instanceof RegistroEntrada) {
            pdfSignat = justificantePlugin.generarJustificanteEntrada((RegistroEntrada)registro, url, specialValue, csv, idioma);
          } else {
            pdfSignat = justificantePlugin.generarJustificanteSalida((RegistroSalida)registro, url, specialValue, csv, idioma);
          }

          // Cream l'annex justificant i el firmam
          // Firma el justificant
          SignatureCustody sign = signatureServerEjb.signJustificante(pdfSignat, idioma, idEntidad);
          sign.setName(nombreFichero);

          anexoFull.setSignatureCustody(sign);
          anexoFull.setSignatureFileDelete(false);
          anexoFull.getAnexo().setSignType("PAdES");
          anexoFull.getAnexo().setSignFormat("implicit_enveloped/attached");
          anexoFull.getAnexo().setSignProfile("AdES-EPES");

          // Cream l'annex justificant
          anexoFull = crearJustificanteAnexo(anexoFull, usuarioEntidad, registro.getId(), tipoRegistro,custodyID);

          return anexoFull;
      } catch(I18NValidationException i18nve) {
        throw i18nve;
      } catch(I18NException i18ne) {
        throw i18ne;
      } catch(Exception e) {
        throw new I18NException(e, "error.desconegut",
            new I18NArgumentString("Error no controlat generant justificant: " + e.getMessage()));
      }

    }

    

    

    /* FIN METODOS DEL AnnexDocumentCustodyManager.java hecho por marilen del TODO DE TONI*/
}
