import com.diyiliu.bean.User;
import com.diyiliu.bll.service.BaseService;
import com.diyiliu.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Description: PluginTest
 * Author: DIYILIU
 * Update: 2016-08-19 11:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = {"classpath:jdbc.properties"})
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PluginTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDao userDao;

    @Resource
    private BaseService userService;

    @Test
    public void testInsert(){

        User user = new User("测试", 22);

        //userDao.insertEntity(user);
        userService.insert(user);

        logger.debug("testInsert ... ok！");
    }

    @Test
    public void testDelete(){
        User user = new User();
        user.setId(22);

        userService.delete(user);

        logger.debug("testInsert ... ok！");
    }
}
