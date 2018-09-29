package com.canalmina.mina.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

/**
 * 实现 Iohandler 接口 负责接收和处理数据
 *
 * @author yihang.lv 2018/9/29、16:27
 */
@Slf4j
public class MinaSocketHandler implements IoHandler {
    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        //获取客户端ip
        InetSocketAddress remoteAddress = (InetSocketAddress) ioSession.getRemoteAddress();
        String hostAddress = remoteAddress.getAddress().getHostAddress();
        log.info("Client ip is : " + hostAddress);
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {

    }

    /**
     * 空闲时，将定时调用一次会话
     *
     * @param ioSession
     * @param idleStatus
     * @throws Exception
     */
    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

    }

    /**
     * 当遇到异常时调用，通常的做法是，关闭会话
     *
     * @param ioSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        log.info("session is close cause by : " + throwable.getMessage());
        //true : 立即关闭会话
        ioSession.close(true);
    }

    /**
     * 当接收到消息时调用的方法，根据协议编码器的不同，object 类型也会不同
     *
     * @param ioSession
     * @param o
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        String message = o.toString();
        if ("exit".equals(message)) {
            log.info("session is exit");
            ioSession.close(true);
        }
        log.info("received msg : " + message);
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
        log.info("received msg : " + o.toString());
    }
}
