package es.caib.regweb3.webapp.form;


/**
 * Created by Fundació Bit
 * @author jpernia
 */
public class EliminarForm {

    private Long id;
    private String observaciones;


    public EliminarForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}