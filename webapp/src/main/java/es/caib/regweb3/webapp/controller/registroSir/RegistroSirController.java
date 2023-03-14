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
import es.caib.regweb3.utils.TimeStamp;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
/*import es.gob.ad.registros.sir.gestionEni.bean.ContenidoBean;
import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.bean.InteresadoBean;
import es.gob.ad.registros.sir.interService.bean.OtrosMetadatos;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoDocumental;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoMetadatos;*/
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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.FORMATO_FECHA_SICRES4;

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

        RegistroSir registroSir = registroSirEjb.getRegistroSirConMetadatos(idRegistroSir);

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

            RegistroEntrada registroEntrada = sirEnvioEjb.aceptarRegistroSir(registroSir, entidad, usuarioEntidad, oficinaActiva, registrarForm.getIdLibro(), registrarForm.getIdIdioma(), registrarForm.getIdOrganismoDestino(), registrarForm.getDistribuir(), registrarForm.getEmails(), registrarForm.getMotivo());

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

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroSir/" + idRegistroSir + "/detalle";
    }

    /*@RequestMapping(value = "/pruebaAsientoBean", method = RequestMethod.GET)
    public String pruebaAsientoBean(HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        Entidad entidad = getEntidadActiva(request);
        //Parte de los datos cogidos de SIR-RC-PR-001
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_SICRES4);

        AsientoBean asientoBean = new AsientoBean();
        asientoBean.setCdEnRgOrigen("O00002721");
        asientoBean.setDsEnRgOrigen("Correos de cala d'Or");
        asientoBean.setNuRgOrigen("NOREGAGE19e000000001");
        asientoBean.setFeRgOrigen(format.parse("20220707123922CEST"));
        asientoBean.setTsRgOrigen( "MIIpIgYJKoZIhvcNAQcCoIIpEzCCKQ8CAQMxCzAJBgUrDgMCGgUAMIIfTAYLKoZIhvcNAQkQAQSggh87BIIfNzCCHzMCAQEGBysEBgEDBAYwgh8BMAkGBSsOAwIaBQAEgh7yPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48RmljaGVyb19JbnRlcmNhbWJpb19TSUNSRVNfMyB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIj4KPERlX09yaWdlbl9vX1JlbWl0ZW50ZT4KICAgIDxDb2RpZ29fRW50aWRhZF9SZWdpc3RyYWxfT3JpZ2VuPjwhW0NEQVRBW08wMDAwMjc0MV1dPjwvQ29kaWdvX0VudGlkYWRfUmVnaXN0cmFsX09yaWdlbj4KICAgIDxEZWNvZGlmaWNhY2lvbl9FbnRpZGFkX1JlZ2lzdHJhbF9PcmlnZW4+PCFbQ0RBVEFbUkVHSVNUUk8gR0VORVJBTCBERUwgQVlVTlRBTUlFTlRPIERFIEpVTl1dPjwvRGVjb2RpZmljYWNpb25fRW50aWRhZF9SZWdpc3RyYWxfT3JpZ2VuPgogICAgPE51bWVyb19SZWdpc3Ryb19FbnRyYWRhPjwhW0NEQVRBW08wMDAwMjc0MV8xNV8wMDAwNzc0XV0+PC9OdW1lcm9fUmVnaXN0cm9fRW50cmFkYT4KICAgIDxGZWNoYV9Ib3JhX0VudHJhZGE+PCFbQ0RBVEFbMjAxNTA3MjIxMzM1MjRdXT48L0ZlY2hhX0hvcmFfRW50cmFkYT4KICAgIDxUaW1lc3RhbXBfRW50cmFkYS8+CiAgICA8Q29kaWdvX1VuaWRhZF9UcmFtaXRhY2lvbl9PcmlnZW4vPgogICAgPERlY29kaWZpY2FjaW9uX1VuaWRhZF9UcmFtaXRhY2lvbl9PcmlnZW4vPgo8L0RlX09yaWdlbl9vX1JlbWl0ZW50ZT4KPERlX0Rlc3Rpbm8+CiAgICA8Q29kaWdvX0VudGlkYWRfUmVnaXN0cmFsX0Rlc3Rpbm8+PCFbQ0RBVEFbTzAwMDAxMjMwXV0+PC9Db2RpZ29fRW50aWRhZF9SZWdpc3RyYWxfRGVzdGlubz4KICAgIDxEZWNvZGlmaWNhY2lvbl9FbnRpZGFkX1JlZ2lzdHJhbF9EZXN0aW5vPjwhW0NEQVRBW01JTklTVEVSSU8gREUgU0FOSURBRCwgU0VSVklDSU9TIFNPQ0lBTEVTIEUgSUdVQUxEQURdXT48L0RlY29kaWZpY2FjaW9uX0VudGlkYWRfUmVnaXN0cmFsX0Rlc3Rpbm8+CiAgICA8Q29kaWdvX1VuaWRhZF9UcmFtaXRhY2lvbl9EZXN0aW5vPjwhW0NEQVRBW0UwNDkyMTkwMV1dPjwvQ29kaWdvX1VuaWRhZF9UcmFtaXRhY2lvbl9EZXN0aW5vPgogICAgPERlY29kaWZpY2FjaW9uX1VuaWRhZF9UcmFtaXRhY2lvbl9EZXN0aW5vPjwhW0NEQVRBW01JTklTVEVSSU8gREUgU0FOSURBRCwgU0VSVklDSU9TIFNPQ0lBTEVTIEUgSUdVQUxEQURdXT48L0RlY29kaWZpY2FjaW9uX1VuaWRhZF9UcmFtaXRhY2lvbl9EZXN0aW5vPgo8L0RlX0Rlc3Rpbm8+CjxEZV9JbnRlcmVzYWRvPgogICAgPFRpcG9fRG9jdW1lbnRvX0lkZW50aWZpY2FjaW9uX0ludGVyZXNhZG8vPgogICAgPERvY3VtZW50b19JZGVudGlmaWNhY2lvbl9JbnRlcmVzYWRvLz4KICAgIDxSYXpvbl9Tb2NpYWxfSW50ZXJlc2Fkby8+CiAgICA8Tm9tYnJlX0ludGVyZXNhZG8+PCFbQ0RBVEFbSk9TRV1dPjwvTm9tYnJlX0ludGVyZXNhZG8+CiAgICA8UHJpbWVyX0FwZWxsaWRvX0ludGVyZXNhZG8+PCFbQ0RBVEFbVklPVEFdXT48L1ByaW1lcl9BcGVsbGlkb19JbnRlcmVzYWRvPgogICAgPFNlZ3VuZG9fQXBlbGxpZG9fSW50ZXJlc2Fkby8+CiAgICA8VGlwb19Eb2N1bWVudG9fSWRlbnRpZmljYWNpb25fUmVwcmVzZW50YW50ZS8+CiAgICA8RG9jdW1lbnRvX0lkZW50aWZpY2FjaW9uX1JlcHJlc2VudGFudGUvPgogICAgPFJhem9uX1NvY2lhbF9SZXByZXNlbnRhbnRlLz4KICAgIDxOb21icmVfUmVwcmVzZW50YW50ZS8+CiAgICA8UHJpbWVyX0FwZWxsaWRvX1JlcHJlc2VudGFudGUvPgogICAgPFNlZ3VuZG9fQXBlbGxpZG9fUmVwcmVzZW50YW50ZS8+CiAgICA8UGFpc19JbnRlcmVzYWRvLz4KICAgIDxQcm92aW5jaWFfSW50ZXJlc2Fkby8+CiAgICA8TXVuaWNpcGlvX0ludGVyZXNhZG8vPgogICAgPERpcmVjY2lvbl9JbnRlcmVzYWRvLz4KICAgIDxDb2RpZ29fUG9zdGFsX0ludGVyZXNhZG8vPgogICAgPENvcnJlb19FbGVjdHJvbmljb19JbnRlcmVzYWRvLz4KICAgIDxUZWxlZm9ub19Db250YWN0b19JbnRlcmVzYWRvLz4KICAgIDxEaXJlY2Npb25fRWxlY3Ryb25pY2FfSGFiaWxpdGFkYV9JbnRlcmVzYWRvLz4KICAgIDxDYW5hbF9QcmVmZXJlbnRlX0NvbXVuaWNhY2lvbl9JbnRlcmVzYWRvLz4KICAgIDxQYWlzX1JlcHJlc2VudGFudGUvPgogICAgPFByb3ZpbmNpYV9SZXByZXNlbnRhbnRlLz4KICAgIDxNdW5pY2lwaW9fUmVwcmVzZW50YW50ZS8+CiAgICA8RGlyZWNjaW9uX1JlcHJlc2VudGFudGUvPgogICAgPENvZGlnb19Qb3N0YWxfUmVwcmVzZW50YW50ZS8+CiAgICA8Q29ycmVvX0VsZWN0cm9uaWNvX1JlcHJlc2VudGFudGUvPgogICAgPFRlbGVmb25vX0NvbnRhY3RvX1JlcHJlc2VudGFudGUvPgogICAgPERpcmVjY2lvbl9FbGVjdHJvbmljYV9IYWJpbGl0YWRhX1JlcHJlc2VudGFudGUvPgogICAgPENhbmFsX1ByZWZlcmVudGVfQ29tdW5pY2FjaW9uX1JlcHJlc2VudGFudGUvPgogICAgPE9ic2VydmFjaW9uZXMvPgo8L0RlX0ludGVyZXNhZG8+CjxEZV9Bc3VudG8+CiAgICA8UmVzdW1lbj48IVtDREFUQVtTT0xJQ0lUVUQgTUlSXV0+PC9SZXN1bWVuPgogICAgPENvZGlnb19Bc3VudG9fU2VndW5fRGVzdGluby8+CiAgICA8UmVmZXJlbmNpYV9FeHRlcm5hLz4KICAgIDxOdW1lcm9fRXhwZWRpZW50ZS8+CjwvRGVfQXN1bnRvPgo8RGVfQW5leG8+CiAgICA8Tm9tYnJlX0ZpY2hlcm9fQW5leGFkbz48IVtDREFUQVtlamVtcGxvLnBkZl1dPjwvTm9tYnJlX0ZpY2hlcm9fQW5leGFkbz4KICAgIDxJZGVudGlmaWNhZG9yX0ZpY2hlcm8+PCFbQ0RBVEFbTzAwMDAyNzQxXzE1XzAwMDAwNTY5XzAxXzAwMDEucGRmXV0+PC9JZGVudGlmaWNhZG9yX0ZpY2hlcm8+CiAgICA8VmFsaWRlel9Eb2N1bWVudG8+PCFbQ0RBVEFbMDNdXT48L1ZhbGlkZXpfRG9jdW1lbnRvPgogICAgPFRpcG9fRG9jdW1lbnRvPjwhW0NEQVRBWzAyXV0+PC9UaXBvX0RvY3VtZW50bz4KICAgIDxDZXJ0aWZpY2Fkby8+CiAgICA8RmlybWFfRG9jdW1lbnRvLz4KICAgIDxUaW1lU3RhbXAvPgogICAgPFZhbGlkYWNpb25fT0NTUF9DZXJ0aWZpY2Fkby8+CiAgICA8SGFzaD48IVtDREFUQVtaV1ExWldaaU5tWTROall4WldFeU9EQmtZakk0T1dJME5EUmhaalJqTXpZMk5tRTJNREF5TURVMU5tSmhNVEkyWm1Nd1kySm1Oemd3Tmpsak5tUm1Zdz09XV0+PC9IYXNoPgogICAgPFRpcG9fTUlNRT48IVtDREFUQVthcHBsaWNhdGlvbi9wZGZdXT48L1RpcG9fTUlNRT4KICAgIDxBbmV4by8+CiAgICA8SWRlbnRpZmljYWRvcl9Eb2N1bWVudG9fRmlybWFkby8+CiAgICA8T2JzZXJ2YWNpb25lcy8+CjwvRGVfQW5leG8+CjxEZV9BbmV4bz4KICAgIDxOb21icmVfRmljaGVyb19BbmV4YWRvPjwhW0NEQVRBW2Zpcm1hLnhzaWddXT48L05vbWJyZV9GaWNoZXJvX0FuZXhhZG8+CiAgICA8SWRlbnRpZmljYWRvcl9GaWNoZXJvPjwhW0NEQVRBW08wMDAwMjc0MV8xNV8wMDAwMDU2OV8wMV8wMDAyLnhzaWddXT48L0lkZW50aWZpY2Fkb3JfRmljaGVybz4KICAgIDxWYWxpZGV6X0RvY3VtZW50by8+CiAgICA8VGlwb19Eb2N1bWVudG8+PCFbQ0RBVEFbMDNdXT48L1RpcG9fRG9jdW1lbnRvPgogICAgPENlcnRpZmljYWRvPjwhW0NEQVRBW01JSUZPakNDQktPZ0F3SUJBZ0lFUFNhNzNqQU5CZ2txaGtpRzl3MEJBUVVGQURBMk1Rc3dDUVlEVlFRR0V3SkZVekVOTUFzR0ExVUUKQ2hNRVJrNU5WREVZTUJZR0ExVUVDeE1QUms1TlZDQkRiR0Z6WlNBeUlFTkJNQjRYRFRFMU1ERXlOakV6TWpJeU9Wb1hEVEU0TURFeQpOakV6TWpJeU9Wb3dnWUl4Q3pBSkJnTlZCQVlUQWtWVE1RMHdDd1lEVlFRS0V3UkdUazFVTVJnd0ZnWURWUVFMRXc5R1RrMVVJRU5zCllYTmxJRElnUTBFeEVqQVFCZ05WQkFzVENUVXdNRFk0TURBeU1qRTJNRFFHQTFVRUF4TXRUazlOUWxKRklGWkpUMVJCSUVoRlVrRlQKSUVwUFUwVWdUVUZTU1VFZ0xTQk9TVVlnTURJMk5UazFPVE5DTUlHZk1BMEdDU3FHU0liM0RRRUJBUVVBQTRHTkFEQ0JpUUtCZ1FDeQp4SzFjMUxWd2I2NTZ5b1krZkJvZzJRUnpmc3ZHQkxxYUh6ZEpteHFvaVhEaDRGRjNsVFMrbTVtcjVoWEh0NENqbENyNXlyZFc5OHRZCmgzQWlKS3l4WFJZTjNQdGJwcnJ1dHRUQWFMV2tEaGlFOThkQy9GZVZLK3doR1VNeFUyNjFVSUVKZDhzN3k4S1ArRDV3bjBBeVVNcWUKQ3ROTElRTFYzZEducUJoSSt3SURBUUFCbzRJREJqQ0NBd0l3Z1lRR0ExVWRFUVI5TUh1QkZFcFBVMFZOUVZaSlQxUkFSMDFCU1V3dQpRMDlOcEdNd1lURVlNQllHQ1NzR0FRUUJyR1lCQkJNSk1ESTJOVGsxT1ROQ01SUXdFZ1lKS3dZQkJBR3NaZ0VERXdWSVJWSkJVekVVCk1CSUdDU3NHQVFRQnJHWUJBaE1GVmtsUFZFRXhHVEFYQmdrckJnRUVBYXhtQVFFVENrcFBVMFVnVFVGU1NVRXdDUVlEVlIwVEJBSXcKQURBckJnTlZIUkFFSkRBaWdBOHlNREUxTURFeU5qRXpNakl5T1ZxQkR6SXdNVGd3TVRJMk1UTXlNakk1V2pBTEJnTlZIUThFQkFNQwpCYUF3RVFZSllJWklBWWI0UWdFQkJBUURBZ1dnTUIwR0ExVWREZ1FXQkJUM28rY3pDNHBmdzF0UGh0QWRxTmR6S3N0Y0ZqQWZCZ05WCkhTTUVHREFXZ0JSQW1uWkVsM1FIeEt3VXl4Nk5UenBGZkREWFlUQ0NBVEVHQTFVZElBU0NBU2d3Z2dFa01JSUJJQVlKS3dZQkJBR3MKWmdNRk1JSUJFVEEwQmdnckJnRUZCUWNDQVJZb2FIUjBjRG92TDNkM2R5NWpaWEowTG1adWJYUXVaWE12WTI5dWRtVnVhVzh2WkhCagpMbkJrWmpDQjJBWUlLd1lCQlFVSEFnSXdnY3NhZ2NoRFpYSjBhV1pwWTJGa2J5QlNaV052Ym05amFXUnZJR1Y0Y0dWa2FXUnZJSE5sClovcHVJR3hsWjJsemJHRmphZk51SUhacFoyVnVkR1V1VlhOdklHeHBiV2wwWVdSdklHRWdiR0VnUTI5dGRXNXBaR0ZrSUVWc1pXTjAKY3ZOdWFXTmhJSEJ2Y2lCMllXeHZjaUJ0NFhocGJXOGdaR1VnTVRBd0lHVWdjMkZzZG04Z1pYaGpaWEJqYVc5dVpYTWdaVzRnUkZCRApMa052Ym5SaFkzUnZJRVpPVFZRNlF5OUtiM0puWlNCS2RXRnVJREV3TmkweU9EQXdPUzFOWVdSeWFXUXRSWE53WWZGaExqQWRCZ2tyCkJnRUVBYXhtQVNFRUVCWU9VRVZTVTA5T1FTQkdTVk5KUTBFd0x3WUlLd1lCQlFVSEFRTUVJekFoTUFnR0JnUUFqa1lCQVRBVkJnWUUKQUk1R0FRSXdDeE1EUlZWU0FnRmtBZ0VBTUZ3R0ExVWRId1JWTUZNd1VhQlBvRTJrU3pCSk1Rc3dDUVlEVlFRR0V3SkZVekVOTUFzRwpBMVVFQ2hNRVJrNU5WREVZTUJZR0ExVUVDeE1QUms1TlZDQkRiR0Z6WlNBeUlFTkJNUkV3RHdZRFZRUURFd2hEVWt3eE5qTTNOREFOCkJna3Foa2lHOXcwQkFRVUZBQU9CZ1FBK3p2b3UyaEkydmw4TDVNMHplZkcxeWdYSVFhbUdmMmtiSXdoRWtHUUMycXVnUzFndTl5U1QKRGlPeXlyUDFCUzFjR0V0NEMrL0VPZ3hYUGljTVYxM290TEJIT0NjQ1hsTVVCengrWG5Sd2Y5VWttdjlEQkFrbzFFMkgxcithdkJLOQphaXRZK3p5MWYrZjRndHNBTjZLSUFwZXdPTlU4S21uVzRvZjBseTh0OVE9PV1dPjwvQ2VydGlmaWNhZG8+CiAgICA8RmlybWFfRG9jdW1lbnRvLz4KICAgIDxUaW1lU3RhbXAvPgogICAgPFZhbGlkYWNpb25fT0NTUF9DZXJ0aWZpY2Fkby8+CiAgICA8SGFzaD48IVtDREFUQVtZVEV3T0dReU5UZ3pZakJqTm1FelpUWTJNV1ExWkRSbE56WTJNekkyT1RNPV1dPjwvSGFzaD4KICAgIDxUaXBvX01JTUU+PCFbQ0RBVEFbYXBwbGljYXRpb24veG1sXV0+PC9UaXBvX01JTUU+CiAgICA8QW5leG8vPgogICAgPElkZW50aWZpY2Fkb3JfRG9jdW1lbnRvX0Zpcm1hZG8+PCFbQ0RBVEFbTzAwMDAyNzQxXzE1XzAwMDAwNTY5XzAxXzAwMDEucGRmXV0+PC9JZGVudGlmaWNhZG9yX0RvY3VtZW50b19GaXJtYWRvPgogICAgPE9ic2VydmFjaW9uZXMvPgo8L0RlX0FuZXhvPgo8RGVfQW5leG8+CiAgICA8Tm9tYnJlX0ZpY2hlcm9fQW5leGFkbz48IVtDREFUQVtqdXN0aWZpY2FudGUucGRmXV0+PC9Ob21icmVfRmljaGVyb19BbmV4YWRvPgogICAgPElkZW50aWZpY2Fkb3JfRmljaGVybz48IVtDREFUQVtPMDAwMDI3NDFfMTVfMDAwMDA1NjlfMDFfMDAwMy5wZGZdXT48L0lkZW50aWZpY2Fkb3JfRmljaGVybz4KICAgIDxWYWxpZGV6X0RvY3VtZW50bz48IVtDREFUQVswNF1dPjwvVmFsaWRlel9Eb2N1bWVudG8+CiAgICA8VGlwb19Eb2N1bWVudG8+PCFbQ0RBVEFbMDJdXT48L1RpcG9fRG9jdW1lbnRvPgogICAgPENlcnRpZmljYWRvLz4KICAgIDxGaXJtYV9Eb2N1bWVudG8vPgogICAgPFRpbWVTdGFtcC8+CiAgICA8VmFsaWRhY2lvbl9PQ1NQX0NlcnRpZmljYWRvLz4KICAgIDxIYXNoPjwhW0NEQVRBW05qTXdOell4TURVM1ltRm1OVEZoT1Raak1ETXdORGxpTVdSaVl6RTNNekU9XV0+PC9IYXNoPgogICAgPFRpcG9fTUlNRT48IVtDREFUQVthcHBsaWNhdGlvbi9wZGZdXT48L1RpcG9fTUlNRT4KICAgIDxBbmV4by8+CiAgICA8SWRlbnRpZmljYWRvcl9Eb2N1bWVudG9fRmlybWFkby8+CiAgICA8T2JzZXJ2YWNpb25lcy8+CjwvRGVfQW5leG8+CjxEZV9BbmV4bz4KICAgIDxOb21icmVfRmljaGVyb19BbmV4YWRvPjwhW0NEQVRBW3NpY3Jlc19maXJtYWRvLnhzaWddXT48L05vbWJyZV9GaWNoZXJvX0FuZXhhZG8+CiAgICA8SWRlbnRpZmljYWRvcl9GaWNoZXJvPjwhW0NEQVRBW08wMDAwMjc0MV8xNV8wMDAwMDU2OV8wMV8wMDA0LnhzaWddXT48L0lkZW50aWZpY2Fkb3JfRmljaGVybz4KICAgIDxWYWxpZGV6X0RvY3VtZW50bz48IVtDREFUQVswNF1dPjwvVmFsaWRlel9Eb2N1bWVudG8+CiAgICA8VGlwb19Eb2N1bWVudG8+PCFbQ0RBVEFbMDNdXT48L1RpcG9fRG9jdW1lbnRvPgogICAgPENlcnRpZmljYWRvLz4KICAgIDxGaXJtYV9Eb2N1bWVudG8vPgogICAgPFRpbWVTdGFtcC8+CiAgICA8VmFsaWRhY2lvbl9PQ1NQX0NlcnRpZmljYWRvLz4KICAgIDxIYXNoPjwhW0NEQVRBW05ESmhZVEptWkRBMFl6azNPR1k0T1RNM09HVmxZVFU0WXpsa1pUa3laRFk9XV0+PC9IYXNoPgogICAgPFRpcG9fTUlNRT48IVtDREFUQVthcHBsaWNhdGlvbi94bWxdXT48L1RpcG9fTUlNRT4KICAgIDxBbmV4by8+CiAgICA8SWRlbnRpZmljYWRvcl9Eb2N1bWVudG9fRmlybWFkby8+CiAgICA8T2JzZXJ2YWNpb25lcy8+CjwvRGVfQW5leG8+CjxEZV9JbnRlcm5vc19Db250cm9sPgogICAgPFRpcG9fVHJhbnNwb3J0ZV9FbnRyYWRhLz4KICAgIDxOdW1lcm9fVHJhbnNwb3J0ZV9FbnRyYWRhLz4KICAgIDxOb21icmVfVXN1YXJpbz48IVtDREFUQVtKb3NlIE1hcsOtYSBWaW90YSBkZSBsYXMgSGVyYXNdXT48L05vbWJyZV9Vc3VhcmlvPgogICAgPENvbnRhY3RvX1VzdWFyaW8vPgogICAgPElkZW50aWZpY2Fkb3JfSW50ZXJjYW1iaW8+PCFbQ0RBVEFbTzAwMDAyNzQxXzE1XzAwMDAwNTY5XV0+PC9JZGVudGlmaWNhZG9yX0ludGVyY2FtYmlvPgogICAgPEFwbGljYWNpb25fVmVyc2lvbl9FbWlzb3JhLz4KICAgIDxUaXBvX0Fub3RhY2lvbj48IVtDREFUQVswMl1dPjwvVGlwb19Bbm90YWNpb24+CiAgICA8RGVzY3JpcGNpb25fVGlwb19Bbm90YWNpb24vPgogICAgPFRpcG9fUmVnaXN0cm8+PCFbQ0RBVEFbMF1dPjwvVGlwb19SZWdpc3Rybz4KICAgIDxEb2N1bWVudGFjaW9uX0Zpc2ljYT48IVtDREFUQVsxXV0+PC9Eb2N1bWVudGFjaW9uX0Zpc2ljYT4KICAgIDxPYnNlcnZhY2lvbmVzX0FwdW50ZS8+CiAgICA8SW5kaWNhZG9yX1BydWViYT48IVtDREFUQVswXV0+PC9JbmRpY2Fkb3JfUHJ1ZWJhPgogICAgPENvZGlnb19FbnRpZGFkX1JlZ2lzdHJhbF9JbmljaW8+PCFbQ0RBVEFbTzAwMDAyNzQxXV0+PC9Db2RpZ29fRW50aWRhZF9SZWdpc3RyYWxfSW5pY2lvPgogICAgPERlY29kaWZpY2FjaW9uX0VudGlkYWRfUmVnaXN0cmFsX0luaWNpbz48IVtDREFUQVtSRUdJU1RSTyBHRU5FUkFMIERFTCBBWVVOVEFNSUVOVE8gREUgSlVOXV0+PC9EZWNvZGlmaWNhY2lvbl9FbnRpZGFkX1JlZ2lzdHJhbF9JbmljaW8+CjwvRGVfSW50ZXJub3NfQ29udHJvbD4KPERlX0Zvcm11bGFyaW9fR2VuZXJpY28+CiAgICA8RXhwb25lLz4KICAgIDxTb2xpY2l0YS8+CjwvRGVfRm9ybXVsYXJpb19HZW5lcmljbz4KPC9GaWNoZXJvX0ludGVyY2FtYmlvX1NJQ1JFU18zPgoCBAhmCiYYDzIwMTUwNzIyMTE0MjA2WjAJAgEBgAEBgQECoIIHczCCB28wggZXoAMCAQICAwUHnjANBgkqhkiG9w0BAQUFADBEMQswCQYD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");
        asientoBean.setCdUnTrOrigen("EA0023307");
        asientoBean.setDsUnTrOrigen("Sociedad Estatal Correos y Telegrafos, S.A.");

        //Destino
        asientoBean.setCdEnRgDestino("O00006056");
        asientoBean.setDsEnRgDestino("C. Salud y Consumo - Oficina Principal");
        asientoBean.setCdUnTrDestino("A04026919");
        asientoBean.setDsUnTrDestino("Consejería de Salud y Consumo");
        asientoBean.setDsResumen("Resumen del asiento");
        asientoBean.setCdAsunto("O001");
        asientoBean.setRfExterna("JRR0909");
        asientoBean.setNuExpediente("EXP_MPR0000000000000000000000000000000000000000000000000000000000000000000010207");
        asientoBean.setCdTpTransporte("02");
        asientoBean.setNuTransporte("00000000000000000001");
        asientoBean.setNoUsuario("PRUEBA TODOS LOS CAMPOS");
        asientoBean.setCtUsuario("333669987");
        asientoBean.setCdIntercambio("O00002721_22_90000000");
        asientoBean.setApVersion("ORVE0000000000000001");
        asientoBean.setCdTpAnotacion("02");
        asientoBean.setDsTpAnotacion("Envio de asiento sin representante Envio de asiento sin representante Envio de asiento sin representante Envio de asiento sin representante Envio de asiento sin");
        asientoBean.setCdTpRegistro("1");
        asientoBean.setCdDocFisica("1");
        asientoBean.setDsObservaciones("160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160Caracteres160C");
        asientoBean.setCdInPrueba("1");
        asientoBean.setCdEnRgInicio("O00002721");
        asientoBean.setDsEnRgInicio("Correos de cala d'Or");
        asientoBean.setDsExpone("");
        asientoBean.setDsSolicita("");
        asientoBean.setCdEstado("R");
        // asientoBean.setFeEntradaDestino("new Date()");
        // asientoBean.setNuRgEntradaDestino("");
        asientoBean.setModoRegistro("2"); //Eliminar el 0 inicial
        asientoBean.setFeRgPresentacion(format.parse("20220707123922CEST"));
        asientoBean.setTsRgPresentacion("c2VsbG9kZXRpZW1wbw==");
        asientoBean.setCdSia("00000000000000000000000000000000000000000000000000000000000000000000000000000001");
        asientoBean.setReferenciaUnica(false);
        asientoBean.setParaIntercambiar(false);
        asientoBean.setCdEnRgProcesa("A04003003");
        asientoBean.setCdIntercambioPrevio("");
        asientoBean.setCdUnTrInicio("EA0023307");
        asientoBean.setDsUnTrInicio("Sociedad Estatal Correos y Telegrafos, S.A.");

        //No tiene interados ni anexos

        //Metadatos Asiento
        OtrosMetadatos metadato = new OtrosMetadatos();
        Set<OtrosMetadatos> metadatosGenerales = new HashSet<OtrosMetadatos>();
        Set<OtrosMetadatos> metadatosParticulares = new HashSet<OtrosMetadatos>();
        metadato.setCampo("GeneralAsiento");
        metadato.setValor("ASIENTO");
        metadatosGenerales.add(metadato);
        asientoBean.setOtrosMetadatosGenerales(metadatosGenerales);

        metadato = new OtrosMetadatos();
        metadato.setCampo("ParticularesAsiento");
        metadato.setValor("ASIENTO");
        metadatosGenerales = new HashSet<>();
        metadatosGenerales.add(metadato);

        asientoBean.setOtrosMetadatosParticulares(metadatosGenerales);

        Set<InteresadoBean> interesados = new LinkedHashSet<>();


        //InteresadoBean datos cogidos de (SIR-RC-PR-261) //Persona Física
        InteresadoBean interesadoBean = new InteresadoBean();
        interesadoBean.setTipoPersonaInteresado("1");
        interesadoBean.setTipoDocumentoIdentificacionInteresado("N");
        interesadoBean.setDocumentoIdentificacionInteresado("84925334L");
        interesadoBean.setNombreInteresado("Prueba Solo Obligatorios");
        interesadoBean.setPrimerApellidoInteresado("Obligatorios");
        interesadoBean.setSegundoApellidoInteresado("Cacerez");
        interesadoBean.setPaisInteresado("724");
        interesadoBean.setProvinciaInteresado("28");
        interesadoBean.setMunicipioInteresado("0164");
        interesadoBean.setDireccionInteresado("Plaza de Les Lletres, 2");
        interesadoBean.setCodPostalInteresado("28010");
        interesadoBean.setCorreoElectronicoInteresado("correointeresado@example.org");
        interesadoBean.setTelefonoMovilInteresado("11111111111111111111");
        interesadoBean.setCanalPreferenteComunicacionInteresado("1");
        interesadoBean.setSolicitaNotificacionSMSInteresado(true);
        interesadoBean.setSolicitaNotificacionEmailInteresado(true);
        interesadoBean.setReceptorNotificacionInteresado(true);

        //Parte del representante(SIR-RC-PR-262)
        interesadoBean.setTipoPersonaRepresentante("2");
        interesadoBean.setTipoDocumentoIdentificacionRepresentante("C");
        interesadoBean.setTipoDocumentoIdentificacionRepresentante("A15022510");
        interesadoBean.setRazonSocialRepresentante("Abogados TTS");
        interesadoBean.setPaisRepresentante("724");
        interesadoBean.setProvinciaRepresentante("28");
        interesadoBean.setMunicipioRepresentante("0164");
        interesadoBean.setDireccionRepresentante("Paseo de la castellana, 24");
        interesadoBean.setCorreoElectronicoRepresentante("notifica@abogadotss.es");
        interesadoBean.setTelefonoFijoRepresentante("11111111111111111111");
        interesadoBean.setReceptorNotificacionRepresentante(false);
        interesadoBean.setCanalPreferenteComunicacionRepresentante("1");
        interesadoBean.setSolicitaNotificacionSMSRepresentante(false);
        interesadoBean.setSolicitaNotificacionEmailRepresentante(true);

        interesados.add(interesadoBean);

        asientoBean.getInteresadosBean().add(interesadoBean);

        // SIR-RC-PR-261
        AnexoBean anexoBean = new AnexoBean();
        anexoBean.setNombreFichero("JustificanteSIR_32257555.xml");
        anexoBean.setIdentificadorFichero("O00002721_22_90000262_01_0001.xml");
        anexoBean.setTipoAnexo("01");
        anexoBean.setResumen("TODOS LOS CAMPOS");
        anexoBean.setCodigoFormulario("CODIGO FORMULARIO 1");
        anexoBean.setObservaciones("Observaciones del anexo");
        //anexoBean.setTamanioFichero("");
        ContenidoBean contenidoBean = new ContenidoBean();
        contenidoBean.setContenido(Base64.getDecoder().decode("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pg0KPFJlZmVyZW5jaWFEb2N1bWVudG8+DQogICAgPElkZW50aWZpY2Fkb3I+DQogICAgICAgIDxlRU1HREUuRmlybWEuRm9ybWF0b0Zpcm1hLlZhbG9yQ1NWPkNTVl9hYmNkZWZnaGlqa2xtbjwvZUVNR0RFLkZpcm1hLkZvcm1hdG9GaXJtYS5WYWxvckNTVj4NCiAgICAgICAgPGVFTUdERS5JZGVudGlmaWNhZG9yLlNlY3VlbmNpYUlkZW50aWZpY2Fkb3I+SURfQ1NWX0VqZW1wbG8xPC9lRU1HREUuSWRlbnRpZmljYWRvci5TZWN1ZW5jaWFJZGVudGlmaWNhZG9yPg0KICAgIDwvSWRlbnRpZmljYWRvcj4NCiAgICA8UGVybWlzbz4NCiAgICAgICAgPFB1YmxpY28+UFVCTElDTzwvUHVibGljbz4NCiAgICA8L1Blcm1pc28+DQogICAgPERpcmVjY2lvbj5odHRwczovL3NlLWNzdnN0b3JhZ2UucmVkc2FyYS5lcy9jc3ZzdG9yYWdlL3NlcnZpY2VzL0NTVlF1ZXJ5RG9jdW1lbnRNdG9tU2VydmljZT93c2RsPC9EaXJlY2Npb24+DQo8L1JlZmVyZW5jaWFEb2N1bWVudG8+"));
        contenidoBean.setNombreFormato("application/xml");

        anexoBean.setContenidoBean(contenidoBean);
        anexoBean.setUrlRepositorio("");

        metadato = new OtrosMetadatos();
        metadato.setCampo("Hash");
        metadato.setValor("WXpSaFpUbGpNREF3TXpNeU5XVTRPR1JsTTJJM09UaGpaakJqWXpZd1pUZzRZMk01TURVNU9UaGpNMkprWVRVME1URmhObVkyTURVME9HTXlZbU00T1dFMk1qVmhNR014TW1RMVpEaG1OV1l5TmpRMU1UZzBOREkzTnpNek4yRTVORFpqTVRrNE16QmlPV0l3T1dVd01qZzRNR1F3TTJOaFpXWmxNbVUwWTJVPQ==");
        metadatosGenerales = new HashSet<>();
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("AlgoritmoHash");
        metadato.setValor("SHA256");
        metadatosGenerales.add(metadato);


        metadato = new OtrosMetadatos();
        metadato.setCampo("Resolucion");
        metadato.setValor("200 ppp");
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("Tamanio");
        metadato.setValor("1,56 MB");
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("Idioma");
        metadato.setValor("español");
        metadatosGenerales.add(metadato);


        anexoBean.setOtrosMetadatosGenerales(metadatosGenerales);
        anexoBean.setOtrosMetadatosParticulares(new HashSet<>());

        asientoBean.getAnexosBean().add(anexoBean);

        //Anexo 2 SIR-RC-PR-262
        anexoBean = new AnexoBean();
        anexoBean.setNombreFichero("FicheroPades.xml");
        anexoBean.setIdentificadorFichero("O00002721_22_90000262_01_0002.xml");
        anexoBean.setTipoAnexo("02");
        anexoBean.setResumen("TODOS LOS CAMPOS");
        anexoBean.setCodigoFormulario("CODIGO FORMULARIO 1");
        anexoBean.setObservaciones("Observaciones del anexo");
        //anexoBean.setTamanioFichero("");
        contenidoBean = new ContenidoBean();
        contenidoBean.setContenido(Base64.getDecoder().decode("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pg0KDQoNCiAgICAgICAgPGVFTUdERS5JZGVudGlmaWNhZG9yLlNlY3VlbmNpYUlkZW50aWZpY2Fkb3I+RVNfRUEwMDAwMDAwXzIwMTlfMDAwMDAwMDAwMDM8L2VFTUdERS5JZGVudGlmaWNhZG9yLlNlY3VlbmNpYUlkZW50aWZpY2Fkb3I+DQogICAgPC9JZGVudGlmaWNhZG9yPg0KICAgIDxQZXJtaXNvPg0KICAgICAgICA8UHVibGljbz5QVUJMSUNPPC9QdWJsaWNvPg0KICAgIDwvUGVybWlzbz4NCiAgICA8RGlyZWNjaW9uPmh0dHBzOi8vc2UtY3N2c3RvcmFnZS5yZWRzYXJhLmVzL2NzdnN0b3JhZ2Uvc2VydmljZXMvQ1NWUXVlcnlEb2N1bWVudFNlcnZpY2U/d3NkbDwvRGlyZWNjaW9uPg0KPC9SZWZlcmVuY2lhRG9jdW1lbnRvPg=="));
        contenidoBean.setNombreFormato("application/xml");

        anexoBean.setContenidoBean(contenidoBean);
        anexoBean.setUrlRepositorio("");

        metadato = new OtrosMetadatos();
        metadato.setCampo("Hash");
        metadato.setValor("Tb8+k+/ungNpyamAyAxdPk5fSeSp4HoPxmRxFQKqmcifXvGfY/Ve9Jfgd7AMEpboV4dqT3mHtrIH/bYGN5oI3g==");
        metadatosGenerales = new HashSet<>();
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("AlgoritmoHash");
        metadato.setValor("SHA256");
        metadatosGenerales.add(metadato);


        metadato = new OtrosMetadatos();
        metadato.setCampo("Resolucion");
        metadato.setValor("200 ppp");
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("Tamanio");
        metadato.setValor("1,56 MB");
        metadatosGenerales.add(metadato);

        metadato = new OtrosMetadatos();
        metadato.setCampo("Idioma");
        metadato.setValor("español");
        metadatosGenerales.add(metadato);


        anexoBean.setOtrosMetadatosGenerales(metadatosGenerales);

        metadato = new OtrosMetadatos();
        metadato.setCampo("ParticularesAnexo");
        metadato.setValor("ANEXO");
        metadatosParticulares.add(metadato);


        anexoBean.setOtrosMetadatosParticulares(metadatosParticulares);

        TipoMetadatos tipoMetadatos = new TipoMetadatos();
        tipoMetadatos.setOrigenCiudadanoAdministracion(false);

        XMLGregorianCalendar xmlDate = null;

        // Gregorian Calendar object creation
        GregorianCalendar gc = new GregorianCalendar();

        // giving current date and time to gc
        gc.setTime(new Date());
        xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gc);
        tipoMetadatos.setFechaCaptura(xmlDate);

        tipoMetadatos.setTipoDocumental(TipoDocumental.TD_01);

        anexoBean.setTipoMetadatos(tipoMetadatos);

        asientoBean.getAnexosBean().add(anexoBean);





        RegistroSir registroSir = registroSirEjb.crearRegistroSir(asientoBean,entidad);



        return "redirect:/registroSir/"+registroSir.getId()+"/detalle";


    }*/


    /**
     * compone los anexos sir recibidos en una lista de anexosSirFull
     *
     * @param anexos
     * @return
     * @throws Exception
     */
    private List<AnexoSirFull> componerAnexoSirFull(List<AnexoSir> anexos) throws Exception {

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


