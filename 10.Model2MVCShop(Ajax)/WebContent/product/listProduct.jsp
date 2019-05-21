<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta charset="EUC-KR">
	<title>상품 목록조회</title>

	<link rel="stylesheet" href="/css/admin.css" type="text/css">
	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
	<script type="text/javascript">
		// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
		function fncGetUserList(currentPage) {
			$("#currentPage").val(currentPage);
			var uri = "/product/listProduct?menu=${param.menu}";
			uri = $("form").attr("method" , "POST").attr("action",uri);	
			uri.submit();
		}
		
		$(function(){
			$('td.ct_btn01:contains("검색"),input[value="prodNo"],input[value="priceASC"],input[value="priceDESC"]').on("click", function(){
				$("#currentPage").val('1');
				var uri = "/product/listProduct?menu=${param.menu}";
				$("form").attr("method" , "POST").attr("action",uri).submit();	
			})

			$( ".ct_list_pop td:nth-child(3)" ).on("click" , function() {
				var prodNo = $(this).next('td').next('td').next('td').next('td').text().trim();
				//var uri = "/product/getProduct?prodNo="+prodNo+"&menu=search";
				var tran=$(this).next('td').next('td').next('td').next('td').next('td').next('td').text().trim();
				if('${param.menu}'=='manage' && tran=='판매중'){
					uri = "/product/getProduct?prodNo="+prodNo+"&menu=manage";
					self.location = uri;
				}else{
					if($( "#"+prodNo+"" ).html().length!=0){
						$( "#"+prodNo+"" ).empty();
					}else{
					$.ajax(
							{
								url : "/product/json/getProduct/"+prodNo,
								method : "GET",
								dataType : "json",
								headers : {
									"Accept" : "application/json",
									"Content-Type" : "application/json"
								},
								success : function(JSONData, status){
									//alert(status);
									//alert("JSONData : \n"+JSONData);
									
									var displayValue= "<h3>"+"상품번호: "+JSONData.prodNo+"<br>"
															+"상품명: "+JSONData.prodName+"<br>"
															+"상세 설명: "+JSONData.prodDetail+"<br>"
															+"상품 이미지: <img src='../images/uploadFiles/"+JSONData.fileName+"'/><br>"
															+"가격: "+JSONData.price+"<br>"
															+"제조일: "+JSONData.manuDate+"<br>"
															+"등록일: "+JSONData.regDate+"<br>"
															+"<hr>";
										if(JSONData.proTranCode=='1  '){
										displayValue += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='/purchase/addPurchase?prodNo="+JSONData.prodNo+"'>"+JSONData.prodName+" 구매하기</a>"
									}
									displayValue += "</h3>"
									$("#"+prodNo+"").html(displayValue);
								},
								error : function(jqXHR){
									alert("url:"+"/product/json/getProduct/"+prodNo)
									alert("error:"+jqXHR.status);
								}
							}
						)
					}
				}
				
			})
		
			 $('input[name="searchKeyword"]').on("keyup", function(){
				//alert("success");
				var searchKeyword = $(this).val();
				var searchCondition = $('select[name=searchCondition]').val();
				//console.log("searchKeyword:"+searchKeyword+"\nsearchCondition:"+searchCondition);
				$.ajax( 
						{
							url : "/product/json/autoCompleteUser",
							method : "POST" ,
							data:JSON.stringify({
								"searchKeyword" : searchKeyword, 
								"searchCondition" : searchCondition		
							}),
							dataType : "text" ,
							headers : {
								"Accept" : "application/json",
								"Content-Type" : "application/json"
							},
							success : function(JSONData , status) {

								//Debug...
								//alert(status);
								//Debug...
								//alert("JSONData : \n"+JSONData);
								//alert("list : \n"+JSON.stringify(JSONData))
								var jsonarr = $.parseJSON(JSONData);
								//alert("jsonarr:\n"+jsonarr)
								//var autoC = jsonarr;
								//alert("autoC : \n"+autoC);
								
								$('input[name="searchKeyword"]').autocomplete({source : jsonarr});
							}
					}
				)})
				
			$(".ct_list_pop:nth-child(4n+6)" ).css("background-color" , "whitesmoke");
			$("h7").css("color" , "green");
			
			$('#deliver').on("click", function(){
				var prodNo = $($(this).next('span').html()).val();
				console.log(prodNo);
				location="/purchase/updateTranCodeByProd?prodNo="+prodNo+"&tranCode=3"
			})
		})
	</script>
	</head>
	
	<body bgcolor="#ffffff" text="#000000">
	
	<div style="width:98%; margin-left:10px;">
	
	<form>
	
	<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
		<tr>
			<td width="15" height="37">
				<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
			</td>
			<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="93%" class="ct_ttl01">
						 <c:if test="${!empty param.menu && param.menu=='manage'}">
						 	상품관리
						 </c:if>
						 <c:if test="${!empty param.menu && param.menu=='search'}">
						 	상품 목록 조회
						 </c:if>
						</td>
					</tr>
				</table>
			</td>
			<td width="12" height="37">
				<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
			</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
		<tr>
			<td align="right">
					(상품 가격 검색? <input type="checkbox" name="priceSearch" ${search.priceSearch ? "checked" : ""}> 
			<input 	type="text" name="searchKeyword1" value="${!empty search.searchKeyword1 ? search.searchKeyword1 : ""}" 
			
								class="ct_input_g" style="width:100px; height:19px" >
								원 ~
			<input 	type="text" name="searchKeyword2" value="${!empty search.searchKeyword2 ? search.searchKeyword2 : ""}" 
			
								class="ct_input_g" style="width:100px; height:19px" >원
								)&nbsp;
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="0" ${!empty search.searchCondition && search.searchCondition=='0' ? "selected" : ""}>상품번호</option>
				<option value="1" ${!empty search.searchCondition && search.searchCondition=='1' ? "selected" : ""}>상품명</option>
				<%--<option value="2" ${!empty search.searchCondition && search.searchCondition=='2' ? "selected" : ""}>상품가격</option> --%>
				<option value="3" ${!empty search.searchCondition && search.searchCondition=='3'? "selected" : ""}>판매중</option>
				<c:if test="${user.role=='admin'}">
				<option value="4" ${!empty search.searchCondition && search.searchCondition=='4'? "selected" : ""}>구매완료</option>
				<option value="5" ${!empty search.searchCondition && search.searchCondition=='5'? "selected" : ""}>배송중</option>
				<option value="6" ${!empty search.searchCondition && search.searchCondition=='6'? "selected" : ""}>배송완료</option>
				</c:if>
			</select>
			<input 	type="text" name="searchKeyword" value="${!empty search.searchKeyword ? search.searchKeyword : ""}" 
			
								class="ct_input_g" style="width:200px; height:19px" >
	
		</td>
			<td align="right" width="70">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="17" height="23">
							<img src="/images/ct_btnbg01.gif" width="17" height="23">
						</td>
						<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
							검색
						</td>
						<td width="14" height="23">
							<img src="/images/ct_btnbg03.gif" width="14" height="23">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
	
		
	<td align="right">
			&nbsp;&nbsp;&nbsp;&nbsp;(정렬기준
			<input type=radio name="sorting" value="prodNo" ${empty search.sorting || (!empty search.sorting && search.sorting=='prodNo') ? "checked" : ""}>상품번호
			<input type=radio name="sorting" value="priceASC" ${!empty search.sorting && search.sorting=='priceASC' ? "checked" : ""}>가격낮은순
			<input type=radio name="sorting" value="priceDESC" ${!empty search.sorting && search.sorting=='priceDESC' ? "checked" : ""}>가격높은순
			)</td>
		</tr>
	</table>
	
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
		<tr>
			<td colspan="11" >전체  ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage } 페이지</td>
		</tr>
		<tr>
			<td class="ct_list_b" width="100">No</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b" width="150">상품명
			<h7>(클릭시상세보기)</h7></td>
			<td class="ct_line02"></td>
			<td class="ct_list_b" width="150">가격</td>
			<td class="ct_line02"></td>
			<td class="ct_list_b">상품 번호</td>	
			<td class="ct_line02"></td>
			<td class="ct_list_b">현재상태</td>
			<td class="ct_line02"></td>	
			<td class="ct_list_b">상품 이미지</td>
		</tr>
		<tr>
			<td colspan="11" bgcolor="808285" height="1"></td>
		</tr>
	<c:set var="i" value="0" />
	<c:forEach items="${list}" var="product">
		<tr class="ct_list_pop">
			<td align="center">${i+1}</td>
	<c:set var="i" value="${i+1}"/>
			<td></td>
					<td align="left"><h7>${product.prodName}</h7></td>
			
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<td align="left">${product.prodNo}</td>
			<td></td>
			<td align="left" id="tranStaaus">
				<c:choose>
					<c:when test="${product.proTranCode=='1  '}">
					판매중
					</c:when>
					<c:when test="${product.proTranCode=='2  '&& user.role=='admin'}">
					구매 완료
						<c:if test="${param.menu=='manage'}">
							<span id="deliver">배송하기</span>
							<span><input type="hidden" value=${product.prodNo}></span>
						</c:if>
					</c:when>
					<c:when test="${product.proTranCode=='3  '&& user.role=='admin'}">
					배송중
					</c:when>
					<c:when test="${product.proTranCode=='4  '&& user.role=='admin'}">
					배송 완료
					</c:when>
					<c:otherwise>
					재고 없음
					</c:otherwise>
				</c:choose>
			</td>	
			<td></td>
			<td align="center">
				<c:if test="${!empty product.fileName}">
					<p><img src="../images/uploadFiles/${product.fileName}" width="100" height="100"/></p><%--${product.fileName} --%>
				</c:if>
				<c:if test="${empty product.fileName}">
					<p><img src="http://placehold.it/100x100"/> </p><%--${product.fileName} --%>
				</c:if>
			</td>
		</tr>
		<tr>
			<td id="${product.prodNo}" colspan="11" height="1"></td>
		</tr>	
	</c:forEach>
	
	</table>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
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
