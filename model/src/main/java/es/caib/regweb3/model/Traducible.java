package es.caib.regweb3.model;

import es.caib.regweb3.utils.RegwebConstantes;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 6/02/14
 */
@XmlTransient
public class Traducible implements Serializable {

    //todo añadir referencia Propiedades.getDefaultLanguage
    private final String defautLanguage = RegwebConstantes.IDIOMA_CATALAN_CODIGO;

    @XmlTransient
    protected Map<String, Traduccion> traducciones = new HashMap<String, Traduccion>();

    public Map<String, Traduccion> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map<String, Traduccion> traducciones) {
        this.traducciones = traducciones;
    }

    // Metodos publicos.

    /**
     * Conjunto de idiomas para los que hay traduccion.
     *
     * @return codigos de idioma para los que hay traduccion.
     */
    public Set<String> getLangs() {
        return this.traducciones.keySet();
    }


    /**
     * Obtiene la traduccion por defecto.
     *
     * @return La traduccion en el idioma per defecto.
     */
    public Traduccion getTraduccion() {
        return traducciones.get(defautLanguage);
    }

    /**
     * Obtiene la traduccion en un idioma determinado o <code>null</code>.
     *
     * @param idioma Idioma de la traduccion.
     * @return Traduccion en el idioma indicado o <code>null</code> si no existe.
     */
    public Traduccion getTraduccion(String idioma) {
        return traducciones.get(idioma);
    }


    /**
     * Fija una traduccion en un idioma determinado, o la borra si es <code>null</code>.
     *
     * @param idioma     Idioma de la traduccion,
     * @param traduccion La traduccion a fijar.
     */
    public void setTraduccion(String idioma, Traduccion traduccion) {
        if (traduccion == null) {
            traducciones.remove(idioma);
        } else {
            traducciones.put(idioma, traduccion);
        }
    }
}
