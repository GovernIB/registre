package es.caib.regweb3.webapp.utils;


import es.caib.regweb3.utils.RegwebConstantes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created 20/11/14 15:34
 *
 * @author mgonzalez
 */
public class RegwebUtils {


      /**
       * Función que formatea un Date en otro Date con el formato especificado
       * @param fecha
       * @param formato
       * @return
       * @throws Exception
       */
      public static Date formateaFecha(Date fecha, String formato) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(formato);
        String formattedDate= df.format(fecha);
        return df.parse(formattedDate);
      }

    public static boolean getVariableAnexosPluginDistribucion() throws Exception {
        //Cogemos la propiedad que nos indica si tenemos que enviar los anexos+archivos
        final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "distribucion.plugin";
        final String tipusStr = System.getProperty(propertyName);
        String basePlugin = propertyName + "." + tipusStr;
        String completePropertyName = basePlugin + ".conanexos";
        return Boolean.getBoolean(completePropertyName);
    }
}
