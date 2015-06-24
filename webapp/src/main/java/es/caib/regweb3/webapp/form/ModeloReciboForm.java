package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.ModeloRecibo;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created 9/04/14 13:01
 *
 * @author mgonzalez
 */
public class ModeloReciboForm {

  private ModeloRecibo modeloRecibo;
  private CommonsMultipartFile modelo;

  public ModeloReciboForm() {
  }

  public ModeloReciboForm(ModeloRecibo modeloRecibo) {
      this.modeloRecibo = modeloRecibo;
  }

  public ModeloRecibo getModeloRecibo() {
        return modeloRecibo;
    }

  public void setModeloRecibo(ModeloRecibo modeloRecibo) {
      this.modeloRecibo = modeloRecibo;
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
