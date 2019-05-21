package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	//setter Method 구현 않음
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
		
	public PurchaseRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping(value="/json/addPurchase/{prodNo}", method=RequestMethod.GET)
	public Product addPurchase(@PathVariable int prodNo) throws Exception{
		System.out.println("/purchase/addPurchase : GET");
		return productService.getProduct(prodNo);
	}
	
	@RequestMapping(value="/json/addPurchase/{prodNo}/{userId}", method=RequestMethod.POST)
	public Purchase addPurchase(@RequestBody Purchase purchase, @PathVariable int prodNo, @PathVariable String userId) throws Exception{
		System.out.println("/purchase/json/addPurchase : POST");
		purchase.setPurchaseProd(productService.getProduct(prodNo));
//		String checked = null;
//		if(checked!=null && checked.equals("on")) {
//			purchaseService.discountPurchase(purchase, 0.9, userId, "discountCoupon10");	
//		}
		purchase.setBuyer(userService.getUser(userId));
		purchase.setTranCode("2  ");
		purchaseService.addPurchase(purchase);
		
		Purchase returnPurchase = purchaseService.getPurchase2(prodNo);
		System.out.println(returnPurchase);
		return returnPurchase;
	}
	
	@RequestMapping(value="/json/listPurchase")
	public Map listPurchase(@RequestBody Search search, HttpSession session) throws Exception{
		System.out.println("/purchase/listPurchase");
	
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, "user01");
		//Map<String, Object> map = purchaseService.getPurchaseList(search, ((User)session.getAttribute("user")).getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		Map map2 = new HashMap();
		map2.put("list", map.get("list"));
		map2.put("resultPage", resultPage);
		map2.put("search", search);
		
		return map2;
	}
	
	@RequestMapping("/json/getPurchase/{tranNo}")
	public Purchase getPurchase(@PathVariable int tranNo) throws Exception{
		System.out.println("/json/getPurchase");
		
		return purchaseService.getPurchase(tranNo);
	}
	
	@RequestMapping(value="/json/updatePurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase updatePurchase(@PathVariable int tranNo) throws Exception{
		System.out.println("/purchse/updatePurchase : GET");
		
		return purchaseService.getPurchase(tranNo);
	}
	
	@RequestMapping(value="/json/updatePurchase/{tranNo}", method=RequestMethod.POST)
	public Purchase updatePurchase(@RequestBody Purchase purchase, @PathVariable int tranNo) throws Exception{
		System.out.println("/purchase/updatePurchase : POST");
		purchase.setPurchaseProd(purchaseService.getPurchase(tranNo).getPurchaseProd());
		
		purchaseService.updatePurchase(purchase);
		
		return purchaseService.getPurchase(tranNo);
	}
	
	@RequestMapping("/json/deletePurchase/{tranNo}")
	public Map deletePurchase(@PathVariable int tranNo) throws Exception{
		System.out.println("/purchase/deletePurchase");

		purchaseService.deletePurchase(tranNo);
		
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
	
	@RequestMapping("/json/updateTranCode/{tranNo}/{tranCode}")
	public Map updateTranCode(@PathVariable int tranNo, @PathVariable String tranCode) throws Exception{
		System.out.println("json/updateTranCode");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
	
	@RequestMapping("/json/updateTranCodeByProd/{prodNo}/{tranCode}")
	public Map updateTranCodeByProd(@PathVariable int prodNo, @PathVariable String tranCode) throws Exception{
		System.out.println("/updateTranCodeByProd");
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		Map map = new HashMap();
		map.put("message", "ok");
		return map;
	}
}