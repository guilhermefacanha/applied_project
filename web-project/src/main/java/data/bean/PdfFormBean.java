package data.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import core.util.UtilFunctions;
import data.services.PdfFormService;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class PdfFormBean implements Serializable{
	
	private static final long serialVersionUID = 1047161817775711044L;

	@Inject PdfFormService pdfService;
	
	@Getter @Setter
	private String manufacturer;
	@Getter @Setter
	private String color;
	@Getter @Setter
	private String plate;
	@Getter @Setter
	private String observation;
	@Getter @Setter
	private boolean test = true;;
	
	public void generateForm() {
		try {
			pdfService.generateParkingForm(manufacturer, color, plate, observation, test);
			UtilFunctions.adicionarMsg("Form genereated. Check the email for the pdf", false);
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}
	}

}
