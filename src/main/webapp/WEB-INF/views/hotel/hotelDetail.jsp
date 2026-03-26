<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/common.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
   <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
   <title></title>
   

   <link rel="shortcut icon" href="/resources/image/icon.png"/>
   <link rel="apple-touch-icon" href="/resources/image/icon.png"/>
   
   <link rel="stylesheet" type="text/css" href="/resources/include/scorilo/css/bootstrap.min.css"/>
   <link rel="stylesheet" type="text/css" href="/resources/include/scorilo/css/bootstrap-theme.min.css"/>
   <link rel="stylesheet" type="text/css" href="/resources/include/scorilo/css/style.css" >
<!--    <link rel="stylesheet" type="text/css" href="/resources/include/scorilo/css/justified-nav.css"/> -->
   <link href="https://fonts.googleapis.com/css?family=Dosis:200,300,400,500,600,700" rel="stylesheet">
   <link href="https://fonts.googleapis.com/css?family=Roboto:200,300,400,500,600,700" rel="stylesheet">
   <script type="text/javascript" src="/resources/include/js/jquery-1.12.4.min.js"></script>
   <script type="text/javascript" src="/resources/include/js/common.js"></script>
   <script type="text/javascript" src="/resources/include/scorilo/js/bootstrap.min.js"></script>
   <script type="text/javascript" src="/resources/include/scorilo/js/anim.js"></script>
   <style>
      #reservBtn {
           background: url( "/resources/images/common/reservbtn.png" ) no-repeat;
           border: none;
           width: 100px;
           height: 100px;
           background-size: contain;
           cursor: pointer;
           line-height:200%;
           color:black;
           margin-bottom:30px;
         }
         #hotelListBtn{
            background: url( "/resources/images/common/listbtn.png" ) no-repeat;
           border: none;
           width: 55px;
           height: 55px;
           background-size: contain;
           cursor: pointer;
           line-height:200%;
           color:black;
           margin-bottom:20px;
         }
         #reservBtn,#hotelListBtn:focus { outline: none; }	
         .hotelThumb{
         	text-align: center;
         }      
   </style>
   <script type="text/javascript">
               $(function(){
                  $("#hotelListBtn").click(function(){
                     location.href = "/hotel/hotelList";
                  });
                  
                  $("#reservBtn").click(function(){
                     $("#f_updateForm").attr({
                        "method" : "get",
                        "action" : "/reserv/reservForm"
                     });
                     $("#f_updateForm").submit();
                  });
                  
            
               });
               
               
   </script>
</head>
<body>

<!-- HEADER =============================-->
<header class="item header margin-top-0">
<div class="wrapper">
   <div class="container">
      <div class="row">
         <div class="col-md-12 text-center">
            <div class="text-pageheader">
               <div class="subtext-image" data-scrollreveal="enter bottom over 1.7s after 0.0s">
                   Hotels
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
         <h1 class="text-center latestitems">숙소 상세보기</h1>
      </div>
      <div class="wow-hr type_short">
         <span class="wow-hr-h">
         <i class="fa fa-star"></i>
         <i class="fa fa-star"></i>
         <i class="fa fa-star"></i>
         </span>
      </div>
   </div>
 </div>
<!-- NAVBAR=============================== -->
   
        
 <!-- CONTENT============================== --> 
 <section class="item content">
<div class="container toparea">
   <div class="underlined-title">

   </div>
<!-- NAVBAR=============================== -->
   
        
 <!-- CONTENT============================== -->       
         <div class="contentContainer container">
            <div class="contentTit page-header"><h3 class="text-center">${detail.hotel_name}</h3></div>
               <div class="hotelThumb fadeshop">
                  <span class="maxproduct"><img src="/petcationStorage/hotel/thumbnail/${detail.hotel_thumb}" alt="" style="object-fit:cover; width:650px; height:143px;"></span>
               </div>
               <div class="contentTB text-center">
                  <div class="fontchange">
                     <div class="text-right">
                        <input type="button" class="btn btn-info" value="" id="hotelListBtn" />
                     </div>
                  <table class="table table-bordered">
                     <tbody>
                        <tr>
                           <td class="col-md-1 text-center" >숙소명</td>
                           <td class="col-md-2 text-left">${detail.hotel_name}</td>
                           <td class="col-md-1">가격</td>
                           <td class="col-md-2 text-right">${detail.hotel_price } 원 / 일</td>
                        </tr>
                        <tr>
                           <td class="col-md-1">숙소주소</td>
                           <td colspan="4" class="col-md-8 text-left">${detail.hotel_address }</td>
                        </tr>
                        <tr>
                           <td class="col-md-1">전화번호</td>
                           <td colspan="4" class="col-md-8 text-left">${detail.hotel_tel }</td>
                        </tr>
                        <tr class="table-tr-height">
                           <td class="col-md-1">소개글</td>
                           <td colspan="4" class="col-md-8 text-left">${detail.hotel_info }</td>
                        </tr>
                        
                     </tbody>
                  </table>
                  </div>
               </div>
            </div>
            <div class="text-center"><input type="button" class="btn btn-info" id="reservBtn"/></div>
         </div>
</section>

   <form id="f_updateForm" name="f_updateForm">
      <input type="hidden" id="hotel_no" name="hotel_no" value="${detail.hotel_no }" />
   </form>

<!-- SCRIPTS =============================-->
<script src="/resources/include/scorilo/js/jquery-.js"></script>
<script src="/resources/include/scorilo/js/bootstrap.min.js"></script>
<script src="/resources/include/scorilo/js/anim.js"></script>

</body>
</html>