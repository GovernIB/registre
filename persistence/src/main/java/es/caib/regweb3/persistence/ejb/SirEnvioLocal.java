package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface SirEnvioLocal {

    /**
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro,
                                            Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Renintenta los envíos a SIR que pendientes de llegar a destino.
     * @param idEntidad
     * @throws Exception
     */
    void reintentarEnviosSinConfirmacion(Long idEntidad) throws Exception;

    /**
     * Renintenta los envíos con ERROR a SIR que pendientes de llegar a destino.
     * @param idEntidad
     * @throws Exception
     */
    void reintentarEnviosConError(Long idEntidad) throws Exception;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     */
    RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;


    /**
     *
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaReenvio
     * @param oficinaActiva
     * @param usuario
     * @param observaciones
     * @throws Exception
     */
    void reenviarRegistro(String tipoRegistro, Long idRegistro, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException, I18NValidationException;

    /**
     * Reenvio de un Registro SIR
     * @param registroSir
     * @throws Exception
     */
    void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

    /**
     * Método que indica si el RegistroSir puede ser reenviado en función de su estado.
     * @param estado del RegistroSir
     * @return
     */
    boolean puedeReenviarRegistroSir(EstadoRegistroSir estado);

    /**
     *
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

}

