package es.caib.regweb3.persistence.ejb;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.geiser.api.GeiserPluginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.persistence.utils.IntegracionGeiserHelper;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SirEnvioEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class SirEnvioBean implements SirEnvioLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;
    @EJB private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;
    @EJB private EmisionLocal emisionEjb;
    @EJB private MensajeLocal mensajeEjb;
    @EJB private MensajeControlLocal mensajeControlEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private DistribucionLocal distribucionEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private EntidadLocal entidadEjb;
    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private JustificanteLocal justificanteEjb;
    
//    @Autowired private ArxiuCaibUtils arxiuCaibUtils;
//    @Autowired private ConversionHelper conversioHelper;
    @Autowired 
    private IntegracionGeiserHelper integracionGeiserHelper;
    
    @Resource
    private javax.ejb.SessionContext ejbContext;


    /**
     * Creamos el Intercambio y el Oficio de remisión SIR
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public RegistroEntrada crearIntercambioEntrada(RegistroEntrada registroEntrada, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = null;

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Crear intercambio a " + codigoOficinaSir;
        peticion.append("TipoRegistro: ").append("Entrada").append(System.getProperty("line.separator"));
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));
        peticion.append("Número registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        try {

            // OficinaSir destino
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaSirDestino = oficinasService.obtenerOficina(codigoOficinaSir, null, null);

            // Actualizamos el Registro con campos SIR
            registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
            
//            registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroEntrada.getOficina().getCodigo(), usuario.getEntidad()));
            
            registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
            registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
            registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
            registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

            // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
            registroDetalle.setOficinaOrigen(registroEntrada.getOficina());
            registroDetalle.setOficinaOrigenExternoCodigo(null);
            registroDetalle.setOficinaOrigenExternoDenominacion(null);
//            registroDetalle.setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
//            registroDetalle.setFechaOrigen(registroEntrada.getFecha());

            // Actualizamos el registro
            registroEntrada = registroEntradaEjb.merge(registroEntrada);

            // Crear y registrar el Oficio de remisión
            oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionSIR(registroEntrada, oficinaActiva, usuario, oficinaSirDestino);

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(oficioRemision.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(oficioRemision.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(oficioRemision.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(
            		inicio, 
            		RegwebConstantes.INTEGRACION_SIR, 
            		descripcion, 
            		peticion.toString(), 
            		System.currentTimeMillis() - tiempo, 
            		usuario.getEntidad().getId(), 
            		oficioRemision.getIdentificadorIntercambio());

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (oficioRemision != null) {
                integracionEjb.addIntegracionError(
                		RegwebConstantes.INTEGRACION_SIR, 
                		descripcion, 
                		peticion.toString(), 
                		s, 
                		null, 
                		System.currentTimeMillis() - tiempo, 
                		usuario.getEntidad().getId(), 
                		oficioRemision.getIdentificadorIntercambio());
            }
            throw s;
        }

        return registroEntrada;
    }

    /**
     * Creamos el Intercambio y el Oficio de remisión SIR
     * @param registroSalida
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public RegistroSalida crearIntercambioSalida(RegistroSalida registroSalida, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = null;

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Crear intercambio a " + codigoOficinaSir;
        peticion.append("TipoRegistro: ").append("Salida").append(System.getProperty("line.separator"));
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));
        peticion.append("Número registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        try {

            // OficinaSir destino
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaSirDestino = oficinasService.obtenerOficina(codigoOficinaSir, null, null);

            // Actualizamos el Registro con campos SIR
            registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
            
//            registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroSalida.getOficina().getCodigo(), usuario.getEntidad()));
            
            registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
            registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
            registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
            registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

            // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
            registroDetalle.setOficinaOrigen(registroSalida.getOficina());
//            registroDetalle.setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
//            registroDetalle.setFechaOrigen(registroSalida.getFecha());

            // Actualizamos el registro
            registroSalida = registroSalidaEjb.merge(registroSalida);

            // Crear y registrar el Oficio de remisión
            oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionSIR(registroSalida, oficinaActiva, usuario, oficinaSirDestino);

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(oficioRemision.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(oficioRemision.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(oficioRemision.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

//			integracionEjb.addIntegracionOk(
//					inicio, 
//					RegwebConstantes.INTEGRACION_SIR, 
//					descripcion, 
//					peticion.toString(),
//					System.currentTimeMillis() - tiempo, 
//					usuario.getEntidad().getId(),
//					oficioRemision.getIdentificadorIntercambio());

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (oficioRemision != null) {
				integracionEjb.addIntegracionError(
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion, 
						peticion.toString(),
						s, 
						null, 
						System.currentTimeMillis() - tiempo, 
						usuario.getEntidad().getId(),
						oficioRemision.getIdentificadorIntercambio());
            }
            throw s;
        }

        return registroSalida;
    }


    /**
     * @param registro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public RegistroSir enviarIntercambio(Long tipoRegistro, IRegistro registro, Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException {
        RegistroSir registroSir = null;
        String numRegistro = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Registro en GEISER y envío intercambio a " + codigoOficinaSir;
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));
        if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
            peticion.append("tipoRegistro: ").append(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO).append(System.getProperty("line.separator"));
        } else {
            peticion.append("tipoRegistro: ").append(RegwebConstantes.REGISTRO_SALIDA_ESCRITO).append(System.getProperty("line.separator"));
        }
        try {

            // OficinaSir destino
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaSirDestino = oficinasService.obtenerOficina(codigoOficinaSir, null, null);
            RegistroEntrada registroEntrada = null;
            RegistroSalida registroSalida = null;
            log.info("----------------------------------------------------------------------------------------------");
            log.info("Enviando FicheroIntercambio del registro: " + registro.getNumeroRegistroFormateado() + " mediante SIR a: " + oficinaSirDestino.getDenominacion());
            log.info("");

            if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {

                // Creamos el Intercambio y el Oficio de remisión SIR
                registroEntrada = (RegistroEntrada) registro;
                registroEntrada = crearIntercambioEntrada(registroEntrada, oficinaActiva, usuario, codigoOficinaSir);

                // Añadimos los anexos cargados anteriormente, para no tener que volver a hacerlo
                registroEntrada.getRegistroDetalle().setAnexosFull(registro.getRegistroDetalle().getAnexosFull());

                //Transformamos el registro de Entrada a RegistroSir
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            } else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)) {

                // Creamos el Intercambio y el Oficio de remisión SIR
                registroSalida = (RegistroSalida) registro;
                registroSalida = crearIntercambioSalida(registroSalida, oficinaActiva, usuario, codigoOficinaSir);

                // Añadimos los anexos cargados anteriormente, para no tener que volver a hacerlo
                registroSalida.getRegistroDetalle().setAnexosFull(registro.getRegistroDetalle().getAnexosFull());

                // Transformamos el RegistroSalida en un RegistroSir
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

            }
            
            try{
                // Integración
                peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
                peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
                peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

                //Envío GEISER
    	        try {
    	        	// Realizar registro SIR
    	        	if (registro instanceof RegistroEntrada) {
    	        		integracionGeiserHelper.realizarRegistroSir(registroSir, registroEntrada, usuario, peticion);
    	        		
    	        		registroEntradaEjb.merge(registroEntrada);
	    	            numRegistro = registroEntrada.getNumeroRegistro();
	    	            peticion.append("Número registro: ").append(numRegistro).append(System.getProperty("line.separator"));
    	        	} else  {
    	        		integracionGeiserHelper.realizarRegistroSir(registroSir, registroSalida, usuario, peticion);
    	        		
    	        		registroSalidaEjb.merge(registroSalida);
	    	            numRegistro = registroSalida.getNumeroRegistro();
	    	            peticion.append("Número registro: ").append(numRegistro).append(System.getProperty("line.separator"));
    	        	}
    	        	
    	        	em.persist(registroSir);
    	        	
    	        	if (registroSir.getInteresados() != null) {
    					for (InteresadoSir interesadoSir : registroSir.getInteresados()) {
    						interesadoSir.setRegistroSir(registroSir);
    						interesadoSirEjb.merge(interesadoSir);
    					}
    				}
    				if (registroSir.getAnexos() != null) {
    					for (AnexoSir anexoSir : registroSir.getAnexos()) {
    						anexoSir.setRegistroSir(registroSir);
    						anexoSirEjb.merge(anexoSir);
    					}
    				}
    				
//    	        	actualizarEnvioSirRealizado(registroSir, usuario);
    	        	
     	        } catch (GeiserPluginException gpe) {
     	        	// solo eliminar anexos si viene de ws (si es presencial, siempre habrá el registro creado, los SIR primero se guardan los anexos y depsues
     	        	// se registra)
     	        	if (! registro.getRegistroDetalle().getPresencial()) {
     	        		anexoEjb.eliminarAnexosRegistro(registro);
     	        	}
     	        	
    	        	log.error("Ha habido un error realizando el registro en GEISER");
					integracionEjb.addIntegracionError(
							RegwebConstantes.INTEGRACION_SIR, 
							descripcion,
							peticion.toString(), 
							gpe, 
							null, 
							System.currentTimeMillis() - tiempo,
							usuario.getEntidad().getId(), 
							numRegistro);
                    gpe.printStackTrace();
    				throw gpe;
    	        }
                // Integración
				integracionEjb.addIntegracionOk(
						inicio, 
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion,
						peticion.toString(), 
						System.currentTimeMillis() - tiempo, 
						registroSir.getEntidad().getId(),
						numRegistro);

            }catch (Exception e){
                e.printStackTrace();
				integracionEjb.addIntegracionError(
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion, 
						peticion.toString(),
						e, 
						null, 
						System.currentTimeMillis() - tiempo, 
						usuario.getEntidad().getId(), 
						numRegistro);
				ejbContext.setRollbackOnly();
				throw e;
            }

            log.info("");
            log.info("Fin enviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (registroSir != null) {
				integracionEjb.addIntegracionError(
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion, 
						peticion.toString(),
						s, 
						null, 
						System.currentTimeMillis() - tiempo, 
						usuario.getEntidad().getId(),
						registroSir.getIdentificadorIntercambio());
            }
            throw s;
        }

        return registroSir;
    }
    
    @Override
    public List<RegistroSir> actualizarEstadoEnvioSir(Entidad entidad, OficioRemision oficioRemision, UsuarioEntidad usuario) throws Exception, I18NException {
    	StringBuilder peticion = new StringBuilder();
    	String descripcion = "[MANUAL] Actualizando estado oficio SIR (idOficioSir=" + oficioRemision.getId() + ")";
        long tiempo = System.currentTimeMillis();
        List<RegistroSir> registroSirActualizados = new ArrayList<RegistroSir>();
    	try {
    		List<RegistroEntrada> registrosEntradaOficioRemision = oficioRemision.getRegistrosEntrada();
    		for (RegistroEntrada registroEntrada : registrosEntradaOficioRemision) {
    			Long registroSirId = registroSirEjb.getRegistroSirByNumeroRegistro(
    					registroEntrada.getNumeroRegistro(), 
    					oficioRemision.getCodigoEntidadRegistralDestino());
    			RegistroSir registroSir = registroSirEjb.findById(registroSirId);
			    if (registroSir != null) {
			    	descripcion += "\n  [MANUAL] Actualizando estado envío SIR (idEnvioSir=" + registroSir.getId() + ")";
//		    		actualizarEnvioSir(registroSir, entidad, oficioRemision, true);
		    		integracionGeiserHelper.actualizarEstadoRegistroSir(registroSir, oficioRemision);
		    		registroSirActualizados.add(registroSir);
		    		registroSirEjb.merge(registroSir);
			    }
    		}
    		List<RegistroSalida> registrosSalidaOficioRemision = oficioRemision.getRegistrosSalida();
    		for (RegistroSalida registroSalida : registrosSalidaOficioRemision) {
    			Long registroSirId = registroSirEjb.getRegistroSirByNumeroRegistro(
    					registroSalida.getNumeroRegistro(), 
    					oficioRemision.getCodigoEntidadRegistralDestino());
    			
    			// Arregla bug registres sir sense entrada a rwe_registro_sir
    			if (registroSirId == null) {
    				RegistroSir registroSirFromSalida = registroSirEjb.transformarRegistroSalidaAndCrearRegistroSir(registroSalida, usuario);
    				registroSirId = registroSirFromSalida.getId();
    			}
    			
    			RegistroSir registroSir = registroSirEjb.findById(registroSirId);
			    if (registroSir != null) {
			    	descripcion += "\n  [MANUAL] Actualizando estado envío SIR (idEnvioSir=" + registroSir.getId() + ")";
			    	integracionGeiserHelper.actualizarEstadoRegistroSir(registroSir, oficioRemision);
//		    		actualizarEnvioSir(registroSir, entidad, oficioRemision, true);
		    		registroSirActualizados.add(registroSir);
		    		registroSirEjb.merge(registroSir);
			    }
    		}
        } catch (Exception | I18NException e) {
        	peticion.append("Oficio remision: ").append(oficioRemision.getId()).append(System.getProperty("line.separator"));
        	e.printStackTrace();
			integracionEjb.addIntegracionError(
					RegwebConstantes.INTEGRACION_SIR, 
					descripcion, 
					peticion.toString(), 
					e, 
					null, 
					System.currentTimeMillis() - tiempo, 
					entidad.getId(), 
					null);
			throw e;
		} 
		return registroSirActualizados;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 3000)  // 50 minutos
    @Override
    public void actualizarEnvioSir(Entidad entidad, Long registroSirId) throws Exception {
    	long tiempo = System.currentTimeMillis();
		try {
			StringBuilder peticion = new StringBuilder();
	    	String descripcion = "Actualizando estado envío SIR (idEnvioSir=" + registroSirId + ")";
			try {
				RegistroSir registroSir = registroSirEjb.findById(registroSirId);
		        peticion.append("Número registro: ").append(registroSir.getNumeroRegistro()).append(System.getProperty("line.separator"));
		        OficioRemision oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
		        		registroSir.getNumeroRegistro(), 
		            	entidad.getCodigoDir3());
		        if (oficioRemision != null) {
		        	peticion.append("ID Oficio remisión: ").append(oficioRemision.getId()).append(System.getProperty("line.separator"));
		        	synchronized (registroSir) {
			        	integracionGeiserHelper.actualizarEstadoRegistroSir(
			        			registroSir, 
			        			oficioRemision);
			        	
	 		        	em.merge(registroSir);
			        	em.merge(oficioRemision);
					}
//					actualizarEnvioSir(registroSir, entidad, oficioRemision, true);
		        } else {
		        	throw new RuntimeException("No s'ha trobat cap ofici remisió relacionat amb el registre: " + registroSir.getNumeroRegistro());
		        }
			} catch (Exception e) {
				registroSirEjb.actualizarReintentosRegistroSir(registroSirId);
				e.printStackTrace();
				integracionEjb.addIntegracionError(
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion, 
						peticion.toString(), 
						e, 
						null, 
						System.currentTimeMillis() - tiempo, 
						entidad.getId(), 
						null);
			} catch (I18NException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void actualizarEnvioSirRecibidoGeiser(RegistroSir registroSir, UsuarioEntidad usuario) throws Exception, I18NException {
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
    	long tiempo = System.currentTimeMillis();
    	Long entidadId = usuario.getEntidad().getId();
    	OficioRemision oficioRemision = null;
		if (registroSir != null) {
			String descripcion = "[MANUAL] Actualizando estado registro SIR (idRegistroSir=" + registroSir.getId() + ")";
			try {
				peticion.append("Número registro: ").append(registroSir.getNumeroRegistro()).append(System.getProperty("line.separator"));
				
				oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
						registroSir.getNumeroRegistro(), 
						usuario.getEntidad().getCodigoDir3());
				
				integracionGeiserHelper.actualizarEstadoRegistroSir(
						registroSir, 
						oficioRemision);
//				actualizarEnvioSir(registroSir, usuario.getEntidad(), oficioRemision, true);
				
				if (registroSir.getInteresados() != null) {
					for (InteresadoSir interesadoSir : registroSir.getInteresados()) {
						interesadoSir.setRegistroSir(registroSir);
						interesadoSirEjb.merge(interesadoSir);
					}
				}
				if (registroSir.getAnexos() != null) {
					for (AnexoSir anexoSir : registroSir.getAnexos()) {
						anexoSir.setRegistroSir(registroSir);
						anexoSirEjb.merge(anexoSir);
					}
				}
				
				integracionEjb.addIntegracionOk(
						inicio, 
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion,
						peticion.toString(), 
						System.currentTimeMillis() - tiempo, 
						registroSir.getEntidad().getId(),
						registroSir.getNumeroRegistro());

            } catch (Exception e) {
				if (oficioRemision != null)
					peticion.append("ID Oficio remisión: ").append(oficioRemision.getId()).append(System.getProperty("line.separator"));
				e.printStackTrace();
				integracionEjb.addIntegracionError(
						RegwebConstantes.INTEGRACION_SIR, 
						descripcion, 
						peticion.toString(),
						e, 
						null, 
						System.currentTimeMillis() - tiempo, 
						entidadId, 
						null);
			}
		}
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@TransactionTimeout(value = 7200)  // 120 minutos
	public void actualizarIdEnviosSirRecibidos(Entidad entidad) throws Exception, I18NException {
		StringBuilder peticion = new StringBuilder();
		long tiempo = System.currentTimeMillis();
		try {
//          // RegistrosSir con estado no final
			List<Long> registrosSirIds = registroSirEjb.getRegistrosSirRecibidosSinId(entidad.getId());
			
//			synchronized(Semaforo.getCreacionSemaforo()) {
			for (Long registroSirId : registrosSirIds) {
				String descripcion = "Actualizando identificador intercambio envío SIR (idEnvioSir=" + registroSirId + ")";
				try {
					RegistroSir registroSir = registroSirEjb.findById(registroSirId);
					peticion.append("Número registro: ")
							.append(registroSir.getNumeroRegistro())
							.append(System.getProperty("line.separator"));
					if (registroSir.getNumeroRegistroOrigen() != null) {
						synchronized (registroSir) {
							integracionGeiserHelper.actualizarIdentificadorIntercambio(registroSir, null);
						}
					}
//						actualizarEnvioSir(registroSir, entidad, null, false);
				} catch (Exception e) {
					e.printStackTrace();
					integracionEjb.addIntegracionError(
							RegwebConstantes.INTEGRACION_SIR, 
							descripcion,
							peticion.toString(), 
							e, 
							null, 
							System.currentTimeMillis() - tiempo, 
							entidad.getId(), null);
				} catch (I18NException e) {
					e.printStackTrace();
				}
			}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    
	@Override
	public void forzarGuardado() {
		em.flush();
	}
    
}
