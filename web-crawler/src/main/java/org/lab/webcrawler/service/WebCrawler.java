package org.lab.webcrawler.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lab.webcrawler.util.UtilFunction;

public class WebCrawler {
	Document doc = null;
	Document doc2 = null;
	HashMap<String, String> cookies = new HashMap<>();
	String[] searchForCookies = new String[] {"session-id","session-id-time", "lc-acbca","x-wl-uid","x-acbca","at-acbca","sess-at-acbca","sst-acbca","i18n-prefs","session-token","csm-hit"}; 
	
	public void load(String url) throws IOException {
		doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
				.get();
	}
	
	public void loadSecond(String url) throws IOException {
		doc2 = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
				.cookies(cookies).get();
	}
	
	public String getHtml() {
		return doc.html();
	}
	
	public Elements getElements(String elementSearch) {
		return doc.select(elementSearch);
	}

	public Elements getElementsSecond(String elementSearch) {
		return doc2.select(elementSearch);
	}
	
	public List<String> getLinks(String attr, String pattern){
		List<String> links = new ArrayList<>();
		Elements elementsLinks = doc.getElementsByTag("a");
		
		for(Element link : elementsLinks) {
			String value = link.attr(attr);
			if(value.contains(pattern))
				links.add(value);
		}
		
		links = links.stream().distinct().collect(Collectors.toList());
		return links;
		
	}
	
	public List<String[]> getStructuredData(){
		List<String[]> structured = new ArrayList<>();
		
		Elements elements = doc.select("li[class=result-row]");
		for(Element element : elements) {
			String id = searchAttr(element,"li[class=result-row]", "data-pid");
			String price = searchValue(element,"span[class=result-price]");
			String title = searchValue(element,"a[class=result-title hdrlnk]");
			String hrefLink = searchAttr(element,"a[class=result-title hdrlnk]", "href");
			String characteristics = searchValue(element,"span[class=housing]");
			String date = getPostingDate();
			if(!UtilFunction.isNullEmpty(characteristics))
				characteristics = characteristics.replace("<sup>2</sup>", "_sq");
			
			String[] data = new String[] {id,price,title,characteristics,hrefLink,date};
			
			structured.add(data);
		}
		
		
		return structured;
	}
	
	public String getFullDescription() {
		String description = "";
		Elements elements = doc.select("section[id=postingbody]");
		for(Element element : elements) {
			description = element.text();
		}
		
		return description ;
	}
	
	public String getPostingDate() {
		String date = "";
		Elements elements = doc.select("p[class=postinginfo reveal]");
		for(Element element : elements) {
			date = searchAttr(element, "time[class=date timeago]", "datetime");
			if(!date.isEmpty())
				return date;
		}
		
		return date ;
		
	}

	public boolean isRented() {
		try {
			Elements elements = doc.select("div[class=removed]");
			for (Element element : elements) {
				if(element.text().contains("posting has been deleted"))
					return true;
			}
			
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}

	public boolean isFlagged() {
		try {
			Elements elements = doc.select("div[class=removed]");
			for (Element element : elements) {
				if(element.text().contains("flagged for removal"))
					return true;
			}
			
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}

	public String searchValue(Element element, String searchFor) {
		String value = "";
		Elements prices = element.select(searchFor);
		if(prices!=null && !prices.isEmpty()) {
			for(Element childElem : prices) {
				if(!UtilFunction.isNullEmpty(childElem.html()))
					value = childElem.html();
			}
		}
		return value;
	}
	
	public String searchAttr(Element element, String searchFor, String attribute) {
		String value = "";
		Elements prices = element.select(searchFor);
		if(prices!=null && !prices.isEmpty()) {
			for(Element childElem : prices) {
				value = childElem.attr(attribute);
			}
		}
		return value;
	}




}
