package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatLocalidad;
import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
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


    @Override
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception {
        return asientoRegistralSirEjb.crearAsientoRegistralSir(asientoRegistralSir);
    }

    @Override
    public CatPais findByCodigoPais(Long codigo) throws Exception{
        return catPaisEjb.findByCodigo(codigo);
    }

    @Override
    public CatProvincia findByCodigoProvincia(Long codigo) throws Exception {
        return catProvinciaEjb.findByCodigo(codigo);
    }

    @Override
    public CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception {
        return catLocalidadEjb.findByLocalidadProvincia(codigoLocalidad, codigoProvincia);
    }
}
