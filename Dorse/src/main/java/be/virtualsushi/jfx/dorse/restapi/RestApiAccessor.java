package be.virtualsushi.jfx.dorse.restapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import be.virtualsushi.jfx.dorse.model.Customer;

public class RestApiAccessor extends RestTemplate {

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

	public RestApiAccessor() {
		super();

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

		HttpClient httpClient = new DecompressingHttpClient(new DefaultHttpClient(connectionManager));
		setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	public List<Customer> getCustomersList(Integer offset, Integer count) {
		ArrayList<Customer> result = new ArrayList<Customer>();
		Customer[] ids = getForObject("http://dorse.me/customers", Customer[].class);
		for (int i = 0; i < count; i++) {
			result.add(getCustomer(ids[i + offset].getId()));
		}
		return result;
	}

	public Customer getCustomer(Long id) {
		return getForObject("http://dorse.me/customers/{id}", Customer.class, id);
	}

	public String login(String username, String password) {
		return null;
	}

}
