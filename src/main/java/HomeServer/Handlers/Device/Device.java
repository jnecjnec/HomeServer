/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers.Device;

import HomeServer.Handlers.Client.ClientRequest;
import static HomeServer.Handlers.BaseHandler.CHARSET;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jnec
 */
public class Device {

    private static final String[] ALLOVED_COMMANDS = new String[]{"Open", "Close", "State"};

    private String _deviceName = "";
    private String _deviceNumber = "";
    private ClientRequest _clientRequest = null;
    private ChannelHandlerContext _chanelHandlerContext = null;

    public Device(String name, String number, ChannelHandlerContext channelHandlerContext) {
        _deviceName = name;
        _deviceNumber = number;
        _chanelHandlerContext = channelHandlerContext;
    }

    /**
     * @return the _deviceName
     */
    public String getDeviceName() {
        return _deviceName;
    }

    /**
     * @return the _deviceNumber
     */
    public String getDeviceNumber() {
        return _deviceNumber;
    }

    /**
     * @return the _chanelHandlerContext
     */
    public ChannelHandlerContext getChanelHandlerContext() {
        return _chanelHandlerContext;
    }

    public String getIp() {
        String str = "";
        if (_chanelHandlerContext != null) {
            str = _chanelHandlerContext.channel().remoteAddress().toString();
        }

        return str;
    }

    public void writeCommand(String command, ClientRequest clientRequest) {
        if (_clientRequest == null) {
            _clientRequest = clientRequest;

            List<String> commandList = Arrays.asList(ALLOVED_COMMANDS);

            if (_chanelHandlerContext.channel() != null) {
                if (commandList.contains(command)) {
                    CharSequence ch = command;
                    ByteBuf message = Unpooled.buffer(ch.length());
                    message.writeCharSequence(ch, CHARSET);

                    _chanelHandlerContext.channel().writeAndFlush(message);

                    long writeTime = System.currentTimeMillis();
                    do {
                        long thisTime = System.currentTimeMillis();
                        if ((thisTime - writeTime) > 10000) {
                            SendResponse("Timeouted");

                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DeviceHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } while (_clientRequest != null);

                } else {
                    SendResponse("UnknownCommand");
                }
            } else {
                SendResponse("Disconnected");
            }
        } else {
            DoSendResponse("Busy", clientRequest);
        }
    }

    public void SendResponse(String message) {
        DoSendResponse(message, _clientRequest);
        _clientRequest = null;
    }

    private void DoSendResponse(String message, ClientRequest clientRequest) {
        if (clientRequest != null) {
            clientRequest.Response(message, _deviceName, _deviceNumber);
        }
    }

}
