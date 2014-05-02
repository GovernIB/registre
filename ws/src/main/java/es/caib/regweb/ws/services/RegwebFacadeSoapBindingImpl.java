/**
 * RegwebFacadeSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services;

import java.rmi.RemoteException;

import es.caib.regweb.ws.RegwebFacadeImpl;
import es.caib.regweb.ws.model.ListaResultados;
import es.caib.regweb.ws.model.ParametrosRegistroEntradaWS;
import es.caib.regweb.ws.model.ParametrosRegistroSalidaWS;
import es.caib.regweb.ws.model.RegwebFacadeException;

public class RegwebFacadeSoapBindingImpl implements es.caib.regweb.ws.services.RegwebFacade{


	private RegwebFacadeImpl crearFacade() {
		RegwebFacadeImpl facade = new RegwebFacadeImpl();
		return facade;
	}
	
	public ParametrosRegistroEntradaWS actualizarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		return crearFacade().actualizarEntrada(parametrosEntrada);
	}


	public ParametrosRegistroSalidaWS actualizarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		return crearFacade().actualizarSalida(parametrosSalida);
	}

	public boolean anularEntrada(ParametrosRegistroEntradaWS parametrosEntrada,
			boolean anular) throws RemoteException, RegwebFacadeException {
		
		return crearFacade().anularEntrada(parametrosEntrada, anular);
	}

	public boolean anularSalida(ParametrosRegistroSalidaWS parametrosSalida,
			boolean anular) throws RemoteException, RegwebFacadeException {
		
		return crearFacade().anularSalida(parametrosSalida, anular);
	}

	public ListaResultados buscarDocumentos(String usuario, String password)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().buscarDocumentos(usuario, password);
	}

	public ListaResultados buscarOficinasFisicas(String usuario,
			String password, String usuarioRegistro, String tipo)
			throws RemoteException, RegwebFacadeException {
		return crearFacade().buscarOficinasFisicas(usuario, password, usuarioRegistro, tipo);
	}

	public ListaResultados buscarOficinasFisicasDescripcion(String usuario,
			String password, String filtro, String tipo) throws RemoteException,
			RegwebFacadeException {
		
		return crearFacade().buscarOficinasFisicasDescripcion(usuario, password, filtro, tipo);
	}

	
	public ListaResultados buscarTodosDestinatarios(String usuario,
			String password) throws RemoteException, RegwebFacadeException {
		
		return crearFacade().buscarTodosDestinatarios(usuario, password)
		;
	}

	public ParametrosRegistroEntradaWS grabarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().grabarEntrada(parametrosEntrada);
	}

	public ParametrosRegistroSalidaWS grabarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().grabarSalida(parametrosSalida);
	}

	public ParametrosRegistroEntradaWS leerEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().leerEntrada(parametrosEntrada);
	}

	public ParametrosRegistroSalidaWS leerSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().leerSalida(parametrosSalida);
	}

	public ParametrosRegistroEntradaWS validarEntrada(
			ParametrosRegistroEntradaWS parametrosEntrada)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().validarEntrada(parametrosEntrada);
	}

	public ParametrosRegistroSalidaWS validarSalida(
			ParametrosRegistroSalidaWS parametrosSalida)
			throws RemoteException, RegwebFacadeException {
		
		return crearFacade().validarSalida(parametrosSalida);
	}

	

	
   
}
