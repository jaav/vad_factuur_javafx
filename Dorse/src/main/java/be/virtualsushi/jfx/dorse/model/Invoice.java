package be.virtualsushi.jfx.dorse.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateDeserializer;
import be.virtualsushi.jfx.dorse.model.jsonserialization.CustomDateSerializer;

public class Invoice extends BaseEntity {

  public static String DEFAULT_COLUMN = "id";
  public static Boolean DEFAULT_ASC = Boolean.FALSE;

	@NotNull
	private Customer customer;

	@JsonProperty("inv_address")
	@NotNull
	private Long invoiceAddress;

	@JsonProperty("del_address")
	private Long deliveryAddress;

	@NotBlank
	private String code;

	private String remark;

	private Float shipping;

  private Float products;

	private Float total;

	private Float vat;

	private Date creationDate;

	@JsonProperty("delivery_date")
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date deliveryDate;

	@JsonProperty("paid_date")
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date paidDate;

	private Float weight;

	private Integer status;

	private Long creator;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(Long invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Long getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Long deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Float getShipping() {
		return shipping == null ? 0 : shipping;
	}

	public void setShipping(Float shipping) {
		this.shipping = shipping;
	}

  public Float getProducts() {
    return products==null ? 0 : products;
  }

  public void setProducts(Float products) {
    this.products = products;
  }

  public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

  @JsonProperty("creation_date")
 	@JsonSerialize(using = CustomDateSerializer.class)
 	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

  @JsonIgnore
  public String getTotalPrice(){
    return ""+(getShipping()+getProducts());
  }
}
