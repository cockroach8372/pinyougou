import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class demo {
   @Autowired
    private SolrTemplate solrTemplate;

   @Test
    public void testAdd(){
       TbItem item = new TbItem();
       item.setId(1L);
       item.setBrand("华为");
       item.setCategory("手机");
       item.setGoodsId(1L);
       item.setSeller("华为 2 号专卖店");
       item.setTitle("华为 Mate9");
       item.setPrice(new BigDecimal(2000));
       solrTemplate.saveBean(item);
       solrTemplate.commit();
   }

   @Test
   public void testFindOne(){
      TbItem item = solrTemplate.getById(1, TbItem.class);
      System.out.println(item);

   }
   @Test
   public void dele(){
      solrTemplate.deleteById("1");
      solrTemplate.commit();
}
   @Test
   public void testAddList(){
      List list=new ArrayList();
      for (int i = 0; i <100 ; i++) {
         TbItem item = new TbItem();
         item.setId(i+1L);
         item.setBrand("华为");
         item.setCategory("手机");
         item.setGoodsId(1L);
         item.setSeller("华为 2 号专卖店");
         item.setTitle("华为 Mate"+i);
         item.setPrice(new BigDecimal(2000));
         list.add(item);
      }
      solrTemplate.saveBeans(list);
      solrTemplate.commit();
   }

   @Test
   public void testPageQuery(){
      Query query=new SimpleQuery("*:*");
      query.setOffset(1);//开始索引（默认 0）
      query.setRows(20);//每页记录数(默认 10)
      ScoredPage<TbItem>page = solrTemplate.queryForPage(query, TbItem.class);
      System.out.println("总记录数："+page.getTotalElements());
      List<TbItem>list = page.getContent();
      System.out.println(page.getTotalPages());
      System.out.println(page.getSize());

      showList(list);
   }
//显示记录数据
   private void showList(List<TbItem>list){
      for(TbItem item:list){
         System.out.println(item.getTitle() +","+item.getPrice());
      }
   }

   @Test
   public void testQueryMutil(){
      Query query=new SimpleQuery("*:*");
      query.setOffset(1);
      query.setRows(50);
      Criteria criteria=new Criteria("item_category").contains("手");
      criteria.and("item_brand").is("华为");
      query.addCriteria(criteria);
      ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
      List<TbItem>list = page.getContent();
      System.out.println(page.getSize());
      showList(list);

   }
   @Test
   public void dele1(){
      Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
   }
}
