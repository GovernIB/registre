package es.caib.regweb3.persistence.ejb;

import static es.caib.regweb3.utils.RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION;
import static es.caib.regweb3.utils.RegwebConstantes.ANEXO_ORIGEN_CIUDADANO;
import static es.caib.regweb3.utils.RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED;
import static es.caib.regweb3.utils.RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA;
import static es.caib.regweb3.utils.RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA;
import static es.caib.regweb3.utils.RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.utils.cxf.CXFUtils;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.model.TrazabilidadSir;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.ArchivoManager;
import es.caib.regweb3.persistence.utils.ConversionHelper;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.GeiserPluginHelper;
import es.caib.regweb3.persistence.utils.ProgresoActualitzacion;
import es.caib.regweb3.persistence.utils.ProgresoActualitzacion.TipoInfo;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import net.java.xades.security.xml.XMLSignatureElement;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */

@Stateless(name = "RegistroSirHelperEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistroSirHelperBean extends BaseEjbJPA<RegistroSir, Long> implements RegistroSirHelperLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private ArchivoLocal archivoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
    
    public static Map<Long, ProgresoActualitzacion> progresoActualitzacion = new HashMap<Long, ProgresoActualitzacion>();
    
    @Autowired
    private ConversionHelper conversioHelper;
    @Autowired 
    private GeiserPluginHelper pluginHelper;
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void crearRegistroSirRecibido(
			ApunteRegistro apunteRegistroBusquedaFiltrado, 
			Long entidadId,
			Integer total, 
			ProgresoActualitzacion progreso) throws Exception, I18NException {
    	// La petición de búsqueda no devuelve los anexos, hacer petición consulta...
		progreso.addInfo(TipoInfo.INFO, "Realizando consulta detalle registro [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
		RegistroSir registroGeiserConsulta = new RegistroSir();
		registroGeiserConsulta.setNumeroRegistro(apunteRegistroBusquedaFiltrado.getNuRegistro());
		registroGeiserConsulta.setCodigoEntidadRegistralOrigen(apunteRegistroBusquedaFiltrado.getCdAmbitoOrigen());
		registroGeiserConsulta.setIncluirContenidoAnexo(true);
		RespuestaConsultaGeiser consultaApunte = pluginHelper.postProcesoConsultarRegistroSirGeiser(registroGeiserConsulta, entidadId);
		
		List<ApunteRegistro> apuntesConsulta = consultaApunte.getApuntes();
		if (apuntesConsulta != null) {
			progreso.addInfo(TipoInfo.INFO, "Conversión a registro SIR [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
			List<RegistroSir> registrosSirRecibidos = conversioHelper.convertirList(apuntesConsulta, RegistroSir.class); 
			// Recorrer y crearlo si no se encuentra en Regweb
			for (RegistroSir registroSir: registrosSirRecibidos) {
				
				try {
					progreso.addInfo(TipoInfo.INFO, "Consultando ámbito creación a partir del registro origen [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + ", numeroRegistroOrigen=" + apunteRegistroBusquedaFiltrado.getNuRegistroOrigen() + "]");
					// Consulta ámbito creación registro entidad externa (origen registro)
					RegistroSir registroGeiserConsultaOrigen = new RegistroSir();
					registroGeiserConsultaOrigen.setNumeroRegistro(apunteRegistroBusquedaFiltrado.getNuRegistroOrigen());
					registroGeiserConsultaOrigen.setCodigoEntidadRegistralOrigen(apunteRegistroBusquedaFiltrado.getCdAmbitoOrigen());
					RespuestaConsultaGeiser consultaRegistrosEntExterna = pluginHelper.postProcesoConsultarRegistroSirGeiser(registroGeiserConsultaOrigen, entidadId);
					
					List<ApunteRegistro> registrosEntidadExterna = consultaRegistrosEntExterna.getApuntes();
					if (!registrosEntidadExterna.isEmpty()) {
						for (ApunteRegistro apunteRegistroEntExterna : registrosEntidadExterna) {
							if (apunteRegistroEntExterna.getNuRegistro().equals(apunteRegistroBusquedaFiltrado.getNuRegistroOrigen())) { // Si está relacionado con el registro externo
								// Recuperamos el ámbito de creación del registro externo y asignarlo al registro aceptado en destino
								registroSir.setCodigoEntidadRegistralOrigen(apunteRegistroEntExterna.getCdAmbitoOrigen());
								registroSir.setDecodificacionEntidadRegistralOrigen(apunteRegistroEntExterna.getNombreAmbitoOrigen());
								registroSir.setFechaRegistroOrigen(apunteRegistroEntExterna.getFechaRegistro());
							}
						}
					}
						Oficina oficinaOrigenIntern = oficinaEjb.findByCodigoEntidad(registroSir.getCodigoUnidadTramitacionOrigen(), entidadId);
					// Solo crear el asiento recibido en la oficina (se duplica al confirmer y enviar registro a la entidad)
					if (oficinaOrigenIntern == null) { 
						List<InteresadoSir> interesadosSir = registroSir.getInteresados();
						if (interesadosSir != null && !interesadosSir.isEmpty()) {
							// En caso de recepción, le asignamos la entidad a la que va dirigida
			                try {
			                	progreso.addInfo(TipoInfo.INFO, "Buscando entidad relacionada con el registro recibido [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
				                Entidad entidad = null;
					            if(registroSir.getEntidad() == null && registroSir.getCodigoEntidadRegistralDestino() != null) { // Ámbito creación
					                if(multiEntidadEjb.isMultiEntidad()) {
					                    entidad = new Entidad(oficinaEjb.obtenerEntidadMultiEntidad(registroSir.getCodigoEntidadRegistralDestino()));
					                }else{
					                    entidad = new Entidad(oficinaEjb.obtenerEntidad(registroSir.getCodigoEntidadRegistralDestino()));
					                }
					                registroSir.setEntidad(entidad);
					            } else if (registroSir.getEntidad() == null && registroSir.getCodigoUnidadTramitacionDestino() != null) { // Entidad destino
//							            	entidad = entidadEjb.findByCodigoDir3(registroSir.getCodigoUnidadTramitacionDestino());
					                registroSir.setEntidad(entidad);
					            }
			                } catch (Exception e) {
								log.error("No s'ha trobat cap entitat relacionada amb l'àmbit destí " + registroSir.getCodigoEntidadRegistralDestino());
								throw e;
							}
			                registroSir.setEstado(EstadoRegistroSir.RECIBIDO_CONFIRMADO); // Confirmat a l'oficina
							registroSir = persist(registroSir);
				            // Guardamos los Interesados
				            if(registroSir.getInteresados() != null && registroSir.getInteresados().size() > 0){
								progreso.addInfo(TipoInfo.INFO, "Se procede a guardar los interesados [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
				                for(InteresadoSir interesadoSir: registroSir.getInteresados()){
				                    interesadoSir.setRegistroSir(registroSir);
				                    
				                    interesadoSirEjb.guardarInteresadoSir(interesadoSir);
				                }
				                progreso.addInfo(TipoInfo.INFO, "Interesados guardados correctamente [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + ", idRegistroSir=" + registroSir.getId() + "]");
				            }
				            // Guardamos los Anexos
				            if(registroSir.getAnexos() != null && registroSir.getAnexos().size() > 0){
					            progreso.addInfo(TipoInfo.INFO, "Se procede a guardar los anexos [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
				                for(AnexoSir anexoSir: registroSir.getAnexos()) {
				                    anexoSir.setRegistroSir(registroSir);
			
				                    anexoSirEjb.persist(anexoSir);
				                }
				                progreso.addInfo(TipoInfo.INFO, "Anexos guardados correctamente [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + ", idRegistroSir=" + registroSir.getId() + "]");
				            }
				            
				            // Creamos la TrazabilidadSir
				            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION);
				            trazabilidadSir.setRegistroSir(registroSir);
				            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
				            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
				            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
				            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
				            trazabilidadSir.setAplicacion(registroSir.getAplicacion());
				            trazabilidadSir.setNombreUsuario(registroSir.getNombreUsuario());
				            trazabilidadSir.setContactoUsuario(registroSir.getContactoUsuario());
				            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
				            trazabilidadSirEjb.persist(trazabilidadSir);
						}
					}
					try {
						if (registroSir.getCodigoUnidadTramitacionDestino() != null && registroSir.getCodigoEntidadRegistralDestino() != null) {
							log.info("Creando registro de entrada... [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
							progreso.addInfo(TipoInfo.INFO, "Creando registro de entrada... [numeroRegistro=" + apunteRegistroBusquedaFiltrado.getNuRegistro() + "]");
							Organismo organismoDestino = organismoEjb.findByCodigoEntidad(registroSir.getCodigoUnidadTramitacionDestino(), entidadId);
							Oficina oficinaDestino = oficinaEjb.findByCodigoEntidad(registroSir.getCodigoEntidadRegistralDestino(), entidadId);
							String usuario = pluginHelper.getUsuarioResponsableCreacionRegistros(entidadId);
							UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificador(usuario);
							generarRegistroEntradaRegistroSirGeiser(
									registroSir, 
									usuarioEntidad,
									oficinaDestino, 
									usuarioEntidad.getEntidad().getLibro().getId(), 
									1L,
									new ArrayList<CamposNTI>(), 
									organismoDestino.getId());
							log.info("Registro de entrada " + apunteRegistroBusquedaFiltrado.getNuRegistro() + " creado correctamente");
							progreso.addInfo(TipoInfo.INFO, "Registro de entrada creado correctamente");
							progreso.addInfo(TipoInfo.INFO, "-------------------------------------------");
						} else {
							throw new RuntimeException("No se ha informado ninguna oficina y entidad destino para el registro recibido");
						}
					} catch (I18NValidationException e) {
						e.printStackTrace();
						throw new RuntimeException("Ha habido error creando el registro SIR");
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					total++;
				} catch (Exception e) {
					log.info("Error al crear el RegistroSir: ");
					e.printStackTrace();
					for (AnexoSir anexoSir : registroSir.getAnexos()) {
						try {
						ArchivoManager am = new ArchivoManager(anexoSir.getAnexo(), archivoEjb);
						am.processErrorArchivosWithoutThrowException();
						if (anexoSir.getAnexo() != null)
							log.info("Eliminamos los posibles archivos creados: " + anexoSir.getAnexo().getId());
						} catch (Exception ex) {
							e.printStackTrace();
						}
					}	
				}
			}
		}
		progreso.incrementRegistrosRecuperados();
	}

    
    private RegistroEntrada generarRegistroEntradaRegistroSirGeiser(
    		RegistroSir registroSir, 
    		UsuarioEntidad usuario, 
    		Oficina oficinaActiva, 
    		Long idLibro, 
    		Long idIdioma, 
    		List<CamposNTI> camposNTIs, 
    		Long idOrganismoDestino)
            throws Exception, I18NException, I18NValidationException {

        Libro libro = libroEjb.findById(idLibro);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setNumeroRegistro(registroSir.getNumeroRegistro());
        registroEntrada.setNumeroRegistroFormateado(registroSir.getNumeroRegistro());
        registroEntrada.setFecha(registroSir.getFechaRegistro());
        registroEntrada.setUsuario(usuario);
        registroEntrada.setOficina(oficinaActiva);
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroEntrada.setLibro(libro);

        // Obtenemos el Organismo destino indicado
        Organismo organismoDestino = organismoEjb.findByIdLigero(idOrganismoDestino);

        registroEntrada.setDestino(organismoDestino);
        registroEntrada.setDestinoExternoCodigo(null);
        registroEntrada.setDestinoExternoDenominacion(null);

        // RegistroDetalle
        registroEntrada.setRegistroDetalle(getRegistroDetalle(registroSir, idIdioma));
        
        // Interesados
        List<Interesado> interesados = procesarInteresados(registroSir.getInteresados());

        // Anexos
        List<AnexoFull> anexosFull = procesarAnexos(registroSir, camposNTIs);

        // Registramos el Registro Entrada
        registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario,interesados,anexosFull, true, false);

        /*
        // Creamos la TrazabilidadSir
        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_CONFIRMADO);
        trazabilidadSir.setRegistroSir(registroSir);
        trazabilidadSir.setRegistroEntrada(registroEntrada);
        trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
        trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
        trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
        trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
        trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
        trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
        trazabilidadSirEjb.persist(trazabilidadSir);
        */
        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        trazabilidad.setRegistroSir(registroSir);
        trazabilidad.setRegistroEntradaOrigen(null);
        trazabilidad.setOficioRemision(null);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(registroEntrada);
        trazabilidad.setFecha(new Date());
        trazabilidadEjb.persist(trazabilidad);
//      Si es estado final actualiza el registro con el estado aceptado (aceptado en geiser)
		if (RegwebUtils.contains(RegwebConstantes.ESTADOS_REGISTRO_SIR_FINALES, Integer.valueOf(registroSir.getEstado().getValue()))) {
			registroEntradaEjb.cambiarEstado(registroEntrada.getId(), RegwebConstantes.REGISTRO_OFICIO_SIR); // Revisar por parte del dep. de registro y distribuir
		}
        // Modificamos el estado del RegistroSir
//      modificarEstado(registroSir.getId(), EstadoRegistroSir.ACEPTADO);
        return registroEntrada;
    }
    
    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesadosSir
     * @return
     * @throws Exception
     */
    private List<Interesado> procesarInteresados(List<InteresadoSir> interesadosSir) throws Exception{
        List<Interesado> interesados = new ArrayList<Interesado>();

        for (InteresadoSir interesadoSir : interesadosSir) {
            Interesado interesado = transformarInteresado(interesadoSir);

            if (interesadoSir.getRepresentante()) {

                Interesado representante = transformarRepresentante(interesadoSir);
                representante.setIsRepresentante(true);
                representante.setRepresentado(interesado);
                interesado.setRepresentante(representante);

                interesados.add(interesado);
                interesados.add(representante);
            }else{
                interesados.add(interesado);
            }


        }
        return interesados;
    }

    /**
     * Transforma un {@link InteresadoSir} en un {@link Interesado}
     * @param interesadoSir
     * @return Interesado de tipo {@link Interesado}
     * @throws Exception
     */
    private Interesado transformarInteresado(InteresadoSir interesadoSir) throws Exception{

        Interesado interesado = new Interesado();
        interesado.setId((long) (Math.random() * 10000));
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if (StringUtils.isEmpty(interesadoSir.getRazonSocialInteresado())) {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotEmpty(interesadoSir.getRazonSocialInteresado())) {
            interesado.setRazonSocial(interesadoSir.getRazonSocialInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getNombreInteresado())) {
            interesado.setNombre(interesadoSir.getNombreInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getPrimerApellidoInteresado())) {
            interesado.setApellido1(interesadoSir.getPrimerApellidoInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getSegundoApellidoInteresado())) {
            interesado.setApellido2(interesadoSir.getSegundoApellidoInteresado());
        }
        if (interesadoSir.getTipoDocumentoIdentificacionInteresado() != null) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesadoSir.getTipoDocumentoIdentificacionInteresado().charAt(0)));
        }
        if (StringUtils.isNotEmpty(interesadoSir.getDocumentoIdentificacionInteresado())) {
            interesado.setDocumento(interesadoSir.getDocumentoIdentificacionInteresado());
        }

        if (StringUtils.isNotEmpty(interesadoSir.getCodigoPaisInteresado())) {
            try {
                interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoPaisInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoProvinciaInteresado())) {
            try {
                interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoMunicipioInteresado())) {
            try {
                interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(interesadoSir.getCodigoMunicipioInteresado()), Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(interesadoSir.getDireccionInteresado())) {
            interesado.setDireccion(interesadoSir.getDireccionInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCodigoPostalInteresado())) {
            interesado.setCp(interesadoSir.getCodigoPostalInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getCorreoElectronicoInteresado())) {
            interesado.setEmail(interesadoSir.getCorreoElectronicoInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getTelefonoInteresado())) {
            interesado.setTelefono(interesadoSir.getTelefonoInteresado());
        }
        if (StringUtils.isNotEmpty(interesadoSir.getDireccionElectronicaHabilitadaInteresado())) {
            interesado.setDireccionElectronica(interesadoSir.getDireccionElectronicaHabilitadaInteresado());
        }
        if (interesadoSir.getCanalPreferenteComunicacionInteresado() != null) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(interesadoSir.getCanalPreferenteComunicacionInteresado()));
        }
        if (StringUtils.isNotEmpty(interesadoSir.getObservaciones())) {
            interesado.setObservaciones(interesadoSir.getObservaciones());
        }

        return interesado;

    }


    /** Transforma un {@link InteresadoSir} en un {@link Interesado}
     *
     * @param representanteSir
     * @return Interesado de tipo {@link Interesado}
     */
    private Interesado transformarRepresentante(InteresadoSir representanteSir) {

        Interesado representante = new Interesado();
        representante.setId((long) (Math.random() * 10000));
        representante.setIsRepresentante(true);

        // Averiguamos que tipo es el Representante
        if (StringUtils.isEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setRazonSocial(representanteSir.getRazonSocialRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getNombreRepresentante())) {
            representante.setNombre(representanteSir.getNombreRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getPrimerApellidoRepresentante())) {
            representante.setApellido1(representanteSir.getPrimerApellidoRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getSegundoApellidoRepresentante())) {
            representante.setApellido2(representanteSir.getSegundoApellidoRepresentante());
        }
        if (representanteSir.getTipoDocumentoIdentificacionRepresentante() != null) {
            representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(representanteSir.getTipoDocumentoIdentificacionRepresentante().charAt(0)));
        }
        if (StringUtils.isNotEmpty(representanteSir.getDocumentoIdentificacionRepresentante())) {
            representante.setDocumento(representanteSir.getDocumentoIdentificacionRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoPaisRepresentante())) {
            try {
                representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoPaisRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoProvinciaRepresentante())) {
            try {
                representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoMunicipioRepresentante())) {
            try {
                representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(representanteSir.getCodigoMunicipioRepresentante()), Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(representanteSir.getDireccionRepresentante())) {
            representante.setDireccion(representanteSir.getDireccionRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCodigoPostalRepresentante())) {
            representante.setCp(representanteSir.getCodigoPostalRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getCorreoElectronicoRepresentante())) {
            representante.setEmail(representanteSir.getCorreoElectronicoRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getTelefonoRepresentante())) {
            representante.setTelefono(representanteSir.getTelefonoRepresentante());
        }
        if (StringUtils.isNotEmpty(representanteSir.getDireccionElectronicaHabilitadaRepresentante())) {
            representante.setDireccionElectronica(representanteSir.getDireccionElectronicaHabilitadaRepresentante());
        }
        if (representanteSir.getCanalPreferenteComunicacionRepresentante() != null) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(representanteSir.getCanalPreferenteComunicacionRepresentante()));
        }
        if (StringUtils.isNotEmpty(representanteSir.getObservaciones())) {
            representante.setObservaciones(representanteSir.getObservaciones());
        }

        return representante;

    }


    
    /**
     * Obtiene un {@link RegistroDetalle} a partir de los datos de un RegistroSir
     * @param registroSir
     * @param idIdioma
     * @return
     * @throws Exception
     */
    private RegistroDetalle getRegistroDetalle(RegistroSir registroSir, Long idIdioma) throws Exception{

        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroDetalle.setRecibidoSir(true);
        registroDetalle.setPresencial(false);
        registroDetalle.setExtracto(registroSir.getResumen());
        registroDetalle.setTipoDocumentacionFisica(Long.valueOf(registroSir.getDocumentacionFisica()));
        registroDetalle.setIdioma(idIdioma);
       // registroDetalle.setTipoAsunto(new TipoAsunto(idTipoAsunto));
        registroDetalle.setCodigoAsunto(null);

        if(registroSir.getTipoTransporte() != null){
            registroDetalle.setTransporte(Long.valueOf(registroSir.getTipoTransporte()));
        }
        if(StringUtils.isNotEmpty(registroSir.getNumeroTransporte())){
            registroDetalle.setNumeroTransporte(registroSir.getNumeroTransporte());
        }
        if(StringUtils.isNotEmpty(registroSir.getObservacionesApunte())){
            registroDetalle.setObservaciones(registroSir.getObservacionesApunte());
        }
        if(StringUtils.isNotEmpty(registroSir.getReferenciaExterna())){
            registroDetalle.setReferenciaExterna(registroSir.getReferenciaExterna());
        }
        if(StringUtils.isNotEmpty(registroSir.getNumeroExpediente())){
            registroDetalle.setExpediente(registroSir.getNumeroExpediente());
        }
        if(StringUtils.isNotEmpty(registroSir.getExpone())){
            registroDetalle.setExpone(registroSir.getExpone());
        }
        if(StringUtils.isNotEmpty(registroSir.getSolicita())){
            registroDetalle.setSolicita(registroSir.getSolicita());
        }

        registroDetalle.setOficinaOrigen(null);
        registroDetalle.setOficinaOrigenExternoCodigo(registroSir.getCodigoEntidadRegistralOrigen());
        registroDetalle.setOficinaOrigenExternoDenominacion(registroSir.getDecodificacionEntidadRegistralOrigen());

        registroDetalle.setNumeroRegistroOrigen(registroSir.getNumeroRegistroOrigen());
        registroDetalle.setFechaOrigen(registroSir.getFechaRegistroOrigen());
        
        // Interesados
        registroDetalle.setInteresados(null);

        // Anexos
        //registroDetalle.setAnexos(null);

        return registroDetalle;
    }

    /**
    *
    * @param registroSir
    * @param camposNTIs representa la lista de anexos del RegistroSir en los que el usuario ha especificado
    *                          los valores de los campos NTI no informados por SICRES (validez Documento, origen, Tipo Documental)
    * @return
    * @throws Exception
    */
   private List<AnexoFull> procesarAnexos(RegistroSir registroSir, List<CamposNTI> camposNTIs) throws Exception {

       HashMap<String,AnexoFull> anexosProcesados = new HashMap<String, AnexoFull>();

       // Procesamos los Documentos con firma Attached o sin firma
       for (AnexoSir anexoSir : registroSir.getAnexos()) {
       	// Los registros se aceptan en GEISER, el usuario no puede indicar los metadatos NTI
       	CamposNTI campoNti = new CamposNTI();
       	campoNti.setId(anexoSir.getId());
       	campoNti.setIdOrigen(ANEXO_ORIGEN_ADMINISTRACION.longValue());
       	campoNti.setIdTipoDocumental(null); // Otros
       	campoNti.setIdValidezDocumento(anexoSir.getValidezDocumento());
       	camposNTIs.add(campoNti);
       	
           for (CamposNTI cnti : camposNTIs) {
           	if (anexoSir.getId() != null) {
	                if (anexoSir.getId().equals(cnti.getId())) {
	                    transformarAnexoDocumento(anexoSir, registroSir.getEntidad().getId(), cnti, anexosProcesados,anexoSir.getRegistroSir().getAplicacion());
	                }
           	}
           }
       }

       // Procesamos las Firma detached
       for (AnexoSir anexoSir : registroSir.getAnexos()) {
           transformarAnexoFirmaDetached(anexoSir, anexosProcesados,registroSir.getEntidad().getId(), anexoSir.getRegistroSir().getAplicacion());
       }

       // Eliminam duplicats
       Set<AnexoFull> set = new HashSet<AnexoFull>(anexosProcesados.values());

       return new ArrayList<AnexoFull>(set);
   }

   /**
    * Transforma un {@link AnexoSir} en un {@link AnexoFull}
    * A partir de la clase AnexoSir transformamos a un AnexoFull para poder guardarlo en regweb3.
    * La particularidad de este método, es que se necesita pasar una lista de los anexos que se han procesado anteriormente
    * del AnexoSir que nos envian, ya que puede haber anexos que son firma de uno anteriormente procesado y lo necesitamos
    * para acabar de montar el anexo ya que para regweb3 el anexo y su firma van en el mismo AnexoFull.
    * Además ahora se pasa una lista de anexosSirRecibidos ya que para cada anexo el usuario debe escoger 3 campos que
    * pueden no venir informados en SICRES y son obligatorios en NTI.
    * Los campos en concreto son (validezDocumento, origen, tipo Documental)
    * @param anexoSir
    * @param idEntidad
    * @return AnexoFull tipo {@link AnexoFull}
    */
   private void transformarAnexoDocumento(AnexoSir anexoSir, Long idEntidad, CamposNTI camposNTI, HashMap<String,AnexoFull> anexosProcesados, String aplicacion) throws Exception {

       // Solo procesamos Documentos no firmados o firmados attached, no las firmas detached
       if((StringUtils.isEmpty(anexoSir.getIdentificadorDocumentoFirmado()) ||
               anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero())) && !anexoSir.isAnexoFirma()){

           AnexoFull anexoFull = new AnexoFull();
           Anexo anexo = new Anexo(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

           //TODO Temporal hata que se acepten todos los registros sir pendientes con anexos que tienen caracteres prohibidos
           anexo.setTitulo(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosArxiu(anexoSir.getNombreFichero(), '_'));

           // Validez Documento
           if (anexoSir.getValidezDocumento() != null) {
               //Transformamos de copia compulsada a copia_original = autèntica
               if(Long.valueOf(anexoSir.getValidezDocumento()).equals(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA)){
                   anexo.setValidezDocumento(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL);
               }else {
                   anexo.setValidezDocumento(Long.valueOf(anexoSir.getValidezDocumento()));
               }
           } else {//Campo NTI Cogemos la validez de documento indicada por el usuario
               if (camposNTI.getIdValidezDocumento() != null) {
                   anexo.setValidezDocumento(Long.valueOf(camposNTI.getIdValidezDocumento()));

               }else{ //Si no hay valor, por defecto "Copia"
                   anexo.setValidezDocumento(TIPOVALIDEZDOCUMENTO_COPIA);
               }
           }

           // Tipo Documento
           if (anexoSir.getTipoDocumento() != null) {
               anexo.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento()));

               // Si es un Documento técnico, ponemos el Origen a ADMINSITRACIÓN
               if(anexoSir.getTipoDocumento().equals(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO_SICRES)){
                   anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
               }
           }
           anexo.setObservaciones(anexoSir.getObservaciones());

           //Campo NTI no informados, asignamos lo que indica el usuario
           if (camposNTI.getIdOrigen() != null) {
               anexo.setOrigenCiudadanoAdmin(camposNTI.getIdOrigen().intValue());

           }else{ // Si no está informado, por defecto Ciudadano
               anexo.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
           }

           // Si el usuario no especifica el tipo Documental, por defecto se pone TD99 - Otros
           if (camposNTI.getIdTipoDocumental() == null || camposNTI.getIdTipoDocumental().equals("")) {
               anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
           }else{
               anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad(camposNTI.getIdTipoDocumental(), idEntidad));
           }

           if(anexoSir.getCertificado()!= null) {
               anexo.setCertificado(anexoSir.getCertificado().getBytes());
           }

           if (anexoSir.getFirma() != null) {
               anexo.setFirma(anexoSir.getFirma());

           }
           if (anexoSir.getTimestamp() != null) {
               anexo.setTimestamp(anexoSir.getTimestamp().getBytes());
           }

           if (anexoSir.getValidacionOCSPCertificado() != null) {
               anexo.setValidacionOCSPCertificado(anexoSir.getValidacionOCSPCertificado().getBytes());
           }

           if(anexoSir.getHash()!= null){
               anexo.setHash(anexoSir.getHash().getBytes());
           }

           DocumentCustody dc;
           SignatureCustody sc;
           // Si el IdentificadorDocumentoFirmado está informado
           if (es.caib.regweb3.utils.StringUtils.isNotEmpty(anexoSir.getIdentificadorDocumentoFirmado())) {

               // Si el IdentificadorDocumentoFirmado es igual al IdentificadorFichero, es una Firma Attached
               if(anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero()) && anexoSir.getFirma() == null){
                   /** PARCHE ESPU*/
                   if(RegwebConstantes.APLICACION_SIR_ESPU.equals(aplicacion)){
                       //log.info("Documento con firma attached aplicación ESPU: " + anexoSir.getIdentificadorFichero());
                       //En este caso se guarda como un no firmado
                       anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
                       dc = getDocumentCustody(anexoSir);
                       anexoFull.setAnexo(anexo);
                       anexoFull.setDocumentoCustody(dc);
                   }else{
                       //log.info("Documento con firma attached: " + anexoSir.getIdentificadorFichero());
                       //Caso Firma Attached caso 5, se guarda el documento en signatureCustody, como lo especifica el API DE CUSTODIA(II)
                       anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
                       sc = getSignatureCustody(anexoSir, null, anexo.getModoFirma());
                       anexoFull.setDocumentoCustody(null);
                       anexoFull.setSignatureCustody(sc);
                       anexoFull.setAnexo(anexo);
                   }

               }

           } else { // El anexo no es firma de nadie
               //log.info("Documento sin firma: " + anexoSir.getIdentificadorFichero());
               anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);

               /** PARCHE GREG PROBLEMA: El campo  firma que se informa es más grande que 255 y al intentar hacer el insert peta por superar longitud
                * De momento cortamos el campo, pero se debe informar a MADRID de este caso concreto */
               if (anexoSir.getFirma() != null) { // Anexo con Firma CSV
                   if(anexoSir.getFirma().length() >= 255) {
                       anexo.setCsv(null);
                   }else{
                       anexo.setCsv(anexoSir.getFirma());
                   }
                   //TODO Metadada a custodia pel csv.
               }
               dc = getDocumentCustody(anexoSir);
               anexoFull.setAnexo(anexo);
               anexoFull.setDocumentoCustody(dc);
           }

           anexosProcesados.put(anexoSir.getIdentificadorFichero(), anexoFull);

       }
   }


   /**
    * Transforma un {@link AnexoSir} en un {@link AnexoFull}
    * A partir de la clase AnexoSir transformamos a un AnexoFull para poder guardarlo en regweb3.
    * La particularidad de este método, es que se necesita pasar una lista de los anexos que se han procesado anteriormente
    * del AnexoSir que nos envian, ya que puede haber anexos que son firma de uno anteriormente procesado y lo necesitamos
    * para acabar de montar el anexo ya que para regweb3 el anexo y su firma van en el mismo AnexoFull.
    * Además ahora se pasa una lista de anexosSirRecibidos ya que para cada anexo el usuario debe escoger 3 campos que
    * pueden no venir informados en SICRES y son obligatorios en NTI.
    * Los campos en concreto son (validezDocumento, origen, tipo Documental)
    * @param anexoSir
    * @param anexosProcesados Lista de anexos procesados anteriores.
    * @return AnexoFull tipo {@link AnexoFull}
    */
   private void transformarAnexoFirmaDetached(AnexoSir anexoSir, Map<String, AnexoFull> anexosProcesados, Long idEntidad, String aplicacion) throws Exception {

//       // En este método solo se procesan las firmas detached(aquellas que nos informan
//       // con el identificador de documento firmado, que son firma de otro segmento anexo
//       if (es.caib.regweb3.utils.StringUtils.isNotEmpty(anexoSir.getIdentificadorDocumentoFirmado()) &&
//               !anexoSir.getIdentificadorDocumentoFirmado().equals(anexoSir.getIdentificadorFichero())) {
     // En este método solo se procesan las firmas detached
     if (es.caib.regweb3.utils.StringUtils.isEmpty(anexoSir.getIdentificadorDocumentoFirmado()) &&
   		  es.caib.regweb3.utils.StringUtils.isEmpty(anexoSir.getFirma())) {
           log.info("Firma detached del documento: " + anexoSir.getIdentificadorFichero());

           AnexoFull anexoFirmado = anexosProcesados.get(anexoSir.getIdentificadorFichero());//obtenemos el anexo firmado previamente procesado
           if(anexoFirmado.getAnexo().getModoFirma() != MODO_FIRMA_ANEXO_ATTACHED) { // si la firma detached es de un firma attached, la descartamos


               byte[] anexoSirData = FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId());

               // CASO ORVE BASE64 Decodificamos previamente porque vienen las firmas codificadas en base64
               if (Base64.isBase64(anexoSirData)) {
                   log.info("Entramos en decodificar caso ORVE");
                   anexoSirData = Base64.decodeBase64(anexoSirData);
                   anexoSir.setAnexoData(anexoSirData);
               }

               if (CXFUtils.isXMLFormat(anexoSirData)) { //Miramos si es un formato XML
                   //A pesar de que por identificador de documento firmado nos indican que es una firma detached, debemos
                   // averiguar si es una firma attached o detached  y esto nos lo indica el contenido del xml que nos envian.

                   /*******  COMENTAMOS TEMPORALMENTE MIRAR EL FORMATO DEL XADES YA QUE EN TODOS LOS CASOS SE ELIMINA EL XSIG, LO HACEMOS A RAIZ DEL PROBLEMA DE FORMATO DE GEISER CON EL XML CON CAPA <AFIRMA></AFIRMA>  ********/
               String format = getXAdESFormat(anexoSirData);

               if(SIGNFORMAT_EXPLICIT_DETACHED.equals(format)){// XADES Detached
                   //Obtenemos el anexo original cuya firma es la que estamos tratando, que ha sido previamente procesado
                   AnexoFull anexoFull = anexosProcesados.get(anexoSir.getIdentificadorFichero());

                   if(anexoFull.getSignatureCustody() == null) { // Es un documento sin firma(documento plano)
                       //Descartamos la firma xsig y lanzamos mensaje.
                       log.warn("FIRMA d'un anexoSir(document pla) ja que es tipus Xades Detached no suportada per ARXIU: només guardam document ");

                       // Descomentar aquest codi quan arxiu deixi guardar xsig (propuesta de toni, pero esta mas ordenado en el codigo de marilen
                       //CASO XADES DETACHED DE DOCUMENT PLA = CADES (AFEGIT MARILEN, CODI OPTIMITZAT, descomentar quan ARXIU guardi Xsig)
                       //Caso Firma Detached, caso 4, se guarda 1 anexo, con el doc original en documentCustody y la firma en SignatureCustody
                       anexoFull.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED); // asignamos el modo de firma
                       SignatureCustody sc = getSignatureCustody(anexoSir, anexoFull.getDocumentoCustody(), anexoFull.getAnexo().getModoFirma()); //Asignamos la firma
                       anexoFull.setSignatureCustody(sc);

                   }else{ //Es un documento firmado que además nos envian el xsig como detached.

                       log.warn("Descartat FIRMA d'un anexoSir(documentsignat) ja que es tipus Xades Detached no suportada per ARXIU: només guardam document ");

                       // Descomentar aquest codi quan arxiu deixi guardar xsig

                       //NO SOPORTADO POR MODELO CUSTODIA
                       //Obtenemos el documento firmado que está en signatureCustody porque ha sido procesado
                       // anteriormente.
                       SignatureCustody scAntic = anexoFull.getSignatureCustody();

                       // Apunta a una signatura (attached o detached): collim la signatura com a doc detached
                       //Creamos un nuevo documentCustody para guardar el documento firmado
                       DocumentCustody  dc = new DocumentCustody(scAntic.getName(), scAntic.getMime(), scAntic.getData());

                       /**//*Montamos el nuevo anexo
                           -(DocumentCustody = documento firmado)
                           -(signatureCustody = firma detached que estamos tratando)
                       *//**/
                       AnexoFull anexoFullnou = new AnexoFull();
                       anexoFullnou.setDocumentoCustody(dc);

                       SignatureCustody sc = new SignatureCustody();
                       sc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
                       sc.setMime(anexoSir.getTipoMIME());
                       sc.setName(anexoSir.getNombreFichero());
                       anexoFullnou.setSignatureCustody(sc);

                       anexosProcesados.put(anexoSir.getIdentificadorFichero(),anexoFullnou );

                   }

               }else{
                   // XADES attached
                   // CAS ORVE:  es una XADES attached pero AnexoSIR apunta a un DETACHED

                   // nomes ens serveix el contingut que ja s'ha donat d'alta: no feim res per que ARXIU no funciomna
                   //log.warn("AnexoSir-Detached amb firma tipus Xades attached (CAS ORVE)");

                   // Descomentar aquest codi quan arxiu deixi guardar xsig i si es vol guardar dins arxiu la firma de comunicació

                   //En este caso, nos mandan un xml attached, como detached. Por tanto lo guardaremos como una firma attached aislada
                   //Aquí perdemos el vinculo al documento del que nos indican que es su firma detached(lo cual no es cierto)
                   SignatureCustody sc = new SignatureCustody();

                   sc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
                   sc.setMime(anexoSir.getTipoMIME());
                   sc.setName(anexoSir.getNombreFichero());
                   sc.setAttachedDocument(true);

                   //Montamos el nuevo anexoFull como attached, ya que es un xades attached
                   AnexoFull anexoFullnou = new AnexoFull();
                   Anexo anexoNou = new Anexo();
                   anexoFullnou.setSignatureCustody(sc);
                   anexoFullnou.setDocumentoCustody(null);
                   anexoNou.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);

                   //Debemos construir el anexo completo, porque es nuevo.
                   anexoNou.setTitulo(anexoSir.getNombreFichero());
                   anexoNou.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
                   anexoNou.setObservaciones(anexoSir.getObservaciones());
                   anexoNou.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
                   anexoNou.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
                   if(anexoSir.getTipoDocumento()!=null) {
                       anexoNou.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento()));
                   }
                   if(anexoSir.getCertificado()!= null) {
                       anexoNou.setCertificado(anexoSir.getCertificado().getBytes());
                   }
                   if (anexoSir.getFirma() != null) {
//                       anexoNou.setFirma(anexoSir.getFirma().getBytes());
                   	anexoNou.setFirma(anexoSir.getFirma());
                   }
                   if (anexoSir.getTimestamp() != null) {
                       anexoNou.setTimestamp(anexoSir.getTimestamp().getBytes());
                   }
                   if (anexoSir.getValidacionOCSPCertificado() != null) {
                       anexoNou.setValidacionOCSPCertificado(anexoSir.getValidacionOCSPCertificado().getBytes());
                   }
                   if(anexoSir.getHash()!= null){
                       anexoNou.setHash(anexoSir.getHash().getBytes());
                   }
                   anexoFullnou.setAnexo(anexoNou);


                   anexosProcesados.put(anexoSir.getIdentificadorFichero(),anexoFullnou );

               }
               } else {
                   log.info("Entro en CADES");
                   // CADES

                   //Caso Firma Detached, caso 4, se guarda 1 anexo, con el doc original en documentCustody y la firma en SignatureCustody
                   anexoFirmado.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED); // asignamos el modo de firma
                   SignatureCustody sc = getSignatureCustody(anexoSir, anexoFirmado.getDocumentoCustody(), anexoFirmado.getAnexo().getModoFirma()); //Asignamos la firma
                   anexoFirmado.setSignatureCustody(sc);

               }

           }

       }

   }


   /**
    *
    * @param anexoSir
    * @return
    * @throws Exception
    */
   private DocumentCustody getDocumentCustody(AnexoSir anexoSir) throws Exception {
       if (log.isDebugEnabled()) {
           log.debug("  ------------------------------");
           log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
       }
       DocumentCustody dc = null;
       if (anexoSir.getAnexo() != null) {
           dc = new DocumentCustody();
           dc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
           dc.setMime(anexoSir.getTipoMIME());
           dc.setName(anexoSir.getNombreFichero());
       }
       return dc;
   }

   /**
    *
    * @param anexoSir
    * @param dc
    * @param modoFirma
    * @return
    * @throws Exception
    */
   private SignatureCustody getSignatureCustody(AnexoSir anexoSir, DocumentCustody dc,
                                                int modoFirma) throws Exception {
       if (log.isDebugEnabled()) {
           log.debug("  ------------------------------");
           log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
       }

       SignatureCustody sc = null;
       if (anexoSir.getAnexo() == null) {

           if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                   || modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
               String msg = "L'usuari ens indica que hi ha una firma a "+anexoSir.getIdentificadorFichero()+" i no ve (modoFirma = " + modoFirma + ")";
               log.info(msg, new Exception());
               throw new Exception(msg);
           }

       } else {

           if (modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                   && modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
               String msg = "L'usuari ens indica que NO hi ha una a "+anexoSir.getIdentificadorFichero()+" firma pero n'envia una"
                       + " (modoFirma = " + modoFirma + ")";
               log.error(msg, new Exception());
               throw new Exception(msg);
           }



           sc = new SignatureCustody();

           //Averiguamos si está en Base64 para decodificarlo y luego que se valide bien la firma.
           byte[] anexoData = FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId());
           if(Base64.isBase64(anexoData)){
               log.info("Entramos en decodificar base64");
               anexoData=Base64.decodeBase64(anexoData);
           };
           sc.setData(anexoData);
           sc.setMime(anexoSir.getTipoMIME());
           sc.setName(anexoSir.getNombreFichero());


           if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
               // Document amb firma adjunta
               sc.setAttachedDocument(null);

               // TODO Emprar mètode per descobrir tipus de signatura
               sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

           } else if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
               // Firma en document separat CAS 4
               if (dc == null) {
                   throw new Exception("Aquesta firma "+ anexoSir.getIdentificadorFichero() +"  requereix el document original"
                           + " i no s'ha enviat");
               }

               sc.setAttachedDocument(false);
               // TODO Emprar mètode per descobrir tipus de signatura
               sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
           }
       }
       return sc;
   }
   

   /** La firma està continguda dins del document: PADES, ODT, OOXML */
   public static final String SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED = "implicit_enveloped/attached";

   /** La firma conté al document: Xades ATTACHED */
   public static final String SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED = "implicit_enveloping/attached";

   /**
    * El documetn està forà de la firma: xades detached i cades detached
    */
   public static final String SIGNFORMAT_EXPLICIT_DETACHED = "explicit/detached";

   /**
    * Cas específic de Xades externally detached
    */
   public static final String SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED = "explicit/externally_detached";


   /**
    * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
    * @param signature
    * @return
    */
   private  String getXAdESFormat(byte[] signature) throws Exception {

       DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
       dBFactory.setNamespaceAware(true);

       Document eSignature = dBFactory.newDocumentBuilder().parse(
          new ByteArrayInputStream(signature));

       XMLSignature xmlSignature;
       String rootName = eSignature.getDocumentElement().getNodeName();
       if (rootName.equalsIgnoreCase("ds:Signature") || rootName.equals("ROOT_COSIGNATURES")) {
           //  "XAdES Enveloping"
           return SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
       }
       NodeList signatureNodeLs = eSignature.getElementsByTagName("ds:Manifest");
       if (signatureNodeLs.getLength() > 0) {
           //  "XAdES Externally Detached
           return SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED;
       }
       NodeList signsList = eSignature.getElementsByTagNameNS(
          "http://www.w3.org/2000/09/xmldsig#", "Signature");
       if (signsList.getLength() == 0) {
           throw new Exception("No te firmes");
       }
       Node signatureNode = signsList.item(0);
       try {
           xmlSignature = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
       } catch (MarshalException e) {
           throw new Exception("marshal exception: " + e.getMessage(), e);
       }
       List<?> references = xmlSignature.getSignedInfo().getReferences();
       for (int i = 0; i < references.size(); ++i) {
           if (!"".equals(((Reference) references.get(i)).getURI()))
               continue;
           //  "XAdES Enveloped"
           return SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
       }
       //  "XAdES Detached"
       return SIGNFORMAT_EXPLICIT_DETACHED;
   }

	@Override
	public RegistroSir findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RegistroSir getReference(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<RegistroSir> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Long getTotal() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<RegistroSir> getPagination(int inicio) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}