package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SchedulerEJB")
//@RolesAllowed({"RWE_ADMIN","RWE_USUARI"})
@RunAs("RWE_USUARI")
public class SchedulerBean implements SchedulerLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private SirEnvioLocal sirEnvioEjb;
    @EJB private EntidadLocal entidadEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private ArxiuLocal arxiuEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private NotificacionLocal notificacionEjb;
    @EJB private DistribucionLocal distribucionEjb;
    @EJB private SesionLocal sesionEjb;
    @EJB private ColaLocal colaEjb;
    @EJB private CustodiaLocal custodiaEjb;


    @Override
    public void purgarIntegraciones() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Integraciones";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                integracionEjb.purgarIntegraciones(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }

    @Override
    public void purgarAnexosSir() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar AnexosSir";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                //fijamos un numero máximo de elementos por iteración
                Integer numElementos = PropiedadGlobalUtil.getNumElementosPurgoAnexos(entidad.getId());
                int total = anexoSirEjb.purgarArchivos(entidad.getId(), numElementos);
                peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    /**
     * Para cada una de las entidades del sistema, purga los anexos candidatos a
     * purgar (anexos marcados como distribuidos hace x meses).
     *
     * @throws Exception
     */
    public void purgarAnexosDistribuidos() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Anexos distribuidos";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                // Obtenemos la propiedad global  "getMesesPurgoAnexos"
                Integer mesesPurgo = PropiedadGlobalUtil.getMesesPurgoAnexos(entidad.getId());
                Integer numElementos = PropiedadGlobalUtil.getNumElementosPurgoAnexos(entidad.getId());

                if (mesesPurgo != null && mesesPurgo != -1) { // si nos han indicado meses, borramos.

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion = new StringBuilder();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    //Purgamos los anexos de registros distribuidos un máximo de numElementos
                    int total = anexoEjb.purgarAnexosRegistrosDistribuidos(entidad.getId(), mesesPurgo, numElementos);
                    peticion.append("total anexos purgados: ").append(total).append(System.getProperty("line.separator"));

                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
                }
            }

        } catch (Exception | I18NException e) {
            log.error("Error purgando anexos distribuidos ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    /**
     * Método que purga los anexos de los registros que se han enviado via SIR y han sido confirmados en destino.
     *
     * @throws Exception
     */
    public void purgarAnexosRegistrosConfirmados() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Anexos de registros recibidos SIR Confirmados";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                //fijamos un numero máximo de elementos por iteración
                Integer numElementos = PropiedadGlobalUtil.getNumElementosPurgoAnexos(entidad.getId());
                int total = anexoEjb.purgarAnexosRegistrosAceptados(entidad.getId(), numElementos);
                peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }


        } catch (Exception | I18NException e) {
            log.error("Error purgando anexos enviados por sir y que han sido confirmados ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    @Override
    public void reintentarIntercambiosSinConfirmacion() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando intercambios sin ACK de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarIntercambiosSinConfirmacion(entidad);
        }
    }

    @Override
    public void reintentarIntercambiosConError() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando intercambios con ERROR de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarIntercambiosConError(entidad);
        }
    }

    @Override
    public void reiniciarContadoresEntidad() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reiniciar contadores de todas las entidades";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

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
     * Inicia la distribución de los registros en cola de cada entidad.
     *
     * @throws Exception
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribuirRegistrosEnCola() throws Exception {

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if (!PropiedadGlobalUtil.pararColaDistribucion(entidad.getId())) {

                    distribucionEjb.distribuirRegistrosEnCola(entidad.getId());

                }
            }

        } catch (Exception e) {
            log.error("Error distribuyendo registros de la Cola ...", e);
        }
    }

    /**
     * Inicia la custodia de Justificantes en cola de cada entidad.
     *
     * @throws Exception
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void custodiarJustificantesEnCola() throws Exception {

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if(!PropiedadGlobalUtil.pararColaCustodia(entidad.getId())){

                    custodiaEjb.custodiarJustificantesEnCola(entidad.getId());
                }
            }

        } catch (Exception e) {
            log.error("Error custodiando justificantes de la Cola ...", e);
        }

    }

    /**
     * Cierra los expedientes que están en DM del Arxiu Digital del GOIB
     */
    @Override
    public void cerrarExpedientes() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Cerrar expedientes que están en DM del Arxiu Digital";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                if (PropiedadGlobalUtil.getCerrarExpedientes(entidad.getId())) {

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
    public void generarComunicaciones() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Generar comunicaciones automáticas de registros pendientes";
        Entidad entidadActiva = null;

        for (Entidad entidad : entidades) {

            if (PropiedadGlobalUtil.getGenerarComunicaciones(entidad.getId())) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                try {
                    peticion.append("tipo: ").append("notificacionesRegistrosSirPendientes").append(System.getProperty("line.separator"));
                    notificacionEjb.notificacionesRegistrosSirPendientes(entidad.getId());
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

                } catch (Exception e) {
                    log.error("Error generando notificacionesRegistrosSirPendientes", e);
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
                }

                try {
                    peticion.append("tipo: ").append("notificacionesRechazadosDevueltos").append(System.getProperty("line.separator"));
                    notificacionEjb.notificacionesRechazadosDevueltos(entidad.getId());
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

                } catch (Exception e) {
                    log.error("Error generando notificacionesRechazadosDevueltos", e);
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
                }
            }
        }
    }

    @Override
    public void purgarSesionesWs() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar sesiones WS";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                sesionEjb.purgarSesiones(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }


    @Override
    public void enviarEmailErrorDistribucion() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Envio email de errores de la cola de distribución";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion = new StringBuilder();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                distribucionEjb.enviarEmailErrorDistribucion(entidad);

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }

    @Override
    public void purgarProcesadosColaDistribucion() throws Exception {


        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Elementos Procesados Cola";
        Entidad entidadActiva = null;

        try {

            for (Entidad entidad : entidades) {

                // Obtenemos la propiedad global  "getMesesPurgoProcesadosCola"
                Integer mesesPurgo = PropiedadGlobalUtil.getMesesPurgoProcesadosCola(entidad.getId());

                if (mesesPurgo != null && mesesPurgo != -1) { // si nos han indicado meses, borramos.

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion = new StringBuilder();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    // Obtenemos todos los elementos procesados con anterioridad a los meses purgo indicados
                    colaEjb.purgarElementosProcesados(entidad.getId(), mesesPurgo);

                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
                }
            }

        } catch (Exception e) {
            log.error("Error purgando elementos procesados cola ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }
}
