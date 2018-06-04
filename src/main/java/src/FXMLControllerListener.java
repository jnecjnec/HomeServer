/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author jnec
 */
public interface FXMLControllerListener {

    public void serverStart(int clientPort, int devicePort, boolean ssl);

    public void serverStop();
    
    public boolean addUser(String name, String password);
    
    public boolean deleteUser(String name);
}
