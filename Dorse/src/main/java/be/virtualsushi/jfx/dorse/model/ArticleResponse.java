package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 11/11/12
 * Time: 10:48
 */
public class ArticleResponse extends ServerResponse{

  @JsonProperty("data")
  private List<Article> data;

  @Override
  public List<Article> getData() {
    return data;
  }

  public void setData(List<Article> data) {
    this.data = data;
  }
}
