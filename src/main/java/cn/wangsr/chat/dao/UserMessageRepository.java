package cn.wangsr.chat.dao;


import cn.wangsr.chat.model.UserMessagePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author wjl
 */
@Repository
public interface UserMessageRepository extends JpaRepository<UserMessagePO,Long>,QuerydslPredicateExecutor<UserMessagePO> {
}
