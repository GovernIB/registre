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
     * @param registros
     * @param destino
     * @param oficina
     * @param idLibro
     * @param usuarioEntidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer aceptarRegistrosERTE(List<Long> registros, String destino, Oficina oficina, Long idLibro, UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception;

    /**
     * @param registros
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws Exception;

    /**
     * Envia un intercambio a la oficina destino
     *
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @throws Exception
     * @throws I18NException
     */
    OficioRemision enviarIntercambio(Long tipoRegistro, Long idRegistro,
                                     Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Reenvia un intercambio, cuando este ha sido RECHAZADO O REENVIADO
     *
     * @param tipoRegistro
     * @param idRegistro
     * @param oficinaReenvio
     * @param oficinaActiva
     * @param usuario
     * @param observaciones
     * @throws Exception
     */
    void reenviarIntercambio(Long tipoRegistro, Long idRegistro, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException, I18NValidationException;

    /**
     * Vuelve a enviar un intercambio que ya había sido enviado previamente
     *
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    void reenviarIntercambio(OficioRemision oficioRemision) throws Exception, I18NException;

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
     * Renintenta los envíos a SIR que pendientes de llegar a destino.
     *
     * @param entidad
     * @throws Exception
     */
    void reintentarEnviosSinConfirmacion(Entidad entidad) throws Exception;

    /**
     * Renintenta los envíos con ERROR a SIR que pendientes de llegar a destino.
     *
     * @param entidad
     * @throws Exception
     */
    void reintentarEnviosConError(Entidad entidad) throws Exception;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param camposNTIs
     * @return
     */
    RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino, Boolean distribuir) throws Exception, I18NException, I18NValidationException;


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

}

