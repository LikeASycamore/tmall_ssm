package com.how2java.tmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.ReviewService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    OrderItemService orderItemService;
	@Override
	public void add(Product product) {
		productMapper.insert(product);
	}

	@Override
	public void delete(int id) {
		productMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 这里获取产品的时候设置了产品的分类，以及缩略图
	 */
	@Override
	public Product get(int id) {
		Product p = productMapper.selectByPrimaryKey(id);
		setCategory(p);
		setFirstProductImage(p);
		return p;
	}

	@Override
	public void update(Product product) {
		productMapper.updateByPrimaryKey(product);
	}
	
	//设置产品的分类
    public void setCategory(List<Product> ps){
        for (Product p : ps)
            setCategory(p);
    }
    public void setCategory(Product p){
        int cid = p.getCid();
        Category c = categoryService.get(cid);
        p.setCategory(c);
    }

    /**
     * 根据分类id查询出所有产品
     * 并为每一个产品都加上分类属性以及图片缩略
     */
	@Override
	public List<Product> list(int id) {
		ProductExample	productExample = new ProductExample();
		productExample.createCriteria().andCidEqualTo(id);
		productExample.setOrderByClause("id desc");
		List<Product> products = productMapper.selectByExample(productExample);
		//为查找出的所有分类设置一个category
		setCategory(products);
		//为所有产品设置预览图
		setFirstProductImage(products);
		return products;
	}

	/**
	 * 为分类下的所有产品设置预览图
	 */
	@Override
	 public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        if (!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }
	/**
	 * 设置产品的第一张图片作为缩略图
	 * 将产品列表都设置firstProductImage属性
	 * @param ps
	 */
    public void setFirstProductImage(List<Product> ps) {
        for (Product p : ps) {
            setFirstProductImage(p);
        }
    }

	@Override
	public void fill(List<Category> categorys) {
		for(Category category : categorys) {
			fill(category);
		}
	}
	/**
	 * 为单个分类填充产品
	 */
	@Override
	public void fill(Category category) {
		List<Product> products = list(category.getId());
		category.setProducts(products);
	}

	/**
	 * 为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示
	 */
	@Override
	public void fillByRow(List<Category> categorys) {
		 	int productNumberEachRow = 8; 
	        for (Category c : categorys) {
	        	//一个分类的所有产品
	            List<Product> products =  c.getProducts();
	            //存储所有分类的所有产品    其中productsOfEachRow为一行产品
	            List<List<Product>> productsByRow =  new ArrayList<>();
	            //一个分类的产品
	            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
	                int size = i+productNumberEachRow;
	                //将一个分类的总数与8n对比,取小，防止越界   譬如26 0-7 8-16 17-24 25-26  26>32?26:32
	                size= size>products.size()?products.size():size;
	                //将一个分类的每行产品加入总集合  subList [0,8)
	                List<Product> productsOfEachRow =products.subList(i, size);
	                productsByRow.add(productsOfEachRow);
	            }
	            //一个分类的多行List<List<Product>> 设置产品集合
	            c.setProductsByRow(productsByRow);
	        }
	}

	/**
	 * 为单条产品设置销量和评价条数两条属性
	 */
	@Override
	public void setSaleAndReviewNumber(Product p) {
		/*
		 * 1.根据产品id获取评价条数
		 * 2.获取销量
		 */
		int reviewCount = reviewService.getCount(p.getId());
		int saleCount = orderItemService.getSaleCount(p.getId());
		p.setReviewCount(reviewCount);
		p.setSaleCount(saleCount);
	}

	/**
	 * 为多条产品设置销量和评价条数
	 */
	@Override
	public void setSaleAndReviewNumber(List<Product> ps) {
		for(Product product : ps) {
			setSaleAndReviewNumber(product);
		}
	}

	/**
	 * 根据关键词对产品进行模糊查询
	 */
	@Override
	public List<Product> search(String keyword) {
		ProductExample example = new ProductExample();
		example.createCriteria().andNameLike("%"+keyword+"%");
		example.setOrderByClause("id desc");
		
		List<Product> products =  productMapper.selectByExample(example);
		setCategory(products);
		setFirstProductImage(products);
		
		return products;
	}

}
