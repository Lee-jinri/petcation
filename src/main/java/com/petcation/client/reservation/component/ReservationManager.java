package com.petcation.client.reservation.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.petcation.client.reservation.vo.ReservVO;
import com.petcation.common.exception.BadRequestException;
import com.petcation.common.exception.ForbiddenException;
import com.petcation.common.vo.ReservationRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationManager {
    
    public void validateOrder(ReservationRequestDTO req, LocalDateTime start, LocalDateTime end) {        
        LocalDateTime now = LocalDateTime.now();
        
        if(start == null || end == null) {
            throw new BadRequestException("예약 날짜를 모두 선택해주세요.");
        }
        if(!start.isBefore(end)) {
            throw new BadRequestException("체크인 날짜는 체크아웃 날짜보다 빨라야 합니다.");
        }
        if(start.isBefore(now.toLocalDate().atStartOfDay())) {
            throw new BadRequestException("이미 지난 날짜는 예약할 수 없습니다.");
        }

        int hotelNo = req.getHotelNo();
        String hotelName = req.getHotelName();
        
        if(hotelNo <= 0 || hotelName == null || hotelName.isEmpty()) {
            throw new BadRequestException("잘못된 숙소 정보입니다. 다시 시도해주세요.");
        }
        
        String name = req.getReserv_name();
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("예약자명을 입력하세요.");
        }
        if (!name.matches("^[가-힣]{2,15}$")) {
            throw new BadRequestException("올바른 예약자명을 입력하세요. (특수문자,영어,숫자 사용 불가 , 한글로 2자 이상 입력)");
        }

        String phone = req.getReserv_phone();
        if (phone == null || !phone.matches("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$")) {
            throw new BadRequestException("전화번호를 다시 입력해주세요. 010-1234-5678 또는 02-1234-5678의 형태로 입력하세요.");
        }

        int people = req.getReserv_people();
        String peopleStr = String.valueOf(people);
        if (!peopleStr.matches("^(?:[1-9]|[1-9][0-9]|100)$")) {
            throw new BadRequestException("예약 인원을 잘못 입력하셨습니다.");
        }
    }
    
    public int calculatePrice(int price, LocalDateTime start, LocalDateTime end) {
        long nights = ChronoUnit.DAYS.between(start, end);
        int totalPrice = (int) nights * price;
        
        return totalPrice;
    }
    
    public void validateCancellation(ReservVO r, String orderId, int userNo) {
        if (r.getUser_no() != userNo) {
            throw new ForbiddenException("본인의 예약만 취소할 수 있습니다."); 
        }
        
        if(r.getReserv_status().equals("CANCELED")) {
            throw new BadRequestException("이미 취소된 예약입니다.");
        }
        if(r.getReserv_status().equals("FAILED")) {
            throw new BadRequestException("이미 실패 처리된 예약입니다. 다시 확인해 주세요.");
        }
        
        if (!r.getReserv_status().equals("DONE")) {
            throw new BadRequestException("취소 가능한 예약 상태가 아닙니다.");
        }
        
        LocalDateTime checkInDateTime = LocalDate.parse(r.getCheckin()).atStartOfDay();
        LocalDateTime deadline = checkInDateTime.minusDays(7);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(deadline)) {
            throw new BadRequestException("예약 취소는 체크인 날짜 7일 전까지만 가능합니다.");
        }
    }
}
