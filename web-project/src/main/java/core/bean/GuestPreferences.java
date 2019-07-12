package core.bean;

import java.io.InputStream;
import java.io.Serializable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

@Named
@SessionScoped
public class GuestPreferences implements Serializable {

	private static final long serialVersionUID = 1072206046208755994L;

	private String menuMode = "layout-slim";

	private String theme = "bluegrey-teal";

	private String menuColor = "layout-menu-light";

	private String topBarColor = "layout-topbar-bluegrey";

	private String logo = "logo-olympia-white";
	
	Attributes attr;

	@PostConstruct
	public void loadManifest() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			InputStream inputStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(inputStream);
			attr = manifest.getMainAttributes();
		} catch (Exception e) {
			System.out.println("Unable to load manifest "+e.getMessage());
		}
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMenuMode() {
		return this.menuMode;
	}

	public void setMenuMode(String menuMode) {
		this.menuMode = menuMode;
	}

	public String getMenuColor() {
		return this.menuColor;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}

	public String getTopBarColor() {
		return this.topBarColor;
	}

	public void setTopBarColor(String topBarColor, String logo) {
		this.topBarColor = topBarColor;
		this.logo = logo;
	}

	public String getLogo() {
		return this.logo;
	}

	public String getProjectName() {
		try {
			return attr.getValue("project-name");
		} catch (Exception e) {
			return "";
		}
	}

	public String getProjectVersion() {
		try {
			return attr.getValue("project-version");
		} catch (Exception e) {
			return "0.0.1";
		}
	}

	public String getProjectDate() {
		try {
			return attr.getValue("project-date");
		} catch (Exception e) {
			return "";
		}
	}
}
