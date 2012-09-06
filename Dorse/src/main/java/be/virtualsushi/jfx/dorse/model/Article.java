package be.virtualsushi.jfx.dorse.model;

import java.util.Date;

public class Article extends BaseEntity {

	private Long articleType;
	private String code;
	private String name;
	private String description;
	private Float price;
	private Long unit;
	private Integer weight;
	private Date creationDate;
	private Long supplier;
	private Long creator;
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

}
