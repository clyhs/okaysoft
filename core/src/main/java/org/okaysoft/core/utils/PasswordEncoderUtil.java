package org.okaysoft.core.utils;

import org.okaysoft.core.security.model.User;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;


public class PasswordEncoderUtil {

	public static String encode(String password,User user){
        return new Md5PasswordEncoder().encodePassword(password,user.getMetaData());
    }
}
