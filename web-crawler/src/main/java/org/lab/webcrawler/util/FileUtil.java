package org.lab.webcrawler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	public static String saveImage(String imageUrl) throws IOException {
		String dir = System.getProperty("java.io.tmpdir") + File.separator + "img";
		File fDir = new File(dir);
		fDir.mkdirs();

		URL url = new URL(imageUrl);
		String fileName = url.getFile();
		String destName = dir + File.separator + fileName.substring(fileName.lastIndexOf("/"));
		System.out.println(destName);

		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destName);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
		
		return destName;
	}
	
	public static String saveJson(String json, String name) throws IOException {
		String dir = System.getProperty("java.io.tmpdir") + File.separator + "json";
		File fDir = new File(dir);
		fDir.mkdirs();
		
		String pathname = dir+File.separator+name;
		File f = new File(pathname);
		
		FileUtils.writeStringToFile(f, json,"UTF-8");
		return pathname;
	}

}
