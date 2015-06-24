package es.caib.regweb3.webapp.editor;

import es.caib.regweb3.model.Libro;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class LibroEditor extends PropertyEditorSupport {

    protected final Log log = LogFactory.getLog(getClass());

    public LibroEditor(){
        super();
    }

    public void setAsText(String text) throws IllegalArgumentException {

        Libro libro = new Libro();

        if(text != null && text.length() > 0){
            libro.setId(Long.parseLong(text));
        }

        setValue(libro);
    }
}
