package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.ContactoTF;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SirEJB")
@SecurityDomain("seycon")
public class SirBean implements SirLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private JustificanteLocal justificanteEjb;
    @EJB private EmisionLocal emisionEjb;
    @EJB private MensajeLocal mensajeEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private AnexoLocal anexoEjb;

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    @Override
    public Boolean recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{

        Boolean ack = true; // Indica si enviaremos un ack o no
        RegistroSir registroSir = null;
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Recepción SIR: " + TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName();

        // Comprobamos que el destino pertenece a alguna de las Entidades configuradas
        //comprobarEntidad(ficheroIntercambio.getCodigoEntidadRegistralDestino());

        peticion.append("TipoAnotación: ").append(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName()).append(System.getProperty("line.separator"));
        peticion.append("IdentificadorIntercambio: ").append(ficheroIntercambio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));

        // ENVIO
        if (TipoAnotacion.ENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un ENVIO: " + ficheroIntercambio.getIdentificadorIntercambio());

            // Buscamos si el Registro recibido ya existe en el sistema
            registroSir = registroSirEjb.getRegistroSir(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

            if (registroSir != null) { // Ya existe en el sistema

                if (EstadoRegistroSir.RECIBIDO.equals(registroSir.getEstado())) {

                    log.info("El RegistroSir " + registroSir.getIdentificadorIntercambio() + " ya se habia recibido, enviamos otro ACK");

                } else if (EstadoRegistroSir.ACEPTADO.equals(registroSir.getEstado())) {

                    log.info("Se ha recibido un ENVIO que ya ha sido aceptado previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", enviamos un mensaje de Confirmacion");

                    // Enviamos el Mensaje de Confirmación
                    RegistroEntrada registroEntrada = trazabilidadEjb.getRegistroAceptado(registroSir.getId());
                    mensajeEjb.enviarMensajeConfirmacion(registroSir, registroEntrada.getNumeroRegistroFormateado());
                    ack = false;
                } else {
                    log.info("Se ha recibido un ENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);
                }

            } else {

                // Creamos un nuevo RegistroSir
                registroSir = registroSirEjb.crearRegistroSir(ficheroIntercambio);
                log.info("El registroSir no existia en el sistema y se ha creado: " + registroSir.getIdentificadorIntercambio());
            }

            // REENVIO
        }else if (TipoAnotacion.REENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un REENVIO: " + ficheroIntercambio.getDescripcionTipoAnotacion() +" - " + ficheroIntercambio.getIdentificadorIntercambio());

            // Buscamos si el Registro recibido ya existe en el sistema
            registroSir = registroSirEjb.getRegistroSir(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());
            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

            // Registro Sir: Lo hemos recibido de SIR
            if (registroSir != null) { // Ya existe en el sistema

                if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.RECHAZADO_Y_ACK.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.RECHAZADO_Y_ERROR.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado())) {

                    // Modificar el estado del Registro a RECIBIDO
                    registroSir.setEstado(EstadoRegistroSir.RECIBIDO);
                    registroSir.setFechaRecepcion(new Date());
                    registroSir.setFechaEstado(new Date());
                    registroSirEjb.merge(registroSir);

                    //Creamos la TrazabilidadSir
                    TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_REENVIO);
                    trazabilidadSir.setRegistroSir(registroSir);
                    trazabilidadSir.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                    trazabilidadSir.setDecodificacionEntidadRegistralOrigen(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                    trazabilidadSir.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralDestino());
                    trazabilidadSir.setDecodificacionEntidadRegistralDestino(ficheroIntercambio.getDescripcionEntidadRegistralDestino());
                    trazabilidadSir.setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                    trazabilidadSir.setNombreUsuario(ficheroIntercambio.getNombreUsuario());
                    trazabilidadSir.setContactoUsuario(ficheroIntercambio.getContactoUsuario());
                    trazabilidadSir.setObservaciones(ficheroIntercambio.getDescripcionTipoAnotacion());
                    trazabilidadSir.setFecha(new Date());
                    trazabilidadSirEjb.persist(trazabilidadSir);


                    log.info("El registroSir existia en el sistema, se ha vuelto a recibir: " + registroSir.getIdentificadorIntercambio());

                }else if (EstadoRegistroSir.RECIBIDO.equals(registroSir.getEstado())) {

                    log.info("Se ha recibido un REENVIO que ya habia sido recibido previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                }else if (EstadoRegistroSir.ACEPTADO.equals(registroSir.getEstado())) {

                    log.info("Se ha recibido un REENVIO que ya habia sido aceptado previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                } else{
                    log.info("Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);
                }

                // Oficio Remision: Ha sido enviado por nosotros a SIR
            }else if(oficioRemision != null){ // Ya existe en el sistema

                if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK){

                    if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){

                        RegistroEntrada registroEntrada = oficioRemision.getRegistrosEntrada().get(0);
                        // Actualizamos el registro de entrada
                        registroEntrada.setEstado(RegwebConstantes.REGISTRO_REENVIADO);
                        registroEntrada.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroEntrada.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroEntrada.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroEntrada.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroEntradaEjb.merge(registroEntrada);

                    }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

                        RegistroSalida registroSalida = oficioRemision.getRegistrosSalida().get(0);
                        // Actualizamos el registro de salida
                        registroSalida.setEstado(RegwebConstantes.REGISTRO_REENVIADO);
                        registroSalida.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroSalida.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroSalida.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroSalida.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroSalidaEjb.merge(registroSalida);
                    }

                    // Actualizamos el oficio
                    oficioRemision.setCodigoEntidadRegistralProcesado(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                    oficioRemision.setDecodificacionEntidadRegistralProcesado(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_DEVUELTO);
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemisionEjb.merge(oficioRemision);

                    log.info("El oficio de remision existia en el sistema, nos lo han renviado: " + oficioRemision.getIdentificadorIntercambio());

                }else if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_DEVUELTO){
                    log.info("Se ha recibido un REENVIO que ya habia sido recibido previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                } else{
                    log.info("Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);
                }

            }else{
                // Creamos un nuevo RegistroSir
                registroSir = registroSirEjb.crearRegistroSir(ficheroIntercambio);
                log.info("El registroSir no existia en el sistema y se ha creado: " + registroSir.getIdentificadorIntercambio());
            }

            // RECHAZO
        }else if (TipoAnotacion.RECHAZO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un RECHAZO");

            // Buscamos si el FicheroIntercambio ya existe en el sistema
            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

            // Oficio Remision: Ha sido enviado por nosotros a SIR
            if(oficioRemision != null) { // Existe en el sistema

                if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK){

                    if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){

                        RegistroEntrada registroEntrada = oficioRemision.getRegistrosEntrada().get(0);
                        // Actualizamos el registro de entrada
                        registroEntrada.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
                        registroEntrada.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroEntrada.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroEntrada.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroEntrada.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroEntradaEjb.merge(registroEntrada);

                    }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

                        RegistroSalida registroSalida = oficioRemision.getRegistrosSalida().get(0);
                        // Actualizamos el registro de salida
                        registroSalida.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
                        registroSalida.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroSalida.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroSalida.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroSalida.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroSalidaEjb.merge(registroSalida);
                    }

                    // Actualizamos el oficio
                    oficioRemision.setCodigoEntidadRegistralProcesado(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                    oficioRemision.setDecodificacionEntidadRegistralProcesado(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_RECHAZADO);
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemisionEjb.merge(oficioRemision);

                    log.info("El oficio de remision existia en el sistema, nos lo han rechazado: " + oficioRemision.getIdentificadorIntercambio());

                }else if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_RECHAZADO){

                    log.info("Se ha recibido un RECHAZO de un registroSir que ya esta devuelto" + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);

                }else{
                    log.info("Se ha recibido un RECHAZO cuyo estado no lo permite: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);
                }

            }else{
                // Caso extinguido de RECHAZO A ORIGEN
                registroSir = registroSirEjb.getRegistroSir(ficheroIntercambio.getIdentificadorIntercambio(),ficheroIntercambio.getCodigoEntidadRegistralDestino());

                if(registroSir != null){

                    log.info("Se trata de un Rechazo a Origen: " + ficheroIntercambio.getIdentificadorIntercambio());

                    if(EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado()) ||
                            EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado())){

                        // Modificar el estado del Registro a RECIBIDO
                        registroSir.setEstado(EstadoRegistroSir.RECIBIDO);
                        registroSir.setFechaEstado(new Date());
                        registroSirEjb.merge(registroSir);

                        //Creamos la TrazabilidadSir
                        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO_ORIGEN);
                        trazabilidadSir.setRegistroSir(registroSir);
                        trazabilidadSir.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                        trazabilidadSir.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralDestino());
                        trazabilidadSir.setDecodificacionEntidadRegistralDestino(ficheroIntercambio.getDescripcionEntidadRegistralDestino());
                        trazabilidadSir.setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        trazabilidadSir.setNombreUsuario(ficheroIntercambio.getNombreUsuario());
                        trazabilidadSir.setContactoUsuario(ficheroIntercambio.getContactoUsuario());
                        trazabilidadSir.setObservaciones(ficheroIntercambio.getDescripcionTipoAnotacion());
                        trazabilidadSir.setFecha(new Date());
                        trazabilidadSirEjb.persist(trazabilidadSir);
                    }
                }else{
                    log.info("El registro recibido no existe en el sistema: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037);
                }

            }
        }

        // Integracion
        if(registroSir != null){
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(" (").append(registroSir.getCodigoEntidadRegistralOrigen()).append(")").append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(" (").append(registroSir.getCodigoEntidadRegistralDestino()).append(")").append(System.getProperty("line.separator"));
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
        }

        return ack;
    }

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     * @param mensaje
     * @throws Exception
     */
    @Override
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception{

        // Comprobamos que el destino pertenece a alguna de las Entidades configuradas
        comprobarEntidad(mensaje.getCodigoEntidadRegistralDestino());

        // Mensaje ACK
        if(mensaje.getTipoMensaje().equals(TipoMensaje.ACK)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());
            RegistroSir registroSir = registroSirEjb.getRegistroSir(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeACK(oficioRemision);
            }else if(registroSir != null){
                procesarMensajeACK(registroSir);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037);
            }

            // Mensaje CONFIRMACIÓN
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.CONFIRMACION)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeCONFIRMACION(oficioRemision, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037);
            }


            // Mensaje ERROR
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.ERROR)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());
            RegistroSir registroSir = registroSirEjb.getRegistroSir(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeERROR(oficioRemision, mensaje);
            }else if(registroSir != null){
                procesarMensajeERROR(registroSir, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037);
            }

        }else{
            log.info("El tipo mensaje de control no es válido: " + mensaje.getTipoMensaje());
            throw new ValidacionException(Errores.ERROR_0037);
        }

    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeACK(OficioRemision oficioRemision) throws Exception{

        switch (oficioRemision.getEstado()) {

            case RegwebConstantes.OFICIO_SIR_ENVIADO:

                // Actualizamos el OficioRemision
                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ACK);
                oficioRemision.setFechaEstado(new Date());
                oficioRemision.setNumeroReintentos(0);
                oficioRemisionEjb.merge(oficioRemision);

                break;

            case RegwebConstantes.OFICIO_SIR_REENVIADO:

                // Actualizamos el OficioRemision
                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ACK);
                oficioRemision.setFechaEstado(new Date());
                oficioRemision.setNumeroReintentos(0);
                oficioRemisionEjb.merge(oficioRemision);

                break;

            case RegwebConstantes.OFICIO_SIR_ENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ACK:

                log.info("Se ha recibido un mensaje ACK duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());

                break;

            default:
                log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
                throw new ValidacionException(Errores.ERROR_0037);
        }
    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param registroSir
     * @throws Exception
     */
    private void procesarMensajeACK(RegistroSir registroSir) throws Exception{

        if (EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado())){

            // Actualizamos el registroSir
            registroSir.setEstado(EstadoRegistroSir.REENVIADO_Y_ACK);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado())){

            // Actualizamos el registroSir
            registroSir.setEstado(EstadoRegistroSir.RECHAZADO_Y_ACK);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.RECHAZADO_Y_ACK.equals(registroSir.getEstado())){

            log.info("Se ha recibido un mensaje ACK duplicado con identificador: " + registroSir.getIdentificadorIntercambio());

        }else{
            log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
            throw new ValidacionException(Errores.ERROR_0037);
        }
    }

    /**
     * Procesa un mensaje de control de tipo CONFIRMACION
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeCONFIRMACION(OficioRemision oficioRemision, Mensaje mensaje) throws Exception{

        switch (oficioRemision.getEstado()) {

            case RegwebConstantes.OFICIO_SIR_ENVIADO:
            case RegwebConstantes.OFICIO_SIR_ENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR:
            case RegwebConstantes.OFICIO_SIR_REENVIADO:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR:

                oficioRemision.setCodigoEntidadRegistralProcesado(mensaje.getCodigoEntidadRegistralOrigen());
                oficioRemision.setDecodificacionEntidadRegistralProcesado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), mensaje.getCodigoEntidadRegistralOrigen(), RegwebConstantes.OFICINA));
                oficioRemision.setNumeroRegistroEntradaDestino(mensaje.getNumeroRegistroEntradaDestino());
                oficioRemision.setFechaEntradaDestino(mensaje.getFechaEntradaDestino());
                oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
                oficioRemision.setFechaEstado(mensaje.getFechaEntradaDestino());
                oficioRemisionEjb.merge(oficioRemision);

                // Marcamos el Registro original como ACEPTADO
                if (oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {
                    registroEntradaEjb.cambiarEstado(oficioRemision.getRegistrosEntrada().get(0).getId(), RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);

                    //Borrar Anexos de Custodia
                    //TODO PENDIENTE DE PROBAR
                    try {
                        anexoEjb.eliminarAnexosCustodiaRegistroDetalle(oficioRemision.getRegistrosEntrada().get(0).getRegistroDetalle().getId(),oficioRemision.getRegistrosEntrada().get(0).getUsuario().getEntidad().getId());
                    }catch(I18NException e){
                        e.printStackTrace();
                    }


                }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
                    registroSalidaEjb.cambiarEstado(oficioRemision.getRegistrosSalida().get(0).getId(),RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
                    // TODO PENDIENTE DE PROBAR EN PROVES
                    //Borrar Anexos de Custodia
                    try {
                        anexoEjb.eliminarAnexosCustodiaRegistroDetalle(oficioRemision.getRegistrosSalida().get(0).getRegistroDetalle().getId(),oficioRemision.getRegistrosSalida().get(0).getUsuario().getEntidad().getId());
                    }catch (I18NException e){
                        e.printStackTrace();
                    }
                }

                break;

            case (RegwebConstantes.OFICIO_ACEPTADO):

                log.info("Se ha recibido un mensaje de confirmación duplicado: " + mensaje.toString());

                break;

            default:
                log.info("El RegistroSir no tiene el estado necesario para ser Confirmado: " + oficioRemision.getIdentificadorIntercambio());
                throw new ValidacionException(Errores.ERROR_0037);
        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param oficioRemision
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(OficioRemision oficioRemision, Mensaje mensaje) throws Exception{

        switch (oficioRemision.getEstado()) {

            case (RegwebConstantes.OFICIO_SIR_ENVIADO):

                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
                oficioRemision.setCodigoError(mensaje.getCodigoError());
                oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
                oficioRemision.setNumeroReintentos(0);
                oficioRemision.setFechaEstado(new Date());
                oficioRemisionEjb.merge(oficioRemision);

                break;

            case (RegwebConstantes.OFICIO_SIR_REENVIADO):

                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
                oficioRemision.setCodigoError(mensaje.getCodigoError());
                oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
                oficioRemision.setNumeroReintentos(0);
                oficioRemision.setFechaEstado(new Date());
                oficioRemisionEjb.merge(oficioRemision);

                break;

            case (RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR):
            case (RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR):

                log.info("Se ha recibido un mensaje duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());
                throw new ValidacionException(Errores.ERROR_0037);

        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param registroSir
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(RegistroSir registroSir, Mensaje mensaje) throws Exception{

        if (EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado())){

            registroSir.setEstado(EstadoRegistroSir.REENVIADO_Y_ERROR);
            registroSir.setCodigoError(mensaje.getCodigoError());
            registroSir.setDescripcionError(mensaje.getDescripcionMensaje());
            registroSir.setNumeroReintentos(0);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado())){

            registroSir.setEstado(EstadoRegistroSir.RECHAZADO_Y_ERROR);
            registroSir.setCodigoError(mensaje.getCodigoError());
            registroSir.setDescripcionError(mensaje.getDescripcionMensaje());
            registroSir.setNumeroReintentos(0);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.REENVIADO_Y_ERROR.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.RECHAZADO_Y_ERROR.equals(registroSir.getEstado())){

            log.info("Se ha recibido un registroSir duplicado con identificador: " + registroSir.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0037);

        }
    }

    /**
     *
     * @param idRegistro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro,
                                                   Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = null;
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Envio SIR a " + codigoOficinaSir;
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuario);

        try{

            // OficinaSir destino
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaSirDestino = oficinasService.obtenerOficina(codigoOficinaSir, null, null);

            //Obtenemos los contactos de la oficina Sir de destino
            String contactosEntidadRegistralDestino = getContactosOficinaSir(oficinaSirDestino);

            if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)){

                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
                RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroEntrada.getNumeroRegistroFormateado()+" mediante SIR a: " + oficinaSirDestino.getDenominacion());
                log.info("");

                // Si no tiene generado el Justificante, lo hacemos
                if (!registroDetalle.getTieneJustificante()) {

                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroEntrada, tipoRegistro.toLowerCase(), "es");
                    registroDetalle.getAnexosFull().add(anexoFull);
                }

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroEntrada.getOficina().getCodigo(), usuario.getEntidad()));
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

                // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
                registroDetalle.setOficinaOrigen(registroEntrada.getOficina());
                registroDetalle.setOficinaOrigenExternoCodigo(null);
                registroDetalle.setOficinaOrigenExternoDenominacion(null);
                registroDetalle.setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
                registroDetalle.setFechaOrigen(registroEntrada.getFecha());

                // Actualizamos el registro
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroEntrada.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
                oficioRemision.setDestinoExternoCodigo(registroEntrada.getDestinoExternoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
                oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosSalida(null);
                oficioRemision.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                oficioRemision.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                oficioRemision.setContactosEntidadRegistralDestino(contactosEntidadRegistralDestino);

                //Transformamos el registro de Entrada a RegistroSir
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            } else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)){

                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);
                RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroSalida.getNumeroRegistroFormateado()+" mediante SIR a: " + oficinaSirDestino.getDenominacion());
                log.info("");


                // Si no tiene generado el Justificante, lo hacemos
                if (!registroDetalle.getTieneJustificante()) {
                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroSalida, tipoRegistro.toLowerCase(), "es");

                    registroDetalle.getAnexosFull().add(anexoFull);
                }

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroSalida.getOficina().getCodigo(), usuario.getEntidad()));
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

                // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
                registroDetalle.setOficinaOrigen(registroSalida.getOficina());
                registroDetalle.setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
                registroDetalle.setFechaOrigen(registroSalida.getFecha());

                // Actualizamos el registro
                registroSalida = registroSalidaEjb.merge(registroSalida);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroSalida.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
                oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroSalida.getInteresadoDestinoDenominacion());
                oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosEntrada(null);

                // Transformamos el RegistroSalida en un RegistroSir
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

            }

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
            peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

            // Registramos el Oficio de Remisión SIR
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);

            // Enviamos el Registro al Componente CIR
            emisionEjb.enviarFicheroIntercambio(registroSir);

            // Modificamos el estado del OficioRemision
            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_ENVIADO);

            // Integración
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin enviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");


        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }catch (I18NException e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }catch (I18NValidationException e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }

        return oficioRemision;
    }

    /**
     * Acepta un RegistroSir, creando un Registro de Entrada
     * @param registroSir
     * @throws Exception
     */
    @Override
    public RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs)
            throws Exception, I18NException, I18NValidationException  {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Aceptando RegistroSir: " + TipoAnotacion.getTipoAnotacion(registroSir.getTipoAnotacion()).getName();
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("");
        log.info("Aceptando RegistroSir " + registroSir.getIdentificadorIntercambio());

        // Creamos y registramos el RegistroEntrada a partir del RegistroSir aceptado
        RegistroEntrada registroEntrada;

        try {
            registroEntrada = registroSirEjb.transformarRegistroSirEntrada(registroSir, usuario, oficinaActiva, idLibro, idIdioma, idTipoAsunto, camposNTIs);

            // Creamos la TrazabilidadSir
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setRegistroEntrada(registroEntrada);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
            trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
            trazabilidadSir.setFecha(new Date());
            trazabilidadSirEjb.persist(trazabilidadSir);

            // CREAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
            trazabilidad.setRegistroSir(registroSir);
            trazabilidad.setRegistroEntradaOrigen(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.ACEPTADO);

            // Enviamos el Mensaje de Confirmación
            mensajeEjb.enviarMensajeConfirmacion(registroSir, registroEntrada.getNumeroRegistroFormateado());

            // Integracion
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            return registroEntrada;

        } catch (I18NException e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        } catch (I18NValidationException e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    @Override
    public void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reenviando RegistroSir a " + oficinaReenvio.getDenominacion();
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("----------------------------------------------------------------------------------------------");
        log.info("Reenviando RegistroSIR: " + registroSir.getNumeroRegistro()+" mediante SIR a: " + oficinaReenvio.getDenominacion());
        log.info("");

        try {

            // Actualizamos la oficina destino con la escogida por el usuario
            registroSir.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
            registroSir.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
            registroSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            registroSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());

            // Actualizamos la oficina de origen con la oficina activa
            registroSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            registroSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

            // Modificamos usuario, contacto, aplicacion
            registroSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            registroSir.setNombreUsuario(usuario.getNombreCompleto());
            registroSir.setContactoUsuario(usuario.getEmail());
            registroSir.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
            registroSir.setDecodificacionTipoAnotacion(observaciones);

            // Actualizamos el RegistroSir
            registroSir = registroSirEjb.merge(registroSir);
            registroSir = registroSirEjb.getRegistroSirConAnexos(registroSir.getId());

            // Enviamos el Registro al Componente CIR
            emisionEjb.reenviarFicheroIntercambio(registroSir);

            // Creamos la TrazabilidadSir para el Reenvio
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_REENVIO);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());
            trazabilidadSir.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
            trazabilidadSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            trazabilidadSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
            trazabilidadSir.setContactoUsuario(usuario.getEmail());
            trazabilidadSir.setObservaciones(observaciones);
            trazabilidadSir.setFecha(new Date());
            trazabilidadSirEjb.persist(trazabilidadSir);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.REENVIADO);

            // Integracion
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin reenviando RegistroSIR: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    /**
     *
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    @Override
    public void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Rechazando RegistroSir";
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("----------------------------------------------------------------------------------------------");
        log.info("Rechazando RegistroSIR: " + registroSir.getNumeroRegistro()+" mediante SIR a: " + registroSir.getDecodificacionEntidadRegistralInicio());
        log.info("");

        try{

            // Modificamos la oficina destino con la de inicio
            registroSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
            registroSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralInicio());
            //registroSir.setCodigoUnidadTramitacionDestino("");
            //registroSir.setDecodificacionUnidadTramitacionDestino("");

            // Modificamos la oficina de origen con la oficina activa
            registroSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            registroSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

            // Modificamos usuario, contacto, aplicacion
            registroSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            registroSir.setNombreUsuario(usuario.getNombreCompleto());
            registroSir.setContactoUsuario(usuario.getEmail());

            registroSir.setTipoAnotacion(TipoAnotacion.RECHAZO.getValue());
            registroSir.setDecodificacionTipoAnotacion(observaciones);

            registroSir = registroSirEjb.merge(registroSir);

            registroSir = registroSirEjb.getRegistroSirConAnexos(registroSir.getId());

            // Rechazamos el RegistroSir
            emisionEjb.rechazarFicheroIntercambio(registroSir);

            // Creamos la TrazabilidadSir para el Rechazo
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralInicio());
            trazabilidadSir.setCodigoUnidadTramitacionDestino(null);
            trazabilidadSir.setDecodificacionUnidadTramitacionDestino(null);
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
            trazabilidadSir.setContactoUsuario(usuario.getEmail());
            trazabilidadSir.setObservaciones(observaciones);
            trazabilidadSir.setFecha(new Date());
            trazabilidadSirEjb.persist(trazabilidadSir);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.RECHAZADO);

            // Integracion
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin rechazando RegistroSIR: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    @Override
    public void reenviarRegistro(String tipoRegistro, Long idRegistro, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException, I18NValidationException{

        RegistroSir registroSir = null;
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reenvío SIR a " + oficinaReenvio.getDenominacion();
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuario);

        try{

            if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)){

                RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
                RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Reenviando FicheroIntercambio del registro: " + registroEntrada.getNumeroRegistroFormateado()+" mediante SIR a: " + oficinaReenvio.getDenominacion());
                log.info("");

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(observaciones);

                // Actualizamos el registro
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroEntrada.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
                oficioRemision.setDestinoExternoCodigo(oficinaReenvio.getOrganismoResponsable().getCodigo());
                oficioRemision.setDestinoExternoDenominacion(oficinaReenvio.getOrganismoResponsable().getDenominacion());
                oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosSalida(null);

                // Transformamos el RegistroEntrada en un RegistroSir
                registroEntrada = registroEntradaEjb.getConAnexosFull(oficioRemision.getRegistrosEntrada().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            }else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)){

                RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
                RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroSalida.getNumeroRegistroFormateado()+" mediante SIR a: " + oficinaReenvio.getDenominacion());
                log.info("");

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(observaciones);

                // Actualizamos el registro
                registroSalida = registroSalidaEjb.merge(registroSalida);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroSalida.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
                oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroSalida.getInteresadoDestinoDenominacion());
                oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosEntrada(null);

                // Transformamos el RegistroSalida en un RegistroSir
                registroSalida = registroSalidaEjb.getConAnexosFull(oficioRemision.getRegistrosSalida().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

            }

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
            peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

            // Registramos el Oficio de Remisión SIR
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);

            // Actualizamos la unidad de tramitación destino con el organismo responsable de la oficina de reenvio
            registroSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            registroSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());

            // Enviamos el Registro al Componente CIR
            emisionEjb.reenviarFicheroIntercambio(registroSir);

            // Modificamos el estado del OficioRemision
            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_REENVIADO);

            // Integración
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(),System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin reenviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }catch (I18NException e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }catch (I18NValidationException e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }

    }

    @Override
    public void reintentarEnviosSinConfirmacion(Long idEntidad) throws Exception{

        try {

            // RegistrosSir pendientes de volver a intentar su envío
            List<Long> registrosSir = registroSirEjb.getEnviadosSinAck(idEntidad);

            if(registrosSir.size() > 0){

                log.info("Hay " + registrosSir.size() +  " RegistrosSir pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los RegistrosSir
                for (Long registroSir : registrosSir) {

                    reintentarEnvio(registroSir);
                }
            }else{
                log.info("No hay RegistrosSir pendientes de volver a enviar al nodo CIR");
            }

            // OficiosRemision pendientes de volver a intentar su envío
            List<OficioRemision> oficios = oficioRemisionEjb.getEnviadosSinAck(idEntidad);

            if(oficios.size() > 0){

                log.info("Hay " + oficios.size() +  " Oficios de Remision pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los OficiosRemision
                for (OficioRemision oficio : oficios) {

                    reintentarEnvioOficioRemision(oficio);
                }
            }else{
                log.info("No hay Oficios de Remision pendientes de volver a enviar al nodo CIR");
            }


        } catch (I18NException e) {
            e.printStackTrace();
        }catch (Exception e){
            log.info("Error al reintenar el envio de registros sin confirmacion");
            e.printStackTrace();
        }
    }

    @Override
    public void reintentarEnviosConError(Long idEntidad) throws Exception{

        try {

            // RegistrosSir enviados con errores
            List<Long> registrosSir = registroSirEjb.getEnviadosConError(idEntidad);

            if(registrosSir.size() > 0){

                log.info("Hay " + registrosSir.size() +  " RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los RegistrosSir
                for (Long registroSir : registrosSir) {

                    reintentarEnvio(registroSir);
                }
            }else{
                log.info("No hay RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");
            }

            // OficiosRemision enviados con errores
            List<OficioRemision> oficios = oficioRemisionEjb.getEnviadosConError(idEntidad);

            if(oficios.size() > 0){

                log.info("Hay " + oficios.size() +  " Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los OficiosRemision
                for (OficioRemision oficio : oficios) {

                    reintentarEnvioOficioRemision(oficio);
                }
            }else{
                log.info("No hay Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");
            }


        } catch (I18NException e) {
            e.printStackTrace();
        }catch (Exception e){
            log.info("Error al reintenar el envio de registros con error");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param idRegistroSir
     * @throws Exception
     */
    private void reintentarEnvio(Long idRegistroSir) throws Exception{

        RegistroSir registroSir = registroSirEjb.getRegistroSirConAnexos(idRegistroSir);

        log.info("Reintentando envio registroSir "+ registroSir.getIdentificadorIntercambio()+" a " + registroSir.getDecodificacionEntidadRegistralDestino());

        emisionEjb.enviarFicheroIntercambio(registroSir);
        registroSir.setNumeroReintentos(registroSir.getNumeroReintentos() + 1);
        registroSir.setFechaEstado(new Date());

        // Modificamos su estado si estaba marcado con ERROR
        if(registroSir.getEstado().equals(EstadoRegistroSir.REENVIADO_Y_ERROR)){
            registroSir.setEstado(EstadoRegistroSir.REENVIADO);
        }else if(registroSir.getEstado().equals(EstadoRegistroSir.RECHAZADO_Y_ERROR)){
            registroSir.setEstado(EstadoRegistroSir.RECHAZADO);
        }

        registroSirEjb.merge(registroSir);

    }

    /**
     *
     * @param oficio
     * @throws Exception
     * @throws I18NException
     */
    private void reintentarEnvioOficioRemision(OficioRemision oficio) throws Exception, I18NException{

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reintentar envío con error: " + oficio.getIdentificadorIntercambio();
        peticion.append("IdentificadorIntercambio: ").append(oficio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(oficio.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(oficio.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

        if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){

            try{
                log.info("Reintentando envio OficioRemisionSir " + oficio.getIdentificadorIntercambio()+ " a " + oficio.getDecodificacionEntidadRegistralDestino());

                // Transformamos el RegistroEntrada en un RegistroSir
                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficio.getRegistrosEntrada().get(0).getId());
                RegistroSir registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

                // Enviamos el Registro al Componente CIR
                emisionEjb.enviarFicheroIntercambio(registroSir);

            }catch (Exception e){
                e.printStackTrace();
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
                throw e;
            }


        }else if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

            try{
                log.info("Reintentando envio OficioRemisionSir" + oficio.getIdentificadorIntercambio()+ " a " + oficio.getDecodificacionEntidadRegistralDestino());

                // Transformamos el RegistroSalida en un RegistroSir
                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficio.getRegistrosSalida().get(0).getId());
                RegistroSir registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

                // Enviamos el Registro al Componente CIR
                emisionEjb.enviarFicheroIntercambio(registroSir);

            }catch (Exception e){
                e.printStackTrace();
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
                throw e;
            }

        }

        // Contabilizamos los reintentos
        oficio.setNumeroReintentos(oficio.getNumeroReintentos() + 1);
        oficio.setFechaEstado(new Date());

        // Modificamos su estado si estaba marcado con ERROR
        if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR) {
            oficio.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
        }else if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR) {
            oficio.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO);
        }

        // Actualizamos el Oficio
        oficioRemisionEjb.merge(oficio);

    }

    /**
     * Indica si el RegistroSir  se puede reenviar, en función de su estado
     * @param estado del registroSir
     * @return
     */
    public boolean puedeReenviarRegistroSir(EstadoRegistroSir estado){
        return  estado.equals(EstadoRegistroSir.RECIBIDO) ||
                estado.equals(EstadoRegistroSir.REENVIADO) ||
                estado.equals(EstadoRegistroSir.REENVIADO_Y_ERROR);

    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     *
     * @param codOficinaOrigen
     * @return el identificador intercambio (String)
     * @throws Exception
     */
    private String generarIdentificadorIntercambio(String codOficinaOrigen, Entidad entidad) throws Exception {

        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String secuencia = contadorEjb.secuenciaSir(entidad.getContadorSir().getId());

        return codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + secuencia;

    }

    /**
     * Comprueba a partir de la Oficina destino, si la Entidad está integrada en SIR
     * @param codigoEntidadRegistralDestino
     * @throws Exception
     */
    private void comprobarEntidad(String codigoEntidadRegistralDestino) throws Exception{

        Entidad entidad;
        Oficina oficina = oficinaEjb.findByCodigo(codigoEntidadRegistralDestino);

        if(oficina != null){
            entidad = oficina.getOrganismoResponsable().getEntidad();

            if(!entidad.getActivo() || !entidad.getSir()){
                log.info("La Entidad de la oficina "+ oficina.getDenominacion() +" no esta activa o no se ha activado su integracion con SIR");
                throw new ValidacionException(Errores.ERROR_0037, "La Entidad de la oficina "+ oficina.getDenominacion() +" no esta activa o no se ha activado su integracion con SIR");

            }else if(!oficinaEjb.isSIRRecepcion(oficina.getId())){
                log.info("La Oficina "+ oficina.getDenominacion() +" no esta habilitada para recibir asientos SIR");
                throw new ValidacionException(Errores.ERROR_0037, "La Oficina "+ oficina.getDenominacion() +" no esta habilitada para recibir asientos SIR");
            }

        }else{
            log.info("El CodigoEntidadRegistralDestino del FicheroIntercambio no pertenece a ninguna Entidad del sistema: " + codigoEntidadRegistralDestino);
            throw new ValidacionException(Errores.ERROR_0037, "El CodigoEntidadRegistralDestino del FicheroIntercambio no pertenece a ninguna Entidad del sistema: " + codigoEntidadRegistralDestino);
        }
    }

    /**
     * Calcula una cadena de ocho dígitos a partir del instante de tiempo actual.
     *
     * @return la cadena (String) de ocho digitos
     */
    private static final AtomicLong TIME_STAMP = new AtomicLong();

    private String getIdToken() {
        long now = System.currentTimeMillis();
        while (true) {
            long last = TIME_STAMP.get();
            if (now <= last)
                now = last + 1;
            if (TIME_STAMP.compareAndSet(last, now))
                break;
        }
        long unsignedValue = Long.toString(now).hashCode() & 0xffffffffL;
        String result = Long.toString(unsignedValue);
        if (result.length() > 8) {
            result = result.substring(result.length() - 8, result.length());
        } else {
            result = String.format("%08d", unsignedValue);
        }
        return result;
    }


    /**
     * Método que obtiene los contactos de la oficina Sir de destino
     * @param oficinaSir
     * @return
     * @throws Exception
     */
    private String getContactosOficinaSir(OficinaTF oficinaSir) throws Exception {
        StringBuilder stb = new StringBuilder();
        for(ContactoTF contactoTF: oficinaSir.getContactos()){
            String scontactoTF = "<b>" + contactoTF.getTipoContacto()+"</b>: "+ contactoTF.getValorContacto();
            stb.append(scontactoTF);
            stb.append("<br>");
        }

        return stb.toString();

    }

}
