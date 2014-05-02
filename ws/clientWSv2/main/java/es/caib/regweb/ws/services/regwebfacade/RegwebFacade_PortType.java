/**
 * RegwebFacade_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

import es.caib.regweb.ws.model.ListaResultados;
import es.caib.regweb.ws.model.ParametrosRegistroEntradaWS;
import es.caib.regweb.ws.model.ParametrosRegistroSalidaWS;

public interface RegwebFacade_PortType extends java.rmi.Remote {
    public ParametrosRegistroEntradaWS actualizarEntrada(ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroSalidaWS actualizarSalida(ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public boolean anularEntrada(ParametrosRegistroEntradaWS parametrosEntrada, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public boolean anularSalida(ParametrosRegistroSalidaWS parametrosSalida, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroEntradaWS grabarEntrada(ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroSalidaWS grabarSalida(ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroEntradaWS leerEntrada(ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroSalidaWS leerSalida(ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroEntradaWS validarEntrada(ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ParametrosRegistroSalidaWS validarSalida(ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ListaResultados buscarOficinasFisicasDescripcion(java.lang.String usuario, java.lang.String password, java.lang.String filtro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ListaResultados buscarOficinasFisicas(java.lang.String usuario, java.lang.String password, java.lang.String usuarioRegistro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ListaResultados buscarDocumentos(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
    public ListaResultados buscarTodosDestinatarios(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException;
}
