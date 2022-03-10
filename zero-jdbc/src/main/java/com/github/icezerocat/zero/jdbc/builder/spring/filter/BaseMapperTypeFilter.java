package com.github.icezerocat.zero.jdbc.builder.spring.filter;

import com.github.icezerocat.zero.jdbc.builder.spring.BaseMapper;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;

import java.util.Arrays;

/**
 * @author dragons
 * @date 2022/1/12 11:27
 */
public class BaseMapperTypeFilter extends AbstractClassTestingTypeFilter {

    private final String className;

    public BaseMapperTypeFilter() {
        className = BaseMapper.class.getName();
    }

    @Override
    protected boolean match(ClassMetadata classMetadata) {
        if (classMetadata.isInterface() && Arrays.asList(classMetadata.getInterfaceNames()).contains(className)) {
            return true;
        }
        return false;
    }
}
