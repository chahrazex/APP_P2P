package Model;

import java.io.Serializable;

public class file implements Serializable
{
    String file ;
    int count ;

    public file(String file, int count) {
        this.file = file;
        this.count = count;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
