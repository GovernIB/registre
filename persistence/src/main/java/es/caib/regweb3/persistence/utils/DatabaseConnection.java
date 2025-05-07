package es.caib.regweb3.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {

	public static Connection getConnection() throws SQLException {
		String url = PropiedadGlobalUtil.getJdbcRolsacUrl();
		String user = PropiedadGlobalUtil.getJdbcRolsacUsername();
		String password = PropiedadGlobalUtil.getJdbcRolsacPassword();
		
        return DriverManager.getConnection(url, user, password);
    }
	
}
