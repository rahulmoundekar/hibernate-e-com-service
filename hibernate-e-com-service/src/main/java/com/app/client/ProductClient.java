package com.app.client;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.app.exception.WrongInputException;
import com.app.service.ProductService;
import com.app.service.ProductServiceImpl;

import com.app.utils.ScannerUtility;

public class ProductClient {

	public static void main(String[] args) {

		int option = 0;
		ProductService service = new ProductServiceImpl();
		do {
			Scanner sc = ScannerUtility.getScanner();
			System.out.println("____________________________________________");
			System.out.println("1> 	Add Product");
			System.out.println("2>	Display Product");
			System.out.println("3>	Buy Product");
			System.out.println("4>	Billing");
			System.out.println("5> Exit");
			System.out.println("_______________________________________________");
			System.out.println("Select any one option");
			try {

				option = sc.nextInt();
				if (option != 0) {
					switch (option) {

					case 1:
						service.addProduct();
						break;
					case 2:
						service.displayProduct();
						break;
					case 3:
						service.buyProduct();
						break;
					case 4:
						service.billing();
						break;
					case 5:
						System.exit(0);
						break;
					default:
						System.out.println("wrong input");
					}
				} else {
					try {
						throw new WrongInputException("wrong option");
					} catch (WrongInputException e) {
						System.out.println(e.getMessage());
					}
				}

			} catch (InputMismatchException e) {
				try {
					throw new WrongInputException("Wrong input");
				} catch (WrongInputException w) {
					System.out.println(w.getMessage());
				}
			}
		} while (option != 5);

	}
}