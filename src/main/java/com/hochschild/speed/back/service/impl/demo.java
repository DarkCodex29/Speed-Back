package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.ApplicationConfig;
import com.hochschild.speed.back.util.EncryptUtil;

public class demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ApplicationConfig applicationConfig;
		String externalLink="";
        try {
            String key= EncryptUtil.encrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, "WCEVALLOS");
            System.out.println(key);
            //externalLink=applicationConfig.getFrontUrl()+"#/login-external/"+key+"/"+"";
        } catch (Exception e) {
           
        }

	}

}
