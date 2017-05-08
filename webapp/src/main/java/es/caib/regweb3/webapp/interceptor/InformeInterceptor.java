package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para los Informes
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class InformeInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            List<Libro> librosAdm = (List<Libro>) session.getAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS);

            if(librosAdm != null){
                if(librosAdm.size() == 0){
                    librosAdm = null;
                }
            }

            // Comprobamos que el usuario dispone del Rol RWE_ADMIN o tiene Libros Administrados
            if(url.equals("/informe/registroLopd")||url.equals("/informe/usuarioLopd")) {
                if (!(librosAdm != null || rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Comprobamos que el usuario dispone del RWE_ADMIN
            if(url.equals("/informe/indicadores")) {
                if (!(librosAdm != null || rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Comprobamos que el usuario dispone del RWE_ADMIN o RWE_USUARI
            if(url.equals("/informe/libroRegistro")) {
                if (!(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN) || rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Informe Registro Lopd
            if(url.contains("informeRegistroLopd")){
                String subUrl =  url.replace("/informe/","").replace("/informeRegistroLopd", ""); //Obtenemos el id a partir de la url
                StringTokenizer tokens = new StringTokenizer(subUrl,"/");
                String idRegistro = tokens.nextToken();
                String tipoRegistro = tokens.nextToken();

                if (!(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN) || rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

                // Comprueba que el Tipo de Registro existe
                if(!tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA.toString()) && !tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA.toString())){
                    log.info("No existe el tipo registro");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.tipoRegistro.noExiste"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

            }

            return true;
        } finally {
            //log.info("Interceptor Informe: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}