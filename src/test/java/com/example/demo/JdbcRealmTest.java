package com.example.demo;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @ProjectName: demo
 * @Package: com.example.demo
 * @ClassName: JdbcRealmTest
 * @Description: java类作用描述
 * @Author: alj
 * @CreateDate: 2018/7/26 20:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/26 20:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcRealmTest {

    DruidDataSource druidDataSource = new DruidDataSource();
    {

        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
    }

    @Test
    public void testAuthenticationTest() {

        // 设置内置的jdbc
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        // 权限数据的开关
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "select password from test_user where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql = "select role_name from test_user_role where user_name = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 这个是需要认证的信息
        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming","654321");
        subject.login(token);

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        //subject.logout();
        /*subject.checkRole("admin");

        subject.checkPermission("user:select");*/
        subject.checkRole("user");
    }
}
