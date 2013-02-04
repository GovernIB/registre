package es.caib.regweb.ws;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.Context;
import javax.security.auth.login.LoginContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.helper.ParametrosRegistroPublicadoEntrada;
import es.caib.regweb.logic.helper.ParametrosRegistroSalida;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacade;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacadeHome;
import es.caib.regweb.logic.interfaces.RegistroSalidaFacade;
import es.caib.regweb.logic.interfaces.RegistroSalidaFacadeHome;
import es.caib.regweb.logic.interfaces.ValoresFacadeHome;
import es.caib.regweb.ws.model.ErrorEntrada;
import es.caib.regweb.ws.model.ErrorSalida;
import es.caib.regweb.ws.model.ListaErroresEntrada;
import es.caib.regweb.ws.model.ListaErroresSalida;
import es.caib.regweb.ws.model.ListaResultados;
import es.caib.regweb.ws.model.ParametrosRegistroEntradaWS;
import es.caib.regweb.ws.model.ParametrosRegistroPublicadoEntradaWS;
import es.caib.regweb.ws.model.ParametrosRegistroSalidaWS;
import es.caib.regweb.ws.model.RegwebFacadeException;
import es.caib.regweb.ws.services.RegwebFacade;

public class RegwebFacadeImpl implements RegwebFacade{

	private static final Log logger = LogFactory.getLog(RegwebFacadeImpl.class);
	
	
	public ParametrosRegistroEntradaWS actualizarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		logger.debug("actualizarEntrada: " + parametrosEntrada.getOficina() + "/" + parametrosEntrada.getNumeroEntrada() + "/" + parametrosEntrada.getAnoEntrada());
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroEntrada registro = parametrosEntradaWStoParametrosEntrada(parametrosEntrada);
			
			// Conectamos a Registro
			lc = doLogin(parametrosEntrada.getUsuario(), parametrosEntrada.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regFacade = home.create();
			
			ParametrosRegistroEntrada resultado = regFacade.actualizar(registro);
			
			logger.debug("Registro actualizado");
			return parametrosEntradaToParametrosEntradaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion actualizando entrada: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}
	
	public ParametrosRegistroSalidaWS actualizarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		logger.debug("actualizarSalida: " + parametrosSalida.getOficina() + "/" + parametrosSalida.getNumeroSalida() + "/" + parametrosSalida.getAnoSalida());
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroSalida registro = parametrosSalidaWStoParametrosSalida(parametrosSalida);
			
			// Conectamos a Registro
			lc = doLogin(parametrosSalida.getUsuario(), parametrosSalida.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regFacade = home.create();
			
			ParametrosRegistroSalida resultado = regFacade.actualizar(registro);
			
			logger.debug("Registro actualizado");
			return parametrosSalidaToParametrosSalidaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion actualizando salida: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}

	


	public boolean anularEntrada(ParametrosRegistroEntradaWS parametrosEntrada,
			boolean anular) throws RemoteException, RegwebFacadeException {
		
		logger.debug("anularEntrada: " + parametrosEntrada.getOficina() + "/" + parametrosEntrada.getNumeroEntrada() + "/" + parametrosEntrada.getAnoEntrada());
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
			registro.fijaUsuario(parametrosEntrada.getUsuario());
			registro.fijaPasswordUser(parametrosEntrada.getPassword());
			registro.setOrigenRegistro(parametrosEntrada.getOrigenRegistro()); 
			registro.setoficina(parametrosEntrada.getOficina());
			registro.setNumeroEntrada(parametrosEntrada.getNumeroEntrada()); 			
			registro.setAnoEntrada(parametrosEntrada.getAnoEntrada());
			
			// Conectamos a Registro
			lc = doLogin(parametrosEntrada.getUsuario(), parametrosEntrada.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regFacade = home.create();
			
			boolean resultado = regFacade.anular(registro,true);
			logger.debug("Registro anulado: " + resultado);
			return resultado;						
		} catch (Exception ex) {
			logger.error("Excepcion anulando entrada: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}									
	}	

	public boolean anularSalida(ParametrosRegistroSalidaWS parametrosSalida,
			boolean anular) throws RemoteException, RegwebFacadeException {
		logger.debug("anularSalida: " + parametrosSalida.getOficina() + "/" + parametrosSalida.getNumeroSalida() + "/" + parametrosSalida.getAnoSalida());
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroSalida registro = new ParametrosRegistroSalida();
			registro.fijaUsuario(parametrosSalida.getUsuario());
			registro.fijaPasswordUser(parametrosSalida.getPassword());
			registro.setoficina(parametrosSalida.getOficina());
			registro.setNumeroSalida(parametrosSalida.getNumeroSalida()); 			
			registro.setAnoSalida(parametrosSalida.getAnoSalida());
			
			// Conectamos a Registro
			lc = doLogin(parametrosSalida.getUsuario(), parametrosSalida.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regent = home.create();
			
			boolean resultado = regent.anular(registro,true);
			logger.debug("Registro anulado: " + resultado);
			return resultado;						
		} catch (Exception ex) {
			logger.error("Excepcion anulando salida: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}						
	}

	public ListaResultados buscarDocumentos(String usuario, String password)
			throws RemoteException, RegwebFacadeException {
		logger.debug("buscarDocumentos");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			Vector resposta = null;
			
			// Conectamos a Registro
			lc = doLogin(usuario, password);
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
						objRef,
						ValoresFacadeHome.class);
			resposta = home.create().buscarDocumentos();
			
			String[] listaDocs;
			if (resposta != null) {
				listaDocs = new String[resposta.size()];
				for (int i=0; i<resposta.size(); i++) {
					listaDocs[i] = (String) resposta.get(i);
				}
			} else {
				listaDocs = new String[0];
			}
			
			logger.debug("documentos encontrados: " + listaDocs.length);
			return new ListaResultados(listaDocs);			
		} catch (Exception ex) {
			logger.error("Excepcion buscando documentos: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}		
	}

	public ListaResultados buscarOficinasFisicas(String usuario,
			String password, String usuarioRegistro, String tipo) throws RemoteException,
			RegwebFacadeException {
		logger.debug("buscarOficinasFisicas");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			Vector resposta = null;
			
			// Conectamos a Registro
			lc = doLogin(usuario, password);
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarOficinasFisicas(usuarioRegistro,tipo);
			
			String[] listaStr;
			if (resposta != null) {
				listaStr = new String[resposta.size()];
				for (int i=0; i<resposta.size(); i++) {
					listaStr[i] = (String) resposta.get(i);
				}
			} else {
				listaStr = new String[0];
			}
			
			logger.debug("oficinas encontradas: " + listaStr.length);
			return new ListaResultados(listaStr);			
		} catch (Exception ex) {
			logger.error("Excepcion buscarOficinasFisicas: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}		
	}

	public ListaResultados buscarOficinasFisicasDescripcion(String usuario,
			String password, String filtro, String tipo) throws RemoteException,
			RegwebFacadeException {
		logger.debug("buscarOficinasFisicasDescripcion");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			Vector resposta = null;
			
			// Conectamos a Registro
			lc = doLogin(usuario, password);
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarOficinasFisicasDescripcion(filtro,tipo);
			
			String[] listaStr;
			if (resposta != null) {
				listaStr = new String[resposta.size()];
				for (int i=0; i<resposta.size(); i++) {
					listaStr[i] = (String) resposta.get(i);
				}
			} else {
				listaStr = new String[0];
			}
			
			logger.debug("oficinas encontradas: " + listaStr.length);
			return new ListaResultados(listaStr);			
		} catch (Exception ex) {
			logger.error("Excepcion buscarOficinasFisicasDescripcion: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}		
	}

	public ListaResultados buscarTodosDestinatarios(String usuario,
			String password) throws RemoteException, RegwebFacadeException {
		
		logger.debug("buscarTodosDestinatarios");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			Vector resposta = null;
			
			// Conectamos a Registro
			lc = doLogin(usuario, password);
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarTodosDestinatarios();
			
			String[] listaStr;
			if (resposta != null) {
				listaStr = new String[resposta.size()];
				for (int i=0; i<resposta.size(); i++) {
					listaStr[i] = (String) resposta.get(i);
				}
			} else {
				listaStr = new String[0];
			}
			
			logger.debug("destinatarios encontrados: " + listaStr.length);
			return new ListaResultados(listaStr);			
		} catch (Exception ex) {
			logger.error("Excepcion buscarTodosDestinatarios: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}				
	}

	public ParametrosRegistroEntradaWS grabarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		logger.debug("grabarEntrada");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroEntrada registro = parametrosEntradaWStoParametrosEntrada(parametrosEntrada);
			
			// Conectamos a Registro
			lc = doLogin(parametrosEntrada.getUsuario(), parametrosEntrada.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regFacade = home.create();
			
			ParametrosRegistroEntrada resultado = regFacade.grabar(registro);
			
			logger.debug("Registro grabado");
			return parametrosEntradaToParametrosEntradaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion grabando entrada: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}

	public ParametrosRegistroSalidaWS grabarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		logger.debug("grabarSalida");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroSalida registro = parametrosSalidaWStoParametrosSalida(parametrosSalida);
			
			// Conectamos a Registro
			lc = doLogin(parametrosSalida.getUsuario(), parametrosSalida.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regFacade = home.create();
			
			ParametrosRegistroSalida resultado = regFacade.grabar(registro);
			
			logger.debug("Registro grabado");
			return parametrosSalidaToParametrosSalidaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion grabarSalida: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}

	public ParametrosRegistroEntradaWS leerEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		
		logger.debug("leerEntrada: " + parametrosEntrada.getOficina() + "/" + parametrosEntrada.getNumeroEntrada() + "/" + parametrosEntrada.getAnoEntrada());
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroEntrada registro = parametrosEntradaWStoParametrosEntrada(parametrosEntrada);
			
			// Conectamos a Registro
			lc = doLogin(parametrosEntrada.getUsuario(), parametrosEntrada.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regFacade = home.create();
			
			ParametrosRegistroEntrada resultado = regFacade.leer(registro);
			
			logger.debug("Registro leido");
			return parametrosEntradaToParametrosEntradaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion leerEntrada: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}		
	}

	public ParametrosRegistroSalidaWS leerSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		logger.debug("leerSalida");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroSalida registro = parametrosSalidaWStoParametrosSalida(parametrosSalida);
			
			// Conectamos a Registro
			lc = doLogin(parametrosSalida.getUsuario(), parametrosSalida.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regFacade = home.create();
			
			ParametrosRegistroSalida resultado = regFacade.leer(registro);
			
			logger.debug("Registro leido");
			return parametrosSalidaToParametrosSalidaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion leerSalida: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}

	public ParametrosRegistroEntradaWS validarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {

		logger.debug("validarEntrada");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroEntrada registro = parametrosEntradaWStoParametrosEntrada(parametrosEntrada);
			
			// Conectamos a Registro
			lc = doLogin(parametrosEntrada.getUsuario(), parametrosEntrada.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regFacade = home.create();
			
			ParametrosRegistroEntrada resultado = regFacade.validar(registro);
			
			logger.debug("Registro validado");
			return parametrosEntradaToParametrosEntradaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion validarEntrada: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}		
	}

	public ParametrosRegistroSalidaWS validarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		
		logger.debug("validarSalida");
		
		LoginContext lc = null;
		Context ctx = null;
		try {
			// Establecemos parametros
			ParametrosRegistroSalida registro = parametrosSalidaWStoParametrosSalida(parametrosSalida);
			
			// Conectamos a Registro
			lc = doLogin(parametrosSalida.getUsuario(), parametrosSalida.getPassword());
			ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regFacade = home.create();
			
			ParametrosRegistroSalida resultado = regFacade.validar(registro);
			
			logger.debug("Registro validado");
			return parametrosSalidaToParametrosSalidaWS(resultado);
									
		} catch (Exception ex) {
			logger.error("Excepcion validarSalida: " + ex.getMessage(), ex);
			throw createRegwebFacadeException(ex);
		} finally {	
			if (lc != null) {
				try{lc.logout();}catch(Exception e1){}
			}
			if (ctx != null) {
				try{ctx.close();}catch(Exception e1){}
			}
		}			
	}	
	
	
	private ParametrosRegistroEntradaWS parametrosEntradaToParametrosEntradaWS(
			ParametrosRegistroEntrada parametrosEntrada) {
		ParametrosRegistroEntradaWS registro = new ParametrosRegistroEntradaWS();
		registro.setUsuario(parametrosEntrada.getUsuario());
		registro.setPassword(parametrosEntrada.getPassword());
		registro.setActualizacion(parametrosEntrada.getActualizacion());
		registro.setAltres(parametrosEntrada.getAltres());
		registro.setAltresNuevo(parametrosEntrada.getAltresNuevo());
		registro.setAnoEntrada(parametrosEntrada.getAnoEntrada());
		registro.setBalears(parametrosEntrada.getBalears());
		registro.setComentario(parametrosEntrada.getComentario());
		registro.setComentarioNuevo(parametrosEntrada.getComentarioNuevo());
		registro.setCorreo(parametrosEntrada.getCorreo());
		registro.setData(parametrosEntrada.getData());
		registro.setDataentrada(parametrosEntrada.getDataEntrada());
		registro.setDataVisado(parametrosEntrada.getDataVisado());
		registro.setDescripcionDocumento(parametrosEntrada.getDescripcionDocumento());
		registro.setDescripcionIdiomaDocumento(parametrosEntrada.getDescripcionIdiomaDocumento());
		registro.setDescripcionMunicipi060(parametrosEntrada.getDescripcionMunicipi060());
		registro.setDescripcionOficina(parametrosEntrada.getDescripcionOficina());
		registro.setDescripcionOficinaFisica(parametrosEntrada.getDescripcionOficinaFisica());
		registro.setDescripcionOrganismoDestinatario(parametrosEntrada.getDescripcionOrganismoDestinatario());
		registro.setDescripcionRemitente(parametrosEntrada.getDescripcionRemitente());
		registro.setDestinatari(parametrosEntrada.getDestinatari());
		registro.setDisquet(parametrosEntrada.getDisquet());
		registro.setEmailRemitent(parametrosEntrada.getEmailRemitent());
		registro.setEntidad1(parametrosEntrada.getEntidad1());
		registro.setEntidad1Grabada(parametrosEntrada.getEntidad1Grabada());
		registro.setEntidad1Nuevo(parametrosEntrada.getEntidad1Nuevo());
		registro.setEntidad2(parametrosEntrada.getEntidad2());
		registro.setEntidad2Nuevo(parametrosEntrada.getEntidad2Nuevo());
		registro.setEntidadCastellano(parametrosEntrada.getEntidadCastellano());
		registro.setFora(parametrosEntrada.getFora());
		registro.setHora(parametrosEntrada.getHora());
		registro.setIdioex(parametrosEntrada.getIdioex());
		registro.setIdioma(parametrosEntrada.getIdioma());
		registro.setIdiomaExtracto(parametrosEntrada.getIdiomaExtracto());
		registro.setLeido(parametrosEntrada.getLeido());
		registro.setLocalitzadorsDocs(parametrosEntrada.getLocalitzadorsDocs());
		registro.setMotivo(parametrosEntrada.getMotivo());
		registro.setMunicipi060(parametrosEntrada.getMunicipi060());
		registro.setNumeroDocumentosRegistro060(parametrosEntrada.getNumeroDocumentosRegistro060());
		registro.setNumeroEntrada(parametrosEntrada.getNumeroEntrada());
		registro.setOficina(parametrosEntrada.getOficina());
		registro.setOficinafisica(parametrosEntrada.getOficinafisica());
		registro.setOrigenRegistro(parametrosEntrada.getOrigenRegistro());
		if (parametrosEntrada.getParamRegPubEnt() != null) {
			ParametrosRegistroPublicadoEntradaWS pub = new ParametrosRegistroPublicadoEntradaWS();
			pub.setAnoEntrada(parametrosEntrada.getParamRegPubEnt().getAnoEntrada());
			pub.setContenido(parametrosEntrada.getParamRegPubEnt().getContenido());
			pub.setFecha(parametrosEntrada.getParamRegPubEnt().getFecha());
			pub.setLeido(parametrosEntrada.getParamRegPubEnt().getLeido());
			pub.setLineas(parametrosEntrada.getParamRegPubEnt().getLineas());
			pub.setNumero(parametrosEntrada.getParamRegPubEnt().getNumero());
			pub.setNumeroBOCAIB(parametrosEntrada.getParamRegPubEnt().getNumeroBOCAIB());
			pub.setObservaciones(parametrosEntrada.getParamRegPubEnt().getObservaciones());
			pub.setOficina(parametrosEntrada.getParamRegPubEnt().getOficina());
			pub.setPagina(parametrosEntrada.getParamRegPubEnt().getPagina());				
			registro.setParamRegPubEnt(pub);
		}
		
		registro.setProcedenciaGeografica(parametrosEntrada.getProcedenciaGeografica());			
		registro.setRegistroActualizado(parametrosEntrada.getregistroActualizado());
		registro.setRegistroAnulado(parametrosEntrada.getRegistroAnulado());
		registro.setRegistroGrabado(parametrosEntrada.getregistroGrabado());
		registro.setSalida1(parametrosEntrada.getSalida1());
		registro.setSalida2(parametrosEntrada.getSalida2());
		registro.setTipo(parametrosEntrada.getTipo());
		registro.setValidado(parametrosEntrada.getValidado());
		
		if (parametrosEntrada.getErrores() != null) {
			ListaErroresEntrada listaErrores = new ListaErroresEntrada();
			ErrorEntrada[] errores = new ErrorEntrada[parametrosEntrada.getErrores().size()];
			int i=0;
			for (Iterator it = parametrosEntrada.getErrores().keySet().iterator(); it.hasNext();) {
				String codError = (String) it.next();
				String desError = (String) parametrosEntrada.getErrores().get(codError);
				errores[i] = new ErrorEntrada(codError, desError);
				i++;
			}			
			listaErrores.setErrores(errores);
		}
		
		return registro;
	}

	private ParametrosRegistroEntrada parametrosEntradaWStoParametrosEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada) {
		ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();		
		registro.fijaUsuario(parametrosEntrada.getUsuario());
		registro.fijaPasswordUser(parametrosEntrada.getPassword());
		if (parametrosEntrada.getActualizacion() != null)
			registro.setActualizacion(booleanValue(parametrosEntrada
					.getActualizacion()));
		if (parametrosEntrada.getAltres() != null)
			registro.setaltres(parametrosEntrada.getAltres());
		if (parametrosEntrada.getAltresNuevo() != null)
			registro.setAltresNuevo(parametrosEntrada.getAltresNuevo());
		if (parametrosEntrada.getAnoEntrada() != null)
			registro.setAnoEntrada(parametrosEntrada.getAnoEntrada());
		if (parametrosEntrada.getBalears() != null)
			registro.setbalears(parametrosEntrada.getBalears());
		if (parametrosEntrada.getComentario() != null)
			registro.setcomentario(parametrosEntrada.getComentario());
		if (parametrosEntrada.getComentarioNuevo() != null)
			registro.setComentarioNuevo(parametrosEntrada.getComentarioNuevo());
		if (parametrosEntrada.getCorreo() != null)
			registro.setCorreo(parametrosEntrada.getCorreo());
		if (parametrosEntrada.getData() != null)
			registro.setdata(parametrosEntrada.getData());
		if (parametrosEntrada.getDataentrada() != null)
			registro.setdataentrada(parametrosEntrada.getDataentrada());
		if (parametrosEntrada.getDataVisado() != null)
			registro.setDataVisado(parametrosEntrada.getDataVisado());
		if (parametrosEntrada.getDescripcionDocumento() != null)
			registro.setDescripcionDocumento(parametrosEntrada
					.getDescripcionDocumento());
		if (parametrosEntrada.getDescripcionIdiomaDocumento() != null)
			registro.setDescripcionIdiomaDocumento(parametrosEntrada
					.getDescripcionIdiomaDocumento());
		if (parametrosEntrada.getDescripcionMunicipi060() != null)
			registro.setDescripcionMunicipi060(parametrosEntrada
					.getDescripcionMunicipi060());
		if (parametrosEntrada.getDescripcionOficina() != null)
			registro.setDescripcionOficina(parametrosEntrada
					.getDescripcionOficina());
		if (parametrosEntrada.getDescripcionOficinaFisica() != null)
			registro.setDescripcionOficinaFisica(parametrosEntrada
					.getDescripcionOficinaFisica());
		if (parametrosEntrada.getDescripcionOrganismoDestinatario() != null)
			registro.setDescripcionOrganismoDestinatario(parametrosEntrada
					.getDescripcionOrganismoDestinatario());
		if (parametrosEntrada.getDescripcionRemitente() != null)
			registro.setDescripcionRemitente(parametrosEntrada
					.getDescripcionRemitente());
		if (parametrosEntrada.getDestinatari() != null)
			registro.setdestinatari(parametrosEntrada.getDestinatari());
		if (parametrosEntrada.getDisquet() != null)
			registro.setdisquet(parametrosEntrada.getDisquet());
		if (parametrosEntrada.getEmailRemitent() != null)
			registro.setEmailRemitent(parametrosEntrada.getEmailRemitent());
		if (parametrosEntrada.getEntidad1() != null)
			registro.setentidad1(parametrosEntrada.getEntidad1());
		if (parametrosEntrada.getEntidad1Grabada() != null)
			registro.setEntidad1Grabada(parametrosEntrada.getEntidad1Grabada());
		if (parametrosEntrada.getEntidad1Nuevo() != null)
			registro.setEntidad1Nuevo(parametrosEntrada.getEntidad1Nuevo());
		if (parametrosEntrada.getEntidad2() != null)
			registro.setentidad2(parametrosEntrada.getEntidad2());
		if (parametrosEntrada.getEntidad2Nuevo() != null)
			registro.setEntidad2Nuevo(parametrosEntrada.getEntidad2Nuevo());
		if (parametrosEntrada.getEntidadCastellano() != null)
			registro.setEntidadCastellano(parametrosEntrada
					.getEntidadCastellano());
		if (parametrosEntrada.getFora() != null)
			registro.setfora(parametrosEntrada.getFora());
		if (parametrosEntrada.getHora() != null)
			registro.sethora(parametrosEntrada.getHora());
		if (parametrosEntrada.getIdioex() != null)
			registro.setidioex(parametrosEntrada.getIdioex());
		if (parametrosEntrada.getIdioma() != null)
			registro.setidioma(parametrosEntrada.getIdioma());
		if (parametrosEntrada.getIdiomaExtracto() != null)
			registro.setIdiomaExtracto(parametrosEntrada.getIdiomaExtracto());
		if (parametrosEntrada.getLeido() != null)
			registro.setLeido(booleanValue(parametrosEntrada.getLeido()));
		if (parametrosEntrada.getLocalitzadorsDocs() != null)
			registro.setLocalitzadorsDocs(parametrosEntrada
					.getLocalitzadorsDocs());
		if (parametrosEntrada.getMotivo() != null)
			registro.setMotivo(parametrosEntrada.getMotivo());
		if (parametrosEntrada.getMunicipi060() != null)
			registro.setMunicipi060(parametrosEntrada.getMunicipi060());
		if (parametrosEntrada.getNumeroDocumentosRegistro060() != null)
			registro.setNumeroDocumentosRegistro060(parametrosEntrada
					.getNumeroDocumentosRegistro060());
		if (parametrosEntrada.getNumeroEntrada() != null)
			registro.setNumeroEntrada(parametrosEntrada.getNumeroEntrada());
		if (parametrosEntrada.getOficina() != null)
			registro.setoficina(parametrosEntrada.getOficina());
		if (parametrosEntrada.getOficinafisica() != null)
			registro.setoficinafisica(parametrosEntrada.getOficinafisica());
		if (parametrosEntrada.getOrigenRegistro() != null)
			registro.setOrigenRegistro(parametrosEntrada.getOrigenRegistro());

		if (parametrosEntrada.getParamRegPubEnt() != null) {
			ParametrosRegistroPublicadoEntrada pub = new ParametrosRegistroPublicadoEntrada();
			pub.setAnoEntrada(parametrosEntrada.getParamRegPubEnt()
					.getAnoEntrada());
			pub.setContenido(parametrosEntrada.getParamRegPubEnt()
					.getContenido());
			pub.setFecha(parametrosEntrada.getParamRegPubEnt().getFecha());
			pub.setLeido(parametrosEntrada.getParamRegPubEnt().isLeido());
			pub.setLineas(parametrosEntrada.getParamRegPubEnt().getLineas());
			pub.setNumero(parametrosEntrada.getParamRegPubEnt().getNumero());
			pub.setNumeroBOCAIB(parametrosEntrada.getParamRegPubEnt()
					.getNumeroBOCAIB());
			if (parametrosEntrada.getParamRegPubEnt()
					.getObservaciones() != null)
				pub.setObservaciones(parametrosEntrada.getParamRegPubEnt()
					.getObservaciones());
			pub.setOficina(parametrosEntrada.getParamRegPubEnt().getOficina());
			pub.setPagina(parametrosEntrada.getParamRegPubEnt().getPagina());
			registro.setParamRegPubEnt(pub);
		}

		if (parametrosEntrada.getProcedenciaGeografica() != null)
			registro.setProcedenciaGeografica(parametrosEntrada
					.getProcedenciaGeografica());
		if (parametrosEntrada.getRegistroActualizado() != null)
			registro.setregistroActualizado(booleanValue(parametrosEntrada
					.getRegistroActualizado()));
		if (parametrosEntrada.getRegistroAnulado() != null)
			registro.setRegistroAnulado(parametrosEntrada.getRegistroAnulado());
		if (parametrosEntrada.getRegistroGrabado() != null)
			registro.setregistroGrabado(booleanValue(parametrosEntrada
					.getRegistroGrabado()));
		if (parametrosEntrada.getSalida1() != null)
			registro.setsalida1(parametrosEntrada.getSalida1());
		if (parametrosEntrada.getSalida2() != null)
			registro.setsalida2(parametrosEntrada.getSalida2());
		if (parametrosEntrada.getTipo() != null)
			registro.settipo(parametrosEntrada.getTipo());
		if (parametrosEntrada.getValidado() != null)
			registro.setValidado(booleanValue(parametrosEntrada.getValidado()));
		
		return registro;
	}

	private Context getInitialContext() throws Exception {		
		//return EjbUtil.getInitialContext(false,System.getProperty("es.caib.regweb.urlEjb"));		
	  return EjbUtil.getInitialContext(true,null);   
	}

	private LoginContext doLogin(String userName, String password) throws Exception {
		LoginContext lc = null;		
		if (userName != null && userName.length() > 0 && password != null && password.length() > 0) {
			lc = new LoginContext(
					"client-login",
					new UsernamePasswordCallbackHandler(userName, password));
			lc.login();
		}
		return lc;
	}
	
	private RegwebFacadeException createRegwebFacadeException(Exception ex) {
		RegwebFacadeException rfe = new RegwebFacadeException();
		rfe.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "Sender"));
		rfe.setFaultString(ex.getMessage());
		rfe.setFaultDetailString(ExceptionUtils.getStackTrace(ex));
		return rfe;
	}

	private boolean booleanValue(Boolean boolean1) {
		return (boolean1!=null?boolean1.booleanValue():false);
	}
	
	private ParametrosRegistroSalidaWS parametrosSalidaToParametrosSalidaWS(
			ParametrosRegistroSalida parametrosSalida) {
		ParametrosRegistroSalidaWS registro = new ParametrosRegistroSalidaWS();
		registro.setUsuario(parametrosSalida.getUsuario());
		registro.setPassword(parametrosSalida.getPassword());
		registro.setActualizacion(parametrosSalida.getActualizacion());
		registro.setAltres(parametrosSalida.getAltres());
		registro.setAltresNuevo(parametrosSalida.getAltresNuevo());
		registro.setAnoSalida(parametrosSalida.getAnoSalida());
		registro.setBalears(parametrosSalida.getBalears());
		registro.setComentario(parametrosSalida.getComentario());
		registro.setComentarioNuevo(parametrosSalida.getComentarioNuevo());
		registro.setCorreo(parametrosSalida.getCorreo());
		registro.setData(parametrosSalida.getData());
		registro.setDatasalida(parametrosSalida.getDataSalida());
		registro.setDataVisado(parametrosSalida.getDataVisado());
		registro.setDescripcionDestinatario(parametrosSalida.getDescripcionDestinatario());
		registro.setDescripcionDocumento(parametrosSalida.getDescripcionDocumento());
		registro.setDescripcionIdiomaDocumento(parametrosSalida.getDescripcionIdiomaDocumento());
		registro.setDescripcionOficina(parametrosSalida.getDescripcionOficina());
		registro.setDescripcionOficinaFisica(parametrosSalida.getDescripcionOficinaFisica());
		registro.setDescripcionOrganismoRemitente(parametrosSalida.getDescripcionOrganismoRemitente());
		registro.setDestinoGeografico(parametrosSalida.getDestinoGeografico());
		registro.setDisquet(parametrosSalida.getDisquet());
		registro.setEmailRemitent(parametrosSalida.getEmailRemitent());
		registro.setEntidad1(parametrosSalida.getEntidad1());
		registro.setEntidad1Grabada(parametrosSalida.getEntidad1Grabada());
		registro.setEntidad1Nuevo(parametrosSalida.getEntidad1Nuevo());
		registro.setEntidad2(parametrosSalida.getEntidad2());
		registro.setEntidad2Nuevo(parametrosSalida.getEntidad2Nuevo());
		registro.setEntidadCastellano(parametrosSalida.getEntidadCastellano());
		registro.setEntrada1(parametrosSalida.getEntrada1());
		registro.setEntrada2(parametrosSalida.getEntrada2());
		registro.setFora(parametrosSalida.getFora());
		registro.setHora(parametrosSalida.getHora());
		registro.setIdioex(parametrosSalida.getIdioex());
		registro.setIdioma(parametrosSalida.getIdioma());
		registro.setIdiomaExtracto(parametrosSalida.getIdiomaExtracto());
		registro.setLeido(parametrosSalida.getLeido());
		registro.setLocalitzadorsDocs(parametrosSalida.getLocalitzadorsDocs());
		registro.setMotivo(parametrosSalida.getMotivo());
		registro.setNumeroSalida(parametrosSalida.getNumeroSalida());
		registro.setOficina(parametrosSalida.getOficina());
		registro.setOficinafisica(parametrosSalida.getOficinafisica());
		registro.setRegistroActualizado(parametrosSalida.getregistroActualizado());
		registro.setRegistroAnulado(parametrosSalida.getRegistroAnulado());
		registro.setRegistroSalidaGrabado(parametrosSalida.getregistroSalidaGrabado());
		registro.setRemitent(parametrosSalida.getRemitent());
		registro.setTipo(parametrosSalida.getTipo());
		registro.setValidado(parametrosSalida.getValidado());
		
		if (parametrosSalida.getErrores() != null) {
			ListaErroresSalida listaErrores = new ListaErroresSalida();
			ErrorSalida[] errores = new ErrorSalida[parametrosSalida.getErrores().size()];
			int i=0;
			for (Iterator it = parametrosSalida.getErrores().keySet().iterator(); it.hasNext();) {
				String codError = (String) it.next();
				String desError = (String) parametrosSalida.getErrores().get(codError);
				errores[i] = new ErrorSalida(codError, desError);
				i++;
			}			
			listaErrores.setErrores(errores);
		}
		
		return registro;
	}




	private ParametrosRegistroSalida parametrosSalidaWStoParametrosSalida(
			ParametrosRegistroSalidaWS parametrosSalida) {
		ParametrosRegistroSalida registro = new ParametrosRegistroSalida();
		registro.fijaUsuario(parametrosSalida.getUsuario());
		registro.fijaPasswordUser(parametrosSalida.getPassword());
		if (parametrosSalida.getActualizacion() != null) registro.setActualizacion(booleanValue(parametrosSalida.getActualizacion()));
		if (parametrosSalida.getAltres() != null) registro.setaltres(parametrosSalida.getAltres());
		if (parametrosSalida.getAltresNuevo() != null) registro.setAltresNuevo(parametrosSalida.getAltresNuevo());
		if (parametrosSalida.getAnoSalida() != null) registro.setAnoSalida(parametrosSalida.getAnoSalida());
		if (parametrosSalida.getBalears() != null) registro.setbalears(parametrosSalida.getBalears());
		if (parametrosSalida.getComentario() != null) registro.setcomentario(parametrosSalida.getComentario());
		if (parametrosSalida.getComentarioNuevo() != null) registro.setComentarioNuevo(parametrosSalida.getComentarioNuevo());
		if (parametrosSalida.getCorreo() != null) registro.setCorreo(parametrosSalida.getCorreo());
		if (parametrosSalida.getData() != null) registro.setdata(parametrosSalida.getData());
		if (parametrosSalida.getDatasalida() != null) registro.setdatasalida(parametrosSalida.getDatasalida());
		if (parametrosSalida.getDataVisado() != null) registro.setDataVisado(parametrosSalida.getDataVisado());
		if (parametrosSalida.getDescripcionDestinatario() != null) registro.setDescripcionDestinatario(parametrosSalida.getDescripcionDestinatario());
		if (parametrosSalida.getDescripcionDocumento() != null) registro.setDescripcionDocumento(parametrosSalida.getDescripcionDocumento());
		if (parametrosSalida.getDescripcionIdiomaDocumento() != null) registro.setDescripcionIdiomaDocumento(parametrosSalida.getDescripcionIdiomaDocumento());
		if (parametrosSalida.getDescripcionOficina() != null) registro.setDescripcionOficina(parametrosSalida.getDescripcionOficina());
		if (parametrosSalida.getDescripcionOficinaFisica() != null) registro.setDescripcionOficinaFisica(parametrosSalida.getDescripcionOficinaFisica());
		if (parametrosSalida.getDescripcionOrganismoRemitente() != null) registro.setDescripcionOrganismoRemitente(parametrosSalida.getDescripcionOrganismoRemitente());
		if (parametrosSalida.getDestinoGeografico() != null) registro.setDestinoGeografico(parametrosSalida.getDestinoGeografico());
		if (parametrosSalida.getDisquet() != null) registro.setdisquet(parametrosSalida.getDisquet());
		if (parametrosSalida.getEmailRemitent() != null) registro.setEmailRemitent(parametrosSalida.getEmailRemitent());
		if (parametrosSalida.getEntidad1() != null) registro.setentidad1(parametrosSalida.getEntidad1());
		if (parametrosSalida.getEntidad1Grabada() != null) registro.setEntidad1Grabada(parametrosSalida.getEntidad1Grabada());
		if (parametrosSalida.getEntidad1Nuevo() != null) registro.setEntidad1Nuevo(parametrosSalida.getEntidad1Nuevo());
		if (parametrosSalida.getEntidad2() != null) registro.setentidad2(parametrosSalida.getEntidad2());
		if (parametrosSalida.getEntidad2Nuevo() != null) registro.setEntidad2Nuevo(parametrosSalida.getEntidad2Nuevo());
		if (parametrosSalida.getEntidadCastellano() != null) registro.setEntidadCastellano(parametrosSalida.getEntidadCastellano());
		if (parametrosSalida.getEntrada1() != null) registro.setentrada1(parametrosSalida.getEntrada1());
		if (parametrosSalida.getEntrada2() != null) registro.setentrada2(parametrosSalida.getEntrada2());
		if (parametrosSalida.getFora() != null) registro.setfora(parametrosSalida.getFora());
		if (parametrosSalida.getHora() != null) registro.sethora(parametrosSalida.getHora());
		if (parametrosSalida.getIdioex() != null) registro.setidioex(parametrosSalida.getIdioex());
		if (parametrosSalida.getIdioma() != null) registro.setidioma(parametrosSalida.getIdioma());
		if (parametrosSalida.getIdiomaExtracto() != null) registro.setIdiomaExtracto(parametrosSalida.getIdiomaExtracto());
		if (parametrosSalida.getLeido() != null) registro.setLeido(parametrosSalida.getLeido());
		if (parametrosSalida.getLocalitzadorsDocs() != null) registro.setLocalitzadorsDocs(parametrosSalida.getLocalitzadorsDocs());
		if (parametrosSalida.getMotivo() != null) registro.setMotivo(parametrosSalida.getMotivo());
		if (parametrosSalida.getNumeroSalida() != null) registro.setNumeroSalida(parametrosSalida.getNumeroSalida());
		if (parametrosSalida.getOficina() != null) registro.setoficina(parametrosSalida.getOficina());
		if (parametrosSalida.getOficinafisica() != null) registro.setoficinafisica(parametrosSalida.getOficinafisica());
		if (parametrosSalida.getRegistroActualizado() != null) registro.setregistroActualizado(booleanValue(parametrosSalida.getRegistroActualizado()));
		if (parametrosSalida.getRegistroAnulado() != null) registro.setRegistroAnulado(parametrosSalida.getRegistroAnulado());
		if (parametrosSalida.getRegistroSalidaGrabado() != null) registro.setregistroSalidaGrabado(booleanValue(parametrosSalida.getRegistroSalidaGrabado()));
		if (parametrosSalida.getRemitent() != null) registro.setremitent(parametrosSalida.getRemitent());
		if (parametrosSalida.getTipo() != null) registro.settipo(parametrosSalida.getTipo());
		if (parametrosSalida.getValidado() != null) registro.setValidado(booleanValue(parametrosSalida.getValidado()));
		return registro;
	}


}
