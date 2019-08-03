package cn.itcast.daoTest;

import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Demo {
    @Autowired
    private CustomerDao customerDao;

    /**
     * 根据id查询，立即查询
     */
    @Test
    public void testFindOne(){
        Customer customer = customerDao.findOne(3L);
        System.out.println(customer);
    }
    /**
     * 根据id查询 ,掩饰查询
     */
    @Test
    public void testGetOne(){
        Customer customer = customerDao.getOne(3L);
        System.out.println(customer);
    }

    /**
     * 保存
     */
    @Test
    public void testSave(){
        Customer customer = new Customer();
        customer.setCustName("孙悟空");
        customer.setCustIndustry("佛教");
        customer.setCustPhone("110");
        customerDao.save(customer);
    }

    /**
     * 修改，根据有五id，有id修改，没有删除
     */
    @Test
    public void testUpdate(){
        Customer customer = new Customer();
        customer.setCustName("唐僧");
        customer.setCustId(1L);
        customerDao.save(customer);
    }
    /**
     * 查询所有
     */
    @Test
    public void testFindAll(){
        List<Customer> all = customerDao.findAll();
        System.out.println(all);
    }
    /**
     * 测试统计查询
     */
    @Test
    public void testCount(){
        long count = customerDao.count();
        System.out.println(count);
    }
    /**
     * 判断id为1的客户是不是存在
     */
   @Test
    public void testIfExists(){
       boolean result = customerDao.exists(1L);
       System.out.println(result);
   }


}
