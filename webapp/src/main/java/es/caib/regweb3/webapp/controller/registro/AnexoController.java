package es.caib.regweb3.webapp.controller.registro;

import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.persistence.ejb.SignatureServerLocal;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.AnexoWebValidator;
import org.apache.commons.lang.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NTranslation;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.scanweb.api.ScanWebPlainFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

/**
 * Created 3/06/14 14:22
 * <p>
 * Controller para gestionar la parte común de los anexos
 *
 * @author mgonzalez
 * @author anadal (plugin de custodia, errors i refactoring no ajax)
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 */
@Controller
@RequestMapping(value = "/anexo")
@SessionAttributes(types = {AnexoForm.class})
public class AnexoController extends BaseController {

    @Autowired
    private AnexoWebValidator anexoValidator;


    @EJB(mappedName = AnexoLocal.JNDI_NAME)
    private AnexoLocal anexoEjb;

    @EJB(mappedName = RegistroDetalleLocal.JNDI_NAME)
    private RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = ScanWebModuleLocal.JNDI_NAME)
    private ScanWebModuleLocal scanWebModuleEjb;

    @EJB(mappedName = SignatureServerLocal.JNDI_NAME)
    private SignatureServerLocal signatureServerEjb;

    @Autowired
    ArxiuCaibUtils arxiuCaibUtils;



    @RequestMapping(value = "/nou2", method = RequestMethod.GET)
    public String crearAnexoGet2(HttpServletRequest request, Model model ) throws I18NException, Exception {

        //Recibimos de sesión los datos en anexoForm
        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");
        model.addAttribute("anexoForm", anexoForm);

        //Cargamos atributos comunes
        loadCommonAttributes(request, model, anexoForm.getAnexo().getScan());

        return "registro/formularioAnexo";
    }


    @RequestMapping(value = "/nou", method = RequestMethod.POST)
    public String crearAnexoPost(@ModelAttribute AnexoForm anexoForm, BindingResult result, HttpServletRequest request,
                                 HttpServletResponse response, Model model) throws I18NException {

        Entidad entidad = getEntidadActiva(request);
        //Validamos el anexo
        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {

                //Creamos el anexo
                anexoEjb.crearAnexo(anexoForm, getUsuarioEntidadActivo(request), entidad,
                        anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), null, false);

                //Actualizamos el contador de anexos creados
                anexoForm.setNumAnexosRecibidos(anexoForm.getNumAnexosRecibidos()-1);
                //fijamos el id a null despues de crearlo para que pueda seguir procesando los siguientes.
                anexoForm.getAnexo().setId(null);
                //Si no quedan más anexos acabamos proceso
                if(anexoForm.getNumAnexosRecibidos() == 0){
                    model.addAttribute("closeAndReload", "true");
                    return "registro/formularioAnexo";
                }else { //si quedan más anexos volvemos a coger el siguiente a procesar
                    return "redirect:/anexoScan/transforma";
                }
            } catch (I18NValidationException i18n) {
                log.error(i18n.getMessage(), i18n);
                // TODO
                Mensaje.saveMessageError(request, i18n.getMessage());
            } catch (I18NException i18n) {
                log.debug(i18n.getMessage(), i18n);
                Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));
            }

        }

        // si hay errores, volvemos al formulario cargando los valores comunes
        loadCommonAttributes(request, model, anexoForm.getAnexo().getScan());

        return "registro/formularioAnexo";

    }


    /*
     Prepara los datos de un anexo para su edición
     */
    @RequestMapping(value = "/editar/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String editarAnexoGet(HttpServletRequest request,
                                 HttpServletResponse response, @PathVariable Long registroDetalleID,
                                 @PathVariable Long tipoRegistro, @PathVariable Long registroID,
                                 @PathVariable Long anexoID, @PathVariable boolean isOficioRemisionSir, Model model) throws I18NException, Exception {

        final boolean debug = log.isDebugEnabled();

        if (debug) {
            log.debug(" Passa per AnexoController::editarAnexoGet("
                    + "registroDetalleID = " + registroDetalleID
                    + " | tipoRegistro = " + tipoRegistro
                    + " | registroID = " + registroID
                    + " | anexoID = " + anexoID + ")");
        }

        AnexoFull anexoFull2 = anexoEjb.getAnexoFullLigero(anexoID, getEntidadActiva(request).getId());
        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, anexoID, isOficioRemisionSir);

        //Preparamos el formulario con los datos a mostrar
        AnexoForm anexoForm = new AnexoForm(anexoFull2);
        anexoForm.setRegistroID(registroID);
        anexoForm.setIdRegistroDetalle(registroDetalleID);
        anexoForm.setTipoRegistro(tipoRegistro);
        anexoForm.setOficioRemisionSir(isOficioRemisionSir);

        if (debug) {

            log.info(" Carregant ANEXO: ");
            log.info("     - anexoFull.getDocumentoCustody(): " + anexoForm.getDocumentoCustody());
            if (anexoForm.getDocumentoCustody() != null) {
                log.info("     - anexoFull.getDocumentoCustody().getName(): " + anexoForm.getDocumentoCustody().getName());
            }
            log.info("     - anexoFull.getSignatureCustody(): " + anexoForm.getSignatureCustody());
            if (anexoForm.getSignatureCustody() != null) {
                log.info("     - anexoFull.getSignatureCustody().getName(): " + anexoForm.getSignatureCustody().getName());
            }
        }

        final String scanWebID = String.valueOf(registroID);
        scanWebModuleEjb.closeScanWebProcess(request, scanWebID);

        //Cargamos los atributos comunes
        loadCommonAttributes(request, model, anexoForm.getAnexo().getScan());


        model.addAttribute("anexoForm", anexoForm);

        return "registro/formularioAnexo";

    }


    /**
     * Modifica los datos de un anexo
     */
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String editarAnexoPost(@ModelAttribute AnexoForm anexoForm, BindingResult result, HttpServletRequest request,
                                  HttpServletResponse response, Model model) throws Exception, I18NValidationException, I18NException {

        Entidad entidad = getEntidadActiva(request);
        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {
                anexoEjb.actualizarAnexo(anexoForm, getUsuarioEntidadActivo(request), entidad, registroDetalleEjb.getReference(anexoForm.getIdRegistroDetalle()),
                        anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), anexoForm.getAnexo().isJustificante(), false);

                model.addAttribute("closeAndReload", "true");
                return "registro/formularioAnexo";
            } catch (I18NException i18n) {
                log.error(i18n.getMessage(), i18n);
                Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Mensaje.saveMessageError(request, e.getMessage());
            }

        }
        loadCommonAttributes(request, model, anexoForm.getAnexo().getScan());

        return "registro/formularioAnexo";

    }


    /**
     * Elimina un Anexo del registroDetalle
     *
     * @param registroDetalleID
     * @param tipoRegistro
     * @param registroID
     * @param anexoID
     * @return
     */
    @RequestMapping(value = "/delete/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}", method = RequestMethod.GET)
    public String eliminarAnexo(@PathVariable Long registroDetalleID,
                                @PathVariable Long tipoRegistro, @PathVariable Long registroID,
                                @PathVariable Long anexoID, HttpServletRequest request) {

        try {
            registroDetalleEjb.eliminarAnexoRegistroDetalle(anexoID, registroDetalleID, getEntidadActiva(request).getId());
        } catch (I18NException i18ne) {
            String msg = I18NUtils.getMessage(i18ne);
            log.error(msg, i18ne);
            Mensaje.saveMessageError(request, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
        }

        return getRedirectURL2(request, tipoRegistro, registroID);
    }

    /**
     * Método que monta la url a donde ir después de eliminar un anexo
     */
    protected String getRedirectURL2(HttpServletRequest request, Long tipoRegistro,
                                     Long registroID) {
        if (tipoRegistro == null) {
            return request.getContextPath();

        } else {
            String nombreCompleto = getNombreCompletoTipoRegistro(tipoRegistro);
            if (registroID == null || registroID == 0) {
                return "redirect:/" + nombreCompleto + "/list";
            } else {
                return "redirect:/" + nombreCompleto + "/" + registroID + "/detalle";
            }
        }
    }


    protected String getNombreCompletoTipoRegistro(Long tipoRegistro) {

        if(tipoRegistro.equals(REGISTRO_ENTRADA)){
            return "registroEntrada";
        }else{
            return "registroSalida";
        }
    }


    /**
     * Función que nos permite mostrar el contenido de un anexo
     *
     * @param anexoId identificador del anexo
     */
    @RequestMapping(value = "/descargarDocumento/{anexoId}", method = RequestMethod.GET)
    public void anexo(@PathVariable("anexoId") Long anexoId, HttpServletRequest request,
                      HttpServletResponse response) throws Exception, I18NException {

        AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexoId, getEntidadActiva(request).getId());
        fullDownload(anexoFull.getAnexo(), anexoFull.getDocumentoCustody().getMime(), false, response, getEntidadActiva(request).getId(), false);
    }

    /**
     * Función que nos permite mostrar el contenido de un firma de un anexo
     *
     * @param anexoId identificador del anexo
     */
    @RequestMapping(value = "/descargarFirma/{anexoId}/{original}", method = RequestMethod.GET)
    public void firma(@PathVariable("anexoId") Long anexoId, @PathVariable("original") Boolean original, HttpServletRequest request,
                      HttpServletResponse response) throws Exception, I18NException {

        AnexoFull anexo = anexoEjb.getAnexoFullLigero(anexoId, getEntidadActiva(request).getId());
        //Parche para la api de custodia antigua que se guardan los documentos firmados (modofirma == 1 Attached) en DocumentCustody.
        if (anexo.getSignatureCustody() == null) {//Api antigua, hay que descargar el document custody
            fullDownload(anexo.getAnexo(), anexo.getDocumentoCustody().getMime(), false, response, getEntidadActiva(request).getId(), original);
        } else {
            fullDownload(anexo.getAnexo(), anexo.getSignatureCustody().getMime(), true, response, getEntidadActiva(request).getId(), original);
        }

    }


    /**
     * Función que nos permite mostrar el contenido del separador de digitalizacionMasiva
     *
     *
     */
    @RequestMapping(value = "/descargarSeparador", method = RequestMethod.GET)
    public void separador(HttpServletRequest request, HttpServletResponse response) throws Exception, I18NException {

        Entidad entidadActiva = getEntidadActiva(request);
        String languageUI = request.getParameter("lang");

        if (languageUI == null) {
            languageUI = I18NUtils.getLocale().getLanguage();
        }

        ScanWebPlainFile separador = scanWebModuleEjb.obtenerDocumentoSeparador(entidadActiva.getId(), languageUI);

        AnexoUtils.download(separador.getMime(), response, separador.getName(), separador.getData());

    }

    /**
     * Función que nos permite descargar el ustificante generado con el Api ArxiuCaiJb
     *
     * @param anexoId identificador del anexo
     */
    @RequestMapping(value = "/descargarJustificante/{anexoId}/{original}", method = RequestMethod.GET)
    public void descargarJustificante(@PathVariable("anexoId") Long anexoId, @PathVariable("original") Boolean original, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception, I18NException {

        Entidad entidadActiva = getEntidadActiva(request);
        Anexo anexo = anexoEjb.findById(anexoId);

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){
            IArxiuPlugin iArxiuPlugin = arxiuCaibUtils.cargarPlugin(entidadActiva.getId());

            // Comprova que existeix el plugin de Arxiu del Justificante
            if (iArxiuPlugin == null) {
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.10"));
            }

            Document justificante = arxiuCaibUtils.getDocumento(anexo.getCustodiaID(), null, true, original);

            if(justificante != null){
                AnexoUtils.download(justificante.getContingut().getTipusMime(), response, justificante.getNom(), justificante.getContingut().getContingut());
            }else {
                Mensaje.saveMessageError(request, getMessage("justificante.noExiste", anexo.getCustodiaID()));
                response.sendRedirect("/regweb3/inici");
                log.info("No se ha obtenido el  justificante");
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexoId, getEntidadActiva(request).getId());

            //Parche para la api de custodia antigua que se guardan los documentos firmados (modofirma == 1 Attached) en DocumentCustody.
            if (anexoFull.getSignatureCustody() == null) {//Api antigua, hay que descargar el document custody
                fullDownload(anexoFull.getAnexo(), anexoFull.getDocumentoCustody().getMime(),
                        false, response, getEntidadActiva(request).getId(), original);
            } else {
                fullDownload(anexoFull.getAnexo(), anexoFull.getSignatureCustody().getMime(),
                        true, response, getEntidadActiva(request).getId(), original);
            }
        }
    }


    /**
     * Función que obtiene los datos de un archivo del sistema de custodia para mostrarlo
     *
     * @param anexo     identificador del archivo
     * @param contentType
     * @param firma
     * @param response
     * @return Descarga el archivo y además devuelve true o false en funcion de si ha encontrado el archivo indicado.
     * En la api de custodia antigua, los documentos firmados se guardaban en DocumentCustody y en la nueva en SignatureCustody.
     * Por tanto cuando vaya a recuperar un documento con firma antiguo, mirarà en SignatureCustody y no lo encontrará, por tanto controlamos ese caso y devolvemos false.
     * para poder ir a buscarlo a DocumentCustody, que es donde estará. (todo esto se hace en el método firma)
     */
    private void fullDownload(Anexo anexo, String contentType, boolean firma, HttpServletResponse response, Long idEntidad, boolean original) {

        String filename = null;
        OutputStream output;
        byte[] data;

        try {

            // Si l'arxiu té identificador, entram
            if (anexo.getCustodiaID() != null) {

                // Si és un arxiu sense firma
                if (!firma) {
                    DocumentCustody dc = anexoEjb.getArchivo(anexo, idEntidad);
                    filename = dc.getName();
                    data = dc.getData();
                } else {   // Si és firma d'un arxiu
                    if (original) {
                        SignatureCustody dc = anexoEjb.getFirma(anexo, idEntidad);
                        filename = dc.getName();
                        data = dc.getData();
                    } else {
                        AnexoSimple anexoSimple = anexoEjb.descargarFirmaDesdeUrlValidacion(anexo, idEntidad);
                        data = anexoSimple.getData();
                        filename = anexoSimple.getFilename();
                    }
                }

                AnexoUtils.download(contentType, response, filename, data);
            }
        } catch (I18NException i18ne) {
            log.error(I18NUtils.getMessage(i18ne), i18ne);
        } catch (NumberFormatException e) {
            // TODO QUE FER
            log.info(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Función que nos permite mostrar el contenido de un documentCustody en session
     */
    @RequestMapping(value = "/descargarDocumentoCustody", method = RequestMethod.GET)
    public void descargarDocumentoCustody(HttpServletRequest request,
                                          HttpServletResponse response) throws Exception, I18NException {

        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");

        byte[] data = anexoForm.getDocumentoCustody().getData();
        String contentType = anexoForm.getDocumentoCustody().getMime();
        String filename = anexoForm.getDocumentoCustody().getName();

        AnexoUtils.download(contentType, response, filename, data);
    }


    /**
     * Función que nos permite mostrar el contenido de un signatureCustody en session
     */
    @RequestMapping(value = "/descargarSignatureCustody", method = RequestMethod.GET)
    public void descargarSignatureCustody(HttpServletRequest request,
                                          HttpServletResponse response) throws Exception, I18NException {

        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");
        byte[] data = anexoForm.getSignatureCustody().getData();
        String contentType = anexoForm.getSignatureCustody().getMime();
        String filename = anexoForm.getSignatureCustody().getName();

        AnexoUtils.download(contentType, response, filename, data);
    }

    /**
     * Guarda en sesión los datos asociados al ultimo anexo
     *
     * @param request
     * @param registroDetalleID
     * @param registroID
     * @param tipoRegistro
     * @param anexoID
     * @param isOficioRemisionSir
     */
    protected void saveLastAnnexoAction(HttpServletRequest request, Long registroDetalleID,
                                        Long registroID, Long tipoRegistro, Long anexoID, boolean isOficioRemisionSir) {
        HttpSession session = request.getSession();
        session.setAttribute("LAST_registroDetalleID", registroDetalleID);
        session.setAttribute("LAST_tipoRegistro", tipoRegistro);
        session.setAttribute("LAST_registroID", registroID);
        session.setAttribute("LAST_anexoID", anexoID); // nou = null o editar != null
        session.setAttribute("LAST_isOficioRemisionSir", isOficioRemisionSir);
    }


    /**
     * Se cargan los atributos comunes de los anexos
     *
     * @param request
     * @param model
     * @throws Exception
     */
    protected void loadCommonAttributes(HttpServletRequest request, Model model, Boolean scan) throws I18NException {
        model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO_REGISTRO_PRESENCIAL);
        model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);

        // Tipos Validez según casuistica
        if(!scan){ // Desde archivo

            if(Configuracio.isCAIB() && getUsuarioAutenticado(request).getDib_user_rw()) { //  Tiene el rol DIB_USER_RW activo
                model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
            }else{
                model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_ARCHIVO);
            }

        }else{ // Se trata de un Scan
            if(Configuracio.isCAIB() && getUsuarioAutenticado(request).getDib_user_rw()) { //  Tiene el rol DIB_USER_RW activo

                model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_SCAN_ORIGINAL);

            }else if(Configuracio.isCAIB() && !getUsuarioAutenticado(request).getDib_user_rw()){ // NO tiene el rol DIB_USER_RW activo

                model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_SCAN);
            } else{

                model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_SCAN_ORIGINAL);
            }
        }
    }


    /**
     * Obtiene los anexos completos del registro indicado
     *
     * @param idRegistro
     * @param tipoRegistro
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private List<AnexoFull> obtenerAnexosFullByRegistro(Long idRegistro, Long tipoRegistro) throws Exception, I18NException {
        if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFullLigero(idRegistro);
            return registroEntrada.getRegistroDetalle().getAnexosFull();

        } else {
            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFullLigero(idRegistro);
            return registroSalida.getRegistroDetalle().getAnexosFull();
        }
    }

    /**
     * Método que verifica si el anexo que se está creando no supera el tamano establecido por las propiedades SIR
     * y tiene una extensión de documento dentro de las permitidas
     *
     * @param registroID     identificador del registro al que se quiere asociar el anexo
     * @param tipoRegistro   si es "entrada" o "salida
     * @param docSize        tamaño del documento a anexar
     * @param firmaSize      tamaño de la firma a anexar
     * @param docExtension   extensión del documento a anexar
     * @param firmaExtension extensión de la firma a anexar
     * @param result
     * @param scan           true si viene de scan, false si no viene de scan
     * @throws Exception
     * @throws I18NException
     */
    public void validarLimitacionesSIRAnexos(Long registroID, Long tipoRegistro, long docSize,
                                             long firmaSize, String docExtension, String firmaExtension,
                                             BindingResult result, boolean scan) throws Exception, I18NException {

        // Obtenemos los anexos del registro para validar que no exceda el máximo de MB establecido
        List<AnexoFull> anexosFull = obtenerAnexosFullByRegistro(registroID, tipoRegistro);

        //Se suman las distintas medidas de los anexos que tiene el registro hasta el momento.
        long tamanyoTotalAnexos = AnexoUtils.obtenerTamanoTotalAnexos(anexosFull);


        Long tamanyoMaximoTotalAnexosSIR = PropiedadGlobalUtil.getTamanoMaxTotalAnexosSir();
        String maxTotalAnexos = RegwebUtils.bytesToHuman(tamanyoMaximoTotalAnexosSIR);

        Long tamanoMaximoPorAnexoSIR = PropiedadGlobalUtil.getTamanoMaximoPorAnexoSir();
        String sTamanoMaximoPorAnexoSIR = RegwebUtils.bytesToHuman(tamanoMaximoPorAnexoSIR);

        // Comprobamos que el nuevo anexo no supere el tamaño máximo total ni el maximo por anexo en sir
        if (docSize > 0 ) {
            String tamanoDoc= RegwebUtils.bytesToHuman(docSize);
            tamanyoTotalAnexos += docSize;
            if (tamanyoTotalAnexos > tamanyoMaximoTotalAnexosSIR) {
                String totalAnexos = RegwebUtils.bytesToHuman(tamanyoTotalAnexos );
                if (!scan) {
                    result.rejectValue("documentoFile", "tamanymaxtotalsuperat", new Object[]{totalAnexos, maxTotalAnexos}, I18NUtils.tradueix("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos));
                } else {
                    throw new I18NException("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos);
                }
            }
            if (docSize > tamanoMaximoPorAnexoSIR) {
                if (!scan) {
                    result.rejectValue("documentoFile", "tamanyfitxerpujatsuperat", new Object[]{tamanoDoc, sTamanoMaximoPorAnexoSIR}, I18NUtils.tradueix("tamanyfitxerpujatsuperat",tamanoDoc, sTamanoMaximoPorAnexoSIR));
                } else {
                    throw new I18NException("tamanyfitxerpujatsuperat", tamanoDoc, sTamanoMaximoPorAnexoSIR);
                }
            }
        } else {// Solo comprobamos el tamaño en el documento firma en el caso que el documento este vacio, ya que se trata de firma attached
            String tamanoFirma = RegwebUtils.bytesToHuman(firmaSize);
            tamanyoTotalAnexos += firmaSize;
            if (tamanyoTotalAnexos > tamanyoMaximoTotalAnexosSIR) {
                String totalAnexos = RegwebUtils.bytesToHuman(tamanyoTotalAnexos);
                if (!scan) {
                    result.rejectValue("firmaFile", "tamanymaxtotalsuperat", new Object[]{totalAnexos, maxTotalAnexos}, I18NUtils.tradueix("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos));
                } else {
                    throw new I18NException("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos);
                }
            }
            if (firmaSize > tamanoMaximoPorAnexoSIR) {
                if (!scan) {
                    result.rejectValue("firmaFile", "tamanyfitxerpujatsuperat", new Object[]{tamanoFirma, sTamanoMaximoPorAnexoSIR}, I18NUtils.tradueix("tamanyfitxerpujatsuperat", tamanoFirma, sTamanoMaximoPorAnexoSIR));
                } else {
                    throw new I18NException("tamanyfitxerpujatsuperat", tamanoFirma, sTamanoMaximoPorAnexoSIR);
                }
            }

        }

        //Validamos que las extensiones del documento y la firma esten dentro de los formatos permitidos.
        if (!docExtension.isEmpty()) {
            if (!Arrays.asList(RegwebConstantes.ANEXO_EXTENSIONES_SIR).contains(docExtension)) {
                if (!scan) {
                    result.rejectValue("documentoFile", "formatonopermitido", new Object[]{docExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR)}, I18NUtils.tradueix("formatonopermitido", docExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR)));
                } else {
                    throw new I18NException("formatonopermitido", docExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR));
                }
            }
        } else {// Solo comprobamos la extensión en el documento firma en el caso que el documento este vacio, ya que se trata de firma attached
            if (!Arrays.asList(RegwebConstantes.ANEXO_EXTENSIONES_SIR).contains(firmaExtension)) {
                if (!scan) {
                    result.rejectValue("firmaFile", "formatonopermitido", new Object[]{firmaExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR)}, I18NUtils.tradueix("formatonopermitido", firmaExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR)));
                } else {
                    throw new I18NException("formatonopermitido", firmaExtension, Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR));
                }

            }
        }

    }

    /**
     * Método que valida el nombre del fichero o firma anexado
     * @param anexoForm
     * @param result
     */
    public void validadNombreFichero(AnexoForm anexoForm, BindingResult result){

        if (anexoForm.getDocumentoFile() != null) {

            if(StringUtils.indexOfAny(anexoForm.getDocumentoFile().getOriginalFilename(), RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) != -1){
                result.rejectValue("documentoFile", "error.caracteres.noPermitidos", new Object[]{Arrays.toString(RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU)}, I18NUtils.tradueix("error.caracteres.noPermitidos", Arrays.toString(RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU)));

            }
        }

        if (anexoForm.getFirmaFile() != null) {
            if(StringUtils.indexOfAny(anexoForm.getFirmaFile().getOriginalFilename(), RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) != -1){
                result.rejectValue("firmaFile", "error.caracteres.noPermitidos", new Object[]{Arrays.toString(RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU)},I18NUtils.tradueix("error.caracteres.noPermitidos", Arrays.toString(RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU)));

            }
        }

    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));
    }


    /**
     * Método que valida la firma indicada en el AnexoForm que recibimos.
     * @param request
     * @param anexoForm
     * @throws I18NException
     */
    protected void validarAnexoForm(HttpServletRequest request, AnexoForm anexoForm) throws I18NException {
        Entidad entidad = getEntidadActiva(request);
        final boolean force = false; //Indica si queremos forzar la excepción.
        I18NTranslation i18n;
        if (anexoForm.getAnexo().getModoFirma() != RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {// Si no tiene firma no se valida
            i18n = signatureServerEjb.checkDocument(anexoForm, entidad.getId(),
                    I18NUtils.getLocale(), force);
            if (i18n != null) {
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix(i18n));
                Mensaje.saveMessageError(request, I18NUtils.tradueix("error.checkanexosir.avisaradministradors"));
            }
            if (anexoForm.getAnexo().getEstadoFirma() == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoForm.getAnexo().getEstadoFirma() == RegwebConstantes.ANEXO_FIRMA_ERROR) {
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("error.firmanovalida") + anexoForm.getAnexo().getMotivoNoValidacion());
            }
        }
    }


    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static long getDocSize(String custodiaID, Long idEntidad) throws Exception, I18NException {

        DocumentCustody dc = getAnexoLocalEJBStatic().getDocumentInfoOnly(custodiaID, idEntidad);
        if (dc == null) {
            return -1;
        }
        long size = dc.getLength();
        return formatFileSize(size);
    }


    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static long getSignSize(String custodiaID, Long idEntidad) throws Exception, I18NException {

        SignatureCustody sc = getAnexoLocalEJBStatic().getSignatureInfoOnly(custodiaID, idEntidad);
        if (sc == null) {
            return -1;
        }
        long size = sc.getLength();
        return formatFileSize(size);
    }

    protected static long formatFileSize(long size) {
        if (size < 1024) {
            return 1;
        } else {
            return size / 1024;
        }
    }

    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static String getDocName(String custodiaID, Long idEntidad) throws Exception, I18NException {

        DocumentCustody dc = getAnexoLocalEJBStatic().getDocumentInfoOnly(custodiaID, idEntidad);
        if (dc == null) {
            return "";
        }
        return dc.getName();
    }

    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static String getSignName(String custodiaID, Long idEntidad) throws Exception, I18NException {

        SignatureCustody sc = getAnexoLocalEJBStatic().getSignatureInfoOnly(custodiaID, idEntidad);
        if (sc == null) {
            return "";
        }
        return sc.getName();
    }


    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static String getDocMime(String custodiaID, Long idEntidad) throws Exception, I18NException {

        DocumentCustody dc = getAnexoLocalEJBStatic().getDocumentInfoOnly(custodiaID, idEntidad);
        if (dc == null) {
            return "";
        }
        return dc.getMime();
    }

    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static String getSignMime(String custodiaID, Long idEntidad) throws Exception, I18NException {

        SignatureCustody sc = getAnexoLocalEJBStatic().getSignatureInfoOnly(custodiaID, idEntidad);
        if (sc == null) {
            return "";
        }
        return sc.getMime();
    }

    /**
     * Método que descarga un fichero mediante HttpServletResponse response
     * @param contentType
     * @param response
     * @param filename
     * @param data
     * @throws IOException
     */
   /* private void download(String contentType, HttpServletResponse response, String filename, byte[] data) throws IOException, Exception {
        OutputStream output;

        // Obtenemos el ContentType si el que nos indican es null
        if (StringUtils.isEmpty(contentType)) {
            contentType = AnexoUtils.getContentType(filename, data);
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", AnexoUtils.getContentDispositionHeader(true, filename));
        response.setContentLength(data.length);

        output = response.getOutputStream();
        output.write(data);

        output.flush();
    }*/

    /**
     * Método que prepara el anexoForm previo a crear un anexo
     * @param request
     * @param registroDetalleID
     * @param tipoRegistro
     * @param registroID
     * @param isOficioRemisionSir
     * @return
     * @throws Exception
     */
    protected AnexoForm prepararAnexoForm(HttpServletRequest request, Long registroDetalleID, Long tipoRegistro, Long registroID, Boolean isOficioRemisionSir, Boolean scan) throws Exception {

        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, null, isOficioRemisionSir);
        RegistroDetalle registroDetalle = registroDetalleEjb.findById(registroDetalleID);

        //Prepara el anexoForm con los datos
        AnexoForm anexoForm = new AnexoForm();
        anexoForm.setRegistroID(registroID);
        anexoForm.setTipoRegistro(tipoRegistro);
        anexoForm.setIdRegistroDetalle(registroDetalleID);
        anexoForm.getAnexo().setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);
        anexoForm.getAnexo().setScan(scan);
        anexoForm.setOficioRemisionSir(isOficioRemisionSir);
        return anexoForm;
    }


    protected static AnexoLocal anexoEjbStatic = null;

    private static AnexoLocal getAnexoLocalEJBStatic() throws Exception {

        if (anexoEjbStatic == null) {

            anexoEjbStatic = (AnexoLocal) new InitialContext().lookup(AnexoLocal.JNDI_NAME);

        }

        return anexoEjbStatic;
    }

}
