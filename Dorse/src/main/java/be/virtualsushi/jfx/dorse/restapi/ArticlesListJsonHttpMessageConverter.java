package be.virtualsushi.jfx.dorse.restapi;

import be.virtualsushi.jfx.dorse.model.Article;

public class ArticlesListJsonHttpMessageConverter extends AbstractJsonHttpMessageConverter<Article[]> {

	@Override
	protected Class<Article[]> getSupportedModelClass() {
		return Article[].class;
	}

}
