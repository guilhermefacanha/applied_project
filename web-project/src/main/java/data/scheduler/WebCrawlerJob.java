package data.scheduler;

import java.io.IOException;
import java.util.Date;

import org.lab.webcrawler.service.CraiglistRentalService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import core.util.UtilFunctions;

public class WebCrawlerJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		String format = "MMM dd, yyyy HH:mm";
		System.out.println("============================================");
		System.out.println("EXECUTING WEB CRAWLER JOB STARTED AT "+UtilFunctions.getStringDeData(new Date(), format));
		System.out.println("============================================");

		CraiglistRentalService craigService = new CraiglistRentalService();
		craigService.getDatabaseSize();
		craigService.crawRentalPropertiesAndUpdateDatabase();
		try {
			craigService.updateFullDescriptions();
		} catch (IOException e) {
			System.out.println("ERROR in method updateFullDescriptions: " + e.getMessage());
		}
		craigService.updateCreationDate();
		craigService.updateRentedProperties();
		craigService.getDatabaseSize();
		System.out.println("============================================");
		System.out.println("EXECUTING WEB CRAWLER JOB ENDED AT "+UtilFunctions.getStringDeData(new Date(), format));
		System.out.println("============================================");
	}
}