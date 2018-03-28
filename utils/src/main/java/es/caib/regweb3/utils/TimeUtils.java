package es.caib.regweb3.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author anadal
 * 
 */
public class TimeUtils {

  public static String formatElapsedTime(final long l) {
    final long hr = TimeUnit.MILLISECONDS.toHours(l);
    final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
    final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr)
        - TimeUnit.MINUTES.toMillis(min));
    final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr)
        - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
    return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
  }

  public static String formatElapsedTimeShort(final long l) {
    final long hr = TimeUnit.MILLISECONDS.toHours(l);
    final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
    final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr)
            - TimeUnit.MINUTES.toMillis(min));
    final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr)
            - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
    return String.format("%02d:%02d.%03d", min, sec, ms);
  }



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
}
