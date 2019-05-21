package com.model2.mvc.common;

public class Coupon {
	//field 
	private String discountCoupon10;
	private String freeDelivery;
	
	public Coupon() {
		// TODO Auto-generated constructor stub
	}

	public String getDiscountCoupon10() {
		return discountCoupon10;
	}

	public void setDiscountCoupon10(String discountCoupon10) {
		this.discountCoupon10 = discountCoupon10;
	}

	public String getFreeDelivery() {
		return freeDelivery;
	}

	public void setFreeDelivery(String freeDelivery) {
		this.freeDelivery = freeDelivery;
	}

	@Override
	public String toString() {
		return "Coupon [discountCoupon10=" + discountCoupon10 + ", freeDelivery=" + freeDelivery + "]";
	}

}
