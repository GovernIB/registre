package es.caib.regweb3.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Propiedades {

    @Value("${es.caib.regweb3.sir.serverbase}")
    private String sirServerbase;

    @Value("${es.caib.regweb3.iscaib}")
    private Boolean isCaib;

    @Value("${es.caib.regweb3.showtimestamp}")
    private Boolean showtimestamp;

    @Value("${es.caib.regweb3.defaultlanguage}")
    private String defaultLanguage;

    @Value("${es.caib.regweb3.preregistre}")
    private String urlPreregistre;

    @Value("${es.caib.regweb3.archivos.path}")
    private String archivosPath;


    public String getSirServerBase() {
        return sirServerbase;
    }

    public Boolean isCAIB() {
        return isCaib;
    }

    public Boolean showTimeStamp() {
        return showtimestamp;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getUrlPreregistre() {
        return urlPreregistre;
    }

    public String getArchivosPath() {
        return archivosPath;
    }
}
