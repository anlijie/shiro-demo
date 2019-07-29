package com.example.demo;

import com.example.demo.com.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @ProjectName: demo
 * @Package: com.example.demo
 * @ClassName: CustomRealmTest
 * @Description: java类作用描述
 * @Author: alj
 * @CreateDate: 2018/7/26 22:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/26 22:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CustomRealmTest {

    @Test
    public void testAuthenticationTest() {
        // 这个是自定义的realm
        CustomRealm customRealm = new CustomRealm();

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // 这是散列加密工具的封装类啊
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        // 这一步是把自定义的散列加密集成到我自定义的认证机制中
        customRealm.setCredentialsMatcher(matcher);

        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        // 这个是需要认证的信息
        UsernamePasswordToken token = new UsernamePasswordToken("Mark","123456");
        subject.login(token);

        System.out.println("isAuthenticated="+subject.isAuthenticated());

        //subject.logout();

    /*    System.out.println("isAuthenticated="+subject.isAuthenticated());

        subject.checkRole("admin");
        subject.checkPermissions("user:delete","user:update");*/
        /*subject.checkRole("admin");
        subject.checkPermission("user:add");*/

    }

}

