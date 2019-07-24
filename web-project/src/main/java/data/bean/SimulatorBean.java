package data.bean;

import java.io.Serializable;
import java.util.Collections;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lab.webcrawler.dao.RentPropertyDAO;
import org.lab.webcrawler.entity.RentProperty;
import org.lab.webcrawler.service.WebCrawler;

import com.google.gson.Gson;

import core.config.AppConfig;
import core.util.OkHttpTrustAll;
import core.util.UtilFunctions;
import lombok.Getter;
import lombok.Setter;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

@ViewScoped
@Named
public class SimulatorBean implements Serializable {

	private static final long serialVersionUID = -2604175097122520038L;
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	
	RentPropertyDAO dao = new RentPropertyDAO();

	@Getter
	@Setter
	private RentProperty property = new RentProperty();

	@Getter
	@Setter
	private String city;

	@Getter
	@Setter
	private String url;

	@Getter
	String output;

	@Getter
	@Setter
	private boolean professionally_managed;
	@Getter
	@Setter
	private boolean no_pet_allowed;
	@Getter
	@Setter
	private boolean suit_laundry;
	@Getter
	@Setter
	private boolean park_stall;
	@Getter
	@Setter
	private boolean available_now;
	@Getter
	@Setter
	private boolean amenities;
	@Getter
	@Setter
	private boolean brand_new;
	@Getter
	@Setter
	private boolean basement;
	@Getter
	@Setter
	private boolean furnished;

	public void getSimulation() {
		testGetMethod();

	}
	
	public void loadData() {
		try {
			if(UtilFunctions.isNullEmpty(url))
				throw new Exception("Enter a URL to load the data from!");
			
			try {
				long pid = Long.parseLong(url);
				if(pid>0) {
					property = dao.getPropertyById(pid);
				}
			} catch (Exception e) {
				try {
					property = dao.getPropertyByLink(url);
				} catch (Exception e2) {
				}
			}
			
			if(property==null || property.getId()<=0) {
				property = new RentProperty();
				WebCrawler crawler = new WebCrawler();
				crawler.load(url);
				String fullDescription = crawler.getFullDescription();
				if(!UtilFunctions.isNullEmpty(fullDescription))
					property.setFullDescription(fullDescription);
				
				Elements price = crawler.getElements("span[class=price]");
				if(price!=null && price.size()>0)
					this.property.setPrice(Double.parseDouble(price.get(0).text().replace("$", "")));
				Elements elements = crawler.getElements("span[class=shared-line-bubble]");
				for (Element element : elements) {
					String text = element.text();
					if(text.toUpperCase().contains("/")) {
						String[] split = text.split("/");
						for (String string : split) {
							if(string.toUpperCase().contains("R")) {
								String bdrm = string.replaceAll("[^\\d.]", "");
								this.property.setBedrooms(Double.parseDouble(bdrm));
							}else {
								String bath = string.replaceAll("[^\\d.]", "");
								this.property.setBath(Double.parseDouble(bath));
								
							}
						}
					}
					else if(text.toUpperCase().contains("FT")) {
						text = text.toUpperCase();
						String size = text.replaceAll("FT2", "").replaceAll("[^\\d.]", "");
						this.property.setSize_sqft(Double.parseDouble(size));
						
					}
				}
			}
			else {
				loadCharacteristicsFromProperty();
			}
			
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}
	}

	private void loadCharacteristicsFromProperty() {
		if(this.property.getProfessionally_managed()==1)
			professionally_managed = true;
		if(this.property.getNo_pet_allowed()==1)
			no_pet_allowed = true;
		if(this.property.getSuit_laundry()==1)
			suit_laundry = true;
		if(this.property.getPark_stall()==1)
			park_stall = true;
		if(this.property.getAvailable_now()==1)
			available_now = true;
		if(this.property.getAmenities()==1)
			amenities = true;
		if(this.property.getBrand_new()==1)
			brand_new = true;
		if(this.property.getNo_basement()==1)
			basement = false;
		else
			basement = true;
		
		if(this.property.getLoc_vancouver()==1)
			city = "vancouver";
		else if(this.property.getLoc_burnaby()==1)
			city = "burnaby";
		else if(this.property.getLoc_richmond()==1)
			city = "richmond";
		else if(this.property.getLoc_newwest()==1)
			city = "newwest";
		else if(this.property.getLoc_surrey()==1)
			city = "surrey";
		else if(this.property.getLoc_abbotsford()==1)
			city = "abbot";
		else
			city = "other";
	}

	public void simulateRent() {
		if (professionally_managed)
			this.property.setProfessionally_managed(1);
		if (no_pet_allowed)
			this.property.setNo_pet_allowed(1);
		if (suit_laundry)
			this.property.setSuit_laundry(1);
		if (park_stall)
			this.property.setPark_stall(1);
		if (available_now)
			this.property.setAvailable_now(1);
		if (amenities)
			this.property.setAmenities(1);
		if (brand_new)
			this.property.setBrand_new(1);
		if (basement)
			this.property.setNo_basement(0);
		else
			this.property.setNo_basement(1);
		
		if (furnished)
			this.property.setFurnished(1);;

		switch (city) {
		case "vancouver":
			this.property.setLoc_vancouver(1);
			break;
		case "burnaby":
			this.property.setLoc_burnaby(1);
			break;
		case "richmond":
			this.property.setLoc_richmond(1);
			break;
		case "newwest":
			this.property.setLoc_newwest(1);
			break;
		case "surrey":
			this.property.setLoc_surrey(1);
			break;
		case "abbot":
			this.property.setLoc_abbotsford(1);
			break;
		default:
			this.property.setLoc_other(1);
			break;
		}

		getSimulatedModels();

	}

	private void getSimulatedModels() {
		try {
			this.property.resetPrices();
			ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
				    .tlsVersions(TlsVersion.TLS_1_2)
				    .cipherSuites(
				          CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
				          CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
				          CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
				    .build();

			OkHttpClient pre = new OkHttpClient.Builder()
				    .connectionSpecs(Collections.singletonList(spec))
				    .build();
			
			OkHttpClient client = OkHttpTrustAll.trustAllSslClient(pre);
			
			String json = property.getJson();
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(AppConfig.getServiceURL() + "simulate").post(body).build();
			Response response = client.newCall(request).execute();
			String responseStr = response.body().string();
			output = new JSONObject(responseStr).toString(3).replace("\n", "<br/>");
			JSONObject jsonobj = new JSONObject(responseStr);
			if (jsonobj.has("0")) {
				JSONObject p = jsonobj.getJSONObject("0");
				property = new Gson().fromJson(p.toString(), RentProperty.class);
			}

			this.property.resetProperties();
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}

	}

	private void testGetMethod() {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(AppConfig.getServiceURL() + "test").build();
			Response response = client.newCall(request).execute();
			output = response.body().string();
			output = new JSONObject(output).toString(2).replace("\n", "<br/>");
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}
	}

}
