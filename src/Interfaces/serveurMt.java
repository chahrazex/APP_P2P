package Interfaces;

import Connectivity.CennectionClass;
import Model.Ressource;
import Model.User;
import Model.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class serveurMt extends Thread
{
    public static int nbrClients ;

    public static void main(String[] args)
    {
        new serveurMt().start();
    }


    @Override
    public void run()
    {
        try
        {
            ServerSocket ss =new ServerSocket(9191) ;
            System.out.println("Démarage de Serveur ......");

            while (true)
            {
                Socket s=ss.accept() ;
                ++nbrClients;
                new Conversation(s, nbrClients).start();
            }

        }
        catch (IOException e)
        {
           System.out.println(e.getMessage());
        }
    }
    static class Conversation extends Thread
    {

        public Socket socket ;
        public  PrintWriter pw ;
        public int num ;
        boolean result ;

        public Conversation(Socket socket,int num)
        {
            this.socket=socket ;
            this.num=num ;
        }

        @Override
        public void run()
        {
            try {
                InputStream is =socket.getInputStream() ;
                InputStreamReader isr =new InputStreamReader(is) ;
                BufferedReader bis=new BufferedReader(isr) ;



                OutputStream os=socket.getOutputStream() ;
                pw =new PrintWriter(os,true) ;
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ObjectInputStream ois = new ObjectInputStream(is);

                String Ip =socket.getRemoteSocketAddress().toString() ;

                System.out.println("Connexion de client numéro :"+num+" , IP : "+Ip);
                pw.println(num);

                /*---Reception d'un client ---------------------------------*/
                while (true)
                {
                    int acces = Integer.parseInt(bis.readLine());

                    switch (acces)
                    {
                        case 1 :
                            System.out.println("User "+num+ "want to signUp");
                            User user = (User) ois.readObject();//Read user
                            pw.println(signUp(user));
                            break;
                        case 2 :
                            System.out.println("User "+num+ "want to signIn");
                            User user2 = (User) ois.readObject();//Read user
                            result = signIn(user2);
                            pw.println(result);
                            break;
                        case 3 :
                            String nameFile = bis.readLine() ;
                            System.out.println("User "+num+ "search file : "+nameFile);
                            oos.writeObject(SearchFile(nameFile));
                            break;
                        case 4 :
                            File f = new File(bis.readLine());
                            System.out.println("User "+num+ "share file : "+f.getName());
                            String username = bis.readLine() ;
                            Enregistre(f,username);
                            break;
                        case 5 :
                            String name = bis.readLine() ;
                            System.out.println("User "+num+ " Logout .");
                            logOut(name) ;
                            break;
                        case 6 :
                            String uName = bis.readLine();
                            CennectionClass cennectionClass=new CennectionClass() ;
                            User user1 = cennectionClass.SearchUserInfo(uName) ;
                            pw.println(user1.getPort());
                            break;
                        case 7:
                            oos.writeObject(getListRess());
                            break;
                        case 8:
                            String fileDelet = bis.readLine();
                            String nameDelte = bis.readLine() ;
                            pw.println(DeletFile(fileDelet,nameDelte));
                            break;
                        case 9:
                            oos.writeObject(getListUsers());
                            break;
                        case 10 :
                            String connecUser = bis.readLine();
                            oos.writeObject(getUserInfo(connecUser) );
                            break;
                        case 11:
                            ArrayList<String> ressources = getListRessDis() ;
                            Vector<file> files = new Vector<>();
                            for (int i =0 ; i<ressources.size();i++)
                            {
                                file f1 = new file(ressources.get(i),count(ressources.get(i)));
                                files.add(f1) ;
                            }
                            oos.writeObject(files);
                            break;
                        case 12 :
                            pw.println(countUsers());
                            break;
                        case 13 :
                            pw.println(countRess());
                            break;



                    }
                }

            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }


        }
        public boolean signUp(User user)
        {
            boolean result = true;
            try
            {
                CennectionClass cennectionClass=new CennectionClass() ;
                result = cennectionClass.Add("users",user.getUsername(),user.getEmail(),
                        user.getPassword(),user.getIp(),user.getPort(),user.getStatus()) ;
                System.out.println(result);
            }
            catch (Exception e) {

            }
            return result ;
        }
        public boolean signIn(User user)
        {
            boolean result = false;
            try
            {
                CennectionClass cennectionClass=new CennectionClass() ;
                result = cennectionClass.Login(user.getUsername(),user.getPassword()) ;
            }
            catch (Exception e) {}

            return result ;

        }
        /*------------------------------------------------------------------*/
        public Ressource SearchFile(String filename)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            Ressource res = cennectionClass.SearchFile(filename) ;
            return res ;
        }
        /*-------------------------------------------------------------------------*/
        public void  Enregistre(File f ,String name)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            System.out.println(name);
            User user = cennectionClass.SearchUserInfo(name);
            result = cennectionClass.AddFile("annuair",user.getUsername(),f,user.getIp(),user.getPort(),user.getStatus()) ;

        }
        public ArrayList<Ressource> getListRess()
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return  cennectionClass.getRessources();
        }
        /*----------------------------------------------------------------------------------*/
        public ArrayList<String> getListRessDis()
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return  cennectionClass.getRessourcesDistact();
        }
        /*------------------------------------------------------------------------------------*/
        public void logOut(String name)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            cennectionClass.updateSatusFalse(name );
        }
        /*-----------------------------------------------------------------------------------*/
        public boolean DeletFile(String filename,String nameuser)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return  cennectionClass.Delete(filename,nameuser);
        }
        /*-----------------------------------------------------------------------------------*/
        public ArrayList<String> getListUsers()
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return  cennectionClass.getUsers();
        }
        /*----------------------------------------------------------------------------------------*/
        public User getUserInfo(String username)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return cennectionClass.SearchUserInfo(username );

        }
        /*----------------------------------------------------------------------------------------*/
        public int count(String filename)
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return cennectionClass.count(filename) ;
        }
        /*----------------------------------------------------------------------------------------*/
        public int countUsers()
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return cennectionClass.countUsers() ;
        }
        /*----------------------------------------------------------------------------------------*/
        public int countRess()
        {
            CennectionClass cennectionClass=new CennectionClass() ;
            return cennectionClass.countRess() ;
        }




    }

}
