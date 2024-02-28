package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.utils.AnexoFull;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import javax.xml.datatype.DatatypeConfigurationException;
import java.text.ParseException;
import java.util.Date;
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
     * @param oficinaSirDestino
     * @throws I18NException
     * @throws I18NException
     */
    AsientoBean crearIntercambioEntrada(RegistroEntrada registroEntrada, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, OficinaTF oficinaSirDestino)
            throws I18NException, I18NValidationException, InterException, DatatypeConfigurationException, ParseException;

    /**
     * Crear un intercambio SIR salida, preparado par enviarse
     * @param registroSalida
     * @param oficinaActiva
     * @param usuario
     * @param oficinaSirDestino
     * @throws I18NException
     * @throws I18NException
     */
    AsientoBean crearIntercambioSalida(RegistroSalida registroSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuario, OficinaTF oficinaSirDestino)
            throws I18NException, I18NValidationException, InterException, DatatypeConfigurationException, ParseException;

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
            throws I18NException, I18NValidationException, DatatypeConfigurationException, InterException, ParseException;

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
    void reenviarIntercambioLIBSIR(Long tipoRegistro, Long idRegistro, Entidad entidad, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws I18NException, I18NValidationException, ParseException, InterException, DatatypeConfigurationException;

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
    Boolean enviarConfirmacion(Long idRegistroSir) throws I18NException, InterException;

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
     * @return
     */
    RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idOrganismoDestino, String emails, String motivo) throws I18NException, I18NValidationException, ParseException, InterException;


    /**
     * Reenvio de un Registro SIR
     *
     * @param registroSir
     * @throws I18NException
     */
    void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException;

    /**
     * Reenvio de un Registro SIR con LIBSIR
     *
     * @param registroSir
     * @throws I18NException
     */
    void reenviarRegistroSirLIBSIR(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException, InterException;

    /**
     *
     * @param idRegistroSir
     * @param entidad
     * @throws I18NException
     */
    void reenviarRegistroSir(Long idRegistroSir, Entidad entidad) throws I18NException;

    /**
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws I18NException
     */
    void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException;

    /**
     * Confirma un registro sir a través de LIBSIR
     * @param registroSir
     * @param numeroRegistroFormateado
     * @param fechaRegistro
     * @return
     * @throws I18NException
     */

    void confirmarRegistroSirLIBSIR(RegistroSir registroSir, String numeroRegistroFormateado, Date fechaRegistro) throws I18NException, InterException;
    /**
     * Rechaza un registro sir a través de LIBSIR
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws I18NException
     */
    void rechazarRegistroSirLIBSIR(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws I18NException, InterException;

    Integer aceptarRegistrosERTE(List<Long> registros, Entidad entidad, String destino, Oficina oficina,Long idLibro, UsuarioEntidad usuarioEntidad) throws I18NException;

    Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws I18NException;

    List<AnexoFull> gestionAnexosInterdoc(RegistroDetalle registroDetalle, Entidad entidad, String unTramitacionDestino, String numeroRegistroFormateado, Date fechaRegistro, Long tipoRegistro) throws I18NException;
}

