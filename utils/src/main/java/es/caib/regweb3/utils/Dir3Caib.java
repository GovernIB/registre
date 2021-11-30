package es.caib.regweb3.utils;

public class Dir3Caib {

    private String server;
    private String user;
    private String password;

    public Dir3Caib(String server, String user, String password) {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
