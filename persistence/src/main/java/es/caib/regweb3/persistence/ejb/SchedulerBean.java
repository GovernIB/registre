package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.sir.TipoMensaje;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.utils.Assert;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.gob.ad.registros.sir.interModel.dao.enums.TipoEstadoEnum;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.bean.DatosRegistroProcesoBean;
import es.gob.ad.registros.sir.interService.bean.ResultadoRegistroProcesoBean;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.IConsultaService;
import es.gob.ad.registros.sir.interService.service.ISalidaService;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SchedulerEJB")
//@RolesAllowed({"RWE_ADMIN","RWE_USUARI"})
@RunAs("RWE_USUARI")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class SchedulerBean implements SchedulerLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private SirEnvioLocal sirEnvioEjb;
    @EJB private EntidadLocal entidadEjb;
    @EJB
    private ContadorLocal contadorEjb;
    @EJB
    private IntegracionLocal integracionEjb;
    @EJB
    private ArxiuLocal arxiuEjb;
    @EJB
    private AnexoSirLocal anexoSirEjb;
    @EJB
    private AnexoLocal anexoEjb;
    @EJB
    private NotificacionLocal notificacionEjb;
    @EJB
    private DistribucionLocal distribucionEjb;
    @EJB
    private SesionLocal sesionEjb;
    @EJB
    private ColaLocal colaEjb;
    @EJB
    private CustodiaLocal custodiaEjb;
    @EJB
    private LibSirLocal libSirEjb;
    @EJB
    private RegistroSirLocal registroSirEjb;
    @EJB
    private OficinaLocal oficinaEjb;
    @EJB
    private OficioRemisionLocal oficioRemisionEjb;
    @EJB
    private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB
    private RegistroEntradaLocal registroEntradaEjb;
    @EJB
    private RegistroSalidaLocal registroSalidaEjb;
    @EJB
    private MensajeControlLocal mensajeControlEjb;

    @Autowired IConsultaService consultaService;
    @Autowired ISalidaService salidaService;


    @Override
    public void purgarIntegraciones() throws I18NException {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Integraciones";
        Entidad entidadActiva = null;
        log.info("------------- Purgando INTEGRACIONES -------------");
        try{

            for(Entidad entidad: entidades) {
                log.info("Purgando INTEGRACIONES de " + entidad.getNombre());
                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                integracionEjb.purgarIntegraciones(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }

    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void purgarAnexosSir() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar AnexosSir";
        Entidad entidadActiva = null;

        try{

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                int total = anexoSirEjb.purgarAnexosAceptados(entidad.getId());
                peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    /**
     * Para cada una de las entidades del sistema, purga los anexos candidatos a
     * purgar (anexos marcados como distribuidos hace x meses).
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void purgarAnexosDistribuidos() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Anexos distribuidos";
        Entidad entidadActiva = null;

        try {

            for(Entidad entidad: entidades) {

                if(PropiedadGlobalUtil.getPurgarAnexosDistribuidos(entidad.getId())){

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion = new StringBuilder();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    //Purgamos los anexos de registros distribuidos un máximo de numElementos
                    int total = anexoEjb.purgarAnexosRegistrosDistribuidos(entidad.getId());
                    peticion.append("total anexos purgados: ").append(total).append(System.getProperty("line.separator"));

                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
                }

            }

        } catch (I18NException e) {
            log.error("Error purgando anexos distribuidos ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    /**
     * Método que purga los anexos de los registros que se han enviado via SIR y han sido confirmados en destino.
     * @throws I18NException
     */
    public void purgarAnexosRegistrosConfirmados() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Anexos de registros recibidos SIR Confirmados";
        Entidad entidadActiva = null;

        try {

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                int total = anexoEjb.purgarAnexosRegistrosAceptados(entidad.getId());
                peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }


        } catch (I18NException e) {
            log.error("Error purgando anexos enviados por sir y que han sido confirmados ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    @Override
    public void reintentarIntercambiosSinAck() throws I18NException {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando intercambios sin ACK de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarIntercambiosSinAck(entidad);
        }
    }

    @Override
    public void reintentarReenviosRechazosSinAck() throws I18NException {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando reenvios/rechazos sin ACK de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarReenviosRechazosSinAck(entidad);
        }
    }

    @Override
    public void reintentarIntercambiosConError() throws I18NException {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando intercambios con ERROR de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarIntercambiosConError(entidad);
        }
    }

    @Override
    public void reintentarReenviosRechazosConError() throws I18NException {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando reenvios/rechazos con ERROR de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarReenviosRechazosConError(entidad);
        }
    }

    @Override
    public void reiniciarContadoresEntidad() throws I18NException {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reiniciar contadores de todas las entidades";
        Entidad entidadActiva = null;

        try{

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                //Reinicia los contadores del Libro único de la entidad
                contadorEjb.reiniciarContadoresLibro(entidad.getLibro());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }

    /**
     * Envía a la cola de distribución los registros que cumplen con los requisitos
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribucionAutomatica() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Distribución automática de registros";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                if(PropiedadGlobalUtil.distribucionAutomatica(entidad.getId())) {

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion = new StringBuilder();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    // Distribuimos registros automáticamente
                    int total = distribucionEjb.distribuirAutomaticamente(entidad);
                    peticion.append("total registros distribuidos: ").append(total).append(System.getProperty("line.separator"));

                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

                }
            }

        }catch (Exception e){

            log.error("Error distribuyendo automaticamente registros ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");

        }
    }


    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribuirRegistrosEnCola() throws I18NException{

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if(!PropiedadGlobalUtil.pararColaDistribucion(entidad.getId())) {

                    distribucionEjb.distribuirRegistrosEnCola(entidad);

                }
            }

        }catch (Exception e){
            log.error("Error distribuyendo registros de la Cola ...", e);
        }
    }

    /**
     * Inicia la custodia de Justificantes en cola de cada entidad.
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void custodiarJustificantesEnCola() throws I18NException{

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if(!PropiedadGlobalUtil.pararColaCustodia(entidad.getId())){

                    // Obtiene un numero de elementos pendientes de distribuir que estan en la cola
                    List<Cola> elementos = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_CUSTODIA, entidad.getId(), null, PropiedadGlobalUtil.getElementosColaCustodia(entidad.getId()));

                    custodiaEjb.custodiarJustificantesEnCola(entidad.getId(), elementos);
                }
            }

        }catch (Exception e){
            log.error("Error custodiando justificantes de la Cola ...", e);
        }

    }

    /**
     * Segundo hilo de custodia de Justificantes en cola de cada entidad.
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void custodiarJustificantesEnCola2() throws I18NException{

        try {

            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if(!PropiedadGlobalUtil.pararColaCustodia(entidad.getId()) && PropiedadGlobalUtil.segundoHiloCustodia(entidad.getId())){

                    // Obtiene un numero de elementos pendientes de distribuir que estan en la cola
                    Integer totalElementos = PropiedadGlobalUtil.getElementosColaCustodia(entidad.getId());
                    List<Cola> elementos = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_CUSTODIA, entidad.getId(), totalElementos, totalElementos);

                    custodiaEjb.custodiarJustificantesEnCola(entidad.getId(), elementos);
                }
            }

        }catch (Exception e){
            log.error("Error custodiando justificantes de la Cola ...", e);
        }

    }

    /**
     * Cierra los expedientes que están en DM del Arxiu Digital del GOIB
     */
    @Override
    public void cerrarExpedientes() throws I18NException {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Cerrar expedientes que están en DM del Arxiu Digital";
        Entidad entidadActiva = null;

        try {

            for(Entidad entidad: entidades) {

                if(PropiedadGlobalUtil.getCerrarExpedientes(entidad.getId())){

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion = new StringBuilder();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    arxiuEjb.cerrarExpedientesScheduler(entidad.getId(), PropiedadGlobalUtil.getFechaInicioCerrarExpedientes(entidad.getId()));

                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
                }
            }

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
            e.printStackTrace();
        }
    }


    @Override
    public void generarComunicaciones() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Generar comunicaciones automáticas de registros pendientes";
        Entidad entidadActiva = null;

        for(Entidad entidad: entidades) {

            if(PropiedadGlobalUtil.getGenerarComunicaciones(entidad.getId())){

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                try{
                    peticion.append("tipo: ").append("notificacionesRegistrosSirPendientes").append(System.getProperty("line.separator"));
                    notificacionEjb.notificacionesRegistrosSirPendientes(entidad.getId());
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

                }catch (Exception e){
                    log.error("Error generando notificacionesRegistrosSirPendientes", e);
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
                }

                try{
                    peticion.append("tipo: ").append("notificacionesRechazadosDevueltos").append(System.getProperty("line.separator"));
                    notificacionEjb.notificacionesRechazadosDevueltos(entidad.getId());
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

                }catch (Exception e){
                    log.error("Error generando notificacionesRechazadosDevueltos", e);
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
                }
            }
        }
    }

    @Override
    public void purgarSesionesWs() throws I18NException{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar sesiones WS";
        Entidad entidadActiva = null;

        try{

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                sesionEjb.purgarSesiones(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    @Override
    public void purgarProcesadosColas() throws I18NException {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Elementos Procesados Cola";
        Entidad entidadActiva = null;

        try {

            for(Entidad entidad: entidades) {
                //Integración
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));
                entidadActiva = entidad;

                // Purgamos los elementos
                Integer purgados = colaEjb.purgarElementosProcesados(entidad.getId());

                peticion.append("purgados: ").append(purgados).append(System.getProperty("line.separator"));

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            log.error("Error purgando elementos procesados cola ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }


    @Override
    public void consultarAsientosPendientesSIR() throws I18NException {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        Date inicio = null;
        String descripcion = "";
        Entidad entidadActiva = null;
        try {
            List<AsientoBean> asientosPendientes = libSirEjb.consultaAsientosPendientes(10);

            //ENVIADOS
            List<DatosRegistroProcesoBean> aProcesar= new ArrayList<>();

            for(AsientoBean asiento: asientosPendientes){

                log.info("Asiento CDINTERCAMBIO" + asiento.getCdIntercambio());
                log.info("Asiento Estado" + asiento.getCdEstado());
                log.info("Asiento Destino" + asiento.getCdEnRgDestino());


                if(TipoEstadoEnum.R.getCodigo().equals(asiento.getCdEstado())){ //Recibido
                    inicio = new Date();
                    descripcion = "Recepción Intercambio: ";
                    RegistroSir registroSir = registroSirEjb.getByIdIntercambio(asiento.getCdIntercambio());
                    if(registroSir == null){
                        log.info("CASO REGISTRO RECIBIDO NUEVO");
                        Oficina oficina = oficinaEjb.findByMultiEntidad(asiento.getCdEnRgDestino());
                        Entidad entidad = oficina.getOrganismoResponsable().getEntidad();
                        Assert.notNull(entidad,"No existe ninguna Entidad a la que corresponda este envío");
                        Assert.isTrue(entidad.getActivo(), "La Entidad a la que va dirigida el Asiento Registral no está activa");
                        Assert.isTrue(entidad.getSir(), "La Entidad a la que va dirigida el Asiento Registral no tiene el servicio SIR activo");
                        registroSir= registroSirEjb.crearRegistroSir(asiento, entidad);

                        descripcion = descripcion.concat(asiento.getDsTpAnotacion());
                        datosIntegracion(asiento, peticion);
                        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidad.getId(), asiento.getCdIntercambio());
                        log.info("El registroSir no existia en el sistema y se ha creado: " + registroSir.getIdentificadorIntercambio());
                    }else{
                       if (EstadoRegistroSir.ACEPTADO.equals(registroSir.getEstado())) {
                           log.info("Se ha recibido un ENVIO que ya ha sido aceptado previamente: " + registroSir.getIdentificadorIntercambio() + "lo marcamos como procesado");
                           //Volvemos a recibir un asiento que ya hemos aceptado, marcamos como procesado
                           DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                           datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                           datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                           datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.R.getCodigo());
                           aProcesar.add(datosRegistroProcesoBean);
                       }else if(EstadoRegistroSir.RECIBIDO.equals(registroSir.getEstado())){
                           log.info("El RegistroSir " + registroSir.getIdentificadorIntercambio() + " ya se habia recibido anteriormente y está en estado pendiente de procesar");
                       }else{
                           log.info("Se ha recibido un ENVIO con estado incompatible: " + registroSir.getIdentificadorIntercambio());
                       }
                    }
                }
                if(TipoEstadoEnum.EC.getCodigo().equals(asiento.getCdEstado())){ //Enviado y confirmado
                    log.info("ENVIADO " + asiento.getCdIntercambio());
                    log.info("ENVIADOS ESTADO" + asiento.getCdEstado());
                    OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(asiento.getCdIntercambio());
                    if(oficioRemision!=null) { log.info("Oficio SIR ID" + oficioRemision.getId());}
                    if(oficioRemision!=null) {

                        switch (oficioRemision.getEstado()) {

                            case RegwebConstantes.OFICIO_SIR_ENVIADO:
                           /* case RegwebConstantes.OFICIO_SIR_ENVIADO_ACK:
                            case RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR:*/
                            case RegwebConstantes.OFICIO_SIR_REENVIADO:
                           /* case RegwebConstantes.OFICIO_SIR_REENVIADO_ACK:
                            case RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR:*/

                                oficioRemisionEjb.aceptarOficioSir(oficioRemision, asiento.getCdEnRgDestino(), asiento.getDsEnRgDestino(), asiento.getCdEnRgOrigen(), asiento.getFeEntradaDestino());
                                break;

                            case (RegwebConstantes.OFICIO_ACEPTADO):
                                log.info("Se ha recibido un mensaje de confirmación duplicado");
                                break;

                            default:
                                log.info("El RegistroSir no tiene el estado necesario para ser Confirmado: " + oficioRemision.getIdentificadorIntercambio());
                                throw new ValidacionException(Errores.ERROR_0037, "El RegistroSir no tiene el estado necesario para ser Confirmado: " + oficioRemision.getIdentificadorIntercambio());
                        }


                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.EC.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);

                    }
                }
                if(TipoEstadoEnum.ERCH.getCodigo().equals(asiento.getCdEstado())) { //Rechazo de un asiento enviado
                    //Integraciones revisadas OK
                    OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(asiento.getCdIntercambio());

                    // Oficio Remision: Ha sido enviado por nosotros a SIR
                    if (oficioRemision != null) { // Existe en el sistema

                        oficioRemisionEjb.  marcarRechazadoOficioSir(oficioRemision,asiento.getCdEnRgOrigen(),TipoMensaje.RECHAZO.getValue(),TipoMensaje.RECHAZO.getName(),asiento.getCdIntercambio());

                        //Procesamos
                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.ERCH.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);

                    }

                }

                //PROBADO y OK.
                if(TipoEstadoEnum.RRE.getCodigo().equals(asiento.getCdEstado())) { //– Recibido Reenviado: el reenvío de un registro se ha enviado por la plataforma SIR y se
                    //ha recibido su correspondiente ACK (el registro reenviado ha llegado al destino).

                    //Integraciones revisadas OK
                    RegistroSir registroSir = registroSirEjb.getByIdIntercambio(asiento.getCdIntercambio());
                    OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(asiento.getCdIntercambio());

                    if(registroSir!=null){
                            registroSirEjb.modificarEstadoNuevaTransaccion(registroSir.getId(), EstadoRegistroSir.REENVIADO_Y_ACK);

                            //PROCESAMOS
                            DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                            datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                            datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                            datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.RRE.getCodigo());
                            aProcesar.add(datosRegistroProcesoBean);


                    // Oficio Remision: Ha sido enviado por nosotros a SIR//TODO no tengo claro si este caso se tiene que contemplar con LIBSIR
                    }/* else if (oficioRemision != null) { // Ya existe en el sistema

                            log.info("XXXXXXXX Entro en oficioSir reenviado ");
                            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_REENVIADO_ACK);

                            DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                            datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                            datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                            datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.RRE.getCodigo());
                            aProcesar.add(datosRegistroProcesoBean);

                    }*/

                }

                if(TipoEstadoEnum.EERR.getCodigo().equals(asiento.getCdEstado())){//Enviado Erróneo: se ha producido un error en el envío del registro.

                    descripcion = "Recepción Error en Envio: " + TipoEstadoEnum.EERR.getCodigo();
                    peticion.append("IdentificadorIntercambio: ").append(asiento.getCdIntercambio()).append(System.getProperty("line.separator"));
                    peticion.append("Origen: ").append(asiento.getCdEnRgOrigen()).append(System.getProperty("line.separator"));
                    peticion.append("Destino: ").append(asiento.getCdEnRgDestino()).append(System.getProperty("line.separator"));
                    peticion.append("Descripcion: ").append(TipoEstadoEnum.EERR.getDescripcion()).append(System.getProperty("line.separator"));


                    OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(asiento.getCdIntercambio());


                   // if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_ACEPTADO){
                        //PROCESAMOS
                    log.info("SSSSS " + asiento.getCdTpAnotacion());
                    log.info("SSSSS " + asiento.getDsTpAnotacion());
                        oficioRemisionEjb.modificarEstadoError(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR, TipoMensaje.ERROR.getName(), TipoMensaje.ERROR.getValue());

                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.EERR.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);
                   // }

                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), null, null, System.currentTimeMillis() - inicio.getTime(), oficioRemision.getEntidad().getId(), asiento.getCdIntercambio());
                }

//TODO PENDENT DE PROVAR TOTS AQUESTS CASOS QUAN INTERDOC EM RETORNI UN ENIDOC
                if(TipoEstadoEnum.RERR.getCodigo().equals(asiento.getCdEstado())){ // Recibido Confirmado Erróneo: se ha producido un error en la confirmación del registro
                    RegistroSir registroSir = registroSirEjb.getByIdIntercambio(asiento.getCdIntercambio());
                    if(EstadoRegistroSir.ACEPTADO.equals(registroSir.getEstado())){
                        //PROCESAMOS
                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.RERR.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);
                    }else{
                        log.info("Se ha producido un error en la confirmación del asiento (" + asiento.getCdIntercambio()+ ") - (" + TipoEstadoEnum.RERR.getDescripcion()+ ")");
                    }
                }

               /* if(TipoEstadoEnum.RCHERR.getCodigo().equals(asiento.getCdEstado())){// Rechazado Erróneo: se ha producido un error en el rechazo de un registro.
                    RegistroSir registroSir = registroSirEjb.getByIdIntercambio(asiento.getCdIntercambio());
                    if(EstadoRegistroSir.RECHAZADO_Y_ACK.equals(registroSir.getEstado())){
                        //PROCESAMOS
                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.RCHERR.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);
                    }else{
                        log.info("Se ha producido un error en el rechazo del asiento (" + asiento.getCdIntercambio()+ ") - (" + TipoEstadoEnum.RCHERR.getDescripcion()+ ")");
                    }
                }

                if(TipoEstadoEnum.REERR.getCodigo().equals(asiento.getCdEstado())){// Reenviado Erróneo: se ha producido un error en el reenvío del registro.
                    RegistroSir registroSir = registroSirEjb.getByIdIntercambio(asiento.getCdIntercambio());
                    if(EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado())){
                        //PROCESAMOS
                        DatosRegistroProcesoBean datosRegistroProcesoBean = new DatosRegistroProcesoBean();
                        datosRegistroProcesoBean.setCdIntercambio(asiento.getCdIntercambio());
                        datosRegistroProcesoBean.setCdEnRgProcesa(asiento.getCdEnRgProcesa());
                        datosRegistroProcesoBean.setEstadoAplicacion(TipoEstadoEnum.REERR.getCodigo());
                        aProcesar.add(datosRegistroProcesoBean);
                    }else{
                        log.info("Se ha producido un error en el reenvio del asiento (" + asiento.getCdIntercambio()+ ") - (" + TipoEstadoEnum.REERR.getDescripcion()+ ")");
                    }
                }*/
            }


            //Marcamos como procesados los enviados
           ResultadoRegistroProcesoBean resultado = consultaService.procesar(aProcesar);
            if (resultado != null) {
                if(resultado.getRegistrosProcesados()!=null){
                    resultado.getRegistrosProcesados().forEach((v) -> log.error(("Intercambio procesado :" + v)));
                }
                if(resultado.getRegistrosErrorProceso()!=null){
                    resultado.getRegistrosErrorProceso().forEach((k, v) -> log.error(("Error producido : " + k + " - " + v)));
                    // Els errors que retorna son (he mirat el codi del mètode)
                    //0075=[{0}] no puede ser procesado porque no se encuentra en estado {1}.
                    //0076=[{0}] no tiene ningún estado pendiente de procesar para la oficina {1}.

                }
            }
        } catch (Exception e) {
            log.error("Error obteniendo asientos SIR pendientes ...", e);
        }
    }


    @Override
    public void reencolarAsientos() throws I18NException {

        //Estados candidatos a ser reencolados
        List<String> estados = new ArrayList<>();
        estados.add(TipoEstadoEnum.PE.getCodigo());
        estados.add(TipoEstadoEnum.PRAE.getCodigo());
        estados.add(TipoEstadoEnum.PERCH.getCodigo());
        estados.add(TipoEstadoEnum.PRAERCH.getCodigo());
        estados.add(TipoEstadoEnum.PRE.getCodigo());
        estados.add(TipoEstadoEnum.PRARE.getCodigo());

        try{
            //Obtenemos aquellos asientos que se encuentran en alguno de los estados anteriores
            List<AsientoBean> asientos = consultaService.consultarAsientosPendientesPorEstado(20, estados);

            //Los agrupamos por oficina que los procesa
            Map<String, List<String>> asientosPorCdEnProcesa = asientos.stream()
                    .collect(groupingBy(AsientoBean::getCdEnRgProcesa, Collectors.mapping(AsientoBean::getCdIntercambio, toList())));

            //LLamamos al método de libsir para que sean reencolados.
            asientosPorCdEnProcesa.forEach((k, v) -> {
                try {
                    salidaService.reencolar(k, v);
                } catch (InterException ie) {
                    ie.printStackTrace();
                }
            });

        }catch ( InterException ie){
            throw new I18NException("Error consultando los asientos para ser reencolados ");
        }

    }




    /**
     *
     * @param asientoBean
     * @param peticion
     */
    private void datosIntegracion(AsientoBean asientoBean, StringBuilder peticion){
        peticion.append("TipoAnotación: ").append(TipoAnotacion.getTipoAnotacion(asientoBean.getCdTpAnotacion()).getName()).append(System.getProperty("line.separator"));
        peticion.append("IdentificadorIntercambio: ").append(asientoBean.getCdIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(asientoBean.getDsEnRgOrigen()).append(" (").append(asientoBean.getCdEnRgOrigen()).append(")").append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(asientoBean.getDsEnRgDestino()).append(" (").append(asientoBean.getCdEnRgDestino()).append(")").append(System.getProperty("line.separator"));

        if(StringUtils.isNotEmpty(asientoBean.getDsTpAnotacion())){
            peticion.append("Motivo: ").append(asientoBean.getDsTpAnotacion()).append(System.getProperty("line.separator"));
        }
    }
}
