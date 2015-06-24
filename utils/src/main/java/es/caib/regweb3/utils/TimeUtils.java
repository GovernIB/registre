package es.caib.regweb3.utils;

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

}
