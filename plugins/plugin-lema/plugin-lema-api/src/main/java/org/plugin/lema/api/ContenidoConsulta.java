package org.plugin.lema.api;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContenidoConsulta {
    
	protected List<Serializable> content;
    protected String contentType;
    
}
