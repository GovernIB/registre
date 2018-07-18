package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
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
@RunAs("RWE_USUARI") //todo Revisar si se puede eliminar
public class WebServicesMethodsEJB implements WebServicesMethodsLocal {

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;

    @Override
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception{
        sirEjb.recibirMensajeDatosControl(mensaje);
    }

    @Override
    public Boolean recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{
        return  sirEjb.recibirFicheroIntercambio(ficheroIntercambio);
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
}
