package es.caib.regweb3.persistence.ejb;


import es.caib.carpeta.apiinterna.client.api.NotificacionsApi;
import es.caib.carpeta.apiinterna.client.model.SendMessageResult;
import es.caib.carpeta.apiinterna.client.services.ApiClient;
import es.caib.carpeta.apiinterna.client.services.ApiException;
import es.caib.carpeta.apiinterna.client.services.Configuration;
import es.caib.carpeta.apiinterna.client.services.auth.HttpBasicAuth;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



/**
 * Created by DGMAD
 * @author earrivi
 * Date: 22/02/2023
 */

@Stateless(name = "CarpetaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class CarpetaBean implements CarpetaLocal {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @EJB private IntegracionLocal integracionEjb;


    @Override
    @Asynchronous
    public void enviarNotificacionCarpeta(IRegistro registro, Long idEntidad) throws I18NException {

        // Comprobamos si la Entidad tiene activado el envío de notificaciones a Carpeta
        if(!PropiedadGlobalUtil.getCarpetaEnviarMensajes(idEntidad)){
            return;
        }

        // Integración
        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();
        String descripcion = "Enviar notificación a carpeta";

        NotificacionsApi apiInstance = getApiInstance(idEntidad);

        String notificationCode = PropiedadGlobalUtil.getCarpetaNotificationCode(idEntidad); // String | Codi de la notificació. Demanar a l'administrador de Carpeta.
        List<String> notificationParameters = Arrays.asList(registro.getNumeroRegistroFormateado()); // List<String> | Paràmetres associats al Codi de la notificació
        String notificationLang = "ca"; // String | Idioma en que s'enviaran les notificacions
        String langError = "ca"; // String | Idioma en que s'enviaran els missatges d'error

        // Integración
        peticion.append("notificationCode: ").append(notificationCode).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        peticion.append("documento: ").append(registro.getRegistroDetalle().getDocumentoInteresado()).append(System.getProperty("line.separator"));

        try {
            SendMessageResult result = apiInstance.sendNotificationToMobile(registro.getRegistroDetalle().getDocumentoInteresado(), notificationCode, notificationParameters, notificationLang, langError);
            peticion.append("ResultCode: ").append(result.getCode()).append(System.getProperty("line.separator"));
            if(StringUtils.isNotEmpty(result.getMessage())){
                peticion.append("ResultMessage: ").append(result.getMessage()).append(System.getProperty("line.separator"));
            }

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CARPETA, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), idEntidad, registro.getNumeroRegistroFormateado());
        } catch (ApiException e) {
            log.info("Exception when calling NotificacionsApi#help");
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CARPETA, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), idEntidad, registro.getNumeroRegistroFormateado());

        }

    }

    private NotificacionsApi getApiInstance(Long idEntidad) throws I18NException {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(PropiedadGlobalUtil.getCarpetaServer(idEntidad));
        // Configure HTTP basic authorization: BasicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
        basicAuth.setUsername(PropiedadGlobalUtil.getCarpetaUsername(idEntidad));
        basicAuth.setPassword(PropiedadGlobalUtil.getCarpetaPassword(idEntidad));

        return new NotificacionsApi();

    }
}
