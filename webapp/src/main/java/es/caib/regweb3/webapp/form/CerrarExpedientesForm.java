package es.caib.regweb3.webapp.form;

import es.caib.arxiudigital.apirest.facade.pojos.Expediente;

import java.util.ArrayList;
import java.util.List;

public class CerrarExpedientesForm {

    private List<String> uuids = new ArrayList<String>();

    public CerrarExpedientesForm() {
    }

    public CerrarExpedientesForm(List<Expediente> expedientes) {

        for (Expediente expediente : expedientes) {
            uuids.add(expediente.getId());
        }
    }


    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }
}
