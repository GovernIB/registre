package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonFormController extends BaseController {


    @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;



    @ModelAttribute("organismosOficinaActiva")
    public Set<Organismo> getOrganismosOficinaActiva(HttpServletRequest request) throws Exception {
        return organismoEjb.getByOficinaActiva(getOficinaActiva(request).getId());
    }

    @ModelAttribute("tiposAsunto")
    public List<TipoAsunto> tiposAsunto(HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        return tipoAsuntoEjb.getActivosEntidad(entidadActiva.getId());
    }

    @ModelAttribute("tiposPersona")
    public Long[] tiposPersona() throws Exception {
        return RegwebConstantes.TIPOS_PERSONA;
    }

    @ModelAttribute("tiposInteresado")
    public Long[] tiposInteresado() throws Exception {
        return  RegwebConstantes.TIPOS_INTERESADO;
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_REGISTRO;
    }

    @ModelAttribute("transportes")
    public Long[] transportes() throws Exception {
        return RegwebConstantes.TRANSPORTES;
    }

    @ModelAttribute("tiposDocumentacionFisica")
    public Long[] tiposDocumentacionFisica() throws Exception {
        return RegwebConstantes.TIPOS_DOCFISICA;
    }

    @ModelAttribute("tiposDocumento")
    public long[] tiposDocumento() throws Exception {
        return RegwebConstantes.TIPOS_DOCUMENTOID;
    }

    @ModelAttribute("paises")
    public List<CatPais> paises() throws Exception {
        return catPaisEjb.getAll();
    }

    @ModelAttribute("personasFisicas")
    public List<Persona> personasFisicas(HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        return personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA);
    }

    @ModelAttribute("personasJuridicas")
    public List<Persona> personasJuridicas(HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        return personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA);
    }

    @ModelAttribute("provincias")
    public List<CatProvincia> provincias() throws Exception {
        return catProvinciaEjb.getAll();
    }

    @ModelAttribute("canalesNotificacion")
    public long[] canalesNotificacion() throws Exception {
        return RegwebConstantes.CANALES_NOTIFICACION;
    }

    @ModelAttribute("comunidadesAutonomas")
    public List<CatComunidadAutonoma> comunidadesAutonomas() throws Exception {
        return catComunidadAutonomaEjb.getAll();
    }

    @ModelAttribute("nivelesAdministracion")
    public List<CatNivelAdministracion> nivelesAdministracion() throws Exception {
        return catNivelAdministracionEjb.getAll();
    }

    @ModelAttribute("estados")
    public Long[] estados() throws Exception {
        return RegwebConstantes.ESTADOS_REGISTRO;
    }


    /**
     * Obtiene los {@link es.caib.regweb.model.CodigoAsunto} del TipoAsunto seleccionado
     */
    @RequestMapping(value = "/obtenerCodigosAsunto", method = RequestMethod.GET)
    public @ResponseBody
    List<CodigoAsunto> obtenerCodigosAsunto(@RequestParam Long id) throws Exception {

        return codigoAsuntoEjb.getByTipoAsunto(id);
    }

    /**
     * Obtiene los {@link es.caib.regweb.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerLocalidades", method = RequestMethod.GET)
    public @ResponseBody
    List<CatLocalidad> obtenerLocalidades(@RequestParam Long id) throws Exception {

        return catLocalidadEjb.getByProvincia(id);
    }

    /*private IRegistro procesarRegistro(IRegistro registro, String tipoRegistro) throws Exception{

        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)){
            registro = (RegistroEntrada) registro;
        }else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)){
            registro = (RegistroSalida) registro;
        }

        Organismo organismoDestino = registro.getDestino();

        // Gestionamos el Organismo, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoVigente(organismoDestino.getCodigo());
        if(orgDestino != null){ // es interno

            registro.setDestino(orgDestino);
            registro.setDestinoExternoCodigo(null);
            registro.setDestinoExternoDenominacion(null);
        } else { // es externo
            registro.setDestinoExternoCodigo(registro.getDestino().getCodigo());
            if(registro.getId()!= null){//es una modificación
                registro.setDestinoExternoDenominacion(registro.getDestinoExternoDenominacion());
            }else{
                registro.setDestinoExternoDenominacion(registro.getDestino().getDenominacion());
            }

            registro.setDestino(null);
        }

        // Cogemos los dos posibles campos
        Oficina oficinaOrigen = registro.getRegistroDetalle().getOficinaOrigen();

        // Si no han indicado ni externa ni interna, se establece la oficina en la que se realiza el registro.
        if(oficinaOrigen == null){
            registro.getRegistroDetalle().setOficinaOrigen(registro.getOficina());
        } else {

            if(!oficinaOrigen.getCodigo().equals("-1")){ // si han indicado oficina origen
                Oficina ofiOrigen = oficinaEjb.findByCodigo(oficinaOrigen.getCodigo());
                if(ofiOrigen != null){ // Es interna

                    registro.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                    registro.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                    registro.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
                } else {  // es externa

                    registro.getRegistroDetalle().setOficinaOrigenExternoCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo());
                    if(registro.getId()!= null){//es una modificación
                        registro.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                    }else{
                        registro.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    }

                    registro.getRegistroDetalle().setOficinaOrigen(null);

                }
            }else { // No han indicado oficina de origen
                registro.getRegistroDetalle().setOficinaOrigen(null);
                registro.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registro.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
            }
        }


        // Solo se comprueba si es una modificación de RegistroEntrada
        if(registro.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registro.getRegistroDetalle().getFechaOrigen() == null){
                registro.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registro de Origen, le ponemos el actual.
            if(registro.getRegistroDetalle().getNumeroRegistroOrigen() == null || registro.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registro.getRegistroDetalle().setNumeroRegistroOrigen(registro.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registro.getRegistroDetalle().getCodigoAsunto().getId() == null || registro.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registro.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registro.getRegistroDetalle().getTransporte() == -1){
            registro.getRegistroDetalle().setTransporte(null);
        }

        // Organimo Interesado



        return registro;
    }*/
  
  
}
