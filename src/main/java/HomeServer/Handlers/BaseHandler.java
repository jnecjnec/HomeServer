/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers;

import HomeServer.Handlers.Device.Device;
import HomeServer.Handlers.User.User;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.Charset;
import javafx.collections.ObservableList;

/**
 *
 * @author jnec
 */
@Sharable
public abstract class BaseHandler extends SimpleChannelInboundHandler<String> {

    public static Charset CHARSET = Charset.forName("UTF-8");
    private ObservableListWrapper _observableListWrapper = null;

    public BaseHandler(ObservableListWrapper observableListWrapper) {
        super();
        _observableListWrapper = observableListWrapper;
    }

    protected ObservableList<Device> getDevices() {
        return _observableListWrapper.getDevices();
    }

    protected ObservableList<User> getUsers() {
        return _observableListWrapper.getUsers();
    }

}
