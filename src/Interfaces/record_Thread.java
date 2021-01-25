package Interfaces;

import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class record_Thread extends Thread {
    public TargetDataLine audio_in = null;
    public DatagramSocket dout;

    public InetAddress server_ip;
    public int server_port;
    public static double amplification = 1.0;

    @Override
    public void run() {
        int i = 0;
        while (MainController.Calling)
        {
            if (audio_in.available() >=1200) { //we got enough data to send
                byte[] byte_buffer = new byte[1200];
                DatagramPacket data ;
                audio_in.read(byte_buffer, 0, byte_buffer.length);

                long tot = 0;
                for (int j = 0; j < byte_buffer.length; j++) {
                    byte_buffer[j] *= amplification;
                    tot += Math.abs(byte_buffer[j]);
                }
                tot *= 2.5;
                tot /= byte_buffer.length;
                //create and send packet

                if (tot == 0) {//send empty packet
                    byte[] buffer = new byte[1200];
                     data = new DatagramPacket(buffer, 0, server_ip, server_port);
                    try
                    {
                        dout.send(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { //send data
                     data = new DatagramPacket(byte_buffer, byte_buffer.length, server_ip, server_port);
                    try {
                        dout.send(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        audio_in.close();
        audio_in.drain();
        dout.close();

        System.out.println("Thread Stop");
    }
}
