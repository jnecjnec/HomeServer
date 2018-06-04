package HomeServer.Handlers.Client;

import HomeServer.Handlers.Device.Device;
import HomeServer.Handlers.BaseHandler;
import HomeServer.ResponseListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import HomeServer.Handlers.ObservableListWrapper;
import HomeServer.Handlers.User.User;

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

    private static final String PREFIX = "HomeServer";
    private static final String RESPONSE_FORMAT = PREFIX + ".%s.%s.%s";

    public ClientHandler(ObservableListWrapper observableListWrapper) {
        super(observableListWrapper);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, CHARSET);

        boolean accepted = false;
        String deviceName = "";
        String devicenumber = "";
        String cmd = "";

        String[] parts = in.split("\\.");
        if (parts.length == 6) {
            String id = parts[0];
            String userName = parts[1];
            String clientHash = parts[2];
            deviceName = parts[3];
            devicenumber = parts[4];
            cmd = parts[5];

            for (User u : getUsers()) {
                if (id.equals(PREFIX) & u.getName().getValue().equals(userName) & u.getPasswordHash().getValue().equals(clientHash)) {
                    u.setIp(ctx.channel().remoteAddress().toString());
                    accepted = true;
                    break;
                }
            }
        }

        if (accepted) {
            boolean result = false;
            // send to device and should wait
            for (Device device : getDevices()) {
                if ((device.getDeviceNumber().getValue().equals(devicenumber)) & (device.getDeviceName().getValue().equals(deviceName))) {
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

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        for (User u : getUsers()) {
            if (u.getIp().getValue().equals(ctx.channel().remoteAddress().toString())) {
                u.setIp("");
                break;
            }
        }

        super.channelInactive(ctx);
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
