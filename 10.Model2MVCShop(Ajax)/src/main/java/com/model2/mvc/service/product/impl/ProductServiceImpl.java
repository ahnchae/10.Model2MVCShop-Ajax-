package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
	//field
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;
	
	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	//constructor
	public ProductServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		productDao.insertProduct(product);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return productDao.findProduct(prodNo);
	}

	@Override
	public Map getProductList(Search search) throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("list", productDao.getProductList(search));
		map.put("totalCount", productDao.getTotalCount(search));
		return map;
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		productDao.updateProduct(product);
	}

	@Override
	public void deleteProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		productDao.deleteProduct(prodNo);
	}

}
