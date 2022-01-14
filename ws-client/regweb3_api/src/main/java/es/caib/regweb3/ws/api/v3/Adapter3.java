
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Time;

public class Adapter3
    extends XmlAdapter<String, Time>
{


    public Time unmarshal(String value) {
        return (org.fundaciobit.genapp.common.ws.WsTimeAdapter.parseTime(value));
    }

    public String marshal(Time value) {
        return (org.fundaciobit.genapp.common.ws.WsTimeAdapter.printTime(value));
    }

}
