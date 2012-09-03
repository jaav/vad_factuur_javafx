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

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class RestApiAccessor extends RestTemplate {

	public static final String BASE_SERVICE_URI = "http://dorse.me/";

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

	public <E extends BaseEntity> List<E> getList(Integer offset, Integer count, Class<E> entityClass, Class<E[]> arrayClass) {
		ArrayList<E> result = new ArrayList<E>();
		E[] ids = getForObject(BASE_SERVICE_URI + getEntitySubPath(entityClass) + "s", arrayClass);
		for (int i = 0; i < count; i++) {
			result.add(get(ids[i + offset].getId(), entityClass));
		}
		return result;
	}

	public <E extends BaseEntity> E get(Long id, Class<E> clazz) {
		return getForObject(BASE_SERVICE_URI + getEntitySubPath(clazz) + "/{id}", clazz, id);
	}

	public <E extends BaseEntity> void save(E entity) {
		if (entity.getId() == null) {
			postForObject(BASE_SERVICE_URI + getEntitySubPath(entity.getClass()), entity, entity.getClass());
		} else {
			postForObject(BASE_SERVICE_URI + getEntitySubPath(entity.getClass()) + "/{id}", entity, entity.getClass(), entity.getId());
		}
	}

	public String login(String username, String password) {
		return null;
	}

	private String getEntitySubPath(Class<? extends BaseEntity> entityClass) {
		return entityClass.getSimpleName().toLowerCase();
	}

}
