/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jnec
 */
public class User {
    
    private final StringProperty _name  = new SimpleStringProperty("");
    private final StringProperty _passwordHash = new SimpleStringProperty("");
    private final StringProperty _Ip = new SimpleStringProperty("");

    /**
     * @return the _name
     */
    public StringProperty getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        _name.setValue(name); 
    }

    /**
     * @return the _passwordHash
     */
    public StringProperty getPasswordHash() {
        return _passwordHash;
    }

    /**
     * @param password the _passwordHash to set
     */
    public void setPasswordHash(String password) {
        _passwordHash.setValue(password);
    }

    /**
     * @return the _Ip
     */
    public StringProperty getIp() {
        return _Ip;
    }
    

    /**
     * @param Ip
     */
    public void setIp(String Ip) {
        _Ip.setValue(Ip);
    }    
}
