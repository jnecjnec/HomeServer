/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers.User;

/**
 *
 * @author jnec
 */
public class User {
    
    private String _name;
    private String _passwordHash;
    private String _Ip;

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the _passwordHash
     */
    public String getPasswordHash() {
        return _passwordHash;
    }

    /**
     * @param password the _passwordHash to set
     */
    public void setPasswordHash(String password) {
        this._passwordHash = password;
    }

    /**
     * @return the _Ip
     */
    public String getIp() {
        return _Ip;
    }

    /**
     * @param _Ip the _Ip to set
     */
    public void setIp(String _Ip) {
        this._Ip = _Ip;
    }

    
    
}
