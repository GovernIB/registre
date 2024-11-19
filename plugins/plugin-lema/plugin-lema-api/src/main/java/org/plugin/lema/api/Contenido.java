package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Contenido {

    protected String value;
//    @XmlAttribute(name = "href", required = true)
//    @XmlAttachmentRef
//    protected DataHandler href;
    private String base64;
    protected ContenidoConsulta contenido;
	protected String tipoMIME;
    
}
