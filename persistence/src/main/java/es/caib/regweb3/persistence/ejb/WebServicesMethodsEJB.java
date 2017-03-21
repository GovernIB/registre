package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.utils.Mensaje;
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

    @EJB(mappedName = "regweb3/AsientoRegistralSirEJB/local")
    public AsientoRegistralSirLocal asientoRegistralSirEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    public SirLocal sirEjb;

    @Override
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception{
        return  asientoRegistralSirEjb.getAsientoRegistral(identificadorIntercambio, codigoEntidadRegistralDestino);
    }

    @Override
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception {
        return asientoRegistralSirEjb.crearAsientoRegistralSir(asientoRegistralSir);
    }

    @Override
    public Archivo persistArchivo(Archivo archivo) throws Exception{
        return archivoEjb.persist(archivo);
    }

    @Override
    public void removeArchivo(Archivo archivo) throws Exception{
         archivoEjb.remove(archivo);
    }

    @Override
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception{
        sirEjb.recibirMensajeDatosControl(mensaje);
    }
}
