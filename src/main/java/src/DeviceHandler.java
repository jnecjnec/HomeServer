/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jnec
 */
@Sharable
public class DeviceHandler extends BaseHandler {

    Channel chanel = null;
    boolean finished = false;
    String devicename = "";
    String devicenumber = "";
    String response = "";

    public DeviceHandler(String remoteAddress) {
        super();
        System.out.println("Handler create " + remoteAddress);
    }

    @Override
    public String writeCommand(String command) {
        response = "Unknown";

        String[] allovedCommands = new String[]{"Open", "Close", "State"};
        List<String> commandList = Arrays.asList(allovedCommands);

        if ((chanel != null) & (commandList.contains(command))) {

            finished = false;
            Charset chrst = Charset.forName("UTF-8");
            CharSequence ch = command;
            ByteBuf message = Unpooled.buffer(ch.length());
            message.writeCharSequence(ch, chrst);

            chanel.writeAndFlush(message);
           
            long writeTime = System.currentTimeMillis();
            do {
                long thisTime = System.currentTimeMillis();
                if ((thisTime - writeTime) > 10000) {
                    finished = true;
                    response = "Timeouted";
                } 
            } while (!finished);
 
        } else {
            response = "Disconnected";
        }

        return response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Charset chrst = Charset.forName("UTF-8");
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, chrst);

        String[] parts = in.split("\\.");
        
        if (parts[3].equals("Identification")) {
            devicename = parts[1];;
            devicenumber = parts[2];
        } else  {
            response = parts[3];
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        finished = true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        chanel = ctx.channel();
        channels.add(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.remove(this);
    }

    @Override
    protected String getDeviceName() {
        return devicename;
    }

    @Override
    protected String getDeviceNumber() {
        return devicenumber;
    }

}
