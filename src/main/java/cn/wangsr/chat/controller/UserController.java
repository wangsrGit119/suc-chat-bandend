package cn.wangsr.chat.controller;

import cn.wangsr.chat.annotation.IgnoreToken;
import cn.wangsr.chat.common.ResponseData;
import cn.wangsr.chat.model.UserInfoPO;
import cn.wangsr.chat.model.dto.UserSuccessDTO;
import cn.wangsr.chat.service.UserServiceImpl;
import cn.wangsr.chat.util.JwtUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wjl
 */
@RestController
public class UserController {
    @Resource
    UserServiceImpl userService;

    @PostMapping("/register")
    @IgnoreToken
    public ResponseData register(@RequestBody UserInfoPO userInfoPO, HttpServletResponse response){
        return userService.register(userInfoPO);
    }

    @PostMapping("/login")
    @IgnoreToken
    public ResponseData login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        UserSuccessDTO login = userService.login(username, password);
        response.setHeader(JwtUtils.AUTH_HEADER_KEY,login.getToken());
        return ResponseData.ofSuccess("登录成功",login);
    }

    @GetMapping("/loadMessages")
    public ResponseData loadMessages(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam Integer type){
        return ResponseData.ofSuccess("success",userService.loadUserMessageByBindTargetId(userId, targetId, type));
    }

    @GetMapping("/loadUserFriendsPage")
    public ResponseData loadUserFriendsPage(Long userId){
       return ResponseData.ofSuccess("success",userService.loadUserFriendsPage(userId));
    }

    @GetMapping("/searchUser")
    public ResponseData searchUser(Long userId,String username){
        return ResponseData.ofSuccess("success",userService.searchUser(userId, username));
    }

    @PostMapping("/applyAddFriends")
    public ResponseData applyAddFriends(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
        userService.applyAddFriends(userId, targetId, noteName);
        return ResponseData.ofSuccess("申请成功",null);
    }

    @PostMapping("/acceptFriendsApply")
    public ResponseData acceptFriendsApply(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
        userService.acceptFriendsApply(userId, targetId, noteName);
        return ResponseData.ofSuccess("success",null);
    }

//    // TODO
//    @PostMapping("/acceptGroupApply")
//    public ResponseData acceptGroupApply(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
//        userService.acceptFriendsApply(userId, targetId, noteName);
//        return ResponseData.ofSuccess("success",null);
//    }
//
//    // TODO
//    @PostMapping("/applyAddGroup")
//    public ResponseData applyAddGroup(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
//        userService.acceptFriendsApply(userId, targetId, noteName);
//
//        return ResponseData.ofSuccess("success",null);
//    }

    // TODO
    @PostMapping("/createGroup")
    public ResponseData createGroup(@RequestBody Map<String,Object> params){
        return userService.createGroup(params);
    }



    @GetMapping("/loadReceivingFriends")
    public ResponseData loadReceivingFriends(Long userId){
        return ResponseData.ofSuccess("success",userService.loadReceivingFriends(userId));
    }

    @GetMapping("/loadGroupUserInfo")
    public ResponseData loadGroupUserInfo(Long groupId){
        return ResponseData.ofSuccess("success",userService.loadGroupUserInfo(groupId));
    }
}
