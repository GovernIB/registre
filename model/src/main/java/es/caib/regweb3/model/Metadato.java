package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author mgonzalez
 * @version 1
 * 02/09/2022
 */
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@Table(name = "RWE_METADATO")
@XmlRootElement(name = "metadato")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadato implements Serializable {


    private Long id;
    private String campo;
    private String valor;
    private Anexo anexoGeneral;
    private Anexo anexoParticular;
    private RegistroDetalle registroDetalleGeneral;
    private RegistroDetalle registroDetalleParticular;

    public Metadato(String campo, String valor) {
        this.campo = campo;
        this.valor = valor;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Metadato() {
    }


    @Column(name = "CAMPO" , length = 80)
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Column(name = "VALOR")
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANEXO", foreignKey = @ForeignKey(name = "RWE_META_ANEXO_FK"))
    @JsonIgnore
    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }*/

    @ManyToOne()
    @JoinColumn(name = "ANEXOGENERAL", foreignKey = @ForeignKey(name = "RWE_METADATO_ANEXGEN_FK"))
    public Anexo getAnexoGeneral() {
        return anexoGeneral;
    }

    public void setAnexoGeneral(Anexo anexoGeneral) {
        this.anexoGeneral = anexoGeneral;
    }


    @ManyToOne()
    @JoinColumn(name = "ANEXOPARTICULAR", foreignKey = @ForeignKey(name = "RWE_METADATO_ANEXPART_FK"))
    public Anexo getAnexoParticular() {
        return anexoParticular;
    }

    public void setAnexoParticular(Anexo anexoParticular) {
        this.anexoParticular = anexoParticular;
    }


    @ManyToOne()
    @JoinColumn(name = "REGDETGEN", foreignKey = @ForeignKey(name = "RWE_METADATO_REGDETGEN_FK"))
    public RegistroDetalle getRegistroDetalleGeneral() {
        return registroDetalleGeneral;
    }

    public void setRegistroDetalleGeneral(RegistroDetalle registroDetalleGeneral) {
        this.registroDetalleGeneral = registroDetalleGeneral;
    }

    @ManyToOne()
    @JoinColumn(name = "REGDETPART", foreignKey = @ForeignKey(name = "RWE_METADATO_REGDETPART_FK"))
    public RegistroDetalle getRegistroDetalleParticular() {
        return registroDetalleParticular;
    }

    public void setRegistroDetalleParticular(RegistroDetalle registroDetalleParticular) {
        this.registroDetalleParticular = registroDetalleParticular;
    }
}
