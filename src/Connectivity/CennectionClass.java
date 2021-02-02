package Connectivity;


import Model.Ressource;
import Model.User;

import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class CennectionClass {
    boolean trouve = false;

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        String dbName = "SP2P";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, password);
            //JOptionPane.showMessageDialog(null,"Done");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No connection");
        }

        return connection;
    }

    public static void main(String[] args) throws SQLException {
        getConnection();
    }

    public ArrayList<Ressource> getRessources() {
        CennectionClass cennectionClass = new CennectionClass();
        ArrayList<Ressource> list = new ArrayList<>();
        //ObservableList<Ressource> list = FXCollections.observableArrayList() ;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM annuair");
            while (set.next()) {
                list.add(new Ressource(
                        set.getString("filename"),
                        set.getString("username"),
                        set.getString("ip"),
                        set.getInt("port"),
                        set.getString("status"),
                        set.getString("pathFile")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*-------------------------------------------Login ------------------------------------------------*/
    public boolean Login(String username, String password) {
        CennectionClass cennectionClass = new CennectionClass();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users");
            while (set.next() & !trouve) {
                if (username.equals(set.getString("username")) && password.equals(set.getString("password"))) {
                    trouve = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (trouve) {

            //System.out.println(trouve);
            //JOptionPane.showMessageDialog(null, "User exist");
            updateSatusTrue(username);
            return true;

        } else {
            //System.out.println(trouve);
            //JOptionPane.showMessageDialog(null, "User n'exist pas");
            return false;
        }

    }

    /*------------------------------------------------Add new User in Database--------------------------------*/

    public boolean Add(String table, String name, String email, String password, String ip, int port, String status) {
        CennectionClass cennectionClass = new CennectionClass();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.execute("INSERT INTO " + table + "(username,email,password,ip,port,status) VALUES ('" + name + "','" + email + "','" + password + "','" + ip + "','" + port + "','" + status + "')");
        } catch (SQLException e) {
            return true;
        }
    }

    /*-------------------------------------------------------------------------------*/
    public boolean AddFile(String table, String user, File file, String ip, int port, String status) {
        CennectionClass cennectionClass = new CennectionClass();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.execute("INSERT INTO " + table + "(username,filename,ip,port,status,pathFile) VALUES ('"
                    + user + "','" + file.getName() + "','" + ip + "','" + port + "','" + status + "','" + file.getPath() + "')");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Number or Name of Client exist");
            return true;
        }
    }

    /*---------------------------------------------------------------------------------------------*/
    public User SearchUserInfo(String name) {
        User user = null;
        CennectionClass cennectionClass = new CennectionClass();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users WHERE username = '" + name + "'");

            while (set.next()) {
                user = new User(set.getString("username"), set.getString("email"),
                        set.getString("password"), set.getString("ip"),
                        set.getInt("port"), set.getString("status"));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    /*---------------------------------------------------------------------------------------------*/
    public Ressource SearchFile(String filename) {
        Ressource ressource = null;
        CennectionClass cennectionClass = new CennectionClass();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM annuair WHERE filename = '" + filename + "'");

            while (set.next()) {
                ressource = new Ressource(set.getString("filename"),
                        set.getString("username"),
                        set.getString("ip"),
                        set.getInt("port"),
                        set.getString("status"),
                        set.getString("pathfile"));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ressource;
    }
    /*---------------------------------------------------------------------------------------------*/

    public void updateSatusFalse(String cond) {
        CennectionClass cennectionClass = new CennectionClass();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE users SET status ='Offline' WHERE username = '" + cond + "'");
            statement.executeUpdate("UPDATE annuair SET status ='Offline' WHERE username = '" + cond + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSatusTrue(String cond) {
        CennectionClass cennectionClass = new CennectionClass();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE users SET status ='En Ligne' WHERE username = '" + cond + "'");
            statement.executeUpdate("UPDATE annuair SET status ='En Ligne' WHERE username = '" + cond + "'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean Delete(String filename ,String nameuser) {

        String sql = null;

        sql = "DELETE FROM annuair WHERE filename='" + filename + "' AND username='"+nameuser+"'";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

    }

    public ArrayList<String> getUsers() {

        ArrayList<String> list = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users");
            while (set.next())
            {
                list.add(set.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*---------------------------------------------------------Count --------------------------------------*/
    public  int count(String filename) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  COUNT('filename') FROM annuair WHERE filename='"+filename+"'");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return Integer.parseInt(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
    /*---------------------------------------------------------------------------------------------------------------*/
    public ArrayList<String> getRessourcesDistact() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT  DISTINCT filename FROM annuair");
            while (set.next())
            {
                list.add(set.getString("filename")) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countUsers()
    {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  COUNT('username') FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return Integer.parseInt(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
    public int countRess()
    {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  COUNT('filename') FROM annuair");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return Integer.parseInt(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

}



