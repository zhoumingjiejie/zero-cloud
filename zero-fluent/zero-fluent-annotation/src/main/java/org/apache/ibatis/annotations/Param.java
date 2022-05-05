package org.apache.ibatis.annotations;

import java.lang.annotation.*;

/**
 * The annotation that specify the parameter name.
 *
 * <p>
 * <b>How to use:</b>
 *
 * <pre>
 * public interface UserMapper {
 *   &#064;Select("SELECT id, name FROM users WHERE name = #{name}")
 *   User selectById(&#064;Param("name") String value);
 * }
 * </pre>
 *
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    /**
     * Returns the parameter name.
     *
     * @return the parameter name
     */
    String value();
}

