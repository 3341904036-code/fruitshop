package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fruitshop.dao.mapper.FruitMapper;
import com.fruitshop.dao.mapper.OrderItemMapper;
import com.fruitshop.dao.mapper.OrderMapper;
import com.fruitshop.dto.OrderDTO;
import com.fruitshop.dto.OrderItemDTO;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.service.OrderService;
import com.fruitshop.util.VipDiscountUtil;
import com.fruitshop.vo.OrderItemVO;
import com.fruitshop.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 处理订单的创建、修改、删除和查询业务逻辑
 */
@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private FruitMapper fruitMapper;

    /**
     * 创建订单（含订单项插入、库存扣减、折扣计算）
     * @param orderDTO 订单数据传输对象
     * @return 生成的订单号
     */
    @Override
    public Integer createOrder(OrderDTO orderDTO) {
        log.info("创建订单:  cId={}, items数量={}, 折扣={}",
                orderDTO.getCId(),
                orderDTO.getItems() != null ? orderDTO.getItems().size() : 0,
                orderDTO.getDiscount());

        // 验证基本信息
        validateOrderDTO(orderDTO);

        // 使用前端传入的折扣值，默认1.0（无折扣）
        BigDecimal discount = orderDTO.getDiscount() != null ? orderDTO.getDiscount() : BigDecimal.ONE;
        // 校验折扣范围（0-1之间，防止非法值）
        if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.ONE) > 0) {
            throw new BusinessException("折扣值需在0-1之间（0=0折，1=无折扣）");
        }

        // 创建订单对象（使用传入的折扣）
        Order order = Order.builder()
                .oDate(LocalDateTime.now())
                .cId(orderDTO.getCId())
                .originalPrice(BigDecimal.ZERO)
                .discount(discount)
                .pay(BigDecimal.ZERO)
                .build();

        // 插入订单到数据库
        int insertResult = orderMapper.insert(order);
        if (insertResult <= 0) {
            throw new BusinessException("订单创建失败");
        }

        Integer oNum = order.getONum();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 处理订单项目（扣减库存 + 插入订单项）
        if (orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            totalPrice = processOrderItems(oNum, orderDTO.getItems());
        }

        // 按折扣计算实付金额
        order.setOriginalPrice(totalPrice);
        BigDecimal payPrice = totalPrice.multiply(discount);
        order.setPay(payPrice);

        int updateResult = orderMapper.updateById(order);
        if (updateResult <= 0) {
            throw new BusinessException("订单金额更新失败");
        }

        log.info("订单创建成功: oNum={}, totalPrice={}, discount={}, pay={}",
                oNum, totalPrice, discount, payPrice);
        return oNum;
    }

    /**
     * 验证订单DTO基础参数
     */
    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getCId() == null || orderDTO.getCId() <= 0) {
            throw new BusinessException("顾客ID不合法");
        }

        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new BusinessException("订单至少需要一个商品");
        }
    }

    /**
     * 处理订单项目（扣减库存 + 插入订单项到orderitems表）
     */
    private BigDecimal processOrderItems(Integer oNum, List<OrderItemDTO> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 遍历每个订单项，oItem从1开始递增
        for (int i = 0; i < items.size(); i++) {
            OrderItemDTO itemDTO = items.get(i);
            int oItem = i + 1; // 订单项序号

            // 验证订单项数据
            validateOrderItem(itemDTO, i);

            // 获取水果信息
            Fruit fruit = fruitMapper.selectById(itemDTO.getFId());
            if (fruit == null) {
                throw new BusinessException("第" + (i + 1) + "项: 水果不存在, ID=" + itemDTO.getFId());
            }

            // 检查库存是否充足
            if (fruit.getQuantity() < itemDTO.getQuantity()) {
                throw new BusinessException("第" + (i + 1) + "项: 水果库存不足, 库存=" + fruit.getQuantity()
                        + ", 购买数量=" + itemDTO.getQuantity());
            }

            // 扣减水果库存
            int updateCount = fruitMapper.updateQuantity(itemDTO.getFId(), itemDTO.getQuantity());
            if (updateCount <= 0) {
                throw new BusinessException("第" + (i + 1) + "项: 水果库存更新失败, ID=" + itemDTO.getFId());
            }
            log.info("第{}项: 水果ID={} 库存扣减成功，原库存={} - 下单数={} = 剩余{}",
                    (i + 1), itemDTO.getFId(), fruit.getQuantity(), itemDTO.getQuantity(),
                    fruit.getQuantity() - itemDTO.getQuantity());

            // 插入订单项到orderitems表
            OrderItem orderItem = OrderItem.builder()
                    .oNum(oNum)
                    .oItem(oItem)
                    .fId(itemDTO.getFId())
                    .quantity(itemDTO.getQuantity())
                    .itemPrice(fruit.getFPrice())
                    .build();
            int insertItemResult = orderItemMapper.insert(orderItem);
            if (insertItemResult <= 0) {
                throw new BusinessException("第" + (i + 1) + "项: 订单项插入失败, ID=" + itemDTO.getFId());
            }
            log.debug("第{}项: 订单项插入成功，oNum={}, oItem={}, fId={}",
                    (i + 1), oNum, oItem, itemDTO.getFId());

            // 计算小计金额并累加总价
            BigDecimal subtotal = fruit.getFPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalPrice = totalPrice.add(subtotal);

            log.debug("订单项处理: oNum={}, oItem={}, fId={}, quantity={}, price={}",
                    oNum, oItem, itemDTO.getFId(), itemDTO.getQuantity(), fruit.getFPrice());
        }

        return totalPrice;
    }

    /**
     * 验证订单项参数
     */
    private void validateOrderItem(OrderItemDTO itemDTO, int index) {
        if (itemDTO.getFId() == null || itemDTO.getFId().trim().isEmpty()) {
            throw new BusinessException("第" + (index + 1) + "项: 水果ID不能为空");
        }

        if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
            throw new BusinessException("第" + (index + 1) + "项: 购买数量必须大于0");
        }
    }

    /**
     * 修改订单
     */
    @Override
    public boolean updateOrder(OrderDTO orderDTO) {
        log.info("修改订单: oNum={}", orderDTO.getONum());

        if (orderDTO.getONum() == null || orderDTO.getONum() <= 0) {
            throw new BusinessException("订单号不合法");
        }

        Order existingOrder = orderMapper.selectById(orderDTO.getONum());
        if (existingOrder == null) {
            throw new BusinessException("订单不存在");
        }

        // 使用原值作为默认值，仅更新传入的值
        Order order = Order.builder()
                .oNum(orderDTO.getONum())
                .oDate(orderDTO.getODate() != null ? orderDTO.getODate() : existingOrder.getODate())
                .cId(orderDTO.getCId() != null ? orderDTO.getCId() : existingOrder.getCId())
                .originalPrice(orderDTO.getOriginalPrice() != null ? orderDTO.getOriginalPrice() : existingOrder.getOriginalPrice())
                .discount(orderDTO.getDiscount() != null ? orderDTO.getDiscount() : existingOrder.getDiscount())
                .pay(orderDTO.getPay() != null ? orderDTO.getPay() : existingOrder.getPay())
                .build();

        int result = orderMapper.updateById(order);
        log.info("订单修改成功: oNum={}", orderDTO.getONum());
        return result > 0;
    }

    /**
     * 删除订单
     */
    @Override
    public boolean deleteOrder(Integer oNum) {
        log.info("删除订单: oNum={}", oNum);

        if (oNum == null || oNum <= 0) {
            throw new BusinessException("订单号不合法");
        }

        Order order = orderMapper.selectById(oNum);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 先删除订单项，再删除订单（保证外键关联完整性）
        orderItemMapper.deleteByOrderNum(oNum);
        int result = orderMapper.deleteById(oNum);

        log.info("订单删除成功: oNum={}", oNum);
        return result > 0;
    }

    /**
     * 根据订单号查询订单（仅主信息）
     */
    @Override
    public OrderVO getOrderById(Integer oNum) {
        if (oNum == null || oNum <= 0) {
            throw new BusinessException("订单号不合法");
        }

        Order order = orderMapper.selectById(oNum);
        if (order == null) {
            return null;
        }
        return convertToVO(order);
    }

    /**
     * 查询订单详情（含订单项、水果名称、小计金额）
     */
    @Override
    public OrderVO getOrderDetail(Integer oNum) {
        if (oNum == null || oNum <= 0) {
            throw new BusinessException("订单号不合法");
        }

        // 1. 查询订单主信息
        Order order = orderMapper.selectById(oNum);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 查询该订单的所有订单项
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNum(oNum);
        if (CollectionUtils.isEmpty(orderItems)) {
            throw new BusinessException("订单无商品项");
        }

        // 3. 转换订单项为VO（补充水果名称、小计）
        List<OrderItemVO> itemVOList = orderItems.stream().map(item -> {
            // 查询水果名称
            Fruit fruit = fruitMapper.selectById(item.getFId());
            String fName = fruit != null ? fruit.getFName() : "未知水果";

            // 计算小计金额
            BigDecimal subtotal = item.getItemPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            return OrderItemVO.builder()
                    .oNum(item.getONum())
                    .oItem(item.getOItem())
                    .fId(item.getFId())
                    .fName(fName)
                    .quantity(item.getQuantity())
                    .itemPrice(item.getItemPrice())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());

        // 4. 构建带订单项的订单VO
        OrderVO orderVO = convertToVO(order);
        orderVO.setOrderItems(itemVOList);

        return orderVO;
    }

    /**
     * 查询所有订单
     */
    @Override
    public List<OrderVO> getAllOrders() {
        List<Order> orders = orderMapper.selectAllOrders();
        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据顾客ID查询订单
     */
    @Override
    public List<OrderVO> getOrdersByCustomer(Integer cId) {
        if (cId == null || cId <= 0) {
            throw new BusinessException("顾客ID不合法");
        }

        List<Order> orders = orderMapper.selectByCustomerId(cId);
        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 计算订单总额（根据原价和折扣）
     */
    @Override
    public void calculateOrderTotal(Integer oNum) {
        if (oNum == null || oNum <= 0) {
            throw new BusinessException("订单号不合法");
        }

        Order order = orderMapper.selectById(oNum);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 应付款 = 原价 × 折扣
        BigDecimal total = order.getOriginalPrice().multiply(order.getDiscount());
        order.setPay(total);

        orderMapper.updateById(order);
        log.info("订单总额计算完成: oNum={}, originalPrice={}, discount={}, pay={}",
                oNum, order.getOriginalPrice(), order.getDiscount(), total);
    }

    @Override
    public int count() {
        return orderMapper.selectCount(null) == null ? 0 : orderMapper.selectCount(null).intValue();
    }

    /**
     * 将Order实体转换为OrderVO视图对象
     */
    private OrderVO convertToVO(Order order) {
        return OrderVO.builder()
                .oNum(order.getONum())
                .oDate(order.getODate())
                .cId(order.getCId())
                .originalPrice(order.getOriginalPrice())
                .discount(order.getDiscount())
                .pay(order.getPay())
                .build();
    }
}