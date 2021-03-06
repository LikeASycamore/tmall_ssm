package com.how2java.tmall.service.impl;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.how2java.tmall.mapper.UserMapper;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.pojo.UserExample;
import com.how2java.tmall.service.UserService;
 
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
 
    @Override
    public void add(User u) {
        userMapper.insert(u);
    }
 
    @Override
    public void delete(int id) {
        userMapper.deleteByPrimaryKey(id);
    }
 
    @Override
    public void update(User u) {
        userMapper.updateByPrimaryKeySelective(u);
    }
 
    @Override
    public User get(int id) {
        return userMapper.selectByPrimaryKey(id);
    }
 
    public List<User> list(){
        UserExample example =new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);
 
    }

    /**
     * 判断用户名是否存在，存在返回true，否则返回false
     */
	@Override
	public boolean isExist(String name) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name);
		List<User> users = userMapper.selectByExample(example);
		return users.isEmpty()?false:true;
	}

	@Override
	public User get(String name, String password) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
		List<User> users = userMapper.selectByExample(example);
		return !users.isEmpty()?users.get(0):null;
	}
 
}