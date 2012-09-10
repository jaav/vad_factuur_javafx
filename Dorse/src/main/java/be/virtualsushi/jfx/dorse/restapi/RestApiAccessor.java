package be.virtualsushi.jfx.dorse.restapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class RestApiAccessor extends RestTemplate {

	public static final String BASE_SERVICE_URI = "http://www.dorse.me/";

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

	private HttpClient httpClient;

	public RestApiAccessor() {
		super();

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

		httpClient = new DecompressingHttpClient(new DefaultHttpClient(connectionManager));
		setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	public <E extends BaseEntity> List<E> getList(Class<E> entityClass, Class<E[]> arrayClass, boolean needFullInfo) {
		return getList(null, null, entityClass, arrayClass, needFullInfo);
	}

	public <E extends BaseEntity> List<E> getList(Integer offset, Integer count, Class<E> entityClass, Class<E[]> arrayClass, boolean needFullInfo) {
		ArrayList<E> result = new ArrayList<E>();
		E[] ids = getForObject(BASE_SERVICE_URI + getEntitySubPath(entityClass) + "s", arrayClass);
		if (needFullInfo) {
			if (offset != null && count != null) {
				for (int i = 0; i < count; i++) {
					result.add(get(ids[i + offset].getId(), entityClass));
				}
			} else {
				for (E id : ids) {
					result.add(get(id.getId(), entityClass));
				}
			}
		} else {
			result.addAll(Arrays.asList(ids));
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
		HttpPost loginPost = new HttpPost(BASE_SERVICE_URI + "login");
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[] { new BasicNameValuePair("username", username),
					new BasicNameValuePair("password", password) }));
			loginPost.setEntity(entity);
			HttpResponse response = httpClient.execute(loginPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return "successful";
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	private String getEntitySubPath(Class<? extends BaseEntity> entityClass) {
		return entityClass.getSimpleName().toLowerCase();
	}

}
