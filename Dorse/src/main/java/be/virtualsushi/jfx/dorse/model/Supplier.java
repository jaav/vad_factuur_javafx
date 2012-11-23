package be.virtualsushi.jfx.dorse.model;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Supplier extends IdNamePairEntity {

  public static Supplier getEmptySupplier(){
    Supplier empty = new Supplier();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }

  public Supplier(Long id) {
    this.setId(id);
  }

  public Supplier() {
    super();
  }
}
