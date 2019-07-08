package data.services;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import core.util.UtilFunctions;

public class PdfFormService implements Serializable{

	private static final long serialVersionUID = -8581480006475383131L;
	
	@Inject MailSender sender;
	
	public String generateParkingForm(String txtmanufacturer, String txtcolor, String txtplate, String txtobservation, boolean test)
			throws Exception {
		String outputDir = System.getProperty("java.io.tmpdir");
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("parking_form.pdf").getFile());
		String outPath = "";
		
		try (PDDocument pdfDocument = PDDocument.load(file)) {
			// get the document catalog
			PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

			// as there might not be an AcroForm entry a null check is necessary
			if (acroForm != null) {
				// Retrieve an individual field and set its value.
				PDTextField date1 = (PDTextField) acroForm.getField("date1");
				PDTextField date2 = (PDTextField) acroForm.getField("date2");
				PDTextField manufacturer = (PDTextField) acroForm.getField("manufacturer");
				PDTextField color = (PDTextField) acroForm.getField("color");
				PDTextField plate = (PDTextField) acroForm.getField("plate");
				PDTextField observation = (PDTextField) acroForm.getField("observation");

				String date = UtilFunctions.getStringFromDate(new Date(), "MMM dd, yyyy");
				date1.setValue(date);
				date2.setValue(date);

				manufacturer.setValue(txtmanufacturer);
				color.setValue(txtcolor);
				plate.setValue(txtplate);
				observation.setValue(txtobservation);

				acroForm.flatten();

				outPath = outputDir + "/Unit 703 - Form K01 Vehicle Parking "+date+".pdf";
				File outFile = new File(outPath);

				// Save and close the filled out form.
				pdfDocument.save(outFile);
				
				System.out.println("parking pdf generated at: "+outPath);
				
				sender.sendForm(outPath,test);
			}

		} catch (Exception e) {
			throw new Exception("Error generating parking pdf form: " + e.getMessage());
		}
		
		return outPath;
	}

}
