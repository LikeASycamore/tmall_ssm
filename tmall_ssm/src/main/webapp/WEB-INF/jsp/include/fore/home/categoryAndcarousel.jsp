<!-- 首页竖状分类菜单右侧的推荐产品列表，在这里是前四个分类 平板电视、马桶、沙发、天猫国际
            本页面实现了函数，具体排版在在其他页面
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<script>
//显示某个分类右边含小标题的div，同时改变该区域的颜色
function showProductsAsideCategorys(cid){
	$("div.eachCategory[cid="+cid+"]").css("background-color","white");
	$("div.eachCategory[cid="+cid+"] a").css("color","#87CEFA");
	$("div.productsAsideCategorys[cid="+cid+"]").show();
}

//隐藏某个分类右边产品，同时改变该区域的颜色
function hideProductsAsideCategorys(cid){
	$("div.eachCategory[cid="+cid+"]").css("background-color","#e2e2e3");
	$("div.eachCategory[cid="+cid+"] a").css("color","#000");
	$("div.productsAsideCategorys[cid="+cid+"]").hide();
}

//鼠标进入分类导航栏时，显示产品；离开则隐藏
$(function(){
    $("div.eachCategory").mouseenter(function(){
        var cid = $(this).attr("cid");
        showProductsAsideCategorys(cid);
    });
    $("div.eachCategory").mouseleave(function(){
        var cid = $(this).attr("cid");
        hideProductsAsideCategorys(cid);
    });
    $("div.productsAsideCategorys").mouseenter(function(){
    	var cid = $(this).attr("cid");
    	showProductsAsideCategorys(cid);
    });
    $("div.productsAsideCategorys").mouseleave(function(){
    	var cid = $(this).attr("cid");
    	hideProductsAsideCategorys(cid);
    });
  	//天猫超市 天猫国际右边的菜单栏，猫脑袋显示
	$("div.rightMenu span").mouseenter(function(){
		var left = $(this).position().left; 
		var top = $(this).position().top;
		var width = $(this).css("width");
		var destLeft = parseInt(left) + parseInt(width)/2;
		//设置猫出现的位置  左边从span的中间开始，向上移动20
		$("img#catear").css("left",destLeft);
		$("img#catear").css("top",top-20);
		$("img#catear").fadeIn(500);
				
	});
	//天猫超市 天猫国际右边的菜单栏，猫脑袋隐藏
	$("div.rightMenu span").mouseleave(function(){
		$("img#catear").hide();
	});
	
	var left = $("div#carousel-of-product").offset().left;
	$("div.categoryMenu").css("left",left-20);
	$("div.categoryWithCarousel div.head").css("margin-left",left);
	$("div.productsAsideCategorys").css("left",left-20);
	
	
});
</script>

<img src="img/site/catear.png" id="catear" class="catear"/>
	
<div class="categoryWithCarousel">


<div class="headbar show1">
	<div class="head ">
	
		<span style="margin-left:10px" class="glyphicon glyphicon-th-list"></span>
		<span style="margin-left:10px" >商品分类</span>
		
	</div>
	
	<div class="rightMenu">
		<span><a href=""><img src="img/site/chaoshi.png"/></a></span>
		<span><a href=""><img src="img/site/guoji.png"/></a></span>

		<c:forEach items="${cs}" var="c" varStatus="st">
			<c:if test="${st.count<=4}">
				<span>
				<a href="forecategory?cid=${c.id}">
					${c.name}
				</a></span>			
			</c:if>
		</c:forEach>
	</div>
	
</div>

	
<div style="position: relative">
	<%@include file="categoryMenu.jsp" %>
</div>

<div style="position: relative;left: 0;top: 0;">
	<%@include file="productsAsideCategorys.jsp" %>
</div>



<%@include file="carousel.jsp" %>

<div class="carouselBackgroundDiv">
</div>

</div>





