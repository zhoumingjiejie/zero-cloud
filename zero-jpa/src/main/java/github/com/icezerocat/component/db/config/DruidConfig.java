package github.com.icezerocat.component.db.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.githup.icezerocat.admin.config.DruidConfig]
 * Description: Druid数据源配置
 * CreateDate:  2020/4/29 14:39
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DruidConfig {

    final private DruidDataSource druidDataSource;

    /**
     * 通过连接池获取sql connection
     *
     * @return sql链接
     */
    public Connection getConnectionByDruid() {
        try {
            return this.druidDataSource.getConnection();
        } catch (SQLException e) {
            log.error("数据池获取connection失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
