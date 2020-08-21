package cn.wangsr.chat.dao;


import cn.wangsr.chat.model.UserFriendsPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author wjl
 */
@Repository
public interface UserFriendsRepository extends JpaRepository<UserFriendsPO,Long>,QuerydslPredicateExecutor<UserFriendsPO> {
}
