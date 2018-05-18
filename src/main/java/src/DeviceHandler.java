/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

/**
 *
 * @author jnec
 */
@Sharable
public class DeviceHandler extends ChannelInboundHandlerAdapter {

    public DeviceHandler(String remoteAddress) {
        System.out.println("Handler create " + remoteAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Charset chrst = Charset.forName("UTF-8");

        int i = ((ByteBuf) msg).readableBytes();
        CharSequence in = ((ByteBuf) msg).readCharSequence(i, chrst);

        System.out.println("lrecieved data " + in);

        CharSequence ch = "";
        if (in.equals("Gate.Garaz.Open")) {

            ch = "Gate.Garaz.Opened";
        }

        ByteBuf firstMessage = Unpooled.buffer(ch.length());
        firstMessage.writeCharSequence(ch, chrst);
        ctx.write(firstMessage);
                
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Device handler active ");
    }
    
    @Override
    public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Device handler added ");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Handler removed ");
    }
}

