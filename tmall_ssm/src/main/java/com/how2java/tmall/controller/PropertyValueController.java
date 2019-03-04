package com.how2java.tmall.controller;
 
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
 
import java.util.List;
 
@Controller
@RequestMapping("")
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;
 
    /**
     * 产品修改属性值
     * 先将产品属性值初始化，再进行获取
     * @param model
     * @param pid 产品id
     * @return
     */
    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model,int pid) {
        Product p = productService.get(pid);
        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(p.getId());
 
        model.addAttribute("p", p);
        model.addAttribute("pvs", pvs);
        return "admin/editPropertyValue";
    }
    
    /**
     * 提交更新
     * responseBody注解将success转化响应给$.post 
     * 等价于response.getWriter.write(JSONObject.fromObject(user).toString());
     * @param pv
     * @return
     */
    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue pv) {
        propertyValueService.update(pv);
        return "success";
    }
}