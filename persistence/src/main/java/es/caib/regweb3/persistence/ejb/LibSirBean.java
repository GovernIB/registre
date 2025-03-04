package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.persistence.utils.LibSirUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.gob.ad.registros.sir.interModel.dao.enums.TipoEstadoEnum;
import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.bean.IntercambiosPendientesProcesar;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.*;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.xml.datatype.DatatypeConfigurationException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by DGMAD
 *
 * @author earrivi
 * Date: 21/10/22
 */

@Stateless(name = "LibSirEJB")
@RunAs("RWE_USUARI")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class LibSirBean implements LibSirLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    //Estados de los asientos de LIBSIR que se deben procesar
    protected final List<String> ESTADOS_A_PROCESAR = Stream.of(TipoEstadoEnum.EC.getCodigo(), TipoEstadoEnum.ERCH.getCodigo(), TipoEstadoEnum.EERR.getCodigo(), TipoEstadoEnum.RERR.getCodigo(), TipoEstadoEnum.REERR.getCodigo()).collect(Collectors.toList());

    @Autowired IEntradaService entradaService;
    @Autowired IConsultaService consultaService;
    @Autowired ISalidaService salidaService;
    @Autowired IAsientoService asientoService;
    @Autowired UtilService utilService;

    @Autowired IAnexoService anexoService;
    @Autowired IEstadoAsientoService estadoAsientoService;
    @Autowired LibSirUtils libSirUtils;


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
    public List<AsientoBean> consultaAsientosPendientesProcesar() throws InterException {

        //TODO REVISAR SI SE HACE CON ESTOS ESTADO o Con la función "consultarAsientosPendientes() que los trae todos).
        List<AsientoBean> pendientes = consultaService.consultarAsientosPendientesPorEstado(RegwebConstantes.MAX_ASIENTOS_SIR_PROCESAR, ESTADOS_A_PROCESAR);

        log.info("XXXXXXX PENDIENTES PROCESAR " + pendientes.size());
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
    public List<IntercambiosPendientesProcesar> consultarCambiosEstadoPendientesProcesar(int maxResults, List<String> oficinas) throws InterException{
        return consultaService.consultarCambiosEstadoPendientesProcesar(maxResults, oficinas);
    }

    @Override
    public List<AsientoBean> consultarAsientosPendientes(int maxResults) throws InterException{
        return consultaService.consultarAsientosPendientes(maxResults);
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

//        libSirUtils.datosAsientoBean(asientoBean);
//
//        if (StringUtil.isCadenaVacia(asientoBean.getCdIntercambio())) {
//            throw new InterException(CatErrorAppEnum.E0089);
//        } else if (asientoBean.isReferenciaUnica() == null) {
//            throw new InterException(CatErrorAppEnum.E0122);
//        } else if (asientoService.existeCodigoIntercambio(asientoBean.getCdIntercambio())) {
//            throw new InterException(CatErrorAppEnum.E0094);
//        } else if (asientoBean.getFeRgOrigen() == null) {
//            throw new InterException(CatErrorAppEnum.E0091);
//        } else if (asientoBean.getFeRgOrigen().after(utilService.getFechaSistemaMasUnDia())) {
//            throw new InterException(CatErrorAppEnum.E0082);
//        } else if (StringUtil.isCadenaVacia(asientoBean.getNuRgOrigen())) {
//            throw new InterException(CatErrorAppEnum.E0090);
//        } else if (StringUtil.isCadenaVacia(Objects.toString(asientoBean.getFeRgPresentacion(), (String)null))) {
//            throw new InterException(CatErrorAppEnum.E0159);
//        } else if (asientoBean.getFeRgPresentacion().after(utilService.getFechaSistemaMasUnDia())) {
//            throw new InterException(CatErrorAppEnum.E0140);
//        } /*else if (!this.existeJustificante(asientoBean.getAnexosBean())) {
//            throw new InterException(CatErrorAppEnum.E0093);
//        }*/



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
    public void reenviarRegistro(IRegistro registro, Long tipoRegistro, OficinaTF oficinaSirDestino) throws InterException, I18NException, ParseException, DatatypeConfigurationException {
        AsientoBean asientoBean = null;

        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            RegistroEntrada registroEntrada = (RegistroEntrada)registro;
             asientoBean = libSirUtils.transformarRegistroEntrada(registroEntrada, oficinaSirDestino);
        }else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)) {
            RegistroSalida registroSalida = (RegistroSalida)registro;
            asientoBean = libSirUtils.transformarRegistroSalida(registroSalida, oficinaSirDestino);
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
        estadoAsientoService.marcarAsientoErrorTecnico(oficina, cdIntercambio);
    }

    @Override
    public void desmarcarErrorTecnicoAsiento(String oficina, String cdIntercambio) throws InterException{
        estadoAsientoService.desmarcarAsientoErrorTecnico(oficina, cdIntercambio);
    }

}
