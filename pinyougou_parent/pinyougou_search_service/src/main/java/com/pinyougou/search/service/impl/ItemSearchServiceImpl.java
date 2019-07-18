package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.data.solr.repository.Highlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));
        map.putAll(searchList(searchMap));
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);
        //3.查询品牌和规格列表
        String category= (String) searchMap.get("category");
        if(!category.equals("")){
            map.putAll(searchBrandAndSpecList(category));
        }else{
            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }
        return map;
    }

    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(List goodsIds) {
        SolrDataQuery query=new SimpleQuery();
        Criteria criteria=new Criteria("item_goodsid").in(goodsIds);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


    public Map searchList(Map searchMap) {
        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);
        //关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //商品分类过滤查询
        if (!"".equals(searchMap.get("category"))){
           FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria= new Criteria("item_category").is(searchMap.get("category"));
             filterQuery.addCriteria(filterCriteria);
             query.addFilterQuery(filterQuery);
        }
        //1.3 按品牌过滤
        if(!"".equals(searchMap.get("brand"))  )	{//如果用户选择了品牌
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //按照规格选项过滤
        if (searchMap.get("spec")!=null){

             Map<String,String> specMap = (Map<String,String>) searchMap.get("spec");
            for (String  key : specMap.keySet()) {
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

        }

        //按价格查询
        String priceStr = (String) searchMap.get("price");
        if (!"".equals(priceStr)){
            String [] price =priceStr.split("-");
            if (!price[0].equals("0")){
                   FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
                filterQuery.addCriteria(filterCriteria);
                   query.addFilterQuery(filterQuery);
            }
            if (!price[1].equals("*")){

                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").lessThanEqual(price[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

        }

        //分页查询
        Integer pageNo= (Integer) searchMap.get("pageNo");
        if (searchMap.get("pageNo")==null){
                  pageNo=1;
        }
        Integer pageSize= (Integer) searchMap.get("pageSize");
        if (searchMap.get("pageSize")==null){
            pageSize=20;
        }
        query.setOffset((pageNo-1)*pageSize);
        query.setRows(pageSize);
     //排序
        String sortValue = (String) searchMap.get("sort");
        String sortFiled = (String) searchMap.get("sortFiled");
       if (!StringUtils.isEmpty(sortValue)){
           Sort sort=null;
           if (sortValue.equals("ASC")){
               sort = new Sort(Sort.Direction.ASC,"item_"+sortFiled);
           }else {
               sort = new Sort(Sort.Direction.DESC,"item_"+sortFiled);
           }
           query.addSort(sort);
       }

        //获取高亮结果集
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : entryList) {
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            if (highlightList.size() > 0 && highlightList.get(0).getSnipplets().size() > 0) {
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }



        Map map = new HashMap();
        map.put("rows", page.getContent());
        map.put("totalPage",page.getTotalPages());
        map.put("total",page.getTotalElements());
        return map;
    }




    private List<String> searchCategoryList(Map searchMap) {
        List<String> list = new ArrayList<String>();

        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        List<GroupEntry<TbItem>> entryList = page.getGroupResult("item_category").getGroupEntries().getContent();
        for (GroupEntry<TbItem> entry : entryList) {
            list.add(entry.getGroupValue());
        }
        return list;
    }

    private Map searchBrandAndSpecList(String category) {
        Map map = new HashMap();
        if (!StringUtils.isEmpty(category)) {
            Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
            if (typeId != null) {

                List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
                map.put("brandList", brandList);
                List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
                map.put("specList", specList);

            }
        }
        return map;
    }

}
