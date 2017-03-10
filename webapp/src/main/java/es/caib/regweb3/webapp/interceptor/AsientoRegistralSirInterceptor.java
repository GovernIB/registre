package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.AsientoRegistralSirLocal;
import es.caib.regweb3.persistence.ejb.PermisoLibroUsuarioLocal;
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

/**
 * Created by Fundació BIT.
 *
 * Interceptor para el AsientoRegistralSir
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class AsientoRegistralSirInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/AsientoRegistralSirEJB/local")
    public AsientoRegistralSirLocal asientoRegistralSirEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);

            // Comprobamos que la oficinaActiva esté integrada en SIR
            if(!oficinaActiva.getSir()){
                log.info("La oficinaActiva no está integrada en SIR");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva.sir"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos que el usuario dispone del Rol RWE_USUARI
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobaciones previas al detalle de un AsientoRegistralSir
            if(url.contains("detalle")){

                String idPreRegistro =  url.replace("/asientoRegistralSir/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

                /*AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(Long.valueOf(idPreRegistro));

                // Comprobamos que el PreRegistro existe
                if(asientoRegistralSir == null){
                    log.info("Aviso: No existeix aquest asientoRegistralSir");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.asientoRegistralSir.detalle"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

                // Comprobamos que el PreRegistro tiene como destino nuestra Oficina Activa
                if(!asientoRegistralSir.getCodigoEntidadRegistralDestino().equals(oficinaActiva.getCodigo())){
                    log.info("Aviso: No és d'aquesta oficina");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.asientoRegistralSir.detalle"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }*/

            }

            return true;
        } finally {
            //log.info("Interceptor PreRegistro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}