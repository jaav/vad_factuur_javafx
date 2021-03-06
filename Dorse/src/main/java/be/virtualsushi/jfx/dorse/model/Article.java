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

  public static String DEFAULT_COLUMN = "code";
  public static Boolean DEFAULT_ASC = Boolean.TRUE;

	private Long articleType;

	@NotBlank
	private String code;

	private String articleName;

	private String description;

	private Float price;

 	private Integer freeQuantity;

 	private Stock stock;

	private Long unit;

	@NotNull
	private Integer weight;

	private Date creationDate;

 	private Date copyDate;

	@NotNull
	private Long supplier;

	@NotNull
	private Long creator;

	private Float vat;

  @JsonProperty("article_type")
 	@NotNull
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

  @JsonProperty("name")
	@NotBlank
  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

 	@NotNull
	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

  @JsonProperty("freeQuantity")
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

  @JsonProperty("create_date")
 	@JsonSerialize(using = CustomDateSerializer.class)
 	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

  @JsonProperty("copyDate")
 	@JsonSerialize(using = CustomDateSerializer.class)
 	@JsonDeserialize(using = CustomDateDeserializer.class)
  public Date getCopyDate() {
    return copyDate;
  }

  public void setCopyDate(Date copyDate) {
    this.copyDate = copyDate;
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

  @JsonSerialize(using = CustomFloatSerializer.class)
  @JsonDeserialize(using = CustomFloatDeserializer.class)
  public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

  @JsonProperty("stock")
  public Stock getStock() {
    return stock;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  @Override
	public String getPrintName() {
		return getCode() + (StringUtils.isNotBlank(getArticleName()) ? " - " + getArticleName() : "");
	}

	@Override
	public String toString() {
		return getCode();
	}

}
