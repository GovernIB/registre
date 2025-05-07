package es.caib.regweb3.persistence.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import es.caib.regweb3.model.utils.TramiteDto;

public class TramiteRowMapper {
   
    public static TramiteDto mapRow(ResultSet rs) throws SQLException {
    	TramiteDto tramite = new TramiteDto(rs.getString("PRO_CODSIA"), rs.getString("TPR_NOMBRE"));
        return tramite;
    }
    
}
