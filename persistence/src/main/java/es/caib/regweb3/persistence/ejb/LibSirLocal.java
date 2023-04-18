package es.caib.regweb3.persistence.ejb;

import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by DGMAD
 *
 * @author earrivi
 * Date: 21/10/22
 */
@Local
public interface LibSirLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/LibSirEJB";

    /**
     *
     * @param registro
     * @param firmaRegistro
     */
    void recibirAsiento(String registro, String firmaRegistro) throws InterException;

    /**
     * @param mensaje
     * @param firma
     * @throws InterException
     */
    void recibirMensajeControl(String mensaje, String firma) throws InterException;


    /**
     * @param maxResults
     * @throws InterException
     */
    List<AsientoBean> consultaAsientosPendientes(int maxResults) throws InterException;

    AsientoBean consultaAsiento(String oficina, String cdIntercambio) throws InterException;

    /**
     * @param cdIntercambio
     * @param idFichero
     * @return
     * @throws InterException
     */
    byte[] obtenerAnexoReferencia(String cdIntercambio, String idFichero) throws InterException;

   /* Anexo obtenerAnexoReferencia2(String cdIntercambio, String idFichero) throws InterException;

    // pruebas
    byte[] obtenerAnexoReferenciaContenido(Long cdAnexo) throws InterException;*/

    void enviarAsiento(AsientoBean asientoBean) throws InterException;
}
