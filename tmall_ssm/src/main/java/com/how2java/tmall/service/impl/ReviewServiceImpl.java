package com.how2java.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2java.tmall.mapper.ReviewMapper;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.pojo.ReviewExample;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.ReviewService;
import com.how2java.tmall.service.UserService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	public ReviewMapper reviewMapper;
	
	@Autowired
	public UserService userService;
	
	@Override
	public void add(Review c) {
		reviewMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Review c) {
		reviewMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	/**
	 * 根据产品id 降序查询出产品的所有评价
	 * 并将所有的评价都设置user属性
	 */
	@Override
	public List<Review> list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");
		
		List<Review> reviews = reviewMapper.selectByExample(example);
		//对每个评价都设置下新增的用户属性，以便后面取用
		reviewSetUser(reviews);
		return reviews;
	}
	/**
	 * 获取产品评价数量
	 */
	@Override
	public int getCount(int pid) {
		return list(pid).size();
	}
	
	/**
	 * 对多条评价添加user属性
	 */
	public void reviewSetUser(List<Review> reviews) {
		for(Review review : reviews) {
			reviewSetUser(review);
		}
	}
	
	/**
	 * 对单条评价设置user属性
	 * @param review 评价
	 */
	public void reviewSetUser(Review review) {
		User user = userService.get(review.getUid());
		review.setUser(user);
	}

}
