package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 11/11/12
 * Time: 10:48
 */
public class CustomerResponse extends ServerResponse{
  @JsonProperty("data")
  private List<Customer> data;

  public List<Customer> getData() {
    return data;
  }

  public void setData(List<Customer> customers) {
    this.data = customers;
  }
}
