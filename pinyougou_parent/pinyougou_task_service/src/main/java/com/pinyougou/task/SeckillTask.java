package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SeckillTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Scheduled(cron = "0/10 * * * * ?")
    public void refreshSeckillGoods(){
        System.out.println("执行了任务调度"+new Date());
        ArrayList goodsList = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        System.out.println(goodsList);
        TbSeckillGoodsExample example=new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        criteria.andStockCountGreaterThan(0);
        criteria.andEndTimeGreaterThan(new Date());
        criteria.andStartTimeLessThan(new Date());
        if (goodsList.size()>0){

            criteria.andIdNotIn(goodsList);//排除缓存中已经有的商品
        }
        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
        for (TbSeckillGoods tbSeckillGoods : seckillGoodsList) {
                  redisTemplate.boundHashOps("seckillGoods").put(tbSeckillGoods.getId(),tbSeckillGoods);
        }
        System.out.println("将"+seckillGoodsList.size()+"条加入缓存");

    }
    //移除过期商品
    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods(){
        List<TbSeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        System.out.println(seckillGoodsList);
        System.out.println("执行了清除秒杀商品的任务"+new Date());
        for (TbSeckillGoods seckillGoods : seckillGoodsList) {
                if (seckillGoods.getEndTime().getTime()<new Date().getTime()){
                        seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
                        redisTemplate.boundHashOps("seckillGoods").delete(seckillGoods.getId());
                    System.out.println("秒杀商品"+seckillGoods.getId()+"已过期");
                }
        }
        System.out.println("执行了清除秒杀商品的任务...end");

    }
}
