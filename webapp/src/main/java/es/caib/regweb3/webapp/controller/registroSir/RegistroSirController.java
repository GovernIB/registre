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
import java.util.*;

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


    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    private CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    private CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/SirEnvioEJB/local")
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/TrazabilidadSirEJB/local")
    private TrazabilidadSirLocal trazabilidadSirEjb;


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

        Paginacion paginacion = registroSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()), registroSir, getOficinaActiva(request).getCodigo(), busqueda.getEstado());

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

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        // Fijamos un libro por defecto
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setLibro(seleccionarLibroOficinaActiva(request, librosConsulta));
        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(oficioRemision, 1);
        oficioRemisionBusqueda.setFechaInicio(new Date());
        oficioRemisionBusqueda.setFechaFin(new Date());

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("librosConsulta", librosConsulta);
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

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), dataFi,null, oficioRemision, librosConsulta, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), true);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;

    }



    /**
     * Carga el formulario para ver el detalle de un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistroSir}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSir(@PathVariable Long idRegistroSir, Model model, HttpServletRequest request) throws Exception {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);

        //si el estado del registro sir  es RECIBIDO, REENVIADO o REENVIADO_Y_ERROR se puede reenviar
        model.addAttribute("puedeReenviar",  sirEnvioEjb.puedeReenviarRegistroSir(registroSir.getEstado()));


        // Si el registro sir cuyo estado es RECIBIDO
        if(registroSir.getEstado().equals(EstadoRegistroSir.RECIBIDO) && isOperador(request)){

            // Tengo permisos para gestionarlo?
            if(getOficinaActiva(request).getCodigo().equals(registroSir.getCodigoEntidadRegistral())){

                // Obtenemos los libros del Organismo destinatário del RegistroSir
                List<Libro> libros = getLibrosRegistroEntrada(request);

                model.addAttribute("libros",libros);
                model.addAttribute("registrarForm", new RegistrarForm());
                model.addAttribute("rechazarForm", new RechazarForm());
                model.addAttribute("reenviarForm", new ReenviarForm());

                // Si se ha indicado la unida de tramitación destino, comprobamos que esté vigente
                if(registroSir.getCodigoUnidadTramitacionDestino()!=null){
                    UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
                    Organismo organismoDestino = organismoEjb.findByCodigoEntidadSinEstadoLigero(registroSir.getCodigoUnidadTramitacionDestino(),usuarioEntidad.getEntidad().getId());
                    if(organismoDestino!=null) {
                        CatEstadoEntidad estado = organismoDestino.getEstado();
                        //Si el órgano está extinguido obtenemos sus órganos sustitutos
                        if (!RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(estado.getCodigoEstadoEntidad())) {
                            Set<Organismo> historicosFinales = new HashSet<Organismo>();
                            Set<Organismo> sustitutos = new HashSet<Organismo>();
                            //Obtenemos los organismos vigentes que lo sustituyen que se devolverán en la variable historicosFinales;
                            organismoEjb.obtenerHistoricosFinales(organismoDestino.getId(), historicosFinales);

                            for (Organismo organismo : historicosFinales) {
                                //Solo devolvemos aquellos sustitutos que tienen oficinas que le dan servicio
                                if (oficinaEjb.tieneOficinasServicio(organismoDestino.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO)) {
                                    sustitutos.add(organismo);
                                }
                            }
                            model.addAttribute("sustitutos", sustitutos);
                        }
                        model.addAttribute("estadoDestino", estado);
                    }

                }
            }else{
                Mensaje.saveMessageError(request, getMessage("registroSir.error.destino"));
                return  "redirect:/registroSir/list";
            }

        }else{
            model.addAttribute("rechazarForm", new RechazarForm());
            model.addAttribute("reenviarForm", new ReenviarForm());
        }

        model.addAttribute("trazabilidades", trazabilidadSirEjb.getByRegistroSir(registroSir.getId()));
        model.addAttribute("registroSir",registroSir);
        model.addAttribute("anexosSirFull",componerAnexoSirFull(registroSir.getAnexos()));

        return "registroSir/registroSirDetalle";
    }



    /**
     * Procesa {@link RegistroSir}, creando un RegistroEntrada
     */
    @RequestMapping(value = "/aceptar/{idRegistroSir}", method = RequestMethod.POST)
    public String confirmarRegistroSir(@PathVariable Long idRegistroSir, @ModelAttribute RegistrarForm registrarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        String variableReturn = "redirect:/registroSir/"+idRegistroSir+"/detalle";

        // Comprobamos si ya ha sido confirmado
        if(registroSir.getEstado().equals(EstadoRegistroSir.ACEPTADO)){
            Mensaje.saveMessageError(request, getMessage("registroSir.procesado.error"));
            return variableReturn;
        }

        // Procesa el RegistroSir
        try{

            RegistroEntrada registroEntrada = sirEnvioEjb.aceptarRegistroSir(registroSir, usuarioEntidad, oficinaActiva, registrarForm.getIdLibro(), registrarForm.getIdIdioma(), registrarForm.getCamposNTIs(), registrarForm.getCodigoSustituto());

            variableReturn = "redirect:/registroEntrada/" + registroEntrada.getId() + "/detalle";

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
            Mensaje.saveMessageError(request, getMessage("registroSir.procesado.error"));
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

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_REGISTRO;
    }

   /* @ModelAttribute("tiposAsunto")
    public List<TipoAsunto> tiposAsunto(HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        return tipoAsuntoEjb.getActivosEntidad(entidadActiva.getId());
    }*/

    @ModelAttribute("tiposValidezDocumento")
    public Long[] validezDocumento() throws Exception {

        return RegwebConstantes.TIPOS_VALIDEZDOCUMENTO;
    }

    @ModelAttribute("tiposDocumentales")
    public List<TipoDocumental> tiposDocumentales(HttpServletRequest request) throws Exception {
        Entidad entidadActiva = getEntidadActiva(request);
        return tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId());
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


