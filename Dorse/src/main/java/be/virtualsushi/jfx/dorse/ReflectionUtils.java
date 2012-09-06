package be.virtualsushi.jfx.dorse;

import java.lang.reflect.ParameterizedType;

public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <C extends Class<?>> C getClassByGenericIndex(Object object, int index) {
		return (C) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
	}

}
