package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
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
public interface SirLocal {

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    public void recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception;

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     *
     * @param mensaje
     * @throws Exception
     */
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception;

    /**
     * @param tipoRegistro
     * @param idRegistro
     * @param codigoEntidadRegistralDestino
     * @param denominacionEntidadRegistralDestino
     * @param oficinaActiva
     * @param usuario
     * @param idLibro
     * @throws Exception
     * @throws I18NException
     */
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro) throws Exception, I18NException;

    /**
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     */
    public RegistroEntrada aceptarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception;


    /**
     * Reenvio de un Asiento Registral SIR
     * @param asientoRegistralSir
     * @throws Exception
     */
    public AsientoRegistralSir reenviarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario,String observaciones) throws Exception;

    /**
     * Método que indica si el asiento registral puede ser reenviado en función de su estado.
     * @param estado del asiento registral
     * @return
     */
    public boolean puedeReenviarAsientoRegistralSir(EstadoAsientoRegistralSir estado);

    /**
     *
     * @param asientoRegistralSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir rechazarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

    /**
     * Transforma un {@link es.caib.regweb3.model.RegistroEntrada} en un {@link AsientoRegistralSir}
     *
     * @param registroEntrada
     * @param codigoEntidadRegistralDestino
     * @param denominacionEntidadRegistralDestino
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public AsientoRegistralSir transformarRegistroEntrada(RegistroEntrada registroEntrada)
            throws Exception, I18NException;

    /**
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroEntrada transformarAsientoRegistralEntrada(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

    /**
     * Transforma un {@link es.caib.regweb3.model.RegistroEntrada} en un {@link AsientoRegistralSir}
     *
     * @param registroSalida
     * @param codigoEntidadRegistralDestino
     * @param denominacionEntidadRegistralDestino
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public AsientoRegistralSir transformarRegistroSalida(RegistroSalida registroSalida, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino)
            throws Exception, I18NException;

    /**
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroSalida transformarAsientoRegistralSalida(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

}

