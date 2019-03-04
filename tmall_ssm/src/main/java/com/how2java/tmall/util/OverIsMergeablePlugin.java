package com.how2java.tmall.util;
 
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
 
import java.lang.reflect.Field;
import java.util.List;
 
/**
 * 当第一次生成了CategoryMapper.xml之后，再次运行会导致CategoryMapper.xml生成重复内容，而影响正常的运行。
 * 这个类是为了解决mybatis逆向工程的这个问题
 * @author chenzhuo
 * date:2018年9月7日
 *
 */
public class OverIsMergeablePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
 
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}