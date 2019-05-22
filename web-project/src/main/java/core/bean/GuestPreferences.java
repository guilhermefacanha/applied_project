package core.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class GuestPreferences implements Serializable {

	private static final long serialVersionUID = 1072206046208755994L;

	private String menuMode = "layout-slim";

    private String theme = "bluegrey-teal";

    private String menuColor = "layout-menu-light";

    private String topBarColor = "layout-topbar-bluegrey";

    private String logo = "logo-olympia-white";

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
}
