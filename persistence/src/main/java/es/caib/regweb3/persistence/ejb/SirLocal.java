package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
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
     * @throws Exception
     * @throws I18NException
     */
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro,
        String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino,
        Oficina oficinaActiva, UsuarioEntidad usuario) 
            throws Exception, I18NException, I18NValidationException;

    /**
     *
     * @param idEntidad
     * @throws Exception
     */
    public void reintentarEnvios(Long idEntidad) ;

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
    public RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception;


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
    public void reenviarRegistro(String tipoRegistro, Long idRegistro, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException;

    /**
     * Reenvio de un Registro SIR
     * @param registroSir
     * @throws Exception
     */
    public void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario,String observaciones) throws Exception;

    /**
     * Método que indica si el RegistroSir puede ser reenviado en función de su estado.
     * @param estado del RegistroSir
     * @return
     */
    public boolean puedeReenviarRegistroSir(EstadoRegistroSir estado);

    /**
     *
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    public void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception;

}

