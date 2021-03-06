package be.virtualsushi.jfx.dorse.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class Customer extends BaseEntity implements Listable {

  public static String DEFAULT_COLUMN = "name";
  public static Boolean DEFAULT_ASC = Boolean.TRUE;

	@NotBlank
	private String name;

	private String vat;

	private String iban;

	private String remark;

	@NotNull
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
    if(address!=null && !address.isEmpty()) return getName() + " (" + address.get(0).getCity() + ")";
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

  public static Customer getEmptyCustomer(){
    Customer empty = new Customer();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }

}
