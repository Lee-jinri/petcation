<!--메인페이지에 들어갈 내용  -->  
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/common.jspf" %>
<tiles:insertDefinition name="intro" />
<style>
   #ad{
      width:1260px;
      height:317px;
   }
   .danate{
      border-radius:50%;
   }
   @keyframes heartBeat {
	  0% {
	    color: #fff;
	    transform: none;
	  }
	  50% {
	    color: #fff;
	    transform: scale(1.1);
	  }
	  100% {
	    color: #fff;
	    transform: none;
	  }
	}
	.mini-box{
		width: 400px;
		height: 317px;
		border-radius: 20px;
		background:rgba(246,140,31,0.7);
		justify-content:center;
		align-items:center;
		text-align:center;
		color:#fff;
		font-size:25px;
		font-weight:800;
		font-family: SCDream5;
		float:right;
		margin-left:20px;
	}
	.weather-icon {
  		animation: heartBeat 1.3s linear infinite;
	}
	.radius {
		border-radius : 30px;
	}
	.main-text{
		font-size : 25px;
	}
	.morehotel{
		animation:heartBeat 1.3s linear infinite;
	}
	.productbox:hover {
	    cursor: pointer;
	    background-color: #f7f7f7;
	}
</style>

<script>
   $(function() {
	   var url = window.location.pathname;
	   var arr = url.split('/');
/*       $("#logoutBtn").click(function() {
         //alert("로그아웃");         
         $.ajax({
               type:"post",
               url:"/member/logout",
               success:function(data){
                   //alert("로그아웃 성공");
                   document.location.reload();     
               } 
           }); // ajax 
      })//logoutBtn */
      
      $(".goNDetail").click(function(){
    	  let n_no =$(this).parents("tr").attr("data-num");
    	  $("#n_no").val(n_no);
    	  
    	  $("#noticeDetail").attr({
    		  "method":"get",
    	  	  "action":"/notice/noticeDetail"
    	  });
    	  $("#noticeDetail").submit();
      })
      
      $(".goDetail").click(function(){
    	  let d_no =$(this).parents("tr").attr("data-num");
    	  $("#d_no").val(d_no);
    	  console.log("글번호 :"+d_no);
    	  
    	  $("#detailForm").attr({
    		  "method":"get",
    	  	  "action":"/diary/diaryBoardDetail"
    	  });
    	  $("#detailForm").submit();
      })//goDetail
      
      $(".goCDetail").click(function(){
			let b_no = $(this).parents("tr").attr("data-num");
			let c_no = $(this).parents("tr").attr("data-category");
			$("#b_no").val(b_no);
			$("#c_no").val(c_no)
			//상세 페이지로 이동하기 위해 form 추가 (id : detailForm)
			$("#f_data").attr({
				"method":"get",
				"action":"/community/communityBoardDetail/"+c_no
			});
			$("#f_data").submit();
      })
      
      $(".productbox").click(function(){
			let hotelNo = $(this).attr("data-no");
			location.href = "/hotel/hotelDetail?hotel_no=" + hotelNo;
      })
   });
</script>
<body>
<header class="item header margin-top-0">
 <div class="container">
      <div class="row">
         <div class="col-md-12 text-center">
            <div class="text-homeimage">
               <div class="maintext-image" data-scrollreveal="enter top over 1.5s after 0.1s" style="-webkit-transform: translatey(0);-moz-transform: translatey(0);transform: translatey(0);opacity: 1;-webkit-transition: all 1.5s ease-in-out 0.1s;-moz-transition: all 1.5s ease-in-out 0.1s;-o-transition: all 1.5s ease-in-out 0.1s;transition: all 1.5s ease-in-out 0.1s;-webkit-perspective: 1000;-webkit-backface-visibility: hidden;" data-scrollreveal-initialized="true">
                   PETCATION
               </div>
            </div>
         </div>
      </div>
   </div>
</header>
<div class="item content"> 
   <div class="container" style="display: flex; justify-content: center; align-items: center;"> <!-- style="display: flex; justify-content: center; align-items: center;" --> 
   		 
      <a href="http://www.pawinhand.kr/main/html.php?htmid=service/sponsorship.html">
      	<img class="radius" id="ad" src="/resources/include/scorilo/images/ad.png"/>
      </a>
      	<section class="mini-box">
   		 	<div class="weather-text">
            	<span></span>
            </div>
            <div class="weather-temp">
            	<span></span>
            </div>
            <div class="weather-icon">
            	<img alt="">
            </div>
            <span class="weather-main"></span>
    	</section>
   </div>
   
</div>

<!-- STEPS =============================-->
<!-- AREA =============================-->
<!-- LATEST ITEMS =============================-->
<section class="item content">
   <div class="container">
      <div class="underlined-title">
         <div class="editContent">
            <h1 class="text-center latestitems">HOT 숙소</h1>
         </div>
         <div class="wow-hr type_short">
            <span class="wow-hr-h">
            <i class="fa fa-star"></i>
            <i class="fa fa-star"></i>
            <i class="fa fa-star"></i>
            </span>
         </div>
      </div>
      <div class="row">
      	<c:choose>
      		<c:when test="${not empty mainHotelList}">
		     	<c:forEach items="${mainHotelList }" var="hotel" varStatus="status">
			         <div class="col-md-4">
			            <div class="productbox" data-no="${hotel.hotel_no }">
			               <div class="fadeshop">
			                  <span class="maxproduct"><img src="/petcationStorage/hotel/thumbnail/${hotel.hotel_thumb}" alt="" style="object-fit:cover; width:650px; height:143px;"></span>
			               </div>
			               <div class="product-details">
			                  <h1>${hotel.hotel_name }</h1>
			
			                  <span class="price">
			                  <span class="edd_price">${hotel.hotel_price}</span>
			                  </span>
			               </div>
			            </div>
			         </div>
			         <!-- /.productbox -->
		         </c:forEach>
	         </c:when>
         </c:choose>
      </div>
   </div>

</section>


<!-- BUTTON =============================-->
<div class="item content">
   <div class="container text-center">
      <a href="/hotel/hotelList" class="homebrowseitems radius morehotel" style="color:#fff;">더 많은 숙소 보러가기
      </a>
   </div>
</div>
<br/>

<!-- AREA =============================-->
<!-- AREA =============================-->
<div class="item content">
   <div class="container">
   	  <form id="detailForm">
	      <input type="hidden" id="d_no" name="d_no"/>
	  </form>
	  <form name="f_data" id="f_data" method="post">
		 <input type="hidden" id="b_no" name="b_no"/>
	  </form>
	  <form id="noticeDetail" method="post">
	  	<input type="hidden" id="n_no" name="n_no"/>
	  </form>
      <div class="row">
         <div class="col-md-4">
            <i class="fa fa-bullhorn infoareaicon"></i>
            <div class="infoareawrap">
               <div class="content_title text-center main-text">
                  공지사항
               </div>
               <div id="content_body">
                   <table>
                  	<c:choose>
                  		<c:when test="${not empty mainNoticeList}">
                  			<c:forEach var="notice" items="${mainNoticeList}" varStatus="status" begin="0" end="5">
                  				<tr class="info" data-num="${notice.n_no}">
                        			<td class="goNDetail" style="cursor:pointer;">${notice.n_title}</td>
                     			</tr>
                  			</c:forEach>
                  		</c:when>
                  		<c:otherwise>
                  			<tr>
                  				<td>등록된 게시물이 존재하지 않습니다.</td>
                  			</tr> 
                  		</c:otherwise>
                  	</c:choose>
                  </table>
               </div>
            </div>
         </div>
         <div class="col-md-4 radius">
            <i class="fa fa-microphone infoareaicon"></i>
            <div class="infoareawrap">
               <div class="content_title text-center main-text">
                  커뮤니티
               </div>
               <div id="content_body">
                  <table>
                  	<c:choose>
                  		<c:when test="${not empty mainCommunityList}">
                  			<c:forEach var="community" items="${mainCommunityList}" varStatus="status" begin="0" end="5">
                  				<tr class="info" data-num="${community.b_no}" data-category="${community.c_no}">
                        			<td class="goCDetail" style="cursor:pointer;">${community.b_title}</td>
                     			</tr>
                  			</c:forEach>
                  		</c:when>
                  		<c:otherwise>
                  			<tr>
                  				<td>등록된 게시물이 존재하지 않습니다.</td>
                  			</tr> 
                  		</c:otherwise>
                  	</c:choose>
                  </table>
               </div>
            </div>
         </div>
         
         <div class="col-md-4">
            <i class="fa fa-comments infoareaicon"></i>
            <div class="infoareawrap">
               <div class="content_title text-center main-text">
                  다이어리
               </div>
               <div id="content_body">
                  <table>
                  	<c:choose>
                  		<c:when test="${not empty mainDiaryList}">
                  			<c:forEach var="diary" items="${mainDiaryList}" varStatus="status">
                  				<tr data-num="${diary.d_no}">
                        			<td class="goDetail" style="cursor:pointer;">${diary.d_title}</td>
                     			</tr>
                  			</c:forEach>
                  		</c:when>
                  		<c:otherwise>
                  			<tr>
                  				<td>등록된 게시물이 존재하지 않습니다.</td>
                  			</tr> 
                  		</c:otherwise>
                  	</c:choose>
                  </table>
               </div>
            </div>
         </div>
      
      </div>
   </div>
</div>



<!-- TESTIMONIAL =============================-->

<!-- SCRIPTS =============================-->
<script src="/resources/include/scorilo/js/jquery-.js"></script>
<script src="/resources/include/scorilo/js/bootstrap.min.js"></script>
<script src="/resources/include/scorilo/js/anim.js"></script>
<script src="/resources/include/js/weather.js"></script>
<script>
//----HOVER CAPTION---//     
jQuery(document).ready(function ($) {
   $('.fadeshop').hover(
      function(){
         $(this).find('.captionshop').fadeIn(150);
      },
      function(){
         $(this).find('.captionshop').fadeOut(150);
      }
   );
});
</script>
</body>
</html>
<tiles:insertDefinition name="outro" />