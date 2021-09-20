package es.caib.regweb3.webapp.controller.descarga;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.DescargaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * Created 14/02/14 12:52
 * Controller que gestiona todas las operaciones con {@link Descarga}
 *
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/descarga")
@SessionAttributes("descarga")
public class DescargaController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = DescargaLocal.JNDI_NAME)
    private DescargaLocal descargaEjb;


    /**
     * Listado de todos los TiposAsuntos
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) throws IOException {

        Rol rolActivo = getRolActivo(request);
        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            return "redirect:/aviso";

        }

        return "redirect:/descarga/list/1";
    }

    /**
     * Listado de tipos de asunto
     *
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {


        ModelAndView mav = new ModelAndView("descarga/descargaList");

        Entidad entidad = getEntidadActiva(request);

        List<Descarga> listado = descargaEjb.getPagination((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = descargaEjb.getTotalEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.TipoAsunto}
     */
    @RequestMapping(value = "/{idDescarga}/delete")
    public String eliminar(@PathVariable Long idDescarga, HttpServletRequest request) {

        try {

            Descarga descarga = descargaEjb.findById(idDescarga);
            descargaEjb.remove(descarga);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/descarga/list";
    }


}
