package es.caib.regweb3.webapp.controller.remesa;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Notificacion;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.DocumentoVisor;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.MultiEntidadLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RemesaConsultaLocal;
import es.caib.regweb3.persistence.ejb.RemesaLocal;
import es.caib.regweb3.persistence.ejb.SignatureServerLocal;
import es.caib.regweb3.persistence.utils.DehuDocumentManager;
import es.caib.regweb3.persistence.utils.DocumentoNotificacionException;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.registro.AbstractRegistroCommonFormController;
import es.caib.regweb3.webapp.form.RemesaBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaWebValidator;

/**
 * Created by Limit Tecnologies S.L.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Remesa}
 * @author Jamal
 */
@Controller
@RequestMapping(value = "/remesa")
public class RemesaListController extends AbstractRegistroCommonFormController {

	@Autowired
    private RegistroEntradaWebValidator registroEntradaValidator;
	
    @EJB(mappedName = "regweb3/RemesaEJB/local")
    public RemesaLocal remesaEjb;
    
    @EJB(mappedName = "regweb3/RemesaConsultaEJB/local")
    public RemesaConsultaLocal remesaConsultaEjb;

	@EJB(mappedName = "regweb3/MultiEntidadEJB/local")
    private MultiEntidadLocal multiEntidadEjb;
	
	@EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;
	
	@EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;
	
	@EJB(mappedName = "regweb3/SignatureServerEJB/local")
    private SignatureServerLocal signatureServerEjb;
	
	@Autowired
	private DehuDocumentManager documentManager;
	
    /**
     * Listado de todas las {@link Notificacion}
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listadoRemesas() {
        return "redirect:/remesa/list";
    }

    /**
     * Listado de registros de entrada
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) throws Exception {
        Entidad entidadActiva = getEntidadActiva(request);
        Remesa remesa = new Remesa();
    	Calendar calendar = Calendar.getInstance();
        Date fechaHasta = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -30); // Restar 30 días
        Date fechaDesde = calendar.getTime();
        
        remesa.setEstado(RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_SEDE);
        
        RemesaBusqueda remesaBusqueda = new RemesaBusqueda(remesa, null, null, null, 1);
        remesaBusqueda.setFechaPuestaDisposicionDesde(fechaDesde);
        remesaBusqueda.setFechaPuestaDisposicionHasta(fechaHasta);
        
        Date fechaPuestaDisposicionHasta = null;
        if (remesaBusqueda.getFechaPuestaDisposicionHasta() != null)
			fechaPuestaDisposicionHasta = RegistroUtils.ajustarHoraBusqueda(remesaBusqueda.getFechaPuestaDisposicionHasta());
        

        
        // Búsqueda de remesas
     	Paginacion paginacion = remesaConsultaEjb.busqueda(
     			remesaBusqueda.getPageNumber(), 
     			remesa,
     			null,
     			remesaBusqueda.getFechaPuestaDisposicionDesde(),
     			fechaPuestaDisposicionHasta,
     			entidadActiva.getId());

     	remesaBusqueda.setPageNumber(1);
     	
     	model.addAttribute("paginacion", paginacion);
        model.addAttribute("remesaBusqueda", remesaBusqueda);
     		
        return "remesa/remesaList";
    }

	/**
	 * Realiza la busqueda de {@link es.caib.regweb3.model.Remesa} según
	 * los parametros del formulario
	 */
	@RequestMapping(value = "/busqueda", method = RequestMethod.GET)
	public ModelAndView busqueda(@ModelAttribute RemesaBusqueda busqueda, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView("remesa/remesaList", result.getModel());

		Entidad entidadActiva = getEntidadActiva(request);

		Date fechaPuestaDisposicionHasta = null;
		// Ponemos la hora 23:59 a la fecha
		if (busqueda.getFechaPuestaDisposicionHasta() != null)
			fechaPuestaDisposicionHasta = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaPuestaDisposicionHasta());

		// Búsqueda de remesas
		Paginacion paginacion = remesaConsultaEjb.busqueda(
				busqueda.getPageNumber(), 
				busqueda.getRemesa(),
				busqueda.getEmisor(),
				busqueda.getFechaPuestaDisposicionDesde(),
				fechaPuestaDisposicionHasta,
				entidadActiva.getId());

		busqueda.setPageNumber(1);
		mav.addObject("paginacion", paginacion);
		return mav;

	}
	
    @RequestMapping(value = "/{identificador}/lectura", method = RequestMethod.POST)
    @ResponseBody
    public Remesa lecturaNotificacion(
    		@PathVariable String identificador,
    		Model model, HttpServletRequest request) throws Exception, I18NException {
    	Entidad entidad = getEntidadActiva(request);
        try {
        	Remesa remesa = remesaConsultaEjb.getByIdentificador(identificador);
        	
        	if (remesa.getReintentosLectura() == 0)
        		throw new RuntimeException("Superado el número de reintentos de lectura, consulte el administrador");
        	UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        	remesaEjb.lecturaNotificacion(
        		identificador, 
        		usuarioEntidad,
        		entidad);
        	
        	List<File> documentos = documentManager.getDocuments(identificador);
        	
        	if (documentos != null) {
        		for (File documento : documentos) {
					String mimeType = Files.probeContentType(documento.toPath());
					byte[] contenido = Files.readAllBytes(documento.toPath());

        			DocumentoVisor documentoRecibido = new DocumentoVisor(
        					documento.getName(), 
        					mimeType, 
        					Base64.encodeBase64String(contenido));
					
					remesa.getDocumentosRecibidos().add(documentoRecibido);
				}
        	}
        	
        	return remesa;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} catch (I18NException i18ne) {
			i18ne.printStackTrace();
			return null;
		}
    }
    
    @RequestMapping(value = "/{identificador}/consulta", method = RequestMethod.POST)
    @ResponseBody
    public Remesa consultaRealizada(
    		@PathVariable String identificador,
    		Model model, HttpServletRequest request) throws Exception, I18NException {
        try {
//        	ConsultaRealizadaResponse respuesta = remesaEjb.consultaRealizada(
//        		identificador, 
//        		entidad);
        	
        	Remesa remesa = remesaConsultaEjb.getByIdentificador(identificador);
        	
        	List<File> documentos = documentManager.getDocuments(identificador);
        	
        	if (documentos != null) {
        		for (File documento : documentos) {
					String mimeType = Files.probeContentType(documento.toPath());
					byte[] contenido = Files.readAllBytes(documento.toPath());
					
					DocumentoVisor documentoRecibido = new DocumentoVisor(
        					documento.getName(), 
        					mimeType, 
        					Base64.encodeBase64String(contenido));
					
					remesa.getDocumentosRecibidos().add(documentoRecibido);
				}
        	}
        	
        	return remesa;
        } catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
    }
    
    @RequestMapping(value = "/{identificador}/registrar", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(
    		@PathVariable String identificador, 
    		Model model, 
    		HttpServletRequest request) throws Exception {
    	
        Oficina oficina = getOficinaActiva(request);
        Entidad entidad = getEntidadActiva(request);
        LinkedHashSet<Oficina> oficinasOrigen;
        if(multiEntidadEjb.isMultiEntidad()){
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
        }else{
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
        }
        RegistroEntrada registroEntrada = new RegistroEntrada();
    
        registroEntrada.setOficina(oficina);
        try {
	        String organoDestinoDefecto = PropiedadGlobalUtil.getOrganoDestinoPorDefecto(entidad.getId());
	        if (organoDestinoDefecto != null) {
	        	Organismo organismo = organismoEjb.findByCodigo(organoDestinoDefecto);
	        	registroEntrada.setDestino(organismo);
	        }
        } catch (Exception ex) {
			log.error("Ha habido un error seleccionando órgano por defecto", ex);
		}
        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        
        cargarFormularioRegistro(model, registroEntrada, request, identificador);

        model.addAttribute("oficinasOrigen",  oficinasOrigen);
        
        return "registroEntrada/registroEntradaForm";
    }
    
    @RequestMapping(value = "/{identificador}/registrar", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String nuevoRegistroEntrada(
    		@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada,
    		@PathVariable String identificador, 
    		BindingResult result, 
    		Model model, 
    		SessionStatus status,
    		HttpServletRequest request) throws Exception, I18NException, I18NValidationException {
        Entidad entidad = getEntidadActiva(request);
    	HttpSession session = request.getSession();
    	
        registroEntrada.setLibro(getLibroEntidad(request));
        registroEntrada.getRegistroDetalle().setPresencial(false);
        
        registroEntradaValidator.validate(registroEntrada, result);
        
        boolean destinoValido = verificarDestinoSeleccionado(registroEntrada.getDestino(), entidad);

        if (result.hasErrors() || !destinoValido) { // Si hay errores volvemos a la vista del formulario

        	cargarFormularioRegistro(model, registroEntrada, request, identificador);
        	
            if (!destinoValido) {
            	 model.addAttribute("destinoNoValido", true);
            }
            
            LinkedHashSet<Oficina> oficinasOrigen;
            if(multiEntidadEjb.isMultiEntidad()){
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
            }else{
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
            }

            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  oficinasOrigen);
            model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));

            // Organismo destino: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            if (registroEntrada.getDestino() != null) { // Si se ha escogido un Organismo destino

                Organismo organismo = organismoEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getDestino().getCodigo(), entidad.getId());

                if (organismo == null) {// Si es externo, lo creamos nuevo y lo añadimos a la lista del select
                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestino().getCodigo(), registroEntrada.getDestino().getDenominacion()));
                } else { // si es interno o multientidad  lo añadimos
                    organismosOficinaActiva.add(organismo);
                }


            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
           // Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            if (!registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) {// Han indicado oficina de origen

                Oficina oficinaOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(),entidad.getId());
                if (oficinaOrigen == null) { // Es externa
                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion()));
                } else { // Es interna o multientidad, la añadimos a la lista por si acaso no está
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);
            model.addAttribute("esRemesa", true);
            
            return "registroEntrada/registroEntradaForm";
        } else { // Si no hay errores guardamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registroEntrada.setOficina(getOficinaActiva(request));
                registroEntrada.setUsuario(usuarioEntidad);
                registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
                
                registroEntrada = procesarRegistroEntrada(registroEntrada, entidad);

                Remesa remesa = remesaConsultaEjb.getByIdentificador(identificador);

                // Crear anexos i interesados relacionados con el registro de entrada
                List<Interesado> interesados = cargarInteresados(remesa);
                
                // Crear lista anexosFull para guardar en archivo DocumentCustordy y SignatureCustody
                List<AnexoFull> anexosFull = cargarAnexosFullRemesa(request, identificador, registroEntrada, entidad);
                
                // Vacias anexos registro entrada para crearlos después del registro de entrada
                registroEntrada.getRegistroDetalle().setAnexos(null);
                
                // Guardamos el RegistroEntrada
                registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuarioEntidad, interesados, null, false, true);
                
                // Crear y guardar anexos
                for (AnexoFull anexoFull : anexosFull) {
                    anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                    AnexoFull anexoFullCreado;
                    if(!anexoFull.getAnexo().getConfidencial()){
                        anexoFullCreado = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroEntrada.getId(), REGISTRO_ENTRADA, null, true);
                    }else{
                        anexoFullCreado = anexoEjb.crearAnexoConfidencial(anexoFull, usuarioEntidad, registroEntrada.getId(), REGISTRO_ENTRADA);
                    }
                    registroEntrada.getRegistroDetalle().getAnexos().add(anexoFullCreado.getAnexo());
                }


                remesaEjb.actualizarEstadoRegistrada(identificador, registroEntrada.getId(), RegwebConstantes.REMESA_ESTADO_REG_REGISTRADA);      
                
                // Borrar documentos de filesystem
				documentManager.deleteDocuments(identificador);
				
                session.removeAttribute("errorAnexos");
            }catch (DocumentoNotificacionException e) {
            	session.setAttribute("errorAnexos", true);
            	return "redirect:/remesa/"+identificador+"/registrar";
			} catch (Exception e) {
            	String eMessage = (!e.getMessage().isEmpty() ? ": " + e.getMessage() : "");
            	String eCause = (e.getCause() != null ? ": " + e.getCause().getMessage() : "");
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro") + (eMessage + (!eMessage.isEmpty() ? "" : eCause)));
                e.printStackTrace();
                return "redirect:/inici";
            } finally {
                status.setComplete();
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
    
    private void cargarFormularioRegistro(Model model, RegistroEntrada registroEntrada, HttpServletRequest request, String identificador) throws Exception {
    	Remesa remesa = remesaConsultaEjb.getByIdentificador(identificador);
        registroEntrada.getRegistroDetalle().setExtracto(remesa.getConcepto());
        registroEntrada.getRegistroDetalle().setTipoDocumentacionFisica(3L);
        if (remesa.getCodigoProcedimiento() != null)
        	registroEntrada.getRegistroDetalle().setCodigoSia(Long.valueOf(remesa.getCodigoProcedimiento()));
        
    	List<Interesado> interesados = cargarInteresados(remesa);
         
         // Inicializar anexos registro entrada
     	List<File> documentos = documentManager.getDocuments(identificador);
     	if (documentos != null) {
     		List<Anexo> anexos = new ArrayList<Anexo>();
     		for (File documento : documentos) {
     			Anexo anexo = new Anexo();
 				String nombre = documento.getName();
 				
 				if (nombre != null) {
 					anexo.setTitulo(FilenameUtils.removeExtension(nombre));
 					anexo.setNombreFichero(nombre);
 				
	 				if (nombre.contains("CERTIFICACION_PDF"))
	 					anexo.setTipoDocumental(new TipoDocumental("24829"));
	 				else
	 					anexo.setTipoDocumental(new TipoDocumental("24825"));
 				}
 				
 	            anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
 	            anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
 	            anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
 	            
 	            anexos.add(anexo);
 			}
     		
     		registroEntrada.getRegistroDetalle().setAnexos(anexos);
     	}
     	HttpSession session = request.getSession();
     	session.removeAttribute("errorAnexos");
     	
        model.addAttribute("interesados",interesados);
         

         
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));
        model.addAttribute("esRemesa", true);
        model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
    }
    
    private List<Interesado> cargarInteresados(Remesa remesa) {
        List<Interesado> interesados = new ArrayList<Interesado>();
        Interesado interesado = new Interesado();
        
        log.debug("Cargando interesados remesa con identificador {" + remesa.getIdentificador() + "}");
        log.debug("[" + remesa.getIdentificador() + "] " + "- Entidad código: " + remesa.getEntidadCodigo());
        log.debug("[" + remesa.getIdentificador() + "] " + "- Emisor NIF: " + remesa.getOrganoEmisorNif());
        log.debug("[" + remesa.getIdentificador() + "] " + "- Emisor código: " + remesa.getOrganoEmisorCodigo());
        log.debug("[" + remesa.getIdentificador() + "] " + "- Emisor nombre: " + remesa.getOrganoEmisorNombre());
        log.debug("[" + remesa.getIdentificador() + "] " + "- Titular NIF: " + remesa.getTitularNif());
        log.debug("[" + remesa.getIdentificador() + "] " + "- Titular Nombre: " + remesa.getTitularNombre());
        
        if (remesa.getOrganoEmisorCodigo() != null) {
        	// Administració
            interesado.setTipo(1L); // 1- Adm 2- P.Fisica 3- Juridica
            interesado.setCodigoDir3(remesa.getOrganoEmisorCodigo());
            interesado.setRazonSocial(remesa.getOrganoEmisorNombre());
        } else { 
        	// Persona jurídica
            interesado.setTipo(3L); // 1- Adm 2- P.Fisica 3- Juridica
            interesado.setTipoDocumentoIdentificacion(2L);
            interesado.setDocumento(remesa.getTitularNif());
            interesado.setRazonSocial(remesa.getTitularNombre());
        }
        
        // Si ve informat el NIF de l'òrgan emisor s'ha de guardar aquest com a interessat
        if (remesa.getOrganoEmisorNif() != null) {
            interesado.setTipoDocumentoIdentificacion(2L); // 1-NIF 2-CIF 3-CODIGO ORIGEN
            interesado.setDocumento(remesa.getOrganoEmisorNif());
        }
        
        interesados.add(interesado);
        
        return interesados;
    }
	
    private List<AnexoFull> cargarAnexosFullRemesa(
    		HttpServletRequest request, 
    		String identificador, 
    		RegistroEntrada registroEntrada, 
    		Entidad entidad) throws Exception, DocumentoNotificacionException {
    	List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
    	
    	if (identificador == null) {
    		List<Remesa> remesas = remesaConsultaEjb.findByRegistroEntrada(registroEntrada.getId());
    		identificador = remesas != null ? remesas.get(0).getIdentificador() : null;
    	}
    	
	    if (identificador != null) {
	        try {
		        for (Anexo anexo : registroEntrada.getRegistroDetalle().getAnexos()) {
			        if (anexo != null) {
			        	AnexoFull anexoFull = new AnexoFull();
			        	File documento = documentManager.getDocument(identificador, anexo.getNombreFichero());
						
			        	String nombre = documento.getName();
						String mimeType = Files.probeContentType(documento.toPath());
						byte[] contenido = Files.readAllBytes(documento.toPath());
			            
			            anexo.setFechaCaptura(new Date());
			            anexo.setScan(false);
			            anexo.setRegistroDetalle(registroEntrada.getRegistroDetalle());
			            anexo.setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);
			            
			            DocumentCustody documentoCustody = new DocumentCustody();
			            documentoCustody.setData(contenido);
			            documentoCustody.setLength(contenido.length);
			            documentoCustody.setMime(mimeType);
			            documentoCustody.setName(nombre);
			            
			            
			        	anexoFull.setAnexo(anexo);
			        	anexoFull.setDocumentoCustody(documentoCustody);
			            
			            validarFirmaAnexo(entidad, registroEntrada, anexoFull);
			            
			            int modoFirma = getModoFirma(anexoFull.getAnexo());
			            
			            anexo.setModoFirma(modoFirma);
			            
			            if (RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED == modoFirma || RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED == modoFirma) {
			            	SignatureCustody signatureCustody = new SignatureCustody();
			            	signatureCustody.setData(contenido);
			            	signatureCustody.setLength(contenido.length);
			            	signatureCustody.setMime(mimeType);
			            	signatureCustody.setName(nombre);
				        	anexoFull.setSignatureCustody(signatureCustody);
				        	
//				        	if (RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED == modoFirma) // Només informar firma si és attached
//				        		anexoFull.setDocumentoCustody(null);
			            }

			            anexosFull.add(anexoFull);
			        }
		        }
                
                registroEntrada.getRegistroDetalle().setAnexos(new ArrayList<Anexo>());
			} catch (Exception e) {
				log.error("Ha habido un error guardando el documento legal de la notificación del registro (id=" + registroEntrada.getId() + ")");
				throw new DocumentoNotificacionException(e.getMessage());
			}
	    }
	    
	    return anexosFull;
    }

	private void validarFirmaAnexo(Entidad entidad, RegistroEntrada registroEntrada, AnexoFull anexoFull) {
		try {
			anexoFull.getAnexo().setFirmaverificada(false);
			
			signatureServerEjb.checkDocument(anexoFull, entidad.getId(), new Locale("es"), false, true, true);
			
			anexoFull.getAnexo().setFirmaverificada(true);
		} catch (Exception e) {
			anexoFull.getAnexo().setFirmaverificada(false);
			log.error("Ha habido un error verificando la firma de un documento de notificación: " + e.getMessage());
			e.printStackTrace();
		} catch (I18NException e) {
			anexoFull.getAnexo().setFirmaverificada(false);
			log.error("Ha habido un error verificando la firma de un documento de notificación: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private int getModoFirma(Anexo anexo) {
		if ((anexo.getFirmaValida() != null && anexo.getFirmaValida()) && anexo.getSignType() != null) {
			switch (anexo.getSignType()) {
			case "PAdES":
			case "CAdES":
			case "XAdES":
				return RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED;
			default:
				return RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED;
			}
		}
		
		return RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA;
	}
    
    private boolean verificarDestinoSeleccionado(Organismo destino, Entidad entidad) {
    	if(destino != null) {
	    	try {
				Organismo organismo = organismoEjb.findByCodigoByEntidadMultiEntidad(destino.getCodigo(), entidad.getId());
		        boolean integradoConSir = organismoEjb.isDestinoDir3Sir(destino.getCodigo());
		        
		        if (organismo == null && !integradoConSir) {// Si es externo y no integrado con SIR
	            	return false;
	            } else if ((organismo != null) || (organismo == null && integradoConSir)) { // Si es interno o externo integrado con SIR 
	            	return true;
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		return false;
	}
    
    private RegistroEntrada procesarRegistroEntrada(RegistroEntrada registroEntrada, Entidad entidad) throws Exception{

        log.info("Entro en procesar entrada ");

        // Organismo destinatario, lo buscamos en función de si es multientidad o no
        Organismo orgDestino = organismoEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getDestino().getCodigo(),entidad.getId());

        //Organismo destinatario, determinamos si es interno o externo teniendo en cuenta la multientidad
        if( orgDestino == null || (!entidad.getId().equals(orgDestino.getEntidad().getId()))){ //Externo

            registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
            if(registroEntrada.getId()!= null){//es una modificación
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
            }else{
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
            }

            registroEntrada.setDestino(null);

        }else{//Interno

            registroEntrada.setDestino(orgDestino);
            registroEntrada.setDestinoExternoCodigo(null);
            registroEntrada.setDestinoExternoDenominacion(null);
        }



        // Oficina origen, determinando si es Interno o Externo
        Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();

        if (oficinaOrigen.getCodigo().equals("-1")) { // No han indicado oficina de origen

            // Asignamos la Oficina donde se realiza el registro
            registroEntrada.getRegistroDetalle().setOficinaOrigen(registroEntrada.getOficina());
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

        } else { // Han indicado oficina origen

            Oficina ofiOrigen =oficinaEjb.findByCodigoByEntidadMultiEntidad(oficinaOrigen.getCodigo(), entidad.getId());

            if (ofiOrigen == null || (!entidad.getId().equals(ofiOrigen.getOrganismoResponsable().getEntidad().getId())) ) { // Es externa

                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                if (registroEntrada.getId() != null) {//es una modificación
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                } else {
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                }

                registroEntrada.getRegistroDetalle().setOficinaOrigen(null);

            } else {  // es interna

                registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
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


        return registroEntrada;
    }
    
    @ModelAttribute("estados")
    public String[] estados(HttpServletRequest request) throws Exception {

        return RegwebConstantes.REMESA_REGWEB_ESTADOS;

    }
    
	@InitBinder("remesaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }
	
    @InitBinder("registroEntrada")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroEntradaValidator);
    }

}
