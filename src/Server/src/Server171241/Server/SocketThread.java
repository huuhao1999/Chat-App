/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server171241.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketThread implements Runnable {

    Socket socket;
    MainForm main;
    DataInputStream dis;
    StringTokenizer st;
    String client, filesharing_username;

    private final int BUFFER_SIZE = 100;

    public SocketThread(Socket socket, MainForm main) {
        this.main = main;
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            main.appendMessage("[SocketThreadIOException]: " + e.getMessage());
        }
    }
    public StringTokenizer getSt() {
        return st;
    }

    public void setSt(StringTokenizer st) {
        this.st = st;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public MainForm getMain() {
        return main;
    }

    public void setMain(MainForm main) {
        this.main = main;
    }

    public String getFilesharing_username() {
        return filesharing_username;
    }

    public void setFilesharing_username(String filesharing_username) {
        this.filesharing_username = filesharing_username;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public int getBUFFER_SIZE() {
        return BUFFER_SIZE;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

   
    private void createConnection(String nguoinhan, String nguoigui, String filename) {
        try {
          
            Socket s = main.getClientList(nguoinhan);
            if (s != null) { // check Client đã tồn tại
                main.appendMessage("[CreateConnection]: Đã tạo kết nối chia sẻ File");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                main.appendMessage("[CreateConnection]: OK");
                String format = "CMD_FILE_XD " + nguoigui + " " + nguoinhan + " " + filename;
                dosS.writeUTF(format);
                main.appendMessage("[createConnection]: " + format);
            } else {
                main.appendMessage("[createConnection]: Client không online '" + nguoinhan + "'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + nguoinhan + "' hình như client đã offline.!");
            }
        } catch (IOException e) {
            main.appendMessage("[createConnection]: " + e.getLocalizedMessage());
        }
    }

    private void createConnectionChatSinglesenmes(String nguoinhan, String nguoigui, String mesString) {
        try {
            main.appendMessage("[createConnection]: đang tạo kết nối");
            Socket s = main.getClientList(nguoinhan);
            if (s != null) { // Client đã tồn tại
                main.appendMessage("[createConnection]: Socket OK");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                main.appendMessage("[createConnection]: DataOutputStream OK");
 
                String format = "CMD_sendmestoreciver " + nguoigui + " " + nguoinhan + " " + mesString;
                dosS.writeUTF(format);
                main.appendMessage("[createConnection]: " + format);
            } else {// Client không tồn tại, gửi lại cho sender rằng receiver không tìm thấy. tien hanh
                main.appendMessage("[createConnection]: Client không được tìm thấy '" + nguoinhan + "'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + nguoinhan + "' không được tìm thấy trong danh sách, bảo đảm rằng user đang online.!");
            }
        } catch (IOException e) {
            main.appendMessage("[createConnection]: " + e.getLocalizedMessage());
        }
    }

    private void createConnectionsingle(String receiver, String sender, String filename) {
        try {
            main.appendMessage("[createConnection]: Đang tạo kết nối chat với " + receiver);
            Socket s = main.getClientList(receiver);
            if (s != null) { 

                main.appendMessage("[createConnection]: Socket OK");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                main.appendMessage("[createConnection]: DataOutputStream OK");

                String format = "CMD_ChatSingle_XD " + sender + " " + receiver + " " + filename;
                dosS.writeUTF(format);
                main.appendMessage("[createConnection]: " + format);
            } else {
                main.appendMessage("[createConnection]: Client không được tìm thấy '" + receiver + "'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + receiver + "' không được tìm thấy trong danh sách, bảo đảm rằng user đang online.!");
            }
        } catch (IOException e) {
            main.appendMessage("[createConnection]: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        try {
            // luôn lắng nghe client
            while (true) {

                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                switch (CMD) {
                    case "CMD_JOIN":
                        /**
                         * CMD_JOIN [clientUsername] * cho client tham gia vao
                         */
                        String clientUsername = st.nextToken();
                        client = clientUsername;
                        main.setClientList(clientUsername);
                        main.setSocketList(socket);
                        main.appendMessage("[Client]: " + clientUsername + " tham gia chatroom.!");
                        break;

                    case "CMD_CHATSINGLEREQUEST":
                        /**
                         * CMD_CHAT [from] [sendTo] [message] *
                         */
                        String from = st.nextToken();
                        String sendTo = st.nextToken();
                        String msg = "chat riêng";
                      
                        try {

                            String send_filename = "Muốn nhắn tin";

                            this.createConnectionsingle(sendTo, from, msg);
                        } catch (Exception e) {
                            main.appendMessage("[CMD_SEND_FILE_XD]: " + e.getLocalizedMessage());
                        }
                        break;
                    // tiếp nhận case   case "CMD_CHATALL": từ client và xử lí 
                    case "CMD_CHATALL":
                        /**
                         * CMD_CHATALL [from] [message] * gửi tin nhắn all
                         */
                        // lấy tên người gửi
                        String chatall_from = st.nextToken();
                    //lấy nôi dung tin nhắn 
                        String chatall_msg = "";
                        while (st.hasMoreTokens()) {
                            chatall_msg = chatall_msg + " " + st.nextToken();
                        }
                        String chatall_content = chatall_from + " " + chatall_msg;
                        for (int x = 0; x < main.clientList.size(); x++) {
                            if (!main.clientList.elementAt(x).equals(chatall_from)) {
                                try {
                                    Socket tsoc2 = (Socket) main.socketList.elementAt(x);
                                    DataOutputStream dos2 = new DataOutputStream(tsoc2.getOutputStream());
                                    dos2.writeUTF("CMD_MESSAGE " + chatall_content);
                                } catch (IOException e) {
                                    main.appendMessage("[CMD_CHATALL]: " + e.getMessage());
                                }
                            }
                        }
                        main.appendMessage("[CMD_CHATALL]: " + chatall_content);
                        break;
                    case "CMD_CHATSENDSINGLEFOR":
                        /**
                         * chat single va gui cho nguoi can nhan
                         */
                        String ChatSingleFrom = st.nextToken();// người gửi
                        String ChatTO = st.nextToken();// người nhận
                        String chatMsg = "";// nội dụng
                        // get content gửi
                        while (st.hasMoreTokens()) {
                            chatMsg = chatMsg + " " + st.nextToken();
                        }
                        try {
                          // check xem chatto có tôn tại hay không nếu tồn tại thì gửi về cho người nhân không thi báo họ đã offline
                            Socket s = main.getClientList(ChatTO);
                            if (s != null) { // Client đã tồn tại
                              
                                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                              
                                // Format:  CMD_FILE_XD [sender] [receiver] [filename]
                               String ContentChatSingle ="CMD_SENDMESTORECIEVE "+ ChatSingleFrom + " " + chatMsg;
                                dosS.writeUTF(ContentChatSingle);
                                main.appendMessage("[createConnection]: " + ContentChatSingle);
                            } else {// Client không tồn tại, gửi lại cho sender rằng receiver không tìm thấy.
                             
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + ChatTO + "' không được tìm thấy trong danh sách, bảo đảm rằng user đang online.!");
                            }
                        } catch (IOException e) {
                            main.appendMessage("[createConnection]: " + e.getLocalizedMessage());
                        }

                        break;

                    case "CMD_SHARINGSOCKET":
                       
                        String file_sharing_username = st.nextToken();
                        filesharing_username = file_sharing_username;
                        main.setClientFileSharingUsername(file_sharing_username);
                        main.setClientFileSharingSocket(socket);
                        main.appendMessage("CMD_SHARINGSOCKET : Username: " + file_sharing_username);
                        main.appendMessage("CMD_SHARINGSOCKET : Chia Sẻ File đang được mở");
                        break;

                    case "CMD_SENDFILE":
              
                        String file_name = st.nextToken();
                        String filesize = st.nextToken();
                        String sendto = st.nextToken();
                        String consignee = st.nextToken();
                        main.appendMessage("CMD_SENDFILE : Từ: " + consignee);
                        main.appendMessage("CMD_SENDFILE : Đến: " + sendto);
                        main.appendMessage("CMD_SENDFILE : sẵn sàng cho các kết nối..");
                        Socket cSock = main.getClientFileSharingSocket(sendto);
                        if (cSock != null) {

                            try {
                          
                               
                                main.appendMessage("CMD_SENDFILE : đang gửi file đến client...");
                                DataOutputStream cDos = new DataOutputStream(cSock.getOutputStream());
                                cDos.writeUTF("CMD_SENDFILE " + file_name + " " + filesize + " " + consignee);

                                InputStream input = socket.getInputStream();
                                OutputStream sendFile = cSock.getOutputStream();
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int cnt;
                                while ((cnt = input.read(buffer)) > 0) {
                                    sendFile.write(buffer, 0, cnt);
                                }
                                sendFile.flush();
                                sendFile.close();
                                
                                 // Xóa danh sách client 
                                 
                                main.removeClientFileSharing(sendto);
                                main.removeClientFileSharing(consignee);
                                main.appendMessage("CMD_SENDFILE : Gửi file hoàn tât");
                            } catch (IOException e) {
                                main.appendMessage("[CMD_SENDFILE]: " + e.getMessage());
                            }
                        } else {
                            main.removeClientFileSharing(consignee);
                            main.appendMessage("CMD_SENDFILE : Client '" + sendto + "' không tìm thấy.!");
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + sendto + "' không tìm thấy, Chia Sẻ File sẽ thoát.");
                        }
                        break;

                    case "CMD_SENDFILERESPONSE":
                        /*
                         Format: CMD_SENDFILERESPONSE [username] [Message]
                         */
                        String receiver = st.nextToken(); // phương thức nhận receiver username
                        String rMsg = ""; // phương thức nhận error message
                        main.appendMessage("[CMD_SENDFILERESPONSE]: username: " + receiver);
                        while (st.hasMoreTokens()) {
                            rMsg = rMsg + " " + st.nextToken();
                        }
                        try {
                            Socket rSock = (Socket) main.getClientFileSharingSocket(receiver);
                            DataOutputStream rDos = new DataOutputStream(rSock.getOutputStream());
                            rDos.writeUTF("CMD_SENDFILERESPONSE" + " " + receiver + " " + rMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_SENDFILERESPONSE]: " + e.getMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_XD":                      
                        try {
                            String send_sender = st.nextToken();
                            String send_receiver = st.nextToken();
                            String send_filename = st.nextToken();
                            main.appendMessage("[CMD_SEND_FILE_XD]: Host: " + send_sender);
                            this.createConnection(send_receiver, send_sender, send_filename);
                        } catch (Exception e) {
                            main.appendMessage("[CMD_SEND_FILE_XD]: " + e.getLocalizedMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_ERROR":
                        String eReceiver = st.nextToken();
                        String eMsg = "";
                        while (st.hasMoreTokens()) {
                            eMsg = eMsg + " " + st.nextToken();
                        }
                        try {
                            /*  Gửi Error đến File Sharing host  */
                            Socket eSock = main.getClientFileSharingSocket(eReceiver); // phương thức nhận file sharing host socket cho kết nối
                            DataOutputStream eDos = new DataOutputStream(eSock.getOutputStream());
                            //  Format:  CMD_RECEIVE_FILE_ERROR [Message]
                            eDos.writeUTF("CMD_RECEIVE_FILE_ERROR " + eMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: " + e.getMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_ACCEPT": // Format:  CMD_SEND_FILE_ACCEPT [receiver] [Message]
                        String aReceiver = st.nextToken();
                        String aMsg = "";
                        while (st.hasMoreTokens()) {
                            aMsg = aMsg + " " + st.nextToken();
                        }
                        try {
                            /*  Send Error to the File Sharing host  */
                            Socket aSock = main.getClientFileSharingSocket(aReceiver); // get the file sharing host socket for connection
                            DataOutputStream aDos = new DataOutputStream(aSock.getOutputStream());
                            //  Format:  CMD_RECEIVE_FILE_ACCEPT [Message]
                            aDos.writeUTF("CMD_RECEIVE_FILE_ACCEPT " + aMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: " + e.getMessage());
                        }
                        break;
                    case "CMD_ACCEPT_MES":
                        String aReceiver1 = st.nextToken();
                        String sender2 = st.nextToken();
                       
                        try {
                            this.createConnectionChatSinglesenmes(aReceiver1, sender2, "dcsdcdsc");
                        } catch (Exception e) {

                        }

                        break;

                    default:
                        main.appendMessage("[CMDException]: Không rõ lệnh " + CMD);
                        break;
                }
            }
        } catch (IOException e) {
            /*   đây là hàm chatting client, remove nếu như nó tồn tại..   */
            System.out.println(client);
            
            main.removeFromTheList(client);
            if (filesharing_username != null) {
                main.removeClientFileSharing(filesharing_username);
            }
            main.appendMessage("[SocketThread]: Kết nối client bị đóng..!");
        }
    }

}
