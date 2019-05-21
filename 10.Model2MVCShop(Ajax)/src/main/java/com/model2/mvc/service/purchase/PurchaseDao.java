package com.model2.mvc.service.purchase;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;



public interface PurchaseDao {
	public Purchase findPurchase(int tranNo) throws Exception;
	
	public Purchase findPurchase2(int prodNo) throws Exception;
	
	public List getPurchaseList(Search search, String userId) throws Exception;
	
	public int getTotalCount(Search search, String userId) throws Exception;

	public void insertPurchase(Purchase purchase) throws Exception; 
	
	public void updatePurchase(Purchase purchase) throws Exception;

	public void updateTranCode(Purchase purchase) throws Exception; 

	public void deletePurchase(int tranNo) throws Exception; 
}
