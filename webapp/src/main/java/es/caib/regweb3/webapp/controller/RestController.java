package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.sir.core.model.InteresadoSir;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.LocalidadJson;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 22/03/16
 */
@Controller
@RequestMapping(value = "/rest")
public class RestController {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/ReproEJB/local")
    public ReproLocal reproEjb;

    @EJB(mappedName = "regweb3/InteresadoSirEJB/local")
    public InteresadoSirLocal interesadoSirEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;


    @RequestMapping(value = "/busquedaPersonas/{tipoPersona}", method = RequestMethod.POST)
    public @ResponseBody List<ObjetoBasico> busquedaPersonas(@PathVariable Long tipoPersona, @RequestParam String query, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        log.info("Persona: " + query);
        log.info("Persona utf8: " + new String(query.getBytes("ISO-8859-1"), "UTF-8"));
        Entidad entidad =  (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        return personaEjb.busquedaPersonas(query,tipoPersona,entidad.getId());
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerLocalidades", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CatLocalidad> obtenerLocalidades(@RequestParam Long id) throws Exception {

        return catLocalidadEjb.getByProvincia(id);
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.CodigoAsunto} del TipoAsunto seleccionado
     */
    @RequestMapping(value = "/obtenerCodigosAsunto", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CodigoAsunto> obtenerCodigosAsunto(@RequestParam Long id) throws Exception {

        return codigoAsuntoEjb.getByTipoAsunto(id);
    }

    /**
     * Obtiene el nombre traducido de un TipoDocumentacionFisica.
     */
    @RequestMapping(value = "/obtenerTipoDocumentacionFisica", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTipoDocumentacionFisica(@RequestParam Long id) throws Exception {

        return I18NUtils.tradueix("tipoDocumentacionFisica." + id);

    }

    /**
     * Obtiene el nombre traducido de un Transporte.
     */
    @RequestMapping(value = "/obtenerTransporte", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTransporte(@RequestParam Long id) throws Exception {

        if (id != null) {
            return I18NUtils.tradueix("transporte." + id);
        } else {
            return null;
        }

    }

    /**
     * Obtiene el nombre traducido de un TipoAsunto.
     */
    @RequestMapping(value = "/obtenerTipoAsunto", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTipoAsunto(@RequestParam Long id) throws Exception {

        TipoAsunto tipoAsunto = tipoAsuntoEjb.findById(id);

        if (tipoAsunto != null) {
            Locale locale = LocaleContextHolder.getLocale();
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) tipoAsunto.getTraduccion(locale.getLanguage());
            return traduccionTipoAsunto.getNombre();

        }
        return null;
    }

    /**
     * Obtiene el nombre traducido de un CodigoAsunto.
     */
    @RequestMapping(value = "/obtenerCodigoAsunto", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerCodigoAsunto(@RequestParam Long id) throws Exception {

        CodigoAsunto codigoAsunto = codigoAsuntoEjb.findById(id);

        if (codigoAsunto != null) {
            Locale locale = LocaleContextHolder.getLocale();
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) codigoAsunto.getTraduccion(locale.getLanguage());
            return traduccionCodigoAsunto.getNombre();

        }
        return null;
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerLocalidadesProvincia", method = RequestMethod.GET)
    public
    @ResponseBody
    List<LocalidadJson> obtenerLocalidadesProvincia(@RequestParam Long id) throws Exception {

        List<Object[]> localidades = catLocalidadEjb.getByCodigoProvinciaObject(id);
        List<LocalidadJson> localidadescv = new ArrayList<LocalidadJson>();
        for (Object[] object : localidades) {
            localidadescv.add(new LocalidadJson(object[0].toString(), (String) object[1], (String) object[2]));
        }

        return localidadescv;

    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerProvincias", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ObjetoBasico> obtenerProvincias(@RequestParam Long id) throws Exception {
        List<ObjetoBasico> provincias = catProvinciaEjb.getByComunidadObject(id);

        return provincias;

    }

    /**
     * Obtiene las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/obtenerRepros", method = RequestMethod.GET)
    public @ResponseBody
    List<Repro> obtenerRepros(@RequestParam Long idUsuario, @RequestParam Long tipoRegistro) throws Exception {

        return reproEjb.getActivasbyUsuario(idUsuario, tipoRegistro);
    }

    /**
     * Obtiene un {@link es.caib.regweb3.sir.core.model.InteresadoSir}
     */
    @RequestMapping(value = "/obtenerInteresadoSir", method = RequestMethod.GET)
    public @ResponseBody
    InteresadoSir obtenerInteresadoSir(@RequestParam Long idInteresadoSir) throws Exception {

        return interesadoSirEjb.findById(idInteresadoSir);
    }

    /**
     * Obtiene un {@link es.caib.regweb3.model.Interesado}
     */
    @RequestMapping(value = "/obtenerInteresado", method = RequestMethod.GET)
    public @ResponseBody
    Interesado obtenerInteresado(@RequestParam Long idInteresado) throws Exception {

        return interesadoEjb.findById(idInteresado);
    }


}
