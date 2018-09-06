package es.caib.regweb3.webapp.controller.organismo;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.editor.UsuarioEditor;
import es.caib.regweb3.webapp.form.OrganismoBusquedaForm;
import es.caib.regweb3.webapp.validator.LibroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/organismo")
@SessionAttributes(types = Organismo.class)
public class OrganismoController extends BaseController {

    @Autowired
    private LibroValidator libroValidator;
    
    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;
    
    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    private CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;

    @EJB(mappedName = "regweb3/RelacionSirOfiEJB/local")
    private RelacionSirOfiLocal relacionSirOfiEjb;


   /**
   * Listado de todos los Organismos
   */
   @RequestMapping(value = "/list", method = RequestMethod.GET)
   public String listado(Model model, HttpServletRequest request) throws  Exception{

       Entidad entidad = getEntidadActiva(request);
       CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

       Organismo organismo = new Organismo();
       organismo.setEstado(vigente);

       OrganismoBusquedaForm organismoBusqueda =  new OrganismoBusquedaForm(organismo,1, false);

       Paginacion paginacion = organismoEjb.busqueda(1, entidad.getId(), null, null, vigente.getId(), false);

       // Mirant si es una sincronitzacio o actualitzacio
       Descarga descarga = descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidad.getId());
       if(descarga != null){
            model.addAttribute("descarga", descarga);
       }

       model.addAttribute("paginacion", paginacion);
       model.addAttribute("organismoBusqueda",organismoBusqueda);
       model.addAttribute("entidad", entidad);

       return "organismo/organismoList";
   }

   /**
   * Listado de organismos
   * @param busqueda
   * @return
   * @throws Exception
   */
   @RequestMapping(value = "/list", method = RequestMethod.POST)
   public ModelAndView list(@ModelAttribute OrganismoBusquedaForm busqueda, HttpServletRequest request)throws Exception {

      ModelAndView mav = new ModelAndView("organismo/organismoList");

      Organismo organismo = busqueda.getOrganismo();
      Entidad entidad = getEntidadActiva(request);

      Paginacion paginacion = organismoEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), null, organismo.getDenominacion(), organismo.getEstado().getId(), busqueda.getLibros());

      // Mirant si es una sincronitzacio o actualitzacio per mostrar botó de sincro o actualizar
      Descarga descarga = descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidad.getId());
      if(descarga != null){
        mav.addObject("descarga", descarga);
      }

      mav.addObject("paginacion", paginacion);
      mav.addObject("organismoBusqueda", busqueda);
      mav.addObject("entidad", entidad);

      return mav;
  }

    /**
     * Listado de oficinas que dan servicio a un un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/oficinas", method = RequestMethod.GET)
    public ModelAndView oficinas(@PathVariable Long idOrganismo)throws Exception {

        ModelAndView mav = new ModelAndView("organismo/oficinasList");

        Organismo organismo = organismoEjb.findById(idOrganismo);
        LinkedHashSet<Oficina> oficinas = oficinaEjb.oficinasServicio(idOrganismo, RegwebConstantes.OFICINA_VIRTUAL_SI);

        mav.addObject("organismo", organismo);
        mav.addObject("oficinas", oficinas);

        return mav;
    }


    /**
     * Listado de usuarios de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/usuarios", method = RequestMethod.GET)
    public ModelAndView usuarios(@PathVariable Long idOrganismo)throws Exception {

        ModelAndView mav = new ModelAndView("usuario/usuariosList");

        Organismo organismo = organismoEjb.findById(idOrganismo);

        mav.addObject("organismo", organismo);

        return mav;
    }

    /**
     * Listado de organismos y oficinas
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/arbolList", method = RequestMethod.GET)
    public ModelAndView arbolList(HttpServletRequest request) throws Exception {
        Long start = System.currentTimeMillis();

        ModelAndView mav = new ModelAndView("organismo/arbolList");

        Entidad entidad = getEntidadActiva(request);

        // Lista los organismos según su nivel
        List<Organismo> organismosPrimerNivel = organismoEjb.getOrganismosByNivel((long) 1, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosSegundoNivel = organismoEjb.getOrganismosByNivel((long) 2, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosTercerNivel = organismoEjb.getOrganismosByNivel((long) 3, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosCuartoNivel = organismoEjb.getOrganismosByNivel((long) 4, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosQuintoNivel = organismoEjb.getOrganismosByNivel((long) 5, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosSextoNivel = organismoEjb.getOrganismosByNivel((long) 6, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Organismo> organismosSeptimoNivel = organismoEjb.getOrganismosByNivel((long) 7, entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        // Subimos los niveles de los organismos para empezar desde la raiz
        if(organismosPrimerNivel.size() == 0){
            if(organismosSegundoNivel.size() == 0){
                if(organismosTercerNivel.size() == 0){
                    if(organismosCuartoNivel.size() == 0){
                        if(organismosQuintoNivel.size() == 0){
                            if(organismosSextoNivel.size() == 0){
                                if(organismosSeptimoNivel.size() > 0){
                                    organismosPrimerNivel.addAll(organismosSeptimoNivel);
                                    organismosSeptimoNivel.clear();
                                }
                            } else{
                                organismosPrimerNivel.addAll(organismosSextoNivel);
                                organismosSegundoNivel.addAll(organismosSeptimoNivel);
                                organismosSextoNivel.clear();
                                organismosSeptimoNivel.clear();
                            }
                        } else{
                            organismosPrimerNivel.addAll(organismosQuintoNivel);
                            organismosSegundoNivel.addAll(organismosSextoNivel);
                            organismosTercerNivel.addAll(organismosSeptimoNivel);
                            organismosQuintoNivel.clear();
                            organismosSextoNivel.clear();
                            organismosSeptimoNivel.clear();
                        }
                    } else{
                        organismosPrimerNivel.addAll(organismosCuartoNivel);
                        organismosSegundoNivel.addAll(organismosQuintoNivel);
                        organismosTercerNivel.addAll(organismosSextoNivel);
                        organismosCuartoNivel.addAll(organismosSeptimoNivel);
                        organismosQuintoNivel.clear();
                        organismosSextoNivel.clear();
                        organismosSeptimoNivel.clear();
                    }
                } else{
                    organismosPrimerNivel.addAll(organismosTercerNivel);
                    organismosSegundoNivel.addAll(organismosCuartoNivel);
                    organismosTercerNivel.addAll(organismosQuintoNivel);
                    organismosCuartoNivel.addAll(organismosSextoNivel);
                    organismosQuintoNivel.addAll(organismosSeptimoNivel);
                    organismosSextoNivel.clear();
                    organismosSeptimoNivel.clear();
                }
            } else{
                organismosPrimerNivel.addAll(organismosSegundoNivel);
                organismosSegundoNivel.addAll(organismosTercerNivel);
                organismosTercerNivel.addAll(organismosCuartoNivel);
                organismosCuartoNivel.addAll(organismosQuintoNivel);
                organismosQuintoNivel.addAll(organismosSextoNivel);
                organismosSextoNivel.addAll(organismosSeptimoNivel);
                organismosSeptimoNivel.clear();
            }
        }

        // Lista las Oficinas según si son Responsables, Dependientes o Funcionales
        List<Oficina> oficinasPrincipales = oficinaEjb.responsableByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Oficina> oficinasAuxiliares = oficinaEjb.dependienteByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        // Lista las Oficinas Organizativas
        List<RelacionOrganizativaOfi> relacionesOrganizativaOfi = relacionOrganizativaOfiEjb.organizativaByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        // Lista las Relaciones SirOfi
        List<RelacionSirOfi> relacionesSirOfi = relacionSirOfiEjb.relacionesSirOfiByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        // Lista los libros de los organismos según el nivel del Organismo al que pertenecen
        Map<String, Long> librosOrganismoPrimerNivel = new HashMap<String, Long>();
        if(!organismosPrimerNivel.isEmpty()) {
            for (Organismo anOrganismosPrimerNivel : organismosPrimerNivel) {
                List<Libro> librosPrimer = libroEjb.getLibrosActivosOrganismo(anOrganismosPrimerNivel.getId());
                for (Libro aLibrosPrimer : librosPrimer) {
                    librosOrganismoPrimerNivel.put(aLibrosPrimer.getCodigo() + " - " + aLibrosPrimer.getNombre(), anOrganismosPrimerNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSegundoNivel = new HashMap<String, Long>();
        if(!organismosSegundoNivel.isEmpty()) {
            for (Organismo anOrganismosSegundoNivel : organismosSegundoNivel) {
                List<Libro> librosSegundo = libroEjb.getLibrosActivosOrganismo(anOrganismosSegundoNivel.getId());
                for (Libro aLibrosSegundo : librosSegundo) {
                    librosOrganismoSegundoNivel.put(aLibrosSegundo.getCodigo() + " - " + aLibrosSegundo.getNombre(), anOrganismosSegundoNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoTercerNivel = new HashMap<String, Long>();
        if(!organismosTercerNivel.isEmpty()) {
            for (Organismo anOrganismosTercerNivel : organismosTercerNivel) {
                List<Libro> librosTercer = libroEjb.getLibrosActivosOrganismo(anOrganismosTercerNivel.getId());
                for (Libro aLibrosTercer : librosTercer) {
                    librosOrganismoTercerNivel.put(aLibrosTercer.getCodigo() + " - " + aLibrosTercer.getNombre(), anOrganismosTercerNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoCuartoNivel = new HashMap<String, Long>();
        if(!organismosCuartoNivel.isEmpty()) {
            for (Organismo anOrganismosCuartoNivel : organismosCuartoNivel) {
                List<Libro> librosCuarto = libroEjb.getLibrosActivosOrganismo(anOrganismosCuartoNivel.getId());
                for (Libro aLibrosCuarto : librosCuarto) {
                    librosOrganismoCuartoNivel.put(aLibrosCuarto.getCodigo() + " - " + aLibrosCuarto.getNombre(), anOrganismosCuartoNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoQuintoNivel = new HashMap<String, Long>();
        if(!organismosQuintoNivel.isEmpty()) {
            for (Organismo anOrganismosQuintoNivel : organismosQuintoNivel) {
                List<Libro> librosQuinto = libroEjb.getLibrosActivosOrganismo(anOrganismosQuintoNivel.getId());
                for (Libro aLibrosQuinto : librosQuinto) {
                    librosOrganismoQuintoNivel.put(aLibrosQuinto.getCodigo() + " - " + aLibrosQuinto.getNombre(), anOrganismosQuintoNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSextoNivel = new HashMap<String, Long>();
        if(!organismosSextoNivel.isEmpty()) {
            for (Organismo anOrganismosSextoNivel : organismosSextoNivel) {
                List<Libro> librosSexto = libroEjb.getLibrosActivosOrganismo(anOrganismosSextoNivel.getId());
                for (Libro aLibrosSexto : librosSexto) {
                    librosOrganismoSextoNivel.put(aLibrosSexto.getCodigo() + " - " + aLibrosSexto.getNombre(), anOrganismosSextoNivel.getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSeptimoNivel = new HashMap<String, Long>();
        if(!organismosSeptimoNivel.isEmpty()) {
            for (Organismo anOrganismosSeptimoNivel : organismosSeptimoNivel) {
                List<Libro> librosSeptimo = libroEjb.getLibrosActivosOrganismo(anOrganismosSeptimoNivel.getId());
                for (Libro aLibrosSeptimo : librosSeptimo) {
                    librosOrganismoSeptimoNivel.put(aLibrosSeptimo.getCodigo() + " - " + aLibrosSeptimo.getNombre(), anOrganismosSeptimoNivel.getId());
                }
            }
        }

        int librosTotal = librosOrganismoPrimerNivel.size() + librosOrganismoSegundoNivel.size() + librosOrganismoTercerNivel.size() +
                librosOrganismoCuartoNivel.size() + librosOrganismoQuintoNivel.size() + librosOrganismoSextoNivel.size() +
                librosOrganismoSeptimoNivel.size();
        Long end = System.currentTimeMillis();
        log.debug("TIEMPO CARGA ARBOLarbol: " + TimeUtils.formatElapsedTime(end - start));

        mav.addObject("organismosPrimerNivel", organismosPrimerNivel);
        mav.addObject("organismosSegundoNivel", organismosSegundoNivel);
        mav.addObject("organismosTercerNivel", organismosTercerNivel);
        mav.addObject("organismosCuartoNivel", organismosCuartoNivel);
        mav.addObject("organismosQuintoNivel", organismosQuintoNivel);
        mav.addObject("organismosSextoNivel", organismosSextoNivel);
        mav.addObject("organismosSeptimoNivel", organismosSeptimoNivel);
        mav.addObject("oficinasPrincipales", oficinasPrincipales);
        mav.addObject("oficinasAuxiliares", oficinasAuxiliares);
        mav.addObject("relacionesOrganizativaOfi", relacionesOrganizativaOfi);
        mav.addObject("relacionesSirOfi", relacionesSirOfi);
        mav.addObject("librosOrganismoPrimerNivel", librosOrganismoPrimerNivel);
        mav.addObject("librosOrganismoSegundoNivel", librosOrganismoSegundoNivel);
        mav.addObject("librosOrganismoTercerNivel", librosOrganismoTercerNivel);
        mav.addObject("librosOrganismoCuartoNivel", librosOrganismoCuartoNivel);
        mav.addObject("librosOrganismoQuintoNivel", librosOrganismoQuintoNivel);
        mav.addObject("librosOrganismoSextoNivel", librosOrganismoSextoNivel);
        mav.addObject("librosOrganismoSeptimoNivel", librosOrganismoSeptimoNivel);
        mav.addObject("librosTotal", librosTotal);
        mav.addObject("entidad", entidad);

        return mav;
    }

    @ModelAttribute("estados")
    public List<CatEstadoEntidad> estados() throws Exception {
        return catEstadoEntidadEjb.getAll();
    }

    @InitBinder("libro")
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Usuario.class, "administradores",new UsuarioEditor());
        binder.setValidator(this.libroValidator);
    }

}
