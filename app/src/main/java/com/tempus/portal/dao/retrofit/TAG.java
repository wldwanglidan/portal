package com.tempus.portal.dao.retrofit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
@Retention(RetentionPolicy.RUNTIME)  //这句话代表着将使用该注解类的信息值保持到真正的客户端运行时环境。
public @interface TAG {

    String value() default "";

}
