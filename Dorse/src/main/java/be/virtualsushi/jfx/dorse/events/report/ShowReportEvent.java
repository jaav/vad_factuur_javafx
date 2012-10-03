package be.virtualsushi.jfx.dorse.events.report;

public class ShowReportEvent {

	private final String reportUri;

	public ShowReportEvent(String reportUri) {
		this.reportUri = reportUri;
	}

	public String getReportUri() {
		return reportUri;
	}

}
