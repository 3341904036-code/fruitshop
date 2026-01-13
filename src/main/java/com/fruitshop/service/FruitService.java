package com.fruitshop.service;

import com. fruitshop.entity. Fruit;
import java.util.List;

/**
 * 水果服务接口
 */
public interface FruitService {

    /**
     * 新增水果
     */
    boolean addFruit(Fruit fruit);

    /**
     * 修改水果信息
     */
    boolean updateFruit(Fruit fruit);

    /**
     * 删除水果
     */
    boolean deleteFruit(String fId);

    /**
     * 查询单个水果
     */
    Fruit getFruitById(String fId);

    /**
     * 查询所有水果
     */
    List<Fruit> getAllFruits();

    /**
     * 根据供应商查询水果
     */
    List<Fruit> getFruitsBySupplier(Integer sId);

    /**
     * 根据名称模糊查询水果
     */
    List<Fruit> searchFruitByName(String name);

    /**
     * 查询库存不足的水果
     */
    List<Fruit> getLowStockFruits(Integer minQty);

    /**
     * 更新库存
     */
    boolean updateQuantity(String fId, Integer qty);
}