package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.commons.io.FilenameUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by mgonzalez on 02/05/2017.
 */
@Controller
@RequestMapping(value = "/anexoFichero")
@SessionAttributes(types = {AnexoForm.class })
public class AnexoFicheroController extends BaseController {

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;


    /**
     *  Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat
     */
    @RequestMapping(value = "/ficheros", method = RequestMethod.GET)
    public ModelAndView ficherosGet(HttpServletRequest request,
                                      HttpServletResponse response, Model model) throws I18NException, Exception {

        HttpSession session = request.getSession();
        Long registroDetalleID = (Long) session.getAttribute("LAST_registroDetalleID");
        String tipoRegistro = (String) session.getAttribute("LAST_tipoRegistro");
        Long registroID = (Long) session.getAttribute("LAST_registroID");
        Long anexoID = (Long) session.getAttribute("LAST_anexoID");

        boolean isOficioRemisionSir = false;
        return new ModelAndView(new RedirectView("/anexoFichero/ficheros/" + registroDetalleID + "/" + tipoRegistro + "/" + registroID + (anexoID == null ? "" : ("/" + anexoID)) + "/" + isOficioRemisionSir, true));
    }

    @RequestMapping(value = "/ficheros/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String ficherosGet(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Long registroDetalleID,
                                @PathVariable String tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                                Model model) throws I18NException, Exception {

        log.info(" Passa per AnexoFicheroController::ficherosGet(" + registroDetalleID
                + "," + tipoRegistro + ", " + registroID + ")");

        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, null, isOficioRemisionSir);

        RegistroDetalle registroDetalle = registroDetalleEjb.findById(registroDetalleID);

        AnexoForm anexoForm = new AnexoForm();
        anexoForm.setRegistroID(registroID);
        anexoForm.setTipoRegistro(tipoRegistro);
        anexoForm.getAnexo().setRegistroDetalle(registroDetalle);
        anexoForm.setOficioRemisionSir(isOficioRemisionSir);
        model.addAttribute("anexoForm" ,anexoForm);

        loadCommonAttributes(request, model);
        return "registro/formularioAnexoFichero";
    }




    @RequestMapping(value = "/ficheros", method = RequestMethod.POST)
    public String ficherosPost(@ModelAttribute AnexoForm anexoForm,
                               BindingResult result, HttpServletRequest request,
                               HttpServletResponse response, Model model) throws Exception,I18NException {

        log.info(" Passa per ficherosPost");
        String variableReturn = "";

        // Si es oficio de remision sir debemos comprobar la limitación de los anexos impuesta por SIR
        //COMENTADO POR PROBLEMAS CON DIR3CAIB
        //if(anexoForm.getOficioRemisionSir()){
        log.info("Entramos en OficioSir");
        variableReturn = validarLimitacionesSIRAnexos(anexoForm, request);
        //}
        if(!variableReturn.isEmpty()){
            log.info("Entramos en OficioSir variable return");
            return variableReturn;
        }

        try {
            manageDocumentCustodySignatureCustody(request, anexoForm);

            loadCommonAttributes(request, model);
            log.info("llego a aqui todo ha ido bien");
            return "registro/formularioAnexo2";
        } catch(I18NException i18n) {
            log.debug(i18n.getMessage(), i18n);
            Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));

        } catch(Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
        }

        return "registro/formularioAnexoFichero";

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


    protected void loadCommonAttributes(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
        model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
    }



    /**
     * Obtiene los anexos completos del registro indicado
     * @param idRegistro
     * @param tipoRegistro
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public List<AnexoFull> obtenerAnexosFullByRegistro(Long idRegistro, String tipoRegistro)  throws Exception, I18NException {
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
     * @param anexosFull
     * @param anexoForm
     * @return
     */
    public long obtenerTamanoTotalAnexos(List<AnexoFull> anexosFull, AnexoForm anexoForm) throws Exception{
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
     * @param anexoForm
     * @return
     */
    public String obtenerExtensionAnexo(AnexoForm anexoForm){
        log.info("DocumentFile " + anexoForm.getDocumentoFile());
        log.info("FirmaFile " + anexoForm.getFirmaFile());
        if (!anexoForm.getDocumentoFile().getOriginalFilename().isEmpty()) {
            return FilenameUtils.getExtension(anexoForm.getDocumentoFile().getOriginalFilename());
        } else {
            return FilenameUtils.getExtension(anexoForm.getFirmaFile().getOriginalFilename());
        }
    }



    /**
     * Valida las limitaciones del cuestionario de verificación de SIR (Numero de archivos, tamaño máximo y total de los archivos
     * @param anexoForm
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public String validarLimitacionesSIRAnexos(AnexoForm anexoForm, HttpServletRequest request) throws Exception, I18NException{
        Entidad entidadActiva = getEntidadActiva(request);

        // Obtenemos los anexos del registro para validar que no exceda el máximo de MB establecido
        List<AnexoFull> anexosFull = obtenerAnexosFullByRegistro(anexoForm.getRegistroID(), anexoForm.getTipoRegistro());

        //Se suman las distintas medidas de los anexos obtenidos
        long  tamanyoTotalAnexos= obtenerTamanoTotalAnexos(anexosFull, anexoForm);

        //Si el tamaño total es mayor del maximo establecido
        Long tamanyoMaximoTotalAnexos = PropiedadGlobalUtil.getMaxUploadSizeTotal(entidadActiva.getId());
        if (tamanyoTotalAnexos > tamanyoMaximoTotalAnexos) {
            String totalAnexos = tamanyoTotalAnexos / (1024 * 1024) + " Mb";
            String maxTotalAnexos = tamanyoMaximoTotalAnexos / (1024 * 1024) + " Mb";
            Mensaje.saveMessageError(request, I18NUtils.tradueix("tamanymaxtotalsuperat", totalAnexos, maxTotalAnexos));


            return "registro/formularioAnexoFichero";
        }

        // Validamos que la extensión del fichero indicado esté dentro de los formatos permitidos.
        String extensionObtenida = obtenerExtensionAnexo(anexoForm);
        String extensionesPermitidas = PropiedadGlobalUtil.getFormatosPermitidos(entidadActiva.getId());
        if (!extensionesPermitidas.contains(extensionObtenida)) {
            Mensaje.saveMessageError(request, I18NUtils.tradueix("formatonopermitido", extensionObtenida, extensionesPermitidas));

            return "registro/formularioAnexoFichero";
        }
        return "";
    }

    /**
     * Método que obtiene el signature custody del anexoForm.
     * Devuelve el actual o el indicado en el formulario del anexo con los valores de custodia ajustados
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
        //Cargamos el signature custody con el actual
        SignatureCustody sc = anexoForm.getSignatureCustody();
        // SignatureCustody sc = null;
      /*if (anexoForm.getFirmaFile().isEmpty()) {

        if (*//*modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED ||*//*
            modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
        //  String msg = "L'usuari ens indica que hi ha una firma i no ve (modoFirma = " + modoFirma + ")";
          String msg = "Falta el document de firma";
          log.error(msg, new Exception());
          throw new Exception(msg);
        }

      } else {*/
        if (!anexoForm.getFirmaFile().isEmpty()) {
            if (modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    && modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
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

            // Ajustamos los valores de custodia con los que se guardará el signaturecustody.
            if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                // Document amb firma adjunta
                sc.setAttachedDocument(null);

                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

            } else if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
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
     * Devuelve el actual o el indicado en el campo del formulario del anexo.
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


        DocumentCustody dc = anexoForm.getDocumentoCustody();
        //DocumentCustody dc = null;
        if (!anexoForm.getDocumentoFile().isEmpty()) {
            dc = new DocumentCustody();
            CommonsMultipartFile multipart = anexoForm.getDocumentoFile();
            dc.setData(multipart.getBytes());
            dc.setMime(multipart.getContentType());
            dc.setName(multipart.getOriginalFilename());
        }
        return dc;
    }


    /**
     * Método que prepara el DocumentCustody y el Signature Custody de un anexo introducido via web
     * @param request
     * @param anexoForm
     * @throws Exception
     * @throws I18NException
     */
    protected void manageDocumentCustodySignatureCustody(
            HttpServletRequest request,  AnexoForm anexoForm) throws Exception, I18NException {

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

    }

}
