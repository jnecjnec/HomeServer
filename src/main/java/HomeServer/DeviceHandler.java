/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

import HomeServer.HandlerListener.EventType;
import static HomeServer.HandlerListener.EventType.evtAdd;
import static HomeServer.HandlerListener.EventType.evtRemove;
import io.netty.buffer.ByteBuf;
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
   
    public DeviceHandler(HandlerListener listener) {
        super(listener);
    }

    private void DoHandlerListener(Device dev, EventType evt) {
        if (_handlerListener != null) {
            _handlerListener.onDeviceChange(dev.getIp(), dev.getDeviceName(), dev.getDeviceNumber(), evt);
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
            for (Device d : DEVICES) {
                if ((d.getDeviceNumber().equals(parts[2])) & (d.getDeviceName().equals(parts[1]))) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                ctx.close();
            } else {
                Device dev = new Device(parts[1], parts[2], ctx);
                DEVICES.add(dev);
                DoHandlerListener(dev, evtAdd);
            }

        } else if ((parts.length == 1) & (commandList.contains(parts[0])) & (DEVICES.size() > 0)) {
            Device responseDevice = null;
            for (Device device : DEVICES) {
                if ((device.getChanelHandlerContext().equals(ctx))) {
                    responseDevice = device;
                    break;
                }
            }
            if (responseDevice != null) {
                responseDevice.SendResponse(parts[0]);
            } else {
                ctx.close();
            }

        } else {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Device deviceToRemove = null;
        for (Device device : DEVICES) {
            if ((device.getChanelHandlerContext().equals(ctx))) {
                deviceToRemove = device;
                break;
            }
        }
        if (deviceToRemove != null) {
            DoHandlerListener(deviceToRemove, evtRemove);
            DEVICES.remove(deviceToRemove);
        }
    }
}
