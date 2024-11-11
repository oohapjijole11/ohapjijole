package com.sparta.final_project.domain.toss.service;

import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.toss.dto.request.DepositRequest;
import com.sparta.final_project.domain.toss.entity.VirtualAccount;
import com.sparta.final_project.domain.toss.repository.VirtualAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;

    public String deposit(Long accountId, DepositRequest depositRequest) {
//        계좌가 있는지 확인
        VirtualAccount account = virtualAccountRepository.findById(accountId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_ACCOUNT));
//        입금 금액이 0이면 오류
        if (depositRequest.getAmount() <= 0) {
            throw new OhapjijoleException(ErrorCode._BAD_REQUEST_ACCOUNT);
        }
//        계좌금액이랑 입금금액 합계
        account.setBalance(account.getBalance() + depositRequest.getAmount());
        virtualAccountRepository.save(account);
        return depositRequest.getAmount() + "원이 입금되었습니다.\n"
                + " 현재 잔액은 " + account.getBalance() + "원입니다.";
    }

    public String withdraw(Long accountId, DepositRequest depositRequest) {
//        계좌가 있는지 확인
        VirtualAccount account = virtualAccountRepository.findById(accountId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_ACCOUNT));
//        출금 금액이 0이면 오류
        if (depositRequest.getAmount() <= 0) {
            throw new OhapjijoleException(ErrorCode._BAD_REQUEST_ACCOUNT);
        }
//        잔고보다 출금 금액이 크면 오류
        if (account.getBalance() < depositRequest.getAmount()) {
            throw new OhapjijoleException(ErrorCode._BAD_REQUEST_AMOUNT);
        }
//        잔고에서 출금
        account.setBalance(account.getBalance() - depositRequest.getAmount());
        virtualAccountRepository.save(account);
        return depositRequest.getAmount() + "원이 출금되었습니다.\n"
                + " 현재 잔액은 " + account.getBalance() + "원입니다.";
    }
}
