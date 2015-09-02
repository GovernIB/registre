package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
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
@SessionAttributes({"registroEntrada"})
public class RegistroEntradaFormController extends AbstractRegistroCommonFormController {

    @Autowired
    private RegistroEntradaWebValidator registroEntradaValidator;
    
    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(Model model, HttpServletRequest request) throws Exception {

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        registroEntrada.setRegistroDetalle(registroDetalle);

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
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("libros", getLibrosRegistroEntrada(request));
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoRegistroEntrada(@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada,
        BindingResult result, Model model, SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        HttpSession session = request.getSession();

        registroEntradaValidator.validate(registroEntrada, result);
        
        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA);
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
            Entidad entidad = getEntidadActiva(request);
            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));
            
            //TODO Obtener los organismo por libro. Revisar todo. Entrada y salida
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
           // List<Organismo> organismosOficinaActiva = organismoEjb.getByLibro(registro.getLibro().getId());
            
            if (registroEntrada.getDestino() == null || registroEntrada.getDestino().getCodigo() == null) {
               // No han triat oficina destinataria: No fer res
            } else {


              // Si el organismo que han seleccionado es externo, lo creaamos nuevo y lo añadimos a la lista del select
              if (organismoEjb.findByCodigoVigente(registroEntrada.getDestino().getCodigo())== null) {
                  //log.info("externo : "+ registro.getDestino().getDenominacion());
                  Organismo organismoExterno = new Organismo();
                  organismoExterno.setCodigo(registroEntrada.getDestino().getCodigo());
                  organismoExterno.setDenominacion(registroEntrada.getDestino().getDenominacion());
                  organismosOficinaActiva.add(organismoExterno);
  
               // si es interno, miramos si ya esta en la lista, si no, lo añadimos
              } else if (!organismosOficinaActiva.contains(registroEntrada.getDestino())){
                  //log.info("es interno : "+registro.getDestino().getCodigo());
                  organismosOficinaActiva.add(organismoEjb.findByCodigo(registroEntrada.getDestino().getCodigo()));
              }


            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);


            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            if(registroEntrada.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.findByCodigoVigente(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo())== null){
                    //log.info("externa : "+ registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                    oficinaExterna.setDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaExterna);

                 // si es interna, miramos si ya esta en la lista, si no, lo añadimos
                }else if(!oficinasOrigen.contains(registroEntrada.getRegistroDetalle().getOficinaOrigen())){
                    //log.info("es interno : "+registro.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaEjb.findByCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo()));
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registroEntrada.setOficina(getOficinaActiva(request));
                registroEntrada.setUsuario(usuarioEntidad);

                // Estado Registro entrada
                registroEntrada.setEstado(RegwebConstantes.ESTADO_VALIDO);

                // Procesamos las opciones comunes del RegistroEntrada
                registroEntrada = procesarRegistroEntrada(registroEntrada);

                // Procesamos lo Interesados de la session
                List<Interesado> interesados = procesarInteresados(interesadosSesion, null);

                registroEntrada.getRegistroDetalle().setInteresados(interesados);

                //Guardamos el RegistroEntrada
                synchronized (this){
                    registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada);
                }

                //Guardamos el HistorioRegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ALTA,false);

                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
                return "redirect:/inici";
            }finally {

                //Eliminamos los posibles interesados de la Sesion
                try {
                    eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/registroEntrada/"+registroEntrada.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroEntrada(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) {

        RegistroEntrada registroEntrada = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registroEntrada = registroEntradaEjb.findById(idRegistro);

            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);

            // Si el Organismo Destino es Externo, lo añadimos al listado.
            if(registroEntrada.getDestino() == null && registroEntrada.getDestinoExternoCodigo() != null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registroEntrada.getDestinoExternoCodigo());
                organismoExterno.setDenominacion(registroEntrada.getDestinoExternoDenominacion());
                organismosOficinaActiva.add(organismoExterno);

             // Si es Interno, pero no esta relacionado con la Oficina Activa
            }else if(!organismosOficinaActiva.contains(registroEntrada.getDestino())){
                organismosOficinaActiva.add(registroEntrada.getDestino());
            }

            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();
            if(oficinaOrigen == null){
                Oficina oficinaExterna = new Oficina();
                oficinaExterna.setCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                oficinaExterna.setDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
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
        model.addAttribute("registroEntrada",registroEntrada);

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Editar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroEntrada(@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroEntradaValidator.validate(registroEntrada, result);

        // Actualizamos los Interesados modificados, en el caso que de un RE Pendiente.
        Boolean errorInteresado = false;
        List<Interesado> interesadosSesion = null;

        if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){
            HttpSession session = request.getSession();
            /*List<Interesado> interesados = registroDetalleEjb.findById(registroEntrada.getRegistroDetalle().getId()).getInteresados();*/
            interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

            if(interesadosSesion == null || interesadosSesion.size() == 0){
                errorInteresado = true;
            }
        }

        if (result.hasErrors() || errorInteresado) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }
            Entidad entidad = getEntidadActiva(request);
            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getEntidadActiva(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));

            // Controlamos si el organismo destino es externo o interno
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            // Si el organismo que han seleccionado es externo, lo creamos nuevo y lo añadimos a la lista del select
            if(organismoEjb.findByCodigoVigente(registroEntrada.getDestino().getCodigo())== null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registroEntrada.getDestino().getCodigo());
                organismoExterno.setDenominacion(registroEntrada.getDestino().getDenominacion());
                organismosOficinaActiva.add(organismoExterno);

             // si es interno, miramos si ya esta en la lista, si no, lo añadimos
            }else if(!organismosOficinaActiva.contains(registroEntrada.getDestino())){
                organismosOficinaActiva.add(organismoEjb.findByCodigo(registroEntrada.getDestino().getCodigo()));
            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);


            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            // Si han indicado OficinaOrigen
            if(registroEntrada.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.findByCodigoVigente(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo()) == null){
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                    oficinaExterna.setDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                    oficinasOrigen.add(oficinaExterna);

                 // si es interna, miramos si ya esta en la lista, si no, lo añadimos
                }else if(!oficinasOrigen.contains(registroEntrada.getRegistroDetalle().getOficinaOrigen())){
                    oficinasOrigen.add(oficinaEjb.findByCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo()));
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                Entidad entidadActiva = getEntidadActiva(request);
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registroEntrada = procesarRegistroEntrada(registroEntrada);

                // Procesamos lo Interesados de la session
                if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){

                    registroEntrada.getRegistroDetalle().setInteresados(procesarInteresados(interesadosSesion, registroEntrada.getRegistroDetalle().getId()));
                }

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

                if(dias >= entidadActiva.getDiasVisado()){ // Si ha pasado los Dias de Visado establecidos por la entidad.

                    registroEntrada.setEstado(RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                }else{ // Si aún no ha pasado los días definidos

                    // Si el Registro de Entrada tiene Estado Pendiente, al editarlo pasa a ser Válido.
                    if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){
                        registroEntrada.setEstado(RegwebConstantes.ESTADO_VALIDO);
                    }
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroEntrada registroEntradaAntiguo = registroEntradaEjb.findById(registroEntrada.getId());

                // Actualizamos el RegistroEntrada
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Creamos el Historico RegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntradaAntiguo, usuarioEntidad, RegwebConstantes.TIPO_MODIF_DATOS,true);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                return "redirect:/inici";
            }

            return "redirect:/registroEntrada/"+registroEntrada.getId()+"/detalle";
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
          registroEntrada.setDestino(orgDestino);
          registroEntrada.setDestinoExternoCodigo(null);
          registroEntrada.setDestinoExternoDenominacion(null);

        } else { // es externo
            registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
            if(registroEntrada.getId()!= null){//es una modificación
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
            }else{
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
            }

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

                registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
              } else {  // es externa
                  registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                  if(registroEntrada.getId()!= null){//es una modificación
                      registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                  }else{
                      registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                  }

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
     * Obtiene los {@link es.caib.regweb3.model.Organismo} a partir del llibre seleccionat
     */
    /*@RequestMapping(value = "/obtenerOrganismoLibro", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismoLibro(@RequestParam Long id) throws Exception {

        return organismoEjb.getByLibro(id);
    }*/



    @InitBinder("registroEntrada")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        //binder.setDisallowedFields("registroDetalle.id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroEntradaValidator);
    }


}