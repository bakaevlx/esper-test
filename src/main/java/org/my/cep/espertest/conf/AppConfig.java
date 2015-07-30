package org.my.cep.espertest.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/** 
 * Annotation based Spring app configuration (v. 3.2+).
 * 
 */
@Configuration
@ComponentScan("org.my.cep.espertest")
@PropertySource("application.properties")
public class AppConfig {


}
