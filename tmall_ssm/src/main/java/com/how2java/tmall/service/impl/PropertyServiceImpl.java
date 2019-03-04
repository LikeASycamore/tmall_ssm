package com.how2java.tmall.service.impl;
 
import com.how2java.tmall.mapper.PropertyMapper;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.pojo.PropertyExample;
import com.how2java.tmall.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    PropertyMapper propertyMapper;
 
    @Override
    public void add(Property p) {
        propertyMapper.insert(p);
    }
 
    /**
     * 当属性下面还有属性值式不能删除
     */
    @Override
    public int delete(int id) {
    	try {
    		propertyMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			return -1;
		}
    	return 0;
    }
 
    @Override
    public void update(Property p) {
        propertyMapper.updateByPrimaryKeySelective(p);
    }
 
    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }
 
    /**
     * 根据id查询分类属性
     */
    @Override
    public List list(int cid) {
        PropertyExample example =new PropertyExample();
        //查询表达式
        example.createCriteria().andCidEqualTo(cid);
        //排序方式
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
    }
 
}