package es.caib.regweb3.persistence.utils;

import java.util.Date;

public class NumeroRegistro {

    private Integer numero;
    private Date fecha;

    public NumeroRegistro() {}

    public NumeroRegistro(Integer numero, Date fecha) {
        this.numero = numero;
        this.fecha = fecha;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}