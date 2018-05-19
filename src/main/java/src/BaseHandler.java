/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jnec
 */
@Sharable
public abstract class BaseHandler extends ChannelInboundHandlerAdapter {

    //protected static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    protected static final List<BaseHandler> channels = new ArrayList<BaseHandler>();
    
    public BaseHandler(){
        super();
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelActive(ctx);
        System.out.println("Base handler added ");
    }

    protected abstract String writeCommand(String command);
    
    protected abstract String getDeviceName();
    
    protected abstract String getDeviceNumber();
}
