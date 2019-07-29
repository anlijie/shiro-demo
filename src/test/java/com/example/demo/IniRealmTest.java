package com.example.demo;

import antlr.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * @ProjectName: demo
 * @Package: com.example.demo
 * @ClassName: IniRealmTest
 * @Description: java类作用描述
 * @Author: alj
 * @CreateDate: 2018/7/26 20:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/26 20:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IniRealmTest {


    @Test
    public void testAuthenticationTest() {

        // 这个是内置的realm类
        IniRealm iniRealm = new IniRealm("classpath:user.ini");


        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 这个是需要认证的信息
        UsernamePasswordToken token = new UsernamePasswordToken("Mark","123456");
        subject.login(token);

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        //subject.logout();

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        subject.checkRole("admin");
        subject.checkPermissions("user:delete","user:update");
    }

}
