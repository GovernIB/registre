package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.ModeloOficioRemision;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created 2/09/14 14:04
 *
 * @author jpernia
 */
public class ModeloOficioRemisionForm {

    private ModeloOficioRemision modeloOficioRemision;
    private CommonsMultipartFile modelo;

    public ModeloOficioRemisionForm() {
    }

    public ModeloOficioRemisionForm(ModeloOficioRemision modeloOficioRemision) {
        this.modeloOficioRemision = modeloOficioRemision;
    }

    public ModeloOficioRemision getModeloOficioRemision() {
        return modeloOficioRemision;
    }

    public void setModeloOficioRemision(ModeloOficioRemision modeloOficioRemision) {
        this.modeloOficioRemision = modeloOficioRemision;
    }

    public CommonsMultipartFile getModelo() {
        return modelo;
    }

    public void setModelo(CommonsMultipartFile modelo) {

        if(modelo != null && !modelo.isEmpty()){
            this.modelo = modelo;
        }
    }

}