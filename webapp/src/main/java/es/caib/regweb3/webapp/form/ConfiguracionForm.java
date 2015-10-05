package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Configuracion;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 07/07/15
 */
public class ConfiguracionForm {

    private Configuracion configuracion;
    private CommonsMultipartFile logoMenu;
    private CommonsMultipartFile logoPie;
    private boolean borrarLogoPie;
    private boolean borrarLogoMenu;

    public ConfiguracionForm() {
    }

    public ConfiguracionForm(Configuracion configuracion) {this.configuracion = configuracion;}

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public CommonsMultipartFile getLogoMenu() {
        return logoMenu;
    }

    public void setLogoMenu(CommonsMultipartFile logoMenu) {

        if(logoMenu != null && !logoMenu.isEmpty()){
            this.logoMenu = logoMenu;
        }
    }

    public CommonsMultipartFile getLogoPie() {
        return logoPie;
    }

    public void setLogoPie(CommonsMultipartFile logoPie) {

        if(logoPie != null && !logoPie.isEmpty()){
            this.logoPie = logoPie;
        }
    }

    public boolean isBorrarLogoPie() {
        return borrarLogoPie;
    }

    public void setBorrarLogoPie(boolean borrarLogoPie) {
        this.borrarLogoPie = borrarLogoPie;
    }

    public boolean isBorrarLogoMenu() {
        return borrarLogoMenu;
    }

    public void setBorrarLogoMenu(boolean borrarLogoMenu) {
        this.borrarLogoMenu = borrarLogoMenu;
    }

}