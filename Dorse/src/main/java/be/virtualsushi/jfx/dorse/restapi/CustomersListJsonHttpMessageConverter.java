package be.virtualsushi.jfx.dorse.restapi;

import be.virtualsushi.jfx.dorse.model.Customer;

public class CustomersListJsonHttpMessageConverter extends AbstractJsonHttpMessageConverter<Customer[]> {

	@Override
	protected Class<Customer[]> getSupportedModelClass() {
		return Customer[].class;
	}

}
