package es.caib.regweb3.model.utils;

import java.io.Serializable;

/**
 * Bean básico para reutilizar
 * @author earrivi on 05/03/2015.
 */
public class ObjetoBasico implements Serializable{

    private Long id;
    private String nombre;

    public ObjetoBasico(Long id) {
        this.id = id;
    }

    public ObjetoBasico(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjetoBasico objetoBasico = (ObjetoBasico) o;

        if (!id.equals(objetoBasico.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
