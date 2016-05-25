package es.caib.regweb3.webapp.controller.oficioRemision;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.OficiosRemisionInternoOrganismo;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.ws.api.manager.FicheroIntercambioManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.manager.impl.FicheroIntercambioManagerImpl;
import es.caib.regweb3.sir.ws.api.manager.impl.SicresXMLManagerImpl;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created 16/07/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.OficioRemision}
 * @author earrivi
 * @author anadal
 */
@Controller
@RequestMapping(value = "/oficioRemision")
@SessionAttributes(types = OficioRemision.class)
public class OficioRemisionController extends BaseController {

    @EJB(mappedName = "regweb3/OficioRemisionUtilsEJB/local")
    private OficioRemisionUtilsLocal oficioRemisionUtils;
    
    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    
    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    public ModeloOficioRemisionLocal modeloOficioRemisionEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;

    FicheroIntercambioManager ficheroIntercambioManager = new FicheroIntercambioManagerImpl();
    SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();

    /**
     * Listado de todos los Oficios de Remision
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/oficioRemision/list";
    }

    /**
     * Listado de oficios de remisión
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionList");

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        List<ObjetoBasico> tiposOficioRemision = new ArrayList<ObjetoBasico>();
        tiposOficioRemision.add(new ObjetoBasico(RegwebConstantes.TIPO_OFICIO_REMISION_INTERNO, getMessage("oficioRemision.interno")));
        tiposOficioRemision.add(new ObjetoBasico(RegwebConstantes.TIPO_OFICIO_REMISION_EXTERNO, getMessage("oficioRemision.externo")));

        OficioRemisionBusquedaForm oficioRemisionBusquedaForm = new OficioRemisionBusquedaForm(new OficioRemision(),1);


        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusquedaForm);
        model.addAttribute("tiposOficioRemision", tiposOficioRemision);
        model.addAttribute("anys", getAnys());


        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionList");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        List<ObjetoBasico> tiposOficioRemision = new ArrayList<ObjetoBasico>();
        tiposOficioRemision.add(new ObjetoBasico((long) 1, getMessage("oficioRemision.interno")));
        tiposOficioRemision.add(new ObjetoBasico((long) 2, getMessage("oficioRemision.externo")));

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), oficioRemision, librosConsulta, busqueda.getTipoOficioRemision().getId());
        
        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficioRemisionBusqueda", busqueda);
        mav.addObject("tiposOficioRemision", tiposOficioRemision);
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Listado de Registro de Entrada para realizar un Oficio de Remisión Interno
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oficiosPendientesRemisionInterna", method = RequestMethod.GET)
    public ModelAndView oficiosPendientesRemisionInterna(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/oficiosPendientesRemisionInternaList");

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroEntrada re = new RegistroEntrada();
        re.setOficina(getOficinaActiva(request));

        OficioPendienteBusquedaForm oficioPendienteBusquedaForm = new OficioPendienteBusquedaForm(re,1);

        model.addAttribute("oficinasRegistro", oficinaEjb.oficinasRegistro(getLibrosRegistroEntrada(request)));
        model.addAttribute("librosRegistro", getLibrosRegistroEntrada(request)); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        model.addAttribute("registroEntradaBusqueda", oficioPendienteBusquedaForm);
        model.addAttribute("tipo", "Interna");
        model.addAttribute("anys", getAnys());


        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Entrada para realizar un Oficio de Remisión Interno según los parametros del formulario
     */
    @RequestMapping(value = "/oficiosPendientesRemisionInterna", method = RequestMethod.POST)
    public ModelAndView oficiosPendientesRemisionInterna(@ModelAttribute OficioPendienteBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/oficiosPendientesRemisionInternaList");

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO); // Fijamos el Estado válido por defecto
        Set<Organismo> organismos = new HashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Obtenemos los Registros de Entrada, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        List<OficiosRemisionInternoOrganismo> oficiosRemisionOrganismos = registroEntradaEjb.oficiosPendientesRemisionInterna(busqueda.getAnyo(),registroEntrada.getOficina().getId(), registroEntrada.getLibro().getId(), getOrganismosOficioRemision(request, organismos));

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismos", oficiosRemisionOrganismos);
        mav.addObject("oficinasRegistro", oficinaEjb.oficinasRegistro(getLibrosRegistroEntrada(request)));
        mav.addObject("librosRegistro", getLibrosRegistroEntrada(request)); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("registroEntradaListForm", new RegistroEntradaListForm());
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Listado de Registro de Entrada para realizar un Oficio de Remisión Externo, es decir, cuyo Organismo destino
     * no pertenece a la Entidad Activa.
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oficiosPendientesRemisionExterna", method = RequestMethod.GET)
    public ModelAndView oficiosPendientesRemisionExterna(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/oficiosPendientesRemisionExternaList");

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroEntrada re = new RegistroEntrada();
        re.setOficina(getOficinaActiva(request));

        OficioPendienteBusquedaForm oficioPendienteBusquedaForm = new OficioPendienteBusquedaForm(re,1);

        mav.addObject("oficinasRegistro", oficinaEjb.oficinasRegistro(getLibrosRegistroEntrada(request)));
        model.addAttribute("librosRegistro", getLibrosRegistroEntrada(request)); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        model.addAttribute("registroEntradaBusqueda", oficioPendienteBusquedaForm);
        model.addAttribute("anys", getAnys());

        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Entrada para realizar un Oficio de Remisión Externo según los parametros del formulario
     */
    @RequestMapping(value = "/oficiosPendientesRemisionExterna", method = RequestMethod.POST)
    public ModelAndView oficiosPendientesRemisionExterna(@ModelAttribute OficioPendienteBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/oficiosPendientesRemisionExternaList");

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        List<Libro> librosRegistro = getLibrosRegistroEntrada(request);

        //Fijamos el Estado válido por defecto
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);

        Entidad entidadActiva = getEntidadActiva(request);

        // Obtenemos los Registros de Entrada, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        List<OficiosRemisionOrganismo> oficiosRemisionOrganismos = registroEntradaEjb.oficiosPendientesRemisionExterna(busqueda.getAnyo(), registroEntrada.getOficina().getId(), registroEntrada.getLibro().getId(), entidadActiva);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismos", oficiosRemisionOrganismos);
        mav.addObject("oficinasRegistro", oficinaEjb.oficinasRegistro(getLibrosRegistroEntrada(request)));
        mav.addObject("librosRegistro", librosRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("tipo", "Externa");
        mav.addObject("registroEntradaListForm", new RegistroEntradaListForm());
        mav.addObject("sirForm", new SirForm());
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} a partir de los
     * RegistroEntrada seleccionados. Creará un RegistroSalida por cada uno de los
     *  Registroentrada seleccionados.
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String oficioRemision(@ModelAttribute RegistroEntradaListForm registroEntradaListForm,
        HttpServletRequest request)throws Exception, I18NException, I18NValidationException {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        Boolean interno = registroEntradaListForm.getIdOrganismo() != null;

        // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
        if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntradaListForm.getIdLibro(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
            log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión");
            Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
            if (interno) {
                return "redirect:/oficioRemision/oficiosPendientesRemisionInterna";
            } else {
                return "redirect:/oficioRemision/oficiosPendientesRemisionExterna";
            }

        }

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        // Recorremos todos los registros de entrada seleccionados para crear un Oficio de Remisión
        for (int i = 0; i < registroEntradaListForm.getRegistros().size(); i++) {

            RegistroEntrada registroEntrada = registroEntradaListForm.getRegistros().get(i);

            if(registroEntrada.getId() != null){ //Si se ha seleccionado

                //registroEntrada = registroEntradaEjb.findById(registroEntrada.getId());

                // Comprobamos si el RegistroEntrada tiene el estado Válido
                if (!registroEntradaEjb.tieneEstado(registroEntrada.getId(), RegwebConstantes.REGISTRO_VALIDO)) {

                    Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                    if (interno) {
                        return "redirect:/oficioRemision/oficiosPendientesRemisionInterna";
                    } else {
                        return "redirect:/oficioRemision/oficiosPendientesRemisionExterna";
                    }

                } else { // Lo añadimos a la lista de registros que contendrá en Oficio de Remisión
                    registrosEntrada.add(registroEntrada);
                }
            }
        }

        // Comprobamos que al menos haya seleccionado algún RegistroEntrada
        if(registrosEntrada.size() == 0) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));

            if (interno) {
                return "redirect:/oficioRemision/oficiosPendientesRemisionInterna";
            } else {
                return "redirect:/oficioRemision/oficiosPendientesRemisionExterna";
            }
        }

        // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.

        OficioRemision oficioRemision = null;

        if (interno) { //Oficio interno
            oficioRemision = oficioRemisionUtils.crearOficioRemisionInterno(registrosEntrada,
                getOficinaActiva(request), usuarioEntidad, registroEntradaListForm.getIdOrganismo(),
                registroEntradaListForm.getIdLibro());

        } else {//Oficio externo todo: Acabar oficio remisión externo

          final String identificadorIntercambioSir = null;
          final String organismoExternoDenominacion = registroEntradaListForm.getOrganismoExternoDenominacion();
            oficioRemision = oficioRemisionUtils.crearOficioRemisionExterno(registrosEntrada,
                getOficinaActiva(request), usuarioEntidad, 
                registroEntradaListForm.getOrganismoExternoCodigo(),
                organismoExternoDenominacion, registroEntradaListForm.getIdLibro(),
                identificadorIntercambioSir);
        }

        return "redirect:/oficioRemision/"+oficioRemision.getId()+"/detalle";

    }


  /**
   * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} hacia SIR a
   * partir de los RegistroEntrada seleccionados. Creará un RegistroSalida por
   * cada uno de los Registroentrada seleccionados.
   */
  @RequestMapping(value = "/sir", method = RequestMethod.POST)
  public ModelAndView oficioRemisionSir(@ModelAttribute SirForm sirForm, 
      HttpServletRequest request, Model model)
      throws Exception {
    
    log.debug("Entra Dins oficioRemisionSir. ");

    List<OficioRemision> oficioRemisionList = new ArrayList<OficioRemision>();
    try {
      UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

      // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
      if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), sirForm.getIdLibro(),
          RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
        log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión");
        Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
          return new ModelAndView("redirect:/oficioRemision/oficiosPendientesRemisionExterna");
      }

      // Comprobamos que la Entidad que envía está en SIR
        Entidad entidadActual = getEntidadActiva(request);
      if (!entidadActual.getSir()) {
        log.error("Aviso: La entidad no está en SIR");
        Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
          return new ModelAndView("redirect:/oficioRemision/oficiosPendientesRemisionExterna");
      }

      
      if (sirForm.getIdOrganismo() != null) {
        log.error("Aviso: sirForm.getIdOrganismo() != null (això significa que es un ofici extern a una entitat de nostra organitzacio");
        Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
          return new ModelAndView("redirect:/oficioRemision/oficiosPendientesRemisionExterna");
      }



      List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

      // Recorremos todos los registros de entrada seleccionados para crear un
      // Oficio de Remisión
      for (int i = 0; i < sirForm.getRegistros().size(); i++) {

        RegistroEntrada registroEntrada = sirForm.getRegistros().get(i);

        if (registroEntrada.getId() != null) { // Si se ha seleccionado

          registroEntrada = registroEntradaEjb.findById(registroEntrada.getId());

          // Comprobamos si el RegistroEntrada tiene el estado Válido
          if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {

            log.error("El registro de entrada " + registroEntrada.getNumeroRegistroFormateado()
                + " se encuentar en un estado no valido. (" + registroEntrada.getEstado() + ")");
            Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
              return new ModelAndView("redirect:/oficioRemision/oficiosPendientesRemisionExterna");

          } else { // Lo añadimos a la lista de registros que contendrá en
                   // Oficio de Remisión
            registrosEntrada.add(registroEntrada);
          }
        }
      }

      // Comprobamos que al menos haya seleccionado algún RegistroEntrada
      if (registrosEntrada.size() == 0) {
        Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));

      } else {

        // Creamos el OficioRemisión a partir de los registros de entrada
        // seleccionados.
        {
          // Oficio externo

          log.debug("Oficio externo");

          //
          
          
          // TODO Aqui falta el nom enlloc del codi dir3
          final String organismoExterno = sirForm.getOrganismoExterno();
          final String organismoExternoDenominacion = sirForm.getOrganismoExternoDenominacion();
          log.info("organismoExterno = " + organismoExterno);
          log.info("organismoExternoDenominacion = " + organismoExternoDenominacion);


          // Para cada Registro de Entrada generamos un oficio de remision SIR.
          // Esto es asi ya que la respuesta de aceptacion de sir se realiza per
          // oficio de remision y no por registro de entrada por lo que si enviasemos 
          // varios registros dentro de un oficio se deberian de aceptar, rechazar
          // o reenviar en bloque.
          for (RegistroEntrada registroEntradaAEnviar : registrosEntrada) {

              // Enviamos el Fichero de datos de intercambio al nodo SIR
              ficheroIntercambioManager.enviarFicheroIntercambio(registroEntradaAEnviar);

              // Ho afegirem si el codi és el correcte
              //registrosEntradaProcesados.add(registroEntradaAEnviar);

              // Cream oficio remision
              // Aquest mètode ja crida a oficioRemisionEjb.registrarOficioRemision
              List<RegistroEntrada> registrosEntradaProcesados = new ArrayList<RegistroEntrada>();

              registrosEntradaProcesados.add(registroEntradaAEnviar);

              String identificadorIntercambioSir = null;
              
              OficioRemision oficioRemision;
              oficioRemision = oficioRemisionUtils.crearOficioRemisionExterno(
                  registrosEntradaProcesados, getOficinaActiva(request), usuarioEntidad,
                  organismoExterno, organismoExternoDenominacion,   sirForm.getIdLibro(),
                  identificadorIntercambioSir);

              oficioRemisionList.add(oficioRemision);

              // Crear
              log.info("Creado oficio remisión SIR " + oficioRemision.getId() 
                  + " para el registro de entrada " 
                  + registroEntradaAEnviar.getNumeroRegistroFormateado());


              // TODO millorar error indicant quin registre falla
              Mensaje.saveMessageError(request, getMessage("sir.error.envio6b"));


          }

        }
      }

    } catch (Throwable e) {
      // TODO que fer en aquest cas
      log.error(" Error enviant a SIR: " + e.getMessage(), e);

      // TODO Borrar Oficio Remision

    }

    log.debug("Final de oficioRemisionSir.");
    // TODO Missatge
    if (oficioRemisionList.size() == 0) {
        return new ModelAndView("redirect:/oficioRemision/oficiosPendientesRemisionExterna");
    } else {

      if (oficioRemisionList.size() == 1) {
        String redirect = "redirect:/oficioRemision/" + oficioRemisionList.get(0).getId() + "/detalle"; 
        log.info("Redirecting to: " + redirect);
        return new ModelAndView(redirect);
      } else  {
        //Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionListSir");

        Paginacion paginacion = new Paginacion(oficioRemisionList.size(), 1);

        paginacion.setListado(new ArrayList<Object>(oficioRemisionList));

        mav.addObject("paginacion", paginacion);
        model.addAttribute("paginacion", paginacion);
        //model.addAttribute("oficioRemisionBusqueda", new OficioRemisionBusquedaForm());
        

        return mav;
     } 
      
      
      
      
    }

  }
  

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/detalle", method = RequestMethod.GET)
    public String detalleOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        //List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByOficioRemision(oficioRemision.getId());

        model.addAttribute(oficioRemision);
        //model.addAttribute("registrosEntrada",registrosEntrada);
        model.addAttribute("trazabilidades", trazabilidades);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()));
        ModeloOficioRemision modeloOficioRemision = new ModeloOficioRemision();
        model.addAttribute("modeloOficioRemision", modeloOficioRemision);
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionDetalle";
    }

    /**
     * Anula un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/anular", method = RequestMethod.GET)
    public String anularOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        if(permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()) &&
                oficioRemision.getEstado() == RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO){
            oficioRemisionEjb.anularOficioRemision(idOficioRemision);
            Mensaje.saveMessageInfo(request, getMessage("aviso.oficioRemision.anulado"));
        }else{
            Mensaje.saveMessageError(request, getMessage("aviso.oficioRemision.anular"));
        }

        return "redirect:/oficioRemision/"+idOficioRemision+"/detalle";
    }

    /**
     * Seleccionar un {@link es.caib.regweb3.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/imprimir/{idModelo}", method = RequestMethod.GET)
    public ModelAndView imprimirModeloRecibo(@PathVariable Long idOficioRemision, @PathVariable Long idModelo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision");

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());
        ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionEjb.findById(idModelo);

        mav.addObject("oficioRemision", oficioRemision);
        mav.addObject("registrosEntrada", registrosEntrada);
        mav.addObject("modeloOficioRemision", modeloOficioRemision);

        return mav;

    }

    /**
     * Listado de oficios de remisión pendientes de Llegada
     */
    @RequestMapping(value = "/oficiosPendientesLlegada/list", method = RequestMethod.GET)
    public String oficiosPendientesLlegada() {
        return "redirect:/oficioRemision/oficiosPendientesLlegada/list/1";
    }

    /**
     * Listado de oficios de remisión pendientes de Llegada
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oficiosPendientesLlegada/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView oficiosPendientesLlegada(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosPendientesLlegadaList");

        Set<Organismo> organismos = new HashSet<Organismo>(getOrganismosOficinaActiva(request));

        List<OficioRemision> oficiosPendientesLlegada = oficioRemisionEjb.oficiosPendientesLlegadaPaginado(organismos,(pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION);

        Paginacion paginacion = new Paginacion(oficioRemisionEjb.oficiosPendientesLlegadaCount(organismos).intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", oficiosPendientesLlegada);

        return mav;
    }


    /**
     * Carga el formulario para procesar un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/procesar", method = RequestMethod.GET)
    public String procesarOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());

        model.addAttribute("oficioRemision", oficioRemision);
        model.addAttribute("registrosEntrada", registrosEntrada);

        // Obtenemos los libros donde el UsuarioEntidad puede registrar
        model.addAttribute("libros", getLibrosRegistroEntrada(request));

        // Obtenemos los Organismos destinatários donde la Oficina puede registrar
        Set<Organismo> organismosOficinaActiva = new HashSet<Organismo>(getOrganismosOficinaActiva(request));
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

        model.addAttribute("oficioPendienteLlegadaForm", new OficioPendienteLlegadaForm());
        model.addAttribute("modeloOficioRemision", new ModeloOficioRemision());
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionProcesar";
    }

    /**
     * Procesa {@link es.caib.regweb3.model.OficioRemision} pendiente de llegada,
     * creando tantos RegistroEntrada como contenga dicho OficioRemision.
     */
    @RequestMapping(value = "/{idOficioRemision}/procesar", method = RequestMethod.POST)
    public String procesarOficioRemision(
        @ModelAttribute OficioPendienteLlegadaForm oficioPendienteLlegadaForm,
        @PathVariable Long idOficioRemision, Model model, HttpServletRequest request)
             throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        // Comprobamos si ya ha sido procesado
        if (oficioRemision.getEstado() != RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.yaprocesado"));
            return "redirect:/oficioRemision/oficiosPendientesLlegada/list";
        }

        List<OficioPendienteLlegada> oficios = oficioPendienteLlegadaForm.getOficios();

        for (OficioPendienteLlegada oficio : oficios) {

            log.info("OficioPendiente: " + oficio.getIdRegistroEntrada() + " - libro: " + oficio.getIdLibro() + " - destinatario: " + oficio.getIdOrganismoDestinatario());

        }

        // Procesa el Oficio de Remisión
        try{
            registrosEntrada = oficioRemisionUtils.procesarOficioRemision(oficioRemision,
                usuarioEntidad, oficinaActiva, oficios);
        }catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("registrosEntrada",registrosEntrada);


        return "oficioRemision/oficioRemisionProcesado";
    }

}

