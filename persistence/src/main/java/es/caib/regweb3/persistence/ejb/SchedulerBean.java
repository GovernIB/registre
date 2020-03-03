package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

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
@SecurityDomain("seycon")
@RunAs("RWE_USUARI")
public class SchedulerBean implements SchedulerLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private SirEnvioLocal sirEnvioEjb;
    @EJB private EntidadLocal entidadEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private ArxiuLocal arxiuEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private NotificacionLocal notificacionEjb;
    @EJB private DistribucionLocal distribucionEjb;
    @EJB private SesionLocal sesionEjb;


    @Override
    public void purgarIntegraciones() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Integraciones";
        Entidad entidadActiva = null;

        try{

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                integracionEjb.purgarIntegraciones(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }

    @Override
    public void purgarAnexosSir() throws Exception{

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
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                anexoSirEjb.purgarArchivos(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    /**
     * Para cada una de las entidades del sistema, purga los anexos candidatos a
     * purgar (anexos marcados como distribuidos hace x meses).
     * @throws Exception
     */
    public void purgarAnexosDistribuidos() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Purgar Anexos distribuidos";
        Entidad entidadActiva = null;

        try {

            for(Entidad entidad: entidades) {

                // Obtenemos la propiedad global  "getMesesPurgoAnexos"
                Integer mesesPurgo = PropiedadGlobalUtil.getMesesPurgoAnexos( entidad.getId());

                if( mesesPurgo != null && mesesPurgo != -1) { // si nos han indicado meses, borramos.

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
                    peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                    // Obtenemos los custodiaID de todos los anexos que se han distribuido los meses indicados
                    List<String> custodyIds = anexoEjb.obtenerCustodyIdAnexosDistribuidos(mesesPurgo);
                    for (String custodyId : custodyIds) {
                        //Purgamos anexo a anexo
                        anexoEjb.purgarAnexo(custodyId, false, entidad.getId());
                    }

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
     * @throws Exception
     */
    public void purgarAnexosRegistrosConfirmados() throws Exception{

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
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                anexoEjb.purgarAnexosRegistrosAceptados(entidad.getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }


        } catch (Exception | I18NException e) {
            log.error("Error purgando anexos enviados por sir y que han sido confirmados ...", e);
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }
    }

    @Override
    public void reintentarEnviosSinConfirmacion() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando envios sin ack de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarEnviosSinConfirmacion(entidad);
        }
    }

    @Override
    public void reintentarEnviosConError() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {

            sirEnvioEjb.reintentarEnviosConError(entidad);
        }
    }

    @Override
    public void reiniciarContadoresEntidad() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reiniciar contadores de todos los libros";
        Entidad entidadActiva = null;

        try{

            for(Entidad entidad: entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                //Reiniciar contadores de todos los libros
                libroEjb.reiniciarContadoresEntidadTask(entidad.getId());
                //Reiniciar contador SIR
                contadorEjb.reiniciarContador(entidad.getContadorSir().getId());

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }


    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     * @throws Exception
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribuirRegistrosEnCola() throws Exception{

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for (Entidad entidad : entidades) {

                if(!PropiedadGlobalUtil.pararDistribucion(entidad.getId())) {

                    distribucionEjb.procesarRegistrosEnCola(entidad.getId());

                }
            }

        }catch (Exception e){
            log.error("Error distribuyendo registros de la Cola ...", e);
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

            for(Entidad entidad: entidades) {

                if(PropiedadGlobalUtil.getCerrarExpedientes(entidad.getId())){

                    //Integración
                    entidadActiva = entidad;
                    Date inicio = new Date();
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
    public void generarComunicaciones() throws Exception{

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
    public void purgarSesionesWs() throws Exception{

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
    public void enviarEmailErrorDistribucion() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Envio email de errores de la cola de distribución";
        Entidad entidadActiva = null;

        try{

            for (Entidad entidad : entidades) {

                //Integración
                entidadActiva = entidad;
                Date inicio = new Date();
                peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

                distribucionEjb.enviarEmailErrorDistribucion(entidad);

                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");
            }

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), "");
        }

    }
}
