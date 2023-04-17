package es.caib.regweb3.persistence.ejb;


import es.gob.ad.registros.sir.interModel.model.Anexo;
import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.IAnexoService;
import es.gob.ad.registros.sir.interService.service.IConsultaService;
import es.gob.ad.registros.sir.interService.service.IEntradaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    IConsultaService consultaService;

    @Autowired
    IAnexoService anexoService;

    @Override
    public void recibirAsiento(String registro, String firmaRegistro) throws InterException {
        log.info("----------------------------------------- LIBSIR: RECIBIR ASIENTO -----------------------------------------");
        entradaService.recibirAsiento(registro, firmaRegistro);

    }

    @Override
    public void recibirMensajeControl(String mensaje, String firma) throws InterException {
        entradaService.recibirMensajeControl(mensaje, firma);
    }

    @Override
    public List<AsientoBean> consultaAsientosPendientes(int maxResults) throws InterException {
        List<String> estados = new ArrayList<>();
        estados.add("R");
        List<AsientoBean> pendientes = consultaService.consultarAsientosPendientes(maxResults);
        log.info("XXXXXXX PENDIENTES" + pendientes.size());
        return pendientes;
    }

    @Override
    public AsientoBean consultaAsiento(String oficina, String cdIntercambio) throws InterException {

        AsientoBean asientoBean = consultaService.consultarAsiento(oficina, cdIntercambio);
        log.info("XXXXXXX ASIENTO" + asientoBean.getCdSia());
        return asientoBean;
    }

    @Override
    public byte[] obtenerAnexoReferencia(String cdIntercambio, String idFichero) throws InterException {

        return consultaService.getDocEniDescargadoIdFicheroYCdIntercambio(cdIntercambio, idFichero);
    }

   /* @Override
    public Anexo obtenerAnexoReferencia2(String cdIntercambio, String idFichero) throws InterException {

        return anexoService.getAnexoPorIdFicheroYCdIntercambio(cdIntercambio,idFichero);
    }


    @Override
    public  byte[] obtenerAnexoReferenciaContenido(Long cdAnexo) throws InterException {

        return anexoService.getContenido(cdAnexo);
    }*/

}
