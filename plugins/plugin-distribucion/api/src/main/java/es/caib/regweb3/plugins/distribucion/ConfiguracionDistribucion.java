package es.caib.regweb3.plugins.distribucion;

/**
 * Created by mgonzalez on 09/05/2016.
 * Clase que especifica la configuración de la distribución de los asientos registrales
 */
public class ConfiguracionDistribucion {

    /*
        Estas dos propiedades se definen conjuntamente ya que se emplean para cuando los objetos van a una cola.
     */
    private int maxReintentos; //numero máximo de reintentos
    private boolean envioCola; // indicamos si queremos que los objetos vayan a una cola.


    public ConfiguracionDistribucion(boolean envioCola) {
        this.envioCola = envioCola;

    }


    public boolean isEnvioCola() {
        return envioCola;
    }

    public void setEnvioCola(boolean envioCola) {
        this.envioCola = envioCola;
    }
}
