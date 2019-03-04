package com.how2java.tmall.controller;
 
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
 
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page;
import com.how2java.tmall.util.UploadedImageFile;
 
@Controller
@RequestMapping("")
public class ProductImageController {
    @Autowired
    ProductService productService;
 
    @Autowired
    ProductImageService productImageService;
    
    @Autowired
    CategoryService categoryService;
 
    /**
     * 
     * @param pi 图片
     * @param session
     * @param uploadedImageFile
     * @return
     */
    @RequestMapping("admin_productImage_add")
    public String add(ProductImage  pi, HttpSession session, UploadedImageFile uploadedImageFile) {
        productImageService.add(pi);
        String fileName = pi.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        
        //单个图片
        if(ProductImageService.type_single.equals(pi.getType())){
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
        }//详情图片
        else{
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
        }
 
        File f = new File(imageFolder, fileName);
       
        if (!f.getParentFile().exists()) {
			System.out.println("不存在目录，创建目录");
			f.getParentFile().mkdirs();  //  创建img/productSinggle目录
		}
        
        try {
        	//将图片转化为jpg格式写入文件
            uploadedImageFile.getImage().transferTo(f);
            BufferedImage img = ImageUtil.change2jpg(f);
            ImageIO.write(img, "jpg", f);
 
            if(ProductImageService.type_single.equals(pi.getType())) {
                File f_small = new File(imageFolder_small, fileName);
                File f_middle = new File(imageFolder_middle, fileName);
 
                //  对single类型的图，分别在img/productSinggle_small  img/productSinggle_middle中添加两个不同尺寸的照片
                ImageUtil.resizeImage(f, 56, 56, f_small);
                ImageUtil.resizeImage(f, 217, 190, f_middle);
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:admin_productImage_list?pid="+pi.getPid();
    }
 
    /**
     * 删除服务器上的图片，以及数据库中的记录
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("admin_productImage_delete")
    public String delete(int id,HttpSession session) {
        ProductImage pi = productImageService.get(id);
 
        String fileName = pi.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
 
        if(ProductImageService.type_single.equals(pi.getType())){
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
            File imageFile = new File(imageFolder,fileName);
            File f_small = new File(imageFolder_small,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            imageFile.delete();
            f_small.delete();
            f_middle.delete();
 
        }
        else{
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
            File imageFile = new File(imageFolder,fileName);
            imageFile.delete();
        }
 
        productImageService.delete(id);
 
        return "redirect:admin_productImage_list?pid="+pi.getPid();
    }
 
    /**
     * 根据图片id以及类型:single和detail查询图片
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model) {
        Product p =productService.get(pid);
        Category category =  categoryService.get(p.getCid());
        p.setCategory(category);
        List<ProductImage> pisSingle = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> pisDetail = productImageService.list(pid, ProductImageService.type_detail);
        
        model.addAttribute("p", p);
        model.addAttribute("pisSingle", pisSingle);
        model.addAttribute("pisDetail", pisDetail);
 
        return "admin/listProductImage";
    }
}