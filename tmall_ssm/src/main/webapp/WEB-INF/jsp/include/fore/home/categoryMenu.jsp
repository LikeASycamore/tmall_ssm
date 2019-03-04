<!-- 分类菜单 
     树状分类菜单导航，滑动导航，右边显示分类对对应的产品

-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<div class="categoryMenu">
		<c:forEach items="${cs}" var="c">
			<div cid="${c.id}" class="eachCategory">
				<span class="glyphicon glyphicon-link"></span>
				<a href="forecategory?cid=${c.id}">
					${c.name}
				</a>
			</div>
		</c:forEach>
	</div>  