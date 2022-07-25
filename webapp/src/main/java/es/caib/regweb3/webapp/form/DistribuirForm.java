package es.caib.regweb3.webapp.form;

/**
 * @author mgonzalez
 * @version 1
 * 14/06/2022
 */
public class DistribuirForm {

    private String emails;
    private String motivo;


    public DistribuirForm() {
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
