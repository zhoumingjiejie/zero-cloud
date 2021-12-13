package com.github.icezerocat.zero.dynamic.authorization.web.controller;

import com.github.icezerocat.zero.dynamic.authorization.boot.ResourceAddressMetadataInitializer;
import github.com.icezerocat.component.common.http.HttpResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 地址元数据控制器
 * CreateDate:  2021/11/2 15:51
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("metadata")
@RequiredArgsConstructor
public class AddressMetadataController {
    final private ResourceAddressMetadataInitializer resourceAddressMetadataInitializer;

    /**
     * 初始化地址元数据
     */
    @GetMapping("initAddressMetadata")
    public HttpResult initAddressMetadata() {
        this.resourceAddressMetadataInitializer.initAddressMetadata();
        return HttpResult.ok();
    }
}
