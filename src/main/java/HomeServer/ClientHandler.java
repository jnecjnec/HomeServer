package HomeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import static HomeServer.BaseHandler.DEVICES;
import static HomeServer.HandlerListener.EventType.evtAdd;
import static HomeServer.HandlerListener.EventType.evtRemove;

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
   // private Channel _chanel = null;

    public ClientHandler(HandlerListener listener) {
        super(listener);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, CHARSET);

        int l = in.indexOf(PREFIX);
        if (l == 0) {
            //chanel = ctx.channel();
            String[] parts = in.split("\\.");
            String deviceName = parts[1];
            String devicenumber = parts[2];
            String cmd = parts[3];
            boolean result = false;
            // send to device and should wait
            for (Device device : DEVICES) {
                if ((device.getDeviceNumber().equals(devicenumber)) & (device.getDeviceName().equals(deviceName))) {
                    result = true;
                    ClientRequest clientRequest = new ClientRequest(ctx, this);
                    device.writeCommand(cmd, clientRequest);
                }
            }
            if (!result) {
                DoResponse(ctx.channel(), "NotFound", deviceName, devicenumber);
            }

        } else {
            ctx.close();
        }
    }

    private void DoResponse(Channel channel, String message, String devicename, String devicenumber) {
        if (channel != null) {
            String response = String.format(RESPONSE_FORMAT, devicename, devicenumber, message);
            CharSequence ch = response;
            ByteBuf mess = Unpooled.buffer(ch.length());
            mess.writeCharSequence(ch, CHARSET);
            channel.writeAndFlush(mess);
        }
    }

    
     private void DoHandlerListener(String ip, HandlerListener.EventType evt) {
        if (_handlerListener != null) {
            _handlerListener.onClientChange(ip, evt);
        }
    }
     
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
       // _chanel = ctx.channel();
        DoHandlerListener(ctx.channel().remoteAddress().toString(), evtAdd);
    }

     @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        DoHandlerListener(ctx.channel().remoteAddress().toString(), evtRemove);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void Response(Channel channel, String message, String devicename, String devicenumber) {
        DoResponse(channel, message, devicename, devicenumber);
    }

}
