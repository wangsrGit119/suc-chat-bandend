package cn.wangsr.chat.controller;

import cn.wangsr.chat.annotation.IgnoreToken;
import cn.wangsr.chat.common.ResponseData;
import cn.wangsr.chat.model.dto.UserSuccessDTO;
import cn.wangsr.chat.service.UserServiceImpl;
import cn.wangsr.chat.util.JwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wjl
 */
@RestController
public class UserController {
    @Resource
    UserServiceImpl userService;

    @PostMapping("/login")
    @IgnoreToken
    public ResponseData login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        UserSuccessDTO login = userService.login(username, password);
        response.setHeader(JwtUtils.AUTH_HEADER_KEY,login.getToken());
        return ResponseData.ofSuccess("登录成功",login);
    }

    @GetMapping("/loadMessages")
    @IgnoreToken
    public ResponseData loadMessages(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam Integer type){
        return ResponseData.ofSuccess("success",userService.loadUserMessageByBindTargetId(userId, targetId, type));
    }

    @GetMapping("/loadUserFriendsPage")
    @IgnoreToken
    public ResponseData loadUserFriendsPage(Long userId){
       return ResponseData.ofSuccess("success",userService.loadUserFriendsPage(userId));
    }

    @GetMapping("/searchUser")
    @IgnoreToken
    public ResponseData searchUser(Long userId,String username){
        return ResponseData.ofSuccess("success",userService.searchUser(userId, username));
    }

    @PostMapping("/applyAddFriends")
    @IgnoreToken
    public ResponseData applyAddFriends(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
        userService.applyAddFriends(userId, targetId, noteName);
        return ResponseData.ofSuccess("申请成功",null);
    }

    @PostMapping("/acceptFriendsApply")
    @IgnoreToken
    public ResponseData acceptFriendsApply(@RequestParam Long userId,@RequestParam Long targetId,@RequestParam(required = false) String noteName){
        userService.acceptFriendsApply(userId, targetId, noteName);
        return ResponseData.ofSuccess("success",null);
    }

    @GetMapping("/loadReceivingFriends")
    @IgnoreToken
    public ResponseData loadReceivingFriends(Long userId){
        return ResponseData.ofSuccess("success",userService.loadReceivingFriends(userId));
    }

}
