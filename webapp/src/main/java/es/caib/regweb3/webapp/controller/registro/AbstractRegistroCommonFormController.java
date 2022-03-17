package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonFormController extends BaseController {

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    private CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    private CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    private CatNivelAdministracionLocal catNivelAdministracionEjb;


    /**
     * Validaciones antes de permitir editar un registro
     * @param registro
     * @param request
     * @param permiso
     * @return
     * @throws Exception
     */
    public Boolean validarPermisosEdicion(IRegistro registro, HttpServletRequest request, Long permiso) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Comprobamos que el Registro de Entrada es válido para editarse
        final List<Long> estados = new ArrayList<Long>();
        estados.add(RegwebConstantes.REGISTRO_RESERVA);
        estados.add(RegwebConstantes.REGISTRO_VALIDO);

        if (!estados.contains(registro.getEstado())) {
            log.info("Este Registro no se puede modificar");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar"));
            return true;
        }

        // Si tiene Justificante generado, no se puede editar
        if (registro.getRegistroDetalle().getTieneJustificante()) {
            log.info("Este Registro no se puede modificar, porque ya se ha generado su Justificante");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar.justificante"));
            return true;
        }

        // Comprobamos que el UsuarioActivo pueda editar ese registro de entrada
        if(!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getOficina().getOrganismoResponsable().getId(), permiso, true)){
            log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar"));
            return true;
        }

        // Comprobamos si se la oficina activa es la misma donde se creó el registro
        if(!registro.getOficina().getId().equals(oficinaActiva.getId()) && (registro.getOficina().getOficinaResponsable() != null && !registro.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()))){
            log.info("Aviso: No puede editar un registro si no se encuentra en la oficina donde se creó");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar.oficina"));
            return true;
        }

        return false;
    }


    @ModelAttribute("codigosAsunto")
    public List<CodigoAsunto> codigosAsunto(HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        return codigoAsuntoEjb.getActivosEntidad(entidadActiva.getId());
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
}
