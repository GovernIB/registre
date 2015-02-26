package es.caib.regweb.webapp.security;

import org.springframework.security.core.authority.mapping.MappableAttributesRetriever;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 5/02/14
 */
public class RolesBasedMappableAttributesRetriever implements MappableAttributesRetriever {

    private Set<String> defaultMappableAttributes;

    private Set<String> mappableAttributes = new HashSet<String>();


    public Set<String> getMappableAttributes() {
        refrescarMappableAttributes();
        return mappableAttributes;
    }

    public void setDefaultMappableAttributes(Set<String> defaultMappableAttributes) {
        this.defaultMappableAttributes = defaultMappableAttributes;
    }



    private void refrescarMappableAttributes() {
        mappableAttributes.clear();
        if (defaultMappableAttributes != null)
            mappableAttributes.addAll(defaultMappableAttributes);
    }
}
