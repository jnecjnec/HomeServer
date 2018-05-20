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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jnec
 */
@Sharable
public class DeviceHandler extends BaseHandler {

    private static final String[] ALLOVED_RESPONSES = new String[]{"Opened", "Closesed", "Opening", "Closeing"};
    private static final String[] ALLOVED_COMMANDS = new String[]{"Open", "Close", "State"};

    private Channel _chanel = null;
    private boolean _finished = true;
    private String _devicename = "";
    private String _devicenumber = "";
    private String _esponse = "";
    private ResponseListener _listener = null;

    @Override
    public void writeCommand(String command, ResponseListener listener) {
        if (_finished) {
            _finished = false;
            _listener = listener;
            _esponse = "Unknown";

            List<String> commandList = Arrays.asList(ALLOVED_COMMANDS);

            if (_chanel != null) {
                if (commandList.contains(command)) {
                    CharSequence ch = command;
                    ByteBuf message = Unpooled.buffer(ch.length());
                    message.writeCharSequence(ch, CHARSET);

                    _chanel.writeAndFlush(message);

                } else {
                    _esponse = "UnknownCommand";
                    DoListener(listener, _esponse, _devicename, _devicenumber);
                }
            } else {
                _esponse = "Disconnected";
                DoListener(listener, _esponse, _devicename, _devicenumber);
            }
        } else {
            _esponse = "Busy";
            DoListener(listener, _esponse, _devicename, _devicenumber);
        }
    }

    private void DoListener(ResponseListener listener, String message, String devicename, String devicenumber) {
        if (listener != null) {
            listener.Response(message, devicename, devicenumber);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, CHARSET);

        List<String> commandList = Arrays.asList(ALLOVED_RESPONSES);

        String[] parts = in.split("\\.");

        if ((parts.length == 3) & (parts[0].equals("Identification"))) {
            boolean exists = false;
            for (BaseHandler c : CHANNELS) {
                if ((c.getDeviceNumber().equals(parts[2])) & (c.getDeviceName().equals(parts[1]))) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                ctx.close();
            } else {
                _devicename = parts[1];
                _devicenumber = parts[2];
            }

        } else if ((parts.length == 1) & (commandList.contains(parts[0])) & (!_devicename.isEmpty())) {
            _esponse = parts[0];
            _finished = true;
            DoListener(_listener, _esponse, _devicename, _devicenumber);
            _listener = null;
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

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
        _chanel = ctx.channel();
        CHANNELS.add(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CHANNELS.remove(this);
    }

    @Override
    protected String getDeviceName() {
        return _devicename;
    }

    @Override
    protected String getDeviceNumber() {
        return _devicenumber;
    }

}
