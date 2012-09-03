package be.virtualsushi.jfx.dorse.events;

public class SetValueEvent<V> {

	private final V value;

	public SetValueEvent(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

}
