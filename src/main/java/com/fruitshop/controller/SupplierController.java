package com.fruitshop.controller;

import com.fruitshop.dao.mapper.SupplierMapper;
import com.fruitshop. entity.Supplier;
import com. fruitshop.util.ResponseUtil;
import lombok. extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 供应商控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierController {

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 新增供应商
     */
    @PostMapping("/add")
    public ResponseUtil<?> addSupplier(@RequestBody Supplier supplier) {
        try {
            log.info("新增供应商请求: sName={}", supplier.getSName());

            int result = supplierMapper.insert(supplier);

            if (result > 0) {
                log.info("供应商新增成功: sId={}", supplier.getSId());
                return ResponseUtil.success("新增成功", supplier);
            } else {
                return ResponseUtil. error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增供应商异常", e);
            return ResponseUtil.error("新增供应商异常");
        }
    }

    /**
     * 修改供应商
     */
    @PutMapping("/update")
    public ResponseUtil<?> updateSupplier(@RequestBody Supplier supplier) {
        try {
            log.info("修改供应商请求: sId={}", supplier.getSId());

            int result = supplierMapper.updateById(supplier);

            if (result > 0) {
                log.info("供应商修改成功: sId={}", supplier.getSId());
                return ResponseUtil.success("修改成功");
            } else {
                return ResponseUtil.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改供应商异常", e);
            return ResponseUtil.error("修改供应商异常");
        }
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/delete/{sId}")
    public ResponseUtil<?> deleteSupplier(@PathVariable Integer sId) {
        try {
            log.info("删除供应商请求: sId={}", sId);

            int result = supplierMapper. deleteById(sId);

            if (result > 0) {
                log.info("供应商删除成功:  sId={}", sId);
                return ResponseUtil.success("删除成功");
            } else {
                return ResponseUtil.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除供应商异常", e);
            return ResponseUtil. error("删除供应商异常");
        }
    }

    /**
     * 查询单个供应商
     */
    @GetMapping("/{sId}")
    public ResponseUtil<?> getSupplier(@PathVariable Integer sId) {
        try {
            log.info("查询供应商请求: sId={}", sId);

            Supplier supplier = supplierMapper.selectById(sId);

            if (supplier != null) {
                return ResponseUtil.success("查询成功", supplier);
            } else {
                return ResponseUtil.error(404, "供应商不存在");
            }
        } catch (Exception e) {
            log.error("查询供应商异常", e);
            return ResponseUtil. error("查询供应商异常");
        }
    }

    /**
     * 查询所有供应商
     */
    @GetMapping("/list/all")
    public ResponseUtil<?> getAllSuppliers() {
        try {
            log. info("查询所有供应商");

            List<Supplier> suppliers = supplierMapper.selectAllSuppliers();

            return ResponseUtil.success("查询成功", suppliers);
        } catch (Exception e) {
            log.error("查询供应商列表异常", e);
            return ResponseUtil.error("查询供应商列表异常");
        }
    }

    /**
     * 根据城市查询供应商
     */
    @GetMapping("/list/city/{city}")
    public ResponseUtil<?> getSuppliersByCity(@PathVariable String city) {
        try {
            log.info("根据城市查询供应商: city={}", city);

            List<Supplier> suppliers = supplierMapper.selectByCity(city);

            return ResponseUtil.success("查询成功", suppliers);
        } catch (Exception e) {
            log.error("根据城市查询供应商异常", e);
            return ResponseUtil.error("根据城市查询供应商异常");
        }
    }
}