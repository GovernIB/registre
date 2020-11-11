package es.caib.regweb3.persistence.integracion;


public class JustificanteArxiu {

    private String expediente;
    private String documento;

    public JustificanteArxiu(String expediente, String documento) {
        this.expediente = expediente;
        this.documento = documento;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
