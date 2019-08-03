package cn.itcast.dao;

import cn.itcast.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
   @Query(value = "from Customer where custName = ?")
    public Customer findJpql(String custName);

   @Query(value = "from Customer where  custName=?2 and custId= ?1")
    public Customer findCustomerAndCustId(Long id, String name);

    @Query(value = "from Customer where  custName=?2 or custId= ?1")
    public List<Customer> findList(Long id, String name);
    @Query(value = "update Customer set custName=?2 where custId=?1")
    @Modifying
    public void updateCustomer(Long id, String custName);
  @Query("delete from Customer  where custId =?")
  @Modifying
    public void  dele(Long id);

    /**
     * 测试sql查询
     */
    @Query(value = "select  * from cst_customer where cust_name=? ",nativeQuery = true)
    public List<Customer> findSql(String name);


    public Customer findByCustName(String custName);

    /**
     * 模糊查询
     */
    public List<Customer> findByCustNameLike(String custName);

    public List<Customer>  findByCustNameOrCustId(String custName, Long custId);
}
