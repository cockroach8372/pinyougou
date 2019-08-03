package cn.itcast.test;

import cn.itcast.domain.Customer;
import cn.itcast.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class JpaTest {
    @Test
    public void test(){
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
//        EntityManager em = factory.createEntityManager();
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = new Customer();
        customer.setCustName("周杰伦");
        customer.setCustPhone("13359872586");
         em.persist(customer);
        tx.commit();
        em.close();
//        factory.close();
    }
    @Test
    public void find(){
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 1L);
        System.out.println(customer);
        tx.commit();
        em.close();
    }
    @Test
    public void testReference(){
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.getReference(Customer.class, 1L);
        System.out.println(customer);
        tx.commit();
        em.close();
    }
    @Test
    public void testdele(){
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 2L);
        em.remove(customer);
        tx.commit();
        em.close();
    }
    @Test
    public void testUpdate(){
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = em.find(Customer.class, 1L);
        customer.setCustIndustry("教育");
        em.merge(customer);
        tx.commit();
        em.close();
    }



}
