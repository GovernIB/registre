package es.caib.regweb3.sir.ws.manager;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.ws.api.service.ServicioIntercambioRegistral;
import org.apache.log4j.Logger;


/**
 * Created by earrivi on 14/01/2016.
 */
public class EnvioManager {

    public final Logger log = Logger.getLogger(getClass());

    ServicioIntercambioRegistral servicioIntercambioRegistral = new ServicioIntercambioRegistral();

    public void envioFichero(String ficheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        try {

            // Recibir asiento registral
            servicioIntercambioRegistral.recibirFicheroIntercambio(ficheroIntercambio, webServicesMethodsEjb);

        } catch (IllegalArgumentException e) {
            log.error("Error en la recepción del fichero de datos de intercambio", e);
            throw new ServiceException(Errores.ERROR_0065, e);
        } catch (ValidacionException e) {
            log.error("Error validacion en la recepción del fichero de datos de intercambio");
            throw new ServiceException(e.getErrorValidacion(), e.getErrorException());
        }

    }
}
