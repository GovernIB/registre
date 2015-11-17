package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PermisoLibroUsuarioLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
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
 * Interceptor para el Registro de Salida
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class RegistroSalidaInterceptor extends AbstractRegistroCommonInterceptor {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        super.preHandle(request,response,handler);

        String url = request.getServletPath();
        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);


        //todo Añadir comprobación de que todas las configuraciones de la entidad están realizadas.
        //Comprobamos que se haya definido un formato para el número de registro en la Entidad
        if(entidadActiva.getNumRegistro() == null || entidadActiva.getNumRegistro().length()==0){
            log.info("No hay configurado el formato del numero de registro para la Entidad activa");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        UsuarioEntidad usuarioEntidad = (UsuarioEntidad)session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);

        // Comprobaciones previas al listado de RegistroSalida
        if(url.equals("/registroSalida/list")){

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para consultar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.consultaRegistro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas a la consulta de RegistroSalida
        if(url.contains("detalle")){

            String idRegistroSalida =  url.replace("/registroSalida/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

            Long idLibro = registroSalidaEjb.getLibro(Long.valueOf(idRegistroSalida));

            if(idLibro != null){
                if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),idLibro,RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA)){
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

        // Comprobaciones previas al registro de un RegistrSalida
        if(url.equals("/registroSalida/new")){

            //Comprobamos que se haya definido un formato para el número de registro en la Entidad
            if(entidadActiva.getNumRegistro() == null || entidadActiva.getNumRegistro().length()==0){
                log.info("No hay configurado el formato del numero de registro para la Entidad activa");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            if(permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA).size() == 0){
                log.info("Aviso: No hay ningún libro con permisos para registrar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            if(tipoDocumentalEjb.getByEntidad(entidadActiva.getId()).size()==0){
                log.info("Aviso: No hay ningún Tipo Documental para la Entidad Activa");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.tipoDocumental"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        // Comprobaciones previas a la edición de un RegistroSalida
        if(url.contains("edit")){
            String idRegistroSalida =  url.replace("/registroSalida/","").replace("/edit", ""); //Obtenemos el id a partir de la url

            RegistroSalida registroSalida = registroSalidaEjb.findById(Long.valueOf(idRegistroSalida));

            // Comprobamos que el UsuarioActivo pueda editar ese registro de salida
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroSalida.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos si se la oficina activa es la misma donde se creó el registro
            if(!registroSalida.getOficina().getId().equals(oficinaActiva.getId()) && (registroSalida.getOficina().getOficinaResponsable() != null && !registroSalida.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()))){
                log.info("Aviso: No puede editar un registro si no se encuentra en la oficina donde se creó");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.editar.oficina"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos que el Registro de Salida es válido para editarse
            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.ESTADO_PENDIENTE);
            estados.add(RegwebConstantes.ESTADO_VALIDO);

            if(!estados.contains(registroSalida.getEstado())){
                log.info("Este Registro no se puede modificar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }

        return true;

    }

}
