package be.virtualsushi.jfx.dorse.model;

import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateDeserializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateSerializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomFloatDeserializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomFloatSerializer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Post extends BaseEntity implements Listable {

  @NotBlank
	private String name;

  @NotNull
	private Float price;

  @NotNull
	private Float bottom;

  @NotNull
	private Float top;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getBottom() {
    return bottom;
  }

  public void setBottom(Float bottom) {
    this.bottom = bottom;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Float getTop() {
    return top;
  }

  public void setTop(Float top) {
    this.top = top;
  }

  @Override
  public String getPrintName() {
    return getName();
  }
}
