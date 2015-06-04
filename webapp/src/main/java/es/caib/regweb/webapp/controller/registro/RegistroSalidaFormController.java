package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.RegistroSalidaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
 * Controller para gestionar los Registros de Salida
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
public class RegistroSalidaFormController extends AbstractRegistroCommonFormController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private RegistroSalidaWebValidator registroSalidaValidator;


    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroSalida(Model model, HttpServletRequest request) throws Exception {

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        //Eliminamos los posibles interesados de la Sesion
        eliminarInteresadosSesion(request);

        RegistroSalida registro = new RegistroSalida();
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
        model.addAttribute("libros", getLibrosRegistroSalida(request));
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoRegistroSalida(@ModelAttribute("registro") RegistroSalida registro,
        BindingResult result, Model model, SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException {

        HttpSession session = request.getSession();

        registroSalidaValidator.validate(registro, result);

        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute("interesados");
        Boolean errorInteresado = true;
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
        }

        if (result.hasErrors()|| errorInteresado) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            model.addAttribute(getEntidadActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
            model.addAttribute("libros", getLibrosRegistroSalida(request));

            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            
            if (registro.getOrigen() == null || registro.getOrigen().getCodigo() == null
                || "".equals(registro.getOrigen().getCodigo().trim())) {
                  // Falta oficina origen
            } else {
            
              // Si el organismo que han seleccionado es externo, lo creaamos nuevo y lo añadimos a la lista del select
              if(organismoEjb.findByCodigoVigente(registro.getOrigen().getCodigo())== null){
                  //log.info("externo : "+ registro.getDestino().getDenominacion());
                  Organismo organismoExterno = new Organismo();
                  organismoExterno.setCodigo(registro.getOrigen().getCodigo());
                  organismoExterno.setDenominacion(registro.getOrigen().getDenominacion());
                  organismosOficinaActiva.add(organismoExterno);
  
                  // si es interno, miramos si ya esta en la lista, si no, lo añadimos
              } else if(!organismosOficinaActiva.contains(registro.getOrigen())){
                  //log.info("es interno : "+registro.getDestino().getCodigo());
                  organismosOficinaActiva.add(organismoEjb.findByCodigo(registro.getOrigen().getCodigo()));
              }
            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            if(registro.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.findByCodigoVigente(registro.getRegistroDetalle().getOficinaOrigen().getCodigo())== null){
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

            return "registroSalida/registroSalidaForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registro.setOficina(getOficinaActiva(request));
                registro.setUsuario(usuarioEntidad);

                // Estado RegistroSalida
                registro.setEstado(RegwebConstantes.ESTADO_VALIDO);

                // Procesamos las opciones comunes del RegistroSalida
                registro = procesarRegistroSalida(registro);

                // Procesamos lo Interesados de la session
                List<Interesado> interesados = procesarInteresados(interesadosSesion);

                registro.getRegistroDetalle().setInteresados(interesados);

                //Guardamos el RegistroSalida
                registro = registroSalidaEjb.registrarSalida(registro);

                //Guardamos el HistorioRegistroSalida
                historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registro, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ALTA,false);

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

            return "redirect:/registroSalida/"+registro.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroSalida(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) {

        RegistroSalida registro = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registro = registroSalidaEjb.findById(idRegistro);

            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);

            // Si el Organismo Destino es Externo, lo añadimos al listado.
            if(registro.getOrigen() == null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registro.getOrigenExternoCodigo());
                organismoExterno.setDenominacion(registro.getOrigenExternoDenominacion());
                organismosOficinaActiva.add(organismoExterno);

                // Si es Interno, pero no esta relacionado con la Oficina Activa
            }else if(!organismosOficinaActiva.contains(registro.getOrigen())){
                organismosOficinaActiva.add(registro.getOrigen());
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

            model.addAttribute("libros", getLibrosRegistroSalida(request));
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
            model.addAttribute("oficinasOrigen", oficinasOrigen);


        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registro",registro);

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Editar un {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroSalida(@ModelAttribute("registro") RegistroSalida registro, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroSalidaValidator.validate(registro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getEntidadActiva(request));
            model.addAttribute("libros", getLibrosRegistroSalida(request));

            // Controlamos si el organismo destino es externo o interno
            Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);
            // Si el organismo que han seleccionado es externo, lo creamos nuevo y lo añadimos a la lista del select
            if(organismoEjb.findByCodigoVigente(registro.getOrigen().getCodigo())== null){
                Organismo organismoExterno = new Organismo();
                organismoExterno.setCodigo(registro.getOrigen().getCodigo());
                organismoExterno.setDenominacion(registro.getOrigen().getDenominacion());
                organismosOficinaActiva.add(organismoExterno);

                // si es interno, miramos si ya esta en la lista, si no, lo añadimos
            }else if(!organismosOficinaActiva.contains(registro.getOrigen())){
                organismosOficinaActiva.add(organismoEjb.findByCodigo(registro.getOrigen().getCodigo()));
            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);


            // Si la Oficina Origen es Externa, la añadimos al listado.
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);
            // Si han indicado OficinaOrigen
            if(registro.getRegistroDetalle().getOficinaOrigen()!=null){
                if(oficinaEjb.findByCodigoVigente(registro.getRegistroDetalle().getOficinaOrigen().getCodigo()) == null){
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

            return "registroSalida/registroSalidaForm";
        }else { // Si no hay errores actualizamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registro = procesarRegistroSalida(registro);

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registro.getFecha());

                if(dias >= 1){ // Si ha pasado 1 día o mas
                    registro.setEstado(RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                }

                // Obtenemos el RS antes de guardarlos, para crear el histórico
                RegistroSalida registroSalidaAntiguo = registroSalidaEjb.findById(registro.getId());

                // Actualizamos el RegistroSalida
                registro = registroSalidaEjb.merge(registro);

                // Creamos el Historico RegistroSalida
                historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalidaAntiguo, usuarioEntidad, RegwebConstantes.TIPO_MODIF_DATOS, true);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                return "redirect:/inici";
            }

            return "redirect:/registroSalida/"+registro.getId()+"/detalle";
        }
    }




    /**
     * Procesa las opciones de comunes de un RegistroSalida, lo utilizamos en la creación y modificación.
     * @param registroSalida
     * @return
     * @throws Exception
     */
    private RegistroSalida procesarRegistroSalida(RegistroSalida registroSalida) throws Exception{

        Organismo organismoDestino = registroSalida.getOrigen();

        // Gestionamos el Organismo, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoVigente(organismoDestino.getCodigo());
        if(orgDestino != null){ // es interno

            registroSalida.setOrigen(orgDestino);
            registroSalida.setOrigenExternoCodigo(null);
            registroSalida.setOrigenExternoDenominacion(null);
        } else { // es externo
            registroSalida.setOrigenExternoCodigo(registroSalida.getOrigen().getCodigo());
            if(registroSalida.getId()!= null){//es una modificación
                registroSalida.setOrigenExternoDenominacion(registroSalida.getOrigenExternoDenominacion());
            }else{
                registroSalida.setOrigenExternoDenominacion(registroSalida.getOrigen().getDenominacion());
            }

            registroSalida.setOrigen(null);
        }

        // Cogemos los dos posibles campos
        Oficina oficinaOrigen = registroSalida.getRegistroDetalle().getOficinaOrigen();
        
        // Si no han indicado ni externa ni interna, se establece la oficina en la que se realiza el registro.
        if(oficinaOrigen == null){
            registroSalida.getRegistroDetalle().setOficinaOrigen(registroSalida.getOficina());
        } else {
          if(!oficinaOrigen.getCodigo().equals("-1")){ // si han indicado oficina origen
              Oficina ofiOrigen = oficinaEjb.findByCodigo(oficinaOrigen.getCodigo());
              if(ofiOrigen != null){ // Es interna
                  // log.info("oficina interna");
                  registroSalida.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                  registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                  registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
              } else {  // es externa

                  registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo());
                  if(registroSalida.getId()!= null){//es una modificación
                      registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                  }else{
                      registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                  }
                  registroSalida.getRegistroDetalle().setOficinaOrigen(null);

              }
          }else { // No han indicado oficina de origen
              registroSalida.getRegistroDetalle().setOficinaOrigen(null);
              registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
              registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
          }
        }





        // Solo se comprueba si es una modificación de RegistroSalida
        if(registroSalida.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroSalida.getRegistroDetalle().getFechaOrigen() == null){
                registroSalida.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registro de Origen, le ponemos el actual.
            if(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroSalida.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroSalida.getRegistroDetalle().getCodigoAsunto() == null || registroSalida.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroSalida.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroSalida.getRegistroDetalle().getTransporte() == -1){
            registroSalida.getRegistroDetalle().setTransporte(null);
        }

        // Organimo Interesado



        return registroSalida;
    }

    /**
     * Obtiene los {@link es.caib.regweb.model.Organismo} a partir del llibre seleccionat
     */
   /* @RequestMapping(value = "/obtenerOrganismoLibro", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismoLibro(@RequestParam Long id) throws Exception {

        return organismoEjb.getByLibro(id);
    }*/


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

        binder.setValidator(this.registroSalidaValidator);
    }

}