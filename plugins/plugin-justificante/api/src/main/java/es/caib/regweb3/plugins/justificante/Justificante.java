package es.caib.regweb3.plugins.justificante;

import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.utils.Metadata;

import java.util.List;


public class Justificante {

    protected DocumentCustody justificant;
    protected List<Metadata> metadades;

    public DocumentCustody getJustificant() {
        return justificant;
    }

    public void setJustificant(DocumentCustody justificant) {
        this.justificant = justificant;
    }

    public List<Metadata> getMetadades() {
        return metadades;
    }

    public void setMetadades(List<Metadata> metadades) {
        this.metadades = metadades;
    }
}
