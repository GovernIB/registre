package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.MensajeControl;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface SirEnvioLocal {


    /**
     * Crear un intercambio SIR entrada, preparado par enviarse
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada crearIntercambioEntrada(RegistroEntrada registroEntrada, Entidad entidad,Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Crear un intercambio SIR salida, preparado par enviarse
     * @param registroSalida
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida crearIntercambioSalida(RegistroSalida registroSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Envia un intercambio a la oficina destino
     * @param tipoRegistro
     * @param registro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    OficioRemision enviarIntercambio(Long tipoRegistro, IRegistro registro, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Reenvia un intercambio, cuando este ha sido RECHAZADO O REENVIADO
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaReenvio
     * @param oficinaActiva
     * @param usuario
     * @param observaciones
     * @throws Exception
     */
    void reenviarIntercambio(Long tipoRegistro, Long idRegistro, Entidad entidad, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException, I18NValidationException;

    /**
     * Vuelve a enviar un intercambio que ya había sido enviado previamente
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    void reenviarIntercambio(OficioRemision oficioRemision)throws Exception, I18NException;

    /**
     * Envía un mensaje ACK a partir de los datos de un RegistroSir
     *
     * @param idRegistroSir
     * @throws Exception
     */
    Boolean enviarACK(Long idRegistroSir) throws Exception;

    /**
     * Envía un mensaje de confirmación a partir de los datos de un RegistroSir ACEPTADO
     *
     * @param idRegistroSir
     * @throws Exception
     */
    Boolean enviarConfirmacion(Long idRegistroSir) throws Exception;

    /**
     * Reenvía un mensaje de control que ya ha sido enviado previamente
     *
     * @param mensaje
     * @return
     * @throws Exception
     */
    Boolean reenviarMensaje(MensajeControl mensaje) throws Exception;

    /**
     * Renintenta los envíos a SIR sin ACK.
     *
     * @param entidad
     * @throws Exception
     */
    void reintentarIntercambiosSinAck(Entidad entidad) throws Exception;

    /**
     * Reintenta los Reenvios/Rechazos sin ACK
     * @param entidad
     * @throws Exception
     */
    void reintentarReenviosRechazosSinAck(Entidad entidad) throws Exception;

    /**
     * Renintenta los intercambios con ERROR a SIR sin ACK
     *
     * @param entidad
     * @throws Exception
     */
    void reintentarIntercambiosConError(Entidad entidad) throws Exception;

    /**
     * Renintenta los Reenvios/Rechazos con ERROR a SIR sin ACK
     *
     * @param entidad
     * @throws Exception
     */
    void reintentarReenviosRechazosConError(Entidad entidad) throws Exception;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param camposNTIs
     * @return
     */
    RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino, Boolean distribuir) throws Exception, I18NException, I18NValidationException;


    /**
     * Reenvio de un Registro SIR
     *
     * @param registroSir
     * @throws Exception
     */
    void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

    /**
     * Método que indica si el RegistroSir puede ser reenviado en función de su estado.
     *
     * @param estado del RegistroSir
     * @return
     */
    boolean puedeReenviarRegistroSir(EstadoRegistroSir estado);

    /**
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

    Integer aceptarRegistrosERTE(List<Long> registros, Entidad entidad, String destino, Oficina oficina,Long idLibro, UsuarioEntidad usuarioEntidad) throws Exception;

    Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws Exception;

}

