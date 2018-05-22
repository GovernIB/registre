package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.ejb.*;
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
    private CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    private TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    private PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/ReproEJB/local")
    private ReproLocal reproEjb;

    @EJB(mappedName = "regweb3/InteresadoSirEJB/local")
    private InteresadoSirLocal interesadoSirEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    private InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;

    @EJB(mappedName = "regweb3/NotificacionEJB/local")
    private NotificacionLocal notificacionEjb;

    @RequestMapping(value = "/busquedaPersonas/{tipoPersona}", method = RequestMethod.POST)
    public @ResponseBody List<ObjetoBasico> busquedaPersonas(@PathVariable Long tipoPersona, @RequestParam String query, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
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
     * Obtiene los {@link es.caib.regweb3.model.CodigoAsunto} Activos del TipoAsunto seleccionado
     */
    @RequestMapping(value = "/obtenerCodigosAsuntoActivos", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CodigoAsunto> obtenerCodigosAsuntoActivos(@RequestParam Long id) throws Exception {

        return codigoAsuntoEjb.getActivosByTipoAsunto(id);
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
            return I18NUtils.tradueix("transporte.0" + id);
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

        return catProvinciaEjb.getByComunidadObject(id);

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
     * Obtiene un {@link InteresadoSir}
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

    /**
     * Obtiene un {@link es.caib.regweb3.model.Integracion}
     */
    @RequestMapping(value = "/obtenerIntegracion", method = RequestMethod.GET)
    public @ResponseBody
    Integracion obtenerIntegracion(@RequestParam Long idIntegracion) throws Exception {

        return integracionEjb.findById(idIntegracion);
    }

    /**
     * Obtiene un {@link es.caib.regweb3.model.Notificacion}
     */
    @RequestMapping(value = "/obtenerNotificacion", method = RequestMethod.GET)
    public @ResponseBody
    Notificacion obtenerNotificacion(@RequestParam Long idNotificacion) throws Exception {

        return notificacionEjb.findById(idNotificacion);
    }

    /**
     * Obtiene un {@link es.caib.regweb3.model.Anexo}
     */
    @RequestMapping(value = "/obtenerAnexo", method = RequestMethod.GET)
    public @ResponseBody
    Anexo obtenerAnexo(@RequestParam Long idAnexo) throws Exception {

        return anexoEjb.findById(idAnexo);
    }

    /**
     * Obtiene el nombre traducido de un TipoDocumental
     */
    @RequestMapping(value = "/obtenerTipoDocumental", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTipoDocumental(@RequestParam Long id) throws Exception {

        TipoDocumental tipoDocumental = tipoDocumentalEjb.findById(id);

        if (tipoDocumental != null) {
            Locale locale = LocaleContextHolder.getLocale();
            TraduccionTipoDocumental traduccionTipoDocumental = (TraduccionTipoDocumental) tipoDocumental.getTraduccion(locale.getLanguage());
            return traduccionTipoDocumental.getNombre();

        }
        return null;

    }

    /**
     * Extiende la sesion
     */
    @RequestMapping(value = "/extenderSesion", method = RequestMethod.POST)
    @ResponseBody
    public void extenderSesion() throws Exception {

    }

    /**
     * Obtiene un {@link es.caib.regweb3.model.Cola}
     */
    @RequestMapping(value = "/obtenerCola", method = RequestMethod.GET)
    public @ResponseBody
    Cola obtenerCola(@RequestParam Long idCola) throws Exception {

        return colaEjb.findById(idCola);
    }
}
