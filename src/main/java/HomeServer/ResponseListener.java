/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

import io.netty.channel.Channel;

/**
 *
 * @author jnec
 */
public interface ResponseListener {
    
    public void Response(Channel channel, String message, String devicename, String devicenumber);
    
}
