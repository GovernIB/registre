package es.caib.regweb3.model.utils;

public class AnexoSimple {

    private byte[] data;
    private String filename;

    public AnexoSimple(byte[] data, String filename) {
        this.data = data;
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
