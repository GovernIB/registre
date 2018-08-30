package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
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
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SirRecepcionEJB")
@SecurityDomain("seycon")
public class SirRecepcionBean implements SirRecepcionLocal {

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
                    try {
                        anexoEjb.eliminarAnexosCustodiaRegistroDetalle(oficioRemision.getRegistrosEntrada().get(0).getRegistroDetalle().getId(),oficioRemision.getRegistrosEntrada().get(0).getUsuario().getEntidad().getId());
                    }catch(I18NException e){
                        e.printStackTrace();
                    }


                }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
                    registroSalidaEjb.cambiarEstado(oficioRemision.getRegistrosSalida().get(0).getId(),RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
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



}
