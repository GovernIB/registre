package es.caib.regweb.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Marilen
 * Date: 07-sep-2012
 * Time: 14:36:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "BZDOCLOC")
@org.hibernate.annotations.Table(appliesTo = "BZDOCLOC", comment = "DocumentoCareo")
public class DocumentoCareo implements Serializable {

    private DocumentoCareoId id;

    public DocumentoCareo(DocumentoCareoId id) {
        this.id = id;
    }

    public DocumentoCareo() {
    }
   
    @EmbeddedId
	@AttributeOverrides( {
    @AttributeOverride(name = "any", column = @Column(name = "LOC_ANY", nullable = false, length = 4)),
    @AttributeOverride(name = "codigoOficina", column = @Column(name = "LOC_OFI", nullable = false, length = 2)),
    @AttributeOverride(name = "codigoRegistro", column = @Column(name = "LOC_NUMREG", nullable = false, length = 5)),
    @AttributeOverride(name = "codigoDocumento", column = @Column(name = "LOC_NUMDOC", nullable = false, length = 2)),
    @AttributeOverride(name = "tipo", column = @Column(name = "LOC_TIPUS", nullable = false, length = 1)) })
    public DocumentoCareoId getId() {
        return id;
    }

    public void setId(DocumentoCareoId id) {
        this.id = id;
    }
}
    