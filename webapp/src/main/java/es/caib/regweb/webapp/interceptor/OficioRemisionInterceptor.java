package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

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

    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb/ModeloOficioRemisionEJB/local")
    public ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // long start = System.currentTimeMillis();
        try {

        String url = request.getServletPath();
        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        //Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }

        // Comprobamos existe al menos un modelo de oficio de remisión
        List<ModeloOficioRemision> modelos = modeloOficioRemisionEjb.getByEntidad(entidadActiva.getId());

        if(modelos.size() == 0){
            log.info("No existen ModelosOficioRemision");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.modeloOficioRemision"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }

        // Comprobaciones previas a la consulta de un OficioRemision
        if(url.contains("detalle")){
            String idOficioRemision =  url.replace("/oficioRemision/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

            log.info("idOficioRemision: " + idOficioRemision);
            OficioRemision oficioRemision = oficioRemisionEjb.findById(Long.valueOf(idOficioRemision));

            if(!oficioRemision.getOficina().equals(oficinaActiva)){ // Si no es la Oficina Activa no se puede consultar
                log.info("Este OficioRemision no se puede consultar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.detalle"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        // Comprobaciones previas a procesar un Oficio de Remisión
        if(url.contains("procesar")){
            String idOficioRemision =  url.replace("/oficioRemision/","").replace("/procesar", ""); //Obtenemos el id a partir de la url

            OficioRemision oficioRemision = oficioRemisionEjb.findById(Long.valueOf(idOficioRemision));

            if(!oficinaActiva.getOrganismoResponsable().getId().equals(oficioRemision.getOrganismoDestinatario().getId())){
                log.info("Este RegistroEntrada no se puede procesar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.procesar"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        return true;
        } finally {
          
          //log.info("Interceptor Oficio Remision : " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}
