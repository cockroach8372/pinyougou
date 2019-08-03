package cn.itcast;

import cn.itcast.dao.CustomerDao;
import cn.itcast.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpecTest {
    @Autowired
    private CustomerDao customerDao;
    @Test
    public void testSpec(){
        Specification<Customer>spec=new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> custName = root.get("custName");
                Predicate predicate = cb.equal(custName, "孙悟空1");
                return predicate;
            }
        };
       Customer customer = customerDao.findOne(spec);
        System.out.println(customer);
    }


    @Test
    public void testSpec1(){
        Specification<Customer>spec=new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> custName = root.get("custName");
                Path<Object> custIndustry = root.get("custIndustry");
                Predicate predicate1= cb.equal(custName, "孙悟空1");
                Predicate predicate2 = cb.equal(custIndustry, "佛教");
                Predicate and = cb.and(predicate1, predicate2);
                return and;
            }
        };
        Customer customer = customerDao.findOne(spec);
        System.out.println(customer);
    }

    @Test
    public void testSpec3(){
        Specification<Customer>spec=new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> custName = root.get("custName");
                Predicate predicate1= cb.like(custName.as(String.class), "孙悟空%");
                return predicate1;
            }
        };
        List <Customer> customer = customerDao.findAll(spec);
        System.out.println(customer);
    }


    @Test
    public void testSpec4(){
        Specification spec = null;
        Pageable pageable= new PageRequest(0,2);
        Page<Customer> page = customerDao.findAll(null, pageable);
        System.out.println(page.getContent()); //得到数据集合列表
        System.out.println(page.getTotalElements());//得到总条数
        System.out.println(page.getTotalPages());//得到总页数
    }
}
