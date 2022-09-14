package es.caib.regweb3.webapp.controller.registroSir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoSirFull;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Controller
@RequestMapping(value = "/registroSir")
@SessionAttributes(types = RegistroSir.class)
public class RegistroSirController extends BaseController {


    @EJB(mappedName = RegistroSirLocal.JNDI_NAME)
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = CatComunidadAutonomaLocal.JNDI_NAME)
    private CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = CatNivelAdministracionLocal.JNDI_NAME)
    private CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = SirEnvioLocal.JNDI_NAME)
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = TrazabilidadSirLocal.JNDI_NAME)
    private TrazabilidadSirLocal trazabilidadSirEjb;

    @EJB(mappedName = AnexoSirLocal.JNDI_NAME)
    private AnexoSirLocal anexoSirEjb;

    @EJB(mappedName = DistribucionLocal.JNDI_NAME)
    private DistribucionLocal distribucionEjb;



    /**
     * Listado de todos los RegistroSirs
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/registroSir/list";
    }

    /**
     * Listado de RegistroSirs
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSir/registroSirList");

        RegistroSirBusquedaForm registroSirBusquedaForm = new RegistroSirBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("tipos", TipoRegistro.values());
        model.addAttribute("registroSirBusqueda", registroSirBusquedaForm);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link RegistroSir} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSir/registroSirList");

        RegistroSir registroSir = busqueda.getRegistroSir();

        Paginacion paginacion = registroSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()), registroSir, getOficinaActiva(request).getCodigo(), busqueda.getEstado(), getEntidadActiva(request).getCodigoDir3());

        busqueda.setPageNumber(1);

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("tipos", TipoRegistro.values());
        mav.addObject("paginacion", paginacion);
        mav.addObject("registroSirBusqueda", busqueda);

        return mav;

    }

    /**
     * Listado de RegistroSir pendientes de procesar
     */
    @RequestMapping(value = "/pendientesProcesar/list", method = RequestMethod.GET)
    public String pendientesProcesar() {

        return "redirect:/registroSir/pendientesProcesar/list/1";
    }

    /**
     * Listado de RegistroSir pendientes de procesar
     */
    @RequestMapping(value = "/pendientesProcesar/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView pendientesProcesar(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroSir/registrosSirEstado");

        Paginacion paginacion = registroSirEjb.getRegistrosEstado(pageNumber,getOficinaActiva(request).getCodigo(), EstadoRegistroSir.RECIBIDO.getValue());

        mav.addObject("estado", EstadoRegistroSir.RECIBIDO);
        mav.addObject("url", "pendientesProcesar");
        mav.addObject("paginacion", paginacion);

        return mav;
    }

    /**
     * Listado de oficios de remisión sir enviados
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enviados", method = RequestMethod.GET)
    public ModelAndView enviados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroSir/enviadosList");

        List<Organismo> organismosConsultaEntrada = getOrganismosConsultaEntrada(request);

        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(new OficioRemision(), 1);
        oficioRemisionBusqueda.setIdOrganismo(seleccionarOrganismoActivo(request, organismosConsultaEntrada));
        oficioRemisionBusqueda.setFechaInicio(new Date());
        oficioRemisionBusqueda.setFechaFin(new Date());

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("organismosConsultaEntrada", organismosConsultaEntrada);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/enviados", method = RequestMethod.POST)
    public ModelAndView enviados(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroSir/enviadosList");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        busqueda.setFechaFin(RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()));

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(),busqueda.getIdOrganismo(), busqueda.getFechaInicio(), busqueda.getFechaFin(),null, oficioRemision, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), true);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("organismosConsultaEntrada", getOrganismosConsultaEntrada(request));
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;
    }


    /**
     * Carga el formulario para ver el detalle de un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistroSir}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSir(@PathVariable Long idRegistroSir, Model model, HttpServletRequest request) throws Exception, I18NException {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);

        // Si el registro sir cuyo estado es RECIBIDO
        if(registroSir.getEstado().equals(EstadoRegistroSir.RECIBIDO) && isOperador(request)){

            // Tengo permisos para gestionarlo?
            if(getOficinaActiva(request).getCodigo().equals(registroSir.getCodigoEntidadRegistral())){

                model.addAttribute("libro",getLibroEntidad(request)); // Libro único
                model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
                model.addAttribute("registrarForm", new RegistrarForm());
                model.addAttribute("pluginDistribucionEmail", distribucionEjb.isDistribucionPluginEmail(getEntidadActiva(request).getId()));

                // Comprobamos que la unida de tramitación destino está VIGENTE
                if(registroSir.getCodigoUnidadTramitacionDestino() !=null ){

                    Organismo organismoDestino = organismoEjb.findByCodigoEntidadLigero(registroSir.getCodigoUnidadTramitacionDestino(),getEntidadActiva(request).getId());
                    if(organismoDestino == null) {
                        model.addAttribute("extinguido", true);
                    }
                }
            }else{
                Mensaje.saveMessageError(request, getMessage("registroSir.error.destino"));
                return  "redirect:/registroSir/list";
            }

        }

        model.addAttribute("idiomas", RegwebConstantes.IDIOMAS_REGISTRO);
        model.addAttribute("tiposValidezDocumento",RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
        model.addAttribute("tiposDocumentales",tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("puedeReenviar",  sirEnvioEjb.puedeReenviarRegistroSir(registroSir.getEstado())); // si el estado es RECIBIDO, REENVIADO o REENVIADO_Y_ERROR se puede reenviar
        model.addAttribute("trazabilidades", trazabilidadSirEjb.getByRegistroSir(registroSir.getId()));
        model.addAttribute("registroSir",registroSir);
        model.addAttribute("anexosSirFull",componerAnexoSirFull(registroSir.getAnexos()));
        model.addAttribute("rechazarForm", new RechazarForm());
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registroSir/registroSirDetalle";
    }



    /**
     * Procesa {@link RegistroSir}, creando un RegistroEntrada
     */
    @RequestMapping(value = "/aceptar/{idRegistroSir}", method = RequestMethod.POST)
    public String confirmarRegistroSir(@PathVariable Long idRegistroSir, @ModelAttribute RegistrarForm registrarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);

        Entidad entidad = getEntidadActiva(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        String variableReturn = "redirect:/registroSir/"+idRegistroSir+"/detalle";

        // Comprobamos si ya ha sido confirmado
        if(registroSir.getEstado().equals(EstadoRegistroSir.ACEPTADO)){
            Mensaje.saveMessageError(request, getMessage("registroSir.estado.error"));
            return variableReturn;
        }

        // Procesa el RegistroSir
        try{

            RegistroEntrada registroEntrada = sirEnvioEjb.aceptarRegistroSir(registroSir, entidad, usuarioEntidad, oficinaActiva, registrarForm.getIdLibro(), registrarForm.getIdIdioma(), registrarForm.getCamposNTIs(), registrarForm.getIdOrganismoDestino(), registrarForm.getDistribuir(), registrarForm.getCodigoSia(),registrarForm.getEmails(),registrarForm.getMotivo());

            variableReturn = "redirect:/registroEntrada/" + registroEntrada.getId() + "/detalle";

            // Purgamos los AnexosSir
            anexoSirEjb.purgarAnexosRegistroSirAceptado(idRegistroSir);

            Mensaje.saveMessageInfo(request, getMessage("registroSir.aceptar.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.aceptar"));
            e.printStackTrace();
            return variableReturn;
        }

        return variableReturn;
    }

    /**
     * Rechaza un {@link RegistroSir}
     */
    @RequestMapping(value = "/rechazar/{idRegistroSir}", method = RequestMethod.POST)
    public String rechazarRegistroSir(@PathVariable Long idRegistroSir, @ModelAttribute RechazarForm rechazarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        String variableReturn = "redirect:/registroSir/"+idRegistroSir+"/detalle";

        // Comprobamos si ya ha sido confirmado
        if(registroSir.getEstado().equals(EstadoRegistroSir.RECHAZADO)){
            Mensaje.saveMessageError(request, getMessage("registroSir.estado.error"));
            return variableReturn;
        }

        // Rechaza el RegistroSir
        try{
            sirEnvioEjb.rechazarRegistroSir(registroSir, oficinaActiva, usuarioEntidad.getUsuario(), rechazarForm.getObservacionesRechazo());

            Mensaje.saveMessageInfo(request, getMessage("registroSir.rechazo.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.rechazo"));
            e.printStackTrace();
        }

        return "redirect:/registroSir/"+idRegistroSir+"/detalle";
    }

    @RequestMapping(value = "/{idRegistroSir}/reenviar", method = RequestMethod.GET)
    public String reenviarRegistroSir(@PathVariable Long idRegistroSir, Model model, HttpServletRequest request) throws Exception {

        model.addAttribute("comunidadesAutonomas", catComunidadAutonomaEjb.getAll());
        model.addAttribute("nivelesAdministracion", catNivelAdministracionEjb.getAll());
        model.addAttribute("registroSir", registroSirEjb.findById(idRegistroSir));
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registroSir/registroSirReenvio";
    }

    /**
     * Reenvia un {@link RegistroSir}
     */
    @RequestMapping(value = "/reenviar/{idRegistroSir}", method = RequestMethod.POST)
    public String reenviarRegistroSir(@PathVariable Long idRegistroSir, @ModelAttribute ReenviarForm reenviarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        //Montamos la oficina de reenvio seleccionada por el usuario
        Oficina oficinaReenvio = reenviarForm.oficinaReenvio();
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        String variableReturn = "redirect:/registroSir/"+idRegistroSir+"/detalle";

        RegistroSir registroSir  = registroSirEjb.findById(idRegistroSir);

        // Comprobamos si ya ha sido reenviado
        if(registroSir.getEstado().equals(EstadoRegistroSir.REENVIADO)){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            return variableReturn;
        }

        // Reenvia el RegistroSir
        try{
            if(oficinaReenvio != null){//Si han seleccionado oficina de reenvio
                //Reenviamos
                sirEnvioEjb.reenviarRegistroSir(registroSir, oficinaReenvio, oficinaActiva,usuarioEntidad.getUsuario(),reenviarForm.getObservaciones());
            }

            Mensaje.saveMessageInfo(request, getMessage("registroSir.reenvio.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroSir/"+idRegistroSir+"/detalle";
    }


    /**
     * compone los anexos sir recibidos en una lista de anexosSirFull
     * @param anexos
     * @return
     * @throws Exception
     */
    private List<AnexoSirFull> componerAnexoSirFull(List<AnexoSir> anexos) throws Exception{

        List<AnexoSirFull> anexosSirFull = new ArrayList<AnexoSirFull>();

        for (AnexoSir anexo : anexos) {

            AnexoSirFull anexoSirFull = new AnexoSirFull();
            // Detached
            if (StringUtils.isNotEmpty(anexo.getIdentificadorFichero()) && StringUtils.isNotEmpty(anexo.getIdentificadorDocumentoFirmado()) &&
                    !anexo.getIdentificadorFichero().equals(anexo.getIdentificadorDocumentoFirmado())) {
                AnexoSir documento = buscarAnexoSir(anexo.getIdentificadorDocumentoFirmado(), anexos);
                anexoSirFull.setDocumento(documento);
                anexoSirFull.setFirma(anexo);
                anexosSirFull.add(anexoSirFull);

            }

            // Attached
            if (StringUtils.isNotEmpty(anexo.getIdentificadorFichero()) && StringUtils.isNotEmpty(anexo.getIdentificadorDocumentoFirmado()) &&
                    anexo.getIdentificadorFichero().equals(anexo.getIdentificadorDocumentoFirmado())) {
                anexoSirFull.setDocumento(anexo);
                anexoSirFull.setFirma(null);
                anexosSirFull.add(anexoSirFull);
                anexoSirFull.setTieneFirma(true);
            }

            // Documento sin Firma
            if (StringUtils.isEmpty(anexo.getIdentificadorDocumentoFirmado()) &&
                    buscarAnexoSirConFirma(anexo.getIdentificadorFichero(), anexos) == null) {
                anexoSirFull.setDocumento(anexo);
                anexoSirFull.setFirma(null);
                anexosSirFull.add(anexoSirFull);
                anexoSirFull.setTieneFirma(false);
            }

        }

        return anexosSirFull;
    }

    /**
     * Busca un anexo sir por su identificador de fichero
     * @param identificadorFichero
     * @param anexos
     * @return
     */
    private AnexoSir buscarAnexoSir(String identificadorFichero, List<AnexoSir> anexos){

        for (AnexoSir anexo : anexos) {
            if(identificadorFichero.equals(anexo.getIdentificadorFichero())) return anexo;
        }
        return null;
    }


    /**
     *  Busca un anexoSir con Firma.
     * @param identificadorFichero
     * @param anexos
     * @return
     */
    private AnexoSir buscarAnexoSirConFirma(String identificadorFichero, List<AnexoSir> anexos){

        for (AnexoSir anexo : anexos) {
            if(identificadorFichero.equals(anexo.getIdentificadorDocumentoFirmado())) return anexo;
        }
        return null;
    }

    @InitBinder("oficioRemisionBusqueda")
    public void oficioRemisionBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }
}


