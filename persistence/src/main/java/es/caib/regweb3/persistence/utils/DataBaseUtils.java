package es.caib.regweb3.persistence.utils;

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

      parametros.put(variable, "%" + value + "%");
      return "upper(translate(" + columnName + ",'ÁÉÍÓÚáéíóúÀÈÌÒÙàèìòù','AEIOUaeiouAEIOUaeiou')) like upper(translate(:" + variable + ",'ÁÉÍÓÚáéíóúÀÈÌÒÙàèìòù','AEIOUaeiouAEIOUaeiou'))";
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
