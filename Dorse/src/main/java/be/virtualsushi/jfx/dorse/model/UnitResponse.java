package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 11/11/12
 * Time: 10:48
 */
public class UnitResponse extends ServerResponse{
  @JsonProperty("data")
  private List<Unit> data;

  public List<Unit> getData() {
    return data;
  }

  public void setData(List<Unit> units) {
    this.data = units;
  }
}
