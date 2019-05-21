package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {
	//field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;

	
	public PurchaseDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Purchase findPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("PurchaseMapper.findPurchase", tranNo);
	}

	@Override
	public Purchase findPurchase2(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("PurchaseMapper.findPurchase2", prodNo);
	}

	@Override
	public List getPurchaseList(Search search, String userId) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap();
		map.put("search", search);
		map.put("userId", userId);
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}

	@Override
	public int getTotalCount(Search search, String userId) throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("search", search);
		map.put("userId", userId);
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", map);
	}

	@Override
	public void insertPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert("PurchaseMapper.insertPurchase", purchase);
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}

	@Override
	public void deletePurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.delete("PurchaseMapper.deletePurchase", tranNo);
	}

}
