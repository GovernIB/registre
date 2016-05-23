package es.caib.regweb3.sir.ws.api.utils;

import es.caib.regweb3.sir.core.model.IndicadorPrueba;
import es.caib.regweb3.sir.core.model.TipoMensaje;

import java.util.Date;
import java.util.List;

/**
 * Created by earrivi on 17/11/2015.
 */
public class Mensaje {

    /**
     * Código único de la entidad registral origen en el directorio común.
     * Obligatorio.
     */
    private String codigoEntidadRegistralOrigen;

    /**
     * Código único de la entidad registral destino en el directorio común.
     * Obligatorio.
     */
    private String codigoEntidadRegistralDestino;

    /**
     * Identificador de intercambio único de la operación. Obligatorio.
     */
    private String identificadorIntercambio;

    /**
     * Tipo de mensaje. Obligatorio.
     */
    private TipoMensaje tipoMensaje;

    /**
     * Texto descriptivo del mensaje. Opcional.
     */
    private String descripcionMensaje;

    /**
     * Número de registro de entrada en la entidad Registral destino. Utilizado
     * para completar el ciclo de envío. Opcional.
     */
    private String numeroRegistroEntradaDestino;

    /**
     * Fecha y hora de entrada en destino. Opcional.
     */
    private Date fechaEntradaDestino;

    /**
     * Indicador de prueba.
     */
    private IndicadorPrueba indicadorPrueba = IndicadorPrueba.NORMAL;

    /**
     * Lista de identificadores de ficheros de intercambio que se tienen que
     * reenviar en caso de error. Opcional.
     */
    private List<String> identificadoresFicheros;

    /**
     * Identificador del tipo de error que se ha producido durante el proceso de
     * intercambio. Opcional.
     */
    private String codigoError;

    /**
     * Constructor.
     */
    public Mensaje() {
        super();
    }

    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    public void setCodigoEntidadRegistralOrigen(
            String codigoEntidadRegistralOrigen) {
        this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
    }

    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    public void setCodigoEntidadRegistralDestino(
            String codigoEntidadRegistralDestino) {
        this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
    }

    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    public void setIdentificadorIntercambio(String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(TipoMensaje tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getDescripcionMensaje() {
        return descripcionMensaje;
    }

    public void setDescripcionMensaje(String descripcionMensaje) {
        this.descripcionMensaje = descripcionMensaje;
    }

    public String getNumeroRegistroEntradaDestino() {
        return numeroRegistroEntradaDestino;
    }

    public void setNumeroRegistroEntradaDestino(
            String numeroRegistroEntradaDestino) {
        this.numeroRegistroEntradaDestino = numeroRegistroEntradaDestino;
    }

    public Date getFechaEntradaDestino() {
        return fechaEntradaDestino;
    }

    public void setFechaEntradaDestino(Date fechaEntradaDestino) {
        this.fechaEntradaDestino = fechaEntradaDestino;
    }

    public List<String> getIdentificadoresFicheros() {
        return identificadoresFicheros;
    }

    public void setIdentificadoresFicheros(List<String> identificadoresFicheros) {
        this.identificadoresFicheros = identificadoresFicheros;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public IndicadorPrueba getIndicadorPrueba() {
        return indicadorPrueba;
    }

    public void setIndicadorPrueba(IndicadorPrueba indicadorPrueba) {
        this.indicadorPrueba = indicadorPrueba;
    }

}
