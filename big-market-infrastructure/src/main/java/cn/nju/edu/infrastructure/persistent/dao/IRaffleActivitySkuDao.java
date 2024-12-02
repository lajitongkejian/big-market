package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivitySkuDao
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryRaffleActivitySkuBySku(Long sku);
}