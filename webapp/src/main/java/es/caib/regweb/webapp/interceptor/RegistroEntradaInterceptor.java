package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.PermisoLibroUsuarioLocal;
import es.caib.regweb.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para el Registro de Entrada
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class RegistroEntradaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String url = request.getServletPath();
        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);


        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
          log.info("Error de rol");
          Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
          response.sendRedirect("/regweb/aviso");
          return false;
        }

        // Comprobamos que el usuario dispone del una EntidadActiva
        if(entidadActiva == null){
            log.info("No existe una EntidadActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone del una OficinaActiva
        if(oficinaActiva == null){
            log.info("No existe una OficinaActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }


        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(usuarioAutenticado.getId(), entidadActiva.getId());

        // Comprobaciones previas al listado de RegistroEntrada
        if(url.equals("/registroEntrada/list")){

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para consultar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        // Comprobaciones previas al registro de un RegistroEntrada
        if(url.equals("/registroEntrada/new") || url.equals("/registroEntrada/reserva")){

            //Comprobamos que se haya definido un formato para el número de registro en la Entidad
            if(entidadActiva.getNumRegistro() == null || entidadActiva.getNumRegistro().length()==0){
                log.info("No hay configurado el formato del numero de registro para la Entidad activa");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para registrar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        // Comprobaciones previas a la edición de un RegistroEntrada
        if(url.contains("edit")){
            String idRegistroEntrada =  url.replace("/registroEntrada/","").replace("/edit", ""); //Obtenemos el id a partir de la url

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(Long.valueOf(idRegistroEntrada));

            // Comprobamos que el UsuarioActivo pueda editar ese registro de entrada
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

            // Comprobamos si se la oficina activa es la misma donde se creó el registro
            if(!registroEntrada.getOficina().getId().equals(oficinaActiva.getId())){
                log.info("Aviso: No puede editar un registro si no se encuentra en la oficina donde se creó");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar.oficina"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

            // Comprobamos que el Registro de Entrada es válido para editarse
            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.ESTADO_PENDIENTE);
            estados.add(RegwebConstantes.ESTADO_VALIDO);

            if(!estados.contains(registroEntrada.getEstado())){
                log.info("Este RegistroEntrada no se puede modificar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        return true;
    }

}
