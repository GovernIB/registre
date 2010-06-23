package es.caib.regweb.model;



import javax.persistence.*;

/**

 */
@Entity
@Table(name = "BZMODREB")
public class ModeloRecibo implements java.io.Serializable {

	private String nombre;
	private String contentType;
	private byte[] datos;

	public ModeloRecibo() {
	}

	public ModeloRecibo(String nombre) {
		this.nombre = nombre;
	}

	public ModeloRecibo(String nombre, String contentType, byte[] datos) {
		this.nombre = nombre;
		this.contentType = contentType;
		this.datos = datos;
	}

	@Id
	@Column(name = "MOR_NOM", unique = true, nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "MOR_CONTYP", length = 32)
	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Lob
    @Column(name = "MOR_DATA", length=1048576)
	public byte[] getDatos() {
		return this.datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}

}
