package com.model2.mvc.web.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 상품관리 Controller
@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
		
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	
	//addProductView 보여주기
	@RequestMapping(value="/json/addProduct", method=RequestMethod.GET)
	public Map addProduct() throws Exception{
		System.out.println("/product/json/addProduct : GET");
		
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
	
	@RequestMapping(value="/json/addProduct", method=RequestMethod.POST)
	public Map addProduct(@RequestBody Product product) throws Exception{
		System.out.println("/product/json/addProduct : POST");
		productService.addProduct(product);
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
	
	@RequestMapping("/json/getProduct/{prodNo}")
	public Product getProduct(@PathVariable int prodNo) throws Exception{
		System.out.println("/product/json/getProduct");
		
		return productService.getProduct(prodNo);
	}
	
	@RequestMapping(value="/json/updateProduct", method=RequestMethod.GET)
	public Map updateProduct() throws Exception{
		System.out.println("/product/json/updateProduct : GET");	
		
		Map map = new HashMap();
		map.put("message", "update GET");
		return map;
	}
	
	@RequestMapping(value="/json/updateProduct", method=RequestMethod.POST)
	public Map updateProduct(@RequestBody Product product) throws Exception{
		System.out.println("/product/json/updateProduct : POST");
		
		productService.updateProduct(product);
		
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
	
	@RequestMapping("/json/deleteProduct/{prodNo}")
	public Map deleteProduct(@PathVariable int prodNo) throws Exception{
		System.out.println("/product/json/deleteProduct/{prodNo}");
		
		productService.deleteProduct(prodNo);
		
		Map map = new HashMap();
		map.put("message", "deleted");
		return map;
	}
	
	@RequestMapping("/json/listProduct")
	public Map listProduct(@RequestBody Search search) throws Exception{
		System.out.println("product/json/listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		Map returnmap = new HashMap();
		returnmap.put("list", map.get("list"));
		returnmap.put("resultPage", resultPage);
		returnmap.put("search", search);

		return returnmap;
	}
	
	@RequestMapping(value="json/autoCompleteUser")
	public List autoCompleteUser(@RequestBody Search search) throws Exception{
		System.out.println("/user/json/autoCompleteUser");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		List<String> list=productService.autoCompleteProduct(search);
		
		return list;
	}
	
	@ExceptionHandler(value=Exception.class)
	public String error(Exception e) throws Exception{
		e.printStackTrace();
		return e.getMessage();
	}
}