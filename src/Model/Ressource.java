package Model;

import java.io.Serializable;

public class Ressource implements Serializable
{


    String fileName ;
    String peerName ;
    String ip ;
    int port;
    String Status ;
    String pathFile ;

    public Ressource(String fileName, String peerName, String ip, int port, String status, String pathFile) {
        this.fileName = fileName;
        this.peerName = peerName;
        this.ip = ip;
        this.port = port;
        Status = status;
        this.pathFile = pathFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
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
