package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PermisoLibroUsuarioLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;

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
public class RegistroEntradaInterceptor extends AbstractRegistroCommonInterceptor {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        super.preHandle(request,response,handler);

        String url = request.getServletPath();
        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);
        UsuarioEntidad usuarioEntidad = (UsuarioEntidad)session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);

        // Comprobaciones previas al listado de RegistroEntrada
        if(url.equals("/registroEntrada/list")){

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para consultar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas a la consulta de RegistroEntrada
        if(url.contains("detalle")){

            String idRegistroEntrada =  url.replace("/registroEntrada/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

            Long idLibro = registroEntradaEjb.getLibro(Long.valueOf(idRegistroEntrada));

            if(idLibro != null){
                if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),idLibro,RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA)){
                    log.info("Aviso: No tiene permisos para consultar este registro");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }else{ // Id de Registro invalido
                log.info("Aviso: No tiene permisos para consultar este registro");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.libro.noExiste"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

        }

        // Comprobaciones previas al registro de un RegistroEntrada
        if(url.equals("/registroEntrada/new") || url.equals("/registroEntrada/reserva")){

            //Comprobamos que se haya definido un formato para el número de registro en la Entidad
            if(entidadActiva.getNumRegistro() == null || entidadActiva.getNumRegistro().length()==0){
                log.info("No hay configurado el formato del numero de registro para la Entidad activa");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para registrar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro"));
                response.sendRedirect("/regweb3/aviso");
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
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos si se la oficina activa es la misma donde se creó el registro
            if(!registroEntrada.getOficina().getId().equals(oficinaActiva.getId()) && (registroEntrada.getOficina().getOficinaResponsable() != null && !registroEntrada.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()))){
                log.info("Aviso: No puede editar un registro si no se encuentra en la oficina donde se creó");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar.oficina"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos que el Registro de Entrada es válido para editarse
            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.ESTADO_PENDIENTE);
            estados.add(RegwebConstantes.ESTADO_VALIDO);

            if(!estados.contains(registroEntrada.getEstado())){
                log.info("Este RegistroEntrada no se puede modificar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        return true;
    }

}
