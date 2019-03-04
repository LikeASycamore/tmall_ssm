package com.how2java.tmall.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.PropertyService;
import com.how2java.tmall.util.Page;
 
@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;
 
    @RequestMapping("admin_property_add")
    public String add(Model model, Property p) {
        propertyService.add(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }
 
    @RequestMapping("admin_property_delete")
    public String delete(int id) {
        Property p = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:admin_property_list?cid="+p.getCid();
    }
 
    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id) {
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p", p);
        return "admin/editProperty";
    }
 
    @RequestMapping("admin_property_update")
    public String update(Property p) {
        propertyService.update(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }
 
    /**
     * 根据分类id查询属性
     * @param cid
     * @param model
     * @param page
     * @return
     */
    @RequestMapping("admin_property_list")
    public String list(int cid, Model model,  Page page) {
    	/*
    	 * 1.获取分类category
    	 * 2.获取分类下的属性 List<property>
    	 * 3.将分类和属性传递到前段页面中
    	 */
        Category c = categoryService.get(cid);
 
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = propertyService.list(cid);
 
        
        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        //这里设置param参数是因为，分页跳转其实就是反复调用本方法，只不过改变了start参数，而本方法需要cid
        //public String list(int cid, Model model,  Page page)
        page.setParam("&cid="+c.getId());
 
        model.addAttribute("ps", ps);
        model.addAttribute("c", c);
        model.addAttribute("page", page);
 
        return "admin/listProperty";
    }
}