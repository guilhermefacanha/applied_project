package core.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import core.util.UtilFunctions;

@FacesConverter(value = "boolean")
public class Boolean implements Converter
{
	public Object getAsObject(FacesContext faces, UIComponent componente, String value)
	{
		return value;
//		try {
//			if(value.equals("true"))
//				return new Double(1);
//		} catch (Exception e) {
//		}
//		
//		return new Double(0);
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
	{
		if (UtilFunctions.isNullEmpty(arg2.toString()) || arg2.toString().equals("false"))
			return false+"";
		else
			return true+"";

	}
}