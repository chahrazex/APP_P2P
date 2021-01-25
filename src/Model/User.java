package Model;

import java.io.Serializable;

public class User implements  Serializable {
    String username;
    String email;
    String password;
    String ip;
    int port;
    String status ;

    public User(String username, String email, String password, String ip, int port, String status)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}