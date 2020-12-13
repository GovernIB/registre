package es.caib.regweb3.ws.model;

public class FileContentWs {

    FileInfoWs fileInfoWs;
    String error;
    byte[] data;
    String url;

    public FileInfoWs getFileInfoWs() {
        return fileInfoWs;
    }

    public void setFileInfoWs(FileInfoWs fileInfoWs) {
        this.fileInfoWs = fileInfoWs;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
