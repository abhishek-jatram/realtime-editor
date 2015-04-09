/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reattimetexteditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author abhishek
 */
public class ReatTimeTextEditor extends JFrame{
    JPanel frame = new JPanel();
    JTextArea editor= new JTextArea();
    JButton btnSync=new JButton("Start Syncing");
    JButton btnStop=new JButton("Stop Syncing");
    JPanel editorPanel= new JPanel();
    JScrollPane scrollPane=new JScrollPane();
    JPanel panelBtn=new JPanel();

    JMenuBar menubar=new JMenuBar();
    JMenu menu_settings=new JMenu("Configure Network");
    JMenuItem item_server=new JMenuItem("Server Address");
    
    String server_ip=new String();
    int port=8372;
    Socket client;
    OutputStream outToServer;
    DataOutputStream out;
    
    ImageIcon icon = new ImageIcon("icon.png");
    
    public ReatTimeTextEditor(){
        this.setSize(400,210);
        init();
    }
    class StreamRunnable implements Runnable{

        @Override
        public void run() {
            while(!client.isClosed()){
                try {
                    out.writeUTF(editor.getText());
                    Thread.sleep(100);
                } catch (IOException ex) {
                    Logger.getLogger(ReatTimeTextEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    try {
                        this.finalize();
                    } catch (Throwable ex1) {
                        Logger.getLogger(ReatTimeTextEditor.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(ReatTimeTextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
    }
    
    private class AJTextArea extends JTextArea{
        private Image bgImg;
        public AJTextArea() {
            super();
            try {
                bgImg=ImageIO.read(new File("bg.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        @Override
        protected void paintComponent(Graphics g){
            g.drawImage(bgImg, 0, 0, null);
            super.paintComponent(g);
        }
    }
    
    public void init(){
        this.setTitle("Text Editor");
        this.setSize(600,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//to end program when JFrame is closed
        this.setLocationRelativeTo(null);//to keep it in middle
        frame.setVisible(true);
        frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));
        
        
        
        editorPanel.setSize(800,900);
        editor.setSize(500,380);
        editor.setVisible(true);
        scrollPane.setViewportView(editor);
        editorPanel.setLayout(new BorderLayout());
        editorPanel.add(scrollPane,BorderLayout.CENTER);
        
        editor.setFont(new Font("Courier 10 Pitch", 0, 19));
        editor.setForeground(new Color(24, 19, 209));
        editor.setBackground(new Color(226,245,244));
        
//        editor.setForeground(Color.BLUE);
        
//        frame.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        btnSync.setVisible(true);
        btnStop.setVisible(false);
        btnSync.setFont(new Font("Courier 10 Pitch", 1, 19));
        btnStop.setFont(new Font("Courier 10 Pitch", 1, 19));
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.LINE_AXIS));
        panelBtn.add(btnSync);
        panelBtn.add(btnStop);
        
        frame.add(editorPanel);
        frame.add(panelBtn);
        add(frame);
        
        menu_settings.add(item_server);
        menubar.add(menu_settings);
        setJMenuBar(menubar);
        btnSync.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(server_ip!=null){
                    btnSync.setEnabled(false);
                    btnSync.setText("Syncing!!!");
                    btnStop.setVisible(true);
                    try {
                        client=new Socket(server_ip,port);
                        outToServer = client.getOutputStream();
                        out = new DataOutputStream(outToServer);

                        //out.writeUTF(editor.getText());
                        StreamRunnable runnable=new StreamRunnable();
                        Thread t = new Thread(runnable);
                        t.start();
                        
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Server not ready", "Alert", JOptionPane.INFORMATION_MESSAGE, icon);
                        btnSync.setEnabled(true);
                        btnSync.setText("Start Syncing");
                        btnStop.setVisible(false);
                    }
                }
            }
        });
        
         btnStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnSync.setEnabled(true);
                btnSync.setText("Start Syncing");
                btnStop.setVisible(false);
                
                try {
                    if(client!=null)
                        client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReatTimeTextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
              
            }
        });
        
        item_server.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            server_ip=(String) JOptionPane.showInputDialog(frame,"Enter server IP address","",JOptionPane.INFORMATION_MESSAGE,icon,null,null);
            
            System.out.println(server_ip);
            }
        });
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReatTimeTextEditor edit=new ReatTimeTextEditor();
        edit.setVisible(true);
    }
    
}
