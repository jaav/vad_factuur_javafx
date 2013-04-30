package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 11/11/12
 * Time: 10:48
 */
public class StatsResponse {
	@JsonProperty("info")
	private MetaData metaData;

	@JsonProperty("data")
	private List<Stat> data;

	public List<Stat> getData() {
		return data;
	}

	public void setData(List<Stat> statLines) {
		this.data = statLines;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
}
