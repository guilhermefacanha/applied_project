package data.services;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender implements Serializable{

	private static final long serialVersionUID = -9215574277075248081L;

	public void sendForm(String filePath, boolean test) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("guilhermefacanhav", "Maximo10");
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("guilhermefacanha@gmail.com"));
			if(!test)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("dcorize.office@gmail.com"));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("gpaculdino@gmail.com"));
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("guilhermefacanha@gmail.com"));
			message.setSubject("[Unit 703] Form K01 Vehicle Parking Notification");
			message.setText("Test Mail," + "\n\n please do not reply!");

            // Set the email msg text.
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText("Hello," + 
            			"\n\nplease see attached the K01 form for Vehicle Parking for Unit 703"
            			+ "\n\nRegards "
            			+ "\nGuilherme Facanha"
            			+ "\n(604) 700-8796");

            // Set the email attachment file
            File f = new File(filePath);
            FileDataSource fileDataSource = new FileDataSource(f);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(fileDataSource.getName());

            // Create Multipart E-Mail.
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}