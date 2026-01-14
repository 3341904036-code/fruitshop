/*
 Navicat Premium Dump SQL

 Source Server         : sql
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : fruitshop

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 12/01/2026 22:29:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customers
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers`  (
  `c_id` int NOT NULL AUTO_INCREMENT,
  `c_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `c_address` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `c_city` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `c_zip` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `c_contact` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `c_email` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `c_vip` int NULL DEFAULT NULL,
  PRIMARY KEY (`c_id`) USING BTREE,
  UNIQUE INDEX `c_id`(`c_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10007 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customers
-- ----------------------------
INSERT INTO `customers` VALUES (10000, 'wuzhubao', 'jimeidaxue', 'Sanming', '365000', 'lingxiuya', '3341904013@qq.com', 1);
INSERT INTO `customers` VALUES (10001, 'RedHook', '200 Street', 'Tianjin', '300000', 'LiMing', 'LMing@163.com', 1);
INSERT INTO `customers` VALUES (10002, 'Stars', '333 Fromage Lane', 'Dalian', '116000', 'Zhangbo', 'Jerry@hotmail.com', 0);
INSERT INTO `customers` VALUES (10003, 'Netbhood', '1 Sunny Place', 'Qingdao', '266000', 'LuoCong', NULL, 2);
INSERT INTO `customers` VALUES (10004, 'JOTO', '829 Riverside Drive', 'Haikou', '570000', 'YangShan', 'sam@hotmail.com', 3);

-- ----------------------------
-- Table structure for fruits
-- ----------------------------
DROP TABLE IF EXISTS `fruits`;
CREATE TABLE `fruits`  (
  `f_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `s_id` int NOT NULL,
  `f_name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `f_price` decimal(8, 2) NOT NULL,
  `quantity` int NULL DEFAULT 1000 COMMENT '库存商品数量',
  PRIMARY KEY (`f_id`) USING BTREE,
  UNIQUE INDEX `f_id`(`f_id` ASC) USING BTREE,
  INDEX `s_id`(`s_id` ASC) USING BTREE,
  CONSTRAINT `fruits_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `suppliers` (`s_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruits
-- ----------------------------
INSERT INTO `fruits` VALUES ('a1', 101, 'apple', 5.20, 1000);
INSERT INTO `fruits` VALUES ('a2', 103, 'apricot', 2.20, 1000);
INSERT INTO `fruits` VALUES ('b1', 101, 'blackberry', 10.20, 1000);
INSERT INTO `fruits` VALUES ('b2', 104, 'berry', 7.60, 1000);
INSERT INTO `fruits` VALUES ('b3', 104, 'lemon', 6.40, 1000);
INSERT INTO `fruits` VALUES ('b5', 107, 'pear', 3.60, 1000);
INSERT INTO `fruits` VALUES ('bs1', 102, 'orange', 11.20, 1000);
INSERT INTO `fruits` VALUES ('bs2', 105, 'melon', 8.20, 1000);
INSERT INTO `fruits` VALUES ('c0', 101, 'plum', 3.20, 1000);
INSERT INTO `fruits` VALUES ('m1', 106, 'mango', 15.60, 1000);
INSERT INTO `fruits` VALUES ('m2', 105, 'watermelon', 2.60, 1000);
INSERT INTO `fruits` VALUES ('m3', 105, 'cherry', 11.60, 1000);
INSERT INTO `fruits` VALUES ('n2', 100, 'cranberry', 8.70, 1000);
INSERT INTO `fruits` VALUES ('o2', 103, 'coconut', 9.20, 1000);
INSERT INTO `fruits` VALUES ('t1', 102, 'banana', 10.30, 1000);
INSERT INTO `fruits` VALUES ('t2', 102, 'grape', 5.30, 1000);
INSERT INTO `fruits` VALUES ('t4', 107, 'peanut', 3.60, 1000);
INSERT INTO `fruits` VALUES ('y1', 100, 'avocate', 6.70, 1000);
INSERT INTO `fruits` VALUES ('y2', 100, 'peach', 4.50, 1000);
INSERT INTO `fruits` VALUES ('z1', 100, 'pineapple', 5.70, 1000);
INSERT INTO `fruits` VALUES ('z2', 100, 'raspberry', 9.70, 1000);

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tablename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `opname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `optime` datetime NOT NULL,
  `ConnUser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation
-- ----------------------------
INSERT INTO `operation` VALUES (1, 'fruits', 'INSERT', '2025-04-18 11:27:21', 'root@localhost');
INSERT INTO `operation` VALUES (2, 'fruits', 'DELETE', '2025-04-18 11:28:10', 'root@localhost');
INSERT INTO `operation` VALUES (3, 'fruits', 'INSERT', '2025-04-18 11:29:43', 'root@localhost');
INSERT INTO `operation` VALUES (4, 'orderitems', 'INSERT', '2025-04-19 20:53:04', 'root@localhost');
INSERT INTO `operation` VALUES (5, 'fruits', 'UPDATE', '2025-04-19 20:53:04', 'root@localhost');
INSERT INTO `operation` VALUES (6, 'orderitems', 'UPDATE', '2025-04-19 20:54:29', 'root@localhost');
INSERT INTO `operation` VALUES (7, 'fruits', 'UPDATE', '2025-04-19 20:54:29', 'root@localhost');
INSERT INTO `operation` VALUES (8, 'orderitems', 'DELETE', '2025-04-19 20:56:16', 'root@localhost');
INSERT INTO `operation` VALUES (9, 'fruits', 'UPDATE', '2025-04-19 20:56:16', 'root@localhost');
INSERT INTO `operation` VALUES (10, 'orderitems', 'INSERT', '2025-04-19 21:10:20', 'root@localhost');
INSERT INTO `operation` VALUES (11, 'fruits', 'UPDATE', '2025-04-19 21:10:20', 'root@localhost');
INSERT INTO `operation` VALUES (12, 'orderitems', 'DELETE', '2025-04-19 21:11:23', 'root@localhost');
INSERT INTO `operation` VALUES (13, 'fruits', 'UPDATE', '2025-04-19 21:11:23', 'root@localhost');
INSERT INTO `operation` VALUES (14, 'fruits', 'INSERT', '2025-04-22 20:01:31', 'root@localhost');
INSERT INTO `operation` VALUES (15, 'fruits', 'DELETE', '2025-04-22 20:03:37', 'root@localhost');
INSERT INTO `operation` VALUES (16, 'fruits', 'INSERT', '2025-04-22 20:05:09', 'root@localhost');
INSERT INTO `operation` VALUES (17, 'fruits', 'DELETE', '2025-04-22 20:05:33', 'root@localhost');
INSERT INTO `operation` VALUES (18, 'orderitems', 'UPDATE', '2025-04-25 09:53:23', 'root@localhost');
INSERT INTO `operation` VALUES (19, 'fruits', 'UPDATE', '2025-04-25 09:53:23', 'root@localhost');
INSERT INTO `operation` VALUES (20, 'orderitems', 'UPDATE', '2025-04-25 09:53:59', 'root@localhost');
INSERT INTO `operation` VALUES (21, 'fruits', 'UPDATE', '2025-04-25 09:53:59', 'root@localhost');
INSERT INTO `operation` VALUES (22, 'fruits', 'INSERT', '2025-04-25 10:33:38', 'root@localhost');
INSERT INTO `operation` VALUES (23, 'orderitems', 'UPDATE', '2025-04-25 10:35:42', 'root@localhost');
INSERT INTO `operation` VALUES (24, 'fruits', 'UPDATE', '2025-04-25 10:35:42', 'root@localhost');
INSERT INTO `operation` VALUES (25, 'orderitems', 'INSERT', '2025-04-25 22:33:42', 'root@localhost');
INSERT INTO `operation` VALUES (26, 'fruits', 'UPDATE', '2025-04-25 22:33:42', 'root@localhost');
INSERT INTO `operation` VALUES (29, 'orderitems', 'INSERT', '2025-04-25 23:48:45', 'root@localhost');
INSERT INTO `operation` VALUES (30, 'fruits', 'UPDATE', '2025-04-25 23:48:45', 'root@localhost');
INSERT INTO `operation` VALUES (31, 'orderitems', 'UPDATE', '2025-04-25 23:50:06', 'root@localhost');
INSERT INTO `operation` VALUES (32, 'fruits', 'UPDATE', '2025-04-25 23:50:06', 'root@localhost');
INSERT INTO `operation` VALUES (33, 'orderitems', 'DELETE', '2025-04-25 23:50:33', 'root@localhost');
INSERT INTO `operation` VALUES (34, 'fruits', 'UPDATE', '2025-04-25 23:50:33', 'root@localhost');
INSERT INTO `operation` VALUES (41, 'fruits', 'UPDATE', '2025-05-05 22:25:53', 'root@localhost');

-- ----------------------------
-- Table structure for orderitems
-- ----------------------------
DROP TABLE IF EXISTS `orderitems`;
CREATE TABLE `orderitems`  (
  `o_num` int NOT NULL,
  `o_item` int NOT NULL,
  `f_id` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `quantity` int NOT NULL,
  `item_price` decimal(8, 2) NOT NULL,
  PRIMARY KEY (`o_num`, `o_item`) USING BTREE,
  INDEX `f_id`(`f_id` ASC) USING BTREE,
  CONSTRAINT `orderitems_ibfk_1` FOREIGN KEY (`f_id`) REFERENCES `fruits` (`f_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orderitems_ibfk_2` FOREIGN KEY (`o_num`) REFERENCES `orders` (`o_num`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderitems
-- ----------------------------
INSERT INTO `orderitems` VALUES (30001, 1, 'a1', 10, 5.20);
INSERT INTO `orderitems` VALUES (30001, 2, 'b2', 3, 7.60);
INSERT INTO `orderitems` VALUES (30001, 3, 'bs1', 5, 11.20);
INSERT INTO `orderitems` VALUES (30001, 4, 'bs2', 15, 9.20);
INSERT INTO `orderitems` VALUES (30002, 1, 'b3', 2, 20.00);
INSERT INTO `orderitems` VALUES (30003, 1, 'c0', 100, 10.00);
INSERT INTO `orderitems` VALUES (30004, 1, 'o2', 50, 2.50);
INSERT INTO `orderitems` VALUES (30005, 1, 'c0', 5, 10.00);
INSERT INTO `orderitems` VALUES (30005, 2, 'b1', 10, 8.99);
INSERT INTO `orderitems` VALUES (30005, 3, 'a2', 10, 2.20);
INSERT INTO `orderitems` VALUES (30005, 4, 'm1', 5, 14.99);
INSERT INTO `orderitems` VALUES (50001, 1, 'b2', 6, 7.60);
INSERT INTO `orderitems` VALUES (50001, 2, 'bs1', 7, 11.20);
INSERT INTO `orderitems` VALUES (50001, 3, 'bs2', 8, 9.20);
INSERT INTO `orderitems` VALUES (50002, 1, 'a2', 10, 2.20);
INSERT INTO `orderitems` VALUES (50002, 2, 'm1', 5, 14.99);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `o_num` int NOT NULL AUTO_INCREMENT,
  `o_date` datetime NOT NULL,
  `c_id` int NOT NULL,
  `original_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价格(订单项总金额)',
  `discount` decimal(10, 2) NOT NULL DEFAULT 1.00 COMMENT '折扣(默认1表示无折扣)',
  `pay` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '应付款(原价×折扣)',
  PRIMARY KEY (`o_num`) USING BTREE,
  UNIQUE INDEX `o_num`(`o_num` ASC) USING BTREE,
  INDEX `c_id`(`c_id` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `customers` (`c_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 50003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (30001, '2018-09-01 00:00:00', 10001, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (30002, '2018-09-12 00:00:00', 10003, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (30003, '2018-09-30 00:00:00', 10004, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (30004, '2018-10-03 00:00:00', 10002, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (30005, '2018-10-08 00:00:00', 10001, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (50001, '2023-03-15 00:00:00', 10000, 0.00, 1.00, 0.00);
INSERT INTO `orders` VALUES (50002, '2023-03-17 00:00:00', 10000, 0.00, 1.00, 0.00);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'Admin', '系统管理员角色，拥有所有权限');
INSERT INTO `roles` VALUES (2, 'Cusm', '客户角色，可以浏览和购买商品');
INSERT INTO `roles` VALUES (3, 'Supp', '供货商角色，可以管理自己的商品信息');
INSERT INTO `roles` VALUES (4, 'Salor', '销售工作人员角色，可以处理订单和客户服务');

-- ----------------------------
-- Table structure for suppliers
-- ----------------------------
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers`  (
  `s_id` int NOT NULL AUTO_INCREMENT,
  `s_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `s_city` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `s_zip` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `s_call` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`s_id`) USING BTREE,
  UNIQUE INDEX `s_id`(`s_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of suppliers
-- ----------------------------
INSERT INTO `suppliers` VALUES (100, 'JMU', 'Sanming', '365000', '15960961335');
INSERT INTO `suppliers` VALUES (101, 'FastFruit Inc.', 'Tianjin', '300000', '48075');
INSERT INTO `suppliers` VALUES (102, 'LT Supplies', 'Chongqing', '400000', '44333');
INSERT INTO `suppliers` VALUES (103, 'ACME', 'Shanghai', '200000', '90046');
INSERT INTO `suppliers` VALUES (104, 'FNK Inc.', 'Zhongshan', '528437', '11111');
INSERT INTO `suppliers` VALUES (105, 'Good Set', 'Taiyuan', '030000', '22222');
INSERT INTO `suppliers` VALUES (106, 'Just Eat Ours', 'Beijing', '010', '45678');
INSERT INTO `suppliers` VALUES (107, 'DK Inc.', 'Zhengzhou', '450000', '33332');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `u_id` int NOT NULL,
  `pwd` blob NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_id` int NULL DEFAULT NULL,
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_id`(`u_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 10000, 0xCA9185886E032C4901F2C58D862F0F81, 'customer', 2, 'Cusm');
INSERT INTO `user` VALUES (2, 10001, 0x8691B700713BF52D50DAEB8D9EA6094C, 'customer', 2, 'Cusm');
INSERT INTO `user` VALUES (3, 10002, 0x3B385BF29F03CF745D942484A9B73283, 'customer', 2, 'Cusm');
INSERT INTO `user` VALUES (4, 10003, 0xE6FC1A23D9F6874ACF195101D49BB4F3, 'customer', 2, 'Cusm');
INSERT INTO `user` VALUES (5, 10004, 0x8B4E66A9434B0EEAE1449823FC4DCEE5, 'customer', 2, 'Cusm');
INSERT INTO `user` VALUES (8, 100, 0xEB4BCCB32CBC21B63B8F6A0DB92514CC, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (9, 101, 0x2632D3EA5752A43E8F7DBAEA75DCCD4C, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (10, 102, 0xDF82307EA176FF03AA50B75C1FADC818, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (11, 103, 0xEF1868B936CFE26CF362A6DBDE243BD2, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (12, 104, 0x9942BF2CFC785D0783D6CA4C24A091EC, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (13, 105, 0xFBADAED37E8CC438B3CA59ACF54129A2, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (14, 106, 0x84942544D16F33F237DE2B8EE3D2C29C, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (15, 107, 0xB8D40128F304C645E51E914B10695905, 'supplier', 3, 'supp');
INSERT INTO `user` VALUES (16, 10086, 0xEFBBBF313233343536, 'Salor', 4, 'Salor');
INSERT INTO `user` VALUES (17, 10085, 0xEFBBBF313233343536, 'Admin', 1, 'Admin');
INSERT INTO `user` VALUES (18, 66666, 0xEFBBBF313233343536, 'Admin', 1, 'Admin');

-- ----------------------------
-- View structure for customer_order_view
-- ----------------------------
DROP VIEW IF EXISTS `customer_order_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `customer_order_view` AS select `c`.`c_id` AS `客户编号`,`c`.`c_name` AS `客户姓名`,`c`.`c_email` AS `客户邮箱`,`o`.`o_num` AS `订单编号`,`o`.`o_date` AS `订单日期`,sum((`oi`.`quantity` * `oi`.`item_price`)) AS `订单总价` from ((`customers` `c` join `orders` `o` on((`c`.`c_id` = `o`.`c_id`))) join `orderitems` `oi` on((`o`.`o_num` = `oi`.`o_num`))) group by `c`.`c_id`,`c`.`c_name`,`c`.`c_email`,`o`.`o_num`,`o`.`o_date`;

-- ----------------------------
-- View structure for supplier_product_view
-- ----------------------------
DROP VIEW IF EXISTS `supplier_product_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `supplier_product_view` AS select `s`.`s_id` AS `供货商编号`,`s`.`s_name` AS `供货商名称`,`s`.`s_zip` AS `邮编`,`s`.`s_call` AS `联系电话`,`s`.`s_city` AS `所在城市`,`f`.`f_id` AS `水果编号`,`f`.`f_name` AS `水果名称`,`f`.`f_price` AS `单价` from (`suppliers` `s` join `fruits` `f` on((`s`.`s_id` = `f`.`s_id`))) order by `s`.`s_id`,`f`.`f_id`;

-- ----------------------------
-- Procedure structure for ADDCU
-- ----------------------------
DROP PROCEDURE IF EXISTS `ADDCU`;
delimiter ;;
CREATE PROCEDURE `ADDCU`()
BEGIN
    DECLARE v_count INT DEFAULT 0;
    
    -- 插入客户账号，忽略已存在的记录
    INSERT INTO user (u_id, pwd, remark)
    SELECT 
        c.c_id,
        AES_ENCRYPT(CONCAT(c.c_id, '123456'), 'hello'),
        'customer'
    FROM customers c
    LEFT JOIN user u ON c.c_id = u.u_id
    WHERE u.u_id IS NULL;
    
    -- 获取实际插入的行数
    SET v_count = ROW_COUNT();
    
    -- 返回处理结果
    SELECT CONCAT('成功添加了 ', v_count, ' 条客户账号记录') AS Result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for ADDSU
-- ----------------------------
DROP PROCEDURE IF EXISTS `ADDSU`;
delimiter ;;
CREATE PROCEDURE `ADDSU`()
BEGIN
    DECLARE v_count INT DEFAULT 0;
    
    -- 插入供货商账号，忽略已存在的记录
    INSERT INTO user (u_id, pwd, remark)
    SELECT 
        s.s_id,
        AES_ENCRYPT(CONCAT(s.s_id, '123456'), 'hello'),
        'supplier'
    FROM suppliers s
    LEFT JOIN user u ON s.s_id = u.u_id
    WHERE u.u_id IS NULL;
    
    -- 获取实际插入的行数
    SET v_count = ROW_COUNT();
    
    -- 返回处理结果
    SELECT CONCAT('成功添加了 ', v_count, ' 条供货商账号记录') AS Result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for COT
-- ----------------------------
DROP PROCEDURE IF EXISTS `COT`;
delimiter ;;
CREATE PROCEDURE `COT`(IN o_num_param INT, OUT total_amount DECIMAL(10,2))
BEGIN
    -- CalculateOrderTotal
    SELECT SUM(quantity * item_price) INTO total_amount
    FROM orderitems
    WHERE o_num = o_num_param;
    
    IF total_amount IS NULL THEN
        SET total_amount = 0.00;
    END IF;

    SELECT CONCAT('订单 ', o_num_param, ' 的总金额为: ', FORMAT(total_amount, 2)) AS Result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for UPDATE_ALL_O_P
-- ----------------------------
DROP PROCEDURE IF EXISTS `UPDATE_ALL_O_P`;
delimiter ;;
CREATE PROCEDURE `UPDATE_ALL_O_P`()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_o_num INT;
    DECLARE v_original_price DECIMAL(10,2);
    DECLARE v_discount DECIMAL(10,2);

    DECLARE order_cursor CURSOR FOR 
        SELECT DISTINCT o_num FROM orders;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    START TRANSACTION;
    OPEN order_cursor;
    order_loop: LOOP
        FETCH order_cursor INTO v_o_num;
        IF done THEN
            LEAVE order_loop;
        END IF;
        SELECT IFNULL(SUM(quantity * item_price), 0) INTO v_original_price
        FROM orderitems
        WHERE o_num = v_o_num;
        SELECT discount INTO v_discount
        FROM orders
        WHERE o_num = v_o_num;
        UPDATE orders
        SET 
            original_price = v_original_price,
            pay = v_original_price * v_discount
        WHERE o_num = v_o_num;
    END LOOP;
    CLOSE order_cursor;
    COMMIT; 
    SELECT CONCAT('已完成全部订单价格更新，共更新 ', ROW_COUNT(), ' 笔订单') AS Result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for UPDATE_OI_P
-- ----------------------------
DROP PROCEDURE IF EXISTS `UPDATE_OI_P`;
delimiter ;;
CREATE PROCEDURE `UPDATE_OI_P`()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_o_num INT;
    DECLARE v_o_item INT;
    DECLARE v_f_id VARCHAR(50);
    DECLARE v_current_price DECIMAL(10,2);
    
    DECLARE cur CURSOR FOR 
        SELECT oi.o_num, oi.o_item, oi.f_id, f.f_price
        FROM orderitems oi
        JOIN fruits f ON oi.f_id = f.f_id
        WHERE oi.item_price != f.f_price;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    
    OPEN cur;
    
    SET @updated_count = 0;
    
    read_loop: LOOP
        FETCH cur INTO v_o_num, v_o_item, v_f_id, v_current_price;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        UPDATE orderitems 
        SET item_price = v_current_price
        WHERE o_num = v_o_num 
        AND o_item = v_o_item;
        
        SET @updated_count = @updated_count + 1;
    END LOOP;
    
    CLOSE cur;
    
    COMMIT;
    
    SELECT CONCAT('成功更新了 ', @updated_count, ' 条订单项的价格') AS Result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for update_vip
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_vip`;
delimiter ;;
CREATE PROCEDURE `update_vip`()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE customer_id INT;
    DECLARE total_spent DECIMAL(10,2);
    DECLARE cur CURSOR FOR SELECT c_id FROM customers;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO customer_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        SELECT SUM(pay) INTO total_spent FROM orders WHERE c_id = customer_id;
        
        IF total_spent >= 100 AND total_spent < 200 THEN
            UPDATE customers SET VIP = 1, discount = 0.99 WHERE c_id = customer_id;
        ELSEIF total_spent >= 200 AND total_spent < 500 THEN
            UPDATE customers SET VIP = 2, discount = 0.97 WHERE c_id = customer_id;
        END IF;
    END LOOP;
    CLOSE cur;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table fruits
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_fruits_after_insert`;
delimiter ;;
CREATE TRIGGER `tr_fruits_after_insert` AFTER INSERT ON `fruits` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('fruits', 'INSERT', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table fruits
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_fruits_after_update`;
delimiter ;;
CREATE TRIGGER `tr_fruits_after_update` AFTER UPDATE ON `fruits` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('fruits', 'UPDATE', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table fruits
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_fruits_after_delete`;
delimiter ;;
CREATE TRIGGER `tr_fruits_after_delete` AFTER DELETE ON `fruits` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('fruits', 'DELETE', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_orderitems_before_insert`;
delimiter ;;
CREATE TRIGGER `tr_orderitems_before_insert` BEFORE INSERT ON `orderitems` FOR EACH ROW BEGIN
    DECLARE v_quantity INT;
    SELECT quantity INTO v_quantity FROM fruits WHERE f_id = NEW.f_id;
    IF v_quantity < NEW.quantity THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '库存不足';
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `before_insert_orderitems`;
delimiter ;;
CREATE TRIGGER `before_insert_orderitems` BEFORE INSERT ON `orderitems` FOR EACH ROW BEGIN
DECLARE fruit_price DECIMAL(8,2);
SELECT f_price INTO fruit_price
FROM fruits
WHERE f_id = NEW.f_id;
SET NEW.item_price = fruit_price;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_orderitems_after_insert`;
delimiter ;;
CREATE TRIGGER `tr_orderitems_after_insert` AFTER INSERT ON `orderitems` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('orderitems', 'INSERT', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `update_fruit_quantity_after_orderitem_insert`;
delimiter ;;
CREATE TRIGGER `update_fruit_quantity_after_orderitem_insert` AFTER INSERT ON `orderitems` FOR EACH ROW BEGIN
    UPDATE fruits 
    SET quantity = quantity - NEW.quantity
    WHERE f_id = NEW.f_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `update_order_total_after_insert`;
delimiter ;;
CREATE TRIGGER `update_order_total_after_insert` AFTER INSERT ON `orderitems` FOR EACH ROW BEGIN
    UPDATE orders o
    SET 
        original_price = (
            SELECT SUM(quantity * item_price)
            FROM orderitems
            WHERE o_num = NEW.o_num
        ),
        pay = (
            SELECT SUM(quantity * item_price) * o.discount
            FROM orderitems
            WHERE o_num = NEW.o_num
        )
    WHERE o.o_num = NEW.o_num;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `sync_order_totals_after_insert`;
delimiter ;;
CREATE TRIGGER `sync_order_totals_after_insert` AFTER INSERT ON `orderitems` FOR EACH ROW BEGIN
    DECLARE v_original_price DECIMAL(10,2);
    DECLARE v_discount DECIMAL(10,2);
    SELECT IFNULL(SUM(quantity * item_price), 0) INTO v_original_price
    FROM orderitems
    WHERE o_num = NEW.o_num;
    SELECT discount INTO v_discount
    FROM orders
    WHERE o_num = NEW.o_num;
    UPDATE orders
    SET 
        original_price = v_original_price,
        pay = v_original_price * v_discount,
        o_date = NOW()
    WHERE o_num = NEW.o_num;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_orderitems_before_update`;
delimiter ;;
CREATE TRIGGER `tr_orderitems_before_update` BEFORE UPDATE ON `orderitems` FOR EACH ROW BEGIN
    DECLARE v_quantity INT;
    DECLARE qty_diff INT;
    SELECT quantity INTO v_quantity FROM fruits WHERE f_id = NEW.f_id;
    SET qty_diff = NEW.quantity - OLD.quantity;
    IF qty_diff > 0 AND v_quantity < qty_diff THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '库存不足，无法增加订单数量';
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_orderitems_after_update`;
delimiter ;;
CREATE TRIGGER `tr_orderitems_after_update` AFTER UPDATE ON `orderitems` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('orderitems', 'UPDATE', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `update_fruit_quantity_after_orderitem_update`;
delimiter ;;
CREATE TRIGGER `update_fruit_quantity_after_orderitem_update` AFTER UPDATE ON `orderitems` FOR EACH ROW BEGIN
    DECLARE quantity_diff INT;
    SET quantity_diff = OLD.quantity - NEW.quantity;
    UPDATE fruits 
    SET quantity = quantity + quantity_diff
    WHERE f_id = NEW.f_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `sync_order_totals_after_update`;
delimiter ;;
CREATE TRIGGER `sync_order_totals_after_update` AFTER UPDATE ON `orderitems` FOR EACH ROW BEGIN
    DECLARE v_original_price DECIMAL(10,2);
    DECLARE v_discount DECIMAL(10,2);
    IF OLD.quantity != NEW.quantity OR OLD.item_price != NEW.item_price THEN
        SELECT IFNULL(SUM(quantity * item_price), 0) INTO v_original_price
        FROM orderitems
        WHERE o_num = NEW.o_num;
        SELECT discount INTO v_discount
        FROM orders
        WHERE o_num = NEW.o_num;
        UPDATE orders
        SET 
            original_price = v_original_price,
            pay = v_original_price * v_discount,
            o_date = NOW()
        WHERE o_num = NEW.o_num;
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_orderitems_after_delete`;
delimiter ;;
CREATE TRIGGER `tr_orderitems_after_delete` AFTER DELETE ON `orderitems` FOR EACH ROW BEGIN
    INSERT INTO operation (tablename, opname, optime, ConnUser)
    VALUES ('orderitems', 'DELETE', NOW(), CURRENT_USER());
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `orderitem_delete_trigger`;
delimiter ;;
CREATE TRIGGER `orderitem_delete_trigger` AFTER DELETE ON `orderitems` FOR EACH ROW BEGIN
    UPDATE fruits
    SET quantity = quantity + OLD.quantity
    WHERE f_id = OLD.f_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orderitems
-- ----------------------------
DROP TRIGGER IF EXISTS `sync_order_totals_after_delete`;
delimiter ;;
CREATE TRIGGER `sync_order_totals_after_delete` AFTER DELETE ON `orderitems` FOR EACH ROW BEGIN
    DECLARE v_original_price DECIMAL(10,2);
    DECLARE v_discount DECIMAL(10,2);
    SELECT IFNULL(SUM(quantity * item_price), 0) INTO v_original_price
    FROM orderitems
    WHERE o_num = OLD.o_num;
    SELECT discount INTO v_discount
    FROM orders
    WHERE o_num = OLD.o_num;
    UPDATE orders
    SET 
        original_price = v_original_price,
        pay = v_original_price * v_discount,
        o_date = NOW()
    WHERE o_num = OLD.o_num;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
