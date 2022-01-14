package es.caib.regweb3.ws.utils;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author anadal
 */
public class UsuarioInfo {

    final Usuario usuario;

    final List<Entidad> entidades = new ArrayList<>();

    final List<Long> entidadesIDs = new ArrayList<>();

    final List<String> entidadesCodigoDir3 = new ArrayList<>();

    final Method method;

    /**
     * @param usuario
     * @param entidades
     */
    public UsuarioInfo(Usuario usuario, List<Entidad> entidades, Method method) {
        super();
        this.usuario = usuario;
        this.method = method;
        this.entidades.addAll(entidades);

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public List<Long> getEntidadesIDs() {
        return entidadesIDs;
    }


    public List<String> getEntidadesCodigoDir3() {
        return entidadesCodigoDir3;
    }

    public Method getMethod() {
        return method;
    }

    public void checkPerteneceAEntidad(String entidadCodigoDir3) throws I18NException {

        if (!entidadesCodigoDir3.contains(entidadCodigoDir3)) {

            throw new I18NException("error.ws.usrappnoestadentroentidad",
                    usuario.getIdentificador(), entidadCodigoDir3);
        }
    }

    public String getIdioma() {
        Long idioma = null;
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }

        String language;
        if (idioma == null) {
            language = Configuracio.getDefaultLanguage();
        } else {
            language = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma);
        }

        return language;
    }

}
