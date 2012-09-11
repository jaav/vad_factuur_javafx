package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import be.virtualsushi.jfx.dorse.restapi.ListResourcePath;

@ListResourcePath("addresses")
public class Address extends BaseEntity {

	private Long customer;

	@JsonProperty("address_type")
	private Integer addressType;

	private String address;

	private Long location;

	@JsonProperty("tel")
	private String phone;

	private String fax;

	private String email;

	private String city;

	private String zipcode;

	public Long getCustomer() {
		return customer;
	}

	public void setCustomer(Long customer) {
		this.customer = customer;
	}

	public Integer getAddressType() {
		return addressType;
	}

	public void setAddressType(Integer addressType) {
		this.addressType = addressType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getLocation() {
		return location;
	}

	public void setLocation(Long location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

}
