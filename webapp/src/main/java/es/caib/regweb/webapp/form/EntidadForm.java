package es.caib.regweb.webapp.form;

import es.caib.regweb.model.Entidad;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 21/03/14
 */
public class EntidadForm {

    private Entidad entidad;
    private CommonsMultipartFile logoMenu;
    private CommonsMultipartFile logoPie;
    private boolean borrarLogoPie;
    private boolean borrarLogoMenu;

    public EntidadForm() {
    }

    public EntidadForm(Entidad entidad) {
        this.entidad = entidad;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
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
