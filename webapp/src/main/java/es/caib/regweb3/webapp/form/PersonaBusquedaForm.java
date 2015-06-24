package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Persona;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 7/05/14
 */
public class PersonaBusquedaForm implements Serializable {

    private Persona persona;
    private Integer pageNumber;

    public PersonaBusquedaForm() {
    }

    public PersonaBusquedaForm(Persona persona, Integer pageNumber) {
        this.persona = persona;
        this.pageNumber = pageNumber;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
