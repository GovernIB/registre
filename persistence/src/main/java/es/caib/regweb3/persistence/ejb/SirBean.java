package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.persistence.utils.RegwebJustificantePluginManager;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;
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
public class SirBean implements SirLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private AsientoRegistralSirLocal asientoRegistralSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private EmisionLocal emisionEjb;
    @EJB private MensajeLocal mensajeEjb;


    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    @Override
    public void recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{

        // ENVIO Y REENVIO
        if (TipoAnotacion.ENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion()) ||
                TipoAnotacion.REENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un: " + ficheroIntercambio.getDescripcionTipoAnotacion());

            // Buscamos si el Registro recibido ya existe en el sistema
            AsientoRegistralSir asiento = asientoRegistralSirEjb.getAsientoRegistral(ficheroIntercambio.getIdentificadorIntercambio(),ficheroIntercambio.getCodigoEntidadRegistralDestino());

            if(asiento != null) { // Ya existe en el sistema

                if(EstadoAsientoRegistralSir.RECIBIDO.equals(asiento.getEstado())){

                    log.info("El AsientoRegistral" + asiento.getIdentificadorIntercambio() +" ya se ha recibido.");
                    throw new ValidacionException(Errores.ERROR_0205);

                }else if(EstadoAsientoRegistralSir.RECHAZADO.equals(asiento.getEstado()) ||
                        EstadoAsientoRegistralSir.RECHAZADO_Y_ACK.equals(asiento.getEstado()) ||
                        EstadoAsientoRegistralSir.RECHAZADO_Y_ERROR.equals(asiento.getEstado()) ||
                        EstadoAsientoRegistralSir.REENVIADO.equals(asiento.getEstado())){

                   // todo El asiento ya existe pero está Rechazado/Reenviado, que hacemos??

                }else{
                    log.info("Se ha intentado enviar un ficheroIntercambio con estado incompatible: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0063);
                }


            }else{ // No existe en el sistema, creamos un nuevo AsientoRegistralSir

                // Convertimos el Fichero de Intercambio SICRES3 en {@link es.caib.regweb3.model.AsientoRegistralSir}
                asiento = asientoRegistralSirEjb.transformarFicheroIntercambio(ficheroIntercambio);
                asiento.setEstado(EstadoAsientoRegistralSir.RECIBIDO);

                asientoRegistralSirEjb.crearAsientoRegistralSir(asiento);
                log.info("El asiento no existía en el sistema y se ha creado: " + asiento.getIdentificadorIntercambio());

            }

        // RECHAZO
        }else if (TipoAnotacion.RECHAZO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            log.info("El ficheroIntercambio recibido es un RECHAZO");

            // Buscamos si el Asiento recibido ya existe en el sistema
            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());

            if(oficioRemision != null) { // Existe en el sistema

                if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO ||
                        oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_RECHAZADO_ACK){

                    if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
                        
                        RegistroEntrada registroEntrada = oficioRemision.getRegistrosEntrada().get(0);
                        // Actualizamos el asiento
                        registroEntrada.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
                        registroEntrada.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroEntrada.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroEntrada.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroEntrada.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroEntradaEjb.merge(registroEntrada);

                    }else if(oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

                        RegistroSalida registroSalida = oficioRemision.getRegistrosSalida().get(0);
                        // Actualizamos el asiento
                        registroSalida.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
                        registroSalida.getRegistroDetalle().setAplicacion(ficheroIntercambio.getAplicacionEmisora());
                        registroSalida.getRegistroDetalle().setObservaciones(ficheroIntercambio.getObservacionesApunte());
                        registroSalida.getRegistroDetalle().setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                        registroSalida.getRegistroDetalle().setDecodificacionTipoAnotacion(ficheroIntercambio.getDescripcionTipoAnotacion());
                        registroSalidaEjb.merge(registroSalida);
                    }

                    // Actualizamos el oficio
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_DEVUELTO);
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemisionEjb.merge(oficioRemision);

                }else if(oficioRemision.getEstado() == RegwebConstantes.OFICIO_SIR_DEVUELTO){

                    log.info("Se ha intentado rechazar un asiento que ya esta devuelto" + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0205);

                }else{
                    log.info("Se ha intentado rechazar cuyo estado no lo permite: " + ficheroIntercambio.getIdentificadorIntercambio());
                    throw new ValidacionException(Errores.ERROR_0063);
                }


            }else{
                log.info("El registro recibido no existe en el sistema: " + ficheroIntercambio.getIdentificadorIntercambio());
                throw new ValidacionException(Errores.ERROR_0063);
            }
        }
    }

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     * @param mensaje
     * @throws Exception
     */
    @Override
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception{

        // Mensaje ACK
        if(mensaje.getTipoMensaje().equals(TipoMensaje.ACK)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio());
            AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.getAsientoRegistral(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeACK(oficioRemision);
            }else if(asientoRegistralSir != null){
                procesarMensajeACK(asientoRegistralSir);
            }else{
                log.info("El mensaje de control corresponde a un Asiento registral que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0044);
            }

        // Mensaje CONFIRMACIÓN
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.CONFIRMACION)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio());

            if(oficioRemision != null){
                procesarMensajeCONFIRMACION(oficioRemision, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un Asiento registral que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0044);
            }


        // Mensaje ERROR
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.ERROR)){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio());
            AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.getAsientoRegistral(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeERROR(oficioRemision, mensaje);
            }else if(asientoRegistralSir != null){
                procesarMensajeERROR(asientoRegistralSir, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un Asiento registral que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0044);
            }

        }else{
            log.info("El tipo mensaje de control no es válido: " + mensaje.getTipoMensaje());
            throw new ValidacionException(Errores.ERROR_0044);
        }

    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeACK(OficioRemision oficioRemision) throws Exception{

        if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO)){

            // Actualizamos el asiento
            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ACK);
            oficioRemision.setFechaEstado(new Date());
            oficioRemision.setNumeroReintentos(0);
            oficioRemisionEjb.merge(oficioRemision);

        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_REENVIADO)){

            // Actualizamos el asiento
            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ACK);
            oficioRemision.setFechaEstado(new Date());
            oficioRemision.setNumeroReintentos(0);
            oficioRemisionEjb.merge(oficioRemision);

        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_RECHAZADO)){

            // Actualizamos el asiento
            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_RECHAZADO_ACK);
            oficioRemision.setFechaEstado(new Date());
            oficioRemision.setNumeroReintentos(0);
            oficioRemisionEjb.merge(oficioRemision);

        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO_ACK) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_REENVIADO_ACK) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_RECHAZADO_ACK)){

            log.info("Se ha recibido un mensaje duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0206);

        }else{
            log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
            throw new ValidacionException(Errores.ERROR_0044);
        }
    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param asientoRegistralSir
     * @throws Exception
     */
    private void procesarMensajeACK(AsientoRegistralSir asientoRegistralSir) throws Exception{

        if (EstadoAsientoRegistralSir.ENVIADO.equals(asientoRegistralSir.getEstado())){

            // Actualizamos el asiento
            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.ENVIADO_Y_ACK);
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.REENVIADO.equals(asientoRegistralSir.getEstado())){

            // Actualizamos el asiento
            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.REENVIADO_Y_ACK);
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.RECHAZADO.equals(asientoRegistralSir.getEstado())){

            // Actualizamos el asiento
            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.RECHAZADO_Y_ACK);
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.ENVIADO_Y_ACK.equals(asientoRegistralSir.getEstado()) ||
                EstadoAsientoRegistralSir.REENVIADO_Y_ACK.equals(asientoRegistralSir.getEstado()) ||
                EstadoAsientoRegistralSir.RECHAZADO_Y_ACK.equals(asientoRegistralSir.getEstado())){

            log.info("Se ha recibido un asiento duplicado con identificador: " + asientoRegistralSir.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0206);

        }else{
            log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
            throw new ValidacionException(Errores.ERROR_0044);
        }
    }

    /**
     * Procesa un mensaje de control de tipo CONFIRMACION
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeCONFIRMACION(OficioRemision oficioRemision, Mensaje mensaje) throws Exception{

        if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO_ACK) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR)){

            oficioRemision.setNumeroRegistroEntradaDestino(mensaje.getNumeroRegistroEntradaDestino());
            oficioRemision.setFechaEntradaDestino(mensaje.getFechaEntradaDestino());
            oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
            oficioRemision.setFechaEstado(mensaje.getFechaEntradaDestino());
            oficioRemisionEjb.merge(oficioRemision);

        }else  if(oficioRemision.getEstado() == (RegwebConstantes.OFICIO_ACEPTADO)){

            log.info("Se ha recibido un mensaje de confirmación duplicado: " + mensaje.toString());
            throw new ValidacionException(Errores.ERROR_0206);

        }else{
            log.info("El asiento registral no tiene el estado necesario para ser Confirmado: " + oficioRemision.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0044);
        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param oficioRemision
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(OficioRemision oficioRemision, Mensaje mensaje) throws Exception{

        if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO)){

            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
            oficioRemision.setCodigoError(mensaje.getCodigoError());
            oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
            oficioRemision.setNumeroReintentos(0);
            oficioRemision.setFechaEstado(new Date());
            oficioRemisionEjb.merge(oficioRemision);


        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_REENVIADO)){

            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
            oficioRemision.setCodigoError(mensaje.getCodigoError());
            oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
            oficioRemision.setNumeroReintentos(0);
            oficioRemision.setFechaEstado(new Date());
            oficioRemisionEjb.merge(oficioRemision);

        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_RECHAZADO)){

            oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_RECHAZADO_ERROR);
            oficioRemision.setCodigoError(mensaje.getCodigoError());
            oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
            oficioRemision.setNumeroReintentos(0);
            oficioRemision.setFechaEstado(new Date());
            oficioRemisionEjb.merge(oficioRemision);

        } else if (oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR) ||
                oficioRemision.getEstado() == (RegwebConstantes.OFICIO_SIR_RECHAZADO_ERROR)){

            log.info("Se ha recibido un mensaje duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0206);

        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param asientoRegistralSir
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(AsientoRegistralSir asientoRegistralSir, Mensaje mensaje) throws Exception{

        if (EstadoAsientoRegistralSir.ENVIADO.equals(asientoRegistralSir.getEstado())){

            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.ENVIADO_Y_ERROR);
            asientoRegistralSir.setCodigoError(mensaje.getCodigoError());
            asientoRegistralSir.setDescripcionError(mensaje.getDescripcionMensaje());
            asientoRegistralSir.setNumeroReintentos(0);
            asientoRegistralSir.setFechaEstado(new Date());
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.REENVIADO.equals(asientoRegistralSir.getEstado())){

            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.REENVIADO_Y_ERROR);
            asientoRegistralSir.setCodigoError(mensaje.getCodigoError());
            asientoRegistralSir.setDescripcionError(mensaje.getDescripcionMensaje());
            asientoRegistralSir.setNumeroReintentos(0);
            asientoRegistralSir.setFechaEstado(new Date());
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.RECHAZADO.equals(asientoRegistralSir.getEstado())){

            asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.RECHAZADO_Y_ERROR);
            asientoRegistralSir.setCodigoError(mensaje.getCodigoError());
            asientoRegistralSir.setDescripcionError(mensaje.getDescripcionMensaje());
            asientoRegistralSir.setNumeroReintentos(0);
            asientoRegistralSir.setFechaEstado(new Date());
            asientoRegistralSirEjb.merge(asientoRegistralSir);

        } else if (EstadoAsientoRegistralSir.ENVIADO_Y_ERROR.equals(asientoRegistralSir.getEstado()) ||
                EstadoAsientoRegistralSir.REENVIADO_Y_ERROR.equals(asientoRegistralSir.getEstado()) ||
                EstadoAsientoRegistralSir.RECHAZADO_Y_ERROR.equals(asientoRegistralSir.getEstado())){

            log.info("Se ha recibido un asiento duplicado con identificador: " + asientoRegistralSir.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0206);

        }
    }

    /**
     *
     * @param idRegistro
     * @param codigoEntidadRegistralDestino
     * @param denominacionEntidadRegistralDestino
     * @param oficinaActiva
     * @param usuario
     * @param idLibro
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro) throws Exception, I18NException {

        AsientoRegistralSir asientoRegistralSir = null;

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuario);
        oficioRemision.setLibro(new Libro(idLibro));

        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)){

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

            // Si no tiene generado el Justificante, lo hacemos
            if (!registroDetalle.tieneJustificante()) {

                IJustificantePlugin justificantePlugin = RegwebJustificantePluginManager.getInstance(usuario.getEntidad().getId());

                if(justificantePlugin != null) {

                    // Generamos el pdf del Justificante
                    ByteArrayOutputStream baos = justificantePlugin.generarJustificante(registroEntrada);

                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = anexoEjb.crearJustificante(usuario, idRegistro, tipoRegistro.toLowerCase(), baos);
                    registroDetalle.getAnexos().add(anexoFull.getAnexo());
                }

            }

            // Actualizamos el Registro con campos SIR
            registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
            registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroEntrada.getOficina().getCodigo()));
            registroDetalle.setCodigoEntidadRegistralDestino(codigoEntidadRegistralDestino);
            registroDetalle.setDecodificacionEntidadRegistralDestino(denominacionEntidadRegistralDestino);
            registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
            registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

            // Actualizamos el registro
            registroEntrada = registroEntradaEjb.merge(registroEntrada);

            // Datos del Oficio de remisión
            oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
            oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
            oficioRemision.setDestinoExternoCodigo(registroEntrada.getDestinoExternoCodigo());
            oficioRemision.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
            oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
            oficioRemision.setOrganismoDestinatario(null);
            oficioRemision.setRegistrosSalida(null);

            // Transformamos el RegistroEntrada en un AsientoRegistralSir
            registroEntrada = registroEntradaEjb.getConAnexosFullCompleto(oficioRemision.getRegistrosEntrada().get(0).getId());
            asientoRegistralSir = asientoRegistralSirEjb.transformarRegistroEntrada(registroEntrada);

        }else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)){

            RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
            RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

            // Si no tiene generado el Justificante, lo hacemos
            if (!registroDetalle.tieneJustificante()) {

                IJustificantePlugin justificantePlugin = RegwebJustificantePluginManager.getInstance(usuario.getEntidad().getId());

                if(justificantePlugin != null) {

                    // Generamos el pdf del Justificante
                    ByteArrayOutputStream baos = justificantePlugin.generarJustificante(registroSalida);

                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = anexoEjb.crearJustificante(usuario, idRegistro, tipoRegistro.toLowerCase(), baos);
                    registroDetalle.getAnexos().add(anexoFull.getAnexo());
                }

            }

            // Actualizamos el Registro con campos SIR
            registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
            registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroSalida.getOficina().getCodigo()));
            registroDetalle.setCodigoEntidadRegistralDestino(codigoEntidadRegistralDestino);
            registroDetalle.setDecodificacionEntidadRegistralDestino(denominacionEntidadRegistralDestino);
            registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
            registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

            // Actualizamos el registro
            registroSalida = registroSalidaEjb.merge(registroSalida);

            // Datos del Oficio de remisión
            oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
            oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
            oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
            oficioRemision.setDestinoExternoDenominacion(registroSalida.interesadoDestinoDenominacion());
            oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
            oficioRemision.setOrganismoDestinatario(null);
            oficioRemision.setRegistrosEntrada(null);

            // Transformamos el RegistroSalida en un AsientoRegistralSir
            registroSalida = registroSalidaEjb.getConAnexosFullCompleto(oficioRemision.getRegistrosSalida().get(0).getId());
            asientoRegistralSir = asientoRegistralSirEjb.transformarRegistroSalida(registroSalida);

        }

        // Registramos el Oficio de Remisión SIR
        try {
            oficioRemision = oficioRemisionEjb.registrarOficioRemisionSIR(oficioRemision);

        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        // Enviamos el Registro al Componente CIR
        emisionEjb.enviarFicheroIntercambio(asientoRegistralSir);

        // Modificamos el estado del OficioRemision
        oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_ENVIADO);

        return oficioRemision;

    }

    @Override
    public void reintentarEnvios(Long idEntidad) {

        try {

            List<OficioRemision> oficios = oficioRemisionEjb.getByEstado(RegwebConstantes.OFICIO_SIR_ENVIADO, idEntidad);

            log.info("Hay " + oficios.size() + " pendientes de volver a enviar al nodo CIR");

            for (OficioRemision oficio : oficios) {

                if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){

                    // Transformamos el RegistroEntrada en un AsientoRegistralSir
                    RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFullCompleto(oficio.getRegistrosEntrada().get(0).getId());
                    AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.transformarRegistroEntrada(registroEntrada);

                    // Enviamos el Registro al Componente CIR
                    log.info("Reintentando envio a: " + asientoRegistralSir.getDecodificacionEntidadRegistralDestino());
                    emisionEjb.enviarFicheroIntercambio(asientoRegistralSir);

                    // Contabilizamos los reintentos
                    oficio.setNumeroReintentos(oficio.getNumeroReintentos() + 1);
                    oficio.setFechaEstado(new Date());

                    oficioRemisionEjb.merge(oficio);


                }else if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

                }
            }

        } catch (I18NException e) {
            e.printStackTrace();
        }catch (Exception e){
            log.info("Error al reintenar el envio");
            e.printStackTrace();
        }

    }

    public void reenviarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {


        //Actualizamos la oficina destino con la escogida por el usuario
        asientoRegistralSir.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
        asientoRegistralSir.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());

        //Actualizamos la oficina de origen con la oficina activa
        asientoRegistralSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
        asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

        //Actualizamos la unidad de tramitación destino con el organismo responsable de la oficina de reenvio
        asientoRegistralSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());

        //Modificamos usuario, contacto, aplicacion
        asientoRegistralSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        asientoRegistralSir.setNombreUsuario(usuario.getNombreCompleto());
        asientoRegistralSir.setContactoUsuario(usuario.getEmail());
        asientoRegistralSir.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
        asientoRegistralSir.setDecodificacionTipoAnotacion(observaciones);

        // Actualizamos el AsientoRegistralSir
        asientoRegistralSirEjb.merge(asientoRegistralSir);

        // Enviamos el Registro al Componente CIR
        emisionEjb.reenviarFicheroIntercambio(asientoRegistralSirEjb.getAsientoRegistralConAnexos(asientoRegistralSir.getId()));

        // Modificamos el estado del AsientoRegistralSir
        asientoRegistralSirEjb.modificarEstado(asientoRegistralSir.getId(), EstadoAsientoRegistralSir.REENVIADO);

    }

    /**
     * Indica si el asiento registral se puede reenviar, en función de su estado
     * @param estado del asiento registral
     * @return
     */
    public boolean puedeReenviarAsientoRegistralSir(EstadoAsientoRegistralSir estado){
       return  estado.equals(EstadoAsientoRegistralSir.RECIBIDO) ||
               estado.equals(EstadoAsientoRegistralSir.DEVUELTO) ||
               estado.equals(EstadoAsientoRegistralSir.REENVIADO) ||
               estado.equals(EstadoAsientoRegistralSir.REENVIADO_Y_ERROR);

    }

    /**
     *
     * @param asiento
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    @Override
    public void rechazarAsientoRegistralSir(AsientoRegistralSir asiento, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {

        // Modificamos la oficina destino con la de inicio
        asiento.setCodigoEntidadRegistralDestino(asiento.getCodigoEntidadRegistralInicio());
        asiento.setDecodificacionEntidadRegistralDestino(asiento.getDecodificacionEntidadRegistralInicio());

        // Modificamos la oficina de origen con la oficina activa
        asiento.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
        asiento.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

        // Modificamos usuario, contacto, aplicacion
        asiento.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        asiento.setNombreUsuario(usuario.getNombreCompleto());
        asiento.setContactoUsuario(usuario.getEmail());

        asiento.setTipoAnotacion(TipoAnotacion.RECHAZO.getValue());
        asiento.setDecodificacionTipoAnotacion(observaciones);

        asiento = asientoRegistralSirEjb.merge(asiento);

        // Rechazamos el AsientoRegistralSir
        emisionEjb.rechazarFicheroIntercambio(asientoRegistralSirEjb.getAsientoRegistralConAnexos(asiento.getId()));

        // Modificamos el estado del AsientoRegistralSir
        asientoRegistralSirEjb.modificarEstado(asiento.getId(), EstadoAsientoRegistralSir.RECHAZADO);
    }

    /**
     * Acepta un AsientoRegistralSir, creando un Registro de Entrada o un Registro de Salida
     * @param asientoRegistralSir
     * @throws Exception
     */
    @Override
    public RegistroEntrada aceptarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs)
            throws Exception {

        // Creamos y registramos el RegistroEntrada a partir del AsientoRegistral aceptado
        RegistroEntrada registroEntrada = null;
        try {
            registroEntrada = asientoRegistralSirEjb.transformarAsientoRegistralEntrada(asientoRegistralSir, usuario, oficinaActiva, idLibro, idIdioma, idTipoAsunto, camposNTIs);

            // CREAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
            trazabilidad.setAsientoRegistralSir(asientoRegistralSir);
            trazabilidad.setRegistroEntradaOrigen(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

            // Modificamos el estado del AsientoRegistralSir
            asientoRegistralSirEjb.modificarEstado(asientoRegistralSir.getId(), EstadoAsientoRegistralSir.ACEPTADO);

            // Enviamos el Mensaje de Confirmación
            mensajeEjb.enviarMensajeConfirmacion(asientoRegistralSir, registroEntrada.getNumeroRegistroFormateado());

            return registroEntrada;

        } catch (I18NException e) {
            e.printStackTrace();
        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     *
     * @param codOficinaOrigen
     * @return
     * @throws Exception
     */
    private String generarIdentificadorIntercambio(String codOficinaOrigen) {

        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits

        String identificador = codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + getIdToken(); //todo: Añadir secuencia real

        return identificador;
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
        long unsignedValue = Long.toString(now).hashCode() & 0xffffffffl;
        String result = Long.toString(unsignedValue);
        if (result.length() > 8) {
            result = result.substring(result.length() - 8, result.length());
        } else {
            result = String.format("%08d", unsignedValue);
        }
        return result;
    }

}
