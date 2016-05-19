package es.caib.regweb3.persistence.utils;

import org.fundaciobit.plugins.distribucion.Destinatarios;

/**
 * Clase que representa la respuesta al distribuir un registro de entrada mediante el plugin de distribución
 * <p/>
 * Created by mgonzalez on 18/05/2016.
 */
public class RespuestaDistribucion {

    Destinatarios destinatarios; //Lista de los destinatarios a los que se debe distribuir
    Boolean enviado; //true si se ha distribuido(enviado) bien y false si no.
    Boolean hayPlugin; //true si se ha especificado plugin.
    // Boolean listado; //true si se ha marcado la opcion listado en el plugin


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

   /* public Boolean getListado() {
        return listado;
    }

    public void setListado(Boolean listado) {
        this.listado = listado;
    }*/
}
