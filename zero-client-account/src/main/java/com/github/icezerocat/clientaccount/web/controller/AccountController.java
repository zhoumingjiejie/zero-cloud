package com.github.icezerocat.clientaccount.web.controller;

import com.github.icezerocat.clientaccount.entity.Account;
import com.github.icezerocat.clientaccount.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 账号控制器
 * CreateDate:  2021/1/28 21:42
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountMapper accountMapper;

    /**
     * 获取全部用户 数据
     *
     * @return list
     */
    @GetMapping("getAll")
    public List<Account> getAll() {
        return this.accountMapper.selectList(null);
    }

    /**
     * 插入用户数据
     */
    @GetMapping("insert")
    public void insert() {
        Account account = new Account();
        account.setUserId(0L);
        account.setTotal(new BigDecimal("0"));
        account.setUsed(new BigDecimal("0"));
        account.setResidue(new BigDecimal("0"));
        this.accountMapper.insert(account);
        String a = null;
        a.equals("");
    }
}
