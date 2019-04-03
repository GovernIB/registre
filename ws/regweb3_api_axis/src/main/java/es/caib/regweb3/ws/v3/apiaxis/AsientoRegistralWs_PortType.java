/**
 * AsientoRegistralWs_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface AsientoRegistralWs_PortType extends java.rmi.Remote {
    public es.caib.regweb3.ws.v3.apiaxis.JustificanteWs obtenerJustificante(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.String libro, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.OficioWs obtenerOficioExterno(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.String libro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public void distribuirAsientoRegistral(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.String libro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs obtenerAsientoRegistral(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.String libro, java.lang.Long tipoRegistro, boolean conAnexos) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs[] obtenerAsientosCiudadano(java.lang.String entidad, java.lang.String documento) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs crearAsientoRegistral(java.lang.String entidad, es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistral, java.lang.Long tipoOperacion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
}
