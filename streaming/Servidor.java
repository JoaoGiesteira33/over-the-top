package streaming;/* ------------------
   Servidor
   usage: java Servidor [Video file]
   adaptado dos streaming.originais pela equipa docente de ESR (nenhumas garantias)
   colocar primeiro o cliente a correr, porque este dispara logo
   ---------------------- */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

/*
Perguntas:
  1: RTP funciona sobre UDP normalmamente, mas podemos usar com TCP ? Temos que fazer alterações ?
  2: A ideia aqui seria chamar o servidor do oNode ? 
*/
public class Servidor extends JFrame implements ActionListener {


  List<InetAddress> ia_list;
  //GUI:
  //----------------
  JLabel label;

  //RTP variables:
  //----------------
  DatagramPacket senddp; //UDP packet containing the video frames (to send)A
  DatagramSocket RTPsocket; //socket to be used to send and receive UDP packet
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

  //--------------------------
  //Constructor
  //--------------------------
  public Servidor(List<String> nextIPs) {

    //init Frame
    super("Servidor");

    ia_list=new ArrayList<>();
    // init para a parte do servidor
    sTimer = new Timer(FRAME_PERIOD, this); //init Timer para servidor
    sTimer.setInitialDelay(0);
    sTimer.setCoalesce(true);
    sBuf = new byte[15000]; //allocate memory for the sending buffer

    try {
	      RTPsocket = new DatagramSocket(); //init RTP socket 
        //ClientIPAddr = InetAddress.getByName(nextIp);
        //System.out.println("Servidor: socket " + ClientIPAddr);
	      video = new VideoStream(VideoFileName); //init the VideoStream object:
        System.out.println("Servidor: vai enviar video da file " + VideoFileName);

        for(String s: nextIPs)
          ia_list.add(InetAddress.getByName(s));

    } catch (SocketException e) {
        System.out.println("Servidor: erro no socket: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Servidor: erro no video: " + e.getMessage());
    }

    //Handler to close the main window
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	    //stop the timer and exit
	      sTimer.stop();
	      System.exit(0);
      }
    });

    //GUI:
    String descricao = "Send frame #" + imagenb;
    label = new JLabel(descricao, JLabel.CENTER);
    getContentPane().add(label, BorderLayout.CENTER);
          
    sTimer.start();
  }


  public Servidor(String nextIp) {
    //init Frame
    super("Servidor");

    // init para a parte do servidor
    sTimer = new Timer(FRAME_PERIOD, this); //init Timer para servidor
    sTimer.setInitialDelay(0);
    sTimer.setCoalesce(true);
    sBuf = new byte[15000]; //allocate memory for the sending buffer

    /*
     * if()
     */
    try {
	      RTPsocket = new DatagramSocket(); //init RTP socket 
        ClientIPAddr = InetAddress.getByName(nextIp);
        System.out.println("Servidor: socket " + ClientIPAddr);
	      video = new VideoStream(VideoFileName); //init the VideoStream object:
        System.out.println("Servidor: vai enviar video da file " + VideoFileName);

    } catch (SocketException e) {
        System.out.println("Servidor: erro no socket: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Servidor: erro no video: " + e.getMessage());
    }

    //Handler to close the main window
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	    //stop the timer and exit
	      sTimer.stop();
	      System.exit(0);
      }
    });

    //GUI:
    String descricao = "Send frame #" + imagenb;
    label = new JLabel(descricao, JLabel.CENTER);
    getContentPane().add(label, BorderLayout.CENTER);
          
    sTimer.start();
  }

  public Servidor() {
    //init Frame
    super("Servidor");

    // init para a parte do servidor
    sTimer = new Timer(FRAME_PERIOD, this); //init Timer para servidor
    sTimer.setInitialDelay(0);
    sTimer.setCoalesce(true);
    sBuf = new byte[15000]; //allocate memory for the sending buffer

    /*
     * if()
     */
    try {
	      RTPsocket = new DatagramSocket(); //init RTP socket 
        ClientIPAddr = InetAddress.getByName("10.0.18.1"); //("127.0.0.1");
        System.out.println("Servidor: socket " + ClientIPAddr);
	      video = new VideoStream(VideoFileName); //init the VideoStream object:
        System.out.println("Servidor: vai enviar video da file " + VideoFileName);

    } catch (SocketException e) {
        System.out.println("Servidor: erro no socket: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Servidor: erro no video: " + e.getMessage());
    }

    //Handler to close the main window
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	    //stop the timer and exit
	      sTimer.stop();
	      System.exit(0);
      }
    });

    //GUI:
    String descricao = "Send frame #" + imagenb;
    label = new JLabel(descricao, JLabel.CENTER);
    getContentPane().add(label, BorderLayout.CENTER);
          
    sTimer.start();
  }

  //------------------------------------
  //main -> args: 0=VideoFileName, 1=Ip do proximo
  //------------------------------------
  public static void main(String argv[]) throws Exception
  {
    //get video filename to request:
    List<String> ipClients = new ArrayList<>();

    if(argv.length >= 2 )
        for(int i=1; i<argv.length; i++){
          ipClients.add(argv[i]);
        }

    if (argv.length >= 1 ) {
        VideoFileName = argv[0];
        System.out.println("Servidor: VideoFileName indicado como parametro: " + VideoFileName);
    } else  {
        VideoFileName = "/home/core/Desktop/tp2/over-the-top/streaming/movie.Mjpeg";
        System.out.println("Servidor: parametro não foi indicado. VideoFileName = " + VideoFileName);
    }

    File f = new File(VideoFileName);
    if (f.exists()) {
        //Create a Main object 
        Servidor s = new Servidor(ipClients); //Servidor(argv[1]) ip do próximo
        //show GUI: (opcional!)
        s.pack();
        s.setVisible(true);
    } else
        System.out.println("Ficheiro de video não existe: " + VideoFileName);
  }

  //------------------------
  //Handler for timer
  //------------------------
  public void actionPerformed(ActionEvent e) {

    //if the current image nb is less than the length of the video
    if (imagenb < VIDEO_LENGTH)
    {
        //update current imagenb
	      imagenb++;
        String descricao = "Send frame #" + imagenb;
        label.setText(descricao);
        
	      try {
	        //get next frame to send from the video, as well as its size
	        int image_length = video.getnextframe(sBuf);
        
	        //Builds an RTPpacket object containing the frame
	        RTPpacket rtp_packet = new RTPpacket(MJPEG_TYPE, imagenb, imagenb*FRAME_PERIOD, sBuf, image_length);
        
	        //get to total length of the full rtp packet to send
	        int packet_length = rtp_packet.getlength();
        
	        //retrieve the packet bitstream and store it in an array of bytes
	        byte[] packet_bits = new byte[packet_length];
	        rtp_packet.getpacket(packet_bits);
        
	        //send the packet as a DatagramPacket over the UDP socket 
          
          for(InetAddress ia: ia_list){
            Thread t = new Thread(){
              @Override
              public void run(){
	              try {

                  senddp = new DatagramPacket(packet_bits, packet_length, ia, RTP_dest_port);
                  RTPsocket.send(senddp);
                  System.out.println("Send frame #"+imagenb);

                } catch (IOException e) {
                  System.out.println("Erro ao enviar: "+e.getMessage());
                }
              }
            };
            t.start();
          }
	        //print the header bitstream
	        rtp_packet.printheader();
        
	        //update GUI
	        //label.setText("Send frame #" + imagenb);
	      }
	      catch(Exception ex)
	        {
	          System.out.println("Exception caught: "+ex);
	          System.exit(0);
	        }
      }
    else
      {
	        //if we have reached the end of the video file, stop the timer
	        sTimer.stop();
      }
  }

}//end of Class Servidor
