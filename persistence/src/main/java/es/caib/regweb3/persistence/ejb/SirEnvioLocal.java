package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
public interface SirEnvioLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/SirEnvioEJB";


    /**
     * Crear un intercambio SIR entrada, preparado par enviarse
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada crearIntercambioEntrada(RegistroEntrada registroEntrada, Entidad entidad,Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws I18NException, I18NValidationException;

    /**
     * Crear un intercambio SIR salida, preparado par enviarse
     * @param registroSalida
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSalida crearIntercambioSalida(RegistroSalida registroSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws I18NException, I18NValidationException;

    /**
     * Envia un intercambio a la oficina destino
     * @param tipoRegistro
     * @param registro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws I18NException
     * @throws I18NException
     */
    OficioRemision enviarIntercambio(Long tipoRegistro, IRegistro registro, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws I18NException, I18NValidationException;

    /**
     * Reenvia un intercambio, cuando este ha sido RECHAZADO O REENVIADO
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaReenvio
     * @param oficinaActiva
     * @param usuario
     * @param observaciones
     * @throws I18NException
     */
    void reenviarIntercambio(Long tipoRegistro, Long idRegistro, Entidad entidad, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws I18NException, I18NValidationException;

    /**
     * Vuelve a enviar un intercambio que ya había sido enviado previamente
     * @param oficioRemision
     * @return
     * @throws I18NException
     */
    void reenviarIntercambio(OficioRemision oficioRemision)throws I18NException;

    /**
     * Envía un mensaje ACK a partir de los datos de un RegistroSir
     *
     * @param idRegistroSir
     * @throws I18NException
     */
    Boolean enviarACK(Long idRegistroSir) throws I18NException;

    /**
     * Envía un mensaje de confirmación a partir de los datos de un RegistroSir ACEPTADO
     *
     * @param idRegistroSir
     * @throws I18NException
     */
    Boolean enviarConfirmacion(Long idRegistroSir) throws I18NException;

    /**
     * Reenvía un mensaje de control que ya ha sido enviado previamente
     *
     * @param mensaje
     * @return
     * @throws I18NException
     */
    Boolean reenviarMensaje(MensajeControl mensaje) throws I18NException;

    /**
     * Renintenta los envíos a SIR sin ACK.
     *
     * @param entidad
     * @throws I18NException
     */
    void reintentarIntercambiosSinAck(Entidad entidad) throws I18NException;

    /**
     * Reintenta los Reenvios/Rechazos sin ACK
     * @param entidad
     * @throws I18NException
     */
    void reintentarReenviosRechazosSinAck(Entidad entidad) throws I18NException;

    /**
     * Renintenta los intercambios con ERROR a SIR sin ACK
     *
     * @param entidad
     * @throws I18NException
     */
    void reintentarIntercambiosConError(Entidad entidad) throws I18NException;

    /**
     * Renintenta los Reenvios/Rechazos con ERROR a SIR sin ACK
     *
     * @param entidad
     * @throws I18NException
     */
    void reintentarReenviosRechazosConError(Entidad entidad) throws I18NException;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param camposNTIs
     * @return
     */
    RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino, Boolean distribuir, Long codigoSia, String emails, String motivo) throws I18NException, I18NValidationException;


    /**
     * Reenvio de un Registro SIR
     *
     * @param registroSir
     * @throws I18NException
     */
    void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException;

    /**
     *
     * @param idRegistroSir
     * @param entidad
     * @throws I18NException
     */
    void reenviarRegistroSir(Long idRegistroSir, Entidad entidad) throws I18NException;

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
     * @throws I18NException
     */
    void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException;

    Integer aceptarRegistrosERTE(List<Long> registros, Entidad entidad, String destino, Oficina oficina,Long idLibro, UsuarioEntidad usuarioEntidad) throws I18NException;

    Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws I18NException;
}

