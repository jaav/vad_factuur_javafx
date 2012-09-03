package be.virtualsushi.jfx.dorse.events.authentication;

public class LoginEvent {

	private final String authToken;

	public LoginEvent(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthToken() {
		return authToken;
	}

}
