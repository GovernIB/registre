package es.caib.regweb3.persistence.utils;


import es.caib.regweb3.plugins.distribucion.Destinatarios;

/**
 * Clase que representa la respuesta al distribuir un registro de entrada mediante el plugin de distribución
 * Nos sirve para tener en cuenta todos los posibles casos que hay.
 * <p/>
 * Created by mgonzalez on 18/05/2016.
 */
public class RespuestaDistribucion {

    private Destinatarios destinatarios; //Lista de los destinatarios a los que se debe distribuir
    private Boolean distribuido = false; //true si se ha enviado distribuido bien
    private Boolean encolado = false; //true si se ha enviado a la cola bien
    private Boolean envioMail = false; //envio con plugin de emails


    public RespuestaDistribucion() {
    }

    public Destinatarios getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(Destinatarios destinatarios) {
        this.destinatarios = destinatarios;
    }

    public Boolean getDistribuido() {
        return distribuido;
    }

    public void setDistribuido(Boolean distribuido) {
        this.distribuido = distribuido;
    }

    public Boolean getEncolado() {
        return encolado;
    }

    public void setEncolado(Boolean encolado) {
        this.encolado = encolado;
    }

    public Boolean getEnvioMail() {
        return envioMail;
    }

    public void setEnvioMail(Boolean envioMail) {
        this.envioMail = envioMail;
    }
}
