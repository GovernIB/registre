
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

public class Adapter3
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (org.fundaciobit.genapp.common.ws.WsSqlDateAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (org.fundaciobit.genapp.common.ws.WsSqlDateAdapter.printDate(value));
    }

}
