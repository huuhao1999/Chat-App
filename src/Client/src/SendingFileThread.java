
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class SendingFileThread implements Runnable {
    
    protected Socket socket;
    private DataOutputStream dos;
    protected SendFile form;
    protected String file;
    protected String receiver;
    protected String sender;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    
    public SendingFileThread(Socket soc, String file, String receiver, String sender, SendFile frm){
        this.socket = soc;
        this.file = file;
        this.receiver = receiver;
        this.sender = sender;
        this.form = frm;
    }
    
    public Socket getSocket() {
        return socket;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public SendFile getForm() {
        return form;
    }

    public String getFile() {
        return file;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public int getBUFFER_SIZE() {
        return BUFFER_SIZE;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setForm(SendFile form) {
        this.form = form;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    @Override
    public void run() {
        try {
            form.disableGUI(true);
            System.out.println("Gửi File..!");
            dos = new DataOutputStream(socket.getOutputStream());
            /** Write filename, recipient, username  **/
            File filename = new File(file);
            int len = (int) filename.length();
            int filesize = (int)Math.ceil(len / BUFFER_SIZE); // phương thức nhận kích thước file
            String clean_filename = filename.getName();
            dos.writeUTF("CMD_SENDFILE "+ clean_filename.replace(" ", "_") +" "+ filesize +" "+ receiver +" "+ sender);
            System.out.println("Từ: "+ sender);
            System.out.println("Đến: "+ receiver);
            /** Create an stream **/
            InputStream input = new FileInputStream(filename);
            OutputStream output = socket.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(input);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count, percent = 0;
            while((count = bis.read(buffer)) > 0){
                percent = percent + count;
                int p = (percent / filesize);
               
                form.updateProgress(p);
                output.write(buffer, 0, count);
            }
        
            form.setMyTitle("File đã được gửi đi.!");
            form.updateAttachment(false);
            JOptionPane.showMessageDialog(form, "File đã gửi thành công.!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            form.closeThis();
            /* Đóng gửi file */
            output.flush();
            output.close();
            System.out.println("File đã được gửi..!");
        } catch (IOException e) {
            form.updateAttachment(false);
            System.out.println("[SendFile]: "+ e.getMessage());
        }
    }
}