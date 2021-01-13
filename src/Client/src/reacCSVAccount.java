
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class reacCSVAccount {

    public List<account> readaccount() {
        BufferedReader br = null;

        ArrayList<account> list = new ArrayList<account>();
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("account.csv"), "utf-8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reacCSVAccount.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(reacCSVAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        String line;
        int count = 0;
        String classSV = null;

        try {
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");

                String user = value[0];

                String pass = value[1];
                account ac;
                ac = new account(user, pass);

                list.add(ac);

            }
        } catch (IOException ex) {
            Logger.getLogger(reacCSVAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    //

}
