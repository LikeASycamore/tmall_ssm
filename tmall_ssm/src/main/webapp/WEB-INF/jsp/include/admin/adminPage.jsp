<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

	
<script>
$(function(){
	$("ul.pagination li.disabled a").click(function(){
		return false;
	});
});

</script>


<nav>
  <ul class="pagination">
  	<!-- 首页 -->
    <li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
      <a  href="?start=0${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>

	<!-- 上一页 -->
    <li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
      <a  href="?start=${page.start-page.count}${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&lsaquo;</span>
      </a>
    </li>    

	<!-- 循环遍历 显示页数  -->
    <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
    
    	
		    <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
		    	<a  
		    	href="?start=${status.index*page.count}${page.param}"
		    	<c:if test="${status.index*page.count==page.start}">class="current"</c:if>
		    	>${status.count}</a>
		    </li>
		
    </c:forEach>

	<!-- 下一页 -->
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.start+page.count}${page.param}" aria-label="Next">
        <span aria-hidden="true">&rsaquo;</span>
      </a>
    </li>
    <!-- 最后一页 -->
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.last}${page.param}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
