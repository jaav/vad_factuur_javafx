package be.virtualsushi.jfx.dorse.model;

public class Sector extends BaseEntity {

	private String name;
	private Long parent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

}
