package com.app.utils;


import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.app.entity.Customer;
import com.app.entity.CustomerProduct;
import com.app.entity.Product;

public class HibernateUtility {

	public static SessionFactory getConnection() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver");
		properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javashopping");
		properties.setProperty("hibernate.connection.username", "root");
		properties.setProperty("hibernate.connection.password", "root");

		properties.setProperty("hibernate.show_Sql", "true");

		properties.setProperty("hibernate.dailect", "org.hibernate.dialect.MySQLDialect");

		// configuration

		Configuration cfg = new Configuration();
		cfg.setProperties(properties);

		cfg.addAnnotatedClass(Product.class);
		cfg.addAnnotatedClass(Customer.class);
		cfg.addAnnotatedClass(CustomerProduct.class);

		// registry

		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();

		builder.applySettings(cfg.getProperties());

		StandardServiceRegistry service = builder.build();

		SessionFactory sf = cfg.buildSessionFactory(service);

		return sf;

	}
}
