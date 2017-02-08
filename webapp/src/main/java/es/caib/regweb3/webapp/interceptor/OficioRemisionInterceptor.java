package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para el Registro de Entrada
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class OficioRemisionInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    public ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // long start = System.currentTimeMillis();
        try {

        String url = request.getServletPath();
        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        UsuarioEntidad usuarioEntidad = (UsuarioEntidad) session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
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

        // Comprobamos que la Entidad esté configurada para tramitar de Oficios de Remisión
        if(!entidadActiva.getOficioRemision()){
            log.info("La Entidad no esta configurada para tramitar Oficios de Remisión");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.entidad"));
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
        if (url.equals("/oficioRemision/list") || url.contains("pendientesLlegada")) {

            Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

            List<Libro> libros = permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

            // Mira que el usuario tiene permisos consulta de entrada en los Libros
            if (libros.size() == 0) {
                log.info("Aviso: No tiene permisos para consultar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.list"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas al listado de Oficios de Remisión Pendientes
        if (url.contains("PendientesRemision") || url.contains("procesar")) {

            Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

            List<Libro> libros = permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA);

            // Mira que el usuario tiene permisos consulta de entrada en los Libros
            if (libros.size() == 0) {
                log.info("Aviso: No tiene permisos para procesar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.procesar"));
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
