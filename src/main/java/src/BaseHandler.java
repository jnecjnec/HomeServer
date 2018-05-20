/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
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

    protected Charset chrst = Charset.forName("UTF-8");
    protected static final List<BaseHandler> channels = new ArrayList<BaseHandler>();

    public BaseHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelActive(ctx);
        // System.out.println("Base handler added ");
    }

    protected abstract void writeCommand(String command, ResponseListener listener);

    protected abstract String getDeviceName();

    protected abstract String getDeviceNumber();
}
