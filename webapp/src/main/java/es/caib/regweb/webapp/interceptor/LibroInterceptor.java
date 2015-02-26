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

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Libros
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class LibroInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());


    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //final long start = System.currentTimeMillis();
      try {
        String url = request.getServletPath();
        String idOrganismo =  null;

        // Nuevo libro de un organismos
        if(url.contains("new")){
            idOrganismo =  url.replace("/libro/","").replace("/new", ""); //Obtenemos el id a partir de la url
            Organismo organismo = organismoEjb.findById(Long.valueOf(idOrganismo));

            Set<Oficina> oficinas = new HashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados
            oficinas.addAll(oficinaEjb.findByOrganismoResponsable(organismo.getId()));
            oficinas.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(organismo.getId()));
            if(oficinas.size() == 0){
                log.info("El organismo no tiene Oficinas");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.organismo.oficinas"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

        }

        // Listado de libros de un organismos
        if(url.contains("libros")){
            idOrganismo =  url.replace("/libro/","").replace("/libros", ""); //Obtenemos el id a partir de la url
            Organismo organismo = organismoEjb.findById(Long.valueOf(idOrganismo));

            if(!organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){

                log.info("El Organismo no está vigente");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.organismo.vigente"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }

        return true;
      } finally {
        //log.info("Interceptor Libro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
      }
    }


}
