package be.virtualsushi.jfx.dorse.model;

import org.hibernate.validator.constraints.NotBlank;

public class IdNamePairEntity extends BaseEntity implements Listable {

	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getPrintName();
	}

	@Override
	public String getPrintName() {
		return getName();
	}



}
