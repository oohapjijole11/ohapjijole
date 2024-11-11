package com.sparta.final_project.domain.toss.controller;

import com.sparta.final_project.domain.toss.dto.request.DepositRequest;
import com.sparta.final_project.domain.toss.service.VirtualAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/virtual-account")
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

//    입금
    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<?> deposit(@PathVariable Long accountId, @RequestBody DepositRequest depositRequest) {
        return ResponseEntity.ok().body(virtualAccountService.deposit(accountId, depositRequest));
    }

//    출금
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId, @RequestBody DepositRequest depositRequest) {
        return ResponseEntity.ok().body(virtualAccountService.withdraw(accountId, depositRequest));
    }
}
