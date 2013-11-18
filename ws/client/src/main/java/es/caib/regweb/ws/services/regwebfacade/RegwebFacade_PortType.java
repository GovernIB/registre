/**
 * RegwebFacade_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

public interface RegwebFacade_PortType extends java.rmi.Remote {
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS actualizarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS actualizarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public boolean anularEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public boolean anularSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS grabarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS grabarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS leerEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS leerSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS validarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS validarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarOficinasFisicasDescripcion(java.lang.String usuario, java.lang.String password, java.lang.String filtro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarOficinasFisicas(java.lang.String usuario, java.lang.String password, java.lang.String usuarioRegistro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarDocumentos(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarTodosDestinatarios(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
}
