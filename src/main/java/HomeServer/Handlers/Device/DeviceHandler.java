/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers.Device;

import HomeServer.Handlers.BaseHandler;
import HomeServer.Handlers.ObservableListWrapper;
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
   
    public DeviceHandler(ObservableListWrapper observableListWrapper) {
        super(observableListWrapper);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int i = ((ByteBuf) msg).readableBytes();
        String in = (String) ((ByteBuf) msg).readCharSequence(i, CHARSET);

        List<String> commandList = Arrays.asList(ALLOVED_RESPONSES);

        String[] parts = in.split("\\.");

        if ((parts.length == 3) & (parts[0].equals("Identification"))) {
            boolean exists = false;
            for (Device d : getDevices()) {
                if ((d.getDeviceNumber().getValue().equals(parts[2])) & (d.getDeviceName().getValue().equals(parts[1]))) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                ctx.close();
            } else {
                Device dev = new Device(parts[1], parts[2], ctx);
                getDevices().add(dev);
            }

        } else if ((parts.length == 1) & (commandList.contains(parts[0])) & (getDevices().size() > 0)) {
            Device responseDevice = null;
            for (Device device : getDevices()) {
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
        for (Device device : getDevices()) {
            if ((device.getChanelHandlerContext().equals(ctx))) {
                deviceToRemove = device;
                break;
            }
        }
        if (deviceToRemove != null) {
            getDevices().remove(deviceToRemove);
        }
    }
}
