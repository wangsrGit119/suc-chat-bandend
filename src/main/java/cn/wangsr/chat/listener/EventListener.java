package cn.wangsr.chat.listener;

import cn.wangsr.chat.model.dto.ReceiveMessageDTO;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjl
 */
@Component
public class EventListener {
    private Logger logger = LoggerFactory.getLogger(EventListener.class);
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    @OnConnect
    public void  eventOnConnect(SocketIOClient client){
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientMap.put(urlParams.get("uid").get(0),client);
        logger.info("链接开启 ，urlParams {}",urlParams);
        logger.info("加入人数： {}",clientMap.size());
    }

    @OnDisconnect
    public void  eventOnDisConnect(SocketIOClient client){
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        String moveUser = urlParams.get("uid").get(0);
        clientMap.remove(moveUser);
        logger.info("链接关闭 ，urlParams {}",urlParams);
        logger.info("剩余人数： {}",clientMap.size());
    }

    @OnEvent("sendMessage")
    public void onSendMessage(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("receiveMessageDTO {}",receiveMessageDTO);
        SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
        if(!StringUtils.isEmpty(socketIOClient)){
            logger.info("目标用户ID {}不在线", receiveMessageDTO.getTargetId());
            socketIOClient.sendEvent("sendMessage",receiveMessageDTO);
        }
    }

}
