package es.caib.regweb3.webapp.controller.sir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoMensaje;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.utils.*;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.dom4j.Document;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/sir")
public class SirController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = RegistroSirLocal.JNDI_NAME)
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = TrazabilidadLocal.JNDI_NAME)
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = TrazabilidadSirLocal.JNDI_NAME)
    private TrazabilidadSirLocal trazabilidadSirEjb;

    @EJB(mappedName =ArchivoLocal.JNDI_NAME)
    private ArchivoLocal archivoEjb;

    @EJB(mappedName = SirEnvioLocal.JNDI_NAME)
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = MensajeControlLocal.JNDI_NAME)
    private MensajeControlLocal mensajeControlEjb;

    @EJB(mappedName = MensajeLocal.JNDI_NAME)
    private MensajeLocal mensajeEjb;



    /**
     * Acepta un registro sir, lo distribuye y copia la documentación a una carpeta
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/aceptarRegistroErte", method = RequestMethod.GET)
    public ModelAndView aceptarRegistroErte(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/aceptarRegistroErte");
        Entidad entidad = getEntidadActiva(request);

        ErteBusquedaForm erteBusquedaForm = new ErteBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("tipos", TipoRegistro.values());
        model.addAttribute("erteBusquedaForm", erteBusquedaForm);
        model.addAttribute("anys", getAnys());
        model.addAttribute("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Acepta un registro sir, lo distribuye y copia la documentación a una carpeta
     */
    @RequestMapping(value = "/aceptarRegistroErte", method = RequestMethod.POST)
    public ModelAndView aceptarRegistroErte(@ModelAttribute ErteBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/aceptarRegistroErte");
        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        long inicio = System.currentTimeMillis();

        RegistroSir registroSir = busqueda.getRegistroSir();

        List<Long> registros = registroSirEjb.getUltimosPendientesProcesarERTE(EstadoRegistroSir.RECIBIDO, registroSir.getCodigoEntidadRegistral(), busqueda.getFechaInicio(), busqueda.getFechaFin(), registroSir.getAplicacion(), busqueda.getTotal());

        Oficina oficina = oficinaEjb.findByCodigo(registroSir.getCodigoEntidadRegistral());

        sirEnvioEjb.aceptarRegistrosERTE(registros, entidad, busqueda.getDestino(), oficina, getLibroEntidad(request).getId(), usuarioEntidad);

        Mensaje.saveMessageInfo(request, "Se han procesado "+registros.size()+" registros en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("tipos", TipoRegistro.values());
        mav.addObject("registroSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;

    }

    /**
     * Copia la documentación a una carpeta de un Registroerte aceptado
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/copiarDocumentacion", method = RequestMethod.GET)
    public ModelAndView copiarDocumentacion(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/copiarDocumentacion");
        Entidad entidad = getEntidadActiva(request);

        ErteBusquedaForm erteBusquedaForm = new ErteBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("tipos", TipoRegistro.values());
        model.addAttribute("erteBusquedaForm", erteBusquedaForm);
        model.addAttribute("anys", getAnys());
        model.addAttribute("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Copia la documentación a una carpeta de un Registroerte aceptado
     */
    @RequestMapping(value = "/copiarDocumentacion", method = RequestMethod.POST)
    public ModelAndView copiarDocumentacion(@ModelAttribute ErteBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/copiarDocumentacion");
        Entidad entidad = getEntidadActiva(request);
        long inicio = System.currentTimeMillis();

        RegistroSir registroSir = busqueda.getRegistroSir();

        List<Long> registros = registroSirEjb.getUltimosPendientesProcesarERTE(EstadoRegistroSir.ACEPTADO, registroSir.getCodigoEntidadRegistral(), busqueda.getFechaInicio(), busqueda.getFechaFin(), registroSir.getAplicacion(), busqueda.getTotal());

        sirEnvioEjb.copiarDocumentacionERTE(registros, entidad.getId());

        Mensaje.saveMessageInfo(request, "Se han procesado "+registros.size()+" registros en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("tipos", TipoRegistro.values());
        mav.addObject("registroSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;

    }



    /**
     * Controller temporal para relacionar un RegistroSir con un RegistroEntrada
     */
    @RequestMapping(value = "/{idRegistroSir}/{idRegistroEntrada}/aceptar", method = RequestMethod.GET)
    public String aceptarRegistroSir(@PathVariable Long idRegistroSir,@PathVariable Long idRegistroEntrada, HttpServletRequest request) throws Exception {

        RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistroEntrada);

        if(registroSir == null || registroEntrada == null){

            Mensaje.saveMessageError(request,"El RegistroSir o el RegistroEntrada no existen");
            return "redirect:/inici";
        }

        try{

            // Creamos la TrazabilidadSir
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO);
            trazabilidadSir.setFecha(registroEntrada.getFecha());
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setRegistroEntrada(registroEntrada);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(registroEntrada.getUsuario().getNombreCompleto());
            trazabilidadSir.setContactoUsuario(registroEntrada.getUsuario().getUsuario().getEmail());
            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
            trazabilidadSirEjb.persist(trazabilidadSir);

            // CREAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
            trazabilidad.setRegistroSir(registroSir);
            trazabilidad.setRegistroEntradaOrigen(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setFecha(registroEntrada.getFecha());

            trazabilidadEjb.persist(trazabilidad);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.ACEPTADO);

            // Enviamos el Mensaje de Confirmación
            MensajeControl confirmacion = mensajeEjb.enviarMensajeConfirmacion(registroSir, registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getFecha());

            // Guardamos el mensaje de confirmación
            mensajeControlEjb.persist(confirmacion);

            Mensaje.saveMessageInfo(request,"Se ha vuelto a aceptar el RegistroSir y enviado el mensaje de conformación");

            return "redirect:/sir/"+registroSir.getIdentificadorIntercambio()+"/detalle";


        }catch (Exception e){
            e.printStackTrace();
            Mensaje.saveMessageError(request,"Ha ocurrido un error anulando el Oficio: " + e.getMessage());
        }

        return "redirect:/sir/"+registroSir.getIdentificadorIntercambio()+"/detalle";
    }

    /**
     * Controller temporal para anular Oficios enviados a SIR
     */
    @RequestMapping(value = "/{idOficio}/anular", method = RequestMethod.GET)
    public String anularOficioSir(@PathVariable Long idOficio, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficio);

        try{

            if((oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO || oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR)
                    && oficioRemision.getSir()){

                oficioRemision.setEstado(RegwebConstantes.OFICIO_ANULADO);
                oficioRemision.setFechaEstado(new Date());
                oficioRemisionEjb.merge(oficioRemision);

                Mensaje.saveMessageInfo(request,"Se ha anulado el intercambio");

                return "redirect:/sir/"+oficioRemision.getIdentificadorIntercambio()+"/detalle";
            }

        }catch (Exception e){
            e.printStackTrace();
            Mensaje.saveMessageError(request,"Ha ocurrido un error anulando el intercambio: " + e.getMessage());
        }

        return "redirect:/inici";
    }

    /**
     * Controller temporal para confirmar Oficios enviados a SIR
     */
    @RequestMapping(value = "/{idIntercambio}/confirmar", method = RequestMethod.GET)
    public String marcarConfirmacion(@PathVariable String idIntercambio, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        Dir3Caib dir3Caib = getLoginInfo(request).getDir3Caib();

        OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(idIntercambio);

        List<MensajeControl> mensajes = mensajeControlEjb.getByIdentificadorIntercambio(idIntercambio, entidad.getId());

        try{

            for (MensajeControl mensaje:mensajes) {
                if(mensaje.getTipoMensaje().equals(TipoMensaje.CONFIRMACION.getValue())){

                    oficioRemision.setCodigoEntidadRegistralProcesado(mensaje.getCodigoEntidadRegistralOrigen());
                    oficioRemision.setDecodificacionEntidadRegistralProcesado(Dir3CaibUtils.denominacion(dir3Caib.getServer(), mensaje.getCodigoEntidadRegistralOrigen(), RegwebConstantes.OFICINA));
                    oficioRemision.setNumeroRegistroEntradaDestino(mensaje.getNumeroRegistroEntradaDestino());
                    oficioRemision.setFechaEntradaDestino(mensaje.getFechaEntradaDestino());
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
                    oficioRemision.setFechaEstado(mensaje.getFechaEntradaDestino());
                    oficioRemisionEjb.merge(oficioRemision);

                    Mensaje.saveMessageInfo(request,"Se ha marcado como confirmado el oficio de remisión");

                    break;
                }
            }
            Mensaje.saveMessageAviso(request,"No existe ningún mensaje de confirmación para este Oficio");

        }catch (Exception e){
            e.printStackTrace();
            Mensaje.saveMessageInfo(request,"Ha ocurrido un error confirmado el Oficio: " + e.getMessage());
        }


        return "redirect:/inici";
    }


    /**
     * Carga el formulario para ver el detalle de un IdentificadorIntercambio
     */
    @RequestMapping(value = "/{idIntercambio}/detalle", method = RequestMethod.GET)
    public String detalleIdentificadorIntercambio(@PathVariable String idIntercambio, Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        List<TrazabilidadSir> trazabilidadesSir = trazabilidadSirEjb.getByIdIntercambio(idIntercambio, entidad.getId());
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByIdIntercambio(idIntercambio, entidad.getId());
        List<MensajeControl> mensajes = mensajeControlEjb.getByIdentificadorIntercambio(idIntercambio, entidad.getId());

        model.addAttribute("trazabilidadesSir", trazabilidadesSir);
        model.addAttribute("trazabilidades", trazabilidades);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("idIntercambio", idIntercambio);
        model.addAttribute("integracion", new BasicForm());

        return "sir/intercambioDetalle";
    }

    /**
     * Listado de oficios de remisión sir enviados
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.GET)
    public ModelAndView monitorEnviados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        List<Organismo> organismos = organismoEjb.getPermitirUsuarios(getEntidadActiva(request).getId());

        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("organismos", organismos);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.POST)
    public ModelAndView monitorEnviados(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        busqueda.setFechaFin(RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()));
        Entidad entidadActiva = getEntidadActiva(request);
        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getIdOrganismo(), busqueda.getFechaInicio(), busqueda.getFechaFin(),null, oficioRemision, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), true, entidadActiva.getId());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("organismos", organismoEjb.getPermitirUsuarios(getEntidadActiva(request).getId()));
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;
    }


    /**
     * Listado de RegistroSir recibidos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.GET)
    public ModelAndView monitorRecibidos(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSirBusquedaForm registroSirBusquedaForm = new RegistroSirBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("tipos", TipoRegistro.values());
        model.addAttribute("registroSirBusqueda", registroSirBusquedaForm);
        model.addAttribute("anys", getAnys());
        model.addAttribute("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));
        mav.addObject("eliminarForm", new EliminarForm());

        return mav;
    }

    /**
     * Realiza la busqueda de {@link RegistroSir} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.POST)
    public ModelAndView monitorRecibidos(@ModelAttribute RegistroSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSir registroSir = busqueda.getRegistroSir();

        Paginacion paginacion = registroSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()), registroSir, registroSir.getCodigoEntidadRegistral(), busqueda.getEstado(),entidad.getCodigoDir3());

        busqueda.setPageNumber(1);

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("tipos", TipoRegistro.values());
        mav.addObject("paginacion", paginacion);
        mav.addObject("registroSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));
        mav.addObject("eliminarForm", new EliminarForm());

        return mav;

    }

    /**
     * Listado de Registros de entrada pendientesDistribuir
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pendientesDistribuir/list", method = RequestMethod.GET)
    public ModelAndView pendientesDistribuir(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/pendientesDistribuirList");
        Entidad entidad = getEntidadActiva(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        mav.addObject("pendientesDistribuirBusqueda", registroEntradaBusqueda);
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de Registros de entrada pendientesDistribuir
     */
    @RequestMapping(value = "/pendientesDistribuir/list", method = RequestMethod.POST)
    public ModelAndView pendientesDistribuir(@ModelAttribute RegistroEntradaBusqueda busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/pendientesDistribuirList");
        Entidad entidad = getEntidadActiva(request);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        Paginacion paginacion = trazabilidadEjb.buscarPendientesDistribuirSir(registroEntrada.getOficina().getId(), entidad.getId(),busqueda.getPageNumber());

        busqueda.setPageNumber(1);

        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));
        mav.addObject("paginacion", paginacion);
        mav.addObject("pendientesDistribuirBusqueda", busqueda);

        return mav;

    }

    /**
     * Vuelve a enviar un Intercambio, ya enviado previamente
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reenviarIntercambio", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reenviarIntercambio(@RequestParam Long idOficioRemision)throws Exception {

        try{
            OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
            sirEnvioEjb.reenviarIntercambio(oficioRemision);

            return true;

        }catch ( I18NException e){
            log.info("Error volviendo a enviar el intercambio..");
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Vuelve a enviar un Intercambio, ya enviado previamente
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reintentarEnvioRegistroSir", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reintentarEnvioRegistroSir(@RequestParam Long idRegistroSir, HttpServletRequest request)throws Exception {

        try{
            sirEnvioEjb.reenviarRegistroSir(idRegistroSir, getEntidadActiva(request));

            return true;

        }catch (Exception e){
            log.info("Error volviendo a enviar el registro sir..");
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Reinicia el contador de reintentos SIR
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oficio/reiniciar", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reiniciarOficio(@RequestParam Long id, HttpServletRequest request)throws Exception {
        try{
            oficioRemisionEjb.reiniciarIntentos(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Reinicia el contador de reintentos SIR
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroSir/reiniciar", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reiniciarRegistroSir(@RequestParam Long id, HttpServletRequest request)throws Exception {
        try{
            registroSirEjb.reiniciarIntentos(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Envia un mensaje ACK
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enviarACK", method = RequestMethod.GET)
    @ResponseBody
    public Boolean enviarACK(@RequestParam Long idRegistroSir)throws Exception {

        try{
            return sirEnvioEjb.enviarACK(idRegistroSir);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Envia un mensaje de Confirmación
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enviarConfirmacion", method = RequestMethod.GET)
    @ResponseBody
    public Boolean enviarConfirmacion(@RequestParam Long idRegistroSir)throws Exception {

        try{
            return sirEnvioEjb.enviarConfirmacion(idRegistroSir);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.RegistroSir}
     */
    @RequestMapping(value = "/registroSir/eliminar", method= RequestMethod.POST)
    public String eliminarRegistroSir(@ModelAttribute EliminarForm eliminarForm, HttpServletRequest request) {

        try {

            UsuarioEntidad usuario = getUsuarioEntidadActivo(request);
            RegistroSir registroSir  = registroSirEjb.findById(eliminarForm.getId());

            if(EstadoRegistroSir.RECIBIDO.equals(registroSir.getEstado()) || EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado())
                    || EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado())){
                registroSirEjb.marcarEliminado(registroSir, usuario, eliminarForm.getObservaciones());
                Mensaje.saveMessageInfo(request, getMessage("registroSir.eliminar.ok"));

            }else{
                Mensaje.saveMessageError(request, getMessage("registroSir.eliminar.estado"));
            }

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("registroSir.eliminar.error"));
            e.printStackTrace();
        }

        return "redirect:/sir/monitorRecibidos";
    }

    /**
     * Genera el xml del fichero de intercambio enviado
     * @param idOficioRemision
     * @param request
     * @param response
     */
    @RequestMapping(value = "/{idOficioRemision}/ficheroIntercambio", method = RequestMethod.GET)
    public void generarFicheroIntercambio(@PathVariable("idOficioRemision") Long idOficioRemision, HttpServletRequest request, HttpServletResponse response)  {
        RegistroSir registroSir = null;
        Sicres3XML sicres3XML = new Sicres3XML();

        try {
            OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

            if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficioRemision.getRegistrosEntrada().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);
            }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficioRemision.getRegistrosSalida().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);
            }

            if(registroSir != null){
                Document doc = sicres3XML.crearXMLFicheroIntercambioSICRES3(registroSir);

                try {

                    String filename = registroSir.getIdentificadorIntercambio()+".xml";
                    response.setContentType("text/xml");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                    response.setContentLength( doc.asXML().getBytes().length);

                    OutputStream output = response.getOutputStream();
                    output.write(doc.asXML().getBytes(Charset.forName("UTF-8")));
                    output.flush();
                    output.close();


                } catch (NumberFormatException e) {
                    log.info(e.getMessage());
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Total Archivos huerfanos
     */
    @RequestMapping(value = "/huerfanos", method = RequestMethod.GET)
    public String huerfanos(HttpServletRequest request) {

        Integer total = 0;
        Integer bbdd = 0;
        Integer documentCustody = 0;
        Integer huerfanos = 0;

        try {
            File directorio = new File(Configuracio.getArchivosPath());
            List<Long> archivos = archivoEjb.getAllLigero();

            if(directorio != null){
                File[] ficheros = directorio.listFiles();

                for (File fichero : ficheros) {
                    if(fichero.isFile()){
                        total = total + 1;
                        try{
                            Long idArchivo = Long.valueOf(fichero.getName());

                            if(!archivos.contains(idArchivo)){

                                huerfanos = huerfanos + 1;
                            }else{
                                bbdd = bbdd + 1;
                            }

                        }catch (NumberFormatException n){
                            documentCustody = documentCustody +1;
                        }
                    }

                }
                Mensaje.saveMessageInfo(request,"Hay " + total+ " ficheros en total");
                Mensaje.saveMessageInfo(request,"Hay " + (documentCustody) + " almacenados en DocumentCustody");
                Mensaje.saveMessageInfo(request,"Hay " + bbdd + " almacenados en bbdd");
                Mensaje.saveMessageInfo(request,"Hay " + huerfanos + " huerfanos");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "redirect:/inici";
    }

    /**
     * Purgar Archivos huerfanos
     */
    @RequestMapping(value = "/purgarHuerfanos", method = RequestMethod.GET)
    public String purgarHuerfanos(HttpServletRequest request) {
        Integer count = 0;

        try {
            File directorio = new File(Configuracio.getArchivosPath());
            List<Long> archivos = archivoEjb.getAllLigero();

            if(directorio != null){
                File[] ficheros = directorio.listFiles();

                for (File fichero : ficheros) {
                    if(fichero.isFile()){

                        try{
                            Long idArchivo = Long.valueOf(fichero.getName());

                            if(!archivos.contains(idArchivo)){
                                log.info("Eliminamos el fichero huerfano: " + fichero.getName());
                                FileSystemManager.eliminarArchivo(Long.valueOf(fichero.getName()));
                                count = count + 1;
                            }
                        }catch (NumberFormatException n){
                            //log.info("Omitimos el archivo: " + fichero.getName());
                        }
                    }

                }
                Mensaje.saveMessageInfo(request,"Se han eliminado " + count + " ficheros huerfanos");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "redirect:/inici";
    }

    /**
     * Cambiar estado OFICIO_SIR
     */
    @RequestMapping(value = "/cambiarEstadoSirEntrada", method = RequestMethod.GET)
    public String cambiarEstadoSirEntrada(HttpServletRequest request) throws Exception {

        Integer total = trazabilidadEjb.actualizarEstadoSirEntrada(getEntidadActiva(request).getId());
        Mensaje.saveMessageInfo(request,"Se han actualizado " + total + " registros de entrada");

        return "redirect:/inici";
    }

    /**
     * Cambiar estado OFICIO_SIR
     */
    @RequestMapping(value = "/cambiarEstadoSirSalida", method = RequestMethod.GET)
    public String cambiarEstadoSirSalida(HttpServletRequest request) throws Exception {

        Integer total = trazabilidadEjb.actualizarEstadoSirSalida(getEntidadActiva(request).getId());
        Mensaje.saveMessageInfo(request,"Se han actualizado " + total + " registros de salida");

        return "redirect:/inici";
    }

}
