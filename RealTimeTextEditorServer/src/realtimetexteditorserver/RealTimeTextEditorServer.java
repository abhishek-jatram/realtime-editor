/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package realtimetexteditorserver;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author abhishek
 */
public class RealTimeTextEditorServer extends Thread{

    JFrame frame = new JFrame();
    JTextArea editor=new JTextArea();
    JScrollPane scrollpane=new JScrollPane();
    
    
    private ServerSocket serverSocket;
    
    
    public RealTimeTextEditorServer(int port) throws IOException{
        serverSocket=new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(600,700);
        frame.setLocationRelativeTo(null);
        scrollpane.setViewportView(editor);
        frame.add(scrollpane);
        editor.setVisible(true);
        editor.setBackground(new Color(255,209,209));
        editor.setForeground(new Color(24, 19, 209));
        editor.setFont(new Font("Courier 10 Pitch", 0, 19));
        editor.setEditable(false);
        
        frame.setVisible(true);
        frame.setTitle("Text Viewer");
        
    }
    
    public void run(){
        Socket server;
        while(true){
            try {
                System.out.println("trying!!!!!!!!");
                server=serverSocket.accept();
//                System.out.println("Connected to "+server.getRemoteSocketAddress());
                while(server.isConnected()){
                    DataInputStream in = new DataInputStream(server.getInputStream());
                     String msg=in.readUTF();
                     System.out.println(msg);
                     editor.setText(msg);
                }
                break;
                
            } catch (IOException ex) {
                System.out.println("Timeout!!");
            }
        }
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 8372;
        try{
            Thread t=new RealTimeTextEditorServer(port);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
