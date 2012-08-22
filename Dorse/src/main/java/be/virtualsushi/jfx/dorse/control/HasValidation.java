package be.virtualsushi.jfx.dorse.control;

public interface HasValidation {

	void setInvalid(String message);

	void clearInvalid();
	
	boolean isValid();

}
