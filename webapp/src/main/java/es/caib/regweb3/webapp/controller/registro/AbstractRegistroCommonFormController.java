package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonFormController extends BaseController {


    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;


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
        if(Configuracio.getDefaultLanguage().equals(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO)){
            return RegwebConstantes.IDIOMAS_REGISTRO_ES;
        }
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
     * Para la busqueda de organismos en interesados
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute("comunidad")
    public CatComunidadAutonoma comunidad(HttpServletRequest request) throws Exception {
        Entidad entidad = getEntidadActiva(request);
        Organismo organismoRaiz = organismoEjb.findByCodigoLigero(entidad.getCodigoDir3());
        if ((organismoRaiz != null) && organismoRaiz.getCodAmbComunidad() != null) {
            return organismoRaiz.getCodAmbComunidad();
        }
        return new CatComunidadAutonoma();
    }
}
