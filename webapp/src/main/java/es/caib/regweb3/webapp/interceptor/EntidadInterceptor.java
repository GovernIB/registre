package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.DescargaLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Entidad
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class EntidadInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //long start = System.currentTimeMillis();

        try {
            String url = request.getServletPath();

            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();
            Entidad entidad = getLoginInfo(request).getEntidadActiva();
            // Listado usuarios de la entidad
            if(url.equals("/entidad/usuarios")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Crear Libro único
            if(url.equals("/entidad/crearLibro")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Editar entidad
            if((url.contains("edit"))){
                if(!(rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN) || rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN))) {
                    log.info("Error, editar entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.edit"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
                //comprobar variable archivos path
//                if(FileSystemManager.getArchivosPath()==null && rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
                if(PropiedadGlobalUtil.getArchivosPath()==null && rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
                    log.info("Error, editar entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Nueva entidad
            if(url.equals("/entidad/new")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                    log.info("Error, nueva entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.alta"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Listado de Entidades
            if(url.contains("/entidad/list")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                    log.info("Error, listar entidades");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.lista"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Permisos Usuario
            if((url.contains("permisos"))){
                if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
                    log.info("Error, modificar permisos");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.permisos"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Activar/Anular entidad
            if((url.contains("activar") || url.contains("anular") || url.contains("eliminar") || url.contains("reiniciarContadores"))){
              if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                  log.info("Error de rol");
                  Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                  response.sendRedirect("/regweb3/aviso");
                  return false;
              }
            }

            // Sincronizar/Actualizar organismos
            if((url.contains("actualizar") || url.contains("sincronizar"))){
                if(rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
                    Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
                    if (catalogo == null) {
                        Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));
                        response.sendRedirect("/regweb3/aviso");
                        return false;
                    }
                }else{
                    log.info("Error, actualizar/sincronizar");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.sincronizar"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            return true;
        } finally {
          //log.info("Interceptor Entidad: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

    /**
     * Retorna la información del UsuarioAutenticado
     * @param request
     * @return
     */
    private LoginInfo getLoginInfo(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

    }
}
