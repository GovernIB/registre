package es.caib.regweb.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Marilen
 * Date: 07-sep-2012
 * Time: 14:45:59
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class DocumentoCareoId implements Serializable {

    private int any;
    private int codigoOficina;
    private int codigoRegistro;
    private int codigoDocumento;

    public DocumentoCareoId(int any, int codigoOficina, int codigoRegistro, int codigoDocumento) {
        this.any = any;
        this.codigoOficina = codigoOficina;
        this.codigoRegistro = codigoRegistro;
        this.codigoDocumento = codigoDocumento;
    }

    public DocumentoCareoId() {
	}
    
    @Column(name = "LOC_ANY", nullable = false, length = 4)
    public int getAny() {
        return any;
    }

    public void setAny(int any) {
        this.any = any;
    }
    @Column(name = "LOC_OFI", nullable = false, length = 2)
    public int getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(int codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    @Column(name = "LOC_NUMREG", nullable = false, length = 5)
    public int getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(int codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
    @Column(name = "LOC_NUMDOC", nullable = false, length = 2)
    public int getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(int codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }
}
