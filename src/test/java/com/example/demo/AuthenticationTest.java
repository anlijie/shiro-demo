package com.example.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ProjectName: demo
 * @Package: com.example.demo
 * @ClassName: AuthenticationTest
 * @Description: java类作用描述
 * @Author: alj
 * @CreateDate: 2018/7/26 17:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/26 17:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("Mark","123456","admin","user");
    }

    @Test
    public void testAuthenticationTest() {

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 这个是内置的答案，就是传过来的信息与它对比
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 这个是需要认证的信息
        UsernamePasswordToken token = new UsernamePasswordToken("Mark","123456");
        subject.login(token);

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        //subject.logout();

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        subject.checkRoles("admin","user");
    }

}
