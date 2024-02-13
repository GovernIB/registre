package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.ModeloOficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.PermisoOrganismoUsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para Oficio Remision
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class OficioRemisionInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = ModeloOficioRemisionLocal.JNDI_NAME)
    private ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = PermisoOrganismoUsuarioLocal.JNDI_NAME)
    private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // long start = System.currentTimeMillis();
        try {

        String url = request.getServletPath();
        HttpSession session = request.getSession();

        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Rol rolActivo = loginInfo.getRolActivo();
        UsuarioEntidad usuarioEntidad = loginInfo.getUsuarioEntidadActivo();
        Oficina oficinaActiva = loginInfo.getOficinaActiva();
        Entidad entidadActiva = loginInfo.getEntidadActiva();

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone de una OficinaActiva
        if(oficinaActiva == null){
            log.info("No existe una OficinaActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos existe al menos un modelo de oficio de remisión
        List<ModeloOficioRemision> modelos = modeloOficioRemisionEjb.getByEntidad(entidadActiva.getId());

        if(modelos.size() == 0){
            log.info("No existen ModelosOficioRemision");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.modeloOficioRemision"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobaciones previas al listado de OficioRemision
        if (url.equals("/oficioRemision/list")) {

            // Mira que el usuario tiene permisos consulta de entrada en los Organismos
            if(permisoOrganismoUsuarioEjb.getOrganismosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA).size() == 0){
                log.info("Aviso: No hay ningún organismo con permisos para consultar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.list"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas al listado de Oficios de Remisión Pendientes
        if (url.contains("entradasPendientesRemision") ) {

            // Comprueba que el usuario tiene permiso
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficinaActiva.getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA, true)) {
                log.info("Aviso: No tiene permisos para procesar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.list"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas al listado de Oficios de Remisión Pendientes
        if (url.contains("salidasPendientesRemision") ) {

            // Comprueba que el usuario tiene permiso
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficinaActiva.getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA, true)) {
                log.info("Aviso: No tiene permisos para procesar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.list"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        if (url.contains("aceptar")) {

            //Obtenemos los Organismos a los que da servicio la Oficina
            Set<Organismo> organismos = loginInfo.getOrganismosOficinaActiva();
            Set<Long> organismosId = new HashSet<Long>();

            for(Organismo organismo:organismos){
                organismosId.add(organismo.getId());
            }

            // Comprueba que el usuario tiene permiso
            if(!permisoOrganismoUsuarioEjb.tienePermiso(organismosId, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA)){
                log.info("Aviso: No tiene permisos para procesar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.aceptar"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        return true;
        } finally {
          
          //log.info("Interceptor Oficio Remision : " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}
