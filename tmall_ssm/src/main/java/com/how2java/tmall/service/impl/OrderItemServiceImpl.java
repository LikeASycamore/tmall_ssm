package com.how2java.tmall.service.impl;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.how2java.tmall.mapper.OrderItemMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderExample;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.OrderItemExample;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.ProductService;
 
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;
 
    @Override
    public void add(OrderItem c) {
        orderItemMapper.insert(c);
    }
 
    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }
 
    @Override
    public void update(OrderItem c) {
        orderItemMapper.updateByPrimaryKeySelective(c);
    }
 
    @Override
    public OrderItem get(int id) {
        OrderItem result = orderItemMapper.selectByPrimaryKey(id);
        setProduct(result);
        return result;
    }
 
    public List<OrderItem> list(){
        OrderItemExample example =new OrderItemExample();
        example.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(example);
 
    }
    /**
     *  做订单与订单项的一对多关系。
     */
 
    @Override
    public void fill(List<Order> os) {
        for (Order o : os) {
            fill(o);
        }
    }
 
    /**
     *  1. 根据订单id查询出其对应的所有订单项
		2. 通过setProduct为所有的订单项设置Product属性
		3. 遍历所有的订单项，然后计算出该订单的总金额和总数量
		4. 最后再把订单项设置在订单的orderItems属性上。

     */
    public void fill(Order o) {
    	
    	OrderItemExample example = new OrderItemExample();
    	example.createCriteria().andOidEqualTo(o.getId());
    	List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
    	
    	setProduct(orderItems);
    	float total = 0;
    	int totalNumber = 0;
    	for(OrderItem orderItem:orderItems) {
    		Product product = orderItem.getProduct();
    		float price = product.getPromotePrice(); //单价
    		int number = orderItem.getNumber(); //数量
    		total += number*price;
    		totalNumber += number;
    	}
    	o.setTotal(total);
    	o.setTotalNumber(totalNumber);
    	o.setOrderItems(orderItems);
    }
 
    /**
     * 对多个订单项设置产品属性
     * @param ois
     */
    public void setProduct(List<OrderItem> ois){
        for (OrderItem oi: ois) {
            setProduct(oi);
        }
    }
 
    private void setProduct(OrderItem oi) {
        Product p = productService.get(oi.getPid());
        oi.setProduct(p);
    }

    /**
     * 获取销量，并且在订单产生后增加销量
     */
	@Override
	public int getSaleCount(int pid) {
		/*
		 * 1.根据产品id获取所有订单项
		 * 2.获取每个订单项的购买产品数量，但是订单项不一定是已经购买的，只有产生了订单才行
		 * 3.叠加起来就是销量
		 */
		OrderItemExample example =new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid).andOidIsNotNull();
        List<OrderItem> ois =orderItemMapper.selectByExample(example);
        int result =0;
        for (OrderItem oi : ois) {
            result+=oi.getNumber();
        }
        return result;
	}

	/**
	 * 查看用户所有没有关联订单的订单项，在加入购物车时需要使用
	 * 
	 */
	@Override
	public List<OrderItem> listByUser(int uid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andUidEqualTo(uid).andOidIsNull();
		
		List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
		setProduct(orderItems);
		return orderItems;
	}
}