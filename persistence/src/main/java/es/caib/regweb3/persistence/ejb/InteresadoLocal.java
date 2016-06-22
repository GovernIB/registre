package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface InteresadoLocal extends BaseEjb<Interesado, Long> {


    /**
     * Obtiene un Interesado a partir del CodigoDir3 y Su registroDetalle
     * @param codigoDir3
     * @param registroDetalle
     * @return
     * @throws Exception
     */
    public Interesado findByCodigoDir3RegistroDetalle(String codigoDir3, Long registroDetalle) throws Exception;

    /**
     * Eliminanos el Interesado del RegistroDetalle y luego de la bbdd
     * @param idInteresado
     * @param idRegistroDetalle
     * @throws Exception
     */
    public void eliminarInteresadoRegistroDetalle(Long idInteresado, Long idRegistroDetalle) throws Exception;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     * @param documento
     * @return
     * @throws Exception
     */
    public Boolean existeDocumentoNew(String documento) throws Exception;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de un Interesado
     * @param documento
     * @param idInteresado
     * @return
     * @throws Exception
     */
    public Boolean existeDocumentoEdit(String documento, Long idInteresado) throws Exception;

    /**
     * Guardamos todos los Interesados de un Registro de Entrada o Salida
     * @param interesadosSesion
     * @param registroDetalle
     * @return
     * @throws Exception
     */
    public List<Interesado> guardarInteresados(List<Interesado> interesadosSesion, RegistroDetalle registroDetalle) throws Exception;
}
