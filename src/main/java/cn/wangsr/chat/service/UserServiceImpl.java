package cn.wangsr.chat.service;

import cn.wangsr.chat.common.CommonConstant;
import cn.wangsr.chat.common.GlobalException;
import cn.wangsr.chat.dao.GroupRepository;
import cn.wangsr.chat.dao.UserFriendsRepository;
import cn.wangsr.chat.dao.UserRepository;
import cn.wangsr.chat.listener.SucEventListener;
import cn.wangsr.chat.model.*;
import cn.wangsr.chat.model.QUserFriendsPO;
import cn.wangsr.chat.model.QUserGroupPO;
import cn.wangsr.chat.model.QUserInfoPO;
import cn.wangsr.chat.model.QUserMessagePO;
import cn.wangsr.chat.model.dto.*;
import cn.wangsr.chat.util.JwtUtils;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjl
 */
@Transactional(rollbackFor = {Exception.class,GlobalException.class})
@Service
public class UserServiceImpl {

    @Resource
    JPAQueryFactory jpaQueryFactory;
    @Resource
    GroupRepository groupRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    UserFriendsRepository userFriendsRepository;


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public UserSuccessDTO login(String username,String password){
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;

        UserSuccessDTO userSuccessDTO = jpaQueryFactory.select(
                Projections.bean(UserSuccessDTO.class,
                        qUserInfoPO.id.as("userId"),
                        qUserInfoPO.username,
                        qUserInfoPO.nickname,
                        qUserInfoPO.avatarUrl,
                        qUserInfoPO.email
                        )
        )
                .where(qUserInfoPO.username.eq(username).and(qUserInfoPO.password.eq(DigestUtils.md5DigestAsHex(password.getBytes()))))
                .from(qUserInfoPO)
                .fetchOne();
        if(StringUtils.isEmpty(userSuccessDTO)){
           throw new GlobalException(400,"用户名或密码错误");
        }
        String jwt = JwtUtils.createJwt(userSuccessDTO.getUsername(), userSuccessDTO.getUserId());
        userSuccessDTO.setToken(jwt);
        return userSuccessDTO;
    }

    /**
     * 搜索用户 精确搜索
     * @param userId 搜索人Id
     * @param username 目标名称
     * @return
     */
    public SearchUserDTO searchUser(Long userId,String username){
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;
        QUserFriendsPO qUserFriendsPO = QUserFriendsPO.userFriendsPO;
        //查询用户
        SearchUserDTO searchUserDTO = jpaQueryFactory.select(
                Projections.bean(SearchUserDTO.class,
                        qUserInfoPO.id.as("userId"),
                        qUserInfoPO.username,
                        qUserInfoPO.nickname,
                        qUserInfoPO.avatarUrl,
                        qUserInfoPO.email)
        )
                .from(qUserInfoPO)
                .where(qUserInfoPO.username.eq(username)).fetchOne();
        if(searchUserDTO != null){
            //判断是否为自己的好友
            UserFriendsPO userFriendsPO = jpaQueryFactory.select(qUserFriendsPO)
                    .from(qUserFriendsPO)
                    .where(qUserFriendsPO.userId.eq(userId).and(qUserFriendsPO.partnerId.eq(searchUserDTO.getUserId())))
                    .fetchOne();
            if(null != userFriendsPO){
                searchUserDTO.setFriendsOrNot(true);
            }else {
                searchUserDTO.setFriendsOrNot(false);
            }
        }
        return searchUserDTO;
    }

    /**
     * 加载聊天记录（自己与好友或群组）（最近的20条或者10条）
     * @param uid  当前用户id
     * @param targetId 选择用户id
     *  @param type 1.用户 2.群组
     * @return
     */
    public List<MessageDTO> loadUserMessageByBindTargetId(Long uid,Long targetId,Integer type){
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;
        QUserMessagePO qUserMessagePO = QUserMessagePO.userMessagePO;
        List<MessageDTO> messageDTOList = new ArrayList<>();
        //点对点
        if(CommonConstant.GROUP_TYPE_FRIENDS.equals(type)){
            //加载自己发送的
            List<MessageDTO> messageDTOS01 = this.loadFriendsMessage(uid, targetId,20);
            messageDTOList.addAll(messageDTOS01);
            //加载对方发送的
            List<MessageDTO> messageDTOS02 = this.loadFriendsMessage(targetId, uid,20);
            messageDTOList.addAll(messageDTOS02);

            //群聊消息
        }else {
            //获取群内所有用户
            UserGroupPO userGroupPO = groupRepository.getOne(targetId);
            String[] users = userGroupPO.getGroupUsersIds().split(CommonConstant.CHAR_CHINESE_DUN);
            for (String userId : users) {
                List<MessageDTO> messageDTOS = loadFriendsMessage(Long.valueOf(userId), targetId, 10);
                messageDTOList.addAll(messageDTOS);
            }
        }
        //按照时间排序
        List<MessageDTO> messageDTOSResult = messageDTOList.stream().sorted(Comparator.comparing(MessageDTO::getCreateTime)).collect(Collectors.toList());
        return messageDTOSResult;
    }

    public List<MessageDTO> loadFriendsMessage(Long uid,Long targetId,Integer pageSize){
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;
        QUserMessagePO qUserMessagePO = QUserMessagePO.userMessagePO;
        List<MessageDTO> messageDTOS = jpaQueryFactory.select(
                Projections.bean(MessageDTO.class,
                        qUserInfoPO.nickname.as("username"),
                        qUserInfoPO.avatarUrl,
                        qUserMessagePO.imageUrl,
                        qUserMessagePO.message,
                        qUserMessagePO.createTime)
        )
                .from(qUserMessagePO)
                .leftJoin(qUserInfoPO)
                .on(qUserInfoPO.id.eq(qUserMessagePO.userId))
                .where(qUserMessagePO.userId.eq(uid).and(qUserMessagePO.bindTarget.eq(targetId)))
                .limit(pageSize)
                .fetch();
        return messageDTOS;
    }



    /**
     * 加载用户通讯录
     * @param userId
     * @return
     */
    public Object loadUserFriendsPage(Long userId){
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;
        QUserFriendsPO qUserFriendsPO = QUserFriendsPO.userFriendsPO;
        QUserGroupPO qUserGroupPO = QUserGroupPO.userGroupPO;
        List<UserFriendsPageDTO> userFriendsPageDTOS01 = jpaQueryFactory.select(
                Projections.bean(
                        UserFriendsPageDTO.class,
                        qUserFriendsPO.partnerId.as("userId"),
                        qUserFriendsPO.noteName.as("chatName"),
                        qUserInfoPO.avatarUrl)
        )
                .from(qUserFriendsPO)
                .leftJoin(qUserInfoPO)
                .on(qUserFriendsPO.partnerId.eq(qUserInfoPO.id))
                //已同意好友
                .where(qUserFriendsPO.userId.eq(userId).and(qUserFriendsPO.type.eq(CommonConstant.RECEIVED)))
                .fetch();
        for (UserFriendsPageDTO userFriendsPageDTO : userFriendsPageDTOS01) {
            userFriendsPageDTO.setType(CommonConstant.GROUP_TYPE_FRIENDS);
        }
        //2.加载所在群组
        List<UserFriendsPageDTO> userFriendsPageDTOS02 = jpaQueryFactory.select(
                Projections.bean(UserFriendsPageDTO.class,
                        qUserGroupPO.id.as("userId"),
                        qUserGroupPO.groupName.as("chatName"),
                        qUserGroupPO.avatarUrl)
        )
                .from(qUserGroupPO)
                .where(qUserGroupPO.groupUsersIds.contains(String.valueOf(userId)))
                .fetch();
        for (UserFriendsPageDTO userFriendsPageDTO : userFriendsPageDTOS02) {
            userFriendsPageDTO.setType(CommonConstant.GROUP_TYPE_GROUP);
        }
        userFriendsPageDTOS01.addAll(userFriendsPageDTOS02);
        return userFriendsPageDTOS01;
    }


    /**
     * 申请加对方为好友
     * @param userId
     * @param targetId 目标用户Id
     * @param noteName 目标用户备注名称
     */
    public void applyAddFriends(Long userId,Long targetId,String noteName){
        List<UserFriendsPO> userFriendsPOS = new ArrayList<>();
        UserInfoPO targetUserInfoPO = userRepository.getOne(targetId);
        QUserFriendsPO qUserFriendsPO = QUserFriendsPO.userFriendsPO;

        //检查是否已有申请
        UserFriendsPO friendsPO = jpaQueryFactory.select(qUserFriendsPO)
                .from(qUserFriendsPO)
                .where(qUserFriendsPO.userId.eq(userId)
                        .and(qUserFriendsPO.partnerId.eq(targetId)
                                .and(qUserFriendsPO.type.eq(CommonConstant.RECEIVING)))
                        .and(qUserFriendsPO.belong.eq(CommonConstant.BELONG_OWN)))
                .fetchOne();
        if(null !=friendsPO){
            throw new GlobalException(400,"已申请，请勿重复申请");
        }


        //申请人好友信息
        UserFriendsPO userFriendsPO = UserFriendsPO.builder()
                .userId(userId)
                .noteName(targetUserInfoPO.getNickname())
                .type(CommonConstant.RECEIVING)
                .belong(CommonConstant.BELONG_OWN)
                .partnerId(targetId).build();
        userFriendsPOS.add(userFriendsPO);
        //被申请人好友信息
        UserFriendsPO userFriendsPO1 = UserFriendsPO.builder()
                .userId(targetId)
                .type(CommonConstant.RECEIVING)
                .belong(CommonConstant.BELONG_TARGET)
                .partnerId(userId).build();
        userFriendsPOS.add(userFriendsPO1);
        SocketIOClient socketIOClient = SucEventListener.clientMap.get(targetId.toString());
        if(socketIOClient != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","您有新的好友申请");
            System.out.println("已通知客户端");
            socketIOClient.sendEvent("newFriendsNotify",jsonObject);
        }
        userFriendsRepository.saveAll(userFriendsPOS);
    }

    /**
     * 同意好友请求
     * @param userId  处理申请用户id
     * @param targetId 发起申请用户Id
     * @param noteName 申请用户备注
     */
    public void acceptFriendsApply(Long userId,Long targetId,String noteName){
        QUserFriendsPO qUserFriendsPO = QUserFriendsPO.userFriendsPO;
        UserInfoPO targetUserInfoPO = userRepository.getOne(targetId);
        //更新自己数据
        long execute01 = jpaQueryFactory.update(qUserFriendsPO).
                set(qUserFriendsPO.type, CommonConstant.RECEIVED)
                //朋友备注
                .set(qUserFriendsPO.noteName, targetUserInfoPO.getNickname())
                .where(qUserFriendsPO.userId.eq(userId)
                        .and(qUserFriendsPO.partnerId.eq(targetId))
                        .and(qUserFriendsPO.type.eq(CommonConstant.RECEIVING)))
                .execute();
        //更新发起方用户数据
        long execute02 = jpaQueryFactory.update(qUserFriendsPO).
                set(qUserFriendsPO.type, CommonConstant.RECEIVED)
                .where(qUserFriendsPO.userId.eq(targetId)
                        .and(qUserFriendsPO.partnerId.eq(userId))
                        .and(qUserFriendsPO.type.eq(CommonConstant.RECEIVING)))
                .execute();
        if((execute01+execute02) != 2){
            throw new GlobalException(400,"操作失败");
        }
    }

    /**
     * 加载用户待处理好友申请信息
     * @param userId
     * @return
     */
    public List<FriendsUserDTO> loadReceivingFriends(Long userId){
        QUserFriendsPO qUserFriendsPO = QUserFriendsPO.userFriendsPO;
        QUserInfoPO qUserInfoPO = QUserInfoPO.userInfoPO;
        List<FriendsUserDTO> friendsUserDTOS = jpaQueryFactory.select(
                Projections.bean(FriendsUserDTO.class,
                        qUserFriendsPO.partnerId.as("userId"),
                        qUserInfoPO.username,
                        qUserInfoPO.nickname,
                        qUserInfoPO.email,
                        qUserInfoPO.avatarUrl)
        )
                .from(qUserFriendsPO)
                .leftJoin(qUserInfoPO)
                .on(qUserFriendsPO.userId.eq(qUserInfoPO.id))
                .where(qUserFriendsPO.userId.eq(userId)
                        .and(qUserFriendsPO.type.eq(CommonConstant.RECEIVING))
                        .and(qUserFriendsPO.belong.eq(CommonConstant.BELONG_TARGET)))
                .fetch();
        return friendsUserDTOS;
    }



}
