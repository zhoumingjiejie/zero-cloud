package com.github.icezerocat.zero.license.listener;

import com.github.icezerocat.component.license.core.model.LicenseExtraParam;
import com.github.icezerocat.component.license.verify.listener.AbsCustomVerifyListener;
import github.com.icezerocat.component.core.exception.ApiException;
import org.springframework.stereotype.Component;

/**
 * <p>Lic自定义验证监听器B</p>
 *
 * @author appleyk
 * @version V.0.2.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on 10:24 下午 2020/8/21
 */
@Component
public class CustomVerifyListenerB extends AbsCustomVerifyListener {
    @Override
    public boolean verify(LicenseExtraParam licenseExtra) throws ApiException {
        System.out.println("======= 自定义证书验证监听器B 实现verify方法  =======");
        return true;
    }
}
