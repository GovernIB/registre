package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.AnexoWebValidator;
import org.apache.axis.utils.StringUtils;
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


    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    private ScanWebModuleLocal scanWebModuleEjb;


    @RequestMapping(value = "/nou2", method = RequestMethod.GET)
    public String crearAnexoGet2(HttpServletRequest request,
                                 HttpServletResponse response, Model model) throws I18NException, Exception {

        //Recibimos de sesión los datos en anexoForm
        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");

        model.addAttribute("anexoForm", anexoForm);

        //Cargamos atributos comunes
        loadCommonAttributes(request, model);
        //Cuando creamos un anexo, no se carga el tipo de validez de documento "Copia Compulsada"
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_ENVIO);
        return "registro/formularioAnexo";
    }


    @RequestMapping(value = "/nou", method = RequestMethod.POST)
    public String crearAnexoPost(@ModelAttribute AnexoForm anexoForm,
                                 BindingResult result, HttpServletRequest request,
                                 HttpServletResponse response, Model model) throws Exception, I18NException {

        log.info(" Passa per crearAnexoPost");

        //Validamos el anexo
        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {
                anexoEjb.crearAnexo(anexoForm, getUsuarioEntidadActivo(request),
                        anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), false);

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

        // si hay errores, volvemos al formulario cargando los valores comunes
        loadCommonAttributes(request, model);
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_ENVIO);

        return "registro/formularioAnexo";


    }


    /*
     Prepara los datos de un anexo para su edición
     */
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

        AnexoFull anexoFull2 = anexoEjb.getAnexoFullLigero(anexoID, getEntidadActiva(request).getId());
        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, anexoID, isOficioRemisionSir);

        //Preparamos el formulario con los datos a mostrar
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

        final String scanWebID = String.valueOf(registroID);
        scanWebModuleEjb.closeScanWebProcess(request, scanWebID);

        model.addAttribute("anexoForm", anexoForm);

        //Cargamos los atributos comunes
        loadCommonAttributes(request, model);
        // En caso de edición se cargan todos los tipos de validez documento
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);

        return "registro/formularioAnexo";

    }


    /**
     * Modifica los datos de un anexo
     */
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String editarAnexoPost(@ModelAttribute AnexoForm anexoForm,
                                  BindingResult result, HttpServletRequest request,
                                  HttpServletResponse response, Model model) throws Exception, I18NValidationException, I18NException {

        log.info(" Passa per editarAnexoPost");

        anexoValidator.validate(anexoForm.getAnexo(), result);

        if (!result.hasErrors()) { // Si no hay errores

            try {
                anexoEjb.actualizarAnexo(anexoForm, getUsuarioEntidadActivo(request),
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
        loadCommonAttributes(request, model);
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
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
    protected String getRedirectURL2(HttpServletRequest request, String tipoRegistro,
                                     Long registroID) {
        if (StringUtils.isEmpty(tipoRegistro)) {

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


    protected String getNombreCompletoTipoRegistro(String tipoRegistro) {
        return "registro" + Character.toUpperCase(tipoRegistro.charAt(0))
                + tipoRegistro.substring(1);
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
        fullDownload(anexoFull.getAnexo().getCustodiaID(), anexoFull.getDocumentoCustody().getMime(),
                anexoFull.getAnexo().isJustificante(), false, response, getEntidadActiva(request).getId(),false);
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
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getDocumentoCustody().getMime(),
                    anexo.getAnexo().isJustificante(), false, response, getEntidadActiva(request).getId(),original);
        } else {
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getSignatureCustody().getMime(),
                    anexo.getAnexo().isJustificante(), true, response, getEntidadActiva(request).getId(),original);
        }

    }


    /**
     * Función que obtiene los datos de un archivo del sistema de custodia para mostrarlo
     *
     * @param custodiaID     identificador del archivo
     * @param contentType
     * @param isJustificante
     * @param firma
     * @param response
     * @return Descarga el archivo y además devuelve true o false en funcion de si ha encontrado el archivo indicado.
     * En la api de custodia antigua, los documentos firmados se guardaban en DocumentCustody y en la nueva en SignatureCustody.
     * Por tanto cuando vaya a recuperar un documento con firma antiguo, mirarà en SignatureCustody y no lo encontrará, por tanto controlamos ese caso y devolvemos false.
     * para poder ir a buscarlo a DocumentCustody, que es donde estará. (todo esto se hace en el método firma)
     */
    private void fullDownload(String custodiaID, String contentType, boolean isJustificante, boolean firma, HttpServletResponse response, Long idEntidad, boolean original) {

        String filename = null;
        OutputStream output;
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        byte[] data;

        try {

            // Si l'arxiu té identificador, entram
            if (custodiaID != null) {

                // Si és un arxiu sense firma
                if (!firma) {
                    DocumentCustody dc = anexoEjb.getArchivo(custodiaID, isJustificante, idEntidad);
                    filename = dc.getName();
                    data = dc.getData();

                } else {   // Si és firma d'un arxiu
                    if(original){
                        SignatureCustody dc = anexoEjb.getFirma(custodiaID, isJustificante, idEntidad);
                        filename = dc.getName();
                        data = dc.getData();
                    }else{
                        SignatureCustody sc = anexoEjb.descargarFirmaDesdeUrlValidacion(custodiaID, isJustificante, idEntidad);
                        data = sc.getData();
                        filename = sc.getName();
                    }

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
                response.setContentLength(data.length);

                output = response.getOutputStream();
                output.write(data);

                output.flush();
            }
        } catch (I18NException i18ne) {
            log.error(I18NUtils.getMessage(i18ne), i18ne);
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

    /*
    Método que muestra y carga un archivo a partir del nombre del archivo y de su byte[]
     */
    private void fullDownload(String filename, String contentType, byte[] data,
                             HttpServletResponse response) {

        OutputStream output;
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
            response.setContentLength(data.length);

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
     * Guarda en sesión los datos asociados al ultimo anexo
     * @param request
     * @param registroDetalleID
     * @param registroID
     * @param tipoRegistro
     * @param anexoID
     * @param isOficioRemisionSir
     */
    protected void saveLastAnnexoAction(HttpServletRequest request, Long registroDetalleID,
                                        Long registroID, String tipoRegistro, Long anexoID, boolean isOficioRemisionSir) {
        HttpSession session = request.getSession();
        session.setAttribute("LAST_registroDetalleID", registroDetalleID);
        session.setAttribute("LAST_tipoRegistro", tipoRegistro);
        session.setAttribute("LAST_registroID", registroID);
        session.setAttribute("LAST_anexoID", anexoID); // nou = null o editar != null
        session.setAttribute("LAST_isOficioRemisionSir", isOficioRemisionSir);
    }


    /**
     * Se cargan los atributos comunes de los anexos
     * @param request
     * @param model
     * @throws Exception
     */
    protected void loadCommonAttributes(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
        model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);

    }


    /**
     * Obtiene los anexos completos del registro indicado
     * @param idRegistro
     * @param tipoRegistro
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private List<AnexoFull> obtenerAnexosFullByRegistro(Long idRegistro, String tipoRegistro)  throws Exception, I18NException {
        if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO.toLowerCase())) {
            RegistroEntrada registroEntrada = registroEntradaConsultaEjb.getConAnexosFullLigero(idRegistro);
            return registroEntrada.getRegistroDetalle().getAnexosFull();

        } else {
            RegistroSalida registroSalida = registroSalidaConsultaEjb.getConAnexosFullLigero(idRegistro);
            return registroSalida.getRegistroDetalle().getAnexosFull();

        }
    }


    /**
     * Método que verifica si el anexo que se está creando no supera el tamano establecido por las propiedades SIR
     * y tiene una extensión de documento dentro de las permitidas
     *
     * @param registroID  identificador del registro al que se quiere asociar el anexo
     * @param tipoRegistro si es "entrada" o "salida
     * @param docSize tamaño del documento a anexar
     * @param firmaSize tamaño de la firma a anexar
     * @param docExtension extensión del documento a anexar
     * @param firmaExtension extensión de la firma a anexar
     * @param result
     * @param scan true si viene de scan, false si no viene de scan
     * @throws Exception
     * @throws I18NException
     */
    public void validarLimitacionesSIRAnexos(Long registroID, String tipoRegistro, long docSize,
                                             long firmaSize, String docExtension, String firmaExtension,
                                              BindingResult result, boolean scan) throws Exception, I18NException{

        // Obtenemos los anexos del registro para validar que no exceda el máximo de MB establecido
        List<AnexoFull> anexosFull = obtenerAnexosFullByRegistro(registroID, tipoRegistro);

        //Se suman las distintas medidas de los anexos que tiene el registro hasta el momento.
        long  tamanyoTotalAnexos= AnexoUtils.obtenerTamanoTotalAnexos(anexosFull);

        // Comprobamos que el nuevo anexo no supere el tamaño máximo.
        Long tamanyoMaximoTotalAnexos = PropiedadGlobalUtil.getTamanoMaxTotalAnexosSir();
        if (docSize != 0) {
            tamanyoTotalAnexos += docSize;
            if (tamanyoTotalAnexos > tamanyoMaximoTotalAnexos) {
                String totalAnexos = tamanyoTotalAnexos / (1024 * 1024) + " Mb";
                String maxTotalAnexos = tamanyoMaximoTotalAnexos / (1024 * 1024) + " Mb";
                if(!scan) {
                    result.rejectValue("documentoFile", "tamanymaxtotalsuperat", new Object[]{totalAnexos, maxTotalAnexos}, I18NUtils.tradueix("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos));
                }else{
                    throw new I18NException("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos);
                }
            }
        } else {// Solo comprobamos el tamaño en el documento firma en el caso que el documento este vacio, ya que se trata de firma attached
            tamanyoTotalAnexos += firmaSize;
            if (tamanyoTotalAnexos > tamanyoMaximoTotalAnexos) {
                String totalAnexos = tamanyoTotalAnexos / (1024 * 1024) + " Mb";
                String maxTotalAnexos = tamanyoMaximoTotalAnexos / (1024 * 1024) + " Mb";
                if(!scan) {
                    result.rejectValue("firmaFile", "tamanymaxtotalsuperat", new Object[]{totalAnexos, maxTotalAnexos}, I18NUtils.tradueix("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos));
                }else{
                    throw new I18NException("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos);
                }
            }
        }


        //Validamos que las extensiones del documento y la firma esten dentro de los formatos permitidos.
        String extensionesPermitidas = PropiedadGlobalUtil.getFormatosAnexosSir();
        if(!docExtension.isEmpty()) {
            if (!extensionesPermitidas.contains(docExtension)) {
                if (!scan) {
                    result.rejectValue("documentoFile", "formatonopermitido", new Object[]{docExtension, extensionesPermitidas}, I18NUtils.tradueix("formatonopermitido", docExtension, extensionesPermitidas));
                } else {
                    throw new I18NException("formatonopermitido", docExtension, extensionesPermitidas);
                }
            }
        }else {// Solo comprobamos la extensión en el documento firma en el caso que el documento este vacio, ya que se trata de firma attached
            if (!extensionesPermitidas.contains(firmaExtension)) {
                if (!scan) {
                    result.rejectValue("firmaFile", "formatonopermitido", new Object[]{firmaExtension, extensionesPermitidas}, I18NUtils.tradueix("formatonopermitido", firmaExtension, extensionesPermitidas));
                } else {
                    throw new I18NException("formatonopermitido", firmaExtension, extensionesPermitidas);
                }

            }
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


    protected static AnexoLocal anexoEjbStatic = null;

    private static AnexoLocal getAnexoLocalEJBStatic() throws Exception {

        if (anexoEjbStatic == null) {

            anexoEjbStatic = (AnexoLocal) new InitialContext()
                    .lookup("regweb3/AnexoEJB/local");

        }

        return anexoEjbStatic;
    }

}
