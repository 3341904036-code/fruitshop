package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fruitshop.dao.mapper.FruitMapper;
import com. fruitshop.dao.mapper.OrderMapper;
import com.fruitshop.dto.OrderDTO;
import com. fruitshop.dto.OrderItemDTO;
import com.fruitshop.entity. Fruit;
import com.fruitshop.entity.Order;
import com.fruitshop. entity.OrderItem;
import com.fruitshop.exception. BusinessException;
import com.fruitshop.service.OrderService;
import com.fruitshop.util.VipDiscountUtil;
import com.fruitshop.vo.OrderItemVO;
import com.fruitshop.vo.OrderVO;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math. BigDecimal;
import java. time.LocalDateTime;
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
    private FruitMapper fruitMapper;

    /**
     * 创建订单
     * @param orderDTO 订单数据传输对象
     * @return 生成的订单号
     */
    @Override
    public Integer createOrder(OrderDTO orderDTO) {
        log.info("创建订单:  cId={}, items数量={}", orderDTO.getCId(),
                orderDTO.getItems() != null ? orderDTO.getItems().size() : 0);

        // 验证基本信息
        validateOrderDTO(orderDTO);

        // 创建订单对象
        Order order = Order.builder()
                .oDate(LocalDateTime.now())
                .cId(orderDTO.getCId())
                .originalPrice(BigDecimal.ZERO)
                .discount(BigDecimal.ONE)
                .pay(BigDecimal.ZERO)
                .build();

        // 插入订单到数据库
        int insertResult = orderMapper.insert(order);
        if (insertResult <= 0) {
            throw new BusinessException("订单创建失败");
        }

        Integer oNum = order.getONum();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 处理订单项目
        if (orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            totalPrice = processOrderItems(oNum, orderDTO.getItems());
        }

        // 更新订单总额
        order.setOriginalPrice(totalPrice);
        order.setDiscount(BigDecimal.ONE);
        order.setPay(totalPrice);

        int updateResult = orderMapper. updateById(order);
        if (updateResult <= 0) {
            throw new BusinessException("订单金额更新失败");
        }

        log.info("订单创建成功: oNum={}, totalPrice={}", oNum, totalPrice);
        return oNum;
    }

    /**
     * 验证订单DTO
     */
    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getCId() == null || orderDTO. getCId() <= 0) {
            throw new BusinessException("顾客ID不合法");
        }

        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new BusinessException("订单至少需要一个商品");
        }
    }

    /**
     * 处理订单项目
     */
    private BigDecimal processOrderItems(Integer oNum, List<OrderItemDTO> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 遍历每个订单项
        for (int i = 0; i < items.size(); i++) {
            OrderItemDTO itemDTO = items.get(i);

            // 验证订单项数据
            validateOrderItem(itemDTO, i);

            // 获取水果信息
            Fruit fruit = fruitMapper.selectById(itemDTO.getFId());
            if (fruit == null) {
                throw new BusinessException("第" + (i + 1) + "项:  水果不存在, ID=" + itemDTO.getFId());
            }

            // 检查库存
            if (fruit.getQuantity() < itemDTO.getQuantity()) {
                throw new BusinessException("第" + (i + 1) + "项: 水果库存不足, 库存=" + fruit.getQuantity()
                        + ", 购买数量=" + itemDTO. getQuantity());
            }

            // 更新库存（减少）
            fruitMapper.updateQuantity(itemDTO.getFId(), itemDTO.getQuantity());

            // 计算小计金额
            BigDecimal subtotal = fruit.getFPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalPrice = totalPrice. add(subtotal);

            log.debug("订单项处理: oNum={}, oItem={}, fId={}, quantity={}, price={}",
                    oNum, (i + 1), itemDTO.getFId(), itemDTO.getQuantity(), fruit.getFPrice());
        }

        return totalPrice;
    }

    /**
     * 验证订单项
     */
    private void validateOrderItem(OrderItemDTO itemDTO, int index) {
        if (itemDTO.getFId() == null || itemDTO. getFId().trim().isEmpty()) {
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
                .cId(orderDTO. getCId() != null ? orderDTO.getCId() : existingOrder.getCId())
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

        int result = orderMapper.deleteById(oNum);
        log.info("订单删除成功: oNum={}", oNum);
        return result > 0;
    }

    /**
     * 根据订单号查询订单
     */
    @Override
    public OrderVO getOrderById(Integer oNum) {
        if (oNum == null || oNum <= 0) {
            throw new BusinessException("订单号不合法");
        }

        Order order = orderMapper. selectById(oNum);
        if (order == null) {
            return null;
        }
        return convertToVO(order);
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
                .map(this:: convertToVO)
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
                .discount(order. getDiscount())
                .pay(order.getPay())
                .build();
    }
}