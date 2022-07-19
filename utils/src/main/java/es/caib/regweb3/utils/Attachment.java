package es.caib.regweb3.utils;

import javax.activation.DataSource;

/**
 * @author mgonzalez
 * @version 1
 * 01/07/2022
 */
public class Attachment {

    private DataSource dataSource;
    private String filename;

    public Attachment(DataSource dataSource, String filename) {
        this.dataSource = dataSource;
        this.filename = filename;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
