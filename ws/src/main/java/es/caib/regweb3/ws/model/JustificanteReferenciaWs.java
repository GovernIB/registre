package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *
 */

@XmlRootElement
public class JustificanteReferenciaWs implements Serializable {

    private String csv;
    private String url;

    public JustificanteReferenciaWs() {
    }

    public JustificanteReferenciaWs(String csv, String url) {
        this.csv = csv;
        this.url = url;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


