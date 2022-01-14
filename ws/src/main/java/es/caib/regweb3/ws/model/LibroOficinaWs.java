package es.caib.regweb3.ws.model;

/**
 * Created by earrivi on 20/05/2015.
 */
public class LibroOficinaWs {

    LibroWs libroWs;
    OficinaWs oficinaWs;


    public LibroOficinaWs() {
    }

    public LibroOficinaWs(LibroWs libroWs, OficinaWs oficinaWs) {
        this.libroWs = libroWs;
        this.oficinaWs = oficinaWs;
    }

    public LibroWs getLibroWs() {
        return libroWs;
    }

    public void setLibroWs(LibroWs libroWs) {
        this.libroWs = libroWs;
    }

    public OficinaWs getOficinaWs() {
        return oficinaWs;
    }

    public void setOficinaWs(OficinaWs oficinaWs) {
        this.oficinaWs = oficinaWs;
    }
}
