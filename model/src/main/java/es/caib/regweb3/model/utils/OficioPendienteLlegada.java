package es.caib.regweb3.model.utils;

/**
 * Created on 18/09/14.
 * @author earrivi
 * Bean para representar un OficioPendiente de Llegada y el Libro donde se creará su entrada
 */
public class OficioPendienteLlegada {

    private Long idRegistroEntrada;
    private Long idLibro;

    public OficioPendienteLlegada() {
    }

    public OficioPendienteLlegada(Long idRegistroEntrada) {
        this.idRegistroEntrada = idRegistroEntrada;
    }

    public Long getIdRegistroEntrada() {
        return idRegistroEntrada;
    }

    public void setIdRegistroEntrada(Long idRegistroEntrada) {
        this.idRegistroEntrada = idRegistroEntrada;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }
}
