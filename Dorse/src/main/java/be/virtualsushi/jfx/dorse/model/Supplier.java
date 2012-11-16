package be.virtualsushi.jfx.dorse.model;

public class Supplier extends IdNamePairEntity {

  public static Supplier getEmptySupplier(){
    Supplier empty = new Supplier();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }

}
