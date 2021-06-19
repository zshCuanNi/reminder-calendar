package cc.kinami.template.service;

import cc.kinami.template.dao.MemoDao;
import cc.kinami.template.dao.UserDao;
import cc.kinami.template.model.po.DayMemo;
import cc.kinami.template.model.po.Memo;
import cc.kinami.template.model.po.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TestService {

    UserDao userDao;
    MemoDao memoDao;

    public String registerCheck(String username, String password) {
        List<User> userList = userDao.selectUserByName(username);
        if (userList.isEmpty()) {
            userDao.insertUser(username, password);
        } else {
            return "注册失败";
        }
        return "注册成功";
    }

    public String loginCheck(String username, String password) {
        List<User> userList = userDao.selectUserByNamePwd(username, password);
        if (userList.isEmpty()) {
            return "登录失败";
        }
        return "登录成功";
    }

    public String newMemo(String username, String deadline, String headline, String detail) {
        int uid = userDao.selectUidByName(username);
        memoDao.insertMemo(uid, deadline, headline, detail);
        int id = memoDao.selectMemo(uid, deadline, headline, detail);
        return "添加成功，备忘序号为" + id;
    }

    public String editMemo(int id, String deadline, String headline, String detail) {
        memoDao.updateMemo(id, deadline, headline, detail);
        return "修改成功";
    }

    public String deleteMemo(int id) {
        memoDao.deleteMemo(id);
        return "删除成功";
    }

    public List<Memo> selectAllMemos(String username) {
        int uid = userDao.selectUidByName(username);
        return memoDao.selectAll(uid);
    }

    public List<DayMemo> selectDayMemos(String username, String deadline) throws ParseException {
        int uid = userDao.selectUidByName(username);
        String d1 = deadline + " 0:00";
        String d2 = deadline + " 23:59:59";
        return memoDao.selectDay(uid, d1, d2);
    }

    //    public void insertUsers(List<Integer> userList) {
//        for (Integer user : userList)
//            userDao.insertUser(new User(user));
//    }
//    public void insertUser(String username, String password) {
//        userDao.insertUser(username, password);
//    }
//
//    public List<User> selectAllUsers() {
//        return userDao.selectAll();
//    }
//
//    public List<User> selectUserByTest(int test) {
//        return userDao.selectByTest(test);
//    }
}
