package es.caib.regweb3.persistence.ejb.test;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.IntegracionBean;
import es.caib.regweb3.persistence.ejb.IntegracionLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionEntradaUtilsLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSirLocal;
import es.caib.regweb3.persistence.ejb.SirEnvioBean;
import es.caib.regweb3.persistence.utils.GeiserPluginHelper;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Dir3CaibUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropiedadGlobalUtil.class, Dir3CaibUtils.class})
public class EnvioSirTest {

	private static final Logger logger = LoggerFactory.getLogger(EnvioSirTest.class);
	
	@InjectMocks
	private SirEnvioBean sirEnvioEjb;
	@InjectMocks
	private IntegracionBean integracionEjb;

	@Mock
	private RegistroSirLocal registroSirLocal;
	@Mock
	private Dir3CaibObtenerOficinasWs oficinaWs;
	@Mock
	private RegistroEntradaLocal registroEntradaLocal;
	@Mock
	private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsLocal;
	@Mock
	private IntegracionLocal integracionLocal;
	@Mock
	private GeiserPluginHelper pluginHelper;
	
	
	String codigoOficinaSir = "O00004518";
	
	@Before
	public void setUp() throws Exception, I18NException, I18NValidationException {
		PowerMockito.mockStatic(Dir3CaibUtils.class);
		PowerMockito.mockStatic(PropiedadGlobalUtil.class);
		Dir3CaibObtenerOficinasWs oficinasWs = initDir3CaibWs();
		
//		Mock ws dir3caib
		Mockito.when(Dir3CaibUtils.getObtenerOficinasService(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(oficinasWs);
	}

	@Test
	public void testCreacioRegistroSir() throws Exception, I18NException, I18NValidationException {
		ExecutorService executor = Executors.newFixedThreadPool(7);
		Collection<Callable<String>> callables = new ArrayList<>();
		logger.info("[MOCK] Construyendo callables para realizar un test múltiple del proceso de registro");
		
		for (int i = 1; i < 6; i++) {
			Callable<String> callable = new Callable<String>() {
				@Override
				public String call() throws Exception {
					return testRealizarProcesoRegistroSir();
				}
			};
			logger.info("[MOCK] Callable " + i + " preprado");
			callables.add(callable);
		}
		
		Long t1 = System.currentTimeMillis();
		
		logger.info("[MOCK] Ejecutando callables...");
		List<Future<String>> result = executor.invokeAll(callables);
		logger.info("[MOCK] La ejecución ha tardado: " + (System.currentTimeMillis() - t1));;
		for (Future f : result) {
			logger.info((String) f.get());
		}
	}
	
	private String testRealizarProcesoRegistroSir() {
		RegistroEntrada registroEntrada = Mockito.mock(RegistroEntrada.class);
		RegistroDetalle registroDetalle = Mockito.mock(RegistroDetalle.class);
		Oficina oficinaActiva = Mockito.mock(Oficina.class);
		UsuarioEntidad usuarioEntidad = Mockito.mock(UsuarioEntidad.class);
		Usuario usuario = Mockito.mock(Usuario.class);
		OficioRemision oficioRemision = Mockito.mock(OficioRemision.class);
		Entidad entidad = Mockito.mock(Entidad.class);
		RegistroSir registroSir = Mockito.mock(RegistroSir.class);
		
		OficinaTF oficinaTF = initOficinaTF();

		try {
			Mockito.when(registroSirLocal.transformarRegistroEntrada(Mockito.any(RegistroEntrada.class))).thenReturn(registroSir);
			Mockito.when(oficinaWs.obtenerOficina(Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class))).thenReturn(oficinaTF);
			Mockito.when(registroEntradaLocal.merge(Mockito.any(RegistroEntrada.class))).thenReturn(registroEntrada);
			Mockito.when(oficioRemisionEntradaUtilsLocal.crearOficioRemisionSIR(Mockito.any(RegistroEntrada.class), Mockito.any(Oficina.class), Mockito.any(UsuarioEntidad.class), Mockito.any(OficinaTF.class))).thenReturn(oficioRemision);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (I18NException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (I18NValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Mockito.when(registroEntrada.getRegistroDetalle()).thenReturn(registroDetalle);
		Mockito.when(oficioRemision.getOficina()).thenReturn(oficinaActiva);
		Mockito.when(usuarioEntidad.getEntidad()).thenReturn(entidad);
		Mockito.when(usuarioEntidad.getUsuario()).thenReturn(usuario);
		Mockito.when(registroEntrada.getOficina()).thenReturn(oficinaActiva);
		Mockito.when(registroSir.getEntidad()).thenReturn(entidad);
		Mockito.when(usuario.getDocumento()).thenReturn("00000000T");
		
//		Plugin
		String nuRegistro = getNuRegistro();
		Date fechaRegistro = new Date();
		RespuestaRegistroGeiser respuestaRegistro = initRespuestaRegistroGeiser(nuRegistro, fechaRegistro);
		RespuestaConsultaGeiser respuestaConsulta = initRespuestaConsultaGeiser(nuRegistro, fechaRegistro);
		Mockito.when(registroSir.getNumeroRegistro()).thenReturn(nuRegistro);
		try {
			Mockito.when(pluginHelper.postProcesoNuevoRegistroSirGeiser(Mockito.any(RegistroSir.class), Mockito.anyLong())).thenReturn(respuestaRegistro);
			Mockito.when(pluginHelper.postProcesoConsultarRegistroSirGeiser(Mockito.any(RegistroSir.class), Mockito.anyLong())).thenReturn(respuestaConsulta);
		} catch (I18NException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sirEnvioEjb.enviarIntercambio(
					1L, 	
					registroEntrada, 
					oficinaActiva, 
					usuarioEntidad,
					codigoOficinaSir);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (I18NException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (I18NValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[MOCK] Registro SIR realizado: " + registroSir.getNumeroRegistro();	
	}
	
	private Dir3CaibObtenerOficinasWs initDir3CaibWs() throws MalformedURLException {
		Dir3CaibObtenerOficinasWs api = new Dir3CaibObtenerOficinasWs() {
			
			@Override
			public List<OficinaTF> obtenerOficinasSIRUnidad(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public OficinaTF obtenerOficina(String arg0, Timestamp arg1, Timestamp arg2) {
				OficinaTF oficinaTF = new OficinaTF();
				oficinaTF.setCodigo("O00004518");
				oficinaTF.setDenominacion("Oficina Autoridad Portuaria de Baleares");
				return oficinaTF;
			}
			
			@Override
			public Timestamp obtenerFechaUltimaActualizacion() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<OficinaTF> obtenerArbolOficinas(String arg0, Timestamp arg1, Timestamp arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getVersionWs() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getVersion() {
				// TODO Auto-generated method stub
				return null;
			}
		};

	    return api;
	}
	
	private RespuestaRegistroGeiser initRespuestaRegistroGeiser(String nuRegistro, Date fechaRegistro) {
		RespuestaRegistroGeiser respuesta = new RespuestaRegistroGeiser();
		respuesta.setNuRegistro(nuRegistro);
		respuesta.setFechaRegistro(fechaRegistro);
		return respuesta;
	}
	
	private RespuestaConsultaGeiser initRespuestaConsultaGeiser(String nuRegistro, Date fechaRegistro) {
		ApunteRegistro apunteRegistro = new ApunteRegistro();
		RespuestaConsultaGeiser respuesta = new RespuestaConsultaGeiser();
		apunteRegistro.setNuRegistro(nuRegistro);
		apunteRegistro.setFechaRegistro(fechaRegistro);
		apunteRegistro.setTipoAsiento(TipoAsiento.ENTRADA);
		respuesta.setApuntes(new ArrayList<ApunteRegistro>());
		respuesta.getApuntes().add(apunteRegistro);
		return respuesta;
	}
	
	private String getNuRegistro() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
	
	private RegistroSir initRegistroSir() {
		RegistroSir registroSir = new RegistroSir();
		return registroSir;
	}

	private OficinaTF initOficinaTF() {
		OficinaTF oficinasWs = new OficinaTF();
		return oficinasWs;
	}
}
