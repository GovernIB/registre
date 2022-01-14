/**
 * RegWebRegistroSalidaWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface RegWebRegistroSalidaWs extends java.rmi.Remote {
    public java.lang.String getVersion() throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.RegistroSalidaWs obtenerRegistroSalida(java.lang.String numeroRegistro, java.lang.String usuario, java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.IdentificadorWs altaRegistroSalida(es.caib.regweb3.ws.v3.apiaxis.RegistroSalidaWs registroSalidaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public void anularRegistroSalida(java.lang.String numeroRegistro, java.lang.String usuario, java.lang.String entidad, boolean anular) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public int getVersionWs() throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.IdentificadorWs obtenerRegistroSalidaID(int any, int numeroRegistro, java.lang.String libro, java.lang.String usuario, java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
}
