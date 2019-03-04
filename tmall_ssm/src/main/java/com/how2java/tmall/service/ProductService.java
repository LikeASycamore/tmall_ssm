package com.how2java.tmall.service;

import java.util.List;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;

public interface ProductService {
	void add(Product product);
	
	void delete(int id);
	
	Product get(int id);
	
	void update(Product product);
	
	List<Product> list(int id);

	void setFirstProductImage(Product p);
	
	public void fill(List<Category> categorys);
	
	//为分类填充产品
	public void fill(Category category);
	
	public void fillByRow(List<Category> categorys);
	
	//为产品设置销量和评价条数
	void setSaleAndReviewNumber(Product p);
	void setSaleAndReviewNumber(List<Product> ps);
	
	//搜索栏按关键字进行查找
	List<Product> search(String keyword);
}
