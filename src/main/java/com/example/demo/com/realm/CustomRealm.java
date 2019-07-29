package com.example.demo.com.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.com.realm
 * @ClassName: CustomRealm
 * @Description: java类作用描述
 * @Author: alj
 * @CreateDate: 2018/7/26 22:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/26 22:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CustomRealm extends AuthorizingRealm {

    /*
    * 模拟数据库
    * */
    Map<String,String> userMap = new HashMap<>(16);

    {
        userMap.put("Mark","283538989cef48f3d7d8a1c1bdf2008f");
        super.setName("customRealm");
    }

    /*
    *   自定义的权限校验
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userName = (String)principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRoleByUserName(userName);
        Set<String> permissions = getPermissionByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionByUserName(String userName) {
        Set<String> sets = new HashSet<>(16);
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /*
    *   这个是模拟数据库
    * */
    private Set<String> getRoleByUserName(String userName) {
        Set<String> sets = new HashSet<>(10);
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    /*
    *  这是用户的登录授权
    * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 1.从主体传过来的认证信息中，获得用户名
        String userName = (String)authenticationToken.getPrincipal();

        // 2.通过用户名到数据库中获得凭证

        String password = getPasswordByUserName(userName);

        if (password == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("Mark",password,"customRealm");
        // 这个是直接加盐在原来加密的基础上
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return authenticationInfo;
    }

    /*
    *   这是模拟数据库的
    * */
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }

    // 这个是加密生成的字符串,后边加盐
  /*  public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","Mark");
        System.out.println(md5Hash);
    }*/
}
