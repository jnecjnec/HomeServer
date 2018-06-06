/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer;

import HomeServer.Handlers.BaseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;

/**
 *
 * @author jnec
 */
public class HomeServer {

    // Configure the server.
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private ServerBootstrap bootstrap = null;
    private ChannelFuture chanel = null;

    public HomeServer() {

    }

    public void runServer(int port, boolean ssl, BaseHandler handler) throws InterruptedException, CertificateException, SSLException {
        final SslContext sslCtx;
        if (ssl) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                //.handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc()));
                        }
                        //p.addLast(new LoggingHandler(LogLevel.INFO));

                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(handler);
                    }
                });

        // Start the server.
        chanel = bootstrap.bind(port);

    }

    public void stopServer() {
        chanel.channel().close();

        try {
            chanel.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            Logger.getLogger(HomeServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
