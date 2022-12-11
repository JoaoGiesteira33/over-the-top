package streaming;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;



public class Encaminhador {//implements Runnable{

  List<InetAddress> ia_list;
    
  //GUI:
  //----------------
  JLabel label;

    //RTP variables:
  //----------------
  DatagramPacket rcvdp; //UDP packet received from the server (to receive)
  DatagramSocket RTPsocket_in; //socket to be used to send and receive UDP packet
  static int RTP_RCV_PORT = 25000; //port where the client will receive the RTP packets

  //RTP variables:
  //----------------
  DatagramPacket senddp; //UDP packet containing the video frames (to send)A
  DatagramSocket RTPsocket_out; //socket to be used to send and receive UDP packet
  int RTP_dest_port = 25000; //destination port for RTP packets 
  InetAddress ClientIPAddr; //Client IP address
  
  static String VideoFileName; //video file to request to the server

  //Video constants:
  //------------------
  int imagenb = 0; //image nb of the image currently transmitted
  VideoStream video; //VideoStream object used to access video frames
  static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video
  static int FRAME_PERIOD = 100; //Frame period of the video to stream, in ms
  static int VIDEO_LENGTH = 500; //length of the video in frames

  Timer sTimer; //timer used to send the images at the video frame rate
  byte[] sBuf; //buffer used to store the images to send to the client 

  Timer cTimer; //timer used to receive data from the UDP socket
  byte[] cBuf; //buffer used to store data received from the server 
  boolean first=true;

  //--------------------------
  //Constructor
  //--------------------------
  public Encaminhador(List<InetAddress> ia_list){ //IMPLEMENTAR THREADS NISTO

    //init para a parte do cliente
    //--------------------------

    cTimer = new Timer(20, new clientTimerListener());
    cTimer.setInitialDelay(1000);
    cTimer.setCoalesce(true);
    cBuf = new byte[15000]; //allocate enough memory for the buffer used to receive data from the server
    sBuf = new byte[15000];
    this.ia_list=ia_list;
    try{
      // socket e video
      RTPsocket_in = new DatagramSocket(RTP_RCV_PORT); //init RTP socket (o mesmo para o cliente e servidor)
      RTPsocket_in.setSoTimeout(4000); // setimeout to 10s
      while(true)
        cTimer.start();

    }catch(SocketException se){
      System.out.println("Erro ao receber: "+se.getMessage());
    }
    
  }

  //------------------------------------
  //main: args: Ips dos clientes
  //------------------------------------
  public static void main(String argv[]) 
  {  
    List<InetAddress> ia_list= new ArrayList<>();
    try{
      for(String s: argv){
        InetAddress ia = InetAddress.getByName(s);//("10.0.18.20");
        ia_list.add(ia);
        //Thread t = new Thread(e);
        //t.start();
      }
      Encaminhador e = new Encaminhador(ia_list);
        
    }
    catch(UnknownHostException e){
      System.out.println("ERRO: "+e);
    }
  }

  //------------------------------------
  //Handler for timer (para cliente)
  //------------------------------------
  
  class clientTimerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      //Datagram packet to store received packet
      rcvdp = new DatagramPacket(cBuf, cBuf.length);

      if(imagenb < VIDEO_LENGTH){
        try{
	          //receive the DP from the socket:
	          RTPsocket_in.receive(rcvdp);
		        imagenb++;

            //Received IPs in command line, starts a thread for each
            for(InetAddress ia: ia_list){
              Thread t = new Thread(){
                @Override
                public void run() {
                  //Update packet destination
                  rcvdp.setAddress(ia);
                  rcvdp.setPort(RTP_RCV_PORT);

                  //Send Packet
                  try{
                    RTPsocket_out = new DatagramSocket();
                    RTPsocket_out.send(rcvdp);
                    System.out.println("Send frame #"+imagenb+" to "+rcvdp.getAddress() + "in Port "+rcvdp.getPort());
                  }
                  catch(IOException e){
                    System.out.println("Erro: " + e.getMessage());
                  }
                }
              };
              t.start();
            }
            //Change Received packet destination to the client IP and Port
		        
        }catch (InterruptedIOException iioe){
        
	        System.out.println("Nothing to read");
        
        }catch (Exception ioe) {
	        System.out.println("Exception caught: "+ioe);
        } 
      }else{
          cTimer.stop();
      }
    }
  }
}

