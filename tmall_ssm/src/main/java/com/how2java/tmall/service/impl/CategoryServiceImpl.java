package com.how2java.tmall.service.impl;
 
import com.how2java.tmall.mapper.CategoryMapper;
import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

	@Override
	public List<Category> list() {
		return categoryMapper.selectByExample(null);
	}

	@Override
	public void add(Category category) {
		categoryMapper.insert(category);
	}

	@Override
	public int delete(int id) {
		try {
			//与产品表有外键约束，如果还有产品则无法删除
			categoryMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}

	@Override
	public Category get(int id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(Category category) {
		categoryMapper.updateByPrimaryKey(category);
	}
 
}