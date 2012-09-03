package be.virtualsushi.jfx.dorse.events;

public class SaveStockEvent {

	private final Integer value;

	public SaveStockEvent(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

}
