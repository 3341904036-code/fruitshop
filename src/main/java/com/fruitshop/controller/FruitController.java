package com.fruitshop.controller;

import com.fruitshop.entity.Fruit;
import com. fruitshop.exception.BusinessException;
import com. fruitshop.service.FruitService;
import com. fruitshop.util.ResponseUtil;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 水果控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/fruit")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FruitController {

    @Autowired
    private FruitService fruitService;

    /**
     * 新增水果
     */
    @PostMapping("/add")
    public ResponseUtil<? > addFruit(@RequestBody Fruit fruit) {
        try {
            log.info("新增水果请求: fName={}", fruit.getFName());

            boolean result = fruitService.addFruit(fruit);

            if (result) {
                log.info("水果新增成功:  fId={}", fruit.getFId());
                return ResponseUtil.success("新增成功", fruit);
            } else {
                return ResponseUtil.error("新增失败");
            }
        } catch (BusinessException e) {
            log.error("新增水果失败:  {}", e.getMessage());
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("新增水果异常", e);
            return ResponseUtil. error("新增水果异常");
        }
    }

    /**
     * 修改水果
     */
    @PutMapping("/update")
    public ResponseUtil<?> updateFruit(@RequestBody Fruit fruit) {
        try {
            log.info("修改水果请求: fId={}", fruit.getFId());

            boolean result = fruitService. updateFruit(fruit);

            if (result) {
                log.info("水果修改成功: fId={}", fruit.getFId());
                return ResponseUtil.success("修改成功");
            } else {
                return ResponseUtil.error("修改失败");
            }
        } catch (BusinessException e) {
            log.error("修改水果失败: {}", e. getMessage());
            return ResponseUtil. error(400, e.getMessage());
        } catch (Exception e) {
            log.error("修改水果异常", e);
            return ResponseUtil.error("修改水果异常");
        }
    }

    /**
     * 删除水果
     */
    @DeleteMapping("/delete/{fId}")
    public ResponseUtil<?> deleteFruit(@PathVariable String fId) {
        try {
            log.info("删除水果请求: fId={}", fId);

            boolean result = fruitService.deleteFruit(fId);

            if (result) {
                log.info("水果删除成功: fId={}", fId);
                return ResponseUtil.success("删除成功");
            } else {
                return ResponseUtil.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除水果异常", e);
            return ResponseUtil. error("删除水果异常");
        }
    }

    /**
     * 查询单个水果
     */
    @GetMapping("/{fId}")
    public ResponseUtil<?> getFruit(@PathVariable String fId) {
        try {
            log.info("查询水果请求: fId={}", fId);

            Fruit fruit = fruitService.getFruitById(fId);

            if (fruit != null) {
                return ResponseUtil.success("查询成功", fruit);
            } else {
                return ResponseUtil.error(404, "水果不存在");
            }
        } catch (Exception e) {
            log.error("查询水果异常", e);
            return ResponseUtil.error("查询水果异常");
        }
    }

    /**
     * 查询所有水果
     */
    @GetMapping("/list/all")
    public ResponseUtil<? > getAllFruits() {
        try {
            log.info("查询所有水果");

            List<Fruit> fruits = fruitService.getAllFruits();

            return ResponseUtil.success("查询成功", fruits);
        } catch (Exception e) {
            log.error("查询水果列表异常", e);
            return ResponseUtil.error("查询水果列表异常");
        }
    }

    /**
     * 根据供应商查询水果
     */
    @GetMapping("/list/supplier/{sId}")
    public ResponseUtil<? > getFruitsBySupplier(@PathVariable Integer sId) {
        try {
            log.info("根据供应商查询水果: sId={}", sId);

            List<Fruit> fruits = fruitService.getFruitsBySupplier(sId);

            return ResponseUtil.success("查询成功", fruits);
        } catch (Exception e) {
            log.error("根据供应商查询水果异常", e);
            return ResponseUtil.error("根据供应商查询水果异常");
        }
    }

    /**
     * 搜索水果
     */
    @GetMapping("/search/{name}")
    public ResponseUtil<?> searchFruit(@PathVariable String name) {
        try {
            log.info("搜索水果:  name={}", name);

            List<Fruit> fruits = fruitService.searchFruitByName(name);

            return ResponseUtil.success("查询成功", fruits);
        } catch (Exception e) {
            log.error("搜索水果异常", e);
            return ResponseUtil.error("搜索水果异常");
        }
    }

    /**
     * 查询库存不足的水果
     */
    @GetMapping("/list/lowStock/{minQty}")
    public ResponseUtil<? > getLowStockFruits(@PathVariable Integer minQty) {
        try {
            log.info("查询库存不足的水果: minQty={}", minQty);

            List<Fruit> fruits = fruitService.getLowStockFruits(minQty);

            return ResponseUtil.success("查询成功", fruits);
        } catch (Exception e) {
            log.error("查询库存不足水果异常", e);
            return ResponseUtil.error("查询库存不足水果异常");
        }
    }

    /**
     * 更新库存
     */
    @PostMapping("/updateQuantity")
    public ResponseUtil<? > updateQuantity(@RequestParam String fId,
                                           @RequestParam Integer qty) {
        try {
            log.info("更新库存请求: fId={}, qty={}", fId, qty);

            boolean result = fruitService.updateQuantity(fId, qty);

            if (result) {
                log. info("库存更新成功: fId={}", fId);
                return ResponseUtil.success("库存更新成功");
            } else {
                return ResponseUtil.error("库存更新失败");
            }
        } catch (BusinessException e) {
            log.error("库存更新失败: {}", e.getMessage());
            return ResponseUtil.error(400, e. getMessage());
        } catch (Exception e) {
            log.error("库存更新异常", e);
            return ResponseUtil. error("库存更新异常");
        }
    }
}