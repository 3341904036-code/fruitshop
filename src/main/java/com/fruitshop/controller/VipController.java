package com.fruitshop. controller;

import com.fruitshop.dto.VipDiscountDTO;
import com.fruitshop.service.VipService;
import com.fruitshop.util. ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework. beans.factory.annotation.Autowired;
import org.springframework. web.bind.annotation.*;
import java.util.List;

/**
 * VIP管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/vip")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VipController {

    @Autowired
    private VipService vipService;

    /**
     * 更新所有顾客的VIP等级
     */
    @PostMapping("/updateAll")
    public ResponseUtil<?> updateAllVipLevels() {
        try {
            log.info("更新所有顾客VIP等级");

            vipService.updateVipLevel();

            log.info("VIP等级更新完成");
            return ResponseUtil. success("VIP等级已更新");
        } catch (Exception e) {
            log.error("更新VIP等级异常", e);
            return ResponseUtil.error("更新VIP等级异常");
        }
    }

    /**
     * 获取单个顾客的VIP信息
     */
    @GetMapping("/info/{cId}")
    public ResponseUtil<?> getVipInfo(@PathVariable Integer cId) {
        try {
            log.info("获取VIP信息: cId={}", cId);

            VipDiscountDTO vipInfo = vipService.getVipInfo(cId);

            if (vipInfo != null) {
                return ResponseUtil. success("获取成功", vipInfo);
            } else {
                return ResponseUtil.error(404, "顾客不存在");
            }
        } catch (Exception e) {
            log.error("获取VIP信息异常", e);
            return ResponseUtil.error("获取VIP信息异常");
        }
    }

    /**
     * 获取所有VIP顾客信息
     */
    @GetMapping("/list")
    public ResponseUtil<?> getAllVipCustomers() {
        try {
            log.info("获取所有VIP顾客");

            List<VipDiscountDTO> vipList = vipService.getAllVipCustomers();

            return ResponseUtil.success("获取成功", vipList);
        } catch (Exception e) {
            log.error("获取VIP列表异常", e);
            return ResponseUtil.error("获取VIP列表异常");
        }
    }

    /**
     * 手动设置顾客VIP等级
     */
    @PostMapping("/setLevel/{cId}/{level}")
    public ResponseUtil<?> setVipLevel(@PathVariable Integer cId, @PathVariable Integer level) {
        try {
            log.info("设置VIP等级: cId={}, level={}", cId, level);

            if (level < 0 || level > 3) {
                return ResponseUtil. error(400, "VIP等级必须在0-3之间");
            }

            boolean result = vipService.setVipLevel(cId, level);

            if (result) {
                log.info("VIP等级设置成功:  cId={}, level={}", cId, level);
                return ResponseUtil.success("VIP等级设置成功");
            } else {
                return ResponseUtil. error("VIP等级设置失败");
            }
        } catch (Exception e) {
            log.error("设置VIP等级异常", e);
            return ResponseUtil.error("设置VIP等级异常");
        }
    }
}