package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
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

@Stateless(name = "FicheroIntercambioEJB")
@SecurityDomain("seycon")
public class FicheroIntercambioBean implements FicheroIntercambioLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private MensajeLocal mensajeEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private MensajeControlLocal mensajeControlEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    @Override
    public RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{

        RespuestaRecepcionSir respuesta = new RespuestaRecepcionSir();
        RegistroSir registroSir = null;
        OficioRemision oficioRemision = null;

        // Obtenemos la Entidad
        Entidad entidad = obtenerEntidad(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        respuesta.setEntidad(entidad);

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

                    // Enviamos el Mensaje de Confirmación, no enviaremos ACK
                    RegistroEntrada registroEntrada = trazabilidadEjb.getRegistroAceptado(registroSir.getId());
                    MensajeControl confirmacion = mensajeEjb.enviarMensajeConfirmacion(registroSir, registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getFecha());
                    mensajeControlEjb.persist(confirmacion);

                    respuesta.setAck(false);
                } else {
                    log.info("Se ha recibido un ENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037,"Se ha recibido un ENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                }

            } else {

                // Creamos un nuevo RegistroSir
                registroSir = registroSirEjb.crearRegistroSir(ficheroIntercambio, entidad);
                log.info("El registroSir no existia en el sistema y se ha creado: " + registroSir.getIdentificadorIntercambio());
            }

            // REENVIO
        }else if (TipoAnotacion.REENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un REENVIO: " + ficheroIntercambio.getDescripcionTipoAnotacion() +" - " + ficheroIntercambio.getIdentificadorIntercambio());

            // Buscamos si el Registro recibido ya existe en el sistema
            registroSir = registroSirEjb.getRegistroSir(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());
            oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

            // Registro Sir: Lo hemos recibido de SIR
            if (registroSir != null) { // Ya existe en el sistema

                if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.RECHAZADO_Y_ACK.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.RECHAZADO_Y_ERROR.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado()) ||
                        EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado())) {

                    // Modificar el estado del Registro a RECIBIDO
                    registroSir.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                    registroSir.setDecodificacionEntidadRegistralOrigen(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                    registroSir.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralDestino());
                    registroSir.setDecodificacionEntidadRegistralDestino(ficheroIntercambio.getDescripcionEntidadRegistralDestino());
                    registroSir.setAplicacion(ficheroIntercambio.getAplicacionEmisora());
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
                    trazabilidadSirEjb.persist(trazabilidadSir);

                    log.info("El registroSir existia en el sistema, se ha vuelto a recibir: " + registroSir.getIdentificadorIntercambio());

                }else if (EstadoRegistroSir.RECIBIDO.equals(registroSir.getEstado())) {

                    log.info("Se ha recibido un REENVIO que ya habia sido recibido previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                }else if (EstadoRegistroSir.ACEPTADO.equals(registroSir.getEstado())) {

                    log.info("Se ha recibido un REENVIO que ya habia sido aceptado previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                } else{
                    log.info("Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
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
                    oficioRemision.setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                    oficioRemision.setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                    oficioRemisionEjb.merge(oficioRemision);

                    log.info("El oficio de remision existia en el sistema, nos lo han renviado: " + oficioRemision.getIdentificadorIntercambio());

                }else if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_DEVUELTO){
                    log.info("Se ha recibido un REENVIO que ya habia sido recibido previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");

                } else{
                    log.info("Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un REENVIO con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                }

            }else{
                // Creamos un nuevo RegistroSir
                registroSir = registroSirEjb.crearRegistroSir(ficheroIntercambio, entidad);
                log.info("El registroSir no existia en el sistema y se ha creado: " + registroSir.getIdentificadorIntercambio());
            }

            // RECHAZO
        }else if (TipoAnotacion.RECHAZO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un RECHAZO");

            // Buscamos si el FicheroIntercambio ya existe en el sistema
            oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

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
                        //registroEntrada.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        //registroEntrada.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroEntrada.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroEntradaEjb.merge(registroEntrada);

                    }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

                        RegistroSalida registroSalida = oficioRemision.getRegistrosSalida().get(0);
                        // Actualizamos el registro de salida
                        registroSalida.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
                        registroSalida.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        //registroSalida.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        //registroSalida.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroSalida.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroSalidaEjb.merge(registroSalida);
                    }

                    // Actualizamos el oficio
                    oficioRemision.setCodigoEntidadRegistralProcesado(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
                    oficioRemision.setDecodificacionEntidadRegistralProcesado(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen());
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_RECHAZADO);
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemision.setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                    oficioRemision.setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                    oficioRemisionEjb.merge(oficioRemision);

                    log.info("El oficio de remision existia en el sistema, nos lo han rechazado: " + oficioRemision.getIdentificadorIntercambio());

                }else if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_RECHAZADO){

                    log.info("Se ha recibido un RECHAZO de un registroSir que ya esta devuelto" + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un RECHAZO de un registroSir que ya esta devuelto" + ficheroIntercambio.getIdentificadorIntercambio());

                }else{
                    log.info("Se ha recibido un RECHAZO cuyo estado no lo permite: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un RECHAZO cuyo estado no lo permite: " + ficheroIntercambio.getIdentificadorIntercambio());
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
                        trazabilidadSirEjb.persist(trazabilidadSir);

                    }
                }else{
                    //Buscamos si ya está rechazado
                    oficioRemision = oficioRemisionEjb.getBySirRechazado(ficheroIntercambio.getIdentificadorIntercambio(), ficheroIntercambio.getCodigoEntidadRegistralDestino());

                    if(oficioRemision != null){
                        log.info("Se ha recibido un RECHAZO que ya habia sido rechazado previamente: " + ficheroIntercambio.getIdentificadorIntercambio() + ", volvemos a enviar un ACK");
                    }else{
                        log.info("El registro recibido no existe en el sistema: " + ficheroIntercambio.getIdentificadorIntercambio());
                        throw new ValidacionException(Errores.ERROR_0065, "El registro recibido no existe en el sistema: " + ficheroIntercambio.getIdentificadorIntercambio());
                    }
                }

            }
        }

        respuesta.setRegistroSir(registroSir);
        return respuesta;
    }


    /**
     * Obtiene la Entidad de REBWEB3 a partir de la Oficina destino
     * @param codigoEntidadRegistralDestino
     * @throws Exception
     */
    private Entidad obtenerEntidad(String codigoEntidadRegistralDestino) throws Exception{

        if(codigoEntidadRegistralDestino != null) {

            Oficina oficina = obtenerOficina(codigoEntidadRegistralDestino);

            if(oficina != null){
                return oficina.getOrganismoResponsable().getEntidad();
            }
        }

        return null;

    }


    private Oficina obtenerOficina(String codigo) throws Exception {

        if(multiEntidadEjb.isMultiEntidad()){
            return oficinaEjb.findByCodigoMultiEntidad(codigo);
        }else{
            return oficinaEjb.findByCodigo(codigo);
        }
    }

}
