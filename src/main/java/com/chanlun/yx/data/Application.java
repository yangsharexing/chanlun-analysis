package com.chanlun.yx.data;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;
 
//@SpringBootApplication
//public class Application {
//	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
//		System.out.println();
//	}
//}



@SpringBootApplication
@MapperScan("com.chanlun.yx.data.dao")
public class Application {

	/**
	 * @Title: main @author: 杨兴 @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return: @throws
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SpringApplication.run(SpringBootTestApplication.class, args);

		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
	    builder.headless(false).run(args);
	}

	@Autowired
	private Environment env;

	// destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/finance?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
		dataSource.setUsername("root");// 用户名
		dataSource.setPassword("yangxing");// 密码
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setInitialSize(2);// 初始化时建立物理连接的个数
		dataSource.setMaxActive(20);// 最大连接池数量
		dataSource.setMinIdle(2);// 最小连接池数量
		dataSource.setMaxWait(6000000);// 获取连接时最大等待时间，单位毫秒。
		dataSource.setValidationQuery("SELECT 1");// 用来检测连接是否有效的sql
		dataSource.setTestOnBorrow(false);// 申请连接时执行validationQuery检测连接是否有效
		dataSource.setTestWhileIdle(true);// 建议配置为true，不影响性能，并且保证安全性。
		dataSource.setPoolPreparedStatements(false);// 是否缓存preparedStatement，也就是PSCache
		dataSource.setQueryTimeout(100000);
		return dataSource;
	}

}

