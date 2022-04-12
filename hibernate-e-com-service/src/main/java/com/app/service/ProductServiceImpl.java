package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import com.app.entity.Customer;
import com.app.entity.CustomerProduct;
import com.app.entity.Product;
import com.app.exception.CartEmptyException;
import com.app.exception.ProductNotAvailableException;
import com.app.utils.HibernateUtility;
import com.app.utils.ScannerUtility;

public class ProductServiceImpl implements ProductService {

	public void addProduct() {
		Session session = HibernateUtility.getConnection().openSession();
		Transaction tx = session.beginTransaction();
		try {

			Scanner sc = ScannerUtility.getScanner();
			System.out.println("how many products you want to add");
			int n = sc.nextInt();
			for (int i = 0; i < n; i++) {
				Product product = new Product();
				System.out.println("Enter the product name");
				product.setName(sc.next());

				System.out.println("Enter the quantity");
				product.setQty(sc.nextInt());

				System.out.println("Enter the product price");
				product.setPrice(sc.nextDouble());

				session.save(product);
			}

			tx.commit();
			if (tx.wasCommitted()) {
				System.out.println("saved");
			} else {
				System.out.println("fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public List<Product> displayProduct() {
		Session session = HibernateUtility.getConnection().openSession();

		List<Product> products = session.createQuery("from Product").list();
		if (!products.isEmpty()) {
			System.out.println("_____________________________________");
			for (Product product : products) {
				System.out.println(product.getName() + "\t" + product.getQty() + "\t" + product.getPrice());
			}
			System.out.println("______________________________________");
		} else {
			try {
				throw new ProductNotAvailableException("product is not available");
			} catch (Exception e) {
				e.printStackTrace();
			}
			addProduct();
		}
		return products;

	}

	private Long isProductAvailable() {
		Session session = HibernateUtility.getConnection().openSession();
		// now we check the count 0 or not
		Long count = (Long) session.createCriteria(Product.class).setProjection(Projections.rowCount()).uniqueResult();
		session.close();
		return count;

	}

	private Product getProductById(int id) {
		Session session = HibernateUtility.getConnection().openSession();
		// here we get the product
		Product product = (Product) session.get(Product.class, id);
		session.close();
		return product;

	}

	public void buyProduct() {
		Session session = HibernateUtility.getConnection().openSession();
		Transaction tx = session.beginTransaction();

		if (isProductAvailable() != null) {
			Scanner sc = ScannerUtility.getScanner();

			Customer customer = new Customer();
			System.out.println("Enter Customer Name");
			customer.setCname(sc.next());
			System.out.println("Enter Mobile no");
			customer.setMobileNo(sc.next());

			displayProduct();

			System.out.println("how many products you want to buy");
			int num = sc.nextInt();

			List<CustomerProduct> addtocart = new ArrayList<>();

			for (int i = 0; i < num; i++) {
				CustomerProduct customerProduct = new CustomerProduct();
				System.out.println("enter ProductId");

				int productId = sc.nextInt();

				Product product = getProductById(productId);

				System.out.println("how many quantity you want to buy");
				int productQty = sc.nextInt();

				// new data add in add to cart
				customerProduct.setQty(productQty);
				customerProduct.setCustomer(customer);
				customerProduct.setProduct(product);
				addtocart.add(customerProduct);
			}
			customer.setCustomerProduct(addtocart);
			session.save(customer);
			tx.commit();
			session.close();
			if (tx.wasCommitted()) {
				Session session1 = HibernateUtility.getConnection().openSession();
				Transaction tx1 = session1.beginTransaction();

				for (CustomerProduct customerProduct : addtocart) {
					Product product = customerProduct.getProduct();
					int availQty = product.getQty() - customerProduct.getQty();
					product.setQty(availQty);
					session1.merge(product);// update
				}
				tx1.commit();
				System.out.println("customer saves with cart");
				session1.close();
			}
		} else {
			try {
				throw new ProductNotAvailableException("Product is not available");
			} catch (Exception e) {
				e.printStackTrace();
			}
			addProduct();
		}

	}

	public void billing() {
		Session session = HibernateUtility.getConnection().openSession();
		Scanner sc = ScannerUtility.getScanner();

		List<Customer> customers = session.createCriteria(Customer.class).list();

		for (Customer customer : customers) {
			double grandTotal = 0;

			if (!customer.getCustomerProduct().isEmpty()) {
				for (CustomerProduct cart : customer.getCustomerProduct()) {
					Product product = cart.getProduct();

					double amount = (double) (cart.getQty() * product.getPrice());
					grandTotal += amount;
				}
			} else {
				try {
					throw new CartEmptyException("cart is Empty");
				} catch (Exception e) {
					e.printStackTrace();
				}
				buyProduct();
			}
			System.out.println("_____________________________________________");
			System.out.println("Customer Details");
			System.out.println("ID \t\t NAME \t\t MOBILE");
			System.out.println(customer.getCid() + "\t\t" + customer.getCname() + "\t\t" + customer.getMobileNo());
			System.out.println("______________________________________________");

			System.out.println("Buy Product Details");
			for (CustomerProduct cart : customer.getCustomerProduct()) {
				Product product = cart.getProduct();
				System.out.println(product.getId() + "\t\t" + product.getName() + "\t\t" + product.getPrice() + "\t\t"+ cart.getQty());
			}
			System.out.println("________________________________________________");
			System.out.println("Billing Amount");
			System.out.println("Grand Total is: " + grandTotal);
			System.out.println("_________________________________________________");
		}

		System.out.println("do you want to continue:Y/N");
		String con = sc.next();
		if (con.equalsIgnoreCase("Y")) {
			buyProduct();

		}
	}

}
