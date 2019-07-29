package com.example.demo.realms;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import com.example.demo.service.UserService;
/**
 * realm实现类,用于实现具体的验证和授权方法
 * @author Bean
 *
 */
public class MyShiroRealm extends AuthorizingRealm {

	/**
	 * 方便用于加密 参数：AuthenticationToken是从表单穿过来封装好的对象
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo:" + token);

		// 将AuthenticationToken强转为AuthenticationToken对象
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;

		// 获得从表单传过来的用户名
		String username = upToken.getUsername();

		// 从数据库查看是否存在用户
		UserService userService = new UserService();

		// 如果用户不存在，抛此异常
		if (!userService.selectUsername(username)) {
			throw new UnknownAccountException("无此用户名！");
		}

		// 认证的实体信息，可以是username，也可以是用户的实体类对象，这里用的用户名
		Object principal = username;
		// 从数据库中查询的密码
		Object credentials = userService.selectPassword(username);
		// 颜值加密的颜，可以用用户名
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
		// 当前realm对象的名称，调用分类的getName()
		String realmName = this.getName();

		// 创建SimpleAuthenticationInfo对象，并且把username和password等信息封装到里面
		// 用户密码的比对是Shiro帮我们完成的
		SimpleAuthenticationInfo info = null;
		info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);

		return info;
	}

	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		System.out.println("MyShiroRealm的doGetAuthorizationInfo授权方法执行");

		// User user=(User)
		// principals.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
		// System.out.println("在MyShiroRealm中AuthorizationInfo（授权）方法中从session中获取的user对象:"+user);

		// 从PrincipalCollection中获得用户信息
		Object principal = principals.getPrimaryPrincipal();
		System.out.println("ShiroRealm  AuthorizationInfo:" + principal.toString());

		// 根据用户名来查询数据库赋予用户角色,权限（查数据库）
		Set<String> roles = new HashSet<>();
		Set<String> permissions = new HashSet<>();
		roles.add("user");
		permissions.add("user:query");
		if ("admin".equals(principal)) {
			roles.add("admin");
			permissions.add("admin:query");
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		//添加权限
		info.setStringPermissions(permissions);
		return info;
		// return null;
	}

}