package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.Dir3CaibUtils;

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
    public void procesarMensajeDatosControl(MensajeControl mensaje) throws Exception {
        mensajeControlEjb.procesarMensajeDatosControl(mensaje);
    }

    @Override
    public RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception {
        return ficheroIntercambioEjb.procesarFicheroIntercambio(ficheroIntercambio);
    }

    @Override
    public void eliminarRegistroSir(RegistroSir registroSir) throws Exception {
        registroSirEjb.eliminarRegistroSir(registroSir);
    }

    @Override
    public void guardarMensajeControl(MensajeControl mensajeControl) throws Exception {
        mensajeControlEjb.persist(mensajeControl);
    }

    @Override
    public Dir3CaibObtenerOficinasWs getObtenerOficinasService(Long idEntidad) throws Exception {
        return Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));
    }

    @Override
    public Dir3CaibObtenerUnidadesWs getObtenerUnidadesService(Long idEntidad) throws Exception {
        return Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));
    }


    @Override
    public Oficina obtenerOficina(String codigo) throws Exception {
        return oficinaEjb.findByMultiEntidad(codigo);
    }

    @Override
    public void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws Exception {
        integracionEjb.addIntegracionError(tipo, descripcion, peticion, th, error, tiempo, idEntidad, numregformat);
    }

    @Override
    public void addIntegracionOk(Date inicio, Long tipo, String descripcion, String peticion, Long tiempo, Long idEntidad, String numregformat) throws Exception {
        integracionEjb.addIntegracionOk(inicio, tipo, descripcion, peticion, tiempo, idEntidad, numregformat);
    }
}
