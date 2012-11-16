package be.virtualsushi.jfx.dorse.model;

public class Unit extends IdNamePairEntity {

  public static Unit getEmptyUnit(){
    Unit empty = new Unit();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }
}
