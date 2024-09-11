package es.caib.regweb3.plugins.justificante;


import org.fundaciobit.pluginsib.core.v3.utils.Metadata;
import org.fundaciobit.pluginsib.documentcustody.api.DocumentCustody;

import java.util.List;

/**
 * 
 * @author jpernia
 *
 */
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
