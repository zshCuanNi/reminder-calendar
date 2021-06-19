package cc.kinami.template.dao;

import cc.kinami.template.model.po.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
//    @Insert("insert into rhq_table(test) values(#{test})")
//    int insertUser(User user);
    @Select("select * from user where username = #{username}")
    List<User> selectUserByName(String username);

    @Insert("insert into user(username, password) values(#{username}, #{password})")
    void insertUser(String username, String password);

    @Select("select * from User where username = #{username} and password = #{password}")
    List<User> selectUserByNamePwd(String username, String password);

    @Select("select uid from User where username = #{username}")
    int selectUidByName(String username);

//    @Select("select * from rhq_table")
//    List<User> selectAll();
//
//    @Select("select * from rhq_table where test = #{test}")
//    List<User> selectByTest(Integer test);
//
//    @Delete("delete from rhq_table where 1=1")
//    void deleteAll();

}
