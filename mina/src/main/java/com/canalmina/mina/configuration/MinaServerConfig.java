package com.canalmina.mina.configuration;

import com.canalmina.mina.configuration.properties.MinaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/** minia服务端
 * @author yihang.lv 2018/9/29、16:20
 */
@Slf4j
@Configuration
public class MinaServerConfig {

    @Autowired
    private MinaProperties minaProperties;

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public MinaMessageKeepAliveFactory messageKeepAliveFactory() {
        return new MinaMessageKeepAliveFactory();
    }

    /**
     * 主要处理消息逻辑的实现类
     * @return
     */
    @Bean
    public IoHandler ioHandler() {
        return new MinaSocketHandler();
    }

    @Bean
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(minaProperties.getPort());
    }

    @Bean
    public IoAcceptor ioAcceptor() throws Exception {
        log.info("socket is starting now !!!");
        //监听传入，连接的对象
        IoAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
        //加入记录日志监听器
        filterChain.addLast("logger", loggingFilter());
        //加入协议编码过滤器，这个过滤器用来转换二进制或专用数据到消息对象中
        filterChain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        //校验心跳过滤器
        filterChain.addLast("keepAliveFilter", new KeepAliveFilter(messageKeepAliveFactory()));
        //创建一个IoHandler来处理和接收消息
        acceptor.setHandler(ioHandler());
        IoSessionConfig sessionConfig = acceptor.getSessionConfig();
        //设置缓冲区大小
        sessionConfig.setReadBufferSize(2048);
        sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 10);
        //必须写在后面，否则报 handler is not set
        acceptor.bind(inetSocketAddress());
        log.info("socket is running now !!!");
        return acceptor;
    }
}
