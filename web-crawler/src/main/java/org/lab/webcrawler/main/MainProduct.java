package org.lab.webcrawler.main;

import org.json.JSONArray;
import org.lab.webcrawler.service.ProductCrawlerService;
import org.lab.webcrawler.util.FileUtil;

public class MainProduct {
	private static final int QTY = 40;
	private static final Long PRODUCT_ID = new Long(54);
	private static final String URL = "https://vancouver.craigslist.org/search/tla?query=power%20tools";
	private static final String JSON_FILE = "product_"+PRODUCT_ID+".json";

	public static void main(String[] args) {
		System.out.println("=================================");
		System.out.println("GET PRODUCTS");
		System.out.println("=================================");

		ProductCrawlerService service = new ProductCrawlerService();

		try {
			JSONArray products = service.fetchProductsCraiglist(URL, PRODUCT_ID, QTY);
			System.out.println(products.toString(4));
			String saveJson = FileUtil.saveJson(products.toString(4), JSON_FILE);
			System.out.println("Products generated at: " + saveJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
