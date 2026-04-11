<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<script src ="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js" type="text/javascript"></script>
	<!-- Moment Js -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
	
	<!-- SCRIPTS =============================-->
	<script src="/resources/include/scorilo/js/jquery-.js"></script>
	<script src="/resources/include/scorilo/js/bootstrap.min.js"></script>
	<script src="/resources/include/scorilo/js/anim.js"></script>
	<style>
		#reservResult {color : #58ACFA;}
		#reservCancel {color : #210B61;}
		#reservList .table > tbody > tr > td {
		    vertical-align: middle;
		}
		.table-striped td {
		    height: 50px;
		    vertical-align: middle;
		    padding: 10px 5px;
		    white-space: nowrap;
		    overflow: hidden;
		    text-overflow: ellipsis;
		}
		.reservCancel {
		    background-color: #4a4a4a;
		    color: white;
		    border: none;
		    padding: 7px 14px;
		    border-radius: 20px;
		    font-size: 12px;
		    letter-spacing: -0.5px;
		}

		.reservCancel:hover {
		    background-color: #222;
		    box-shadow: 0 2px 5px rgba(0,0,0,0.2);
		}
		.cancel-notice {
		    background-color: #fffdf0;
		    border: 1px solid #f1e4a1;
		    padding: 12px 18px;
		    margin: 20px 0;
		    border-radius: 8px;
		    display: inline-block;
		}
		
		.cancel-notice p {
		    margin: 0;
		    color: #856404;
		}
	</style>
	<script type="text/javascript">
	
		$(function(){
			let cancel = "";
			let hiddenDate = "";
			const today = moment().format('YYYY-MM-DD');
			
			$(".reservCancel").click(function(){
				cancel = $(this).parents("tr").attr("cancel"); 
				
				const cancel_date = moment(today).diff(cancel,'days');
				
				if(parseInt(cancel_date) < 0){
					let result = Math.abs(parseInt(cancel_date));
					if( result < 8){
					alert("예약 취소는 체크인 날짜 7일 전까지만 가능합니다.");
					return;
					}
				}
				
				let check = confirm("예약 취소 하시겠습니까?");
				if (check == false) return false;
				
				const orderId = $(this).parents("tr").attr("data-num");
				
				if (!orderId) { 
				    alert("시스템 오류 입니다. 잠시 후 다시 시도하세요.");
				    return;
				}
				const $btn = $(this);
			    $btn.prop("disabled", true);
				$btn.text("처리 중...");
				
				cancelReservation(orderId, $btn);
			})
			
			$(".paginate_button a").click(function(e){
                e.preventDefault();
                $("#m_search").find("input[name='pageNum']").val($(this).attr("href"));
                goPage();
             });
		})
		
		async function cancelReservation(orderId, $btn){
			try {
				const response = await fetch('/payments/' + orderId + '/cancel', {
					method: 'PATCH',
					headers: {
					'Content-Type': 'application/json',
				  },
				});

				if (response.ok) {
					alert("예약이 취소 되었습니다.")
					location.reload();
				} else {
					console.error("실패:", response.status);
					
		            const errorData = await response.json();
		            const errorMsg = errorData.message || "시스템 오류입니다. 잠시 후 다시 시도하세요.";
		            alert(errorMsg);
		            
		            $btn.prop("disabled", false);
		            $btn.text("예약 취소");
				}
			} catch (error) {
				console.error("에러 발생:", error);
				alert("시스템 오류입니다. 잠시 후 다시 시도하세요.");
				$btn.prop("disabled", false);
				$btn.text("예약 취소");
			}
		}
		
		 function goPage(){
               $("#page").attr({
                  "method":"get",
                  "action":"/mypage/reservList"
               });
               $("#page").submit();
            }
		
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
						 MY PAGE
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</header>


<!-- CONTENT =============================-->
<section class="item content">
<div class="container toparea">
	<div class="underlined-title">
		<div class="editContent">
			<h1 class="text-center latestitems">예약 내역</h1>
		</div>
		<div class="wow-hr type_short">
			<span class="wow-hr-h">
			<i class="fa fa-star"></i>
			<i class="fa fa-star"></i>
			<i class="fa fa-star"></i>
			</span>
		</div>
	</div>
<!-- NAVBAR=============================== -->
	<nav>
          <ul class="nav nav-tabs nav-justified">
            <li><a href="/mypage/main">나의 정보</a></li>
            <li class="active"><a href="/mypage/reservList">예약내역</a></li>
            <li><a href="/mypage/boardList">작성한 글</a></li>
          </ul>
    </nav>

 <!-- CONTENT============================== -->
 				<div class="cancel-notice">
 					<p>✔️예약 취소는 체크인 7일 전까지 가능합니다.</p>
 				</div>       
			 	<div id = "reservList" class = "table-hegiht">
			 		<form id="reservCancel_no">
			 			<input type="hidden" id="reserv_no" name="reserv_no">
			 		</form>
			 		<form id="page">
			 			<input type="hidden" name="pageNum" value="${pageMaker.cvo.pageNum}">
						<input type="hidden" name="amount" value="${pageMaker.cvo.amount}">
			 		</form>
				<table summary="예약내역" class="table table-striped">
					<thead>
						<tr class="text-center">
							<th class="text-center">숙소</th>
							<th data-value="b_regdate" class="text-center">숙소 가격</th>
							<th class="text-center"> 체크인 </th>
							<th class="text-center"> 체크아웃 </th>
						</tr>
					</thead>
					<tbody id = "list" class="table-striped">
						<!-- 데이터 출력 -->
						<c:choose>
							<c:when test="${ not empty reservList }">
								<c:forEach var="reserv" items="${ reservList }" varStatus="status">
									<tr class="text-center" data-num="${ reserv.orderId }" cancel="${reserv.checkin }">
										<td class="goDetail text-center"> ${ reserv.hotel_name } </td>
										<td class="text-center">${ reserv.reserv_price }원</td>
										<td class="text-center"> ${reserv.checkin }</td>
                              			<td class="text-center"> ${reserv.checkout }</td>
     									<td>
     										<c:if test="${reserv.reserv_status == 'CANCELED' }">
     											취소 완료
     										</c:if>
     										<c:if test="${reserv.canCancel}">
										        <button type="button" class="reservCancel">예약 취소</button>
										    </c:if>
     									</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5" class="tac text-center">예약 내역이 존재하지 않습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<div class="text-center">
					<ul class="pagination">
						<!-- 이전 바로가기 10개 존재 여부를 prev 필드의 값으로 확인. -->
						<c:if test="${pageMaker.prev}">
							<li class="paginate_button previous">
								<a href="${pageMaker.startPage -1}">이전</a>
							</li>
						</c:if>
						
						<!-- 바로가기 번호 출력 -->
						<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
							<li class="paginate_button ${pageMaker.cvo.pageNum == num? 'active':'' }">
								<a href="${num}">${num}</a>
							</li>
						</c:forEach>
						
						<!-- 다음 바로가기 10개 존재 여부를 next 필드의 값으로 확인. -->
						<c:if test="${pageMaker.next }">
							<li class="paginate_button next">
								<a href="${pageMaker.endPage + 1 }">다음</a>
							</li>
						</c:if>
					</ul>
				</div>
	</div>			
</section>



</body>
</html>