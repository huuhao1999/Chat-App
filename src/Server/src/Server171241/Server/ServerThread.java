/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server171241.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    
    ServerSocket server;
    MainForm main;
    boolean keepGoing = true;
    String MainPORT;
    public ServerThread(int port, MainForm main){// try catch để báo lỗi 
        try {
            this.main = main;
            server = new ServerSocket(port);
            main.appendMessage("[Server]: Máy Chủ đã khởi động.!");
        } 
        // tiến hành báo lỗi nếu đã khởi động ở port 8080
        catch (IOException e) { main.appendMessage("[IOException]: "+ e.getMessage()); }
    }

    @Override
    public void run() {
        try {
            while(keepGoing){
                Socket socket = server.accept();
                new Thread(new SocketThread(socket, main)).start();
            }
        } catch (IOException e) {
            main.appendMessage("[ServerThreadIOException]: "+ e.getMessage());
        }
    }
    
    
    public void stop(){
        try {
            server.close();
            keepGoing = false;
            System.out.println("Máy Chủ đóng!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public MainForm getMain() {
        return main;
    }

    public void setKeepGoing(boolean keepGoing) {
        this.keepGoing = keepGoing;
    }

    public String getMainPORT() {
        return MainPORT;
    }

    public void setMain(MainForm main) {
        this.main = main;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setMainPORT(String MainPORT) {
        this.MainPORT = MainPORT;
    }
    
    
}
