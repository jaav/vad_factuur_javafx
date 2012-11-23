package be.virtualsushi.jfx.dorse.model;

import org.hibernate.validator.constraints.NotBlank;

public class Sector extends BaseEntity implements Listable {
  public Sector() {
    super();
  }

  public Sector(Long id, String name, Long parent) {
    this.setId(id);
    this.name = name;
    this.parent = parent;
  }

  public Sector(String name) {
    this.name = name;
  }

  public Sector(Long id) {
    this.setId(id);
  }

  @NotBlank
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

	@Override
	public String getPrintName() {
		return getName();
	}

	@Override
	public String toString() {
		return getPrintName();
	}

  public static Sector getEmptySector(){
    Sector empty = new Sector();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }

}
