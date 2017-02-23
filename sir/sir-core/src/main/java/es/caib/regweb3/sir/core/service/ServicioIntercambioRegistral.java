package es.caib.regweb3.sir.core.service;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;

/**
 * Created by earrivi on 14/01/2016.
 */

public interface ServicioIntercambioRegistral {


    public AsientoRegistralSir enviarAsientoRegistralSir(Long idRegistroEntrada, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro);

    //public AsientoRegistralSir recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
