package es.caib.regweb3.persistence.ejb;


import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.IEntradaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * Created by DGMAD
 *
 * @author earrivi
 * Date: 21/10/22
 */

@Stateless(name = "LibSirEJB")
@RunAs("RWE_USUARI")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class LibSirBean implements LibSirLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    IEntradaService entradaService;


    @Override
    public void recibirAsiento(String registro, String firmaRegistro) throws InterException{
        log.info("----------------------------------------- LIBSIR: RECIBIR ASIENTO -----------------------------------------");
        entradaService.recibirAsiento(registro, firmaRegistro);

    }

    @Override
    public void recibirMensajeControl(String mensaje, String firma) throws InterException {
        entradaService.recibirMensajeControl(mensaje, firma);
    }
}
