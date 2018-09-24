package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Stateless(name = "WebServicesMethodsEJB")
@SecurityDomain("seycon")
@RunAs("RWE_USUARI")
public class WebServicesMethodsEJB implements WebServicesMethodsLocal {

    @EJB(mappedName = "regweb3/FicheroIntercambioEJB/local")
    private FicheroIntercambioLocal ficheroIntercambioEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/MensajeControlEJB/local")
    private MensajeControlLocal mensajeControlEjb;


    @Override
    public void procesarMensajeDatosControl(MensajeControl mensaje) throws Exception{
        mensajeControlEjb.procesarMensajeDatosControl(mensaje);
    }

    @Override
    public RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{
        return ficheroIntercambioEjb.procesarFicheroIntercambio(ficheroIntercambio);
    }

    @Override
    public void guardarMensajeControl(MensajeControl mensajeControl) throws Exception{
        mensajeControlEjb.persist(mensajeControl);
    }

    @Override
    public Dir3CaibObtenerOficinasWs getObtenerOficinasService() throws Exception {
        return Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
    }

    @Override
    public Dir3CaibObtenerUnidadesWs getObtenerUnidadesService() throws Exception {
        return Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
    }

    @Override
    public String getFormatosAnexosSir() throws Exception {
        return PropiedadGlobalUtil.getFormatosAnexosSir();
    }

    @Override
    public Oficina obtenerOficina(String codigo) throws Exception {
        return oficinaEjb.findByCodigo(codigo);
    }

    @Override
    public void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws Exception {
        integracionEjb.addIntegracionError(tipo, descripcion, peticion, th, error, tiempo, idEntidad, numregformat);
    }
}
