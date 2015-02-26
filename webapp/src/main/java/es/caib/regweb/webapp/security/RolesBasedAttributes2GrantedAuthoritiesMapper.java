package es.caib.regweb.webapp.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.Attributes2GrantedAuthoritiesMapper;

import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 5/02/14
 * Define el mapeo entre roles  J2EE y los roles internos de la aplicacion
 */
public class RolesBasedAttributes2GrantedAuthoritiesMapper implements Attributes2GrantedAuthoritiesMapper {

    protected final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    private Map baseRoleMapping = new HashMap();

    public void setBaseRoleMapping(@SuppressWarnings("rawtypes") Map baseRoleMapping) {
        this.baseRoleMapping = baseRoleMapping;
    }

    @SuppressWarnings("rawtypes")
    public Collection<GrantedAuthority> getGrantedAuthorities(Collection<String> attributes) {
        List<GrantedAuthority> gaList = new ArrayList<GrantedAuthority>();
        for (String attribute: attributes) {
            Object mapping = baseRoleMapping.get(attribute);
            if (mapping != null) {
                if (mapping instanceof Collection) {
                    for (Object obj: (Collection)mapping) {
                        if (obj != null)
                            gaList.add(new SimpleGrantedAuthority(obj.toString()));
                    }
                } else if (mapping != null) {
                    gaList.add(new SimpleGrantedAuthority(mapping.toString()));
                }
            } else {
                gaList.add(new SimpleGrantedAuthority(attribute));
            }
        }

        return gaList;
    }
}
