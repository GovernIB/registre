package es.caib.regweb3.webapp.editor;

import es.caib.regweb3.model.Organismo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class OrganismoEditor extends PropertyEditorSupport {

    protected final Log log = LogFactory.getLog(getClass());

    public OrganismoEditor(){

        super();
    }

    public void setAsText(String text) throws IllegalArgumentException {

        Organismo organismo = new Organismo();

        if(text != null && text.length() > 0){
            organismo.setId(Long.parseLong(text));
        }

        setValue(organismo);
    }
}
