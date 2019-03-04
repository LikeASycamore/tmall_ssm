package com.how2java.tmall.service;

import java.util.List;

import com.how2java.tmall.pojo.ProductImage;

/**
 * 产品图片业务逻辑  图片分为简略图片和详情图片
 * @author chenzhuo
 *
 */
public interface ProductImageService {
	String type_single = "type_single";
	String type_detail = "type_detail";

	void add(ProductImage pi);

	void delete(int id);

	void update(ProductImage pi);

	ProductImage get(int id);

	//根据产品id和图片类型查询
	List<ProductImage> list(int pid, String type);

}
