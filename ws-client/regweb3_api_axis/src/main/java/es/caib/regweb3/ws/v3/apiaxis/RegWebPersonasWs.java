/**
 * RegWebPersonasWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface RegWebPersonasWs extends java.rmi.Remote {
    public java.lang.String getVersion() throws java.rmi.RemoteException;
    public void borrarPersona(java.lang.Long personaID) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.PersonaWs[] listarPersonas(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public java.lang.Long crearPersona(es.caib.regweb3.ws.v3.apiaxis.PersonaWs personaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public int getVersionWs() throws java.rmi.RemoteException;
    public void actualizarPersona(es.caib.regweb3.ws.v3.apiaxis.PersonaWs personaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
}
