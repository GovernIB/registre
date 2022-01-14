package es.caib.regweb3.ws.api.v3.utils;

import es.caib.regweb3.ws.api.v3.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author anadal
 * 
 */
public class WsClientUtils {

  public static String toString(WsValidationException ve) {
    StringBuffer str = new StringBuffer();
    str.append(ve.getMessage()).append("\n");
    int count = 1;
    for (WsFieldValidationError error : ve.getFaultInfo().getFieldFaults()) {
      str.append("  ------( " + count + " )-------\n");
      str.append("  MESSA: " + error.getError() + "\n");
      str.append("  FIELD: " + error.getField() + " (" + error.getLabel() + ")\n");
      str.append("  TRANS: " + toString(error.getTranslation())).append("\n");
      count++;
    }
    return str.toString();
  }

  public static String toString(WsI18NException i18ne) {
    WsI18NError error = i18ne.getFaultInfo();

    StringBuffer strAll = new StringBuffer();

    strAll.append("MESSAGE: ").append(i18ne.getMessage()).append("\n");
    strAll.append("TRANSLA: ").append(toString(error.getTranslation())).append("\n");

    return strAll.toString();

  }

  public static String toString(WsI18NTranslation trans) {
    StringBuffer strAll = new StringBuffer(trans.getCode());

    if (trans.getArgs() != null && trans.getArgs().size() != 0) {
      List<String> str = new ArrayList<String>();
      for (WsI18NArgument arg : trans.getArgs()) {
        str.add((arg.isTranslate() ? "*" : "") + arg.getValue());
      }
      strAll.append(" " + Arrays.toString(str.toArray()));
    }

    return strAll.toString();
  }

}
