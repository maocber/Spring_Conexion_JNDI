package co.com.periferia.alfa.core;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@Configuration
public class AppConfigurationJNDI {
	
 
	
	@Bean
	public JdbcTemplate jdbcTemplate() throws IllegalArgumentException, NamingException {
		return new JdbcTemplate(jndiDataSource());
	}
	
	
	private static Logger LOG = LoggerFactory.getLogger(AppConfigurationJNDI.class); 
	
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		LOG.info("initializing tomcat factory... ");
		return new TomcatServletWebServerFactory() {
			
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return new TomcatWebServer(tomcat, getPort() >= 0);
			}

			@Override
			protected void postProcessContext(Context context) {

				
				ContextResource resource = new ContextResource();
	            resource.setName("JDBC/SIPFACTELV2");
	            resource.setType(DataSource.class.getName());
	            resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
	            resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
	            resource.setProperty("url", URL_SERVER_JNDI);
	            resource.setProperty("username", USER);
	            resource.setProperty("password", USER_PASS);
	            context.getNamingResources().addResource(resource);
				
				context.getNamingResources().addResource(resource);
			}
		};
	}
	
	
	@Bean(destroyMethod="")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		try {
      //name JDBC configurado en el servidor de aplicaciones
			bean.setJndiName("java:comp/env/" + JDBC_NAME);
			bean.setProxyInterface(DataSource.class);
			bean.setLookupOnStartup(true);
			bean.afterPropertiesSet();
		}catch(Exception e) {
			LOG.error("ERRROR tomcat factory... " + e.getMessage());
		}
		
		return (DataSource)bean.getObject();
	}
	
}

