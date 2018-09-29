package com.canalmina.mina.client;

import com.alibaba.fastjson.JSON;
import com.canalmina.mina.configuration.properties.MinaProperties;
import com.canalmina.mina.configuration.MinaMessageKeepAliveFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**mina 客户端
 * @author yihang.lv 2018/9/29、17:04
 */
@Component
public class MinaClient implements ApplicationListener<ApplicationReadyEvent>{

    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private MinaMessageKeepAliveFactory messageKeepAliveFactory;

    @Autowired
    private IoHandler ioHandler;

    @Autowired
    private MinaProperties minaProperties;

    public void client() {
        //可以使用mina客户端，也可以使用普通socket发消息
        //创建客户端连接
        NioSocketConnector connector = new NioSocketConnector();
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        //加入记录日志监听器
        filterChain.addLast("logger", loggingFilter);
        //加入协议编码过滤器，这个过滤器用来转换二进制或专用数据到消息对象中
        filterChain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        //校验心跳过滤器
        filterChain.addLast("keepAliveFilter", new KeepAliveFilter(messageKeepAliveFactory));
        //创建一个IoHandler来处理和接收消息
        connector.setHandler(ioHandler);
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1", minaProperties.getPort()));
        //等待创建连接完成
        connectFuture.awaitUninterruptibly();
        IoSession session = connectFuture.getSession();
        String msg = JSON.toJSONString("hello mina");
        session.write(msg);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        client();
    }
}
