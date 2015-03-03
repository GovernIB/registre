package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Descarga;
import es.caib.regweb.persistence.ejb.DescargaLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Organismos
 *
 * @author earrivi
 * Date: 3/03/15
 */
public class OrganismoInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Comprobamos que el catalogo ha sido sincronizado al menos una vez
        Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
        if(catalogo == null){
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }

        return true;


    }


}
