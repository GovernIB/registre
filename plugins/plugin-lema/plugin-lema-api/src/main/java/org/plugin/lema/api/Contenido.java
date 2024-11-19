package org.plugin.lema.api;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Contenido {

    protected String value;
    @XmlAttribute(name = "href", required = true)
    @XmlAttachmentRef
    protected DataHandler href;
    protected ContenidoConsulta contenido;
	protected String tipoMIME;
    
}
