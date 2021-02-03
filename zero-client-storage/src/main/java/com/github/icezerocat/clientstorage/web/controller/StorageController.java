package com.github.icezerocat.clientstorage.web.controller;

import com.github.icezerocat.clientstorage.entity.Storage;
import com.github.icezerocat.clientstorage.mapper.StorageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: 存储仓库控制器
 * CreateDate:  2021/1/28 10:49
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageMapper storageMapper;

    @GetMapping("getAll")
    public List<Storage> getAll() {
        return this.storageMapper.selectList(null);
    }
}
