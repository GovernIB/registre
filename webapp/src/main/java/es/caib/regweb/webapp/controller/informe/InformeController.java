package es.caib.regweb.webapp.controller.informe;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.editor.LibroEditor;
import es.caib.regweb.webapp.form.InformeIndicadoresBusquedaForm;
import es.caib.regweb.webapp.form.InformeLibroBusquedaForm;
import es.caib.regweb.webapp.form.RegistroLopdBusquedaForm;
import es.caib.regweb.webapp.form.UsuarioLopdBusquedaForm;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
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
 * Controller que gestiona los informes y estadísticas
 * @author jpernia
 * Date: 2/05/14
 */
@Controller
@RequestMapping(value = "/informe")

public class InformeController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;
    
    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;
    
    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb/RegistroLopdMigradoEJB/local")
    public RegistroLopdMigradoLocal registroLopdMigradoEjb;



    /**
     * Listado de registros
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/libroRegistro", method = RequestMethod.GET)
    public String libroRegistro(Model model, HttpServletRequest request)throws Exception {

        InformeLibroBusquedaForm informeLibroBusquedaForm = new InformeLibroBusquedaForm();
        informeLibroBusquedaForm.setFechaFin(new Date());
        model.addAttribute("informeLibroBusquedaForm",informeLibroBusquedaForm);
        model.addAttribute("libros", libros(request));

        return "informe/libroRegistro";
    }

    /**
     * Realiza la busqueda de registros según los parametros del formulario
     */
    @RequestMapping(value = "/libroRegistro", method = RequestMethod.POST)
    public ModelAndView libroRegistro(@ModelAttribute InformeLibroBusquedaForm informeLibroBusquedaForm, HttpServletRequest request)throws Exception {

        ModelAndView mav = null;

        if(informeLibroBusquedaForm.getFormato().equals("pdf")){
            mav = new ModelAndView("libroRegistroPdf");
        }else if(informeLibroBusquedaForm.getFormato().equals("excel")){
            mav = new ModelAndView("libroRegistroExcel");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId());

        Set<String> campos = informeLibroBusquedaForm.getCampos();
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        // Obtener los registros del libro
        ArrayList<ArrayList<String>> registrosLibro = new ArrayList<ArrayList<String>>();

        Date dataFi = RegistroUtils.ajustarHoraBusqueda(informeLibroBusquedaForm.getFechaFin());

        // REGISTROS DE ENTRADA
        if(informeLibroBusquedaForm.getTipo().equals(RegwebConstantes.REGISTRO_ENTRADA)){

            List<RegistroEntrada> registrosEntrada = registroEntradaEjb.buscaLibroRegistro(informeLibroBusquedaForm.getFechaInicio(), dataFi, informeLibroBusquedaForm.getLibros());

            for (int i = 0; i < registrosEntrada.size(); i++) {
                registrosLibro.add(new ArrayList<String>());
                RegistroEntrada registroEntrada = registrosEntrada.get(i);

                for( Iterator<String> it = campos.iterator(); it.hasNext();) {
                    String valorCamp = it.next();
                    if(valorCamp.equals("codAs")){
                        if(registroEntrada.getRegistroDetalle().getCodigoAsunto() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getCodigoAsunto().getCodigo());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("anyRe")){
                        if(registroEntrada.getFecha() != null){
                            String anoRegistro = formatYear.format(registroEntrada.getFecha());
                            registrosLibro.get(i).add(anoRegistro);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("estat")){
                        registrosLibro.get(i).add(I18NUtils.tradueix("registro.estado." +  registroEntrada.getEstado()));
                    } else if(valorCamp.equals("exped")){
                        if(registroEntrada.getRegistroDetalle().getExpediente() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getExpediente());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("extra")){
                        if(registroEntrada.getRegistroDetalle().getExtracto() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getExtracto());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("data")){
                        if(registroEntrada.getFecha() != null){
                            String fechaRegistro = formatDate.format(registroEntrada.getFecha());
                            registrosLibro.get(i).add(fechaRegistro);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("numRe")){
                        if(registroEntrada.getNumeroRegistro() != null){
                            registrosLibro.get(i).add(registroEntrada.getNumeroRegistro().toString());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("ofici")){
                        if(registroEntrada.getOficina().getDenominacion() != null){
                            registrosLibro.get(i).add(registroEntrada.getOficina().getDenominacion());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("tipAs")){
                        if(registroEntrada.getRegistroDetalle().getTipoAsunto() != null){
                            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroEntrada.getRegistroDetalle().getTipoAsunto().getTraduccion();
                            registrosLibro.get(i).add(traduccionTipoAsunto.getNombre());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("obser")){
                        if(registroEntrada.getRegistroDetalle().getObservaciones() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getObservaciones());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("llibr")){
                        if(registroEntrada.getLibro().getNombre() != null){
                            registrosLibro.get(i).add(registroEntrada.getLibro().getNombre());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("docFi")){
                        if (registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica() != null){
                            registrosLibro.get(i).add(I18NUtils.tradueix("tipoDocumentacionFisica." + registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica()));
                        } else {
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("orgDe")){
                        if(registroEntrada.getDestinoExternoCodigo() != null){
                            registrosLibro.get(i).add(registroEntrada.getDestinoExternoCodigo());
                        }else{
                            if(registroEntrada.getDestino() != null){
                                registrosLibro.get(i).add(registroEntrada.getDestino().getDenominacion());
                            }else{
                                registrosLibro.get(i).add("");
                            }
                        }
                    } else if(valorCamp.equals("idiom")){
                        if(registroEntrada.getRegistroDetalle().getIdioma() != null) {
                            final String nombre = I18NUtils.tradueix("idioma." + registroEntrada.getRegistroDetalle().getIdioma());
                            registrosLibro.get(i).add(nombre);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("refEx")){
                        if(registroEntrada.getRegistroDetalle().getReferenciaExterna() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getReferenciaExterna());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("trans")){
                        if(registroEntrada.getRegistroDetalle().getTransporte() != null){
                            registrosLibro.get(i).add( I18NUtils.tradueix("transporte." + registroEntrada.getRegistroDetalle().getTransporte()));
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("numTr")){
                        if(registroEntrada.getRegistroDetalle().getNumeroTransporte() != null){
                          final String nombre = I18NUtils.tradueix("idioma." +registroEntrada.getRegistroDetalle().getIdioma()); 
                            registrosLibro.get(i).add(nombre);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("orgOr")){
                        if(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                        }else{
                            if(registroEntrada.getRegistroDetalle().getOficinaOrigen() != null){
                                registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                            }else{
                                registrosLibro.get(i).add("");
                            }
                        }
                    } else if(valorCamp.equals("numOr")){
                        if(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("datOr")){
                        if(registroEntrada.getRegistroDetalle().getFechaOrigen() != null){
                            registrosLibro.get(i).add(registroEntrada.getRegistroDetalle().getFechaOrigen().toString());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("nomIn")){
                        if(registroEntrada.getRegistroDetalle().getInteresados() != null){
                            String interessats = "";

                            for(int k=0;k<registroEntrada.getRegistroDetalle().getInteresados().size();k++) {
                                Interesado interesado = registroEntrada.getRegistroDetalle().getInteresados().get(k);
                                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){
                                    interessats = interessats + interesado.getNombre();
                                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                                        interessats = interessats + interesado.getNombrePersonaFisica();
                                    } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                                            interessats = interessats + interesado.getNombrePersonaJuridica();
                                        }
                                if(k<registroEntrada.getRegistroDetalle().getInteresados().size()-1){
                                    interessats = interessats + ", ";
                                }
                            }
                            registrosLibro.get(i).add(interessats);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    }
                }
            }

            // Alta en tabla LOPD de los registros del Informe
            Paginacion paginacionEntrada = new Paginacion(0, 0);
            List<Object> entradasList = new ArrayList<Object>(registrosEntrada);
            paginacionEntrada.setListado(entradasList);
            lopdEjb.insertarRegistrosEntrada(paginacionEntrada, usuarioEntidad.getId());

            mav.addObject("tipo", "Entrada");


        // REGISTROS DE SALIDA
        }else if(informeLibroBusquedaForm.getTipo().equals(RegwebConstantes.REGISTRO_SALIDA)){

            List<RegistroSalida> registrosSalida = registroSalidaEjb.buscaLibroRegistro(informeLibroBusquedaForm.getFechaInicio(), dataFi, informeLibroBusquedaForm.getLibros());

            for (int i = 0; i < registrosSalida.size(); i++) {
                registrosLibro.add(new ArrayList<String>());
                RegistroSalida registroSalida = registrosSalida.get(i);

                for( Iterator<String> it = campos.iterator(); it.hasNext();) {
                    String valorCamp = (String) it.next();
                    if(valorCamp.equals("codAs")){
                        if(registroSalida.getRegistroDetalle().getCodigoAsunto() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getCodigoAsunto().getCodigo());
                        } else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("anyRe")){
                        if(registroSalida.getFecha() != null){
                            String anoRegistro = formatYear.format(registroSalida.getFecha());
                            registrosLibro.get(i).add(anoRegistro);
                        } else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("estat")){
                        if(registroSalida.getEstado() != null){
                            registrosLibro.get(i).add(I18NUtils.tradueix("registro.estado." + registroSalida.getEstado() ));
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("exped")){
                        if(registroSalida.getRegistroDetalle().getExpediente() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getExpediente());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("extra")){
                        if(registroSalida.getRegistroDetalle().getExtracto() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getExtracto());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("data")){
                        if(registroSalida.getFecha() != null){
                            registrosLibro.get(i).add(registroSalida.getFecha().toString());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("numRe")){
                        if(registroSalida.getNumeroRegistro() != null){
                            registrosLibro.get(i).add(registroSalida.getNumeroRegistro().toString());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("ofici")){
                        if(registroSalida.getOficina().getDenominacion() != null){
                            registrosLibro.get(i).add(registroSalida.getOficina().getDenominacion());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("tipAs")){
                        if(registroSalida.getRegistroDetalle().getTipoAsunto() != null){
                            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroSalida.getRegistroDetalle().getTipoAsunto().getTraduccion();
                            registrosLibro.get(i).add(traduccionTipoAsunto.getNombre());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("obser")){
                        if(registroSalida.getRegistroDetalle().getObservaciones() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getObservaciones());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("llibr")){
                        if(registroSalida.getLibro().getNombre() != null){
                            registrosLibro.get(i).add(registroSalida.getLibro().getNombre());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("docFi")){
                        if(registroSalida.getRegistroDetalle().getTipoDocumentacionFisica() != null){
                            
                            registrosLibro.get(i).add(I18NUtils.tradueix("tipoDocumentacionFisica." + registroSalida.getRegistroDetalle().getTipoDocumentacionFisica()));
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("orgDe")){
                        if(registroSalida.getOrigen() != null){
                            registrosLibro.get(i).add(registroSalida.getOrigen().getDenominacion());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("idiom")){
                        if(registroSalida.getRegistroDetalle().getIdioma() != null){
                          final String nombre = I18NUtils.tradueix("idioma." + registroSalida.getRegistroDetalle().getIdioma()); 
                            registrosLibro.get(i).add(nombre);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("refEx")){
                        if(registroSalida.getRegistroDetalle().getReferenciaExterna() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getReferenciaExterna());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("trans")){
                        if(registroSalida.getRegistroDetalle().getTransporte() != null){
                            registrosLibro.get(i).add(I18NUtils.tradueix("transporte." + registroSalida.getRegistroDetalle().getTransporte()));
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("numTr")){
                        if(registroSalida.getRegistroDetalle().getNumeroTransporte() != null){
                          final String nombre = I18NUtils.tradueix("idioma." + registroSalida.getRegistroDetalle().getIdioma()); 
                            registrosLibro.get(i).add(nombre);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("orgOr")){
                        if(registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                        }else{
                            if(registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null){
                                registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                            }else{
                                registrosLibro.get(i).add("");
                            }
                        }
                    } else if(valorCamp.equals("numOr")){
                        if(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("datOr")){
                        if(registroSalida.getRegistroDetalle().getFechaOrigen() != null){
                            registrosLibro.get(i).add(registroSalida.getRegistroDetalle().getFechaOrigen().toString());
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    } else if(valorCamp.equals("nomIn")){
                        if(registroSalida.getRegistroDetalle().getInteresados() != null){
                            String interessats = null;

                            for(int k=0;k<registroSalida.getRegistroDetalle().getInteresados().size();k++){
                                Interesado interesado = registroSalida.getRegistroDetalle().getInteresados().get(k);
                                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){
                                    interessats = interessats + interesado.getNombre();
                                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                                        interessats = interessats + registroSalida.getRegistroDetalle().getInteresados().get(k).getNombrePersonaFisica();
                                    } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                                            interessats = interessats + interesado.getNombrePersonaJuridica();
                                        }
                                if(i<registroSalida.getRegistroDetalle().getInteresados().size()-1){
                                    interessats = interessats + ", ";
                                }
                            }
                            registrosLibro.get(i).add(interessats);
                        }else{
                            registrosLibro.get(i).add("");
                        }
                    }
                }
            }

            // Alta en tabla LOPD de los registros del Informe
            Paginacion paginacionSalida = new Paginacion(0, 0);
            List<Object> salidasList = new ArrayList<Object>(registrosSalida);
            paginacionSalida.setListado(salidasList);
            lopdEjb.insertarRegistrosSalida(paginacionSalida, usuarioEntidad.getId());

            mav.addObject("tipo", "Sortida");
        }

        if(informeLibroBusquedaForm.getFechaInicio() != null){
            String fechaInicio = formatDate.format(informeLibroBusquedaForm.getFechaInicio());
            mav.addObject("fechaInicio", fechaInicio);
        }

        if(informeLibroBusquedaForm.getFechaInicio() != null){
            String fechaFin = formatDate.format(informeLibroBusquedaForm.getFechaFin());
            mav.addObject("fechaFin", fechaFin);
        }

        mav.addObject("campos", campos);
        mav.addObject("registrosLibro", registrosLibro);


        return mav;
    }


    /**
     * Informe de Indicadores
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/indicadores", method = RequestMethod.GET)
    public String indicadores(Model model, HttpServletRequest request)throws Exception {

        InformeIndicadoresBusquedaForm informeIndicadoresBusquedaForm = new InformeIndicadoresBusquedaForm();
        informeIndicadoresBusquedaForm.setFechaFin(new Date());
        model.addAttribute("informeIndicadoresBusquedaForm",informeIndicadoresBusquedaForm);

        return "informe/indicadores";
    }

    /**
     * Realiza la busqueda de registros según los parametros del formulario
     */
    @RequestMapping(value = "/indicadores", method = RequestMethod.POST)
    public ModelAndView indicadores(@ModelAttribute InformeIndicadoresBusquedaForm informeIndicadoresBusquedaForm, HttpServletRequest request)throws Exception {

        ModelAndView mav = null;

        if(informeIndicadoresBusquedaForm.getFormato().equals("pdf")){
            mav = new ModelAndView("indicadoresPdf");
        }else if(informeIndicadoresBusquedaForm.getFormato().equals("excel")){
            mav = new ModelAndView("indicadoresExcel");
        }

        //Long mostrarCamps = informeIndicadoresBusquedaForm.getCampoCalendario();
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMMM", new Locale("ca"));
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        String fechaInicio = "";
        String fechaFin = "";

        if(informeIndicadoresBusquedaForm.getFechaInicio() != null){
            fechaInicio = formatDate.format(informeIndicadoresBusquedaForm.getFechaInicio());
            mav.addObject("fechaInicio", fechaInicio);
        }

        if(informeIndicadoresBusquedaForm.getFechaFin() != null){
            fechaFin = formatDate.format(informeIndicadoresBusquedaForm.getFechaFin());
            mav.addObject("fechaFin", fechaFin);
        }

        // Obtener los registros del libro
        ArrayList<ArrayList<String>> registros = new ArrayList<ArrayList<String>>();

        Date dataFi = RegistroUtils.ajustarHoraBusqueda(informeIndicadoresBusquedaForm.getFechaFin());
        Date dataInici = informeIndicadoresBusquedaForm.getFechaInicio();

        // Busca los registros Totales de Entrada y Salida entre las fechas
        Entidad entidadActiva = getEntidadActiva(request);
        List<RegistroEntrada> registrosEntrada = registroEntradaEjb.buscaIndicadores(dataInici, dataFi, entidadActiva.getId());
        List<RegistroSalida> registrosSalida = registroSalidaEjb.buscaIndicadores(dataInici, dataFi, entidadActiva.getId());


        // Busca los registros por Años de Entrada y Salida entre las fechas
        List<String> entradaAnosValor = new ArrayList<String>();
        List<String> entradaAnosNombre = new ArrayList<String>();
        List<String> salidaAnosValor = new ArrayList<String>();
        List<String> salidaAnosNombre = new ArrayList<String>();
        List<RegistroEntrada> entrades = new ArrayList<RegistroEntrada>();
        List<RegistroSalida> sortides = new ArrayList<RegistroSalida>();
        Date inicio = informeIndicadoresBusquedaForm.getFechaInicio();
        while (inicio.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(inicio);
            entradaAnosNombre.add(anyActual);
            salidaAnosNombre.add(anyActual);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            if(cal.getTime().compareTo(dataFi) < 0){
                entrades = registroEntradaEjb.buscaIndicadores(inicio, cal.getTime(), entidadActiva.getId());
                sortides = registroSalidaEjb.buscaIndicadores(inicio, cal.getTime(), entidadActiva.getId());
                entradaAnosValor.add(String.valueOf(entrades.size()));
                salidaAnosValor.add(String.valueOf(sortides.size()));
            }else{
                entrades = registroEntradaEjb.buscaIndicadores(inicio, dataFi, entidadActiva.getId());
                sortides = registroSalidaEjb.buscaIndicadores(inicio, dataFi, entidadActiva.getId());
                entradaAnosValor.add(String.valueOf(entrades.size()));
                salidaAnosValor.add(String.valueOf(sortides.size()));
                break;
            }
            cal.add(Calendar.DATE, 1);
            inicio = cal.getTime();
        }
        mav.addObject("entradaAnosValor", entradaAnosValor);
        mav.addObject("entradaAnosNombre", entradaAnosNombre);
        mav.addObject("salidaAnosValor", salidaAnosValor);
        mav.addObject("salidaAnosNombre", salidaAnosNombre);


        // Busca los registros por Meses de Entrada y Salida entre las fechas
        List<String> entradaMesesValor = new ArrayList<String>();
        List<String> entradaMesesNombre = new ArrayList<String>();
        List<String> salidaMesesValor = new ArrayList<String>();
        List<String> salidaMesesNombre = new ArrayList<String>();
        List<RegistroEntrada> entradesMesos = new ArrayList<RegistroEntrada>();
        List<RegistroSalida> sortidesMesos = new ArrayList<RegistroSalida>();
        inicio = dataInici;
        while (inicio.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(inicio);
            String mesActualNom = formatMes.format(inicio);
            String mesActual = formatMonth.format(inicio);
            String mesActualCompost = mesActualNom + "/" + anyActual;
            entradaMesesNombre.add(mesActualCompost);
            salidaMesesNombre.add(mesActualCompost);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Integer.parseInt(mesActual)-1);
            int mesAra = Integer.parseInt(mesActual);
            int anyAra = Integer.parseInt(anyActual);
            if((mesAra == 1)|| (mesAra == 3)|| (mesAra == 5)|| (mesAra == 7)|| (mesAra == 8)|| (mesAra == 10)|| (mesAra == 12)){
                cal.set(Calendar.DAY_OF_MONTH, 31);
            } else if((mesAra == 4)|| (mesAra == 6)|| (mesAra == 9)|| (mesAra == 11)){
                cal.set(Calendar.DAY_OF_MONTH, 30);
            } else if((anyAra % 4 == 0) && ((anyAra % 100 != 0) || (anyAra % 400 == 0))){
                cal.set(Calendar.DAY_OF_MONTH, 29);
            } else{
                cal.set(Calendar.DAY_OF_MONTH, 28);
            }
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            if(cal.getTime().compareTo(dataFi) < 0){
                entradesMesos = registroEntradaEjb.buscaIndicadores(inicio, cal.getTime(), entidadActiva.getId());
                sortidesMesos = registroSalidaEjb.buscaIndicadores(inicio, cal.getTime(), entidadActiva.getId());
                entradaMesesValor.add(String.valueOf(entradesMesos.size()));
                salidaMesesValor.add(String.valueOf(sortidesMesos.size()));
            }else{
                entradesMesos = registroEntradaEjb.buscaIndicadores(inicio, dataFi, entidadActiva.getId());
                sortidesMesos = registroSalidaEjb.buscaIndicadores(inicio, dataFi, entidadActiva.getId());
                entradaMesesValor.add(String.valueOf(entradesMesos.size()));
                salidaMesesValor.add(String.valueOf(sortidesMesos.size()));
                break;
            }
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);
            inicio = cal.getTime();
        }
        mav.addObject("entradaMesesValor", entradaMesesValor);
        mav.addObject("entradaMesesNombre", entradaMesesNombre);
        mav.addObject("salidaMesesValor", salidaMesesValor);
        mav.addObject("salidaMesesNombre", salidaMesesNombre);


        // Busca los registros por Conselleria de Entrada y Salida entre las fechas
        List<String> entradaConselleriaValor = new ArrayList<String>();
        List<String> entradaConselleriaNombre = new ArrayList<String>();
        List<String> salidaConselleriaValor = new ArrayList<String>();
        List<String> salidaConselleriaNombre = new ArrayList<String>();
        List<Organismo> organismos = getEntidadActiva(request).getOrganismos();
        for(int i=0; i<organismos.size(); i++){
            Organismo organismo = organismos.get(i);
            entradaConselleriaNombre.add(organismo.getDenominacion());
            salidaConselleriaNombre.add(organismo.getDenominacion());
            entradaConselleriaValor.add(String.valueOf(registroEntradaEjb.buscaEntradaPorConselleria(dataInici, dataFi, organismo.getId())));
            salidaConselleriaValor.add(String.valueOf(registroSalidaEjb.buscaSalidaPorConselleria(dataInici, dataFi, organismo.getId())));
        }
        mav.addObject("entradaConselleriaValor", entradaConselleriaValor);
        mav.addObject("entradaConselleriaNombre", entradaConselleriaNombre);
        mav.addObject("salidaConselleriaValor", salidaConselleriaValor);
        mav.addObject("salidaConselleriaNombre", salidaConselleriaNombre);


        // Busca los registros por Tipos de Asunto de Entrada y Salida entre las fechas
        List<String> entradaAsuntoValor = new ArrayList<String>();
        List<String> entradaAsuntoNombre = new ArrayList<String>();
        List<String> salidaAsuntoValor = new ArrayList<String>();
        List<String> salidaAsuntoNombre = new ArrayList<String>();
        List<TipoAsunto> tiposAsunto = tipoAsuntoEjb.getAll();
        for(int i=0; i<tiposAsunto.size(); i++){
            TipoAsunto tipoAsunto = tiposAsunto.get(i);
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) tipoAsunto.getTraduccion();
            entradaAsuntoNombre.add(traduccionTipoAsunto.getNombre());
            salidaAsuntoNombre.add(traduccionTipoAsunto.getNombre());
            entradaAsuntoValor.add(String.valueOf(registroEntradaEjb.buscaEntradaPorAsunto(dataInici, dataFi, tipoAsunto.getId(), entidadActiva.getId())));
            salidaAsuntoValor.add(String.valueOf(registroSalidaEjb.buscaSalidaPorAsunto(dataInici, dataFi, tipoAsunto.getId(), entidadActiva.getId())));
        }
        mav.addObject("entradaAsuntoValor", entradaAsuntoValor);
        mav.addObject("entradaAsuntoNombre", entradaAsuntoNombre);
        mav.addObject("salidaAsuntoValor", salidaAsuntoValor);
        mav.addObject("salidaAsuntoNombre", salidaAsuntoNombre);


        // Busca los registros por Libro de Registro de Entrada y Salida entre las fechas
        List<String> entradaLibroValor = new ArrayList<String>();
        List<String> entradaLibroNombre = new ArrayList<String>();
        List<String> salidaLibroValor = new ArrayList<String>();
        List<String> salidaLibroNombre = new ArrayList<String>();
        for(int i=0; i<organismos.size(); i++){
            Organismo organismo = organismos.get(i);
            List<Libro> libros = organismo.getLibros();
            for(int j=0; j<libros.size(); j++){
                Libro libro = libros.get(j);
                entradaLibroNombre.add(libro.getNombre());
                salidaLibroNombre.add(libro.getNombre());
                entradaLibroValor.add(String.valueOf(registroEntradaEjb.buscaEntradaPorLibro(dataInici, dataFi, libro.getId())));
                salidaLibroValor.add(String.valueOf(registroSalidaEjb.buscaSalidaPorLibro(dataInici, dataFi, libro.getId())));
            }
        }
        mav.addObject("entradaLibroValor", entradaLibroValor);
        mav.addObject("entradaLibroNombre", entradaLibroNombre);
        mav.addObject("salidaLibroValor", salidaLibroValor);
        mav.addObject("salidaLibroNombre", salidaLibroNombre);


        // Busca los registros por Oficina de Registro de Entrada y Salida entre las fechas
        List<String> entradaOficinaValor = new ArrayList<String>();
        List<String> entradaOficinaNombre = new ArrayList<String>();
        List<String> salidaOficinaValor = new ArrayList<String>();
        List<String> salidaOficinaNombre = new ArrayList<String>();
        Entidad entitat = getEntidadActiva(request);
        List<Oficina> oficinas = oficinaEjb.findByEntidad(entitat.getId());
        for(int i=0; i<oficinas.size(); i++){
            Oficina oficina = oficinas.get(i);
            entradaOficinaNombre.add(oficina.getDenominacion());
            salidaOficinaNombre.add(oficina.getDenominacion());
            entradaOficinaValor.add(String.valueOf(registroEntradaEjb.buscaEntradaPorOficina(dataInici, dataFi, oficina.getId())));
            salidaOficinaValor.add(String.valueOf(registroSalidaEjb.buscaSalidaPorOficina(dataInici, dataFi, oficina.getId())));
        }
        mav.addObject("entradaOficinaValor", entradaOficinaValor);
        mav.addObject("entradaOficinaNombre", entradaOficinaNombre);
        mav.addObject("salidaOficinaValor", salidaOficinaValor);
        mav.addObject("salidaOficinaNombre", salidaOficinaNombre);


        // Busca los registros por Idiomas de Entrada y Salida entre las fechas
        List<String> entradaIdiomaValor = new ArrayList<String>();
        List<String> entradaIdiomaNombre = new ArrayList<String>();
        List<String> salidaIdiomaValor = new ArrayList<String>();
        List<String> salidaIdiomaNombre = new ArrayList<String>();
        
        for(Long idioma : RegwebConstantes.IDIOMAS_REGISTRO){
            final String nombre = I18NUtils.tradueix("idioma." + idioma);
            entradaIdiomaNombre.add(nombre);
            salidaIdiomaNombre.add(nombre);
            entradaIdiomaValor.add(String.valueOf(registroEntradaEjb.buscaEntradaPorIdioma(dataInici, dataFi, idioma, entidadActiva.getId())));
            salidaIdiomaValor.add(String.valueOf(registroSalidaEjb.buscaSalidaPorIdioma(dataInici, dataFi, idioma, entidadActiva.getId())));
        }
        mav.addObject("entradaIdiomaValor", entradaIdiomaValor);
        mav.addObject("entradaIdiomaNombre", entradaIdiomaNombre);
        mav.addObject("salidaIdiomaValor", salidaIdiomaValor);
        mav.addObject("salidaIdiomaNombre", salidaIdiomaNombre);


        // Registros de Entrada y Salida
        if(informeIndicadoresBusquedaForm.getTipo() == 0){
            for (int i = 0; i < registrosEntrada.size(); i++) {
                registros.add(new ArrayList<String>());
            }
            mav.addObject("tipo", "Entrada i Sortida");

            // Registros de Entrada
        }else if(informeIndicadoresBusquedaForm.getTipo() == 1){
            for (int i = 0; i < registrosEntrada.size(); i++) {
                registros.add(new ArrayList<String>());
            }
            mav.addObject("tipo", "Entrada");

            // Registros de Salida
        }else if(informeIndicadoresBusquedaForm.getTipo() == 2){
            for (int i = 0; i < registrosSalida.size(); i++) {
                registros.add(new ArrayList<String>());
            }
            mav.addObject("tipo", "Sortida");
        }

        mav.addObject("campoCalendario", informeIndicadoresBusquedaForm.getCampoCalendario());
        mav.addObject("registrosEntrada", registrosEntrada.size());
        mav.addObject("registrosSalida", registrosSalida.size());

        return mav;
    }

    /**
     * Informe de usuario LOPD
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/usuarioLopd", method = RequestMethod.GET)
    public String usuarioLopd(Model model, HttpServletRequest request)throws Exception {

        UsuarioLopdBusquedaForm usuarioLopdBusquedaForm = new UsuarioLopdBusquedaForm();
        usuarioLopdBusquedaForm.setFechaFin(new Date());

        model.addAttribute("usuarios", usuarios(request));
        model.addAttribute("libros", libros(request));
        model.addAttribute("usuarioLopdBusquedaForm",usuarioLopdBusquedaForm);

        return "informe/usuarioLopd";
    }

    /**
     * Realiza la busqueda de acciones de un usuario sobre registros entre dos fechas
     */
    @RequestMapping(value = "/usuarioLopd", method = RequestMethod.POST)
    public ModelAndView usuarioLopd(@ModelAttribute UsuarioLopdBusquedaForm usuarioLopdBusquedaForm, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("informe/usuarioLopd");

        // Obtener los registros del usuario
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(usuarioLopdBusquedaForm.getFechaFin());
        Date dataInici = usuarioLopdBusquedaForm.getFechaInicio();

        Usuario usuario = usuarioEntidadEjb.findById(usuarioLopdBusquedaForm.getUsuario()).getUsuario();
        mav.addObject("usuario", usuario);

        List<RegistroEntrada> entradasCreadas = null;
        List<RegistroSalida> salidasCreadas = null;
        List<HistoricoRegistroEntrada> entradasModificadas = null;
        List<HistoricoRegistroSalida> salidasModificadas = null;

        // Mira si ha elegido un libro en la búsqueda
        if(usuarioLopdBusquedaForm.getLibro() != -1){

            Libro libro = libroEjb.findById(usuarioLopdBusquedaForm.getLibro());
            mav.addObject("libro", libro);

            // Busca los registros Creados por Usuario de Registro de Entrada y Salida entre las fechas
            entradasCreadas = registroEntradaEjb.buscaEntradaPorUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), usuarioLopdBusquedaForm.getLibro());
            salidasCreadas = registroSalidaEjb.buscaSalidaPorUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), usuarioLopdBusquedaForm.getLibro());

            // Busca los registros Modificador por Usuario de Registro de Entrada y Salida entre las fechas
            entradasModificadas = historicoRegistroEntradaEjb.entradaModificadaPorUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), usuarioLopdBusquedaForm.getLibro());
            salidasModificadas = historicoRegistroSalidaEjb.salidaModificadaPorUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), usuarioLopdBusquedaForm.getLibro());

            // Registros Listados y Consultas
            List<Lopd> consultasEntrada = lopdEjb.getByFechasUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libro.getId(), RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("consultasEntrada", consultasEntrada);

            List<Lopd> listadosEntrada = lopdEjb.getByFechasUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libro.getId(), RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("listadosEntrada", listadosEntrada);

            List<Lopd> consultasSalida = lopdEjb.getByFechasUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libro.getId(), RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("consultasSalida", consultasSalida);

            List<Lopd> listadosSalida = lopdEjb.getByFechasUsuarioLibro(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libro.getId(), RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("listadosSalida", listadosSalida);

        // Si no ha elegido ningún libro en la búsqueda también tendrá en cuenta los Registros Migrados
        } else {

            List<Libro> libros = null;

            // Es operador
            if (isOperador(request)) {
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Obtenemos los Libros de los cuales el Usuario es administrador
                libros = permisoLibroUsuarioEjb.getLibrosAdministrados(usuarioEntidad.getId());
            }

            // Es Administrador de Entidad
            if (isAdminEntidad(request)) {
                libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());
            }

            mav.addObject("libros", libros);

            // Busca los registros Creados por Usuario de Registro de Entrada y Salida entre las fechas
            entradasCreadas = registroEntradaEjb.buscaEntradaPorUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros);
            salidasCreadas = registroSalidaEjb.buscaSalidaPorUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros);

            // Busca los registros Modificador por Usuario de Registro de Entrada y Salida entre las fechas
            entradasModificadas = historicoRegistroEntradaEjb.entradaModificadaPorUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros);
            salidasModificadas = historicoRegistroSalidaEjb.salidaModificadaPorUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros);


            // Registros Listados y Consultas
            List<Lopd> consultasEntrada = lopdEjb.getByFechasUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("consultasEntrada", consultasEntrada);

            List<Lopd> listadosEntrada = lopdEjb.getByFechasUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("listadosEntrada", listadosEntrada);

            List<Lopd> consultasSalida = lopdEjb.getByFechasUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("consultasSalida", consultasSalida);

            List<Lopd> listadosSalida = lopdEjb.getByFechasUsuario(dataInici, dataFi, usuarioLopdBusquedaForm.getUsuario(), libros, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("listadosSalida", listadosSalida);


            // Registros Migrados
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(usuarioLopdBusquedaForm.getUsuario());
            Usuario usuarioBusqueda = usuarioEntidad.getUsuario();
            List<RegistroLopdMigrado> consultasMigrado = registroLopdMigradoEjb.getByUsuario(dataInici, dataFi, usuarioBusqueda.getIdentificador(), RegwebConstantes.LOPDMIGRADO_CONSULTA);
            mav.addObject("consultasMigrado", consultasMigrado);

            List<RegistroLopdMigrado> listadosMigrado = registroLopdMigradoEjb.getByUsuario(dataInici, dataFi, usuarioBusqueda.getIdentificador(), RegwebConstantes.LOPDMIGRADO_LISTADO);
            mav.addObject("listadosMigrado", listadosMigrado);

        }


        mav.addObject("entradasCreadas", entradasCreadas);
        mav.addObject("salidasCreadas", salidasCreadas);
        mav.addObject("entradasModificadas", entradasModificadas);
        mav.addObject("salidasModificadas", salidasModificadas);


        return mav;
    }


    /**
     * Informe de registro LOPD
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroLopd", method = RequestMethod.GET)
    public String registroLopd(Model model, HttpServletRequest request)throws Exception {

        RegistroLopdBusquedaForm registroLopdBusquedaForm = new RegistroLopdBusquedaForm();
        model.addAttribute("libros", libros(request));
        model.addAttribute("registroLopdBusquedaForm",registroLopdBusquedaForm);

        return "informe/registroLopd";
    }

    /**
     * Realiza la busqueda de acciones sobre un registro entre dos fechas
     */
    @RequestMapping(value = "/registroLopd", method = RequestMethod.POST)
    public ModelAndView registroLopd(@ModelAttribute RegistroLopdBusquedaForm registroLopdBusquedaForm, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("informe/registroLopd");

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId());

        // Obtener los registros del usuario
        Date dataFi = registroLopdBusquedaForm.getFechaFin();
        if(dataFi != null){
            dataFi = RegistroUtils.ajustarHoraBusqueda(registroLopdBusquedaForm.getFechaFin());
        }
        Date dataInici = registroLopdBusquedaForm.getFechaInicio();

        Libro libro = libroEjb.findById(registroLopdBusquedaForm.getLibro());
        mav.addObject("libro", libro);

        List<RegistroEntrada> entradas = null;
        List<RegistroSalida> salidas = null;

        // Busca los registros según pasámetros de búsqueda
        if((dataInici != null) && (dataFi != null)) {
            if (registroLopdBusquedaForm.getTipoRegistro().equals(RegwebConstantes.REGISTRO_ENTRADA)) {
                entradas = registroEntradaEjb.buscaPorLibroTipoNumero(dataInici, dataFi, registroLopdBusquedaForm.getLibro(), registroLopdBusquedaForm.getNumeroRegistro());
                // Alta en tabla LOPD de las entradas del listado
                Paginacion paginacionEntrada = new Paginacion(0, 0);
                List<Object> entradasList = new ArrayList<Object>(entradas);
                paginacionEntrada.setListado(entradasList);
                lopdEjb.insertarRegistrosEntrada(paginacionEntrada, usuarioEntidad.getId());
            }
            if (registroLopdBusquedaForm.getTipoRegistro().equals(RegwebConstantes.REGISTRO_SALIDA)) {
                salidas = registroSalidaEjb.buscaPorLibroTipoNumero(dataInici, dataFi, registroLopdBusquedaForm.getLibro(), registroLopdBusquedaForm.getNumeroRegistro());
                // Alta en tabla LOPD de las salidas del listado
                Paginacion paginacionSalida = new Paginacion(0, 0);
                List<Object> salidasList = new ArrayList<Object>(salidas);
                paginacionSalida.setListado(salidasList);
                lopdEjb.insertarRegistrosSalida(paginacionSalida, usuarioEntidad.getId());
            }
        }

        mav.addObject("idTipoRegistro", registroLopdBusquedaForm.getTipoRegistro());
        mav.addObject("entradas", entradas);
        mav.addObject("salidas", salidas);

        return mav;
    }


    /**
     * Realiza el informe Lopd del Registro seleccionado
     */
    @RequestMapping(value = "/{idRegistro}/{idTipoRegistro}/informeRegistroLopd", method = RequestMethod.GET)
    public ModelAndView informeRegistroLopd(@PathVariable Long idRegistro, @PathVariable Long idTipoRegistro)throws Exception {

        ModelAndView mav = new ModelAndView("informe/informeRegistroLopd");

        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Integer numRegistro = null;
        String anyoRegistro = "";
        Libro libro = null;

        if(idTipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)){

            // Añade la información del Registro creado
            RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);
            mav.addObject("registro", registro);

            // Añade la información de las modificaciones del Registro
            List<HistoricoRegistroEntrada> modificaciones = historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro);
            // Eliminamos el primer registro porque es el de creación
            if(modificaciones.size()>0) {
                modificaciones.remove(modificaciones.size()-1);
            }
            mav.addObject("modificaciones", modificaciones);

            // Registros Listados y Consultas
            anyoRegistro = formatYear.format(registro.getFecha());
            libro = registro.getLibro();
            Long idLibro = registro.getLibro().getId();
            numRegistro = registro.getNumeroRegistro();

            List<Lopd> consultas = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("consultas", consultas);

            List<Lopd> listados = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
            mav.addObject("listados", listados);
        }

        if(idTipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)){

            // Añade la información del Registro creado
            RegistroSalida registro = registroSalidaEjb.findById(idRegistro);
            mav.addObject("registro", registro);

            // Añade la información de las modificaciones del Registro
            List<HistoricoRegistroSalida> modificaciones = historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro);
            // Eliminamos el primer registro porque es el de creación
            if(modificaciones.size()>0) {
                modificaciones.remove(modificaciones.size()-1);
            }
            mav.addObject("modificaciones", modificaciones);

            // Registros Listados y Consultas
            anyoRegistro = formatYear.format(registro.getFecha());
            libro = registro.getLibro();
            Long idLibro = registro.getLibro().getId();
            numRegistro = registro.getNumeroRegistro();

            List<Lopd> consultas = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("consultas", consultas);

            List<Lopd> listados = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
            mav.addObject("listados", listados);
        }

        mav.addObject("idTipoRegistro", idTipoRegistro);
        mav.addObject("numRegistro", numRegistro);
        mav.addObject("anyoRegistro", anyoRegistro);
        mav.addObject("libro", libro);

        return mav;
    }

    /**
     * Realiza el informe en pdf o excel de con las acciones realizadas sobre un registro
     */
    @RequestMapping(value = "/{formato}/{tipoRegistro}/{idRegistro}/registroLopdCrear", method = RequestMethod.GET)
    public ModelAndView registroLopdCrear(@PathVariable Long idRegistro, @PathVariable String formato, @PathVariable String tipoRegistro, HttpServletRequest request)throws Exception {

        ModelAndView mav = null;

        if(formato.equals("pdf")){
            mav = new ModelAndView("registroLopdPdf");
        }else if(formato.equals("excel")){
            mav = new ModelAndView("registroLopdExcel");
        }

        SimpleDateFormat formatDateLong = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

        mav.addObject("tipoRegistro", tipoRegistro);

        // RESGISTRO DE ENTRADA
        if(tipoRegistro.equals("entrada")) {

            RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);

            mav.addObject("numeroRegistro", registro.getNumeroRegistro().toString());

            // Obtiene los datos del registro de entrada
            ArrayList<String> valorRegistro = new ArrayList<String>();
            valorRegistro.add(formatYear.format(registro.getFecha()) + " / " + registro.getNumeroRegistro().toString());
            valorRegistro.add(formatDateLong.format(registro.getFecha()));
            valorRegistro.add(registro.getLibro().getNombreCompleto());
            valorRegistro.add(registro.getUsuario().getUsuario().getIdentificador());
            valorRegistro.add(registro.getOficina().getDenominacion());

            if(registro.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)) {
                valorRegistro.add("VÀLID");
            } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_TRAMITADO)) {
                    valorRegistro.add("TRAMITAT");
                } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_ANULADO)) {
                        valorRegistro.add("ANUL·LAT");
                    } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_ENVIADO)) {
                            valorRegistro.add("ENVIAT");
                        } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_OFICIO_EXTERNO)) {
                                valorRegistro.add("OFICI EXTERN");
                            } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_OFICIO_INTERNO)) {
                                    valorRegistro.add("OFICI INTERN");
                                } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)) {
                                        valorRegistro.add("PENDENT");
                                    } else if(registro.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE_VISAR)) {
                                            valorRegistro.add("PENDENT VISAR");
                                        }

            valorRegistro.add(registro.getDestino().getDenominacion());
            valorRegistro.add(registro.getRegistroDetalle().getExtracto());

            mav.addObject("valorRegistro", valorRegistro);

            // Obtiene los datos del histórico del registro de entrada
            ArrayList<ArrayList<String>> registros = new ArrayList<ArrayList<String>>();

            List<HistoricoRegistroEntrada> historicos = historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro);

            for (int i = 0; i < historicos.size(); i++) {
                registros.add(new ArrayList<String>());
                HistoricoRegistroEntrada historicoRegistroEntrada = historicos.get(i);
                registros.get(i).add(formatYear.format(historicoRegistroEntrada.getRegistroEntrada().getFecha()) + " / " + historicoRegistroEntrada.getRegistroEntrada().getNumeroRegistro().toString());
                registros.get(i).add(formatDateLong.format(historicoRegistroEntrada.getFecha()));
                registros.get(i).add(historicoRegistroEntrada.getModificacion());
                registros.get(i).add(historicoRegistroEntrada.getUsuario().getUsuario().getIdentificador());

                if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)) {
                    registros.get(i).add("VÀLID");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_TRAMITADO)) {
                        registros.get(i).add("TRAMITAT");
                    } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_ANULADO)) {
                            registros.get(i).add("ANUL·LAT");
                        } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_ENVIADO)) {
                                registros.get(i).add("ENVIAT");
                            } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_OFICIO_EXTERNO)) {
                                    registros.get(i).add("OFICI EXTERN");
                                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_OFICIO_INTERNO)) {
                                        registros.get(i).add("OFICI INTERN");
                                    } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)) {
                                            registros.get(i).add("PENDENT");
                                        } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE_VISAR)) {
                                                registros.get(i).add("PENDENT VISAR");
                                            }
            }

            mav.addObject("registros", registros);


        //  REGISTRO DE SALIDA
        } else if(tipoRegistro.equals("salida")){

            RegistroSalida registro = registroSalidaEjb.findById(idRegistro);

            mav.addObject("numeroRegistro", registro.getNumeroRegistro());

            // Obtiene los datos del registro de salida
            ArrayList<String> valorRegistro = new ArrayList<String>();
            valorRegistro.add(registro.getNumeroRegistro().toString());
            valorRegistro.add(formatDateLong.format(registro.getFecha()));
            valorRegistro.add(registro.getLibro().getNombreCompleto());
            valorRegistro.add(registro.getOficina().getDenominacion());

            mav.addObject("valorRegistro", valorRegistro);

        }

        return mav;
    }


    List<Libro> libros(HttpServletRequest request) throws Exception {

        List<Libro> libros = null;

        // Es operador
        if(isOperador(request)){

            // Obtenemos los Libros de los cuales el Usuario es administrador
            if(getLibrosAdministrados(request).size()>0) {
                libros = getLibrosAdministrados(request);
            }else{
                libros = getLibrosConsultaEntradas(request);
            }
        }

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());
        }

        return libros;
    }

    public List<UsuarioEntidad> usuarios(HttpServletRequest request) throws Exception {

        getRolActivo(request);

        List<UsuarioEntidad> usuarios = null;

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            usuarios = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        }

        // Es operador
        if(isOperador(request)){
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            List<Libro> libros = permisoLibroUsuarioEjb.getLibrosAdministrados(usuarioEntidad.getId());

            // Obtenemos los usuarios de los libros de los cuales el Usuario es administrador
            usuarios = permisoLibroUsuarioEjb.getUsuariosEntidadEnLibros(libros);
        }

        return usuarios;
    }

    /**
     * Obtiene los {@link es.caib.regweb.model.UsuarioEntidad} del Libro Seleccionado
     */
    @RequestMapping(value = "/obtenerUsuarios", method = RequestMethod.GET)
    public @ResponseBody
    List<UsuarioEntidad> obtenerUsuarios(@RequestParam Long id,HttpServletRequest request) throws Exception {

        if(id != -1) {
            return permisoLibroUsuarioEjb.getUsuariosEntidadByLibro(id);
        }else{
            return usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        }

    }

    /**
     * Obtiene los {@link es.caib.regweb.model.Libro} del Usuario actual y Tipo Registro seleccionado
     */
    @RequestMapping(value = "/obtenerLibros", method = RequestMethod.GET)
    public @ResponseBody
    List<Libro> obtenerLibros(HttpServletRequest request, @RequestParam Long id) throws Exception {

        List<Libro> libros = null;

        // Es operador
        if(isOperador(request)){

            // Obtenemos los Libros de los cuales el Usuario es administrador
            if(getLibrosAdministrados(request).size()>0) {
                libros = getLibrosAdministrados(request);
            }else if(id.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
                    libros = getLibrosConsultaEntradas(request);
                }else{
                    libros = getLibrosConsultaSalidas(request);
                }
        }

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());
        }

        return libros;
    }

    @ModelAttribute("estados")
    public Long[] estados() throws Exception {
        return RegwebConstantes.ESTADOS_REGISTRO;
    }

    @InitBinder("informeLibroBusquedaForm")
    public void initBinderLibroRegistro(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
        binder.registerCustomEditor(Libro.class, "libros",new LibroEditor());
    }

    @InitBinder("informeIndicadoresBusquedaForm")
    public void initBinderIndicadores(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

    @InitBinder("usuarioLopdBusquedaForm")
    public void initBinderUsuarioLopd(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

    @InitBinder("registroLopdBusquedaForm")
    public void initBinderRegistroLopd(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

}