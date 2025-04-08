package es.caib.regweb3.integraciones.notib;

import es.caib.regweb3.integraciones.rest.GenericRestClient;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotibService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GenericRestClient restClient;

    /**
     * Envío de un callback a NOTIB avisándo de la confirmación de una Comunicación SIR
     * @param registroSalida
     * @param urlNotib
     * @throws I18NException
     */
    public void callbackComunicacionSir(RegistroSalida registroSalida, String urlNotib) throws I18NException {

        try {

            // Crear un objeto JSON con los parámetros
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("registreNumero", registroSalida.getNumeroRegistroFormateado());
            requestBody.put("entitatDir3Codi", registroSalida.getEntidad().getCodigoDir3());

            // Llamar al cliente REST genérico
            String respuesta = restClient.postRequest(urlNotib, requestBody, String.class);
            log.info("Respuesta NOTIB: " + respuesta);


        } catch (Exception e) {
            throw new I18NException(e, "notib.callback.error", registroSalida.getNumeroRegistroFormateado());
        }
    }
}
