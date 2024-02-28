package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.persistence.utils.LibSirUtils;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.utils.RegwebConstantes;
import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.*;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.xml.datatype.DatatypeConfigurationException;
import java.text.ParseException;
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

    @EJB
    private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;
    @EJB private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @Autowired IEntradaService entradaService;
    @Autowired IConsultaService consultaService;
    @Autowired ISalidaService salidaService;

    @Autowired IAnexoService anexoService;
    @Autowired IEstadoAsientoService estadoAsientoervice;
    @Autowired LibSirUtils libSirUtils;


    private Sicres3XML sicres3XML = new Sicres3XML();
    private WebServicesMethodsLocal webServicesMethodsEjb;

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
    public byte[] contenidoAnexoBean(String oficina, String cdIntercambio, String IdFichero) throws InterException{
        AsientoBean asiento = consultaAsiento(oficina, cdIntercambio);
        for(AnexoBean anexoBean: asiento.getAnexosBean()){
            if(anexoBean.getIdentificadorFichero().equals(IdFichero)){
                return anexoBean.getContenidoBean().getContenido();
            }
        }
        return null;
    }


    @Override
    public String enviarAsiento(AsientoBean asientoBean) throws InterException{

        salidaService.enviar(asientoBean);
        log.info("Enviado AsientoBean" + asientoBean.getNuRgOrigen() + " - " + asientoBean.getCdIntercambio());
        return asientoBean.getCdIntercambio();

    }

    /**
     * Reenvia un registro que aún no se ha generado en LIBSIR
     * @param registro
     * @param tipoRegistro
     * @throws InterException
     * @throws I18NException
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    @Override
    public void reenviarRegistro(IRegistro registro, Long tipoRegistro) throws InterException, I18NException, ParseException, DatatypeConfigurationException {
        AsientoBean asientoBean = null;
        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            RegistroEntrada registroEntrada = (RegistroEntrada)registro;
             asientoBean = libSirUtils.transformarRegistroEntrada(registroEntrada);
        }else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)) {
            RegistroSalida registroSalida = (RegistroSalida)registro;
            asientoBean = libSirUtils.transformarRegistroSalida(registroSalida);
        }
        try {
            salidaService.enviar(asientoBean);
            log.info("Enviado AsientoBean" + asientoBean.getNuRgOrigen() + " - " + asientoBean.getCdIntercambio());
        }catch (InterException ie){
            throw ie;
        }

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
