package org.lab.webcrawler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.jsoup.HttpStatusException;
import org.lab.webcrawler.dao.RentPropertyDAO;
import org.lab.webcrawler.entity.RentProperty;
import org.lab.webcrawler.util.UtilFunction;

public class CraiglistRentalService {
	
	static final String homeDirectory = System.getProperty("user.dir");
	static final String resultDirectory = Paths.get(homeDirectory, "result").toString();
	
	private RentPropertyDAO dao = new RentPropertyDAO();
	
	public void getDatabaseSize() {
		long size = dao.getPropertiesSize();
		System.out.println("Number of properties: "+size);
	}
	
	
	public void crawRentalPropertiesAndUpdateDatabase() {
		String baseUrl = "https://vancouver.craigslist.org";
		String url = "https://vancouver.craigslist.org/d/apts-housing-for-rent/search/apa";
		String attr = "href";
		String pattern = "apa/d";
		String patternNext = "/search/apa?s=";
		List<String[]> structuredData = new ArrayList<>();

		WebCrawler crawler = new WebCrawler();
		List<String> visited = new ArrayList<>();
		while (!url.isEmpty() && !visited.contains(url)) {
			try {
				System.out.println("=========================");
				System.out.println("Loading web crawler for: " + url);
				System.out.println("=========================");
				crawler.load(url);

				System.out.println("");
				System.out.println("");
				System.out.println("=========================");
				System.out.println("Links Next: ");
				List<String> nextLinks = crawler.getLinks(attr, patternNext);
				nextLinks.forEach(l -> System.out.println(l));

				List<String[]> list = crawler.getStructuredData();
				structuredData.addAll(list);

				visited.add(url);
				if (nextLinks != null && nextLinks.size() > 0)
					url = baseUrl + (nextLinks.size() > 1 ? nextLinks.get(1) : nextLinks.get(0));
				else {
					url = "";
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Process finished. Visited urls (" + visited.size() + "):");
		visited.forEach(v -> System.out.println(v + "\n"));

//		printSaveStructuredData(structuredData);
		List<RentProperty> properties = getPropertiesFromStructuredData(structuredData);
//		properties.forEach(p-> System.out.println(p.toString()));
		dao.saveAll(properties);
	}
	
	public void updateCreationDate() {
		List<RentProperty> savedProperties = dao.getPropertiesWithLinkCreationNull ();
		WebCrawler crawler = new WebCrawler();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		int count = 0;
		int size = savedProperties.size();
		for (RentProperty p : savedProperties) {
			try {
				crawler.load(p.getLink());
				String dateStr = crawler.getPostingDate();
				if(!dateStr.isEmpty()) {
					Date date = dateFormat.parse(dateStr);
					if(p.getCreationDate()== null || p.getCreationDate().after(date)) {
						p.setCreationDate(date);
						dao.updateCreationDate(p);
						System.out.println(p.getId()+" - updated creation date");
					}
				}
			} catch (Exception e) {
				System.out.println("Error updating full description for id: "+p.getId());
				e.printStackTrace();
			}
			
			count++;
			printProgress(count,size);
		}
		
	}
	
	public void removeNumberDaysSoldDate() {
		dao.removeNumberDaysSoldDate();
	}

	private void printProgress(double count, double size) {
		if(count%100==0) {
			double percentage = count/size * 100;
			System.out.println("Processed "+count+"/"+size+" ("+NumberFormat.getInstance().format(percentage)+"%)");
		}
	}

	public void updateFullDescriptions() throws IOException {
		List<RentProperty> savedProperties = dao.getPropertiesNullDescription();
		int count = 0;
		int size = savedProperties.size();
		if(savedProperties!=null && savedProperties.size()>0) {
			WebCrawler crawler = new WebCrawler();
			for (RentProperty p : savedProperties) {
				try {
					crawler.load(p.getLink());
					String fullDescription = crawler.getFullDescription();
					if(fullDescription!= null && !fullDescription.isEmpty() && (p.getFullDescription()==null || !p.getFullDescription().equals(fullDescription))) {
						p.setFullDescription(fullDescription);
						dao.updateFullDescription(p);
						System.out.println(p.getId()+" - updated description");
					}
				} catch (Exception e) {
					System.out.println("Error updating full description for id: "+p.getId());
					e.printStackTrace();
				}
				
				count++;
				printProgress(count,size);
			}
		}
		else
			System.out.println("Great! Nothing to update!");
	}
	
	public void updateRentedProperties() {
		List<RentProperty> savedProperties = dao.getPropertiesWithLinkNotRented();
		WebCrawler crawler = new WebCrawler();
		int count = 0;
		int size = savedProperties.size();
		for (RentProperty p : savedProperties) {
			try {
				crawler.load(p.getLink());
				if(crawler.isRented()) {
					DateTime creation = new DateTime(p.getCreationDate());
					DateTime rented = new DateTime(new Date());
					Days daysBetween = Days.daysBetween(creation, rented);
					p.setSoldDate(new Date());
					p.setNumberDays(daysBetween.getDays());
					dao.updateRentDate(p);
				}
				else if(crawler.isFlagged()) {
					System.out.println(p.getId()+" property flagged to remove!");
					dao.remove(p);
				}
			} 
			catch (HttpStatusException e) {
				DateTime creation = new DateTime(p.getCreationDate());
				DateTime rented = new DateTime(new Date());
				Days daysBetween = Days.daysBetween(creation, rented);
				p.setSoldDate(new Date());
				p.setNumberDays(daysBetween.getDays());
				dao.updateRentDate(p);
			}
			catch (Exception e) {
				System.out.println("Error updating full description for id: "+p.getId());
				e.printStackTrace();
			}
			
			count++;
			printProgress(count,size);
		}
	}

	private List<RentProperty> getPropertiesFromStructuredData(List<String[]> structuredData) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		List<RentProperty> list = new ArrayList<>(structuredData.size());
		for (String[] data : structuredData) {
			Date creationDate = null;
			try {
				creationDate = dateFormat.parse(data[5]);
			} catch (Exception e) {
				creationDate = null;
			}
			list.add(
					RentProperty.builder()
					.id(Long.parseLong(data[0]))
					.price(getPrice(data[1]))
					.title(data[2])
					.characteristics(data[3])
					.link(data[4])
					.creationDate(creationDate)
					.build()
					);
		}
		return list;
	}

	private double getPrice(String price) {
		if(price.isEmpty())
			return 0;
		try {
			return Double.parseDouble(price.replace("$", ""));
		} catch (Exception e) {
			return 0;
		}
	}

	private void printSaveStructuredData(List<String[]> structuredData) throws IOException {
		System.out.println("");
		System.out.println("");
		System.out.println("=========================");
		System.out.println("Structured Data (" + structuredData.size() + "): ");
		String format = "%-15s %-15s %-85s %-20s %-20s";
		String[] header = { "ID", "Price", "Title", "Characteristics", "Link" };

		System.out.println(String.format(format, header));
		System.out.println(String.format(format, "---------", "---------", "-----------------------------",
				"-----------------------", "-----------------------"));
		structuredData.forEach(l -> System.out.println(String.format(format, l)));

		String path = resultDirectory + File.separator + "result.csv";
		UtilFunction.saveCsvFile(path, structuredData, header);
	}

}
