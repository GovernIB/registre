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
    private Boolean enviado; //true si se ha enviado distribuido bien
    private Boolean enviadoCola; //true si se ha enviado a la cola bien
    private Boolean hayPlugin; //true si se ha especificado plugin.
    private Boolean listadoDestinatariosModificable; //True si se puede modificar la lista de destinatarios.

    public RespuestaDistribucion() {
    }

    public Destinatarios getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(Destinatarios destinatarios) {
        this.destinatarios = destinatarios;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Boolean getHayPlugin() {
        return hayPlugin;
    }

    public void setHayPlugin(Boolean hayPlugin) {
        this.hayPlugin = hayPlugin;
    }

    public Boolean getListadoDestinatariosModificable() {
        return listadoDestinatariosModificable;
    }

    public void setListadoDestinatariosModificable(Boolean listadoDestinatariosModificable) {
        this.listadoDestinatariosModificable = listadoDestinatariosModificable;
    }

    public Boolean getEnviadoCola() {
        return enviadoCola;
    }

    public void setEnviadoCola(Boolean enviadoCola) {
        this.enviadoCola = enviadoCola;
    }
}
