package es.caib.regweb3.sir.ws.api.service;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.service.ServicioIntercambioRegistral;
import es.caib.regweb3.sir.ws.api.manager.EmisionManager;
import es.caib.regweb3.sir.ws.api.manager.RecepcionManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * Created by earrivi on 14/01/2016.
 */
@Service
public class ServicioIntercambioRegistralImpl implements ServicioIntercambioRegistral {

    public final Logger log = Logger.getLogger(getClass());

    public RecepcionManager recepcionManager = new RecepcionManager();
    public EmisionManager emisionManager = new EmisionManager();

    /**
     *
     * @param xmlFicheroIntercambio
     * @param webServicesMethodsEjb
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        try {

        recepcionManager.recibirFicheroIntercambio(xmlFicheroIntercambio, webServicesMethodsEjb);

        } catch (IllegalArgumentException e) {
            log.error("Error en la recepción del fichero de datos de intercambio", e);
            throw new ServiceException(Errores.ERROR_0065, e);
        } catch (ValidacionException e) {
            log.error("Error validacion en la recepción del fichero de datos de intercambio");
            throw new ServiceException(e.getErrorValidacion(), e.getErrorException());
        }
    }

    /**
     *
     * @param xmlMensaje
     */
    public void recibirMensaje(String xmlMensaje){

        try{
            recepcionManager.recibirMensaje(xmlMensaje);

        }catch (ValidacionException e) {
            log.error("Error validacion en la recepción del mensaje de control");
            throw new ServiceException(e.getErrorValidacion(), e.getErrorException());
        }

    }

    @Override
    public void enviarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir){

        emisionManager.enviarFicheroIntercambio(asientoRegistralSir);
    }

}
