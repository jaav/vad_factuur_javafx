package be.virtualsushi.jfx.dorse.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomFloatDeserializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomFloatSerializer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateDeserializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateSerializer;

public class Article extends BaseEntity implements Listable {

	@JsonProperty("article_type")
	@NotNull
	private Long articleType;

	@NotBlank
	private String code;

	@NotBlank
	private String name;

	private String description;

	@JsonProperty("listPrice")
	@NotNull
	private Float price;

  @JsonProperty("freeQuantity")
 	@NotNull
 	private Integer freeQuantity;

  @JsonProperty("stock")
 	private Stock stock;

	private Long unit;

	@NotNull
	private Integer weight;

	@JsonProperty("create_date")
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@NotNull
	private Date creationDate;

	@NotNull
	private Long supplier;

	@NotNull
	private Long creator;

	@NotNull
  @JsonSerialize(using = CustomFloatSerializer.class)
 	@JsonDeserialize(using = CustomFloatDeserializer.class)
	private Float vat;

	public Long getArticleType() {
		return articleType;
	}

	public void setArticleType(Long articleType) {
		this.articleType = articleType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

  public Integer getFreeQuantity() {
    return freeQuantity;
  }

  public void setFreeQuantity(Integer freeQuantity) {
    this.freeQuantity = freeQuantity;
  }

  public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getSupplier() {
		return supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

  public Stock getStock() {
    return stock;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  @Override
	public String getPrintName() {
		return getCode() + (StringUtils.isNotBlank(getName()) ? " - " + getName() : "");
	}

	@Override
	public String toString() {
		return getCode();
	}

}
