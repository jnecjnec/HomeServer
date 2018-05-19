package src;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import static src.BaseHandler.channels;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jnec
 */
@Sharable
public class ClientHandler extends BaseHandler {

    public ClientHandler(String remoteAddress) {
        super();
        System.out.println("Handler create " + remoteAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Charset chrst = Charset.forName("UTF-8");

        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, chrst);

        CharSequence ch = "";
        int l = in.indexOf("HomeServer.");
        if (l == 0) {
            String[] parts = in.split("\\.");
            String device = parts[1];
            String devicenumber = parts[2];
            String cmd = parts[3];
            String result = "Unknown";
            // send to device and should wait
            for (BaseHandler c : channels) {
                result = "NotFound";
                if ((c.getDeviceNumber().equals(devicenumber)) & (c.getDeviceName().equals(device))) {
                    result = c.writeCommand(cmd);
                }
            }

            ch = "HomeServer." + device + "." + devicenumber + "." + result;

            ByteBuf message = Unpooled.buffer(ch.length());
            message.writeCharSequence(ch, chrst);
            ctx.write(message);

        } else {
            ctx.close();
        }
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
    protected String writeCommand(String command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeviceName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDeviceNumber() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
