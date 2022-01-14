package es.caib.regweb3.ws.utils;

import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;

/**
 * @author anadal
 */
public final class EjbManager {

    protected static final Logger log = LoggerFactory.getLogger(EjbManager.class);


    protected static UsuarioEntidadLocal usuariEntitatEjb;
    protected static UsuarioLocal usuariEjb;
    protected static PluginLocal pluginEjb;


    public static UsuarioEntidadLocal getUsuarioEntidadEJB() throws Exception {

        if (usuariEntitatEjb == null) {

            usuariEntitatEjb = (UsuarioEntidadLocal) new InitialContext().lookup(UsuarioEntidadLocal.JNDI_NAME);

        }
        return usuariEntitatEjb;
    }


    public static UsuarioLocal getUsuarioEJB() throws Exception {

        if (usuariEjb == null) {

            usuariEjb = (UsuarioLocal) new InitialContext().lookup(UsuarioLocal.JNDI_NAME);

        }
        return usuariEjb;

    }

    public static PluginLocal getPluginEJB() throws Exception {

        if (pluginEjb == null) {

            pluginEjb = (PluginLocal) new InitialContext().lookup(PluginLocal.JNDI_NAME);

        }
        return pluginEjb;
    }


}
