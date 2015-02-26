package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Rol;
import es.caib.regweb.persistence.ejb.EntidadLocal;
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
 * Interceptor para la gestión de Usuarios
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class EntidadInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());


    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;


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
                String idEntidad =  url.replace("/entidad/","").replace("/edit", ""); //Obtenemos el id a partir de la url
                Entidad entidad = entidadEjb.findById(Long.valueOf(idEntidad));

                if(!entidad.getActivo()){ //Si la entidad está anulada, no se puede editar
                    log.info("Error, entidad anulada");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.anulada"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Activar/Anular entidad
            if((url.contains("activar") || url.contains("anular"))){
              if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                  log.info("Error de rol");
                  Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
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
