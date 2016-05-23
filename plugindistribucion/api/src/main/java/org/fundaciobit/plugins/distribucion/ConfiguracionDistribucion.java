package org.fundaciobit.plugins.distribucion;

/**
 * Created by mgonzalez on 09/05/2016.
 * Clase que especifica la configuración de la distribución de los asientos registrales
 */
public class ConfiguracionDistribucion {

    /**
     * Configura si el usuario de registro  puede escoger o modificar el listado de destinatarios a quien enviar el registro
     */
    public boolean listadoDestinatariosModificable;

    //especifica que información se enviará en el segemento de anexo del registro de entrada.
    /* 1 = custodiaId + metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
    *  2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
    *  3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
    * */
    public int configuracionAnexos;


    public ConfiguracionDistribucion(boolean listadoDestinatariosModificable, int configuracionAnexos) {
        this.listadoDestinatariosModificable = listadoDestinatariosModificable;
        this.configuracionAnexos = configuracionAnexos;

    }

    public boolean isListadoDestinatariosModificable() {
        return listadoDestinatariosModificable;
    }

    public void setListadoDestinatariosModificable(boolean listadoDestinatariosModificable) {
        this.listadoDestinatariosModificable = listadoDestinatariosModificable;
    }

    public int getConfiguracionAnexos() {
        return configuracionAnexos;
    }

    public void setConfiguracionAnexos(int configuracionAnexos) {
        this.configuracionAnexos = configuracionAnexos;
    }


}
