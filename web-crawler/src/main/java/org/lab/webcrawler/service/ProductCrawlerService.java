package org.lab.webcrawler.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lab.webcrawler.util.UtilFunction;
import org.lab.webcrawler.util.FileUtil;

public class ProductCrawlerService {
	public JSONArray fetchProductsCraiglist(String url, Long productId, int qty) throws IOException {
		JSONArray list = new JSONArray();

		WebCrawler crawler = new WebCrawler();
		crawler.load(url);

		Elements elements = crawler.getElements("li[class=result-row]");

		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String title = crawler.searchValue(element, "a[class=result-title hdrlnk]");
			String price = crawler.searchValue(element, "span[class=result-price]");
			String urlDetail = crawler.searchAttr(element, "a[class=result-title hdrlnk]", "href");
			String date = crawler.searchAttr(element, "time[class=result-date]", "datetime");
			crawler.loadSecond(urlDetail);
			String description = getDescriptioFromDetailUrl(
					crawler.getElementsSecond("section[id=postingbody]"));
			String image = crawler.searchAttr(crawler.getElementsSecond("head").get(0), "meta[property=og:image]", "content");

			if (title != null && price != null && image != null) {
				JSONObject json = new JSONObject();
				json.put("product", productId);
				json.put("title", title);
				json.put("description", description);
				json.put("price", price.replace("&nbsp;", "").replace("$", "").trim());
				json.put("date", date);
				json.put("image", image);
				if(!UtilFunction.isNullEmpty(image))
					json.put("imagefile", FileUtil.saveImage(image));

				list.put(json);
			}

			if (list.length() >= qty)
				return list;
		}

		return list;
	}

	private String getDescriptioFromDetailUrl(Elements elements) throws IOException {
		try {
			return elements.text().replace("QR Code Link to This Post", "").trim();
		} catch (Exception e) {
		}

		return "";

	}
}
