package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.*;

import comparator.ProductAllComparator;
import comparator.ProductDateComparator;
import comparator.ProductPriceComparator;
import comparator.ProductReviewComparator;
import comparator.ProductSaleCountComparator;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class ForeController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ReviewService reviewService;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("forehome")
	public String home(Model model) {
		/*
		 * 1.查询出所有分类 2.将所有分类都填充上产品 products 3.为这些分类填充推荐产品集合 productsByRow
		 */
		List<Category> cs = categoryService.list();
		productService.fill(cs);
		productService.fillByRow(cs);
		model.addAttribute("cs", cs);
		return "fore/home";
	}

	/**
	 * 用户注册功能
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping("foreregister")
	public String register(Model model, User user) {
		String name = user.getName();
		name = HtmlUtils.htmlEscape(name);
		user.setName(name);
		if (userService.isExist(name)) {
			// 如果用户存在，那么设置用户为空；否则user参数会被带到register页面去
			String m = "用户名已经被使用,不能使用";
			model.addAttribute("msg", m);
			return "fore/register";
		}
		userService.add(user);
		return "redirect:registerSuccessPage";
	}

	/**
	 * 用户登录
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping("forelogin")
	public String login(@RequestParam String name, @RequestParam String password, RedirectAttributes model,
			HttpSession session) {
		/*
		 * 1.根据用户名和密码查询用户是否存在 2.如果存在跳转到首页，否则保存错误信息，跳转到登录界面,并且保存信息到session中
		 * 3.这里使用了重定向传递参数msg ，需要使用RedirectAttributes，以及addFlashAttribute()方法
		 * 重定向页面接收msg参数需要使用注解 ModelAttribute("msg")
		 */
		name = HtmlUtils.htmlEscape(name);
		User user = userService.get(name, password);
		if (user == null) {
			String msg = "用户密码错误";
			model.addFlashAttribute("msg", msg);
			return "redirect:loginPage"; // fore/login
		}
		session.setAttribute("user", user);
		return "redirect:forehome";
	}

	/**
	 * 退出登录,跳转到首页
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("forelogout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:forehome";
	}

	/**
	 * 前端查看产品详情
	 * 并将需要展示的参数全部设置好
	 * 包括 1.产品对应的单个图片集合 2. 产品对应的详情图片集合 3.产品对应的所有的评价  4.产品的销量和评价数量
	 * 
	 * @param pid
	 * @param model
	 * @return
	 */
	@RequestMapping("foreproduct")
	public String product(int pid, Model model) {
		/*
		 * 1. 获取参数pid 
		 * 2. 根据pid获取Product 对象p 
		 * 3. 根据对象p，获取这个产品对应的单个图片集合 
		 * 4. 根据对象p，获取这个产品对应的详情图片集合
		 * 5. 获取产品的所有属性值 6. 获取产品对应的所有的评价 
		 * 7. 设置产品的销量和评价数量 
		 * 8. 把上述取值放在request属性上 
		 * 9. 服务端跳转到 "product.jsp" 页面
		 */
		Product p = productService.get(pid);
		List<ProductImage> productSingleImages = productImageService.list(p.getId(), ProductImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(p.getId(), ProductImageService.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);

		List<PropertyValue> pvs = propertyValueService.list(p.getId());
		List<Review> reviews = reviewService.list(p.getId());
		productService.setSaleAndReviewNumber(p);
		model.addAttribute("reviews", reviews);
		model.addAttribute("p", p);
		model.addAttribute("pvs", pvs);
		return "fore/product";
	}

	/**
	 * 检验是否登录，并返回信息给jsp ajax函数
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("forecheckLogin")
	@ResponseBody
	public String checkLogin(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (null != user)
			return "success";
		return "fail";
	}

	/**
	 * 检查登录密码是否正确，并返回信息给jsp页面ajax函数
	 * 
	 * @param name
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping("foreloginAjax")
	@ResponseBody
	public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);
		User user = userService.get(name, password);

		if (null == user) {
			return "fail";
		}
		session.setAttribute("user", user);
		return "success";
	}

	/**
	 * 按照sort排序比较进行分类
	 * 
	 * @param cid
	 * @param sort
	 * @param model
	 * @return
	 */
	@RequestMapping("forecategory")
	public String category(int cid, String sort, Model model) {
		/*
		 *  1. 获取参数cid
			2. 根据cid获取分类Category对象 c
			3. 为c填充产品
			4. 为产品填充销量和评价数据
			5. 获取参数sort
			5.1 如果sort==null，即不排序
			5.2 如果sort!=null，则根据sort的值，从5个Comparator比较器中选择一个对应的排序器进行排序
			6. 把c放在model中
			7. 服务端跳转到 category.jsp
		 */
		Category category = categoryService.get(cid);
		productService.fill(category);
		productService.setSaleAndReviewNumber(category.getProducts());

		if (sort != null) {
			switch (sort) {
			case "review":
				Collections.sort(category.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(category.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(category.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(category.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(category.getProducts(), new ProductAllComparator());
				break;
			}
		}
		model.addAttribute("c",category);
		return "fore/category";
	}
	
	/**
	 * 根据关键词进行模糊查询
	 * @param keyword
	 * @param model
	 * @return
	 */
	
	@RequestMapping("foresearch")
    public String search( String keyword,Model model){
 
        PageHelper.offsetPage(0,20);
        List<Product> ps= productService.search(keyword);
        productService.setSaleAndReviewNumber(ps);
        model.addAttribute("ps",ps);
        return "fore/searchResult";
    }
	
	/**
	 * 立即购买，买一件产品
	 * @param num
	 * @param pid
	 * @param session
	 * @return
	 */
	@RequestMapping("forebuyone")
	public String buyone(int num,int pid,HttpSession session) {
		
//		1. 获取参数pid
//		2. 获取参数num
//		3. 根据pid获取产品对象p
//		4. 从session中获取用户对象user
//
//		接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
//		a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
//		a.1 基于用户对象user，查询没有生成订单的订单项集合
//		a.2 遍历这个集合
//		a.3 如果产品是一样的话，就进行数量追加
//		a.4 获取这个订单项的 id
//
//		b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
//		b.1 生成新的订单项
//		b.2 设置数量，用户和产品
//		b.3 插入到数据库
//		b.4 获取这个订单项的 id
		
		/*
		 * 这里有一个逻辑问题，原购物车产品数量加上现在的产品数量可能会超过产品库存
		 * 超过库存应该重新设置
		 * 
		 */
		
		
		int orderItemId = 0 ;
		Product product = productService.get(pid);
		User user = (User) session.getAttribute("user");
		
		List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
		boolean flag = false;
		for(OrderItem orderItem : orderItems) {//如果产品id相同，证明购物车中有该产品，直接在购物车基础上增加购买数量即可
			if (orderItem.getPid().intValue()==product.getId().intValue()) {
				flag = true;
				orderItem.setNumber(orderItem.getNumber()+num);
				orderItemService.update(orderItem);
				orderItemId = orderItem.getId();
				break;
			}
		}
		if (!flag) {//购物车没有该产品则新增订单项
			OrderItem oi = new OrderItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			orderItemService.add(oi);
			orderItemId = oi.getId();
		}
		return "redirect:forebuy?oiid="+orderItemId;
	}
	
	/**
	 * 结算页面
	 * @param model
	 * @param oiid
	 * @param session
	 * @return
	 */
	@RequestMapping("forebuy")
    public String buy( Model model,String[] oiid,HttpSession session){
        List<OrderItem> ois = new ArrayList<>(); //购物车订单项
        float total = 0;
        /*
         * 关联订单，以及设置总结算金额
         */
        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi= orderItemService.get(id);
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }
        
        session.setAttribute("ois", ois);
        model.addAttribute("total", total);
        return "fore/buy";
    }
	
	/**
	 * 加入购物车
	 * @param pid
	 * @param num
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, Model model,HttpSession session) {
		/*
		 *  a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
			a.1 基于用户对象user，查询没有生成订单的订单项集合
			a.2 遍历这个集合
			a.3 如果产品是一样的话，就进行数量追加
			a.4 获取这个订单项的 id
			
			b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
			b.1 生成新的订单项
			b.2 设置数量，用户和产品
			b.3 插入到数据库
			b.4 获取这个订单项的 id
		 */
        Product p = productService.get(pid);
        User user =(User)  session.getAttribute("user");
        boolean found = false;
 
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }
 
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
        }
        return "success";
    }
	
	/**
	 * 查看购物车
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("forecart")
	public String cart(Model model , HttpSession session) {
		/*
		 * 查看购物车首先需要登录,否则获取user会报错
		 * 根据用户id，查看所有订单项
		 * 跳转到查看购物车页面
		 */
		
		User user = (User) session.getAttribute("user");
		List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
		model.addAttribute("ois",orderItems);
		return "fore/cart";
	}
	
	/**
	 * ajax改变购物车中订单项数量
	 * @param pid
	 * @param session
	 * @param number
	 * @param model
	 * @return
	 */
	@RequestMapping("forechangeOrderItem")
	@ResponseBody
	public String changeOrderItem(int pid,HttpSession session,int number,Model model) {
		/*
		 *  1. 判断用户是否登录
			2. 获取pid和number
			3. 遍历出用户当前所有的未生成订单的OrderItem
			4. 根据pid找到匹配的OrderItem，并修改数量后更新到数据库
			5. 返回字符串"success"
		 */
		User user = (User) session.getAttribute("user");
		if (user==null) {
			return "fail";
		}
		List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
		for(OrderItem orderItem:orderItems) {
			if (orderItem.getPid().intValue()==pid) {
				orderItem.setNumber(number);
				orderItemService.update(orderItem);
			}
		}
		System.out.println("————————————————————数目成功改变——————————————————————————");
		return "success";
	}
	/**
	 * 删除订单项条目
	 * @param model
	 * @param session
	 * @param oiid
	 * @return
	 */
	@RequestMapping("foredeleteOrderItem")
	@ResponseBody
	public String deleteOrderItem(Model model,HttpSession session,int oiid) {
		User user = (User) session.getAttribute("user");
		if (user==null) {
			return "fail";
		}
		orderItemService.delete(oiid);
		return "success";
	}
	
	/**
	 * 创建订单
	 * @param model
	 * @param order
	 * @param session
	 * @return
	 */
	@RequestMapping("forecreateOrder")
	public String createOrder( Model model,Order order,HttpSession session){
		/*
		 * 1.获取用户
		 * 2.设置订单号：时间+4位随机数
		 * 3.设置订单创建时间
		 * 4.设置uid
		 * 5.设置订单状态：等待付款
		 * 6. 获取订单项，并在数据库中插入订单
		 */
	    User user =(User)  session.getAttribute("user");
	    String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
	    order.setOrderCode(orderCode);
	    order.setCreateDate(new Date());
	    order.setUid(user.getId());
	    order.setStatus(OrderService.waitPay);
	    List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");
	 
	    float total =orderService.add(order,ois);
	    return "redirect:forealipay?oid="+order.getId() +"&total="+total;
	}
	
	/**
	 * 将支付状态改变为已支付
	 * @return
	 */
	@RequestMapping("forepayed")
	public String forepayed(int oid,Model model) {
		/*
		 * 1.根据订单id获取订单
		 * 2.将订单的支付状态修改为待发货
		 * 3.设置订单的支付日期
		 * 4.将订单添加到模型中
		 */
		Order order = orderService.get(oid);
		order.setPayDate(new Date());
		order.setStatus(orderService.waitDelivery);
		orderService.update(order);
		model.addAttribute("o", order);
		return "fore/payed";
	}
	
	//查看未删除的订单
	@RequestMapping("forebought")
	public String forebought(Model model,HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Order> orders = orderService.list(user.getId(), OrderService.delete);
		//为订单关联订单项，以便于查看具体条目
		orderItemService.fill(orders);
		model.addAttribute("os",orders);
		return "fore/bought";
	}
	
	/**
	 * 订单页面点击确认收货按钮，
	 * @param oid
	 * @param model
	 * @return
	 */
	@RequestMapping("foreconfirmPay")
	public String confirmPay(int oid,Model model) {
		/*
		 * 1.根据订单id获取订单
		 * 2.对订单填充订单项信息
		 * 3.将order添加到模型中
		 * 4.跳转到确认收货界面
		 */
		Order order = orderService.get(oid);
		orderItemService.fill(order);
		model.addAttribute("o",order);
		return "fore/confirmPay";
	}
	
	/**
	 * 确认收货
	 * @param oid
	 * @return
	 */
	@RequestMapping("foreorderConfirmed")
	public String orderConfirmed(int oid) {
		/*
		 * 1.将订单状态设置为待评价
		 * 2.设置确认收货日期
		 * 3，将数据更新到数据库
		 * 4，跳转到收货成功界面
		 */
		Order order = orderService.get(oid);
		order.setStatus(OrderService.waitReview);
		order.setConfirmDate(new Date());
		orderService.update(order);
		return "fore/orderConfirmed";
	}
	
	/**
	 * ajax删除订单，但是不是在数据库中真正的删除订单，只是将订单的状态修改为deleted
	 * @param oid
	 * @param model
	 * @return
	 */
	@RequestMapping("foredeleteOrder")
	@ResponseBody
	public String foredeleteOrder(int oid,Model model) {
		/*
		 * 1.获取订单id进行删除
		 * 2.删除成功返回success json字符串
		 * 
		 */
		Order order = orderService.get(oid);
		order.setStatus(OrderService.delete);
		orderService.update(order);
		return "success";
	}
	
	/**
	 * 点击评价按钮，跳转到产品的评价界面
	 * @param model
	 * @param oid
	 * @return
	 */
	@RequestMapping("forereview")
	public String review( Model model,int oid) {
		/*
		 * 1.获取订单
		 * 2.将订单项等信息填充到订单中
		 * 3.根据订单获取订单条目 这里简化了，只获取第一个订单项
		 * 4.获取该订单项的产品，并获取该产品的所有评价
		 * 5.将评价信息，产品信息，以及订单信息添加到model中
		 */
		Order order = orderService.get(oid);
		orderItemService.fill(order);
		OrderItem orderItem = order.getOrderItems().get(0);
		Product product = orderItem.getProduct();
		List<Review> reviews = reviewService.list(product.getId());
		
		model.addAttribute("p", product);
		model.addAttribute("o", order);
		model.addAttribute("reviews", reviews);
		return "fore/review";
	}
	
	/**
	 * 提交评价，重定向到产品评价页面
	 * @param model
	 * @param session
	 * @param oid
	 * @param pid
	 * @param content
	 * @return
	 */
	@RequestMapping("foredoreview")
	public String doreview(Model model, HttpSession session, @RequestParam("oid") int oid, @RequestParam("pid") int pid,
			String content) {
		/*
		 * 1.获取订单，设置订单状态为交易完成finish
		 * 2.获取评价信息
		 * 3.设置评价信息，创建时间，以及pid，uid等信息
		 * 4.将评价插入到数据库
		 */
		Order o = orderService.get(oid);
		o.setStatus(OrderService.finish);
		orderService.update(o);

		Product p = productService.get(pid);
		content = HtmlUtils.htmlEscape(content);

		User user = (User) session.getAttribute("user");
		Review review = new Review();
		review.setContent(content);
		review.setPid(pid);
		review.setCreateDate(new Date());
		review.setUid(user.getId());
		reviewService.add(review);
		return "redirect:forereview?oid=" + oid + "&showonly=true";
	}
	
}