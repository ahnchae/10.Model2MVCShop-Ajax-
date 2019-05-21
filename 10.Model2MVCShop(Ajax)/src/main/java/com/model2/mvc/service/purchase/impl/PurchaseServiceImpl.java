package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Coupon;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {
	//field
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.insertPurchase(purchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDao.findPurchase(tranNo);
	}

	@Override
	public Purchase getPurchase2(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDao.findPurchase2(prodNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("list", purchaseDao.getPurchaseList(search, userId));
		map.put("totalCount", purchaseDao.getTotalCount(search, userId));
		return map;
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.updatePurchase(purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.updateTranCode(purchase);
	}

	@Override
	public void deletePurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.deletePurchase(tranNo);
	}

	@Override
	public Purchase discountPurchase(Purchase purchase, double discountRate, String userId, String couponType) throws Exception {
		// TODO Auto-generated method stub
		//가격 할인하기
		purchase.getPurchaseProd().setPrice((int)(purchase.getPurchaseProd().getPrice()*discountRate));
		
		//쿠폰 사용하기
		User user = userService.getUser(userId);
		
		Coupon coupon = new Coupon();
		switch (couponType) {
		case "discountCoupon10":
			userService.addCoupon("deleteDiscountCoupon10", user);
			break;
		}

		return purchase;
	}

}
