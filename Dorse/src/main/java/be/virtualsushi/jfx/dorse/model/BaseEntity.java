package be.virtualsushi.jfx.dorse.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class BaseEntity {

	private Long id;
  private Boolean active;

  @JsonIgnore
  private String sortType;

  @JsonIgnore
  private String columnName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public boolean isNew() {
		return id == null;
	}

  public String getColumnName() {
    if("Stock".equals(columnName)) return "stock.quantity";
    if("Customername".equals(columnName)) return "Customer.name";
    if("Creation date".equals(columnName)) return "creation_date";
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getSortType() {
    if("ASC".equals(sortType)) return "true";
    else if(StringUtils.isBlank(sortType)) return "true";
    else return "false";
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

  public boolean equals(Supplier compareMe) {
     EqualsBuilder builder = new EqualsBuilder().append(this.getId(), compareMe.getId());
     return builder.isEquals();
  }

}
