
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class Chatsingle extends javax.swing.JFrame {

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    MainForm main;
    StringTokenizer st;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    String username;
    String chatto;
    String host;
    int port;
    public boolean attachmentOpen = false;
    private boolean isConnected = false;

    public Chatsingle() {
        initComponents();
          Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        txt_sendto = new javax.swing.JTextField();
        btn_sendSingle = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Đang chat với: ");

        jTextPane1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        txt_sendto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sendtoActionPerformed(evt);
            }
        });
        txt_sendto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_sendtoKeyPressed(evt);
            }
        });

        btn_sendSingle.setBackground(new java.awt.Color(153, 204, 255));
        btn_sendSingle.setForeground(new java.awt.Color(255, 255, 255));
        btn_sendSingle.setText("Gửi");
        btn_sendSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendSingleActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_sendto, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_sendSingle, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_sendto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_sendSingle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getChatto() {
        return chatto;
    }

    public void setChatto(String chatto) {
        this.chatto = chatto;
    }

    public String getUsername() {
        return username;
    }


    private void txt_sendtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sendtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_sendtoActionPerformed

    public void appendMessage(String msg, String header, Color headerColor, Color contentColor) {
        jTextPane1.setEditable(true);
        getMsgHeader(header, headerColor);
        getMsgContent(msg, contentColor);
        jTextPane1.setEditable(false);
    }

    /*
        Tin nhắn chat
     */
    public void appendMyMessage(String msg, String header) {
        jTextPane1.setEditable(true);
        getMsgHeader(header, Color.BLACK);
        getMsgContent(msg, Color.LIGHT_GRAY);
        jTextPane1.setEditable(false);
    }

    /*
        Tiêu đề tin nhắn
     */
    public void getMsgHeader(String header, Color color) {
        int len = jTextPane1.getDocument().getLength();
        jTextPane1.setCaretPosition(len);
        jTextPane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Impact", 13), false);
        jTextPane1.replaceSelection(header + ":");
    }

    /*
        Nội dung tin nhắn
     */
    public void getMsgContent(String msg, Color color) {
        int len = jTextPane1.getDocument().getLength();
        jTextPane1.setCaretPosition(len);
        jTextPane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Arial", 13), false);
        jTextPane1.replaceSelection(msg + "\n\n");
    }
    private void btn_sendSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendSingleActionPerformed
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            String content = username + " " + chatto + " " + txt_sendto.getText();
            System.out.println("COnten:" + content);
            dos.writeUTF("CMD_CHATSENDSINGLEFOR " + content);
            appendMyMessage(" " + txt_sendto.getText(), username);
            txt_sendto.setText("");
        } catch (IOException e) {
            appendMessage(" Không thể gửi tin nhắn đi bây giờ, không thể kết nối đến Máy Chủ tại thời điểm này, xin vui lòng thử lại sau hoặc khởi động lại ứng dụng này.!", "Lỗi", Color.RED, Color.RED);
        }
    }//GEN-LAST:event_btn_sendSingleActionPerformed

    private void txt_sendtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_sendtoKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                String content = username + " " + chatto + " " + txt_sendto.getText();
                System.out.println("COnten:" + content);
                dos.writeUTF("CMD_CHATSENDSINGLEFOR " + content);
                appendMyMessage(" " + txt_sendto.getText(), username);
                txt_sendto.setText("");
            } catch (IOException e) {
                appendMessage(" Không thể gửi tin nhắn đi bây giờ, không thể kết nối đến Máy Chủ tại thời điểm này, xin vui lòng thử lại sau hoặc khởi động lại ứng dụng này.!", "Lỗi", Color.RED, Color.RED);
            }
        }

    }//GEN-LAST:event_txt_sendtoKeyPressed
    public void setChatsingle(String username, String chatto, Socket socket, MainForm main) {
        this.username = chatto;
        this.chatto = username;
        this.socket = socket;
        this.main = main;
        this.jLabel2.setText(this.chatto);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chatsingle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chatsingle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chatsingle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chatsingle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chatsingle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sendSingle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField txt_sendto;
    // End of variables declaration//GEN-END:variables
}
