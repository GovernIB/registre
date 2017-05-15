package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.AnexoWebValidator;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created 3/06/14 14:22
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


    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    public ScanWebModuleLocal scanWebModuleEjb;


    /**
     * Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat
     */
    @RequestMapping(value = "/nou", method = RequestMethod.GET)
    public ModelAndView crearAnexoGet(HttpServletRequest request,
                                      HttpServletResponse response, Model model) throws I18NException, Exception {

        HttpSession session = request.getSession();
        Long registroDetalleID = (Long) session.getAttribute("LAST_registroDetalleID");
        String tipoRegistro = (String) session.getAttribute("LAST_tipoRegistro");
        Long registroID = (Long) session.getAttribute("LAST_registroID");
        Long anexoID = (Long) session.getAttribute("LAST_anexoID");

        boolean isOficioRemisionSir = false;
        return new ModelAndView(new RedirectView("/anexo/" + (anexoID == null ? "nou/" : "editar/")
                + registroDetalleID + "/" + tipoRegistro + "/" + registroID + (anexoID == null ? "" : ("/" + anexoID)) + "/" + isOficioRemisionSir, true));
    }

    /**
     * Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat
     */
    @RequestMapping(value = "/editar", method = RequestMethod.GET)
    public ModelAndView editarAnexoGet(HttpServletRequest request,
                                       HttpServletResponse response, Model model) throws I18NException, Exception {
        return crearAnexoGet(request, response, model);
    }

    @RequestMapping(value = "/nou2", method = RequestMethod.GET)
    public String crearAnexoGet2(HttpServletRequest request,
                                 HttpServletResponse response, Model model) throws I18NException, Exception {
        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");

        model.addAttribute("anexoForm", anexoForm);

        loadCommonAttributes(request, model);
        return "registro/formularioAnexo";
    }

    @RequestMapping(value = "/nou/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Long registroDetalleID,
                                @PathVariable String tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                                Model model) throws I18NException, Exception {

        log.info(" Passa per AnexoController::crearAnexoGet(" + registroDetalleID
                + "," + tipoRegistro + ", " + registroID + ")");


        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, null, isOficioRemisionSir);

        RegistroDetalle registroDetalle = registroDetalleEjb.findById(registroDetalleID);

        AnexoForm anexoForm = new AnexoForm();
        anexoForm.setRegistroID(registroID);
        anexoForm.setTipoRegistro(tipoRegistro);
        anexoForm.getAnexo().setRegistroDetalle(registroDetalle);
        anexoForm.setOficioRemisionSir(isOficioRemisionSir);
        model.addAttribute("anexoForm", anexoForm);


        return "registro/formularioAnexo";
    }


    protected void saveLastAnnexoAction(HttpServletRequest request, Long registroDetalleID,
                                        Long registroID, String tipoRegistro, Long anexoID, boolean isOficioRemisionSir) {
        HttpSession session = request.getSession();
        session.setAttribute("LAST_registroDetalleID", registroDetalleID);
        session.setAttribute("LAST_tipoRegistro", tipoRegistro);
        session.setAttribute("LAST_registroID", registroID);
        session.setAttribute("LAST_anexoID", anexoID); // nou = null o editar != null
        session.setAttribute("LAST_isOficioRemisionSir", isOficioRemisionSir);
    }


    @RequestMapping(value = "/nou", method = RequestMethod.POST)
    public String crearAnexoPost(@ModelAttribute AnexoForm anexoForm,
                                 BindingResult result, HttpServletRequest request,
                                 HttpServletResponse response, Model model) throws Exception, I18NException {

        log.info(" Passa per crearAnexoPost");

        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {

                anexoEjb.crearAnexo(anexoForm, getUsuarioEntidadActivo(request),
                        anexoForm.getRegistroID(), anexoForm.getTipoRegistro());

                model.addAttribute("closeAndReload", "true");
                return "registro/formularioAnexo";

            } catch (I18NValidationException i18n) {
                log.error(i18n.getMessage(), i18n);
                // TODO
                Mensaje.saveMessageError(request, i18n.getMessage());

            } catch (I18NException i18n) {
                log.debug(i18n.getMessage(), i18n);
                Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Mensaje.saveMessageError(request, e.getMessage());
            }

        }

        // Errors
        loadCommonAttributes(request, model);

        return "registro/formularioAnexo";


    }


    // edit
    @RequestMapping(value = "/editar/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}/{isOficioRemisionSir}",
            method = RequestMethod.GET)
    public String editarAnexoGet(HttpServletRequest request,
                                 HttpServletResponse response, @PathVariable Long registroDetalleID,
                                 @PathVariable String tipoRegistro, @PathVariable Long registroID,
                                 @PathVariable Long anexoID, @PathVariable boolean isOficioRemisionSir, Model model) throws I18NException, Exception {

        final boolean debug = log.isDebugEnabled();

        if (debug) {
            log.debug(" Passa per AnexoController::editarAnexoGet("
                    + "registroDetalleID = " + registroDetalleID
                    + " | tipoRegistro = " + tipoRegistro
                    + " | registroID = " + registroID
                    + " | anexoID = " + anexoID + ")");
        }

        AnexoFull anexoFull2 = anexoEjb.getAnexoFull(anexoID);

        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, anexoID, isOficioRemisionSir);


        AnexoForm anexoForm = new AnexoForm(anexoFull2);
        anexoForm.setRegistroID(registroID);
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

        final long scanWebID = registroID;

        scanWebModuleEjb.closeScanWebProcess(request, scanWebID);

        model.addAttribute("anexoForm", anexoForm);

        loadCommonAttributes(request, model);

        return "registro/formularioAnexo";

    }


    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String editarAnexoPost(@ModelAttribute AnexoForm anexoForm,
                                  BindingResult result, HttpServletRequest request,
                                  HttpServletResponse response, Model model) throws Exception, I18NValidationException, I18NException {

        log.info(" Passa per editarAnexoPost");
        String variableReturn = "";


        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {

                anexoEjb.actualizarAnexo(anexoForm, getUsuarioEntidadActivo(request),
                        anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), anexoForm.getAnexo().isJustificante());

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
                                @PathVariable String tipoRegistro, @PathVariable Long registroID,
                                @PathVariable Long anexoID, HttpServletRequest request) {

        try {
            registroDetalleEjb.eliminarAnexoRegistroDetalle(anexoID, registroDetalleID);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
        }

        return getRedirectURL2(request, tipoRegistro, registroID);
    }


    protected Long getRegistroDetalleID(AnexoForm anexoForm) {
        try {
            return anexoForm.getAnexo().getRegistroDetalle().getId();
        } catch (Throwable e) {
            return null;
        }
    }


    protected String getRedirectURL2(HttpServletRequest request, String tipoRegistro,
                                     Long registroID) {
        if (StringUtils.isEmpty(tipoRegistro)) {

            return request.getContextPath();

        } else {
            String nombreCompleto = getNombreCompletoTipoRegistro(tipoRegistro);
            if (registroID == null || registroID == 0) {
                String url = "redirect:/" + nombreCompleto + "/list";
                //log.info("DELETE URL 1 = " + url);
                return url;
            } else {
                String url = "redirect:/" + nombreCompleto + "/" + registroID + "/detalle";
                //log.info("DELETE URL 2 = " + url);
                return url;
            }
        }
    }


    protected String getNombreCompletoTipoRegistro(String tipoRegistro) {
        String nombreCompletoTipoRegistro = "registro" + Character.toUpperCase(tipoRegistro.charAt(0))
                + tipoRegistro.substring(1);
        return nombreCompletoTipoRegistro;
    }



    /**
     * Función que nos permite mostrar el contenido de un anexo
     *
     * @param anexoId identificador del anexo
     */
    @RequestMapping(value = "/descargarDocumento/{anexoId}", method = RequestMethod.GET)
    public void anexo(@PathVariable("anexoId") Long anexoId, HttpServletRequest request,
                      HttpServletResponse response) throws Exception, I18NException {
        AnexoFull anexoFull = anexoEjb.getAnexoFull(anexoId);
        fullDownload(anexoFull.getAnexo().getCustodiaID(), anexoFull.getDocumentoCustody().getName(),
                anexoFull.getDocumentoCustody().getMime(), anexoFull.getAnexo().isJustificante(), false, response);
    }

    /**
     * Función que nos permite mostrar el contenido de un firma de un anexo
     *
     * @param anexoId identificador del anexo
     */
    @RequestMapping(value = "/descargarFirma/{anexoId}", method = RequestMethod.GET)
    public void firma(@PathVariable("anexoId") Long anexoId, HttpServletRequest request,
                      HttpServletResponse response) throws Exception, I18NException {
        AnexoFull anexo = anexoEjb.getAnexoFull(anexoId);
        //Parche para la api de custodia antigua que se guardan los documentos firmados (modofirma == 1 Attached) en DocumentCustody.
        if (anexo.getSignatureCustody() == null) {//Api antigua, hay que descargar el document custody
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getDocumentoCustody().getName(),
                    anexo.getDocumentoCustody().getMime(), anexo.getAnexo().isJustificante(), false, response);
        } else {
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getSignatureCustody().getName(),
                    anexo.getSignatureCustody().getMime(), anexo.getAnexo().isJustificante(), true, response);
        }

    }


    /**
     * Función que obtiene los datos de un archivo para mostrarlo
     *
     * @param custodiaID     identificador del archivo
     * @param filename       nombre del archivo
     * @param contentType
     * @param isJustificante
     * @param response
     * @return Descarga el archivo y además devuelve true o false en funcion de si ha encontrado el archivo indicado.
     * En la api de custodia antigua, los documentos firmados se guardaban en DocumentCustody y en la nueva en SignatureCustody.
     * Por tanto cuando vaya a recuperar un documento con firma antiguo, mirarà en SignatureCustody y no lo encontrará, por tanto controlamos ese caso y devolvemos false.
     * para poder ir a buscarlo a DocumentCustody, que es donde estará. (todo esto se hace en el método firma)
     */
    public void fullDownload(String custodiaID, String filename, String contentType, boolean isJustificante, boolean firma,
                             HttpServletResponse response) {

        //FileInputStream input = null;


        OutputStream output = null;
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        byte[] data;
        try {
            if (custodiaID != null) {
                if (!firma) {
                    DocumentCustody dc = anexoEjb.getArchivo(custodiaID, isJustificante);
                    filename = dc.getName();
                    data = dc.getData();
                } else {
                    SignatureCustody sc = anexoEjb.getFirma(custodiaID, isJustificante);
                    filename = sc.getName();
                    data = sc.getData();
                }


                if (contentType == null) {
                    try {
                        File tmp = File.createTempFile("regweb_annex_", filename);
                        FileOutputStream fos = new FileOutputStream(tmp);
                        fos.write(data);
                        fos.flush();
                        fos.close();
                        contentType = mimeTypesMap.getContentType(tmp);
                        if (!tmp.delete()) {
                            tmp.deleteOnExit();
                        }
                    } catch (Throwable th) {
                        log.error("Error intentant obtenir el tipus MIME: " + th.getMessage(), th);
                        contentType = "application/octet-stream";
                    }
                }
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                response.setContentLength((int) data.length);

                output = response.getOutputStream();
                output.write(data);

                output.flush();
            }

        } catch (NumberFormatException e) {
            // TODO QUE FER
            log.info(e);
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

        fullDownload(filename, contentType, data, response);
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
        fullDownload(filename, contentType, data, response);
    }

    public void fullDownload(String filename, String contentType, byte[] data,
                             HttpServletResponse response) {

        //FileInputStream input = null;


        OutputStream output = null;
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

        try {
            if (contentType == null) {
                try {
                    File tmp = File.createTempFile("regweb_annex_", filename);
                    FileOutputStream fos = new FileOutputStream(tmp);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    contentType = mimeTypesMap.getContentType(tmp);
                    if (!tmp.delete()) {
                        tmp.deleteOnExit();
                    }
                } catch (Throwable th) {
                    log.error("Error intentant obtenir el tipus MIME: " + th.getMessage(), th);
                    contentType = "application/octet-stream";
                }
            }
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentLength((int) data.length);

            output = response.getOutputStream();
            output.write(data);

            output.flush();


        } catch (NumberFormatException e) {
            // TODO QUE FER
            log.info(e);
        } catch (Exception e) {
            e.printStackTrace();
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
    public List<AnexoFull> obtenerAnexosFullByRegistro(Long idRegistro, String tipoRegistro) throws Exception, I18NException {
        if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO.toLowerCase())) {
            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
            return registroEntrada.getRegistroDetalle().getAnexosFull();

        } else {
            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);
            return registroSalida.getRegistroDetalle().getAnexosFull();

        }
    }

    /**
     * Calcula el tamaño total de los anexos
     *
     * @param anexosFull
     * @param anexoForm
     * @return
     */
    public long obtenerTamanoTotalAnexos(List<AnexoFull> anexosFull, AnexoForm anexoForm) throws Exception {
        long tamanyoTotalAnexos = 0;
        long tamanyoanexo = 0;
        for (AnexoFull anexoFull : anexosFull) {
            //Obtenemos los bytes del documento que representa el anexo, en el caso 4 Firma Attached,
            // el documento está en SignatureCustody
            DocumentCustody dc = anexoFull.getDocumentoCustody();
            if (dc != null) {//Si documentCustody es null tenemos que coger SignatureCustody.
                tamanyoanexo = anexoFull.getDocumentoCustody().getLength();
            } else {
                SignatureCustody sc = anexoFull.getSignatureCustody();
                if (sc != null) {
                    tamanyoanexo = anexoFull.getSignatureCustody().getLength();
                }
            }
            tamanyoTotalAnexos += tamanyoanexo;
        }
        //Añadimos el tamaño del nuevo anexo, puede estar en DocumentoFile o en FirmaFile
        if (anexoForm.getDocumentoFile().getSize() != 0) {
            tamanyoTotalAnexos += anexoForm.getDocumentoFile().getSize();
        } else {
            tamanyoTotalAnexos += anexoForm.getFirmaFile().getSize();
        }

        return tamanyoTotalAnexos;

    }

    /**
     * Obtiene la extensión del anexo introducido en el formulario
     *
     * @param anexoForm
     * @return
     */
    public String obtenerExtensionAnexo(AnexoForm anexoForm) {
        if (!anexoForm.getDocumentoFile().getOriginalFilename().isEmpty()) {
            return FilenameUtils.getExtension(anexoForm.getDocumentoFile().getOriginalFilename());
        } else {
            return FilenameUtils.getExtension(anexoForm.getFirmaFile().getOriginalFilename());
        }
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));


    }


    /**
     * Mètodes utilitzat dins regweb3.tld
     *
     * @param custodiaID
     * @return
     */
    public static long getDocSize(String custodiaID) throws Exception {

        DocumentCustody dc = getAnexoLocalEJBStatic().getDocumentInfoOnly(custodiaID);
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
    public static long getSignSize(String custodiaID) throws Exception {

        SignatureCustody sc = getAnexoLocalEJBStatic().getSignatureInfoOnly(custodiaID);
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
    public static String getDocName(String custodiaID) throws Exception {

        DocumentCustody dc = getAnexoLocalEJBStatic().getDocumentInfoOnly(custodiaID);
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
    public static String getSignName(String custodiaID) throws Exception {

        SignatureCustody sc = getAnexoLocalEJBStatic().getSignatureInfoOnly(custodiaID);
        if (sc == null) {
            return "";
        }
        return sc.getName();
    }


    protected static AnexoLocal anexoEjbStatic = null;

    public static AnexoLocal getAnexoLocalEJBStatic() throws Exception {

        if (anexoEjbStatic == null) {

            anexoEjbStatic = (AnexoLocal) new InitialContext()
                    .lookup("regweb3/AnexoEJB/local");

        }

        return anexoEjbStatic;
    }

}
