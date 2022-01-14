/**
 * RegWebRegistroEntradaWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface RegWebRegistroEntradaWs extends java.rmi.Remote {
    public java.lang.String getVersion() throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.RegistroEntradaWs obtenerRegistroEntrada(java.lang.String numeroRegistroFormateado, java.lang.String usuario, java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public void tramitarRegistroEntrada(java.lang.String numeroRegistroFormateado, java.lang.String usuario, java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.IdentificadorWs altaRegistroEntrada(es.caib.regweb3.ws.v3.apiaxis.RegistroEntradaWs registroEntradaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public void anularRegistroEntrada(java.lang.String numeroRegistroFormateado, java.lang.String usuario, java.lang.String entidad, boolean anular) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.IdentificadorWs obtenerRegistroEntradaID(int any, int numeroRegistro, java.lang.String libro, java.lang.String usuario, java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public int getVersionWs() throws java.rmi.RemoteException;
}
