package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.MailUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private SesionLocal sesionEjb;
    @EJB private ColaLocal colaEjb;


    @Override
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void actualizarProximoEventoRegistrosEntrada() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {

            if(PropiedadGlobalUtil.getActualizarProximoEvento(entidad.getId())){
                log.info(" ");
                log.info("------------- Actualizando proximos eventos de los Registros de Entrada de " + entidad.getNombre() + " -------------");
                log.info(" ");
                registroEntradaEjb.actualizarRegistrosSinEvento(entidad);
                log.info(" ");
                log.info("------------- FIN Actualizando proximos eventos de los Registros de Entrada de " + entidad.getNombre() + " -------------");
            }

        }
    }

    @Override
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void actualizarProximoEventoRegistrosSalida() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {

            if(PropiedadGlobalUtil.getActualizarProximoEvento(entidad.getId())){
                log.info(" ");
                log.info("------------- Actualizando proximos eventos de los Registros de Salida de " + entidad.getNombre() + " -------------");
                log.info(" ");
                registroSalidaEjb.actualizarRegistrosSinEvento(entidad);
                log.info(" ");
                log.info("------------- FIN Actualizando proximos eventos de los Registros de Salida de " + entidad.getNombre() + " -------------");
            }

        }
    }

    @Override
    public void purgarIntegraciones() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- Purgando integraciones de " + entidad.getNombre() + " -------------");
            log.info(" ");
            integracionEjb.purgarIntegraciones(entidad.getId());
        }
    }

    @Override
    public void purgarAnexosSir() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- Purgando AnexosSir de " + entidad.getNombre() + " -------------");
            log.info(" ");
            anexoSirEjb.purgarArchivos(entidad.getId());
        }
    }

    @Override
    public void reintentarEnviosSinConfirmacion() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando envios sin ack de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarEnviosSinConfirmacion(entidad.getId());
        }
    }

    @Override
    public void reintentarEnviosConError() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentando envios con errores de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEnvioEjb.reintentarEnviosConError(entidad.getId());
        }
    }

    @Override
    public void reiniciarContadoresEntidad() throws Exception {
        try{

            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {
                log.info("Inicializando contadores de:" + entidad.getNombre());
                //Reiniciar contadores de todos los libros
                libroEjb.reiniciarContadoresEntidadTask(entidad.getId());
                //Reiniciar contador SIR
                contadorEjb.reiniciarContador(entidad.getContadorSir().getId());
            }

        } catch (Throwable e) {
            log.error("Error Inicializando contadores entidad ...", e);
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
                    long inicio =  System.currentTimeMillis();
                    log.info(" ");
                    log.info("------------- Inicio Distribucion registros en Cola " + entidad.getNombre() + " -------------");
                    log.info(" ");
                    distribucionEjb.procesarRegistrosEnCola(entidad.getId());
                    log.info(" ");
                    log.info("------------- Fin Distribucion registros en Cola " + entidad.getNombre() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio)+ " -------------");
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
    public void cerrarExpedientes(){

        try {
            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {

                if(PropiedadGlobalUtil.getCerrarExpedientes(entidad.getId())){
                    log.info(" ");
                    log.info("------------- Cerrando expedientes en DM de la entidad: " + entidad.getNombre() + " -------------");
                    log.info(" ");

                    arxiuEjb.cerrarExpedientesScheduler(entidad.getId(), PropiedadGlobalUtil.getFechaInicioCerrarExpedientes(entidad.getId()));

                    log.info(" ");
                    log.info("------------- FIN expedientes en DM de la entidad: " + entidad.getNombre() + " -------------");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Para cada una de las entidades del sistema, purga los anexos candidatos a
     * purgar (anexos marcados como distribuidos hace x meses).
     * @throws Exception
     */
    public void purgarAnexosDistribuidos() throws Exception{

        try {
            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {
                //Obtenemos los custodiaID de todos los anexos que se han distribuido los meses indicados por la propiedad global  "getMesesPurgoAnexos"
                Integer mesesPurgo = PropiedadGlobalUtil.getMesesPurgoAnexos( entidad.getId());
                if( mesesPurgo != null && mesesPurgo!= -1) { // si nos han indicado meses, borramos.
                    List<String> custodyIds = anexoEjb.obtenerCustodyIdAnexosDistribuidos(mesesPurgo);
                    for (String custodyId : custodyIds) {
                        //Purgamos anexo a anexo
                        anexoEjb.purgarAnexo(custodyId, false, entidad.getId());
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error purgando anexos distribuidos ...", e);
        } catch (I18NException ie){
            log.error("Error purgando anexos distribuidos ...", ie);
        }
    }

    /**
     * Método que purga los anexos de los registros que se han enviado via SIR y han sido confirmados en destino.
     * @throws Exception
     */
    public void purgarAnexosRegistrosConfirmados() throws Exception{

        try {

            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {
                anexoEjb.purgarAnexosRegistrosAceptados(entidad.getId());
            }


        } catch (Exception e) {
            log.error("Error purgando anexos enviados por sir y que han sido confirmados ...", e);
        } catch (I18NException ie){
            log.error("Error purgando anexos enviados por sir y que han sido confirmados...", ie);
        }
    }

    @Override
    public void generarComunicaciones() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {

            if(PropiedadGlobalUtil.getGenerarComunicaciones(entidad.getId())){
                log.info(" ");
                log.info("------------- Generando comunicaciones para " + entidad.getNombre() + " -------------");
                log.info(" ");

                try{
                    notificacionEjb.notificacionesRegistrosSirPendientes(entidad.getId());
                }catch (Exception e){
                    log.error("Error generando notificacionesRegistrosSirPendientes", e);
                }

                try{
                    notificacionEjb.notificacionesRechazadosDevueltos(entidad.getId());
                }catch (Exception e){
                    log.error("Error generando notificacionesRechazadosDevueltos", e);
                }
            }
        }
    }

    @Override
    public void purgarSesionesWs() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {

            log.info(" ");
            log.info("------------- Purgando sesiones para " + entidad.getNombre() + " -------------");
            log.info(" ");

            try{
                sesionEjb.purgarSesiones(entidad.getId());
            }catch (Exception e){
                log.info("Error purgando sesiones");
                e.printStackTrace();
            }

            log.info(" ");
            log.info("------------- Fin Purgando sesiones para " + entidad.getNombre() + " -------------");
        }
    }


    @Override
    public void enviarEmailErrorDistribucion() throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();

        for (Entidad entidad : entidades) {

            Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(entidad.getId());
            Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);

            //Obtenemos el numero de registros que han alcanzado el máximo de reintentos
            int numRegistrosMaxReintentos = colaEjb.findByTipoEntidadMaxReintentos(RegwebConstantes.COLA_DISTRIBUCION, entidad.getId(), maxReintentos).size();
            if (numRegistrosMaxReintentos > 0) {
                try {
                    //Obtenemos los usuarios a los que hay que enviarles el mail
                    List<Usuario> usuariosANotificar = new ArrayList<Usuario>();
                    usuariosANotificar.add(entidad.getPropietario());
                    Set<UsuarioEntidad> administradores = entidad.getAdministradores();
                    for (UsuarioEntidad usuarioEntidad : administradores) {
                        usuariosANotificar.add(usuarioEntidad.getUsuario());
                    }

                    //Montamos textos del mail
                    String asunto = I18NLogicUtils.tradueix(locale, "cola.mail.asunto");
                    String mensajeTexto = "";
                    //Montamos el mensaje del mail con el nombre de la Entidad
                    if (usuariosANotificar.size() > 0) {
                        //Montamos el mensaje del mail con el nombre de la Entidad
                        String[] args = {Integer.toString(numRegistrosMaxReintentos),entidad.getNombre()};
                        mensajeTexto = I18NLogicUtils.tradueix(locale, "cola.mail.cuerpo", args);
                    }

                    //Miramos que estén definidos el remitente y el nombre del remitente
                    if (PropiedadGlobalUtil.getRemitente(entidad.getId()) != null && PropiedadGlobalUtil.getRemitenteNombre(entidad.getId()) != null) {
                        InternetAddress addressFrom = new InternetAddress(PropiedadGlobalUtil.getRemitente(entidad.getId()), PropiedadGlobalUtil.getRemitenteNombre(entidad.getId()));
                        //Enviamos el mail a todos los usuarios
                        for (Usuario usuario : usuariosANotificar) {
                            String mailAdminEntidad = usuario.getEmail();
                            if (!mailAdminEntidad.isEmpty()) {
                                MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, mailAdminEntidad);
                            } else {
                                log.error("Existen problemas de distribución en los registros. Por favor avise al Administrador : " + usuario.getNombreCompleto() + " de la entidad: " + entidad.getNombre());
                            }
                        }
                    } else {
                        log.error("No está definida la propiedad global <es.caib.regweb3.mail.remitente> o la propiedad <es.caib.regweb3.mail.remitente.nombre>");
                    }
                } catch (Exception e) {
                    //Si se produce una excepción continuamos con el proceso.
                    log.error("Se ha producido un excepcion enviando mail");
                    e.printStackTrace();
                }
            }
        }
    }
}
