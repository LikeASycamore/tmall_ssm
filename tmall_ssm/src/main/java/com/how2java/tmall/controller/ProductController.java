package com.how2java.tmall.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.Page;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
    @Autowired
    CategoryService categoryService;
    
    /**
     * 增加产品
     * @param model
     * @param p
     * @return
     */
    @RequestMapping("admin_product_add")
    public String add(Product p) {
        p.setCreateDate(new Date());
        productService.add(p);
        return "redirect:admin_product_list?cid="+p.getCid();
    }
    
    @RequestMapping("admin_product_delete")
    public String delete(int id) {
        Product p = productService.get(id);
        productService.delete(id);
        return "redirect:admin_product_list?cid="+p.getCid();
    }
 
    /**
     * 点击链接，需要获取当前产品
     * 编辑产品，先获取产品id
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id) {
        Product p = productService.get(id);
        model.addAttribute("p", p);
        return "admin/editProduct";
    }
 
    /**
     * 编辑完成后提交，直接调用更新方法即可
     * @param p
     * @return
     */
    @RequestMapping("admin_product_update")
    public String update(Product p) {
        productService.update(p);
        return "redirect:admin_product_list?cid="+p.getCid();
    }
 
	
	@RequestMapping("admin_product_list")
	public String list(int cid,Model model,Page page) {
		/*
		 * 1.获取分类
		 * 2.将该分类下所有产品全部获取
		 * 3.设置分页参数，包括查询总数，起始位置，每页查询数，以及param参数，在这里是cid(因为分页是在分类下进行的，所以分页跳转需要cid)
		 * 4.将产品，分页和分类存储到模型中
		 */
		
		 	Category c = categoryService.get(cid);
		 	if (c==null) {
				System.out.println("无该分类");
			}
	        PageHelper.offsetPage(page.getStart(),page.getCount());
	        List<Product> ps = productService.list(cid);
	        //1.删掉这条代码
	        //productService.setFirstProductImage(ps.get(0));
	        int total = (int) new PageInfo<>(ps).getTotal();
	        page.setTotal(total);
	        page.setParam("&cid="+c.getId());
	 
	        model.addAttribute("ps", ps);
	        model.addAttribute("c", c);
	        model.addAttribute("page", page);
	 
	        return "admin/listProduct";
	}
	
}
