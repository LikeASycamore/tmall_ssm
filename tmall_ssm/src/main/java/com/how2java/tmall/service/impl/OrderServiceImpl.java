package com.how2java.tmall.service.impl;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderExample;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.OrderService;
import com.how2java.tmall.service.UserService;
 
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
 
    @Autowired
    UserService userService;
    
    @Autowired
    OrderItemService orderitemService;
 
    @Override
    public void add(Order c) {
        orderMapper.insert(c);
    }
 
    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }
 
    @Override
    public void update(Order c) {
        orderMapper.updateByPrimaryKeySelective(c);
    }
 
    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }
 
    public List<Order> list(){
        OrderExample example =new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> result =orderMapper.selectByExample(example);
        setUser(result);
        return result;
    }
    public void setUser(List<Order> os){
        for (Order o : os)
            setUser(o);
    }
    public void setUser(Order o){
        int uid = o.getUid();
        User u = userService.get(uid);
        o.setUser(u);
    }

    /**
     * 在订单项表中关联订单，同时在订单表中增加订单，返回订单的金额
     */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order c, List<OrderItem> ois) {
		
		float total = 0;
		add(c);
		/*
		 * 模拟异常情况，增加订单之后，订单项中没有关联订单
		 */
//		if (true) {
//			throw new RuntimeException();
//		}
		for (OrderItem orderItem : ois) {
			orderItem.setOid(c.getId());
			orderitemService.update(orderItem);
			total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
		}
		return total;
	}

	/**
	 * 根据用户和订单状态批量查询订单信息
	 * （注意这里的excludedStatus指的是不含这个状态）
	 */
	@Override
	public List<Order> list(int uid, String excludedStatus) {
		OrderExample example = new OrderExample();
		example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
		example.setOrderByClause("id desc");
		List<Order> orders = orderMapper.selectByExample(example);
		return orders;
	}
	
}