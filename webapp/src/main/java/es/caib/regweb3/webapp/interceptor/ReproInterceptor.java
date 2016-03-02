package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Repro;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.ReproLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.StringTokenizer;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Repro
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class ReproInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/ReproEJB/local")
    public ReproLocal reproEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

            // Cualquier accion con Repro
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobaciones previas al envío de Repro
            if(url.contains("/enviar/")){

                String subUrl =  url.replace("repro/enviar/", ""); //Obtenemos los id's a partir de la url
                StringTokenizer tokens = new StringTokenizer(subUrl,"/");
                String idRepro = tokens.nextToken();
                String idUsuarioEntidad = tokens.nextToken();

                Repro repro = reproEjb.findById(Long.valueOf(idRepro));

                // Comprobamos que la Repro existe
                if(repro==null){
                    log.info("Aviso: No existeix la Repro");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.repro.noExiste"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

                UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(Long.valueOf(idUsuarioEntidad));

                // Comprobamos que el usuarioEntidad al que se envía existe y es de la entidad activa
                if(usuarioEntidad == null){
                    log.info("Aviso: No existeix aquest usuariEntitat");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.usuarioEntidad.noExiste"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                } else if(!usuarioEntidad.getEntidad().getId().equals(entidadActiva.getId())){
                    log.info("Aviso: L'usuari no és d'aquesta entitat");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.usuarioEntidad.noExiste"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

            }

            return true;
        } finally {
            //log.info("Interceptor Repro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}