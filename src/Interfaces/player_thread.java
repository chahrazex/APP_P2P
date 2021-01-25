package Interfaces;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class player_thread extends Thread
{

    public SourceDataLine audio_out = null ;
    public DatagramSocket din ;

    byte[] byte_buffer = new byte[1200] ;


    @Override
    public void run()
    {
        int i = 0 ;
        DatagramPacket incoming =  new DatagramPacket(byte_buffer,byte_buffer.length) ;
        while (MainController.Calling)
        {

            try
            {
                din.receive(incoming);
                byte_buffer = incoming.getData();
                audio_out.write(byte_buffer,0,byte_buffer.length);
                System.out.println("S "+ i++);
            }
            catch (IOException e)
            {
                break;
            }
        }
        audio_out.close();
        audio_out.drain();
        din.close();
        System.out.println("Thread Stop");
    }
}
