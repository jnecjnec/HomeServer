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
public interface HandlerListener {
    public enum EventType {
        evtAdd, evtRemove;        
    }
    
    public void onClientChenge(String ip, String name, EventType evtype);
    
    public void onDeviceChange(String ip, String name, EventType evtype);
    
}
