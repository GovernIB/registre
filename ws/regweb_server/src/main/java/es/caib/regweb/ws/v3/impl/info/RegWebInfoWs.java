package es.caib.regweb.ws.v3.impl.info;

import java.util.List;


import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.CodigoAsuntoWs;
import es.caib.regweb.ws.model.LibroWs;
import es.caib.regweb.ws.model.OrganismoWs;

import es.caib.regweb.ws.model.TipoAsuntoWs;

import org.fundaciobit.genapp.common.ws.WsI18NException;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Fundació BIT.
 *
 * @author anadal
 */
@WebService
public interface RegWebInfoWs {


    /**
     * 
     * @param usuario
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    @WebMethod
    public List<TipoAsuntoWs> listarTipoAsunto(
         @WebParam(name = "usuario") String usuario,
         @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
      throws Throwable, WsI18NException;
    

    @WebMethod
    public List<CodigoAsuntoWs> listarCodigoAsunto(
         @WebParam(name = "usuario") String usuario,
         @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto)
      throws Throwable, WsI18NException;
    
    @WebMethod
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    public List<LibroWs> listarLibros(
        @WebParam(name = "usuario") String usuario,
        @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
        @WebParam(name = "autorizacion") String autorizacion)
    throws Throwable, WsI18NException;
    
    @WebMethod
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    public List<OrganismoWs> listarOrganismos(        
        @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
        throws Throwable, WsI18NException;


    @WebMethod
    public String getVersion();


    @WebMethod
    public int getVersionWs();
}
