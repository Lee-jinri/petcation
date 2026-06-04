package com.petcation.client.payments.controller;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcation.client.join.vo.MemberVO;
import com.petcation.client.payments.service.PaymentFacade;
import com.petcation.client.payments.vo.WebhookDto;
import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.exception.UnauthorizedException;
import com.petcation.common.vo.ReservationRequestDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/payments/*")
@AllArgsConstructor
public class PaymentsController {

	private final PaymentFacade paymentsFacade;
	 
	@PostMapping(value = "/create", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<?> createPayment(@RequestBody ReservationRequestDTO req, HttpServletRequest request){

		HttpSession session = request.getSession();
		MemberVO userID =(MemberVO)session.getAttribute("lmember");
		if(userID == null) throw new UnauthorizedException("로그인이 필요한 서비스 입니다.");
		
		int userNo = userID.getUser_no();
		String orderId = "order_" + UUID.randomUUID().toString().replace("-", "");
		
		req.setUserNo(userNo);
        req.setOrderId(orderId);

        int price = paymentsFacade.createPayment(req);

		return ResponseEntity.ok(Map.of(
				"orderId", orderId,
				"price", price
	    ));
	}
	
	@GetMapping("/success")
	public String paymentSuccess(
	    @RequestParam("hotel_no") int hotelNo,
	    @RequestParam String paymentKey,
        @RequestParam String orderId,
        @RequestParam Long amount,
        RedirectAttributes redirectAttributes,
        Model model) {
	    try {
	        boolean isConfirmed = paymentsFacade.confirmPayment(paymentKey, orderId, amount);
	        
	        if (!isConfirmed) {
	            redirectAttributes.addFlashAttribute("errorMessage", "결제에 실패했습니다. 다시 시도해 주세요.");
	            return "redirect:/reserv/reservForm?hotel_no=" + hotelNo;
	        }
	        
	        ReservVO result = paymentsFacade.getReservationByOrderId(orderId);
	        model.addAttribute("result", result);
	        
	        return "reserv/success";
    	} catch (RuntimeException e) {
    	    String message = Objects.toString(e.getMessage(), "알 수 없는 오류가 발생했습니다");
            log.error("결제 실패 원인: " + message);
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        return "redirect:/reserv/reservForm?hotel_no=" + hotelNo;
	}
	
	@PatchMapping(value = "/{orderId}/cancel", produces = "application/json; charset=UTF-8")
    @ResponseBody
	public ResponseEntity<String> cancel(@PathVariable String orderId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberVO userID = (MemberVO) session.getAttribute("lmember");
        if(userID == null) throw new UnauthorizedException("로그인이 필요한 서비스 입니다.");
        int userNo = userID.getUser_no();
        
	    paymentsFacade.cancelPaymentAndReservation(orderId, userNo);
	    
	    return ResponseEntity.ok("정상적으로 취소되었습니다.");
	}
	
	@PostMapping("/webhook")
	public ResponseEntity<Void> handleWebhook(@RequestBody WebhookDto payload) {

        paymentsFacade.processWebhook(payload.getData());
	    
	    return ResponseEntity.ok().build();
	}
}
