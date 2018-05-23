/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jnec
 */
@Sharable
public abstract class BaseHandler extends ChannelInboundHandlerAdapter {

    protected static Charset CHARSET = Charset.forName("UTF-8");
    protected static final List<Device> DEVICES = new ArrayList<Device>();
    protected HandlerListener _handlerListener = null;
    
    public BaseHandler(HandlerListener handlerListener) {
        super();
         _handlerListener = handlerListener;
    }
   
}
