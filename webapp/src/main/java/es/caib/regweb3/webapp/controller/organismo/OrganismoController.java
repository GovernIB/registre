package es.caib.regweb3.webapp.controller.organismo;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OrganismoBusquedaForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Entidad}
 *
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/organismo")
@SessionAttributes(types = Organismo.class)
public class OrganismoController extends BaseController {

    @EJB(mappedName = DescargaLocal.JNDI_NAME)
    private DescargaLocal descargaEjb;

    @EJB(mappedName = CatEstadoEntidadLocal.JNDI_NAME)
    private CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = LibroLocal.JNDI_NAME)
    private LibroLocal libroEjb;

    @EJB(mappedName = RelacionOrganizativaOfiLocal.JNDI_NAME)
    private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;

    @EJB(mappedName = RelacionSirOfiLocal.JNDI_NAME)
    private RelacionSirOfiLocal relacionSirOfiEjb;


    /**
     * Listado de todos los Organismos
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        Organismo organismo = new Organismo();
        organismo.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        OrganismoBusquedaForm organismoBusqueda = new OrganismoBusquedaForm(organismo, 1);

        Paginacion paginacion = organismoEjb.busqueda(1, entidad.getId(), organismo);

        // Mirant si es una sincronitzacio o actualitzacio
        Descarga descarga = descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidad.getId());
        if (descarga != null) {
            model.addAttribute("descarga", descarga);
        }

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("organismoBusqueda", organismoBusqueda);
        model.addAttribute("entidad", entidad);

        return "organismo/organismoList";
    }

    /**
     * Listado de organismos
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OrganismoBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("organismo/organismoList");

        Organismo organismo = busqueda.getOrganismo();
        Entidad entidad = getEntidadActiva(request);

        Paginacion paginacion = organismoEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), organismo);

        // Mirant si es una sincronitzacio o actualitzacio per mostrar botó de sincro o actualizar
        Descarga descarga = descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidad.getId());
        if (descarga != null) {
            mav.addObject("descarga", descarga);
        }

        mav.addObject("paginacion", paginacion);
        mav.addObject("organismoBusqueda", busqueda);
        mav.addObject("entidad", entidad);

        return mav;
    }

    /**
     * Listado de oficinas que dan servicio a un un Organismo
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/oficinas", method = RequestMethod.GET)
    public ModelAndView oficinas(@PathVariable Long idOrganismo) throws Exception {

        ModelAndView mav = new ModelAndView("organismo/oficinasList");

        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);
        LinkedHashSet<Oficina> oficinas = oficinaEjb.oficinasServicioCompleto(idOrganismo, RegwebConstantes.OFICINA_VIRTUAL_SI);

        mav.addObject("organismo", organismo);
        mav.addObject("oficinas", oficinas);

        return mav;
    }


    /**
     * Listado de usuarios de un Organismo
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/usuarios", method = RequestMethod.GET)
    public String usuariosOrganismo(@PathVariable Long idOrganismo, Model model, HttpServletRequest request) throws Exception {

        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);
        Entidad entidadActiva = getEntidadActiva(request);

        // Comprueba que el Organismo existe
        if (organismo == null) {
            log.info("No existe este organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.noExiste"));
            return "redirect:/organismo/list";
        }

        // Comprueba que el Organismo pertenece a la Entida Activa
        if (!organismo.getEntidad().equals(entidadActiva)) {
            log.info("No administra este Organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.rol"));
            return "redirect:/organismo/list";
        }

        List<PermisoOrganismoUsuario> pou = permisoOrganismoUsuarioEjb.findByOrganismo(organismo.getId());
        List<UsuarioEntidad> usuarios = permisoOrganismoUsuarioEjb.getUsuariosEntidadByOrganismo(organismo.getId());

        model.addAttribute("organismo", organismo);
        model.addAttribute("pou", pou);
        model.addAttribute("permisos", RegwebConstantes.PERMISOS);
        model.addAttribute("usuarios", usuarios);

        return "organismo/usuariosOrganismoList";

    }


    /**
     * Activar los usuarios de un {@link es.caib.regweb3.model.Organismo}
     */
    @RequestMapping(value = "/{idOrganismo}/activarUsuarios")
    public String activarUsuariosOrganismo(@PathVariable Long idOrganismo, HttpServletRequest request) {

        try {

            Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

            if (oficinaEjb.tieneOficinasServicio(idOrganismo, RegwebConstantes.OFICINA_VIRTUAL_SI) && !entidadEjb.existeCodigoDir3Edit(organismo.getCodigo(), getEntidadActiva(request).getId())) {

                organismoEjb.activarUsuarios(idOrganismo);
                permisoOrganismoUsuarioEjb.crearPermisosUsuarioOARM(organismo);

                Mensaje.saveMessageInfo(request, getMessage("organismo.activarUsuarios.ok"));
            } else {
                Mensaje.saveMessageError(request, getMessage("organismo.activarUsuarios.error"));
            }

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/organismo/list";
    }

    /**
     * Desactiv los usuarios de un {@link es.caib.regweb3.model.Organismo}
     */
    @RequestMapping(value = "/{idOrganismo}/desactivarUsuarios")
    public String desactivarUsuariosOrganismo(@PathVariable Long idOrganismo, HttpServletRequest request) {

        try {
            organismoEjb.desactivarUsuarios(idOrganismo);

            // Eliminamos los permisos de los usuarios de ese Organismo
            permisoOrganismoUsuarioEjb.eliminarPermisosOrganismo(idOrganismo);

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/organismo/list";
    }

    /**
     * Listado de organismos y oficinas
     *
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
        if (organismosPrimerNivel.size() == 0) {
            if (organismosSegundoNivel.size() == 0) {
                if (organismosTercerNivel.size() == 0) {
                    if (organismosCuartoNivel.size() == 0) {
                        if (organismosQuintoNivel.size() == 0) {
                            if (organismosSextoNivel.size() == 0) {
                                if (organismosSeptimoNivel.size() > 0) {
                                    organismosPrimerNivel.addAll(organismosSeptimoNivel);
                                    organismosSeptimoNivel.clear();
                                }
                            } else {
                                organismosPrimerNivel.addAll(organismosSextoNivel);
                                organismosSegundoNivel.addAll(organismosSeptimoNivel);
                                organismosSextoNivel.clear();
                                organismosSeptimoNivel.clear();
                            }
                        } else {
                            organismosPrimerNivel.addAll(organismosQuintoNivel);
                            organismosSegundoNivel.addAll(organismosSextoNivel);
                            organismosTercerNivel.addAll(organismosSeptimoNivel);
                            organismosQuintoNivel.clear();
                            organismosSextoNivel.clear();
                            organismosSeptimoNivel.clear();
                        }
                    } else {
                        organismosPrimerNivel.addAll(organismosCuartoNivel);
                        organismosSegundoNivel.addAll(organismosQuintoNivel);
                        organismosTercerNivel.addAll(organismosSextoNivel);
                        organismosCuartoNivel.addAll(organismosSeptimoNivel);
                        organismosQuintoNivel.clear();
                        organismosSextoNivel.clear();
                        organismosSeptimoNivel.clear();
                    }
                } else {
                    organismosPrimerNivel.addAll(organismosTercerNivel);
                    organismosSegundoNivel.addAll(organismosCuartoNivel);
                    organismosTercerNivel.addAll(organismosQuintoNivel);
                    organismosCuartoNivel.addAll(organismosSextoNivel);
                    organismosQuintoNivel.addAll(organismosSeptimoNivel);
                    organismosSextoNivel.clear();
                    organismosSeptimoNivel.clear();
                }
            } else {
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
        mav.addObject("entidad", entidad);

        return mav;
    }

    @ModelAttribute("estados")
    public List<CatEstadoEntidad> estados() throws Exception {
        return catEstadoEntidadEjb.getAll();
    }

}
