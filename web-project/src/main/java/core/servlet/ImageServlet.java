package core.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.config.AppConfig;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = -3425621211956064201L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String requestedImage = request.getPathInfo();
			
			requestedImage = requestedImage.replace("\\", "/");
			
			if (requestedImage == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
				return;
			}
			
			// Decode the file name (might contain spaces and on) and prepare file object.
			File image = new File(AppConfig.getImagePath(), URLDecoder.decode(requestedImage, "UTF-8"));
			
			if (!image.exists()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
				return;
			}
			
			String contentType = getServletContext().getMimeType(image.getName());
			
			if (contentType == null || !contentType.startsWith("image")) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
				return;
			}
			
			response.reset();
			response.setContentType(contentType);
			response.setHeader("Content-Length", String.valueOf(image.length()));
			
			Files.copy(image.toPath(), response.getOutputStream());
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
    }

}