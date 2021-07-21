package es.caib.regweb3.ws.model;

public class FileInfoWs {

    private Long fileID;
    private String name;
    private String mime;
    private Long size;
    private String filename;
    private String validezDocumento;
    private Boolean confidencial = false;
    private byte[] hash;

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getValidezDocumento() {
        return validezDocumento;
    }

    public void setValidezDocumento(String validezDocumento) {
        this.validezDocumento = validezDocumento;
    }

    public Boolean getConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
