package com.chuanshuke.auth.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import com.chuanshuke.auth.service.UserJwt;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		LinkedHashMap response = new LinkedHashMap();
		String name = authentication.getName();
//		System.out.println("自定义用户认证"+ name);
		response.put("user_name", name);
		
		Object principal = authentication.getPrincipal();
		UserJwt userJwt = null;
		if(principal instanceof UserJwt) {
			userJwt = (UserJwt) principal;
		}else {
			//refresh_token默认不去调用userdetailservice获取用户信息
			UserDetails userDetails = userDetailsService.loadUserByUsername(name);
			userJwt = (UserJwt) userDetails;
		}
		response.put("name", userJwt.getName());
		response.put("id", userJwt.getId());
//		response.put("utype", userJwt.getUtype());
//		response.put("userpic", userJwt.getUserpic());
//		response.put("companyId", userJwt.getCompanyId());
		if(authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		
		return response;
	}
}
