package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.PermisoLibroUsuarioLocal;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RegistroSirLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
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

/**
 * Created by Fundació BIT.
 *
 * Interceptor para el RegistroSir
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class RegistroSirInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    public RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);
            Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

            // Comprobamos que la oficinaActiva esté integrada en SIR
            if(!oficinaActiva.getSirEnvio() || !oficinaActiva.getSirRecepcion()){
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
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE)) {
                    log.info("No existe el plugin de custodia del justificante");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;

                }
                //Plugin Custodia
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA)) {
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

            //comprobar variable archivos path
            if(FileSystemManager.getArchivosPath()==null){
                log.info("Error, no esta definida la variable archivos path");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobaciones previas al detalle de un RegistroSir
            if(url.contains("detalle")){

                String idPreRegistro =  url.replace("/registroSir/","").replace("/detalle", ""); //Obtenemos el id a partir de la url

                /*RegistroSir registroSir = registroSirEjb.findById(Long.valueOf(idPreRegistro));

                // Comprobamos que el PreRegistro existe
                if(registroSir == null){
                    log.info("Aviso: No existeix aquest registroSir");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroSir.detalle"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

                // Comprobamos que el PreRegistro tiene como destino nuestra Oficina Activa
                if(!registroSir.getCodigoEntidadRegistralDestino().equals(oficinaActiva.getCodigo())){
                    log.info("Aviso: No és d'aquesta oficina");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registroSir.detalle"));
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