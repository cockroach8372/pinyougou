package cn.itcast;

import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest(classes = SpringDataJpaApplication.class)
public class SpringBootTest {
   @Autowired
   private CustomerDao customerDao ;
   @Test
   public void insert(){
      Customer customer=new Customer();
      customer.setCustName("222");
      customerDao.save(customer);
   }

   @Test
    public void findAll(){
       List<Customer> all = customerDao.findAll();
       System.out.println(all);
   }

}
