package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DocumentoAnexo {

    protected String nombre;
    protected Contenido contenido;
    protected String mimeType;
    protected String metadatos;
    
}
