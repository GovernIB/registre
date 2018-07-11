package es.caib.regweb3.webapp.form;


/**
 * Created by Fundació Bit
 * @author jpernia
 */
public class AnularForm {

    private Long idAnular;
    private String observacionesAnulacion;


    public AnularForm() {
    }

    public Long getIdAnular() {
        return idAnular;
    }

    public void setIdAnular(Long idAnular) {
        this.idAnular = idAnular;
    }

    public String getObservacionesAnulacion() {
        return observacionesAnulacion;
    }

    public void setObservacionesAnulacion(String observacionesAnulacion) {
        this.observacionesAnulacion = observacionesAnulacion;
    }
}