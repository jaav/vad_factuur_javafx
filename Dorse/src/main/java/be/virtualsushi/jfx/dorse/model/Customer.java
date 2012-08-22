package be.virtualsushi.jfx.dorse.model;

public class Customer extends BaseEntity {

	private String name;
	private String vat;
	private String iban;
	private String remark;
	private long sector;
	private long subsector;

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

	public long getSector() {
		return sector;
	}

	public void setSector(long sector) {
		this.sector = sector;
	}

	public long getSubsector() {
		return subsector;
	}

	public void setSubsector(long subsector) {
		this.subsector = subsector;
	}

}
