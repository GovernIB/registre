package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonFormController extends BaseController {


    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;


   /* @ModelAttribute("organismosOficinaActiva")
    public Set<Organismo> getOrganismosOficinaActiva(HttpServletRequest request) throws Exception {
        return organismoEjb.getByOficinaActiva(getOficinaActiva(request));
    }*/


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
        if(Configuracio.getDefaultLanguage().equals(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO)){
            return RegwebConstantes.IDIOMAS_REGISTRO_ES;
        }
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
     * Para la busqueda de organismos en interesados
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute("comunidad")
    public CatComunidadAutonoma comunidad(HttpServletRequest request) throws Exception {
        Entidad entidad = getEntidadActiva(request);
        if ((entidad.getOrganismos() != null && entidad.getOrganismos().size() > 0)) {
            if (entidad.getOrganismos().get(0).getCodAmbComunidad() != null){
                return entidad.getOrganismos().get(0).getCodAmbComunidad();

            }
        }
        return new CatComunidadAutonoma();
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.CatLocalidad} de de la Provincia seleccionada
     */
   /* @RequestMapping(value = "/obtenerProvincias", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CodigoValor> obtenerProvincias(@RequestParam Long id) throws Exception {
        log.info("obtener^Provincias Abstract");
        List<CatProvincia> provincias = catProvinciaEjb.getByComunidad(id);
        List<CodigoValor> provinciascv = new ArrayList<CodigoValor>();
        for (CatProvincia provincia : provincias) {
            CodigoValor cv = new CodigoValor();
            cv.setId(provincia.getCodigoProvincia().toString());
            cv.setNombre(provincia.getDescripcionProvincia());
            provinciascv.add(cv);
        }

        return provinciascv;

    }*/

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
   /* @ModelAttribute("organismosInteresados")
    // TODO BORRAR CUANDO OTRA SOLUCION
    public List<JsonBasicoOrganismo> busquedaOrganimosInteresadosRest() {
        List<JsonBasicoOrganismo> jsonBasicoOrganismos = new ArrayList<JsonBasicoOrganismo>();
        try {
            String urlServidor = Configuracio.getDir3CaibServer();
            String params = "?codigo=&denominacion=&codNivelAdministracion=&codComunidadAutonoma=4&conOficinas=false&unidadRaiz=true";
            URL url = new URL(urlServidor + "/rest/busqueda/organismos" + params);

            ObjectMapper mapper = new ObjectMapper(); // just need one
            // Got a Java class that data maps to nicely? If so:

            log.info("Iniciamos llamada ");
            JsonBasicoOrganismo[] array= mapper.readValue(url, JsonBasicoOrganismo[].class);
            log.info("Acabamos llamada inicio montar lista " );
            jsonBasicoOrganismos = Arrays.asList(array);
            log.info("Lista montada " );

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return jsonBasicoOrganismos;
    }*/

    /*@ModelAttribute("organismosInteresados")
    // TODO BORRAR CUANDO OTRA SOLUCION
    public String busquedaOrganimosInteresadosRest() {
        List<JsonBasicoOrganismo> jsonBasicoOrganismos = new ArrayList<JsonBasicoOrganismo>();
        String output="";
        try {
            String urlServidor = Configuracio.getDir3CaibServer();
            String params = "?codigo=&denominacion=&codNivelAdministracion=&codComunidadAutonoma=4&conOficinas=false&unidadRaiz=true";
            URL url = new URL(urlServidor + "/rest/busqueda/organismos" + params);
            log.info("iniciamos llamada");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            log.info("Finalizada llamada");

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return output;
    }*/
}
