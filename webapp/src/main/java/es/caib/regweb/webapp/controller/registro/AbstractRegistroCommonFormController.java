package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonFormController extends BaseController {


    @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;



    @ModelAttribute("organismosOficinaActiva")
    public Set<Organismo> getOrganismosOficinaActiva(HttpServletRequest request) throws Exception {
        return organismoEjb.getByOficinaActiva(getOficinaActiva(request).getId());
    }

    @ModelAttribute("tiposAsunto")
    public List<TipoAsunto> tiposAsunto(HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        return tipoAsuntoEjb.getActivosEntidad(entidadActiva.getId());
    }

    @ModelAttribute("tiposPersona")
    public Long[] tiposPersona() throws Exception {
        return RegwebConstantes.TIPOS_PERSONA;
    }

    @ModelAttribute("tiposInteresado")
    public Long[] tiposInteresado() throws Exception {
        return  RegwebConstantes.TIPOS_INTERESADO;
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_REGISTRO;
    }

    @ModelAttribute("transportes")
    public Long[] transportes() throws Exception {
        return RegwebConstantes.TRANSPORTES;
    }

    @ModelAttribute("tiposDocumentacionFisica")
    public Long[] tiposDocumentacionFisica() throws Exception {
        return RegwebConstantes.TIPOS_DOCFISICA;
    }

    @ModelAttribute("tiposDocumento")
    public long[] tiposDocumento() throws Exception {
        return RegwebConstantes.TIPOS_DOCUMENTOID;
    }

    @ModelAttribute("paises")
    public List<CatPais> paises() throws Exception {
        return catPaisEjb.getAll();
    }

    @ModelAttribute("personasFisicas")
    public List<Persona> personasFisicas(HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        return personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA);
    }

    @ModelAttribute("personasJuridicas")
    public List<Persona> personasJuridicas(HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        return personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA);
    }

    @ModelAttribute("provincias")
    public List<CatProvincia> provincias() throws Exception {
        return catProvinciaEjb.getAll();
    }

    @ModelAttribute("canalesNotificacion")
    public long[] canalesNotificacion() throws Exception {
        return RegwebConstantes.CANALES_NOTIFICACION;
    }

    @ModelAttribute("comunidadesAutonomas")
    public List<CatComunidadAutonoma> comunidadesAutonomas() throws Exception {
        return catComunidadAutonomaEjb.getAll();
    }

    @ModelAttribute("nivelesAdministracion")
    public List<CatNivelAdministracion> nivelesAdministracion() throws Exception {
        return catNivelAdministracionEjb.getAll();
    }

    @ModelAttribute("estados")
    public Long[] estados() throws Exception {
        return RegwebConstantes.ESTADOS_REGISTRO;
    }


    /**
     * Obtiene los {@link es.caib.regweb.model.CodigoAsunto} del TipoAsunto seleccionado
     */
    @RequestMapping(value = "/obtenerCodigosAsunto", method = RequestMethod.GET)
    public @ResponseBody
    List<CodigoAsunto> obtenerCodigosAsunto(@RequestParam Long id) throws Exception {

        return codigoAsuntoEjb.getByTipoAsunto(id);
    }

    /**
     * Obtiene los {@link es.caib.regweb.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerLocalidades", method = RequestMethod.GET)
    public @ResponseBody
    List<CatLocalidad> obtenerLocalidades(@RequestParam Long id) throws Exception {

        return catLocalidadEjb.getByProvincia(id);
    }
  
  
}
