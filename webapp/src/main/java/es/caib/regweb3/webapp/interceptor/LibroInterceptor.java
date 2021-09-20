package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.LibroLocal;
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
import java.util.StringTokenizer;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Libros
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class LibroInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //final long start = System.currentTimeMillis();
      try {
          String url = request.getServletPath();
          HttpSession session = request.getSession();
          LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
          Rol rolActivo = loginInfo.getRolActivo();
          Entidad entidadActiva = loginInfo.getEntidadActiva();


          // Comprobamos que el usuario dispone del Rol RWE_ADMIN
          if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
              log.info("Error de rol");
              Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
              response.sendRedirect("/regweb3/aviso");
              return false;
          }

          // Borrar un libro
          if(url.contains("delete")){
              String subUrl =  url.replace("/libro/","").replace("/delete", ""); //Obtenemos el id a partir de la url
              StringTokenizer tokens = new StringTokenizer(subUrl,"/");
              String idLibro = tokens.nextToken();

              // Comprobamos que el usuario es Administrador de Entidad de la entidad del libro
              Libro libro = libroEjb.findById(Long.valueOf(idLibro));
              if(libro!=null){
                  Long idEntidadLibro = libro.getOrganismo().getEntidad().getId();
                  if (!idEntidadLibro.equals(entidadActiva.getId())) {
                      log.info("L'usuari no pot esborrar aquest llibre");
                      Mensaje.saveMessageAviso(request, I18NUtils.tradueix("error.libro.usuario"));
                      response.sendRedirect("/regweb3/aviso");
                      return false;
                  }
              }else{
                  log.info("Aquest llibre no existeix");
                  Mensaje.saveMessageAviso(request, I18NUtils.tradueix("error.libro.noExiste"));
                  response.sendRedirect("/regweb3/aviso");
                  return false;
              }
          }

        return true;
      } finally {
        //log.info("Interceptor Libro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
      }
    }


}
