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
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);


        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
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

        // Comprobaciones previas al listado de OficioRemision y Oficios de Remisión Pendientes
        if (url.equals("/oficioRemision/list") || url.contains("oficiosPendientes")) {

            Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();
                UsuarioEntidad usuarioEntidad = (UsuarioEntidad) session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
                List<Libro> libros = permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA);

            // Mira que el usuario tiene permisos de Registro de Entrada
            if (libros.size() == 0) {
                log.info("Aviso: No tiene permisos para consultar Oficios de Remision");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.list"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }


            // Comprobaciones previas a la consulta de un OficioRemision
        if(url.contains("detalle")){
            String idOficioRemision =  url.replace("/oficioRemision/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

            OficioRemision oficioRemision = oficioRemisionEjb.findById(Long.valueOf(idOficioRemision));

            if(!oficioRemision.getOficina().equals(oficinaActiva)){ // Si no es la Oficina Activa no se puede consultar
                log.info("Este OficioRemision no se puede consultar: No se encuentra en la Oficina donde se genero");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.oficinaActiva"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas a procesar un Oficio de Remisión
        if(url.contains("procesar")){
            String idOficioRemision =  url.replace("/oficioRemision/","").replace("/procesar", ""); //Obtenemos el id a partir de la url

            OficioRemision oficioRemision = oficioRemisionEjb.findById(Long.valueOf(idOficioRemision));

            Set<Organismo> organismosOficinaActiva = (Set<Organismo>) session.getAttribute(RegwebConstantes.SESSION_ORGANISMOS_OFICINA);

            if (!organismosOficinaActiva.contains(oficioRemision.getOrganismoDestinatario()) ||
                    oficioRemision.getEstado() != RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO) {

                log.info("Este RegistroEntrada no se puede procesar");
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
