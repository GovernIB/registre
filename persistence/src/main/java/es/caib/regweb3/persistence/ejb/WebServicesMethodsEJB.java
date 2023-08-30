package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Stateless(name = "WebServicesMethodsEJB")
@RolesAllowed({"RWE_USUARI"})
@RunAs("RWE_USUARI")
public class WebServicesMethodsEJB implements WebServicesMethodsLocal {

    @EJB private FicheroIntercambioLocal ficheroIntercambioEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private MensajeControlLocal mensajeControlEjb;
    @EJB private RegistroSirLocal registroSirEjb;


    @Override
    public void procesarMensajeDatosControl(MensajeControl mensaje) throws I18NException {
        mensajeControlEjb.procesarMensajeDatosControl(mensaje);
    }

    @Override
    public RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws I18NException {
        return ficheroIntercambioEjb.procesarFicheroIntercambio(ficheroIntercambio);
    }

    @Override
    public void eliminarRegistroSir(RegistroSir registroSir) throws I18NException {
        registroSirEjb.eliminarRegistroSir(registroSir);
    }

    @Override
    public void guardarMensajeControl(MensajeControl mensajeControl) throws I18NException {
        mensajeControlEjb.persist(mensajeControl);
    }


    @Override
    public Oficina obtenerOficina(String codigo) throws I18NException {
        return oficinaEjb.findByMultiEntidad(codigo);
    }

    @Override
    public void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws I18NException {
        integracionEjb.addIntegracionError(tipo, descripcion, peticion, th, error, tiempo, idEntidad, numregformat);
    }

    @Override
    public void addIntegracionOk(Date inicio, Long tipo, String descripcion, String peticion, Long tiempo, Long idEntidad, String numregformat) throws I18NException {
        integracionEjb.addIntegracionOk(inicio, tipo, descripcion, peticion, tiempo, idEntidad, numregformat);
    }
}
