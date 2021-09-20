package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plantilla;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
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
import java.util.StringTokenizer;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Plantilla
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class PlantillaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/PlantillaEJB/local")
    private PlantillaLocal plantillaEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();
            Entidad entidadActiva = loginInfo.getEntidadActiva();

            // Entidad Activa
            if(entidadActiva == null){
                log.info("No existe ninguna entidad activa para el usuario");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Cualquier accion con Plantilla
            if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobaciones previas al envío de Plantilla
            if(url.contains("/enviar/")){

                String subUrl =  url.replace("plantilla/enviar/", ""); //Obtenemos los id's a partir de la url
                StringTokenizer tokens = new StringTokenizer(subUrl,"/");
                String idPlantilla = tokens.nextToken();
                String idUsuarioEntidad = tokens.nextToken();

                Plantilla plantilla = plantillaEjb.findById(Long.valueOf(idPlantilla));

                // Comprobamos que la Plantilla existe
                if(plantilla ==null){
                    log.info("Aviso: No existeix la plantilla");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plantilla.noExiste"));
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
                    } else if(!usuarioEntidad.getActivo() || usuarioEntidad.getUsuario().getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)){
                        log.info("Aviso: És usuari aplicació o no està actiu");
                        Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.usuarioEntidad.noExiste"));
                        response.sendRedirect("/regweb3/aviso");
                        return false;
                        }

            }

            return true;
        } finally {
            //log.info("Interceptor Plantilla: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}