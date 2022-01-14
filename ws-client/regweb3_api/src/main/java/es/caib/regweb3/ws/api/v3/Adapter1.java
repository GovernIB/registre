
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;

public class Adapter1
    extends XmlAdapter<String, Timestamp>
{


    public Timestamp unmarshal(String value) {
        return (org.fundaciobit.genapp.common.ws.WsTimestampAdapter.parseDateTime(value));
    }

    public String marshal(Timestamp value) {
        return (org.fundaciobit.genapp.common.ws.WsTimestampAdapter.printDateTime(value));
    }

}
