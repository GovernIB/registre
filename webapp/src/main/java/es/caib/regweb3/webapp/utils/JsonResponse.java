package es.caib.regweb3.webapp.utils;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 21/05/14
 */
public class JsonResponse {

    private String status;
    private List<FieldError> errores;
    private Object result;
    private String error;

    public JsonResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FieldError> getErrores() {
        return errores;
    }

    public void setErrores(List<FieldError> errores) {
        this.errores = errores;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
