/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author jnec
 */
public class ClientRequest {

    ResponseListener _listener = null;
    ChannelHandlerContext _ctx = null;

    public ClientRequest(ChannelHandlerContext ctx, ResponseListener listener) {
        _listener = listener;
        _ctx = ctx;
    }

    public void Response(String message, String devicename, String devicenumber) {
        if (_listener != null) {
            _listener.Response(_ctx.channel()  ,message, devicename, devicenumber);
        }
    }

}
