package Server171241.Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class OnlineListThread implements Runnable {

    MainForm main;
    String porString;
    String userString;

    public OnlineListThread(MainForm main) {
        this.main = main;
    }
        public MainForm getMain() {
        return main;
    }

    public String getPorString() {
        return porString;
    }

    public String getUserString() {
        return userString;
    }

    public void setMain(MainForm main) {
        this.main = main;
    }

    public void setPorString(String porString) {
        this.porString = porString;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                String msg = "";
                for (int x = 0; x < main.clientList.size(); x++) {
                    msg = msg + " " + main.clientList.elementAt(x);
                }

                for (int x = 0; x < main.socketList.size(); x++) {
                    Socket tsoc = (Socket) main.socketList.elementAt(x);
                    DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                    if (msg.length() > 0) {
                        dos.writeUTF("CMD_ONLINE " + msg);
                    }
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            main.appendMessage("[InterruptedException]: " + e.getMessage());
        } catch (IOException e) {
            main.appendMessage("[IOException]: " + e.getMessage());
        }
    }



}
