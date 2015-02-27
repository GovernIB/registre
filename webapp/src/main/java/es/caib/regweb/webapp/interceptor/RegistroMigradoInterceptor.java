package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroMigrado;
import es.caib.regweb.model.Rol;
import es.caib.regweb.persistence.ejb.RegistroMigradoLocal;
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
 * Interceptor para los Registros Migrados
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class RegistroMigradoInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/RegistroMigradoEJB/local")
    public RegistroMigradoLocal registroMigradoEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
            Boolean tieneRegistrosMigrados = (Boolean) session.getAttribute(RegwebConstantes.SESSION_MIGRADOS);

            // Comprobamos que el usuario dispone del Rol RWE_USUARI
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

            // Comprobaciones previas al listado de Registro Migrado
            if(url.equals("/registroMigrado/list")){

                // Comprobamos que la Oficina tiene PreRegistros
                if(!tieneRegistrosMigrados){
                    log.info("Aviso: No hi ha Registres Migrats");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroMigrado.list"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }

            }

            // Comprobaciones previas al detalle de un Registro Migrado
            if(url.contains("detalle")){

                String idRegistroMigrado =  url.replace("/registroMigrado/","").replace("/detalle", ""); //Obtenemos el id a partir de la url
                log.info("idRegistroMigrado: " + idRegistroMigrado);

                RegistroMigrado registroMigrado = registroMigradoEjb.findById(Long.valueOf(idRegistroMigrado));

                // Comprobamos que el Registro Migrado existe
                if(registroMigrado == null){
                    log.info("Aviso: No existeix aquest PreRegistre");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroMigrado.detalle"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }

                // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
                if(!registroMigrado.getEntidad().getId().equals(entidadActiva.getId())){
                    log.info("Aviso: No existe este registro en esta Entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroMigrado.detalle"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }

            }

            // Comprobaciones previas de un Registro Migrado Lopd
            if(url.contains("lopd")){

                String idRegistroMigrado =  url.replace("/registroMigrado/","").replace("/lopd", ""); //Obtenemos el id a partir de la url
                log.info("idRegistroMigrado: " + idRegistroMigrado);

                RegistroMigrado registroMigrado = registroMigradoEjb.findById(Long.valueOf(idRegistroMigrado));

                // Comprobamos que el Registro Migrado existe
                if(registroMigrado == null){
                    log.info("Aviso: No existeix aquest Registre Migrat");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroMigrado.detalle"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }

                // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
                if(!registroMigrado.getEntidad().getId().equals(entidadActiva.getId())){
                    log.info("Aviso: No existe este registro Migrado en esta Entidad");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroMigrado.detalle"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }

            }


            return true;
        } finally {
            //log.info("Interceptor PreRegistro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}