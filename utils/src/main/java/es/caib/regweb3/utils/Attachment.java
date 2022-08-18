package es.caib.regweb3.utils;

import javax.activation.DataSource;

/**
 * @author mgonzalez
 * @version 1
 * 01/07/2022
 */
public class Attachment {

    private String filename;

    private byte[] data;
    private String mime;


    public Attachment(String filename, byte[] data, String mime) {
        this.filename = filename;
        this.data = data;
        this.mime = mime;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
