package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.RegistroEntradaConsultaLocal;
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

/**
 * Created by Fundació BIT.
 *
 * Interceptor para el Registro de Entrada
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class RegistroEntradaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = RegistroEntradaConsultaLocal.JNDI_NAME)
    private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Rol rolActivo = loginInfo.getRolActivo();
        Oficina oficinaActiva = loginInfo.getOficinaActiva();
        Entidad entidadActiva = loginInfo.getEntidadActiva();
        String url = request.getServletPath();

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone de una EntidadActiva
        if(entidadActiva == null){
            log.info("No existe una EntidadActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
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


        // Comprobaciones previas al listado de RegistroEntrada
        if(url.equals("/registroEntrada/list")){

            if(loginInfo.getOrganismosConsultaEntrada().size() == 0){
                log.info("Aviso: No hay ningún organismo con permisos para consultar registros");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas a la consulta de RegistroEntrada
        if(url.contains("detalle")){

            String idRegistroEntrada =  url.replace("/registroEntrada/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

            Organismo organismo = registroEntradaConsultaEjb.getOrganismo(Long.valueOf(idRegistroEntrada));

            if(organismo != null){
                if(!loginInfo.getOrganismosConsultaEntrada().contains(organismo)){
                    log.info("Aviso: No tiene permisos para consultar este registro");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }else{ // Id de Organismo invalido
                log.info("Aviso: El organismo no existe");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.libro.noExiste"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobamos si en la OficinaActiva el usuario puede registrar
        if(url.equals("/registroEntrada/new") || url.equals("/registroEntrada/reserva")){

            if(!loginInfo.getOficinasRegistroEntrada().contains(oficinaActiva)){
                log.info("Aviso: No tiene permisos para registrar en el Organismo: " + oficinaActiva.getOrganismoResponsable().getDenominacion());
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.permisos"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        return true;
    }

}
