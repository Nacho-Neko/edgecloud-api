package com.mikou.edgecloud.common.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Configuration
public class MybatisPlusConfig {

    /**
     * 由于 Spring Boot 4.0.2 与当前 MyBatis-Plus (3.5.16) 的自动配置存在兼容性问题
     * (自动配置无法正确检测到 DataSource Bean)，此处需要手动显式配置 SqlSessionFactory。
     * 使用 MybatisSqlSessionFactoryBean 以确保 MyBatis-Plus 的增强功能（如通用 Mapper）正常生效。
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
        
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        // 关键：开启字段映射
        configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);

        // 注册 UUID TypeHandler
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(UUID.class, JdbcType.OTHER, new BaseTypeHandler<UUID>() {
            @Override
            public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
                ps.setObject(i, parameter);
            }

            @Override
            public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
                Object value = rs.getObject(columnName);
                return value instanceof UUID ? (UUID) value : null;
            }

            @Override
            public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
                Object value = rs.getObject(columnIndex);
                return value instanceof UUID ? (UUID) value : null;
            }

            @Override
            public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
                Object value = cs.getObject(columnIndex);
                return value instanceof UUID ? (UUID) value : null;
            }
        });

        factoryBean.setConfiguration(configuration);
        
        // 使用内置工具类默认值，确保 mybatis-plus 基础功能
        com.baomidou.mybatisplus.core.config.GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        // 显式指定 id-type 为 AUTO
        globalConfig.getDbConfig().setIdType(IdType.AUTO);
        factoryBean.setGlobalConfig(globalConfig);
        
        return factoryBean.getObject();
    }
}
