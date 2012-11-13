package be.virtualsushi.jfx.dorse;

import org.junit.Test;

import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;

public class DorseApplicationTest {

	@Test
	public void test() throws ClassNotFoundException {
		System.out.println(getClass().getSimpleName());
		System.out.println(Integer[].class.getName());
		Class<?> c = Class.forName("[Ljava.lang.Float;");
		System.out.println(c.getName());
		createGenericArrayClass(Customer.class);
		createGenericArrayClass(Invoice.class);
	}

	@SuppressWarnings("unchecked")
	private <E extends BaseEntity> void createGenericArrayClass(Class<E> eclass) throws ClassNotFoundException {
		Class<E[]> c = (Class<E[]>) Class.forName("[L" + eclass.getName() + ";");
		System.out.println(c.getName());
	}

  @Test
  public void jsonTest() throws ClassNotFoundException {

  }

}
