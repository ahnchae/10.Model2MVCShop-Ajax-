package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
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
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping(value="/addPurchase", method=RequestMethod.GET)
	public ModelAndView addPurchase(@RequestParam("prodNo") int prodNo) throws Exception{
		System.out.println("/purchase/addPurchase : GET");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", productService.getProduct(prodNo));
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		return modelAndView;
	}
	
	@RequestMapping(value="/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId, @RequestParam(value="couponPrice", required=false) String checked) throws Exception{
		System.out.println("/purchase/addPurchase : POST");
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		if(checked!=null && checked.equals("on")) {
			purchaseService.discountPurchase(purchase, 0.9, userId, "discountCoupon10");	
		}
		purchase.setBuyer(userService.getUser(userId));
		purchase.setTranCode("2  ");
		purchaseService.addPurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		modelAndView.addObject("purchase",purchaseService.getPurchase2(prodNo));
		
		return modelAndView;
	}
	
	@RequestMapping("/listPurchase")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpSession session) throws Exception{
		System.out.println("/purchase/listPurchase");
	
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, ((User)session.getAttribute("user")).getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/getPurchase");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchaseService.getPurchase(tranNo));
		
		return modelAndView;
	}
	
	@RequestMapping(value="/updatePurchase", method=RequestMethod.GET)
	public ModelAndView updatePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/purchse/updatePurchase : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		return modelAndView;
	}
	
	@RequestMapping(value="/updatePurchase", method=RequestMethod.POST)
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/purchase/updatePurchase : POST");
		purchase.setPurchaseProd(purchaseService.getPurchase(tranNo).getPurchaseProd());
		
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchaseService.getPurchase(tranNo));
		return modelAndView;
	}
	
	@RequestMapping("/deletePurchase")
	public ModelAndView deletePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/purchase/deletePurchase");

		purchaseService.deletePurchase(tranNo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCode")
	public ModelAndView updateTranCode(@RequestParam("tranNo") int tranNo, @RequestParam("tranCode") String tranCode) throws Exception{
		System.out.println("/updateTranCode");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@RequestParam("prodNo") int prodNo, @RequestParam("tranCode") String tranCode) throws Exception{
		System.out.println("/updateTranCodeByProd");
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/product/listProduct?menu=manage");
		return modelAndView;
	}
}