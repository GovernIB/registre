package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_ARCHIVO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ARCHIVO_SEQ", allocationSize = 1)
public class Archivo implements Serializable {

    private Long id;
    private String nombre;
    private String mime;
    private Long tamano;
    private boolean borrar = false;

    public Archivo(){}

    public Archivo(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="NOMBRE",nullable=false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name="MIME",nullable=false)
    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    @Column(name="TAMANO",nullable=false)
    public Long getTamano() {
        return tamano;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

    /*@Lob
    @Column(name="DATOS", nullable=false)
    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }*/

    /**
     * A partir de un {@link java.io.InputStream} rellena los campos datos y peso
     *
     * @param inputStream Entrada de datos
     * @throws java.io.IOException
     */
    @Transient
    public void setInputStream(InputStream inputStream) throws IOException {
        byte[] datos = new byte[inputStream.available()];
        inputStream.read(datos);
        //setDatos(datos);
        setTamano(Long.valueOf(datos.length));
    }

    @Transient
    public boolean isBorrar() {
        return borrar;
    }

    public void setBorrar(boolean borrar) {
        this.borrar = borrar;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Archivo)) return false;

        Archivo archivo = (Archivo) o;

        return id.equals(archivo.id);
    }

}
