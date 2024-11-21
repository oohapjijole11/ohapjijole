package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.dto.request.AuctionRequest;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    public void createAuction() {
//        given
        long itemId = 1L;
        AuctionRequest auctionRequest = new AuctionRequest("title", 10000000, LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2023, 1, 2, 0, 0, 0));
        AuthUser authUser = new AuthUser(1L, "user", "email", UserRole.USER);
        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

//        when
        ServerException exception = assertThrows(ServerException.class, () -> {
            auctionService.createAuction(authUser, itemId, auctionRequest);
        });
//        then
        assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
    }
}