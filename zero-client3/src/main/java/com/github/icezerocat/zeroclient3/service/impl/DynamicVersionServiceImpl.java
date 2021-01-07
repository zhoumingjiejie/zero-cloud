package com.github.icezerocat.zeroclient3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.icezerocat.zeroclient3.entity.DynamicVersion;
import com.github.icezerocat.zeroclient3.mapper.DynamicVersionMapper;
import com.github.icezerocat.zeroclient3.service.DynamicVersionService;
import org.springframework.stereotype.Service;

/**
 * Description: 路由版本impl
 * CreateDate:  2020/12/21 15:12
 *
 * @author zero
 * @version 1.0
 */
@Service("dynamicVersionService")
public class DynamicVersionServiceImpl extends ServiceImpl<DynamicVersionMapper, DynamicVersion> implements DynamicVersionService {
}
