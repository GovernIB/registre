package es.caib.regweb3.persistence.ejb;


import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static es.caib.regweb3.utils.Configuracio.getArchivosPath;

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

    @Autowired IEntradaService entradaService;
    @Autowired IConsultaService consultaService;
    @Autowired ISalidaService salidaService;

    @Autowired IAnexoService anexoService;
    @Autowired IEstadoAsientoService estadoAsientoervice;

    @Override
    public void recibirAsiento(String registro, String firmaRegistro) throws InterException {
        log.info("----------------------------------------- LIBSIR: RECIBIR ASIENTO -----------------------------------------");
        entradaService.recibirAsiento(registro, firmaRegistro);

    }

    @Override
    public void recibirMensajeControl(String mensaje, String firma) throws InterException {
        log.info("----------------------------------------- LIBSIR: RECIBIR MENSAJE CONTROL -----------------------------------------");
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
    public List<AsientoBean> consultaAsientosPendientesEstado(int maxResults, String estado) throws InterException {
        List<String> estados = new ArrayList<>();
        estados.add(estado);

        List<AsientoBean> pendientes = consultaService.consultarAsientosPendientesPorEstado(maxResults, estados);
        log.info("XXXXXXX PENDIENTES POR ESTADO: " + estado +" -  " + pendientes.size());
        return pendientes;
    }

    @Override
    public AsientoBean consultaAsiento(String oficina, String cdIntercambio) throws InterException {

        AsientoBean asientoBean = consultaService.consultarAsiento(oficina, cdIntercambio);
        return asientoBean;
    }

    @Override
    public byte[] obtenerAnexoReferencia(String cdIntercambio, String idFichero) throws InterException {
        byte[] data = null;

        /** Eliminar cuando funcione */
        AnexoBean anexoBean= new AnexoBean();

        File enidoc = new File(getArchivosPath(), "enidocmadrid.xml");

        byte[] enidocByte = null;
        try{
            enidocByte = Files.readAllBytes(enidoc.toPath());
        }catch (IOException io){
            io.printStackTrace();
        }
     /** fin Eliminar cuando funcione */

       // byte[] enidoc =  consultaService.getDocEniDescargadoIdFicheroYCdIntercambio(cdIntercambio, idFichero);

        //byte[] eni =  anexoService.getEniFromCodIntercambioIdFichero(cdIntercambio, idFichero);
        try{

            anexoBean = anexoService.generaAnexoBeanFromDocEni(enidocByte);
           // anexoBean = consultaService.descargarAnexoDeRepositorio(1041L);

        }catch (Exception e){
            e.printStackTrace();
            log.info("Se ha producido un error al descargar el anexoBean ");
        }
        return anexoBean.getContenidoBean().getContenido();


        /*LinkedHashMap<String, byte[]> anexos = new LinkedHashMap<>(anexoService.getEnisFromCodigoIntercambio(cdIntercambio));
        if(anexos.size()>1){
            data = anexos.entrySet().stream().findFirst().get().getValue();
        }else{
            data = anexos.entrySet().stream().findFirst().get().getValue();
        }
        log.info("XXXXXXXXXXXXXXXXXXXXX data " + data.length);

        return data;*/
    }


    @Override
    public String enviarAsiento(AsientoBean asientoBean) throws InterException{

        salidaService.enviar(asientoBean);
        log.info("Enviado AsientoBean" + asientoBean.getNuRgOrigen() + " - " + asientoBean.getCdIntercambio());
        return asientoBean.getCdIntercambio();

    }


    @Override
    public void reencolarAsiento(String oficina, String cdIntercambio) throws InterException{
        List<String> cdsIntercambios = new ArrayList<>();
        cdsIntercambios.add(cdIntercambio);
        salidaService.reencolar(oficina, cdsIntercambios);
    }


    @Override
    public void marcarErrorTecnicoAsiento(String oficina, String cdIntercambio) throws InterException{
        estadoAsientoervice.marcarAsientoErrorTecnico(oficina, cdIntercambio);
    }

    @Override
    public void desmarcarErrorTecnicoAsiento(String oficina, String cdIntercambio) throws InterException{
        estadoAsientoervice.desmarcarAsientoErrorTecnico(oficina, cdIntercambio);
    }

}
