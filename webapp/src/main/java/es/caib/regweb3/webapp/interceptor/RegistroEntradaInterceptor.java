package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
public class RegistroEntradaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);
        UsuarioEntidad usuarioEntidad = (UsuarioEntidad)session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
        String url = request.getServletPath();

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
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

        //Comprobamos la existencia de plugins necesarios para el funcionamiento de la aplicación
        try {
            //Plugin Generación Justificante
            if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE)){
                log.info("No existe el plugin de generación del justificante");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginjustificante"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
            //Plugin Custodia Justificante
            if (!pluginEjb.existPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE)) {
                log.info("No existe el plugin de custodia del justificante");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                response.sendRedirect("/regweb3/aviso");
                return false;

            }
            //Plugin Custodia
            if (!pluginEjb.existPlugin(null, RegwebConstantes.PLUGIN_CUSTODIA)) {
                log.info("No existe el plugin de custodia");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodia"));
                response.sendRedirect("/regweb3/aviso");
                return false;

            }
            //Plugin Firma en servidor
            if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_FIRMA_SERVIDOR)) {
                log.info("No existe el plugin de firma en servidor");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginfirma"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }  catch (I18NException i18ne) {
            throw new Exception(i18ne);
        }

        if(url.equals("/registroEntrada/list") || url.contains("detalle") || url.equals("/registroEntrada/new")){
            if(PropiedadGlobalUtil.getDir3CaibServer() == null || PropiedadGlobalUtil.getDir3CaibServer().isEmpty()){
                log.info("La propiedad Dir3CaibServer no está definida");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibserver"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
            if(PropiedadGlobalUtil.getDir3CaibUsername() == null || PropiedadGlobalUtil.getDir3CaibUsername().isEmpty()){
                log.info("La propiedad Dir3CaibUsername no está definida");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibusername"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
            if(PropiedadGlobalUtil.getDir3CaibPassword() == null || PropiedadGlobalUtil.getDir3CaibPassword().isEmpty()){
                log.info("La propiedad Dir3CaibPassword no está definida");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibpassword"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }



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

            // Comprobamos si el usuario dispone de algún libro donde registrar
            Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();
            if (permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA).size() == 0) {
                log.info("Aviso: No hay ningún libro con permisos para registrar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.permisos"));
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

        // Comprobaciones previas a la edición de un RegistroEntrada
        if(url.contains("edit") && request.getMethod().equals("GET")){
       
            String idRegistroEntrada =  url.replace("/registroEntrada/","").replace("/edit", ""); //Obtenemos el id a partir de la url

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(Long.valueOf(idRegistroEntrada));

            // Comprobamos que el Registro de Entrada es válido para editarse
            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.REGISTRO_RESERVA);
            estados.add(RegwebConstantes.REGISTRO_VALIDO);

            if (!estados.contains(registroEntrada.getEstado())) {
                log.info("Este RegistroEntrada no se puede modificar");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Si tiene Justificante generado, no se puede editar
            if (registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                log.info("Este RegistroEntrada no se puede modificar, porque ya se ha generado su Justificante");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.modificar.justificante"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

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

        }

        // Comprobaciones previas al reenvio
        if(url.contains("reenviar")){
            String idRegistroEntrada =  url.replace("/registroEntrada/","").replace("/reenviar", ""); //Obtenemos el id a partir de la url

            RegistroBasico registroEntrada = registroEntradaEjb.findByIdLigero(Long.valueOf(idRegistroEntrada));

            // Comprobamos que está Rechazado
            if(!(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RECHAZADO) || registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_REENVIADO))){
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.reenvioSir"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }


        return true;
    }

}
