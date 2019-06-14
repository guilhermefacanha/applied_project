package data.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.json.JSONObject;
import org.lab.webcrawler.entity.RentProperty;

import com.google.gson.Gson;

import core.util.UtilFunctions;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ViewScoped
@Named
public class SimulatorBean implements Serializable {

	private static final long serialVersionUID = -2604175097122520038L;
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	@Getter
	@Setter
	private RentProperty property = new RentProperty();

	@Getter
	@Setter
	private String city;

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

	private String restPath = "http://158.69.217.148:5000/";

	public void getSimulation() {
		testGetMethod();

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
			break;
		}

		getSimulatedModels();

	}

	private void getSimulatedModels() {
		try {
			this.property.resetPrices();
			OkHttpClient client = new OkHttpClient();
			String json = property.getJson();
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(restPath + "simulate").post(body).build();
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
			Request request = new Request.Builder().url(restPath + "test").build();
			Response response = client.newCall(request).execute();
			output = response.body().string();
			output = new JSONObject(output).toString(2).replace("\n", "<br/>");
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}
	}

}
