package cn.wangsr.chat;

import cn.wangsr.chat.dao.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
class SucChatBackendApplicationTests {

    @Resource
    GroupRepository groupRepository;
    @Test
    @Transactional
    void contextLoads() {
        System.out.println(groupRepository.getOne(1L));
    }

}
