package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;

import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
public interface WebServicesMethodsLocal {

    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception;

    public Archivo persistArchivo(Archivo archivo) throws Exception;

    public void removeArchivo(Archivo archivo) throws Exception;
}
