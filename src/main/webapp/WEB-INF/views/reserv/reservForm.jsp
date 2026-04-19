<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jspf"%>

<script src="/resources/include/js/jquery-1.12.4.min.js"></script>
<!-- Moment Js -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
<!-- Lightpick CSS -->
<link rel="stylesheet"
	href="/resources/include/calendar/css/lightpick.css">
<!-- Lightpick JS -->
<script src="/resources/include/calendar/js/lightpick.js"></script>

<script src="https://js.tosspayments.com/v2/standard"></script>

<style>
	.container{margin-bottom: 30px;}
	#pay {
		background: url( "/resources/images/common/paybtn.png" ) no-repeat;
		border: none;
		width: 100px;
		height: 100px;
		background-size: contain;
		cursor: pointer;
		line-height: 200%;
		color: black;
		margin-bottom: 30px;
		justify-content: center;
		display: inline-block;
	}
	
	#personalTable {
		border: 1px solid #ddd;
		margin-left: auto;
		margin-right: auto;
		width: 1140px;
	}
	
	#personalTable>tbody>tr>td {
		padding: 10px;
	}
	
	#personalTable>tbody>tr>td>input {
		width: 200px;
	}
	
	#agreeDiv {
		margin-top: 30px;
	}
	
	.title, .calendar {
		margin-top: 40px;
		margin-bottom: 40px;
	}
	
	input {
		border: none;
	}
	
	.lightpick {
		z-index: 1;
	}
	
	.infoKey>th {
		width: 30%;
	}
	
	#user_date {
		width: 100%;
		text-align: center;
	}
	
	
	.reservation_Info, .personal_info {
		margin-top: 80px;
	}
	
	#payment-button {
	    padding: 8px 20px;
	    border: none;
	    border-radius: 6px;
	    font-size: 14px;
	    cursor: pointer;
	    transition: opacity 0.2s;
	    background: none; 
	    border: 2px solid #4A90E2; 
	    color: #4A90E2;
	}

	#payment-button:hover {
	    opacity: 0.7;
	    background-color: #4A90E2;
	    color: #fff;
	    transition: background-color 0.2s;
	}
	
	#payment-button:disabled {
	    opacity: 0.5;
	    cursor: not-allowed;
	}
</style>

	<script type="text/javascript">
         $(function(){
			let msg = "${errorMessage}";

			if (msg) {
				showPaymentModal(msg);
			}

            let startDate = "";
            let endDate = "";
            
            const hotel_no = ${hotelVO.hotel_no};
            let res_day = [];
            let widgets = null;
            
            listAll(hotel_no);
            
            const clientKey = "test_ck_DnyRpQWGrNzWdQ7KJqjLrKwv1M9E";
            const customerKey = "USER_" + "${lmember.user_no != null ? lmember.user_no : 'guest_1234'}";
            const tossPayments = TossPayments(clientKey);
            const payment = tossPayments.payment({ customerKey });
            
            function listAll(hotel_no){
                let url = "/reserv/reservDate/"+hotel_no;
                
                $.getJSON(url,function(data){
                   $(data).each(function(){
                      
                      let checkin = this.checkin;
                      let checkout = this.checkout;
                      
                      console.log(checkin + "/" + checkout);
                      
                      res_day.push([checkin,checkout]);
                   });
                   
                   pickerData(res_day);
                   
                }).fail(function(){
                   alert("예약일을 불러오는데 실패했습니다. 다시 시도해주세요.");
                })
             }
            
			
            // 달력
			function pickerData(res_day) {
	            var picker = new Lightpick({
	                field: document.getElementById('date'),
	                inline : true,
	                lang: 'ko',
	                singleDate: false,
	                minDate: moment().startOf('day'),
	                minDays : 2,
	                disableDates: getReservedDates(res_day),
	                format: 'YYYY-MM-DD',
	                onSelect: function(start, end){
	                    if(end == null) return;
	                    
	                    // 사용자가 선택한 날짜가 이미 예약된 날짜를 포함하는지 확인
	                    if(isDateInRange(start, end, res_day)){
	                    	alert("이미 예약 완료된 날짜입니다. 다시 선택해 주세요.");
	                    	// 선택된 날짜 초기화
	                        picker.setDate(null);
	                    	
	                    	return;
	                    }
	                    
	                    startDate = start.format('YYYY-MM-DD');
	                    endDate = end.format('YYYY-MM-DD');
	                    
	                    console.log("체크인 : "  + startDate);
	                    console.log("체크아웃 : " + endDate);
	                    
	                    $("#user_date").val(startDate + " ~ " + endDate); 
	                    $("#checkin").val(startDate); 
	                    $("#checkout").val(endDate);
	                    
	                    let days = moment(endDate).diff(startDate, 'days');
	                    let price = ${hotelVO.hotel_price} * days;
	                    
	                    $("#reserv_price").val(price);
	                    $("#price").val(price + "원");
	                    
	                    if($("#price").val() == NaN){
	                       "#price".type=hidden;
	                    }
	                    
	                    if(widgets) {
	                        widgets.setAmount({
	                            currency: "KRW",
	                            value: price
	                        });
	                    }
	                }
	            });
			}
			
			// 이미 예약 된 날짜 범위 지정
			function getReservedDates(res_day) {
			    if (res_day.length === 0) {
			        console.log("res_day is empty");
			        return [];
			    }
			    
			    var reservedDates = [];
			    res_day.forEach(function(dateRange) {
			    	
					var startDate = moment(dateRange[0]);
					var endDate = moment(dateRange[1]);

					// startDate가 endDate보다 이전이거나 같은 날짜인 경우 true
					while (startDate.isBefore(endDate) || startDate.isSame(endDate, 'day')) {
						reservedDates.push(moment(startDate));
						startDate.add(1, 'day'); // 하루씩 추가
					}
			       
			    });
			    
			    return reservedDates;
			}
            
			// start , end : 사용자가 예약하고자 하는 날
			function isDateInRange(start, end, res_day) {
			    if (res_day.length !== 0) {
			        let isInRange = false;
			
			        res_day.forEach(function (dateRange) {
			            // 이미 예약된 날짜 (startDate : 예약 시작일)
			            var startDate = moment(dateRange[0]);
			
			            // 사용자가 선택한 날짜가 이미 예약된 날을 포함하면 true
			            if (startDate.isBetween(start, end)) {
			                isInRange = true;
			            }
			        });
			
			        return isInRange;
			    } else {
			        return false;
			    }
			}
			
			$("#phone").on("input", function() {
				let val = $(this).val().replace(/\D/g, "");
				let len = val.length;
				let result = "";
				
				if (len < 4) {
					result = val;
				} else if (len < 7) {
					result = val.substring(0, 3) + "-" + val.substring(3);
				} else if (len < 11) {
					result = val.substring(0, 3) + "-" + val.substring(3, 6) + "-" + val.substring(6);
				} else {
					result = val.substring(0, 3) + "-" + val.substring(3, 7) + "-" + val.substring(7);
				}
				
				$(this).val(result);
			});
			
			
			/* 결제 버튼 클릭 */
            function validateReservation(){
               if(!chkData("#checkin","예약 일자를 ")) return false;
               if(!chkData("#checkout","예약 일자를" )) return false;
               if($("#checkout").val().toString() == "...") {
                  alert("예약 일자를 선택해 주세요.");
                  return false;
               }
               
            	// 이름 유효성 검사
               var name = $("#name").val();
               
               if(!chkData("#name","예약자명을 ")) return false;
               
               var n_RegExp = /^[가-힣]{2,15}$/;
               if(!n_RegExp.test(name)){            
                  alert("올바른 예약자명을 입력하세요. (특수문자,영어,숫자 사용 불가 , 한글로 2자 이상 입력)");
                  $("#name").focus();
                  $("#name").val("");  
                  return false;        
               }
               
               // 전화번호 유효성 검사
               var phone = $("#phone").val();
               
               if(!chkData("#phone","예약자 전화번호를 ")) return false;
               var p_RegExp = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}/;
               if(!p_RegExp.test(phone)){
                  alert("전화번호를 다시 입력해주세요. 010-1234-5678 또는 02-1234-5678의 형태로 입력하세요.");
                  $("#phone").focus();
                  $("#phone").val("");
                  return false;
               }
               
               // 예약 인원 유효성 검사
               var people = $("#people").val();
               
               if(!chkData("#people","예약 인원을 ")) return false;
               
               // 1~100
               var m_RegExp = /^(?:[1-9]|[1-9][0-9]|100)$/;
               
               if(!m_RegExp.test(people)){
                  alert("예약 인원을 잘못 입력하셨습니다.");
                  $("#people").focus();
                  $("#people").val("");
                  return false;
               }
               
               if(people > 500){
                  alert("예약 인원을 잘못 입력하셨습니다.");
                  $("#people").focus();
                  $("#people").val("");
                  return false;
               }
               
               // 이용동의 유효성 검사
               let isChecked = $("#agree").prop("checked"); 
               if(!isChecked){
                  alert("이용동의를 확인해주세요.");
                  return false;
               }
               
               const hotelName = document.getElementById("hotel_name").textContent;
               
				var result = confirm(
					"숙소 : " + hotelName + "\n" +
					"가격 : " + $("#price").val() + "\n" + 
					"예약 일자 : " + $("#checkin").val() + " ~ " + $("#checkout").val() + "\n" +
					"예약자명 : " + $("#name").val() + "\n" +
					"전화번호 : " + $("#phone").val() + "\n" +
					"예약 정보가 맞습니까?"
               );
               
				if (result) {
					return true;
				} else {
					return false;
				}
            };
            
            function getPaymentData() {
            	return {
            		name: $("#name").val(),
            		phone: $("#phone").val(),
            		price: $("#reserv_price").val(),
            		hotelName: '${hotelVO.hotel_name}',
            		people: $("#people").val(),
            		reserv_etc: $("#reserv_etc").val(),
            	};
            }
            
            $("#payment-button").on("click", function() {
                requestPayment();
            });
            
            async function requestPayment() {
                if (!validateReservation()) return;
				const data = getPaymentData();
				
				const response = await fetch("/payments/create", {
					method: "POST",
					body: JSON.stringify({
						'hotelNo': hotel_no,
						'hotelName': data.hotelName,
						'reserv_name': data.name,
						'reserv_phone': data.phone,
						'reserv_etc': data.reserv_etc,
						'reserv_people': data.people,
						'checkin': startDate,
						'checkout': endDate
					}),
					headers: {
						"Content-Type": "application/json"
					}
				});
					
				if (!response.ok) {
				    const err = await response.json();
				    alert(err.detail || "예약 생성에 실패했습니다.");
				    return;
				}
					
				const res = await response.json();
				try {
					let cleanPhone = data.phone.replace(/-/g, "");
				
					await payment.requestPayment({
	                  	method: "CARD", // 카드 결제
	                  	amount: {
	                    currency: "KRW",
	                    value: res.price,
					},
						orderId: res.orderId,
	                  	orderName: data.hotelName,
	                  	successUrl: window.location.origin + "/payments/success?hotel_no=" + hotel_no,
	                  	failUrl: window.location.origin + "/payments/fail",
	                  	customerEmail: "customer123@gmail.com",
	                  	customerName: data.name,
	                  	customerMobilePhone: cleanPhone,
	                  	card: {
							useEscrow: false,
							flowMode: "DEFAULT",
							useCardPoint: false,
							useAppCardOnly: false,
						},
					});
				} catch (error) {
					if (error.code === "USER_CANCEL") {
						alert("결제가 취소 되었습니다.");
				    } else {
				    	showPaymentModal(error.message);
				    }
				}
			}
		});
         
		function showPaymentModal(message) {
			document.getElementById("paymentErrorMessage").textContent = message;
			document.getElementById("paymentErrorModal").style.display = "flex";
		}
      </script>

</head>
<body>
	<header class="item header margin-top-0">
		<div class="wrapper">
			<div class="container">
				<div class="row">
					<div class="col-md-12 text-center">
						<div class="text-pageheader">
							<div class="subtext-image" data-scrollreveal="enter bottom over 1.7s after 0.0s">RESERVATION</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</header>
	<div class="container toparea">
		<div class="underlined-title">
			<div class="editContent">
				<h1 class="text-center latestitems">숙소 예약</h1>
			</div>
			<div class="wow-hr type_short">
				<span class="wow-hr-h"> <i class="fa fa-star"></i> <i
					class="fa fa-star"></i> <i class="fa fa-star"></i>
				</span>
			</div>
		</div>

		<div class="container">


			<br />

			<div class="text-center title">
				<h1>RESERVATION</h1>
			</div>

			<form id="reserv_form">
				<div id="dateDiv" class="text-center">
					<input type="hidden" id="date" name="Date">
				</div>

				<input type="hidden" id="checkin" name="checkin"> 
				<input type="hidden" id="checkout" name="checkout"> 
				<input type="hidden" id="hotel_no" name="hotel_no" value="${hotelVO.hotel_no }"> 
				<input type="hidden" id="reserv_price" name="reserv_price">

				<div class="reservation_Info">
					<h3>Reservation Info</h3>
					<div class="text-center">

						<table class="table table-bordered">
							<tbody>
								<tr class="infoKey">
									<th class="text-center">숙소 이름</th>
									<th class="text-center">예약 일시</th>
									<th class="text-center">가격</th>
								</tr>
								<tr class="infoValue text-center">
									<td id="hotel_name">${hotelVO.hotel_name }</td>
									<td>
										<input type="text" id="user_date" readonly=readonly>
									</td>
									<td>
										<input type="text" class="text-center" id="price"readonly=readonly>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="personal_info">
					<h3>Personal Info</h3>
				</div>


				<div class="text-center" id="input">
					<table id="personalTable" class="table table-boardred">
						<colgroup>
							<col width="20%" />
							<col width="80%" />
						</colgroup>
						<tbody>
							<tr>
								<td>이름 :</td>
								<td class="text-left">
									<input type="text" id="name" name="reserv_name" class="form-control">
								</td>
							</tr>
							<tr>
								<td>전화번호 :</td>
								<td class="text-left">
									<input type="text" id="phone" name="reserv_phone" class="form-control" placeholder="010-1234-5678">
								</td>
							</tr>
							<tr>
								<td>예약 인원 :</td>
								<td class="text-left">
									<input type="number" id="people" name="reserv_people" class="form-control" placeholder="숫자만 입력하세요.">
								</td>
							</tr>
							<tr>
								<td>기타 :</td>
								<td class="text-left">
									<textarea style="resize: none;" cols="100" rows="5" class="form-control" id="reserv_etc" name="reserv_etc"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</form>

			<div class="text-center" id="agreeDiv">
				<div class="text-center" id="agreeDiv2">
					<label>이용동의</label> <br /> <input type="checkbox" name="agree"
						id="agree"> 예약 취소는 체크인 날짜 기준 7일 전 까지 가능합니다.
				</div>
			</div>

			<div class="text-center">
				<!-- 결제 UI -->
				<div id="payment-method"></div>
				<!-- 이용약관 UI -->
				<div id="agreement"></div>
				<!-- 결제하기 버튼 -->
				
				<button class="button" id="payment-button" style="margin-top: 30px">결제하기</button>
			</div>
		</div>
	</div>
	<div class="payment-error-overlay" id="paymentErrorModal" style="display:none;">
		<div class="payment-error-modal">
			<div class="payment-error-icon">✕</div>
			<h3 class="payment-error-title">결제 실패</h3>
			<p class="payment-error-desc" id="paymentErrorMessage"></p>
			<p class="payment-error-desc">다시 시도해 주세요.</p>
			<button class="payment-error-btn" onclick="document.getElementById('paymentErrorModal').style.display='none'">
				확인
			</button>
		</div>
	</div>
</body>
</html>



