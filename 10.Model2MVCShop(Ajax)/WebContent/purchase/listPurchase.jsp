<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%--
<%@page import="com.model2.mvc.common.util.CommonUtil"%>
<%@page import="com.model2.mvc.common.Search"%>
<%@page import="com.model2.mvc.common.Page"%>
<%@page import="com.model2.mvc.service.domain.Purchase"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%
	List<Purchase> list= (List<Purchase>)request.getAttribute("list");
	Page resultPage=(Page)request.getAttribute("resultPage");
	
	Search search = (Search)request.getAttribute("search");
	//==> null 을 ""(nullString)으로 변경
	String searchCondition = CommonUtil.null2str(search.getSearchCondition());
	String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
%>
--%>

<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	function fncGetUserList(currentPage) {
		$("#currentPage").val(currentPage);
	   	$('form[name=detailForm]').attr("method", "POST").attr("action", "/purchase/listPurchase").submit();		
	}
	
	$(function(){
		$('.ct_list_pop td:nth-child(1)').on("click", function(){
			var tranNo = $($(this).next('td').html()).val();
			//location="/purchase/getPurchase?tranNo="+tranNo;
			
			if($( "#"+tranNo+"" ).html().length!=0){
				$( "#"+tranNo+"" ).empty();
			}else{$.ajax({
				url:"/purchase/json/getPurchase/"+tranNo,
				method:"POST",
				dataType:"json",
				headers:{
					"Accept" : "application/json",
					"Content-Type":"application/json"
				},
				success: function(JSONData, status){
					//alert(status);
					//alert("JSONData : \n"+JSONData);
				
					var displayValue= "<h3>"+"주문일: "+JSONData.orderDate+"<br>"
											+"상품명: "+JSONData.purchaseProd.prodName+"<br>"
											+"구매자: "+JSONData.buyer.userId+"<br>";
							   displayValue += "배송 받는 분: ";
			 				   displayValue += (JSONData.receiverName==null) ? "<br>" : JSONData.receiverName+"<br>"
			 						   
							   displayValue +=  "연락처: ";	   
							   displayValue += (JSONData.receiverPhone==null) ? "<br>" : JSONData.receiverPhone+"<br>"
									   
							   displayValue += "구매가격: "+JSONData.soldPrice+"&nbsp;(정가 : "+JSONData.purchaseProd.price+")<br>"
							   
							   displayValue +=  "배송 받으실 날짜: ";	   
							   displayValue += (JSONData.divyDate==null) ? "<br>" : JSONData.divyDate+"<br>"
									   
							   displayValue +=   "요청사항: ";
							   displayValue += (JSONData.divyRequest==null) ? "<br>" : JSONData.divyRequest+"<br>"
							   displayValue += "<hr>";
					if(JSONData.tranCode=='2  '){
						displayValue += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='/purchase/deletePurchase?tranNo="+JSONData.tranNo+"'>"+JSONData.purchaseProd.prodName+" 구매 취소</a>";
						displayValue += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='/purchase/updatePurchase?tranNo="+JSONData.tranNo+"'>구매정보수정</a>";
					}
					displayValue += "</h3>"
					$("#"+tranNo+"").html(displayValue);

				},
				error:function(jqXHR){
					alert("error : "+jqXHR.status);
				}
			})
			}
		})
		
		$('.ct_list_pop td:nth-child(3)').on("mousedown", function(){
			var prodNo = $($(this).next('td').html()).val();
			//location="/product/getProduct.do?prodNo="+prodNo;
			var tranNo = $($(this).prev('td').html()).val();
			console.log("tranNo : "+tranNo);
			if($( "#"+prodNo+"" ).html().length!=0){
				$("#"+prodNo+"").empty();	
			}else{
			$.ajax({
				url:"/product/json/getProduct/"+prodNo,
				method:"GET",
				dataType:"json",
				headers:{
					"Accept" : "application/json",
					"Content-Type":"application/json"
				},
				success: function(JSONData, status){
					//alert(status);
					//alert("JSONData : \n"+JSONData);
				
					var displayValue= "<h3><hr>"+"상품번호: "+JSONData.prodNo+"<br>"
															+"상품명: "+JSONData.prodName+"<br>"
															+"상세 설명: "+JSONData.prodDetail+"<br>"
															+"상품 이미지: <img src='../images/uploadFiles/"+JSONData.fileName+"' width=200/><br>"
															+"가격: "+JSONData.price+"<br>"
															+"제조일: "+JSONData.manuDate+"<br>"
															+"등록일: "+JSONData.regDate+"<br>"
															+"<hr></h3>";

					$("#"+prodNo+"").html(displayValue);

				},
				error:function(jqXHR){
					alert("error : "+jqXHR.status);
				}
			})
			}
		})
		
		
		$('.ct_list_pop td:nth-child(11):contains("물건도착")').on("click", function(){
			var tranNo = $($(this).next('td').html()).val();
			location="/purchase/updateTranCode?tranNo="+tranNo+"&tranCode=4";
		})
	})
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">전체  ${resultPage.totalCount } 건수, 현재${resultPage.currentPage } 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">주문일자</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">요청사항</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">수취확인</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

<c:set var="i" value="0" />
<c:forEach items="${list}" var="purchase">	
	<tr class="ct_list_pop">
			<td align="center">${i+1}</td>
			<%--<a href="/purchase/getPurchase?tranNo=${purchase.tranNo}">${i+1}</a> --%>
		<c:set var="i" value="${i+1}"/>
		<td><input type="hidden" value="${purchase.tranNo}"/></td>
		<td align="left">
			${purchase.purchaseProd.prodName}
		</td>
		<td><input type="hidden" value="${purchase.purchaseProd.prodNo}"/></td>
		<td align="left">${purchase.orderDate}</td>
		<td></td>
		<td align="left">${purchase.divyRequest}</td>
		<td></td>
		<td align="left">현재
				
				<c:choose>
				<c:when test="${purchase.tranCode=='2  '}">
				구매 완료
				</c:when>
				<c:when test="${purchase.tranCode=='3  '}">
				배송중
				</c:when>
				<c:when test="${purchase.tranCode=='4  '}">
				배송 완료
				</c:when>
			</c:choose>
				상태 입니다.</td>
		<td></td>
		<td align="left">
		<c:if test="${purchase.tranCode=='3  '}">
		물건도착
		</c:if>
		<c:if test="${purchase.tranCode=='4  '}">
		배송완료
		</c:if>
		</td>
		<td><input type="hidden" value="${purchase.tranNo}"/></td>
	</tr>
	<tr>
		<td id="${purchase.tranNo}" colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<tr>
		<td id="${purchase.purchaseProd.prodNo}" colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
</c:forEach>

	
<%--<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr> --%>	
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
<tr>
		<td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
<jsp:include page="../common/pageNavigator.jsp"/>
    	</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>