package es.caib.regweb3.persistence.integracion;

import es.caib.plugins.arxiu.api.ContingutArxiu;

public class JustificanteArxiu {

    private ContingutArxiu expediente;
    private ContingutArxiu documento;

    public JustificanteArxiu(ContingutArxiu expediente, ContingutArxiu documento) {
        this.expediente = expediente;
        this.documento = documento;
    }

    public ContingutArxiu getExpediente() {
        return expediente;
    }

    public void setExpediente(ContingutArxiu expediente) {
        this.expediente = expediente;
    }

    public ContingutArxiu getDocumento() {
        return documento;
    }

    public void setDocumento(ContingutArxiu documento) {
        this.documento = documento;
    }
}
