package es.caib.regweb.persistence.utils;

import java.util.Map;

/**
 * 
 * @author anadal
 * 
 */
public class DataBaseUtils {

  private static AbstractLike likeManager = new DefaultLike();

  public static abstract class AbstractLike {
    public abstract String like(String columnName, String variable,
        Map<String, Object> parametros, String value);
  }

  public static class OracleLike extends AbstractLike {
    public String like(String columnName, String variable, Map<String, Object> parametros,
        String value) {
      parametros.put(variable, "%" + value.toLowerCase() + "%");
      return " upper(convert(" + columnName + ", 'US7ASCII')) like upper(convert(:" + variable
          + ", 'US7ASCII')) ";
    }
  }

  public static class PostgreSQLLike extends AbstractLike {
    public String like(String columnName, String variable, Map<String, Object> parametros,
        String value) {

      // Convertir caracters < 128 a _ (qualsevol caràcter)
      StringBuffer newValue = new StringBuffer(value.length());

      for (int i = 0; i < value.length(); i++) {

        char charVal = value.charAt(i);
        if (((int) charVal) >= 128) {
          newValue.append('_');
        } else {
          if (charVal == 'a' || charVal == 'e' || charVal == 'i' || charVal == 'o'
              || charVal == 'u') {
            newValue.append('_');
          } else {
            newValue.append((char) charVal);
          }
        }
      }

      parametros.put(variable, "%" + newValue.toString().toLowerCase() + "%");

      return " upper(" + columnName + ") like upper(:" + variable + ")";
    }
  }

  public static class DefaultLike extends AbstractLike {
    public String like(String columnName, String variable, Map<String, Object> parametros,
        String value) {
      parametros.put(variable, "%" + value.toLowerCase() + "%");
      return " upper(" + columnName + ") like upper(:" + variable + ") ";
    }
  }

  public static void setLikeManager(AbstractLike likeManager) {
    DataBaseUtils.likeManager = likeManager;
  }

  public static String like(String columnName, String variable,
      Map<String, Object> parametros, String value) {

    if (value == null) {
      return " 1 = 1 "; // true condition
    }

    return likeManager.like(columnName, variable, parametros, value);

  }

}
