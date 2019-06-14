package data.bean;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import core.config.AppConfig;
import core.util.UtilFunctions;
import lombok.Getter;

@ViewScoped
@Named
public class ServerLogBean implements Serializable {
	private static final long serialVersionUID = -1638091364383505091L;

	final String serverLogPath = AppConfig.getServerLogPath();

	@Getter
	String log;

	public void initServerLog() {
		StringBuffer str = new StringBuffer("========================= <p>Server Log...</p> =========================");
		try {
			Path path = Paths.get(serverLogPath);
//			System.out.println("Server Log Path: "+serverLogPath);
//			System.out.println("File Path: "+path.toString());
			long lineCount = Files.lines(path).count();
			Stream<String> lines = Files.lines(path);
			int maxNoOfLines = 2500;
			if(lineCount>maxNoOfLines) {
				long start = lineCount - maxNoOfLines;
				lines.skip(start).forEach(l->{
					str.append(printLine(l));
				});
			}
			else
				lines.forEach(l->{
					str.append(printLine(l));
				});
			
			lines.close();
			
			UtilFunctions.adicionarMsg("Log loaded successfully", false);
				
		} catch (Exception e) {
			UtilFunctions.adicionarMsg(e.getMessage(), true);
		}
		
		log = str.toString();

	}

	private String printLine(String l) {
		if(l.contains("===="))
			return("<p class=\"blue\">"+l+"</p>");
		else if(l.contains("ERROR"))
			return("<p class=\"red\">"+l+"</p>");
		else
			return("<p>"+l+"</p>");
		
	}
}
