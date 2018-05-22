/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

/**
 *
 * @author jnec
 */
public interface ResponseListener {
    
    public void Response(String message, String devicename, String devicenumber);
    
}
