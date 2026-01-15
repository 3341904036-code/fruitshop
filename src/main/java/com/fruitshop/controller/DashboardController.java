package com.fruitshop.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.fruitshop.entity.Customer;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.Order;
import com.fruitshop.service.CustomerService;
import com.fruitshop.service.FruitService;
import com.fruitshop.service.OrderService;
import com.fruitshop.vo.CustomerVO;
import com.fruitshop.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 仪表盘控制器（数据导出核心逻辑）
 * 适配 EasyExcel 3.x 版本，仅保留 Excel/CSV 导出，移除供应商、PDF 相关逻辑
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    // 仅保留核心服务注入
    private final CustomerService customerService;
    private final FruitService fruitService;
    private final OrderService orderService;

    /**
     * 数据导出接口（仅支持 Excel/CSV）
     * @param types 导出类型（逗号分隔：customers,fruits,orders,statistics）
     * @param format 导出格式（excel/csv）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @param includeHeaders 是否包含表头（true/false）
     * @param response 响应对象（用于输出文件）
     */
    @GetMapping("/export")
    public void exportData(
            @RequestParam String types,
            @RequestParam String format,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "true") boolean includeHeaders,
            HttpServletResponse response
    ) {
        // 1. 解析导出类型
        List<String> exportTypeList = Arrays.asList(types.split(","));
        // 2. 统一响应头配置（文件下载）
        setDownloadResponseHeader(response, format);

        try (OutputStream outputStream = response.getOutputStream()) {
            // 3. 按格式处理导出逻辑（仅保留 Excel/CSV）
            switch (format.toLowerCase()) {
                case "excel":
                    exportToExcel(exportTypeList, startDate, endDate, includeHeaders, outputStream);
                    break;
                case "csv":
                    exportToCsv(exportTypeList, startDate, endDate, includeHeaders, outputStream);
                    break;
                default:
                    throw new IllegalArgumentException("仅支持 excel/csv 格式导出，不支持：" + format);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error("数据导出失败", e);
            // 即使失败也返回基础响应，保证前端能感知
            response.setContentType("text/plain;charset=UTF-8");
            try {
                response.getWriter().write("部分数据导出失败，已导出可用数据：" + e.getMessage());
            } catch (IOException ex) {
                log.error("响应写入失败", ex);
            }
        }
    }

    /**
     * 设置下载响应头（仅处理 Excel/CSV）
     */
    private void setDownloadResponseHeader(HttpServletResponse response, String format) {
        // 文件名
        String fileName = "水果商城报表_" + System.currentTimeMillis();
        // 格式后缀（仅 Excel/CSV）
        String suffix = "xlsx";
        if ("csv".equals(format)) suffix = "csv";

        // 响应头核心配置
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        try {
            // 解决中文文件名乱码
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName + "." + suffix);
        } catch (Exception e) {
            log.error("设置响应头失败", e);
        }
    }

    /**
     * Excel导出（适配 EasyExcel 3.x 版本，支持多sheet，单个sheet失败不影响其他）
     */
    private void exportToExcel(List<String> types, LocalDate startDate, LocalDate endDate, boolean includeHeaders, OutputStream outputStream) {
        // 1. 创建 ExcelWriter（EasyExcel 3.x 正确写法）
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream)
                .excelType(ExcelTypeEnum.XLSX)
                .build()) {

            int sheetNo = 0;

            // 2. 导出顾客数据
            if (types.contains("customers")) {
                try {
                    List<CustomerVO> customerList = customerService.getAllCustomers();
                    // 创建 WriteSheet，配置是否包含表头
                    WriteSheet customerSheet = EasyExcel.writerSheet(sheetNo++, "顾客数据")
                            .head(includeHeaders ? Customer.class : null) // 不包含表头则传 null
                            .build();
                    excelWriter.write(customerList, customerSheet);
                } catch (Exception e) {
                    log.error("顾客数据导出失败", e);
                }
            }

            // 3. 导出水果数据
            if (types.contains("fruits")) {
                try {
                    List<Fruit> fruitList = fruitService.getAllFruits();
                    WriteSheet fruitSheet = EasyExcel.writerSheet(sheetNo++, "水果数据")
                            .head(includeHeaders ? Fruit.class : null)
                            .build();
                    excelWriter.write(fruitList, fruitSheet);
                } catch (Exception e) {
                    log.error("水果数据导出失败", e);
                }
            }

            // 4. 导出订单数据（支持日期筛选）
            if (types.contains("orders")) {
                try {
                    List<OrderVO> orderList = orderService.getAllOrders();
                    // 日期筛选：仅当传入起止日期时过滤
                    if (startDate != null && endDate != null) {
                        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
                        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
                        orderList = orderList.stream()
                                .filter(order -> order.getODate().isAfter(start) && order.getODate().isBefore(end))
                                .toList();
                    }
                    WriteSheet orderSheet = EasyExcel.writerSheet(sheetNo++, "订单数据")
                            .head(includeHeaders ? Order.class : null)
                            .build();
                    excelWriter.write(orderList, orderSheet);
                } catch (Exception e) {
                    log.error("订单数据导出失败", e);
                }
            }

            // 5. 导出统计报表（顾客/水果/订单总数）
            if (types.contains("statistics")) {
                try {
                    List<Map<String, Object>> statsList = new ArrayList<>();
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("顾客总数", customerService.count());
                    stats.put("水果总数", fruitService.count());
                    stats.put("订单总数", orderService.count());
                    stats.put("导出时间", LocalDateTime.now());
                    statsList.add(stats);

                    WriteSheet statsSheet = EasyExcel.writerSheet(sheetNo++, "统计报表")
                            .head(includeHeaders ? buildStatsHead() : null) // 自定义表头
                            .build();
                    excelWriter.write(statsList, statsSheet);
                } catch (Exception e) {
                    log.error("统计报表导出失败", e);
                }
            }
        } catch (Exception e) {
            log.error("Excel 导出核心逻辑异常", e);
        }
    }

    /**
     * CSV导出（简化版：所有数据合并到一个CSV，按类型分隔）
     */
    private void exportToCsv(List<String> types, LocalDate startDate, LocalDate endDate, boolean includeHeaders, OutputStream outputStream) throws IOException {
        StringBuilder csvContent = new StringBuilder();

        // 1. 顾客数据
        if (types.contains("customers")) {
            try {
                List<CustomerVO> list = customerService.getAllCustomers();
                if (includeHeaders) csvContent.append("顾客ID,姓名,地址,城市,邮编,联系人,邮箱,VIP等级,折扣\n");
                for (CustomerVO c : list) {
                    csvContent.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            c.getCId(), c.getCName(), c.getCAddress(), c.getCCity(),
                            c.getCZip(), c.getCContact(), c.getCEmail(), c.getCVip(), c.getDiscount()));
                }
                csvContent.append("\n"); // 分隔不同类型数据
            } catch (Exception e) {
                log.error("顾客CSV导出失败", e);
            }
        }

        // 2. 水果数据
        if (types.contains("fruits")) {
            try {
                List<Fruit> list = fruitService.getAllFruits();
                if (includeHeaders) csvContent.append("水果ID,供应商ID,名称,价格,库存\n");
                for (Fruit f : list) {
                    csvContent.append(String.format("%s,%s,%s,%s,%s\n",
                            f.getFId(), f.getSId(), f.getFName(), f.getFPrice(), f.getQuantity()));
                }
                csvContent.append("\n");
            } catch (Exception e) {
                log.error("水果CSV导出失败", e);
            }
        }

        // 3. 订单数据（支持日期筛选）
        if (types.contains("orders")) {
            try {
                List<OrderVO> list = orderService.getAllOrders();
                // 日期筛选
                if (startDate != null && endDate != null) {
                    LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
                    LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
                    list = list.stream()
                            .filter(order -> order.getODate().isAfter(start) && order.getODate().isBefore(end))
                            .toList();
                }
                if (includeHeaders) csvContent.append("订单号,订单日期,顾客ID,原价,折扣,实付金额\n");
                for (OrderVO o : list) {
                    csvContent.append(String.format("%s,%s,%s,%s,%s,%s\n",
                            o.getONum(), o.getODate(), o.getCId(), o.getOriginalPrice(),
                            o.getDiscount(), o.getPay()));
                }
                csvContent.append("\n");
            } catch (Exception e) {
                log.error("订单CSV导出失败", e);
            }
        }

        // 4. 统计报表
        if (types.contains("statistics")) {
            try {
                if (includeHeaders) csvContent.append("顾客总数,水果总数,订单总数,导出时间\n");
                csvContent.append(String.format("%s,%s,%s,%s\n",
                        customerService.count(), fruitService.count(), orderService.count(), LocalDateTime.now()));
            } catch (Exception e) {
                log.error("统计CSV导出失败", e);
            }
        }

        // 写入CSV（添加BOM头解决Excel打开乱码问题）
        outputStream.write("\uFEFF".getBytes(StandardCharsets.UTF_8)); // UTF-8 BOM
        outputStream.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 构建统计报表的Excel表头
     */
    private List<List<String>> buildStatsHead() {
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("顾客总数"));
        head.add(Collections.singletonList("水果总数"));
        head.add(Collections.singletonList("订单总数"));
        head.add(Collections.singletonList("导出时间"));
        return head;
    }
}