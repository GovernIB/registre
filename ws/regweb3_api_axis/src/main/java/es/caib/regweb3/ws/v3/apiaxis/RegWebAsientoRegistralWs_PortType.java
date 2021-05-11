/**
 * RegWebAsientoRegistralWs_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface RegWebAsientoRegistralWs_PortType extends java.rmi.Remote {
    public es.caib.regweb3.ws.v3.apiaxis.JustificanteWs obtenerJustificante(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.OficioWs obtenerOficioExterno(java.lang.String entidad, java.lang.String numeroRegistroFormateado) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.ResultadoBusquedaWs obtenerAsientosCiudadanoCarpeta(java.lang.String entidad, java.lang.String documento, java.lang.Integer pageNumber, java.lang.String idioma, java.util.Calendar fechaInicio, java.util.Calendar fechaFin, java.lang.String numeroRegistroFormateado, int[] estados) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public void distribuirAsientoRegistral(java.lang.String entidad, java.lang.String numeroRegistroFormateado) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs obtenerAsientoRegistral(java.lang.String entidad, java.lang.String numeroRegistroFormateado, java.lang.Long tipoRegistro, boolean conAnexos) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs obtenerAsientoCiudadano(java.lang.String entidad, java.lang.String documento, java.lang.String numeroRegistroFormateado) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.JustificanteReferenciaWs obtenerReferenciaJustificante(java.lang.String entidad, java.lang.String numeroRegistroFormateado) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoWs obtenerAsientoCiudadanoCarpeta(java.lang.String entidad, java.lang.String documento, java.lang.String numeroRegistroFormateado, java.lang.String idioma) throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralSesionWs verificarAsientoRegistral(java.lang.String entidad, java.lang.Long idSesion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public java.lang.Long obtenerSesionRegistro(java.lang.String entidad) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.ResultadoBusquedaWs obtenerAsientosCiudadano(java.lang.String entidad, java.lang.String documento, java.lang.Integer pageNumber) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.FileContentWs obtenerAnexoCiudadano(java.lang.String entidad, java.lang.Long idAnexo, java.lang.String idioma) throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs crearAsientoRegistral(java.lang.Long idSesion, java.lang.String entidad, es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistral, java.lang.Long tipoOperacion, java.lang.Boolean justificante, java.lang.Boolean distribuir) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
}
