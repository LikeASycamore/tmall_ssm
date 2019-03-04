package com.how2java.tmall.service;
  
import java.util.List;
 
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
 
public interface OrderItemService {
      
    void add(OrderItem c);
 
    void delete(int id);
    void update(OrderItem c);
    OrderItem get(int id);
    List<OrderItem> list();
 
    void fill(List<Order> os);
 
    void fill(Order o);
    
    /**
     * 获取销量
     * @param pid
     * @return
     */
    int getSaleCount(int  pid);
    
    /**
     * 查看用户所有订单项
     * @param uid
     * @return
     */
    List<OrderItem> listByUser(int uid);
 
}