package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.RegistroEntradaWebValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Entrada
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
@SessionAttributes({"registro"})
public class RegistroEntradaFormController extends BaseController {

    @Autowired
    private RegistroEntradaWebValidator registroEntradaValidator;

    
    @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;
    
    @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;
    
    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;
  
    @EJB(mappedName = "regweb/ModeloReciboEJB/local")
    public ModeloReciboLocal modeloReciboEjb;
    
    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;
 
    @EJB(mappedName = "regweb/IdiomaRegistroEJB/local")
    public IdiomaRegistroLocal idiomaRegistroEjb;
    
    @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;
    
    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;
    
    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;
    
    @EJB(mappedName = "regweb/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;
    
    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;
    
    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;
    
    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;
    
    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;
    
    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(Model model, HttpServletRequest request) throws Exception {

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        //Eliminamos los posibles interesados de la Sesion
        eliminarInteresadosSesion(request);

        RegistroEntrada registro = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        registro.setRegistroDetalle(registroDetalle);

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);

        Entidad entidad = getEntidadActiva(request);
        if(oficina == null){
            Mensaje.saveMessageInfo(request, getMessage("oficinaActiva.null"));
            return "redirect:/inici";
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registro",registro);
        model.addAttribute("libros", getLibrosRegistroEntrada(request));
       // model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Guardar un nuevo {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoRegistroEntrada(@ModelAttribute("registro") RegistroEntrada registro, BindingResult result, Model model,
                                       SessionStatus status, HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();

        registroEntradaValidator.validate(registro, result);
        
        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute("interesados");
        Boolean errorInteresado = true;
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
        }

        if (result.hasErrors() || errorInteresado) { // Si hay errores volvemos a la vista del formulario
          
           if (log.isDebugEnabled()) {
            log.info("====== Hi ha errors en REGISTROENTRADA ==========");
            for(ObjectError error:result.getAllErrors()){
                log.info("+ ObjectName: " + ((FieldError)error).getField());
                log.info("    - Code: " + error.getCode());
                log.info("    - DefaultMessage: " + error.getDefaultMessage());
            }
          }

          

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            model.addAttribute(getEntidadActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));
            
            //TODO Obtener los organismo por libro. Revisar todo. Entrada y salida
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
           // List<Organismo> organismosOficinaActiva = organismoEjb.getByLibro(registro.getLibro().getId());
            
            if (registro.getDestino() == null || registro.getDestino().getCodigo() == null) {
               // No han triat oficina destinataria: No fer res
            } else {


              // Si el organismo que han seleccionado es externo, lo creaamos nuevo y lo añadimos a la lista del select
              if (organismoEjb.findByCodigoVigente(registro.getDestino().getCodigo())== null) {
                  //log.info("externo : "+ registro.getDestino().getDenominacion());
                  Organismo organismoExterno = new Organismo();
                  organismoExterno.setCodigo(registro.getDestino().getCodigo());
                  organismoExterno.setDenominacion(registro.getDestino().getDenominacion());
                  organismosOficinaActiva.add(organismoExterno);
  
               // si es interno, miramos si ya esta en la lista, si no, lo añadimos
              } else if (!organismosOficinaActiva.contains(registro.getDestino())){
                  //log.info("es interno : "+registro.getDestino().getCodigo());
                  organismosOficinaActiva.add(organismoEjb.findByCodigo(registro.getDestino().getCodigo()));
              }


            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);


            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            if(registro.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.getOficinaValidaByCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo())== null){
                    //log.info("externa : "+ registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo());
                    oficinaExterna.setDenominacion(registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaExterna);

                 // si es interna, miramos si ya esta en la lista, si no, lo añadimos
                }else if(!oficinasOrigen.contains(registro.getRegistroDetalle().getOficinaOrigen())){
                    //log.info("es interno : "+registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaEjb.findByCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo()));
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registro.setOficina(getOficinaActiva(request));
                registro.setUsuario(usuarioEntidad);

                // Estado Registro entrada
                registro.setEstado(RegwebConstantes.ESTADO_VALIDO);

                // Procesamos las opciones comunes del RegistroEntrada
                registro = procesarRegistroEntrada(registro);

                // Procesamos lo Interesados de la session
                List<Interesado> interesados = procesarInteresados(interesadosSesion);

                registro.getRegistroDetalle().setInteresados(interesados);

                //Guardamos el RegistroEntrada
                synchronized (this){
                    registro = registroEntradaEjb.registrarEntrada(registro);
                }

                //Guardamos el HistorioRegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registro, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ALTA,false);

                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }finally {

                //Eliminamos los posibles interesados de la Sesion
                try {
                    eliminarInteresadosSesion(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/registroEntrada/"+registro.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroEntrada(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) {

        RegistroEntrada registro = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registro = registroEntradaEjb.findById(idRegistro);

            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);

            // Si el Organismo Destino es Externo, lo añadimos al listado.
            if(registro.getDestino() == null && registro.getDestinoExternoCodigo() != null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registro.getDestinoExternoCodigo());
                organismoExterno.setDenominacion(registro.getDestinoExternoDenominacion());
                organismosOficinaActiva.add(organismoExterno);

             // Si es Interno, pero no esta relacionado con la Oficina Activa
            }else if(!organismosOficinaActiva.contains(registro.getDestino())){
                organismosOficinaActiva.add(registro.getDestino());
            }

            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Oficina oficinaOrigen = registro.getRegistroDetalle().getOficinaOrigen();
            if(oficinaOrigen == null){
                Oficina oficinaExterna = new Oficina();
                oficinaExterna.setCodigo(registro.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                oficinaExterna.setDenominacion(registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                oficinasOrigen.add(oficinaExterna);

             // Si es Interna, pero no esta relacionado con la Oficina Activa
            }else if(!oficinasOrigen.contains(oficinaOrigen)){
                oficinasOrigen.add(oficinaOrigen);
            }

            model.addAttribute("libros", getLibrosRegistroEntrada(request));
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
            model.addAttribute("oficinasOrigen", oficinasOrigen);


        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registro",registro);

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Editar un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroEntrada(@ModelAttribute("registro") RegistroEntrada registro, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroEntradaValidator.validate(registro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getEntidadActiva(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));

            // Controlamos si el organismo destino es externo o interno
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            // Si el organismo que han seleccionado es externo, lo creamos nuevo y lo añadimos a la lista del select
            if(organismoEjb.findByCodigoVigente(registro.getDestino().getCodigo())== null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registro.getDestino().getCodigo());
                organismoExterno.setDenominacion(registro.getDestino().getDenominacion());
                organismosOficinaActiva.add(organismoExterno);

             // si es interno, miramos si ya esta en la lista, si no, lo añadimos
            }else if(!organismosOficinaActiva.contains(registro.getDestino())){
                organismosOficinaActiva.add(organismoEjb.findByCodigo(registro.getDestino().getCodigo()));
            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);


            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            // Si han indicado OficinaOrigen
            if(registro.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.getOficinaValidaByCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo())== null){
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo());
                    oficinaExterna.setDenominacion(registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaExterna);

                 // si es interna, miramos si ya esta en la lista, si no, lo añadimos
                }else if(!oficinasOrigen.contains(registro.getRegistroDetalle().getOficinaOrigen())){
                    oficinasOrigen.add(oficinaEjb.findByCodigo(registro.getRegistroDetalle().getOficinaOrigen().getCodigo()));
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                Entidad entidadActiva = getEntidadActiva(request);
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registro = procesarRegistroEntrada(registro);

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registro.getFecha());

                if(dias >= entidadActiva.getDiasVisado()){ // Si ha pasado los Dias de Visado establecidos por la entidad.

                    registro.setEstado(RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

                }else{ // Si aún no ha pasado un día

                    // Si el Registro de Entrada tiene Estado Pendiente, al editarlo pasa a ser Válido.
                    if(registro.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){
                        registro.setEstado(RegwebConstantes.ESTADO_VALIDO);
                    }
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroEntrada registroEntradaAntiguo = registroEntradaEjb.findById(registro.getId());

                // Actualizamos el RegistroEntrada
                registro = registroEntradaEjb.merge(registro);

                // Creamos el Historico RegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntradaAntiguo, usuarioEntidad, RegwebConstantes.TIPO_MODIF_DATOS,true);

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            return "redirect:/registroEntrada/"+registro.getId()+"/detalle";
        }
    }



    /**
     * Procesa las opciones de comunes de un RegistroEntrada, lo utilizamos en la creación y modificación.
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    private RegistroEntrada procesarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception{

        Organismo organismoDestino = registroEntrada.getDestino();

        // Gestionamos el Organismo, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoVigente(organismoDestino.getCodigo());
        if(orgDestino != null){ // es interno
          log.info("orgDestino interno: " + orgDestino.getDenominacion());
          registroEntrada.setDestino(orgDestino);
          registroEntrada.setDestinoExternoCodigo(null);
          registroEntrada.setDestinoExternoDenominacion(null);
        } else { // es externo
           log.info("orgDestino externo: " + registroEntrada.getDestino().getDenominacion());
           registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
           registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
           registroEntrada.setDestino(null);
        }

        // Cogemos los dos posibles campos
        Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();
        
     // Si no han indicado ni externa ni interna, se establece la oficina en la que se realiza el registro.
        if(oficinaOrigen == null){
           registroEntrada.getRegistroDetalle().setOficinaOrigen(registroEntrada.getOficina());
        } else {
        
          if(!oficinaOrigen.getCodigo().equals("-1")){ // si han indicado oficina origen
              Oficina ofiOrigen = oficinaEjb.findByCodigo(oficinaOrigen.getCodigo());
              if(ofiOrigen != null){ // Es interna
               // log.info("oficina interna");
                registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
              } else {  // es interna
                // log.info("oficina externo");
                 registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                 registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                 registroEntrada.getRegistroDetalle().setOficinaOrigen(null);
              }
          }else { // No han indicado oficina de origen
               registroEntrada.getRegistroDetalle().setOficinaOrigen(null);
               registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
               registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
          }
        }


        // Solo se comprueba si es una modificación de RegistroEntrada
        if(registroEntrada.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroEntrada.getRegistroDetalle().getFechaOrigen() == null){
                registroEntrada.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registro de Origen, le ponemos el actual.
            if(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == null || registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroEntrada.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroEntrada.getRegistroDetalle().getTransporte() == -1){
            registroEntrada.getRegistroDetalle().setTransporte(null);
        }

        // Organimo Interesado



        return registroEntrada;
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

     /**
     * Obtiene los {@link es.caib.regweb.model.Organismo} a partir del llibre seleccionat
     */
    /*@RequestMapping(value = "/obtenerOrganismoLibro", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismoLibro(@RequestParam Long id) throws Exception {

        return organismoEjb.getByLibro(id);
    }*/

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
    public List<IdiomaRegistro> idiomas() throws Exception {
        return idiomaRegistroEjb.getAll();
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



    @InitBinder("registro")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setDisallowedFields("registroDetalle.id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroEntradaValidator);
    }


}