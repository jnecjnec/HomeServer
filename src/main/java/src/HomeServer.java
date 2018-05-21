/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;

/**
 *
 * @author jnec
 */
public class HomeServer {

    static boolean _ssl = false;
    static int _clientPort = 8007;
    static int _devicePort = 8008;

    public HomeServer(int clientPort, int devicePort, boolean ssl) {
        _ssl = ssl;
        _clientPort = clientPort;
        _devicePort = devicePort;
    }

    public static void runServer() throws InterruptedException, CertificateException, SSLException {
        final SslContext sslCtx;
        if (_ssl) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap ssl_bootstrap = new ServerBootstrap();
            ssl_bootstrap.group(bossGroup, workerGroup)
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

                            p.addLast(new ClientHandler());
                        }
                    });

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new DeviceHandler());
                        }
                    });

            // Start the server.
            ChannelFuture ssl_chanel = ssl_bootstrap.bind(_clientPort).sync();
            ChannelFuture chanel = bootstrap.bind(_devicePort).sync();

            // Wait until the server socket is closed.
            ssl_chanel.channel().closeFuture().sync();
            chanel.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void stopServer() {
    }
}
