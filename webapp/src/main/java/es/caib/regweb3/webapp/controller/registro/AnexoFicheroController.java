package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mgonzalez on 02/05/2017.
 * Controller que se encarga de gestionar la entrada de anexos via web opción "Archivo"
 */
@Controller
@RequestMapping(value = "/anexoFichero")
@SessionAttributes(types = {AnexoForm.class})
public class AnexoFicheroController extends AnexoController {


    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb;


    /**
     * Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat.
     */
    @RequestMapping(value = "/ficheros", method = RequestMethod.GET)
    public ModelAndView ficherosGet(HttpServletRequest request,
                                    HttpServletResponse response, Model model) throws I18NException, Exception {

        //En caso de error, actualiza las variables de sesión necesarias
        HttpSession session = request.getSession();
        Long registroDetalleID = (Long) session.getAttribute("LAST_registroDetalleID");
        Long tipoRegistro = (Long) session.getAttribute("LAST_tipoRegistro");
        Long registroID = (Long) session.getAttribute("LAST_registroID");
        Long anexoID = (Long) session.getAttribute("LAST_anexoID");
        Boolean isOficioRemisionSir = (Boolean) session.getAttribute("LAST_isOficioRemisionSir");


        return new ModelAndView(new RedirectView("/anexoFichero/ficheros/" + registroDetalleID + "/" + tipoRegistro + "/" + registroID + (anexoID == null ? "" : ("/" + anexoID)) + "/" + isOficioRemisionSir, true));
    }

    @RequestMapping(value = "/ficheros/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String ficherosGet(HttpServletRequest request,
                              HttpServletResponse response, @PathVariable Long registroDetalleID,
                              @PathVariable Long tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                              Model model) throws I18NException, Exception {

        //log.info(" Passa per AnexoFicheroController::ficherosGet(" + registroDetalleID + "," + tipoRegistro + ", " + registroID + ")");

        Entidad entidad = getEntidadActiva(request);
        //Actualiza las variables con la ultima acción y prepara el anexoForm
        AnexoForm anexoForm = prepararAnexoForm(request, registroDetalleID, tipoRegistro, registroID, isOficioRemisionSir, false);

        anexoForm.setPermitirAnexoDetached(PropiedadGlobalUtil.getPermitirAnexosDetached(entidad.getId()));
        anexoForm.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
        model.addAttribute("anexoForm", anexoForm);

        return "registro/formularioAnexoFichero";
    }


    @RequestMapping(value = "/ficheros", method = RequestMethod.POST)
    public String ficherosPost(@ModelAttribute AnexoForm anexoForm,
                               BindingResult result, HttpServletRequest request,
                               HttpServletResponse response, Model model) throws Exception, I18NException {

        //log.info(" Passa per ficherosPost");

        // Validad nombre del fichero
        validadNombreFichero(anexoForm, result);

        // Si es oficio de remision sir debemos comprobar la limitación de los anexos impuesta por SIR
        boolean isSIR = anexoForm.getOficioRemisionSir();

        if (isSIR) { //Verificación de las limitaciones de un anexo via SIR

            long docSize = -1;
            String docExtension = "";
            //Obtenemos extensión y tamaño del archivo
            if (anexoForm.getDocumentoFile() != null) {
                docSize = anexoForm.getDocumentoFile().getSize();
                docExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getDocumentoFile().getOriginalFilename());
            }

            long firmaSize = -1;
            String firmaExtension = "";
            //Obtenemos extensión y tamaño del documento firma
            if (anexoForm.getFirmaFile() != null) {
                firmaSize = anexoForm.getFirmaFile().getSize();
                firmaExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getFirmaFile().getOriginalFilename());
            }

            //Validamos las limitaciones SIR
            validarLimitacionesSIRAnexos(anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), docSize, firmaSize, docExtension, firmaExtension, result, false);

        }

        if (result.hasErrors()) {

            return "registro/formularioAnexoFichero";
        } else {
            try {
                //Preparamos los documentcustody, signaturecustody en el anexoForm para pasarlos a la siguiente pantalla
                manageDocumentCustodySignatureCustody(request, anexoForm);

                //Validamos la firma del anexoForm que nos indican, previo a crear el anexo
                validarAnexoForm(request, anexoForm);

                request.getSession().setAttribute("anexoForm", anexoForm);

                return "redirect:/anexo/nou2";

            } catch (I18NException i18n) {
                String msg = I18NUtils.tradueix(i18n.getTraduccio());
                log.error(msg, i18n);
                Mensaje.saveMessageError(request, msg);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Mensaje.saveMessageError(request, e.getMessage());
            }
        }
        return "registro/formularioAnexoFichero";

    }


    /**
     * Método que obtiene el signature custody del anexoForm.
     *
     * @param anexoForm
     * @param dc
     * @param modoFirma
     * @return
     * @throws Exception
     */
    protected SignatureCustody getSignatureCustody(AnexoForm anexoForm, DocumentCustody dc,
                                                   int modoFirma) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoForm.getFirmaFile() = " + anexoForm.getFirmaFile());
            log.debug(" anexoForm.getFirmaFile().isEmpty() = " + anexoForm.getFirmaFile().isEmpty());
            log.debug(" anexoForm.isFirmaFileDelete() = " + anexoForm.isSignatureFileDelete());
        }
        log.debug(" anexoForm.getFirmaFile() = " + anexoForm.getFirmaFile());
        SignatureCustody sc = null;
        if (!anexoForm.getFirmaFile().isEmpty()) {
            if (modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    && modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                String msg = "L'usuari ens indica que NO hi ha una firma pero n'envia una"
                        + " (modoFirma = " + modoFirma + ")";
                log.error(msg, new Exception());
                throw new Exception(msg);
            }

            //Cogemos el archivo que nos han indicado en el campo firma del formulario
            CommonsMultipartFile multipart = anexoForm.getFirmaFile();
            sc = new SignatureCustody();

            sc.setData(multipart.getBytes());
            sc.setMime(multipart.getContentType());
            sc.setName(multipart.getOriginalFilename());
            sc.setLength(multipart.getSize());

            // Ajustamos los valores de custodia con los que se guardará el signaturecustody.
            if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                // Document amb firma adjunta
                sc.setAttachedDocument(null);

                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

            } else if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                // Firma en document separat CAS 4
                if (dc == null) {
                    throw new Exception("Aquesta firma requereix el document original"
                            + " i no s'ha enviat");
                }

                sc.setAttachedDocument(false);
                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
            }
        }
        return sc;
    }


    /**
     * Método que obtiene el documentCustody del anexo.
     *
     * @param anexoForm
     * @return
     */
    protected DocumentCustody getDocumentCustody(AnexoForm anexoForm) {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");

            log.debug(" anexoForm.getDocumentoFile() = " + anexoForm.getDocumentoFile());
            log.debug(" anexoForm.getDocumentoFile().isEmpty() = " + anexoForm.getDocumentoFile().isEmpty());
            log.debug(" anexoForm.isDocumentoFileDelete() = " + anexoForm.isDocumentoFileDelete());
        }

        log.debug(" anexoForm.getDocumentoFile() = " + anexoForm.getDocumentoFile());
        DocumentCustody dc = null;
        if (!anexoForm.getDocumentoFile().isEmpty()) {
            dc = new DocumentCustody();
            CommonsMultipartFile multipart = anexoForm.getDocumentoFile();
            dc.setData(multipart.getBytes());
            dc.setMime(multipart.getContentType());
            dc.setName(multipart.getOriginalFilename());
            dc.setLength(multipart.getSize());
        }
        return dc;
    }


    /**
     * Método que prepara el DocumentCustody y el Signature Custody de un anexo introducido via web
     *
     * @param request
     * @param anexoForm
     * @throws Exception
     * @throws I18NException
     */
    protected void manageDocumentCustodySignatureCustody(
            HttpServletRequest request, AnexoForm anexoForm) throws Exception, I18NException {

        //Montamos el documentCustody y el signature custody
        DocumentCustody dc;
        SignatureCustody sc;

        // Formulari Fitxer de Sistema
        int modoFirma = anexoForm.getAnexo().getModoFirma();

        // Comprobamos en función del modo de firma que los documentos vengan bien
        dc = getDocumentCustody(anexoForm);
        sc = getSignatureCustody(anexoForm, dc, modoFirma);
        if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA && dc == null) {
            throw new I18NException("anexo.error.sinfichero");
        }
        if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && sc == null) {
            if (dc == null) { //Controlamos que no sea api antigua
                throw new I18NException("anexo.error.sinfichero");
            }

        }
        if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && (dc == null || sc == null)) {
            throw new I18NException("anexo.error.faltadocumento");
        }

        anexoForm.setDocumentoCustody(dc);
        anexoForm.setSignatureCustody(sc);
        //Desde archivo solo se puede adjuntar un archivo cada vez
        anexoForm.setNumAnexosRecibidos(1);

    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));


    }

}
