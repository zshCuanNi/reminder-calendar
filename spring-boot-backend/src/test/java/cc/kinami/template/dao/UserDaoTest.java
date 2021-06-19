package cc.kinami.template.dao;

import cc.kinami.template.TemplateApplication;
import cc.kinami.template.model.po.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TemplateApplication.class)
public class UserDaoTest {

    @Autowired
    UserDao userDao;

//    @Test
//    public void test() {
//        userDao.deleteAll();
//        userDao.insertUser(new User(234));
//        userDao.insertUser(new User(246));
//        userDao.insertUser(new User(234));
//        System.out.println(userDao.selectAll());
//        System.out.println(userDao.selectByTest(234));
//        userDao.deleteAll();
//    }
}
