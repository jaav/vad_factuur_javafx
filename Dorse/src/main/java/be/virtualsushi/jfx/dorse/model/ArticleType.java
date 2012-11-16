package be.virtualsushi.jfx.dorse.model;

public class ArticleType extends IdNamePairEntity {

  public static ArticleType getEmptyArticleType(){
    ArticleType empty = new ArticleType();
    empty.setId(-1L);
    empty.setName("");
    return empty;
  }

}
