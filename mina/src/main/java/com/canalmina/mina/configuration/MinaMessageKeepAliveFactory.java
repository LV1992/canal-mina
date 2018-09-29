package com.canalmina.mina.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**消息心跳工厂检验心跳
 * @author yihang.lv 2018/9/29、17:10
 */
@Slf4j
public class MinaMessageKeepAliveFactory implements KeepAliveMessageFactory{

    /**
     * 自定义的心跳包内容
     */
    private static final String HEARTBEAT_REQUEST = "HEARTBEAT_REQUEST";
    private static final String HEARTBEAT_RESPONSE = "HEARTBEAT_RESPONSE";

    /**
     * 校验是否有请求心跳
     * @param session
     * @param message
     * @return
     */
    @Override
    public boolean isRequest(IoSession session, Object message) {
        log.info("heartbeat_request : " + message.toString());
        if(HEARTBEAT_REQUEST.equals(message)){
            return true;
        }
        return false;
    }

    /**
     * 校验是否有响应心跳
     * @param session
     * @param message
     * @return
     */
    @Override
    public boolean isResponse(IoSession session, Object message) {
        log.info("heartbeat_response : " + message.toString());
        if(HEARTBEAT_RESPONSE.equals(message)){
            return true;
        }
        return false;
    }

    /**
     * 请求心跳包
     * @param session
     * @return
     */
    @Override
    public Object getRequest(IoSession session) {
        return HEARTBEAT_REQUEST;
    }

    /**
     * 返回心跳包
     * @param session
     * @param request
     * @return
     */
    @Override
    public Object getResponse(IoSession session, Object request) {
        return HEARTBEAT_RESPONSE;
    }
}
