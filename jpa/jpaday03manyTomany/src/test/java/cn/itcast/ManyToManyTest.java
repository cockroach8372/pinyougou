package cn.itcast;

import cn.itcast.dao.RoleDao;
import cn.itcast.dao.UserDao;
import cn.itcast.domain.Role;
import cn.itcast.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ManyToManyTest {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;
    @Test
    @Transactional
    @Rollback(value = false)
    public void add(){
        User user = new User();
        user.setUserName("zhangsan");
        Role role = new Role();
        role.setRoleName("程序员");
        user.getRoles().add(role);
        userDao.save(user);
        roleDao.save(role);

    }

}
