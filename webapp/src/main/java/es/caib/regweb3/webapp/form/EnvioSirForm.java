package es.caib.regweb3.webapp.form;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class EnvioSirForm {


    private Long idRegistro;
    private String oficinaSIRCodigo;


    public EnvioSirForm() {
    }


    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }


    public String getOficinaSIRCodigo() {
        return oficinaSIRCodigo;
    }

    public void setOficinaSIRCodigo(String oficinaSIRCodigo) {
        this.oficinaSIRCodigo = oficinaSIRCodigo;
    }
}
