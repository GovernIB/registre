package es.caib.regweb3.ws.utils;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Locale;

/**
 * Veure https://cxf.apache.org/docs/interceptors.html
 *
 * @author anadal
 */

public class RegWebInInterceptor extends AbstractPhaseInterceptor<Message> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public RegWebInInterceptor() {
        // Veure https://cxf.apache.org/docs/interceptors.html
        super(Phase.PRE_INVOKE);
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Message message) throws Fault {

        boolean logEnable = true; // log.isDebugEnabled();
        Method method = null;

        SecurityContext context = message.get(SecurityContext.class);
        if (context == null || context.getUserPrincipal() == null) {
            log.error("S'ha cridat a l'API sense autenticar la petició.");
            return;
        }

        String userapp = context.getUserPrincipal().getName();

        if (logEnable) {
            log.debug(" ------------------ RegWebWSInInterceptor  --------------");

            try {

                method = getTargetMethod(message);

                log.debug("  + Method NAME = " + method.getName());
                log.debug("  + Method CLASS = " + method.getDeclaringClass());

                HttpServletRequest hsr = (HttpServletRequest) message.get("HTTP.REQUEST");
                log.debug(" USER:  " + hsr.getRemoteUser());

                if (hsr.isUserInRole(RegwebConstantes.RWE_SUPERADMIN)) log.debug(" ROLE: RWE_SUPERADMIN");
                if (hsr.isUserInRole(RegwebConstantes.RWE_ADMIN)) log.debug(" ROLE: RWE_ADMIN ");
                if (hsr.isUserInRole(RegwebConstantes.RWE_USUARI)) log.debug(" ROLE: RWE_USER ");
                if (hsr.isUserInRole(RegwebConstantes.RWE_WS_ENTRADA)) log.debug(" ROLE: RWE_WS_ENTRADA ");
                if (hsr.isUserInRole(RegwebConstantes.RWE_WS_SALIDA)) log.debug(" ROLE: RWE_WS_SALIDA ");
                if (hsr.isUserInRole(RegwebConstantes.RWE_WS_CIUDADANO)) log.debug(" ROLE: RWE_WS_CIUDADANO ");

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }


        Usuario usuariAplicacio;
        try {
            usuariAplicacio = EjbManager.getUsuarioEJB().findByIdentificador(userapp);
        } catch (Exception e) {
            log.error("Error instanciant UsuarioEJB " + e.getMessage());
            throw WsUtils.mountException(e.getMessage());
        }

        // Si estam a CAIB llavors s'ha autenticat i autoritzat contra seycon,
        // però a més hauria de tenir usuari app donat d'alta al RegWeb3
        if (usuariAplicacio == null) {
            // TODO Traduccio
            // LogicI18NUtils.tradueix(loc, code, args)
            final String msg = "L´usuari aplicació " + userapp
                    + " està autenticat en el domini de seguretat,"
                    + " però no esta donat d'alta dins RegWeb3.";

            log.error("RegWebInInterceptor::handleMessage(CAIB) ", new Exception(msg));

            throw WsUtils.mountException(msg);

        } else if (usuariAplicacio.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_PERSONA)) {
            final String msg = "L´usuari autenticat " + userapp
                    + " no es de tipus aplicació, i no pot utilitzar els web services.";

            log.error("RegWebInInterceptor::handleMessage(CAIB) ", new Exception(msg));

            throw WsUtils.mountException(msg);
        }


        // Obtenim les entitats a les que pertany
        List<Entidad> entitats;
        try {
            entitats = EjbManager.getUsuarioEntidadEJB().findByUsuario(usuariAplicacio.getId());
        } catch (Exception e) {
            log.error("Error instanciant UsuarioEJB " + e.getMessage());
            throw WsUtils.mountException(e.getMessage());
        }

        if (entitats.size() == 0) {
            throw WsUtils.mountException("L´usuari autenticat " + userapp
                    + " no està relacionat amb cap entitat.");
        }

        // Actualizamos los Roles del usuario aplicación
        try {

            IUserInformationPlugin loginPlugin = (IUserInformationPlugin) EjbManager.getPluginEJB().getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
            RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuariAplicacio.getIdentificador());

            EjbManager.getUsuarioEJB().actualizarRolesWs(usuariAplicacio, rolesInfo);


        } catch (Exception | I18NException e) {
            e.printStackTrace();
            log.info("Error intentando actualizar los Roles del usuario-aplicación: " + usuariAplicacio.getIdentificador());
        }

        UsuarioAplicacionCache.put(new UsuarioInfo(usuariAplicacio, entitats, method));

    }

    private Method getTargetMethod(Message m) {
        BindingOperationInfo bop = m.getExchange().get(BindingOperationInfo.class);
        MethodDispatcher md = (MethodDispatcher) m.getExchange().get(Service.class)
                .get(MethodDispatcher.class.getName());

        return md.getMethod(bop);
    }

    @Override
    public void handleFault(Message message) {

        Fault f = (Fault) message.getContent(Exception.class);

        log.error("RegWebInInterceptor::handleFault() - Code = " + f.getCode());
        log.error("RegWebInInterceptor::handleFault() - Msg = " + f.getMessage());

        Throwable cause = f.getCause();
        Long idioma = null;
        // Obtenir Idioma de l'usuari aplicacio

        if (UsuarioAplicacionCache.get() != null) {
            idioma = UsuarioAplicacionCache.get().usuario.getIdioma();
        }

        String language;
        if (idioma == null) {
            language = Configuracio.getDefaultLanguage();
        } else {
            language = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma);
        }

        log.error("RegWebInInterceptor::handleFault() - Cause = " + cause);

        if (cause != null) {
            log.error("RegWebInInterceptor::handleFault() - Cause Class = " + cause.getClass());
            if (cause instanceof UndeclaredThrowableException) {
                log.error("RegWebInInterceptor::handleFault() - Cause.UndeclaredThrowable");
                cause = ((UndeclaredThrowableException) cause).getUndeclaredThrowable();
            }
            if (cause instanceof I18NException) {
                log.error("RegWebInInterceptor::handleFault() - CAUSE.I18NException");

                I18NException i18n = (I18NException) cause;
                String msg = I18NLogicUtils.getMessage(i18n, new Locale(language));
                message.setContent(Exception.class,
                        // new WsI18NException(i18n.getTraduccio(), msg, cause));
                        new WsI18NException(WsUtils.convertToWsTranslation(i18n.getTraduccio()), msg,
                                cause));
            } else if (cause instanceof I18NValidationException) {
                log.error("RegWebInInterceptor::handleFault() - CAUSE.ValidationException");
                I18NValidationException ve = (I18NValidationException) cause;
                message.setContent(Exception.class,
                        WsUtils.convertToWsValidationException(ve, new Locale(language)));
            } else {
                log.error("RegWebInInterceptor::handleFault() - Cause.msg = " + cause.getMessage());
                log.error("RegWebInInterceptor::handleFault() - Cause.type = " + cause.getClass());
            }

        }

        super.handleFault(message);
    }
}