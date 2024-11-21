package com.sparta.final_project.domain.common.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.common.entity.Color;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.BuyTicketsRepository;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/*티켓 가진 사람들에게 슬랙 알림 보내기*/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SlackTicketService {

    private final SlackService slackService;
    private final TicketRepository ticketRepository;
    private final BuyTicketsRepository buyTicketsRepository;

    //입찰 20전 티켓 가진 사람들에게 슬랙 메세지
    private void sendRemiderSlack(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));
        Auction auction = ticket.getAuction();
        List<String> slackUrlList = buyTicketsRepository.findAllByTicketId(ticketId).stream()
                                        .map(BuyTickets::getUser)
                                        .map(User::getSlackUrl)
                                        .toList();
        String title = "경매 시작 리마인더";
        String message = auction.getTitle()+"는 지금부터 입장이 가능합니다. 20분후 만나요!";
        for(String slackUrl : slackUrlList) {
            slackService.sendSlackMessage(slackUrl, title, message);
        }
    }

    //경매 시작 슬랙 메세지
    private void sendStartSlack(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));
        Auction auction = ticket.getAuction();
        List<String> slackUrlList = buyTicketsRepository.findAllByTicketId(ticketId).stream()
                .map(BuyTickets::getUser)
                .map(User::getSlackUrl)
                .toList();
        String title = "경매 시작 알리미";
        String message = auction.getTitle()+"가 지금 시작됩니다. 서두르세요!";
        for(String slackUrl : slackUrlList) {
            slackService.sendSlackMessage(slackUrl, title, message);
        }
    }

    //티켓구매시 알림
    private void sendTicketBuySlack(String slackUrl, Ticket ticket){
        Auction auction = ticket.getAuction();
        String title = "티켓 구매 알리미";
        String message = auction.getTitle()+" 티켓이 도착했습니다! \n 경매는 20분 전부터 입장이 가능합니다. 경매 일정을 확인하시고, 늦지 않게 들어와주세요! \n";
        String fieldTitle = auction.getTitle();
        String fieldContent = "경매 시작시간 : "+auction.getStartTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))+"\n 경매 입장 등급 : "+auction.getGrade() +"\n 경매 시작 금액 : "+auction.getStartPrice() +" 원";
        slackService.sendSlackMessage(slackUrl, title, Color.GREEN, message, fieldTitle, fieldContent);
    }
}
