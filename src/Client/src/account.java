/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class account {
    String User;
    String PassString;

    public account() {
    }

    public account(String User, String PassString) {
        this.User = User;
        this.PassString = PassString;
    }
    public String getPassString() {
        return PassString;
    }

    public void setPassString(String PassString) {
        this.PassString = PassString;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }
    
}
