package cn.wangsr.chat.listener;

import cn.wangsr.chat.common.CommonConstant;
import cn.wangsr.chat.dao.GroupRepository;
import cn.wangsr.chat.dao.UserMessageRepository;
import cn.wangsr.chat.model.UserGroupPO;
import cn.wangsr.chat.model.UserMessagePO;
import cn.wangsr.chat.model.dto.ReceiveMessageDTO;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.websocket.OnError;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjl
 */
@Component
@Transactional(rollbackFor = Throwable.class)
public class SucEventListener {
    private Logger logger = LoggerFactory.getLogger(SucEventListener.class);
    public static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    @Resource
    UserMessageRepository userMessageRepository;
    @Resource
    GroupRepository groupRepository;
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
        UserMessagePO userMessagePO = UserMessagePO.builder()
                .userId(receiveMessageDTO.getUserId())
                .bindTarget(receiveMessageDTO.getTargetId())
                .createTime(LocalDateTime.now())
                .message(receiveMessageDTO.getData().get("message").toString())
                .messageType(receiveMessageDTO.getTargetType())
                .build();
        //保存消息记录
        userMessageRepository.save(userMessagePO);
        //群聊
        if(CommonConstant.GROUP_TYPE_GROUP.equals(receiveMessageDTO.getTargetType())){
            //加载群聊人员
            UserGroupPO userGroupPO = groupRepository.getOne(receiveMessageDTO.getTargetId());
            String[] split = userGroupPO.getGroupUsersIds().split(",");
            Long userId = receiveMessageDTO.getUserId();
            //不给自己推送
            for (String s : split) {
                if(StringUtils.isEmpty(s)){
                    continue;
                }
                Long groupOne = Long.valueOf(s);

                if(!userId.equals(groupOne)){
                    SocketIOClient ioClient = clientMap.get(groupOne.toString());
                    if(null != ioClient){
                        ioClient.sendEvent("sendMessage",receiveMessageDTO);
                    }
                }
            }
            //1V1
        }else {
            SocketIOClient socketIOClient = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
            if(!StringUtils.isEmpty(socketIOClient)){
                logger.info("目标用户ID {}在线", receiveMessageDTO.getTargetId());
                socketIOClient.sendEvent("sendMessage",receiveMessageDTO);
            }else {
                logger.info("目标用户ID {}不在线", receiveMessageDTO.getTargetId());
            }
        }

    }

    @OnEvent("newFriendsNotify")
    public void onNewFriends(SocketIOClient client, JSONObject data){
        logger.info("newFriendsNotify {}",data);
        SocketIOClient socketIOClient = clientMap.get(String.valueOf(data.get("targetId")));
        if(socketIOClient != null){
            socketIOClient.sendEvent("newFriendsNotify",data);
        }
    }
    @OnEvent("newGroupNotify")
    public void newGroupNotify(SocketIOClient client, JSONObject data){
        logger.info("newGroupNotify {}",data);
        SocketIOClient socketIOClient = clientMap.get(String.valueOf(data.get("targetId")));
        if(socketIOClient != null){
            socketIOClient.sendEvent("newGroupNotify",data);
        }
    }


    @OnEvent("candidate")
    public void eventToCandidate(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("eventTOCandidate {}",receiveMessageDTO);
        SocketIOClient target = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
        target.sendEvent("candidate",receiveMessageDTO);

    }

    @OnEvent("offer")
    public void eventToOffer(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("eventToOffer {}",receiveMessageDTO);
        SocketIOClient target = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
        target.sendEvent("offer",receiveMessageDTO);

    }
    @OnEvent("answer")
    public void eventToAnswer(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("eventToOffer {}",receiveMessageDTO);
        SocketIOClient target = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
        target.sendEvent("answer",receiveMessageDTO);
    }

    @OnEvent("1V1CommunicateVideo")
    public void on1V1CommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("1V1CommunicateVideo {}",receiveMessageDTO);
        if(CommonConstant.GROUP_TYPE_FRIENDS.equals(receiveMessageDTO.getTargetType())){
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("1V1CommunicateVideo",receiveMessageDTO);
            }else {
                logger.info("不在线用户：{} ,用户Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline","对方不在线");
            }
        }
    }

    @OnEvent("1V1CommunicatePhone")
    public void on1V1CommunicatePhone(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("1V1CommunicatePhone {}",receiveMessageDTO);
        if(CommonConstant.GROUP_TYPE_FRIENDS.equals(receiveMessageDTO.getTargetType())){
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("1V1CommunicatePhone",receiveMessageDTO);
            }else {
                logger.info("不在线用户：{} ,用户Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline","对方不在线");
            }
        }
    }

    @OnEvent("ManyToManyCommunicateVideo")
    public void onManyToManyCommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("ManyToManyCommunicateVideo {}",receiveMessageDTO);
        if(CommonConstant.GROUP_TYPE_GROUP.equals(receiveMessageDTO.getTargetType())){
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("ManyToManyCommunicateVideo",receiveMessageDTO);
            }else {
                logger.info("不在线用户：{} ,用户Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
//                socketIOClient02.sendEvent("notOnline","对方不在线");
            }
        }
    }


    @OnEvent("onJoinRoom")
    public void onJoinRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("onJoinRoom {}",receiveMessageDTO);
        if(CommonConstant.GROUP_TYPE_GROUP.equals(receiveMessageDTO.getTargetType())){
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("onJoinRoom",receiveMessageDTO);
            }else {
                logger.info("不在线用户：{} ,用户Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
            }
        }
    }
    @OnEvent("onLeftRoom")
    public void onLeftRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("onLeftRoom {}",receiveMessageDTO);
        if(CommonConstant.GROUP_TYPE_GROUP.equals(receiveMessageDTO.getTargetType())){
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("onLeftRoom",receiveMessageDTO);
            }else {
                logger.info("不在线用户：{} ,用户Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
            }
        }
    }
}
