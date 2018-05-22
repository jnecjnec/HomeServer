package HomeServer;

import HomeServer.BaseHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import static HomeServer.BaseHandler.CHANNELS;

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
public class ClientHandler extends BaseHandler implements ResponseListener {

    private static final String PREFIX = "HomeServer.";
    private static final String RESPONSE_FORMAT = PREFIX + "%s.%s.%s";
    private Channel _chanel = null;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, CHARSET);

        int l = in.indexOf(PREFIX);
        if (l == 0) {
            //chanel = ctx.channel();
            String[] parts = in.split("\\.");
            String device = parts[1];
            String devicenumber = parts[2];
            String cmd = parts[3];
            boolean result = false;
            // send to device and should wait
            for (BaseHandler c : CHANNELS) {
                if ((c.getDeviceNumber().equals(devicenumber)) & (c.getDeviceName().equals(device))) {
                    result = true;
                    c.writeCommand(cmd, this);
                }
            }

            if (!result) {
                DoResponse("NotFound", device, devicenumber);
            }

        } else {
            ctx.close();
        }
    }

    private void DoResponse(String message, String devicename, String devicenumber) {
        if (_chanel != null) {
            String response = String.format(RESPONSE_FORMAT, devicename, devicenumber, message);
            CharSequence ch = response;
            ByteBuf mess = Unpooled.buffer(ch.length());
            mess.writeCharSequence(ch, CHARSET);
            _chanel.writeAndFlush(mess);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        _chanel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void writeCommand(String command, ResponseListener listener) {
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

    @Override
    public void Response(String message, String devicename, String devicenumber) {
        DoResponse(message, devicename, devicenumber);
    }

}
