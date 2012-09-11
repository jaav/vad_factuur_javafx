package be.virtualsushi.jfx.dorse.events.authentication;

public class LoginSuccessfulEvent {

	private final String authToken;

	public LoginSuccessfulEvent(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthToken() {
		return authToken;
	}

}
