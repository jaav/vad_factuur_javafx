package be.virtualsushi.jfx.dorse.model;

import java.util.List;

public class Customer extends BaseEntity implements Listable {

	private String name;

	private String vat;

	private String iban;

	private String remark;

	private Long sector;

	private Long subsector;

	private List<Address> address;

	private List<Person> person;

	public Customer() {

	}

	public Customer(Integer id) {
		setId(id.longValue());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public Long getSubsector() {
		return subsector;
	}

	public void setSubsector(Long subsector) {
		this.subsector = subsector;
	}

	@Override
	public String getPrintName() {
		return getName();
	}

	@Override
	public String toString() {
		return getPrintName();
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<Person> getPerson() {
		return person;
	}

	public void setPerson(List<Person> person) {
		this.person = person;
	}

}
