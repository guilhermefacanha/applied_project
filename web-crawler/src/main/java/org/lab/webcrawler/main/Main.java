package org.lab.webcrawler.main;

import java.io.IOException;
import java.util.Scanner;

import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.service.CraiglistRentalService;

public class Main {

	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		CraiglistRentalService craigService = new CraiglistRentalService();
		printMenu();
		String op = getOperation("Selected Option: ");
		while (!op.equals("0")) {
			if (op.equals("1")) {
				craigService.crawRentalPropertiesAndUpdateDatabase();
			} else if (op.equals("2")) {
				craigService.updateFullDescriptions();
			} else if (op.equals("3")) {
				craigService.updateCreationDate();
			}else if (op.equals("4")) {
				craigService.updateRentedProperties();
			}else if (op.equals("5")) {
				craigService.getDatabaseSize();
			}
			

			printMenu();
			op = getOperation("Selected Option: ");
		}

		scan.close();
//		ResourcesDAO.dispose();
		System.out.println("System Finished!");
	}

	private static String getOperation(String msg) {
		System.out.println(msg);
		return scan.nextLine();
	}

	private static void printMenu() {
		System.out.println("========                  ========");
		System.out.println("======== Data WebCrawler  ========");
		System.out.println("========                  ========");

		System.out.println("1 - Crawl Craiglist and Update Database");
		System.out.println("2 - Update Full Description for Properties");
		System.out.println("3 - Update Creation Date for Properties");
		System.out.println("4 - Update Rented Properties");
		System.out.println("5 - Database Size");
		System.out.println("0 - Exit");

	}

}
