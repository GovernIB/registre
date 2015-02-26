package es.caib.regweb.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author anadal
 *
 */
public class RegwebUtils {

  
  public static <V, K> Map<V, K> invert(Map<K, V> map) {

    Map<V, K> inv = new HashMap<V, K>();

    for (Entry<K, V> entry : map.entrySet())
        inv.put(entry.getValue(), entry.getKey());

    return inv;
}
  
}
