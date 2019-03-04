package com.how2java.tmall.service;
  
import com.how2java.tmall.pojo.Property;
 
import java.util.List;
 
/**
 * 增删查改，以及根据分类id查询属性
 * @author chenzhuo
 * date:2018年9月8日
 *
 */
public interface PropertyService {
    void add(Property c);
    int delete(int id);
    void update(Property c);
    Property get(int id);
    List list(int cid);
}