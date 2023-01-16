package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.catalogo.CatEstadoEntidad;
import es.caib.dir3caib.ws.api.catalogo.CatTipoVia;
import es.caib.dir3caib.ws.api.catalogo.*;
import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.CatServicio;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created 23/10/14 9:33
 *
 * @author mgonzalez
 */
@Stateless(name = "SincronizadorCatalogoEJB")
@RolesAllowed({"RWE_SUPERADMIN"})
public class SincronizadorCatalogoBean implements SincronizadorCatalogoLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private CatNivelAdministracionLocal catNivelAdministracionEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private CatComunidadAutonomaLocal catComunidadAutonomaEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatEntidadGeograficaLocal catEntidadGeograficaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private DescargaLocal descargaEjb;
    @EJB private CatServicioLocal catServicioEjb;
    @EJB private CatTipoViaLocal catTipoViaEjb;

    
    @Override
    public Descarga sincronizarCatalogo() throws I18NException {

        log.info("Inicio sincronizacion catalogo DIR3");

        Dir3CaibObtenerCatalogosWs catalogosService = Dir3CaibUtils.getObtenerCatalogosService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

        /* CACHE */
        Map<Long, CatPais> cachePais = new TreeMap<Long, CatPais>();
        Map<Long, CatProvincia> cacheProvincia = new TreeMap<Long, CatProvincia>();
        Map<Long, CatComunidadAutonoma> cacheComunidadAutonoma = new TreeMap<Long, CatComunidadAutonoma>();
        Map<String, CatEntidadGeografica> cacheEntidadGeografica = new TreeMap<String, CatEntidadGeografica>();

        // Obtenemos todos los CatEstadoEntidad

        List<es.caib.dir3caib.ws.api.catalogo.CatEstadoEntidad> estadosEntidad = catalogosService.obtenerCatEstadoEntidad();

        for (es.caib.dir3caib.ws.api.catalogo.CatEstadoEntidad ceeRWE : estadosEntidad) {
            es.caib.regweb3.model.CatEstadoEntidad cee = new es.caib.regweb3.model.CatEstadoEntidad();
            cee.setCodigoEstadoEntidad(ceeRWE.getCodigoEstadoEntidad());
            cee.setDescripcionEstadoEntidad(ceeRWE.getDescripcionEstadoEntidad());
            catEstadoEntidadEjb.persist(cee);
        }


        //Obtenemos todos los CatNivelAdministracion
        List<es.caib.dir3caib.ws.api.catalogo.CatNivelAdministracion> nivelesAdministracion = catalogosService.obtenerCatNivelAdministracion();

        for (es.caib.dir3caib.ws.api.catalogo.CatNivelAdministracion cnaRWE : nivelesAdministracion) {
            es.caib.regweb3.model.CatNivelAdministracion cna = new es.caib.regweb3.model.CatNivelAdministracion();
            cna.setCodigoNivelAdministracion(cnaRWE.getCodigoNivelAdministracion());
            cna.setDescripcionNivelAdministracion(cnaRWE.getDescripcionNivelAdministracion());
            catNivelAdministracionEjb.persist(cna);
        }


        //Obtenemos todos los CatPais
        List<es.caib.dir3caib.ws.api.catalogo.CatPais> paises = catalogosService.obtenerCatPais();

        for (es.caib.dir3caib.ws.api.catalogo.CatPais paisRWE : paises) {
            es.caib.regweb3.model.CatPais cpais = new es.caib.regweb3.model.CatPais();
            cpais.setCodigoPais(paisRWE.getCodigoPais());
            cpais.setDescripcionPais(paisRWE.getDescripcionPais());
            cpais = catPaisEjb.persist(cpais);
            cachePais.put(paisRWE.getCodigoPais(), cpais);

        }

        //Obtenemos todos los CatComunidadAutonoma
        List<CatComunidadAutonomaTF> comunidades = catalogosService.obtenerCatComunidadAutonoma();

        for (CatComunidadAutonomaTF comuniRWE : comunidades) {
            es.caib.regweb3.model.CatComunidadAutonoma ccomuni = new es.caib.regweb3.model.CatComunidadAutonoma();
            ccomuni.setCodigoComunidad(comuniRWE.getCodigoComunidad());
            ccomuni.setDescripcionComunidad(comuniRWE.getDescripcionComunidad());
            es.caib.regweb3.model.CatPais pais = cachePais.get(comuniRWE.getCodigoPais());
            ccomuni.setPais(pais);
            ccomuni = catComunidadAutonomaEjb.persist(ccomuni);
            cacheComunidadAutonoma.put(comuniRWE.getCodigoComunidad(), ccomuni);
        }


        //Obtenemos todos los CatProvincia
        List<CatProvinciaTF> provincias = catalogosService.obtenerCatProvincia();

        for (CatProvinciaTF provinciaRWE : provincias) {
            es.caib.regweb3.model.CatProvincia cprovincia = new es.caib.regweb3.model.CatProvincia();
            cprovincia.setCodigoProvincia(provinciaRWE.getCodigoProvincia());
            cprovincia.setDescripcionProvincia(provinciaRWE.getDescripcionProvincia());
            CatComunidadAutonoma catComunidadAutonoma = cacheComunidadAutonoma.get(provinciaRWE.getCodigoComunidadAutonoma());
            cprovincia.setComunidadAutonoma(catComunidadAutonoma);
            cprovincia = catProvinciaEjb.persist(cprovincia);
            cacheProvincia.put(provinciaRWE.getCodigoProvincia(), cprovincia);
        }


        //Obtenemos todos los CatEntidadGeografica
        List<CatEntidadGeograficaTF> entidadesGeograficas = catalogosService.obtenerCatEntidadGeografica();

        for (CatEntidadGeograficaTF entidadGeograficaRWE : entidadesGeograficas) {
            es.caib.regweb3.model.CatEntidadGeografica cEntidadGeografica = new es.caib.regweb3.model.CatEntidadGeografica();
            cEntidadGeografica.setCodigoEntidadGeografica(entidadGeograficaRWE.getCodigoEntidadGeografica());
            cEntidadGeografica.setDescripcionEntidadGeografica(entidadGeograficaRWE.getDescripcionEntidadGeografica());
            cEntidadGeografica = catEntidadGeograficaEjb.persist(cEntidadGeografica);
            cacheEntidadGeografica.put(entidadGeograficaRWE.getCodigoEntidadGeografica(), cEntidadGeografica);
        }


        //Obtenemos todos los CatLocalidad
        List<CatLocalidadTF> localidades = catalogosService.obtenerCatLocalidad();

        for (CatLocalidadTF localidadRWE : localidades) {


            //es.caib.regweb3.model.CatProvincia provincia = catProvinciaEjb.findByCodigo(localidadRWE.getCodigoProvincia());
            //CatEntidadGeografica catEntidadGeografica = catEntidadGeograficaEjb.findByCodigo(localidadRWE.getCodigoEntidadGeografica());

            es.caib.regweb3.model.CatProvincia provincia = cacheProvincia.get(localidadRWE.getCodigoProvincia());
            CatEntidadGeografica catEntidadGeografica = cacheEntidadGeografica.get(localidadRWE.getCodigoEntidadGeografica());


            es.caib.regweb3.model.CatLocalidad clocalidad = new es.caib.regweb3.model.CatLocalidad();
            if (localidadRWE.getCodigoLocalidad() != null && localidadRWE.getDescripcionLocalidad() != null) {
                clocalidad.setCodigoLocalidad(localidadRWE.getCodigoLocalidad());
                clocalidad.setNombre(localidadRWE.getDescripcionLocalidad());
                clocalidad.setProvincia(provincia);
                clocalidad.setEntidadGeografica(catEntidadGeografica);
                catLocalidadEjb.persist(clocalidad);
            }

        }

        //Obtenemos todos los CatServicio
        List<es.caib.dir3caib.ws.api.catalogo.Servicio> servicios = catalogosService.obtenerCatServicio();

        for (Servicio servicio : servicios) {

            es.caib.regweb3.model.CatServicio catServicio = new CatServicio(servicio.getCodServicio(), servicio.getDescServicio());
            catServicioEjb.persist(catServicio);
        }

        //Obtenemos todos los CatTipoVia
        List<es.caib.dir3caib.ws.api.catalogo.CatTipoVia> catTiposVia = catalogosService.obtenerCatTipoVia();

        for (CatTipoVia catTipoVia : catTiposVia) {

            es.caib.regweb3.model.CatTipoVia tipoVia = new es.caib.regweb3.model.CatTipoVia(catTipoVia.getCodigoTipoVia(), catTipoVia.getDescripcionTipoVia());
            catTipoViaEjb.persist(tipoVia);
        }

        log.info("Fin sincronizacion catalogo DIR3");

        // Guardamos los datos de la ultima descarga
        Descarga descarga = new Descarga();
        descarga.setEntidad(null);
        descarga.setTipo(RegwebConstantes.CATALOGO);
        Date hoy = new Date();
        descarga.setFechaImportacion(hoy);

        return descargaEjb.persist(descarga);
    }

    @Override
    public Descarga actualizarCatalogo() throws I18NException {

        log.info("Inicio actualizacion catalogo DIR3");

        /* CACHE */
        Map<Long, CatPais> cachePais = cachePais();
        Map<Long, CatProvincia> cacheProvincia = cacheProvincia();
        Map<Long, CatComunidadAutonoma> cacheComunidadAutonoma = cacheComunidadAutonoma();
        Map<String, CatEntidadGeografica> cacheEntidadGeografica = cacheEntidadGeografica();

        // Obtenemos el Service de los WS de Catalogos
        Dir3CaibObtenerCatalogosWs catalogosService = Dir3CaibUtils.getObtenerCatalogosService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

        // Obtenemos todos los CatEstadoEntidad
        List<CatEstadoEntidad> estadosEntidad = catalogosService.obtenerCatEstadoEntidad();

        for (CatEstadoEntidad ceeRWE : estadosEntidad) {

            es.caib.regweb3.model.CatEstadoEntidad cee = catEstadoEntidadEjb.findByCodigo(ceeRWE.getCodigoEstadoEntidad());

            if (cee != null) { // Actualiza CatEstadoEntidad
                cee.setDescripcionEstadoEntidad(ceeRWE.getDescripcionEstadoEntidad());
                catEstadoEntidadEjb.merge(cee);
            } else { // Nuevo CatEstadoEntidad
                cee = new es.caib.regweb3.model.CatEstadoEntidad();
                cee.setCodigoEstadoEntidad(ceeRWE.getCodigoEstadoEntidad());
                cee.setDescripcionEstadoEntidad(ceeRWE.getDescripcionEstadoEntidad());
                catEstadoEntidadEjb.persist(cee);
            }
        }
        log.info("CatEstadoEntidad procesadas: " + estadosEntidad.size());


        //Obtenemos todos los CatNivelAdministracion
        List<es.caib.dir3caib.ws.api.catalogo.CatNivelAdministracion> nivelesAdministracion = catalogosService.obtenerCatNivelAdministracion();

        for (es.caib.dir3caib.ws.api.catalogo.CatNivelAdministracion cnaRWE : nivelesAdministracion) {

            es.caib.regweb3.model.CatNivelAdministracion cna = catNivelAdministracionEjb.findByCodigo(cnaRWE.getCodigoNivelAdministracion());

            if (cna != null) { // Actualiza CatNivelAdministracion
                cna.setDescripcionNivelAdministracion(cnaRWE.getDescripcionNivelAdministracion());
                catNivelAdministracionEjb.merge(cna);
            } else { // Nuevo CatNivelAdministracion
                cna = new es.caib.regweb3.model.CatNivelAdministracion();
                cna.setCodigoNivelAdministracion(cnaRWE.getCodigoNivelAdministracion());
                cna.setDescripcionNivelAdministracion(cnaRWE.getDescripcionNivelAdministracion());
                catNivelAdministracionEjb.persist(cna);
            }

        }
        log.info("CatNivelAdministracion procesadas: " + nivelesAdministracion.size());


        //Obtenemos todos los CatPais
        List<es.caib.dir3caib.ws.api.catalogo.CatPais> paises = catalogosService.obtenerCatPais();

        for (es.caib.dir3caib.ws.api.catalogo.CatPais paisRWE : paises) {

            es.caib.regweb3.model.CatPais cpais = catPaisEjb.findByCodigo(paisRWE.getCodigoPais());

            if (cpais != null) { // Actualiza CatPais
                cpais.setDescripcionPais(paisRWE.getDescripcionPais());
                catPaisEjb.merge(cpais);
            } else { // Nuevo CatEstadoEntidad
                cpais = new es.caib.regweb3.model.CatPais();
                cpais.setCodigoPais(paisRWE.getCodigoPais());
                cpais.setDescripcionPais(paisRWE.getDescripcionPais());
                cpais = catPaisEjb.persist(cpais);
                cachePais.put(paisRWE.getCodigoPais(), cpais);
            }

        }
        log.info("CatPais procesadas: " + paises.size());

        //Obtenemos todos los CatComunidadAutonoma
        List<CatComunidadAutonomaTF> comunidades = catalogosService.obtenerCatComunidadAutonoma();

        for (CatComunidadAutonomaTF comuniRWE : comunidades) {

            es.caib.regweb3.model.CatComunidadAutonoma ccomuni = catComunidadAutonomaEjb.findByCodigo(comuniRWE.getCodigoComunidad());
            es.caib.regweb3.model.CatPais pais = cachePais.get(comuniRWE.getCodigoPais());


            if (ccomuni != null) { // Actualiza CatComunidadAutonoma
                ccomuni.setDescripcionComunidad(comuniRWE.getDescripcionComunidad());
                ccomuni.setPais(pais);
                catComunidadAutonomaEjb.merge(ccomuni);
            } else { // Nuevo CatComunidadAutonoma
                ccomuni = new es.caib.regweb3.model.CatComunidadAutonoma();
                ccomuni.setCodigoComunidad(comuniRWE.getCodigoComunidad());
                ccomuni.setDescripcionComunidad(comuniRWE.getDescripcionComunidad());
                ccomuni.setPais(pais);
                ccomuni = catComunidadAutonomaEjb.persist(ccomuni);
                cacheComunidadAutonoma.put(comuniRWE.getCodigoComunidad(), ccomuni);
            }

        }
        log.info("CatComunidadAutonoma procesadas: " + comunidades.size());


        //Obtenemos todos los CatProvincia
        List<CatProvinciaTF> provincias = catalogosService.obtenerCatProvincia();

        for (CatProvinciaTF provinciaRWE : provincias) {

            es.caib.regweb3.model.CatProvincia cprovincia = catProvinciaEjb.findByCodigo(provinciaRWE.getCodigoProvincia());
            CatComunidadAutonoma catComunidadAutonoma = cacheComunidadAutonoma.get(provinciaRWE.getCodigoComunidadAutonoma());

            if (cprovincia != null) { // Actualiza CatProvincia
                cprovincia.setDescripcionProvincia(provinciaRWE.getDescripcionProvincia());
                cprovincia.setComunidadAutonoma(catComunidadAutonoma);
                catProvinciaEjb.merge(cprovincia);
            } else { // Nuevo CatProvincia
                cprovincia = new es.caib.regweb3.model.CatProvincia();
                cprovincia.setCodigoProvincia(provinciaRWE.getCodigoProvincia());
                cprovincia.setDescripcionProvincia(provinciaRWE.getDescripcionProvincia());
                cprovincia.setComunidadAutonoma(catComunidadAutonoma);
                cprovincia = catProvinciaEjb.persist(cprovincia);
                cacheProvincia.put(provinciaRWE.getCodigoProvincia(), cprovincia);
            }

        }
        log.info("CatProvincia procesadas: " + provincias.size());

        //Obtenemos todos los CatEntidadGeografica
        List<CatEntidadGeograficaTF> entidadesGeograficas = catalogosService.obtenerCatEntidadGeografica();

        for (CatEntidadGeograficaTF entidadGeograficaRWE : entidadesGeograficas) {

            es.caib.regweb3.model.CatEntidadGeografica cEntidadGeografica = catEntidadGeograficaEjb.findByCodigo(entidadGeograficaRWE.getCodigoEntidadGeografica());

            if (cEntidadGeografica != null) { // Actualiza CatEntidadGeografica
                cEntidadGeografica.setDescripcionEntidadGeografica(entidadGeograficaRWE.getDescripcionEntidadGeografica());
                catEntidadGeograficaEjb.merge(cEntidadGeografica);
            } else { // Nuevo CatProvincia
                cEntidadGeografica = new es.caib.regweb3.model.CatEntidadGeografica();
                cEntidadGeografica.setCodigoEntidadGeografica(entidadGeograficaRWE.getCodigoEntidadGeografica());
                cEntidadGeografica.setDescripcionEntidadGeografica(entidadGeograficaRWE.getDescripcionEntidadGeografica());
                catEntidadGeograficaEjb.persist(cEntidadGeografica);
            }

        }


        //Obtenemos todos los CatLocalidad
        List<CatLocalidadTF> localidades = catalogosService.obtenerCatLocalidad();

        for (CatLocalidadTF localidadRWE : localidades) {


            es.caib.regweb3.model.CatProvincia provincia = cacheProvincia.get(localidadRWE.getCodigoProvincia());
            CatEntidadGeografica catEntidadGeografica = cacheEntidadGeografica.get(localidadRWE.getCodigoEntidadGeografica());
            es.caib.regweb3.model.CatLocalidad clocalidad = catLocalidadEjb.findByCodigo(localidadRWE.getCodigoLocalidad(), provincia.getCodigoProvincia(), catEntidadGeografica.getCodigoEntidadGeografica());

            if (localidadRWE.getCodigoLocalidad() != null && localidadRWE.getDescripcionLocalidad() != null) {
                if (clocalidad != null) { // Actualiza CatLocalidad
                    clocalidad.setNombre(localidadRWE.getDescripcionLocalidad());

                    clocalidad.setProvincia(provincia);

                    clocalidad.setEntidadGeografica(catEntidadGeografica);
                    catLocalidadEjb.merge(clocalidad);
                } else { // Nuevo CatProvincia
                    clocalidad = new es.caib.regweb3.model.CatLocalidad();
                    clocalidad.setCodigoLocalidad(localidadRWE.getCodigoLocalidad());
                    clocalidad.setNombre(localidadRWE.getDescripcionLocalidad());
                    clocalidad.setProvincia(provincia);
                    clocalidad.setEntidadGeografica(catEntidadGeografica);
                    catLocalidadEjb.persist(clocalidad);
                }
            }
        }
        log.info("CatProvincia procesadas: " + localidades.size());

        //Obtenemos todos los CatServicio
        List<es.caib.dir3caib.ws.api.catalogo.Servicio> servicios = catalogosService.obtenerCatServicio();

        for (Servicio servicio : servicios) {

            CatServicio cs = catServicioEjb.findByCodigo(servicio.getCodServicio());

            if (cs != null) { //Actualización
                cs.setDescServicio(servicio.getDescServicio());
                catServicioEjb.merge(cs);

            } else { //Nuevo
                es.caib.regweb3.model.CatServicio catServicio = new CatServicio(servicio.getCodServicio(), servicio.getDescServicio());
                catServicioEjb.persist(catServicio);
            }


        }
        log.info("CatServicio procesadas: " + servicios.size());


        //Obtenemos todos los CatTipoVia
        List<es.caib.dir3caib.ws.api.catalogo.CatTipoVia> catTiposVia = catalogosService.obtenerCatTipoVia();

        for (CatTipoVia catTipoVia : catTiposVia) {

            es.caib.regweb3.model.CatTipoVia ctv = catTipoViaEjb.findByCodigo(catTipoVia.getCodigoTipoVia());

            if (ctv != null) {// Actualización
                ctv.setDescripcionTipoVia(catTipoVia.getDescripcionTipoVia());
                catTipoViaEjb.merge(ctv);
            } else { //Nuevo
                es.caib.regweb3.model.CatTipoVia tipoVia = new es.caib.regweb3.model.CatTipoVia(catTipoVia.getCodigoTipoVia(), catTipoVia.getDescripcionTipoVia());
                catTipoViaEjb.persist(tipoVia);
            }


        }
        log.info("CatTipoVia procesadas: " + catTiposVia.size());

        log.info("Fin actualizacion catalogo DIR3");

        // Clear cache
        cachePais.clear();
        cacheComunidadAutonoma.clear();
        cacheEntidadGeografica.clear();
        cacheProvincia.clear();

        // Guardamos los datos de la ultima descarga
        Descarga descarga = new Descarga();
        descarga.setTipo(RegwebConstantes.CATALOGO);
        descarga.setEntidad(null);
        Date hoy = new Date();
        descarga.setFechaImportacion(hoy);

        return descargaEjb.persist(descarga);

    }


    public Map<Long, CatPais> cachePais() throws I18NException {
        Map<Long, CatPais> cachePais = new TreeMap<Long, CatPais>();
        for (CatPais ca : catPaisEjb.getAll()) {
            cachePais.put(ca.getCodigoPais(), ca);
        }
        log.info(" Pais : " + cachePais.size());
        return cachePais;
    }

    public Map<Long, CatProvincia> cacheProvincia() throws I18NException {
        Map<Long, CatProvincia> cacheProvincia = new TreeMap<Long, CatProvincia>();
        for (CatProvincia ca : catProvinciaEjb.getAll()) {
            cacheProvincia.put(ca.getCodigoProvincia(), ca);
        }
        log.info(" Provincia : " + cacheProvincia.size());
        return cacheProvincia;
    }

    public Map<Long, CatComunidadAutonoma> cacheComunidadAutonoma() throws I18NException {
        Map<Long, CatComunidadAutonoma> cacheComunidadAutonoma = new TreeMap<Long, CatComunidadAutonoma>();
        for (CatComunidadAutonoma ca : catComunidadAutonomaEjb.getAll()) {
            cacheComunidadAutonoma.put(ca.getCodigoComunidad(), ca);
        }
        log.info(" Comunidad Autonoma : " + cacheComunidadAutonoma.size());
        return cacheComunidadAutonoma;
    }


    public Map<String, CatEntidadGeografica> cacheEntidadGeografica() throws I18NException {
        Map<String, CatEntidadGeografica> cacheEntidadGeografica = new TreeMap<String, CatEntidadGeografica>();
        for (CatEntidadGeografica ca : catEntidadGeograficaEjb.getAll()) {
            cacheEntidadGeografica.put(ca.getCodigoEntidadGeografica(), ca);
        }
        log.info(" Entidad Geografica : " + cacheEntidadGeografica.size());
        return cacheEntidadGeografica;
    }


}
