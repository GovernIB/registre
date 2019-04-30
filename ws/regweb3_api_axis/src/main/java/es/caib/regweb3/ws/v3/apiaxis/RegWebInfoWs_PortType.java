/**
 * RegWebInfoWs_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public interface RegWebInfoWs_PortType extends java.rmi.Remote {
    public es.caib.regweb3.ws.v3.apiaxis.LibroWs listarLibroOrganismo(java.lang.String entidad, java.lang.String organismo) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[] obtenerLibrosOficinaUsuario(java.lang.String entidadCodigoDir3, java.lang.String usuario, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[] listarCodigoAsunto(java.lang.String entidadCodigoDir3, java.lang.String codigoTipoAsunto) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public java.lang.String getVersion() throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[] listarTipoAsunto(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.LibroWs[] listarLibros(java.lang.String entidadCodigoDir3, java.lang.String oficinaCodigoDir3, java.lang.Long autorizacion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[] listarOrganismos(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[] listarTipoDocumental(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public int getVersionWs() throws java.rmi.RemoteException;
    public es.caib.regweb3.ws.v3.apiaxis.OficinaWs[] listarOficinas(java.lang.String entidadCodigoDir3, java.lang.Long autorizacion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
    public es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[] obtenerLibrosOficina(java.lang.String entidadCodigoDir3, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError;
}
