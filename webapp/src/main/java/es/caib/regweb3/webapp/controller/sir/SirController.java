package es.caib.regweb3.webapp.controller.sir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficioRemisionBusquedaForm;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.form.RegistroSirBusquedaForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/sir")
public class SirController extends BaseController {

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    private ArchivoLocal archivoEjb;

    /**
     * Listado de oficios de remisión sir enviados
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.GET)
    public ModelAndView enviados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        // Obtenemos todos los Libros activos de la Entidad
        List<Libro> libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("librosConsulta", libros);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.POST)
    public ModelAndView enviados(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Obtenemos todos los Libros activos de la Entidad
        List<Libro> libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), dataFi,null, oficioRemision, null, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), true);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("librosConsulta", libros);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;

    }


    /**
     * Listado de RegistroSir recibidos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSirBusquedaForm registroSirBusquedaForm = new RegistroSirBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("registroSirBusqueda", registroSirBusquedaForm);
        model.addAttribute("anys", getAnys());
        model.addAttribute("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de {@link RegistroSir} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSir registroSir = busqueda.getRegistroSir();

        Paginacion paginacion = registroSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()), registroSir, registroSir.getCodigoEntidadRegistral(), busqueda.getEstado());

        busqueda.setPageNumber(1);

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("paginacion", paginacion);
        mav.addObject("registroSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;

    }

    /**
     * Listado de todos los RegistroSirs
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String pendientesDistribuir() {
        return "redirect:/registroSir/pendientesDistribuir";
    }

    /**
     * Listado de RegistroSirs
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pendientesDistribuir", method = RequestMethod.GET)
    public ModelAndView pendientesDistribuir(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/pendientesDistribuirList");
        Entidad entidad = getEntidadActiva(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        mav.addObject("registroEntradaBusqueda", registroEntradaBusqueda);
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de {@link RegistroSir} según los parametros del formulario
     */
    @RequestMapping(value = "/pendientesDistribuir", method = RequestMethod.POST)
    public ModelAndView pendientesDistribuir(@ModelAttribute RegistroEntradaBusqueda busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/pendientesDistribuirList");
        Entidad entidad = getEntidadActiva(request);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        Paginacion paginacion = trazabilidadEjb.getPendientesDistribuirSir(registroEntrada.getOficina().getId(), entidad.getId(),busqueda.getPageNumber());

        busqueda.setPageNumber(1);

        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));
        mav.addObject("paginacion", paginacion);
        mav.addObject("registroEntradaBusqueda", busqueda);

        return mav;

    }

    /**
     * Reinicia el contador de reintentos SIR
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOficio}/reiniciar", method = RequestMethod.GET)
    public String reiniciar(@PathVariable Long idOficio, HttpServletRequest request)throws Exception {

        oficioRemisionEjb.reiniciarIntentos(idOficio);

        Mensaje.saveMessageInfo(request, getMessage("registroSir.reiniciar.ok"));

        return "redirect:/sir/monitorEnviados";
    }

    /**
     * Envia un mensaje ACK
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enviarACK", method = RequestMethod.GET)
    @ResponseBody
    public Boolean enviarACK(@RequestParam Long idRegistroSir, HttpServletRequest request)throws Exception {

        try{
            return registroSirEjb.enviarACK(idRegistroSir);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
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
            File directorio = FileSystemManager.getArchivosPath();
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
            File directorio = FileSystemManager.getArchivosPath();
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

}
