package cn.itcast.daoTest;

import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JpqlDemo {
    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindJpql(){
        System.out.println(customerDao.findJpql("孙悟空"));
    }
    @Test
    public void testFindCustomerAndCustId(){
        System.out.println(customerDao.findCustomerAndCustId(2L,"孙悟空"));
    }
    @Test
    public void findList(){
        List <Customer>  daoList = customerDao.findList(1L, "孙悟空");
        for (Object o : daoList) {
            System.out.println(o);
        }
    }
    @Test
    @Transactional //添加事务的支持
    @Rollback(value = false)
    public void updateCustomer(){
      customerDao.updateCustomer(1L,"唐僧1");
    }
    /**
     * 删除
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testDele(){
        customerDao.dele(4L);
    }
    /**
     * 测试sql
     */
    @Test
    public void testSql(){
        List<Customer> sql = customerDao.findSql("唐僧1");
        System.out.println(sql);
    }

    @Test
    public void testSql2(){
        Customer 猪八戒 = customerDao.findByCustName("猪八戒");
        System.out.println(猪八戒);
    }


    @Test
    public void test3(){
        List<Customer> byCustNameLike = customerDao.findByCustNameLike("唐%");
        System.out.println(byCustNameLike);
    }

    @Test
    public void test4(){
        List<Customer> byCustNameLike = customerDao.findByCustNameOrCustId("唐僧1",3L);
        System.out.println(byCustNameLike);
    }
}
