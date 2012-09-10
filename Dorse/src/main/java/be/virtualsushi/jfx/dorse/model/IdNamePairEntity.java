package be.virtualsushi.jfx.dorse.model;

public class IdNamePairEntity extends BaseEntity implements Listable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getPrintName() {
		return getName();
	}

}
