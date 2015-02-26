package es.caib.regweb.webapp.controller.organismo;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.editor.UsuarioEditor;
import es.caib.regweb.webapp.form.OrganismoBusquedaForm;
import es.caib.regweb.webapp.utils.CodigoValor;
import es.caib.regweb.webapp.validator.LibroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/organismo")
@SessionAttributes(types = Organismo.class)
public class OrganismoController extends BaseController {

    @Autowired
    private LibroValidator libroValidator;
    
    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;
    
    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;
    
    @EJB(mappedName = "regweb/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;
    
    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;


   /**
   * Listado de todos los Organismos
   */
   @RequestMapping(value = "/list", method = RequestMethod.GET)
   public String listado(Model model, HttpServletRequest request) throws  Exception{

       Entidad entidad = getEntidadActiva(request);
       CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

       Organismo organismo = new Organismo();
       organismo.setEstado(vigente);

       OrganismoBusquedaForm organismoBusqueda =  new OrganismoBusquedaForm(organismo,1);

       Paginacion paginacion = organismoEjb.busqueda(1, entidad.getId(), null, vigente.getId());

       // Mirant si es una sincronitzacio o actualitzacio
       Descarga descarga = descargaEjb.findByTipoEntidad(RegwebConstantes.UNIDAD, entidad.getId());
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

      Paginacion paginacion = organismoEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), organismo.getDenominacion(), organismo.getEstado().getId());

      // Mirant si es una sincronitzacio o actualitzacio per mostrar botó de sincro o actualizar
      Descarga descarga = descargaEjb.findByTipoEntidad(RegwebConstantes.UNIDAD, entidad.getId());
      if(descarga != null){
        mav.addObject("descarga", descarga);
      }

      mav.addObject("paginacion", paginacion);
      mav.addObject("organismoBusqueda", busqueda);
      mav.addObject("entidad", entidad);

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
    public ModelAndView oficinaList(HttpServletRequest request)throws Exception {

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
        List<Oficina> oficinasResponsables = oficinaEjb.responsableByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Oficina> oficinasDependientes = oficinaEjb.dependienteByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<RelacionOrganizativaOfi> relacionOrganizativaOfi = relacionOrganizativaOfiEjb.funcionalByEntidadEstado(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<CodigoValor> oficinasFuncionales = new ArrayList<CodigoValor>();
        if(!relacionOrganizativaOfi.isEmpty()) {
            for (int i = 0; i < relacionOrganizativaOfi.size(); i++) {
                CodigoValor codigoValor = new CodigoValor();
                codigoValor.setId(relacionOrganizativaOfi.get(i).getOrganismo().getId().toString());
                codigoValor.setNombre(relacionOrganizativaOfi.get(i).getOficina().getCodigo() + " - " + relacionOrganizativaOfi.get(i).getOficina().getDenominacion());
                oficinasFuncionales.add(codigoValor);
            }
        }

        // Lista los libros de los organismos según el nivel del Organismo al que pertenecen
        Map<String, Long> librosOrganismoPrimerNivel = new HashMap<String, Long>();
        if(!organismosPrimerNivel.isEmpty()) {
            for (int i = 0; i < organismosPrimerNivel.size(); i++) {
                List<Libro> librosPrimer = libroEjb.getLibrosOrganismo(organismosPrimerNivel.get(i).getId());
                for (int j = 0; j < librosPrimer.size(); j++) {
                    librosOrganismoPrimerNivel.put(librosPrimer.get(j).getCodigo() + " - " + librosPrimer.get(j).getNombre(), organismosPrimerNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSegundoNivel = new HashMap<String, Long>();
        if(!organismosSegundoNivel.isEmpty()) {
            for (int i = 0; i < organismosSegundoNivel.size(); i++) {
                List<Libro> librosSegundo = libroEjb.getLibrosOrganismo(organismosSegundoNivel.get(i).getId());
                for (int j = 0; j < librosSegundo.size(); j++) {
                    librosOrganismoSegundoNivel.put(librosSegundo.get(j).getCodigo() + " - " + librosSegundo.get(j).getNombre(), organismosSegundoNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoTercerNivel = new HashMap<String, Long>();
        if(!organismosTercerNivel.isEmpty()) {
            for (int i = 0; i < organismosTercerNivel.size(); i++) {
                List<Libro> librosTercer = libroEjb.getLibrosOrganismo(organismosTercerNivel.get(i).getId());
                for (int j = 0; j < librosTercer.size(); j++) {
                    librosOrganismoTercerNivel.put(librosTercer.get(j).getCodigo() + " - " + librosTercer.get(j).getNombre(), organismosTercerNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoCuartoNivel = new HashMap<String, Long>();
        if(!organismosCuartoNivel.isEmpty()) {
            for (int i = 0; i < organismosCuartoNivel.size(); i++) {
                List<Libro> librosCuarto = libroEjb.getLibrosOrganismo(organismosCuartoNivel.get(i).getId());
                for (int j = 0; j < librosCuarto.size(); j++) {
                    librosOrganismoCuartoNivel.put(librosCuarto.get(j).getCodigo() + " - " + librosCuarto.get(j).getNombre(), organismosCuartoNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoQuintoNivel = new HashMap<String, Long>();
        if(!organismosQuintoNivel.isEmpty()) {
            for (int i = 0; i < organismosQuintoNivel.size(); i++) {
                List<Libro> librosQuinto = libroEjb.getLibrosOrganismo(organismosQuintoNivel.get(i).getId());
                for (int j = 0; j < librosQuinto.size(); j++) {
                    librosOrganismoQuintoNivel.put(librosQuinto.get(j).getCodigo() + " - " + librosQuinto.get(j).getNombre(), organismosQuintoNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSextoNivel = new HashMap<String, Long>();
        if(!organismosSextoNivel.isEmpty()) {
            for (int i = 0; i < organismosSextoNivel.size(); i++) {
                List<Libro> librosSexto = libroEjb.getLibrosOrganismo(organismosSextoNivel.get(i).getId());
                for (int j = 0; j < librosSexto.size(); j++) {
                    librosOrganismoSextoNivel.put(librosSexto.get(j).getCodigo() + " - " + librosSexto.get(j).getNombre(), organismosSextoNivel.get(i).getId());
                }
            }
        }

        Map<String, Long> librosOrganismoSeptimoNivel = new HashMap<String, Long>();
        if(!organismosSeptimoNivel.isEmpty()) {
            for (int i = 0; i < organismosSeptimoNivel.size(); i++) {
                List<Libro> librosSeptimo = libroEjb.getLibrosOrganismo(organismosSeptimoNivel.get(i).getId());
                for (int j = 0; j < librosSeptimo.size(); j++) {
                    librosOrganismoSeptimoNivel.put(librosSeptimo.get(j).getCodigo() + " - " + librosSeptimo.get(j).getNombre(), organismosSeptimoNivel.get(i).getId());
                }
            }
        }

        mav.addObject("organismosPrimerNivel", organismosPrimerNivel);
        mav.addObject("organismosSegundoNivel", organismosSegundoNivel);
        mav.addObject("organismosTercerNivel", organismosTercerNivel);
        mav.addObject("organismosCuartoNivel", organismosCuartoNivel);
        mav.addObject("organismosQuintoNivel", organismosQuintoNivel);
        mav.addObject("organismosSextoNivel", organismosSextoNivel);
        mav.addObject("organismosSeptimoNivel", organismosSeptimoNivel);
        mav.addObject("oficinasResponsables", oficinasResponsables);
        mav.addObject("oficinasDependientes", oficinasDependientes);
        mav.addObject("oficinasFuncionales", oficinasFuncionales);
        mav.addObject("librosOrganismoPrimerNivel", librosOrganismoPrimerNivel);
        mav.addObject("librosOrganismoSegundoNivel", librosOrganismoSegundoNivel);
        mav.addObject("librosOrganismoTercerNivel", librosOrganismoTercerNivel);
        mav.addObject("librosOrganismoCuartoNivel", librosOrganismoCuartoNivel);
        mav.addObject("librosOrganismoQuintoNivel", librosOrganismoQuintoNivel);
        mav.addObject("librosOrganismoSextoNivel", librosOrganismoSextoNivel);
        mav.addObject("librosOrganismoSeptimoNivel", librosOrganismoSeptimoNivel);
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


    /**
     * Obtiene los {@link es.caib.regweb.model.Organismo} hijos de un Organismo
     */
    @RequestMapping(value = "/obtenerOrganismos", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismos(@RequestParam Long id) throws Exception {

        return organismoEjb.getHijos(id);
    }




}
