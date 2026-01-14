package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.dao.mapper.FruitMapper;
import com.fruitshop.entity.Fruit;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.service.FruitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 水果服务实现类
 */
@Slf4j
@Service
@Transactional
public class FruitServiceImpl implements FruitService {

    @Autowired
    private FruitMapper fruitMapper;

    @Override
    public boolean addFruit(Fruit fruit) {
        log.info("新增水果: fName={}, sId={}", fruit.getFName(), fruit.getSId());

        if (fruit.getQuantity() == null) {
            fruit.setQuantity(1000);
        }

        int result = fruitMapper.insert(fruit);
        log.info("水果新增成功: fId={}", fruit.getFId());
        return result > 0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) {
        log.info("修改水果: fId={}", fruit.getFId());

        Fruit existingFruit = fruitMapper.selectById(fruit.getFId());
        if (existingFruit == null) {
            throw new BusinessException("水果不存在");
        }

        int result = fruitMapper.updateById(fruit);
        log.info("水果修改成功: fId={}", fruit.getFId());
        return result > 0;
    }

    @Override
    public boolean deleteFruit(String fId) {
        log.info("删除水果: fId={}", fId);

        int result = fruitMapper.deleteById(fId);
        log.info("水果删除成功: fId={}", fId);
        return result > 0;
    }

    @Override
    public Fruit getFruitById(String fId) {
        return fruitMapper.selectById(fId);
    }

    /**
     * 分页查询所有水果（核心修复：支持分页）
     */
    @Override
    public IPage<Fruit> getAllFruits(Integer page, Integer pageSize) {
        Page<Fruit> pageParam = new Page<>(page, pageSize);
        return fruitMapper.selectPage(pageParam, null);
    }

    /**
     * 不分页查询所有水果（兼容旧逻辑）
     */
    @Override
    public List<Fruit> getAllFruits() {
        return fruitMapper.selectList(null);
    }

    @Override
    public List<Fruit> getFruitsBySupplier(Integer sId) {
        return fruitMapper.selectBySupplier(sId);
    }

    @Override
    public List<Fruit> searchFruitByName(String name) {
        return fruitMapper.selectByNameLike(name);
    }

    @Override
    public List<Fruit> getLowStockFruits(Integer minQty) {
        return fruitMapper.selectLowStock(minQty);
    }

    @Override
    public int count() {
        return fruitMapper.selectCount(null) == null ? 0 : fruitMapper.selectCount(null).intValue();
    }

    /**
     * 更新库存（核心修复：支持增加库存，仅校验qty非负）
     */
    @Override
    public boolean updateQuantity(String fId, Integer qty) {
        log.info("更新库存: fId={}, qty={}", fId, qty);

        // 1. 校验参数合法性
        if (qty < 0) {
            throw new BusinessException("库存数量不能为负数");
        }

        // 2. 查询水果是否存在
        Fruit fruit = fruitMapper.selectById(fId);
        if (fruit == null) {
            throw new BusinessException("水果不存在");
        }

        // 3. 移除“库存不足”判断，支持自由设置库存（增加/减少）
        int result = fruitMapper.updateQuantity(fId, qty);
        return result > 0;
    }
}