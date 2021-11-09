package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.utils.ConversionHelper;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.GeiserPluginHelper;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.EstadoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.geiser.api.AnexoG;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.EstadoTramitacion;
import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

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
    
    @Autowired ArxiuCaibUtils arxiuCaibUtils;
    @Autowired ConversionHelper conversioHelper;
    @Autowired GeiserPluginHelper pluginHelper;
    
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
            registroDetalle.setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            registroDetalle.setFechaOrigen(registroEntrada.getFecha());

            // Actualizamos el registro
            registroEntrada = registroEntradaEjb.merge(registroEntrada);

            // Crear y registrar el Oficio de remisión
            oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionSIR(registroEntrada, oficinaActiva, usuario, oficinaSirDestino);

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(oficioRemision.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(oficioRemision.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(oficioRemision.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), oficioRemision.getIdentificadorIntercambio());

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (oficioRemision != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), s, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), oficioRemision.getIdentificadorIntercambio());
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
            registroDetalle.setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            registroDetalle.setFechaOrigen(registroSalida.getFecha());

            // Actualizamos el registro
            registroSalida = registroSalidaEjb.merge(registroSalida);

            // Crear y registrar el Oficio de remisión
            oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionSIR(registroSalida, oficinaActiva, usuario, oficinaSirDestino);

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(oficioRemision.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(oficioRemision.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(oficioRemision.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), oficioRemision.getIdentificadorIntercambio());

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (oficioRemision != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), s, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), oficioRemision.getIdentificadorIntercambio());
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

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Envío intercambio a " + codigoOficinaSir;
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));
        peticion.append("Número registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

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
    	        	//Registro interno en GEISER
    	        	registroSir.setDocumentoUsuario(usuario.getUsuario().getDocumento());
    	        	realizarEnvioSir(
    	        			tipoRegistro, 
    	        			registroEntrada, 
    	        			registroSalida, 
    	        			registroSir, 
    	        			usuario);
    	        	if (registroEntrada != null) {
        	        	// 1- Recuperar registro salida
        	        	// 2- Assignar nuRegistro GEISER a registro REGWEB
        	        	// 3- Actualizar trazabilidad
        	        	
        	        	RegistroSir registroSirConsulta = new RegistroSir();
        	        	registroSirConsulta.setNumeroRegistro(registroEntrada.getNumeroRegistro());
        	        	registroSirConsulta.setCodigoEntidadRegistralOrigen(registroEntrada.getOficina().getCodigo());
        	        	RespuestaConsultaGeiser consultaRegistroEntrada = pluginHelper.postProcesoConsultarRegistroSirGeiser(registroSirConsulta, registroSir.getEntidad().getId());
        	        	if (consultaRegistroEntrada.getApuntes() != null && !consultaRegistroEntrada.getApuntes().isEmpty()) {
        	        		for (ApunteRegistro apunte: consultaRegistroEntrada.getApuntes()) {
								if (apunte.getTipoAsiento().equals(TipoAsiento.SALIDA) && apunte.getNuRegistroOrigen().equals(registroEntrada.getNumeroRegistro())) {
									
									List<Trazabilidad> trazabilidadesRegistroSir = trazabilidadEjb.getByRegistroEntradaOrigen(registroEntrada.getId());
									if (trazabilidadesRegistroSir != null && !trazabilidadesRegistroSir.isEmpty()) {
										Trazabilidad trazabilidadRegistro = trazabilidadesRegistroSir.get(0);
										if (trazabilidadRegistro.getTipo().equals(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR) || trazabilidadRegistro.getTipo().equals(RegwebConstantes.TRAZABILIDAD_OFICIO)) {
											RegistroSalida registroSalidaSir = trazabilidadRegistro.getRegistroSalida();
											registroSalidaSir.setNumeroRegistro(apunte.getNuRegistro());
											registroSalidaSir.setNumeroRegistroFormateado(apunte.getNuRegistro());
											registroSalidaSir.setFecha(apunte.getFechaRegistro());
										}
									}
//									trazabilidadEjb.getByOficioRegistroEntrada(ofi, idRegistroEntrada)
								}
							}
        	        	}
    	        		registroEntradaEjb.merge(registroEntrada);
    	        	}
    	        	if (registroSalida != null)
    	        		registroSalidaEjb.merge(registroSalida);
    	        	
    	        	actualizarEnvioSirRealizado(registroSir, usuario);
     	        } catch (GeiserPluginException gpe) {
    	        	log.error("Ha habido un error realizando el registro en GEISER");
    				gpe.printStackTrace();
    				throw gpe;
    	        }
                // Integración
                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            }catch (Exception e){
                e.printStackTrace();
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
				ejbContext.setRollbackOnly();
				throw e;
            }

            log.info("");
            log.info("Fin enviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        } catch (I18NValidationException | I18NException | Exception s) {
            s.printStackTrace();
            if (registroSir != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), s, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
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
    		List<RegistroEntrada> registrosOficioRemision = oficioRemision.getRegistrosEntrada();
    		for (RegistroEntrada registroEntrada : registrosOficioRemision) {
    			Long registroSirId = registroSirEjb.getRegistroSirByNumeroRegistro(
		    		registroEntrada.getNumeroRegistro(), 
		    		oficioRemision.getCodigoEntidadRegistralDestino());
    			RegistroSir registroSir = registroSirEjb.findById(registroSirId);
			    if (registroSir != null) {
			    	descripcion += "\n  [MANUAL] Actualizando estado envío SIR (idEnvioSir=" + registroSir.getId() + ")";
		    		actualizarEnvioSir(registroSir, entidad, oficioRemision, true);
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

//
//    @Override
//    public void reintentarIntercambiosConError(Entidad entidad) throws Exception {
//
//        StringBuilder peticion = new StringBuilder();
//        long tiempo = System.currentTimeMillis();
//        String descripcion = "Reintentar intercambios con Error";
//        Date inicio = new Date();
//
//        try {
//
//            peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));
//
//            // RegistrosSir enviados con errores
//            List<Long> registrosSir = registroSirEjb.getEnviadosConError(entidad.getId());
//
//            peticion.append("total registrosSir: ").append(registrosSir.size()).append(System.getProperty("line.separator"));
//
//            if (!registrosSir.isEmpty()) {
//
//                log.info("Hay " + registrosSir.size() + " RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");
//
//                // Volvemos a enviar los RegistrosSir
//                for (Long registroSir : registrosSir) {
//
//                    reintentarEnvioRegistroSir(registroSir, entidad);
//                }
//            } else {
//                log.info("No hay RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");
//            }
//
//            // OficiosRemision enviados con errores
//            List<OficioRemision> oficios = oficioRemisionEjb.getEnviadosConError(entidad.getId());
//
//            peticion.append("total oficios: ").append(oficios.size()).append(System.getProperty("line.separator"));
//
//            if (!oficios.isEmpty()) {
//
//                log.info("Hay " + oficios.size() + " Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");
//
//                // Volvemos a enviar los OficiosRemision
//                for (OficioRemision oficio : oficios) {
//
//                    reintentarEnvioOficioRemision(oficio, RegwebConstantes.INTEGRACION_SCHEDULERS);
//                }
//            } else {
//                log.info("No hay Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");
//            }
//
//            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
//
//
//        } catch (Exception | I18NException e) {
//            log.info("Error al reintenar el envio de registros con error");
//            e.printStackTrace();
//            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
//        }
//    }

//    /**
//     * @param idRegistroSir
//     * @throws Exception
//     */
//    private void reintentarEnvioRegistroSir(Long idRegistroSir, Entidad entidad) throws Exception {
//
//        StringBuilder peticion = new StringBuilder();
//        long tiempo = System.currentTimeMillis();
//        String descripcion = "Reintentar RegistroSir a: ";
//        Date inicio = new Date();
//
//        peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));
//        peticion.append("idRegistroSir: ").append(idRegistroSir).append(System.getProperty("line.separator"));
//
//        try{
//
//            RegistroSir registroSir = registroSirEjb.getRegistroSirConAnexos(idRegistroSir);
//
//            log.info("Reintentado reenvío/rechazo " + registroSir.getIdentificadorIntercambio() + " a " + registroSir.getDecodificacionEntidadRegistralDestino());
//
//            if(registroSir.getEstado().equals(EstadoRegistroSir.REENVIADO)){
//                descripcion = "Reintentar reenvío a: " + registroSir.getCodigoEntidadRegistralDestino();
//            }else if(registroSir.getEstado().equals(EstadoRegistroSir.RECHAZADO)){
//                descripcion = "Reintentar rechazo a: " + registroSir.getCodigoEntidadRegistralDestino();
//            }
//
//            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
//            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
//            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
//
//            emisionEjb.enviarFicheroIntercambio(registroSir);
//            registroSir.setNumeroReintentos(registroSir.getNumeroReintentos() + 1);
//            registroSir.setFechaEstado(new Date());
//
//            // Modificamos su estado si estaba marcado con ERROR
//            if (registroSir.getEstado().equals(EstadoRegistroSir.REENVIADO_Y_ERROR)) {
//                registroSir.setEstado(EstadoRegistroSir.REENVIADO);
//            } else if (registroSir.getEstado().equals(EstadoRegistroSir.RECHAZADO_Y_ERROR)) {
//                registroSir.setEstado(EstadoRegistroSir.RECHAZADO);
//            }
//
//            registroSirEjb.merge(registroSir);
//
//            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), registroSir.getIdentificadorIntercambio());
//
//        }catch (Exception e){
//            log.info("Error al reintenar el envio del RegistroSir id: " + idRegistroSir);
//            e.printStackTrace();
//            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
//        }
//
//    }
//
//    /**
//     * @param oficio
//     * @throws Exception
//     * @throws I18NException
//     */
//    private void reintentarEnvioOficioRemision(OficioRemision oficio, Long tipoIntegracion) throws Exception, I18NException {
//
//        Date inicio = new Date();
//        StringBuilder peticion = new StringBuilder();
//        long tiempo = System.currentTimeMillis();
//        String descripcion = "Reintentar envío intercambio a: " + oficio.getCodigoEntidadRegistralDestino();
//        peticion.append("IdentificadorIntercambio: ").append(oficio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
//        peticion.append("Origen: ").append(oficio.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
//        peticion.append("Destino: ").append(oficio.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
//
//        if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {
//
//            try {
//                log.info("Reintentando intercambio OficioRemisionSir entrada " + oficio.getIdentificadorIntercambio() + " a " + oficio.getDecodificacionEntidadRegistralDestino() + " (" + oficio.getCodigoEntidadRegistralDestino() + ")");
//
//                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficio.getRegistrosEntrada().get(0).getId());
//
//                // Si tiene el Justificante generado y custodiado lo reenviamos
//                if (registroEntrada.getRegistroDetalle().getTieneJustificanteCustodiado()) {
//
//                    // Transformamos el RegistroEntrada en un RegistroSir
//                    RegistroSir registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);
//
//                    // Enviamos el Registro al Componente CIR
//                    emisionEjb.enviarFicheroIntercambio(registroSir);
//
//                    // Contabilizamos los reintentos
//                    oficio.setNumeroReintentos(oficio.getNumeroReintentos() + 1);
//                    oficio.setFechaEstado(new Date());
//
//                    // Modificamos su estado si estaba marcado con ERROR
//                    if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR) {
//                        oficio.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
//                    } else if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR) {
//                        oficio.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO);
//                    }
//
//                    // Actualizamos el Oficio
//                    oficioRemisionEjb.merge(oficio);
//
//                    //Integración
//                    integracionEjb.addIntegracionOk(inicio, tipoIntegracion, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//
//                }else{
//                    integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), null, "No tiene el justificante custodiado", System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//                }
//
//            }catch (I18NException | Exception e){
//                e.printStackTrace();
//                integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//
//                // Solo lanzamos la Excepción si no se trata del SCHEDULER
//                if(tipoIntegracion.equals(RegwebConstantes.INTEGRACION_SIR)){
//                    throw e;
//                }
//            }
//
//
//        } else if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)) {
//
//            try {
//                log.info("Reintentando intercambio OficioRemisionSir salida " + oficio.getIdentificadorIntercambio() + " a " + oficio.getDecodificacionEntidadRegistralDestino() + " (" + oficio.getCodigoEntidadRegistralDestino() + ")");
//
//
//                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficio.getRegistrosSalida().get(0).getId());
//
//                // Si tiene el Justificante generado y custodiado lo reenviamos
//                if (registroSalida.getRegistroDetalle().getTieneJustificanteCustodiado()) {
//
//                    // Transformamos el RegistroSalida en un RegistroSir
//                    RegistroSir registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);
//
//                    // Enviamos el Registro al Componente CIR
//                    emisionEjb.enviarFicheroIntercambio(registroSir);
//
//                    // Contabilizamos los reintentos
//                    oficio.setNumeroReintentos(oficio.getNumeroReintentos() + 1);
//                    oficio.setFechaEstado(new Date());
//
//                    // Modificamos su estado si estaba marcado con ERROR
//                    if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR) {
//                        oficio.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
//                    } else if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR) {
//                        oficio.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO);
//                    }
//
//                    // Actualizamos el Oficio
//                    oficioRemisionEjb.merge(oficio);
//
//                    //Integración
//                    integracionEjb.addIntegracionOk(inicio, tipoIntegracion, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//
//                }else{
//
//                    integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), null, "No tiene el justificante custodiado", System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//                }
//
//            }catch (I18NException | Exception e){
//                e.printStackTrace();
//                integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
//
//                // Solo lanzamos la Excepción si no se trata del SCHEDULER
//                if(tipoIntegracion.equals(RegwebConstantes.INTEGRACION_SIR)){
//                    throw e;
//                }
//            }
//        }
//
//    }

//    /**
//     * Indica si el RegistroSir  se puede reenviar, en función de su estado
//     *
//     * @param estado del registroSir
//     * @return
//     */
//    public boolean puedeReenviarRegistroSir(EstadoRegistroSir estado) {
//        return estado.equals(EstadoRegistroSir.RECIBIDO) ||
//                estado.equals(EstadoRegistroSir.REENVIADO) ||
//                estado.equals(EstadoRegistroSir.REENVIADO_Y_ERROR);
//
//    }

    @Override
    @TransactionTimeout(value = 3000)  // 50 minutos
    public Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws Exception{

        // ruta actual: /app/caib/regweb/archivos
        // ruta erte: /app/caib/regweb/dades/erte

        final String rutaERTE = PropiedadGlobalUtil.getErtePath(idEntidad);

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");

        try{

            log.info("Total registros aceptados erte: " + registros.size());
            log.info("");

            for(Long erte:registros){

                try{

                    // Cargamos el registro
                    TrazabilidadSir trazabilidadSir = trazabilidadSirEjb.getByRegistroSirAceptado(erte);

                    RegistroSir registroSir = trazabilidadSir.getRegistroSir();
                    RegistroEntrada registroEntrada = trazabilidadSir.getRegistroEntrada();

                    log.info("Procesando el registro aceptado: " + registroSir.getId());

                    // Copiamos cada anexo en la carpeta creada
                    for(AnexoSir anexoSir:registroSir.getAnexos()){

                        Archivo archivo = anexoSir.getAnexo();

                        File origen = FileSystemManager.getArchivo(archivo.getId());

                        String rutaDestino = rutaERTE + formatDate.format(registroEntrada.getFecha()) + " - " + registroEntrada.getNumeroRegistroFormateado().replace("/","-");

                        Files.createDirectories(Paths.get(rutaDestino));

                        try{
                            log.info("Copiamos la documentación del registro aceptado a: " + rutaDestino);
                            Files.copy(origen.toPath(), (new File(rutaDestino +"/"+ archivo.getNombre())).toPath(), StandardCopyOption.REPLACE_EXISTING);

                        }catch (Exception e){
                            log.info("No encuentra el fichero");
                        }
                    }

                }catch (Exception e){
                    log.info("Error procesando un registro sir");
                }
            }


            return registros.size();

        } catch(Exception e){
            log.info("Error generando carpetas ERTE");
            e.printStackTrace();
        }

        return 0;

    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void actualizarEnviosSir(Entidad entidad) throws Exception {
    	StringBuilder peticion = new StringBuilder();
    	long tiempo = System.currentTimeMillis();
		try {
//          // RegistrosSir con estado no final
			List<Long> registrosSirIds = registroSirEjb.getRegistrosSirPendientes(entidad.getId());
//			synchronized(Semaforo.getCreacionSemaforo()) {
				for (Long registroSirId : registrosSirIds) {
			    	String descripcion = "Actualizando estado envío SIR (idEnvioSir=" + registroSirId + ")";
					try {
				            RegistroSir registroSir = registroSirEjb.findById(registroSirId);
				            peticion.append("Número registro: ").append(registroSir.getNumeroRegistro()).append(System.getProperty("line.separator"));
				            OficioRemision oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
				            		registroSir.getNumeroRegistro(), 
				            		entidad.getCodigoDir3());
				            if (oficioRemision != null) {
								peticion.append("ID Oficio remisión: ").append(oficioRemision.getId()).append(System.getProperty("line.separator"));
								actualizarEnvioSir(registroSir, entidad, oficioRemision, true);
				            } else {
				            	throw new RuntimeException("No s'ha trobat cap ofici remisió relacionat amb el registre: " + registroSir.getNumeroRegistro());
				            }
					} catch (Exception e) {
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
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }

    private void realizarEnvioSir(
    		Long tipoRegistro,
    		RegistroEntrada registroEntrada, 
    		RegistroSalida registroSalida, 
    		RegistroSir registroSir, 
    		UsuarioEntidad usuario) throws I18NException, I18NValidationException, Exception {
    	Long entidadId = usuario.getEntidad().getId();
        RespuestaRegistroGeiser respuesta = pluginHelper.postProcesoNuevoRegistroSirGeiser(registroSir, entidadId);
        if (respuesta != null) {
        	registroSir.setNumeroRegistro(respuesta.getNuRegistro());
        	registroSir.setFechaRegistro(respuesta.getFechaRegistro());
        	if (tipoRegistro.equals(1L)) { //ENTRADA
        		// Actualizamos metadatos registro entrada
        		actualizarMetadatosRegistro(registroEntrada, respuesta, usuario);
        		// Actualizamos metadatos anexos en custodia
        		actualizarMetadatosAnexosArxiu(registroEntrada, usuario);
        	} else {
        		// Actualizamos metadatos registro salida
        		actualizarMetadatosRegistro(registroSalida, respuesta, usuario);
        		// Actualizamos metadatos anexos en custodia
        		actualizarMetadatosAnexosArxiu(registroSalida, usuario);
        	}
        } else {
        	// No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.11"));
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void actualizarEnvioSirRealizado(RegistroSir registroSir, UsuarioEntidad usuario) throws Exception, I18NException {
    	StringBuilder peticion = new StringBuilder();
    	long tiempo = System.currentTimeMillis();
    	Long entidadId = usuario.getEntidad().getId();
    	OficioRemision oficioRemision = null;
		if (registroSir != null) {
			String descripcion = "[MANUAL] Actualizando estado registro SIR (idRegistroSir=" + registroSir.getId() + ")";
			try {
				oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
						registroSir.getNumeroRegistro(), 
						usuario.getEntidad().getCodigoDir3());
				
				actualizarEnvioSir(registroSir, usuario.getEntidad(), oficioRemision, true);
				
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
			} catch (Exception e) {
				peticion.append("Número registro: ").append(registroSir.getNumeroRegistro()).append(System.getProperty("line.separator"));
				if (oficioRemision != null)
					peticion.append("ID Oficio remisión: ").append(oficioRemision.getId()).append(System.getProperty("line.separator"));
				e.printStackTrace();
				integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(),
						e, null, System.currentTimeMillis() - tiempo, entidadId, null);
			}
		}
    }

	@Override
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
					if (registroSir.getNumeroRegistroOrigen() != null)
						actualizarEnvioSir(registroSir, entidad, null, false);
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
    
    private void actualizarEnvioSir(
    		RegistroSir registroSir, 
    		Entidad entidad, 
    		OficioRemision oficioRemision,
    		boolean actualizarEstado) throws I18NException, Exception {

    	  if (registroSir.getIdentificadorIntercambio() == null || (oficioRemision != null && oficioRemision.getIdentificadorIntercambio() == null)) {
    		  // Recupera identificador intercambio sir de GEISER
    		  RespuestaBusquedaTramitGeiser response = pluginHelper.postProcesoBuscarEstadoTRegistroSirGeiser(null, registroSir, entidad.getId());
    		  List<EstadoTramitacion> estadoTramitacion = response.getEstadosTramitacionRegistro();
    		  // Identificador intercambio a veces no disponible al momento (consultar con una scheduled)  
    		  if (estadoTramitacion != null && !estadoTramitacion.isEmpty() && !estadoTramitacion.get(0).getIdentificadorIntercambioSIR().isEmpty()) {
    			  String identificadorIntercambio = estadoTramitacion.get(0).getIdentificadorIntercambioSIR().get(0);
    			  registroSirEjb.actualizarIdentificadorIntercambio(registroSir.getId(), identificadorIntercambio);
    			  if (oficioRemision != null)
    				  oficioRemisionEjb.actualizarIdentificadorIntercambio(oficioRemision.getId(), identificadorIntercambio);
//    			  int secuencia = 0;
//    			  for (AnexoSir anexoSir: registroSir.getAnexos()) {
//    				  String identificadorFichero = registroSirEjb.generateIdentificadorFichero(identificadorIntercambio, secuencia, anexoSir.getNombreFichero());
//    				  secuencia++;
//    				  
//    				  anexoSir.setIdentificadorFichero(identificadorFichero);
//    				  
//    				  if (anexoSir.getDocumento() != null) { //firma dettached
//    					  anexoSir.setIdentificadorDocumentoFirmado(identificadorFichero);
//    				  }
//    			  }
    		  }
    	  }
    	  if (actualizarEstado)
    		  actualizarEstadoAndIdentificadoresEnvioSir(registroSir, entidad, oficioRemision);
    }
    

    private void actualizarMetadatosRegistro(IRegistro registro, RespuestaRegistroGeiser respuesta, UsuarioEntidad usuario) throws Exception, I18NException {
    	if (registro instanceof RegistroEntrada) {
    		RegistroEntrada registroEntrada = (RegistroEntrada) registro;
    		registroEntrada.setNumeroRegistro(respuesta.getNuRegistro());
    		registroEntrada.setNumeroRegistroFormateado(respuesta.getNuRegistro());
    		registroEntrada.setFecha(respuesta.getFechaRegistro());
    		
            // Si no ha introducido ninguna fecha de Origen
            if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null)
            	registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
                
            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen()))
            	registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            
    	} else {
    		RegistroSalida registroSalida = (RegistroSalida) registro;
			registroSalida.setNumeroRegistro(respuesta.getNuRegistro());
			registroSalida.setNumeroRegistroFormateado(respuesta.getNuRegistro());
			registroSalida.setFecha(respuesta.getFechaRegistro());

            // Si no ha introducido ninguna fecha de Origen
            if (registroSalida.getRegistroDetalle().getFechaOrigen() == null)
                registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
                
            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) 
                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
    	}
    }
    
    private void actualizarMetadatosAnexosArxiu(IRegistro registro, UsuarioEntidad usuario) throws I18NException {	
    	// Actualizar información anexos SGD
    	try {
    		for (AnexoFull anexoFull: registro.getRegistroDetalle().getAnexosFull()) {
        		anexoEjb.actualizarMetadatosAnexo(registro, anexoFull, usuario);
			}
    	} catch (I18NException i18n) {
    		log.error("Ha habido un error actualizando los metadatos de registro del anexo.");
    		i18n.printStackTrace();
		} catch (Exception e) {
			log.error("Ha habido un error actualizando los metadatos de registro del anexo.");
			e.printStackTrace();
		}
    }
    
    private void actualizarEstadoAndIdentificadoresEnvioSir(
    		RegistroSir registroSir, 
    		Entidad entidad,
    		OficioRemision oficioRemision) throws I18NException, Exception {
    	if (oficioRemision == null)
    		oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
            		registroSir.getNumeroRegistro(), 
            		entidad.getCodigoDir3());
    	boolean existeAnexoSinIdentificador = false;
    	List<AnexoSir> anexosSir = registroSir.getAnexos();
    	for (AnexoSir anexoSir : anexosSir) {
			if (anexoSir.getIdentificadorFichero() == null) {
				existeAnexoSinIdentificador = true;
				break;
			}
		}
    	// Solo revisar estado si estado actual si no es FINAL
    	if (oficioRemision != null && !RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_FINALES, oficioRemision.getEstado()) || existeAnexoSinIdentificador) {
	        RespuestaConsultaGeiser responseConsulta = pluginHelper.postProcesoConsultarRegistroSirGeiser(registroSir, entidad.getId());
	        List<ApunteRegistro> apuntes = responseConsulta.getApuntes();
	        if (apuntes != null && !apuntes.isEmpty()) {
	        	int estadoOficioSirActual = 0;
	        	for (ApunteRegistro apunteRegistro : apuntes) {
//	        		apunteRegistro.getTipoAsiento().equals(TipoAsiento.ENTRADA) && 
	              if (apunteRegistro.getNuRegistro().equals(registroSir.getNumeroRegistro())) {
					registroSir.setEstado(EstadoRegistroSir.valueOf(apunteRegistro.getEstado().name()));
					
					if (registroSir.getId() == null)
						registroSirEjb.persist(registroSir);
					estadoOficioSirActual = EstadoUtils.getEstadoOficioRemision(apunteRegistro.getEstado().name());
					if (oficioRemision != null && oficioRemision.getEstado() != estadoOficioSirActual && estadoOficioSirActual != 0) {
						oficioRemisionEjb.modificarEstado(oficioRemision.getId(), estadoOficioSirActual);
					}
	              }
	              if (estadoOficioSirActual == RegwebConstantes.OFICIO_SIR_ENVIADO_CONFIRMADO || estadoOficioSirActual == RegwebConstantes.OFICIO_SIR_ENVIADO_RECHAZADO) {
	            	  RespuestaBusquedaTramitGeiser response = pluginHelper.postProcesoBuscarEstadoTRegistroSirGeiser(null, registroSir, entidad.getId());
	        		  List<EstadoTramitacion> estadoTramitacion = response.getEstadosTramitacionRegistro();
	        		  // Identificador intercambio a veces no disponible al momento (consultar con una scheduled)  
	        		  if (estadoTramitacion != null && !estadoTramitacion.isEmpty() && estadoTramitacion.get(0).getFechaEstado() != null) {
		        		  Date fechaEstado = estadoTramitacion.get(0).getFechaEstado();
		        		  oficioRemisionEjb.modificarFechaEstado(oficioRemision.getId(), fechaEstado);
	        		  } else {
	        			  oficioRemisionEjb.modificarFechaEstado(oficioRemision.getId(), null); // No mostrar fecha estado si no la devuelve GEISER
	        		  }

		              // Actualizar destino si este se cambia en GEISER (reenvío)
		              if (!apunteRegistro.getOrganoDestino().equals(registroSir.getCodigoUnidadTramitacionDestino())) {

		            	  // Si se hace un reenvío el destinatario cambia
		            	  registroSir.setCodigoEntidadRegistral(apunteRegistro.getCdAmbitoActual());
		            	  registroSir.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
		            	  registroSir.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
		            	  registroSir.setCodigoUnidadTramitacionDestino(apunteRegistro.getOrganoDestino());
		            	  registroSir.setDecodificacionUnidadTramitacionDestino(apunteRegistro.getOrganoDestinoDenominacion());
		            	  // Destino oficio remisión
		            	  oficioRemisionEjb.actualizarDestinoExterno(
		            			  oficioRemision.getId(), 
		            			  apunteRegistro.getCdAmbitoActual(), 
		            			  apunteRegistro.getNombreAmbitoActual(), 
		            			  apunteRegistro.getOrganoDestino(),
		            			  apunteRegistro.getOrganoDestinoDenominacion());
		            	  for (RegistroEntrada registroEntrada: oficioRemision.getRegistrosEntrada()) {
		            		  	// Destino registro entrada
		            		  	RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();
		            		  	registroDetalle.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
		            		  	registroDetalle.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
		            		  	registroEntrada.setRegistroDetalle(registroDetalle); 
		            		  	registroEntradaEjb.actualizarDestinoExterno(registroEntrada.getId(), apunteRegistro.getOrganoDestino(), apunteRegistro.getOrganoDestinoDenominacion());
		            	  }
		            	  for (RegistroSalida registroSalida: oficioRemision.getRegistrosSalida()) {
		            		  	// Destino registro salida
		            		  	RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();
		            		  	registroDetalle.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
		            		  	registroDetalle.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
		            		  	registroSalida.setRegistroDetalle(registroDetalle); 
		            		  	registroSalidaEjb.actualizarDestinoExterno(registroSalida.getId(), apunteRegistro.getOrganoDestino(), apunteRegistro.getOrganoDestinoDenominacion());
		            	  }
//		            	  registroSirEjb.merge(registroSir);
		              }
	              }
	              // Actualizar anexos sir con identificador fichero de geiser
	              for (AnexoSir anexoSir: anexosSir) {
	            	  if (anexoSir.getIdentificadorFichero() == null) {
	            		  for (AnexoG anexoGeiser: apunteRegistro.getAnexos()) {
	            			  if (anexoGeiser.getHashBase64().equals(anexoSir.getHash()))
	            				  anexoSir.setIdentificadorFichero(anexoGeiser.getIdentificador());
	            		  }
	            	  }
	              }
	              // Actualizar estado registro
	              if (RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_ENVIADOS_FINALES, estadoOficioSirActual)) {
						for (RegistroEntrada registroEntrada: oficioRemision.getRegistrosEntrada()) {
							registroEntradaEjb.cambiarEstado(registroEntrada.getId(), RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
						}
						for (RegistroSalida registroSalida: oficioRemision.getRegistrosSalida()) {
							registroSalidaEjb.cambiarEstado(registroSalida.getId(), RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
						}
	              }
	              // Ver si rectificar registro
		          if (RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_RECTIFICAR, estadoOficioSirActual)) {
					for (RegistroEntrada registroEntrada: oficioRemision.getRegistrosEntrada()) {
						registroEntradaEjb.cambiarEstado(registroEntrada.getId(), RegwebConstantes.REGISTRO_RECHAZADO);
					}
					for (RegistroSalida registroSalida: oficioRemision.getRegistrosSalida()) {
						registroSalidaEjb.cambiarEstado(registroSalida.getId(), RegwebConstantes.REGISTRO_RECHAZADO);
					}
		          }
	        	}
	        }
	        em.flush();
    	}	
    }
    
}
