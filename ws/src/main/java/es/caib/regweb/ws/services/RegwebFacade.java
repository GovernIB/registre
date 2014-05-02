/**
 * RegwebFacade.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services;

public interface RegwebFacade extends java.rmi.Remote {
    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS actualizarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS actualizarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public boolean anularEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public boolean anularSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS grabarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS grabarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS leerEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS leerSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS validarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS validarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ListaResultados buscarOficinasFisicasDescripcion(java.lang.String usuario, java.lang.String password, java.lang.String filtro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ListaResultados buscarOficinasFisicas(java.lang.String usuario, java.lang.String password, java.lang.String usuarioRegistro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ListaResultados buscarDocumentos(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;
    public es.caib.regweb.ws.model.ListaResultados buscarTodosDestinatarios(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException;

}
