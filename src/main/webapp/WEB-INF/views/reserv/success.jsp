<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jspf" %>

<!DOCTYPE html>
<html>
	<head>

		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="generator" content="">
		<link href="/resources/include/scorilo/css/bootstrap.min.css" rel="stylesheet">
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
		<link href="/resources/include/scorilo/css/style.css" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Dosis:200,300,400,500,600,700" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Roboto:200,300,400,500,600,700" rel="stylesheet">
		<link rel="shortcut icon" href="/resources/images/common/dogpaw.svg" />
		<link rel="apple-touch-icon" href="/resources/images/common/dogpaw.svg" />
		

		<style>
			.main 	{margin-top : 50px; margin-bottom:50px;}
			.table 	{width:50%; margin-left:auto;margin-right:auto;}
			.m		{margin-bottom : 30px; }
		</style>		
		
		<script type="text/javascript">
			$(function(){
				$("#main").click(function(){
					location.href = "/";
				})
			})
		</script>
		
	</head>
	<body>
		<header class="item header margin-top-0">
         <div class="wrapper">
            <div class="container">
               <div class="row">
                  <div class="col-md-12 text-center">
                     <div class="text-pageheader">
                           <div class="subtext-image" data-scrollreveal="enter bottom over 1.7s after 0.0s">
                            RESERVATION
                           </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </header>
      <!-- CONTENT =============================-->
			<div class="container toparea">
		   		<div class="underlined-title">
		      	<div class="editContent">
		         <h1 class="text-center latestitems">숙소 예약</h1>
		      </div>
		      <div class="wow-hr type_short">
		         <span class="wow-hr-h">
		         <i class="fa fa-star"></i>
		         <i class="fa fa-star"></i>
		         <i class="fa fa-star"></i>
		         </span>
		      </div>
		   </div>
		
			<!--========= 결제 완료 페이지 ============== -->
			<div class="main text-center">
				<div class="title">
					<h1>예약이 완료 되었습니다.</h1>
				</div>
				
				<table class="table table-bordered ">
					<tr class="warning">
						<th>숙소</th>
						<td>${result.hotel_name }</td>
					</tr>
					<tr class="warning">
						<th>Check In</th>
						<td>${result.checkin }</td>
					</tr>
					<tr class="warning">
						<th>Check Out</th>
						<td>${result.checkout }</td>
					</tr>
					<tr class="warning"> 
						<th>예약자명</th>
						<td>${result.reserv_name }</td>
					</tr>
					<tr class="warning">
						<th>예약자 전화번호</th>
						<td>${result.reserv_phone }</td>
					</tr>
				</table>
				
				<input type="button" id="main" value="메인 페이지로 이동">
			</div>
		</div>
		
	</body>
</html>



