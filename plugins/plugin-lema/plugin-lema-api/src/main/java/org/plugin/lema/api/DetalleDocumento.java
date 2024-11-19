package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalleDocumento {

    protected String nombre;
    protected Contenido contenido;
    protected HashDocumento hashDocumento;
    protected String mimeType;
    protected String metadatos;
    protected String enlaceDocumento;
    protected byte[] referenciaDocumento;
    protected byte[] referenciaPdfAcuse;
    protected String csvResguardo;
    
}
