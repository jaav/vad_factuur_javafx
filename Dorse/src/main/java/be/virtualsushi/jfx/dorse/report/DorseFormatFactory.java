package be.virtualsushi.jfx.dorse.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.jasperreports.engine.util.DefaultFormatFactory;

public class DorseFormatFactory extends DefaultFormatFactory {

	private final ResourceBundle resources;

	public DorseFormatFactory(ResourceBundle resources) {
		super();
		this.resources = resources;
	}

	@Override
	public DateFormat createDateFormat(String pattern, Locale locale, TimeZone tz) {
		return new SimpleDateFormat(resources.getString("date.format"));
	}

}
