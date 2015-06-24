package es.caib.regweb3.webapp.editor;

import es.caib.regweb3.model.RegistroEntrada;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class RegistroEntradaEditor extends PropertyEditorSupport {

    protected final Log log = LogFactory.getLog(getClass());

    public RegistroEntradaEditor(){
        super();
    }

    public void setAsText(String text) throws IllegalArgumentException {

        RegistroEntrada registroEntrada = new RegistroEntrada();

        if(text != null && text.length() > 0){
            registroEntrada.setId(Long.parseLong(text));
        }

        setValue(registroEntrada);
    }
}
