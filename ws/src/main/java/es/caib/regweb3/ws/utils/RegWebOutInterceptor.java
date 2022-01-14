package es.caib.regweb3.ws.utils;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author anadal
 */
public class RegWebOutInterceptor extends AbstractPhaseInterceptor<Message> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public RegWebOutInterceptor() {
        // Veure https://cxf.apache.org/docs/interceptors.html
        super(Phase.SEND);
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Message message) throws Fault {
        UsuarioAplicacionCache.remove();
    }


}