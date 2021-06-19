package cc.kinami.template.dao;

import cc.kinami.template.model.po.DayMemo;
import cc.kinami.template.model.po.Memo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoDao {
//    @Insert("insert into rhq_table(test) values(#{test})")
//    int insertUser(User user);

    @Insert("insert into memos(uid, deadline, headline, detail) values(#{uid}, #{deadline}, #{headline}, #{detail})")
    void insertMemo(int uid, String deadline, String headline, String detail);

    @Select("select id from memos where uid = #{uid} and deadline = #{deadline} and headline = #{headline} and detail = #{detail}")
    int selectMemo(int uid, String deadline, String headline, String detail);

    @Update("update memos set deadline = #{deadline}, headline = #{headline}, detail = #{detail} where id = #{id}")
    void updateMemo(int id, String deadline, String headline, String detail);

    @Delete("delete from memos where id = #{id}")
    void deleteMemo(int id);

    @Select("select id, deadline, headline, detail from memos where uid = #{uid}")
    List<Memo> selectAll(int uid);

    @Select("select id, headline, detail from memos where uid = #{uid} and deadline > #{d1} and deadline < #{d2}")
    List<DayMemo> selectDay(int uid, String d1, String d2);

//    @Select("select * from memos where uid = #{uid}")
//    List<Memo> selectAll(int uid);

}
