package os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.*;

/*
 * The purpose of this class is just to get the IP address of the given web address.
 * */
class NamingService implements Runnable{
    ServerSocket socket;
    NamingService(ServerSocket socket) {
        this.socket = socket;
    }
    
    public void run(){
        Socket client;
        try {
            client = socket.accept();   //execution gets block here until 
                                        //a client connects to it.    
          //input stream of client connected to server
            InputStream in = client.getInputStream();
            BufferedReader bReader= new BufferedReader(new InputStreamReader(in));
            
            //read web address from client like "www.edx.org"
            String line=bReader.readLine();
            
            //get its IP Adresss
            InetAddress hostAddress = InetAddress.getByName(line);
            
            //get IP Adress in form %d.%d.%d.%d
            String ip = hostAddress.getHostAddress();
            
            //output stream of client
            PrintWriter clientOut=new PrintWriter(client.getOutputStream(), true);
            
            //send the IP Address back to client like "54.230.150.177" 
            clientOut.println(ip);
            
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class NamingServer {
    public static void main(String[] args) {
        try {
            
            ServerSocket socket=new ServerSocket(6053);
            NamingService nService=new NamingService(socket);
            
            while(true){
                Thread thread=new Thread(nService);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
