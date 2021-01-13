
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class ClientThread implements Runnable {
    List<Chatsingle> listchatsingle=new ArrayList<>();
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    MainForm main;
    StringTokenizer st;
    protected DecimalFormat df = new DecimalFormat("##,#00");

    public ClientThread(Socket socket, MainForm main) {
        this.main = main;
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            main.appendMessage("[IOException]: " + e.getMessage(), "Lỗi", Color.RED, Color.RED);
        }
    }
    public StringTokenizer getSt() {
        return st;
    }

    public Socket getSocket() {
        return socket;
    }

    public MainForm getMain() {
        return main;
    }

    public List<Chatsingle> getListchatsingle() {
        return listchatsingle;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setSt(StringTokenizer st) {
        this.st = st;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setMain(MainForm main) {
        this.main = main;
    }

    public void setListchatsingle(List<Chatsingle> listchatsingle) {
        this.listchatsingle = listchatsingle;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                /**
                 * Get Message CMD *
                 */
                String CMD = st.nextToken();

                switch (CMD) {
                    case "CMD_MESSAGE":
                        SoundEffectMesAndFile.MessageReceive.play(); //  Play Audio clip
                        String msg = "";
                        String frm = st.nextToken();
                        while (st.hasMoreTokens()) {
                            msg = msg + " " + st.nextToken();
                        }
                        main.appendMessage(msg, frm, Color.BLUE, Color.BLACK);
                        break;
                   case "CMD_SENDMESTORECIEVE":
                        SoundEffectMesAndFile.MessageReceive.play(); //  Play Audio clip
                        String msg1 = "";
                        String frm1 = st.nextToken();
                        while (st.hasMoreTokens()) {
                            msg1 = msg1 + " " + st.nextToken();
                        }
                        System.out.println(msg1);
                        
                        for (int i=0; i<listchatsingle.size();i++)
                        {
                                if(listchatsingle.get(i).getChatto().equalsIgnoreCase(frm1))
                                {
                                    listchatsingle.get(i).appendMessage(msg1, frm1, Color.BLUE, Color.BLACK);
                                }
                        }
                       
                        break;

                    case "CMD_ONLINE":
                        Vector online = new Vector();
                        while (st.hasMoreTokens()) {
                            String list = st.nextToken();
                            if (!list.equalsIgnoreCase(main.username)) {
                                online.add(list);
                            }
                        }
                        main.appendOnlineList(online);
                        break;

                    //  hàm này sẽ thông báo đến client rằng có một file nhận, Chấp nhận hoặc từ chối file  
                    case "CMD_FILE_XD":  // Format:  CMD_FILE_XD [sender] [receiver] [filename]
                        String sender = st.nextToken();
                        String receiver = st.nextToken();
                        String fname = st.nextToken();
                        int confirm = JOptionPane.showConfirmDialog(main, "Từ: " + sender + "\ntên file: " + fname + "\nbạn có Chấp nhận file này không.?");
                        //SoundEffect.FileSharing.play(); //   Play Audio
                        if (confirm == 0) { // client chấp nhận yêu cầu, sau đó thông báo đến sender để gửi file
                            /* chọn chỗ lưu file   */
                            main.openFolder();
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                // Format:  CMD_SEND_FILE_ACCEPT [ToSender] [Message]
                                String format = "CMD_SEND_FILE_ACCEPT " + sender + " Chấp nhận";
                                dos.writeUTF(format);

                                /*  hàm này sẽ tạo một socket filesharing  để tạo một luồng xử lý file đi vào và socket này sẽ tự động đóng khi hoàn thành.  */
                                Socket fSoc = new Socket(main.getMyHost(), main.getMyPort());
                                DataOutputStream fdos = new DataOutputStream(fSoc.getOutputStream());
                                fdos.writeUTF("CMD_SHARINGSOCKET " + main.getMyUsername());
                                /*  Run Thread for this   */
                                new Thread(new ReceivingFileThread(fSoc, main)).start();
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: " + e.getMessage());
                            }
                        } else { // client từ chối yêu cầu, sau đó gửi kết quả tới sender
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                // Format:  CMD_SEND_FILE_ERROR [ToSender] [Message]
                                String format = "CMD_SEND_FILE_ERROR " + sender + " Người dùng từ chối yêu cầu của bạn hoặc bị mất kết nối.!";
                                dos.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: " + e.getMessage());
                            }
                        }
                        break;
                    // thông báo có người muốn nhắn tin với bạn
                    case "CMD_ChatSingle_XD":  // Format:  CMD_FILE_XD [sender] [receiver] [filename]
                        String sender1 = st.nextToken();
                        String receiver1 = st.nextToken();
                        String fname1 = st.nextToken();
                        System.out.println("Da gui ve clien accpet");
                        int confirm1 = JOptionPane.showConfirmDialog(main, sender1 + " muốn nhắn tin với bạn?");
                        //SoundEffect.FileSharing.play(); //   Play Audio
                        if (confirm1 == 0) { // client chấp nhận yêu cầu, sau đó thông báo đến sender để gửi file
                            /* chọn chỗ lưu file   */
                            
                            Chatsingle sg = new Chatsingle();
                            sg.setVisible(true);
                              sg.setChatsingle(sender1, receiver1,socket,main);
                           
                           listchatsingle.add(sg);
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                // Format:  CMD_SEND_FILE_ACCEPT [ToSender] [Message]
                                String format = "CMD_ACCEPT_MES " + sender1 + " " + receiver1 + " Chấp nhận";
                                dos.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("" + e.getMessage());
                            }
                        } else { // client từ chối yêu cầu, sau đó gửi kết quả tới sender
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                // Format:  CMD_SEND_FILE_ERROR [ToSender] [Message]
                                String format = "CMD_DONTACCEPT_ERROR " + sender1 + " Người dùng từ chối yêu cầu của bạn hoặc bị mất kết nối.!";
                                dos.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("" + e.getMessage());
                            }
                        }
                        break;

                    case "CMD_sendmestoreciver":
                        String sender3 = st.nextToken();
                        String receiver3 = st.nextToken();  
                        Chatsingle sg = new Chatsingle();
                        sg.setVisible(true);
                        sg.setChatsingle(sender3, receiver3,socket,main);
                        listchatsingle.add(sg);
                        break;

                    default:
                        main.appendMessage("[CMDException]: Không rõ lệnh " + CMD, "CMDException", Color.RED, Color.RED);
                        break;
                }
            }
        } catch (IOException e) {
            main.appendMessage(" Bị mất kết nối đến Máy chũ, vui lòng thử lại.!", "Lỗi", Color.RED, Color.RED);
        }
    }
}
