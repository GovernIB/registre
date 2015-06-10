package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Descarga;
import es.caib.regweb.model.Rol;
import es.caib.regweb.persistence.ejb.DescargaLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
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

    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //long start = System.currentTimeMillis();

        try {
            String url = request.getServletPath();

            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

            // Listado usuarios de la entidad
            if(url.equals("/entidad/usuarios")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Editar entidad
            if((url.contains("edit"))){
                if(!(rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN) || rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN))) {
                    log.info("Error, editar entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.edit"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Nueva entidad
            if(url.equals("/entidad/new")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                    log.info("Error, nueva entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.alta"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Listado de Entidades
            if(url.equals("/entidad/list")){
                if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                    log.info("Error, listar entidades");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.lista"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Permisos Usuario
            if((url.contains("permisos"))){
                if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){
                    log.info("Error, modificar permisos");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.permisos"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Activar/Anular entidad
            if((url.contains("activar") || url.contains("anular") || url.contains("eliminar"))){
              if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                  log.info("Error de rol");
                  Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                  response.sendRedirect("/regweb/aviso");
                  return false;
              }
            }

            // Sincronizar/Actualizar organismos
            if((url.contains("actualizar") || url.contains("sincronizar"))){
                if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)) {
                    Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
                    if (catalogo == null) {
                        Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));
                        response.sendRedirect("/regweb/aviso");
                        return false;
                    }
                }else{
                    log.info("Error, actualizar/sincronizar");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.sincronizar"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            return true;
        } finally {
          //log.info("Interceptor Entidad: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }


}
