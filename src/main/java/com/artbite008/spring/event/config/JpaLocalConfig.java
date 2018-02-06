package com.artbite008.spring.event.config;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.artbite008.spring.event.util.EntityManagerFactoryProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;


@Configuration
@EnableTransactionManagement
public class JpaLocalConfig extends JpaBaseConfiguration {

	private EclipseLinkJpaVendorAdapter adapter;
	
	private DataSource dataSource;

	protected JpaLocalConfig(DataSource dataSource, JpaProperties properties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManager,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		super(dataSource, properties, jtaTransactionManager, transactionManagerCustomizers);
		this.dataSource = dataSource;
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		this.adapter = new EclipseLinkJpaVendorAdapter();
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return map;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder) {
		LocalContainerEntityManagerFactoryBean em = EntityManagerFactoryProvider.get(
        			dataSource, 
	        		"com.artbite008.spring.event"
		        );	
		em.setJpaPropertyMap(initJpaProperties());
		return em;
	}
	
	private final Map<String, ?> initJpaProperties() {
		final Map<String, Object> ret = new HashMap();

		/**
		 * for auto-generating DB tables
		 */
//		 ret.put("eclipselink.weaving", "false");
//		 ret.put("eclipselink.ddl-generation", "create-or-extend-tables");
//		 ret.put("eclipselink.ddl-generation.output-mode", "database");
//		 ret.put("eclipselink.cache.shared.default", "false");
		 ret.put("eclipselink.logging.level.sql", "FINE");
		 ret.put("eclipselink.logging.parameters", "true");
		 ret.put("eclipselink.logging.exceptions", "true");

		/**
		 * for generating .sql for creating DB tables
		 */
		// ret.put("eclipselink.weaving", "false");
		// ret.put("eclipselink.ddl-generation", "create-tables");
		// ret.put("eclipselink.ddl-generation.output-mode", "both");
		// ret.put("eclipselink.cache.shared.default", "false");
		// ret.put("eclipselink.application-location", "c:/sql/");
		// ret.put("eclipselink.create-ddl-jdbc-file-nam", "create_table.jdbc");
		// ret.put("eclipselink.drop-ddl-jdbc-file-name", "drop_table.jdbc");
		// ret.put("eclipselink.logging.level.sql", "FINE");
		// ret.put("eclipselink.logging.parameters", "true");
		// ret.put("eclipselink.logging.exceptions", "true");

		/**
		 * for release strategy
		 */
//		ret.put("eclipselink.ddl-generation", "none");
//		ret.put("eclipselink.target-database", "HANA");
//		ret.put("eclipselink.weaving", "false");
//		ret.put("eclipselink.cache.shared.default", "false");
		return ret;
	}
}