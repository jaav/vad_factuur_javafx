package be.virtualsushi.jfx.dorse.restapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.virtualsushi.jfx.dorse.model.Sector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class RestApiAccessor extends RestTemplate {

	private static final Logger log = LoggerFactory.getLogger(RestApiAccessor.class);

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 30;

	@Value("${base.service.uri}")
	private String serviceUri;

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

	/*public <E extends BaseEntity> List<E> getList(Class<E> entityClass, boolean fullInfo, Object... parameters) {
		return getList(entityClass, null, null, "id", fullInfo, false, null, parameters);
	}

  public <E extends BaseEntity> List<E> getList(Class<E> entityClass, String orderOn, String additionalCondition, boolean fullInfo, boolean includesNonActive, Object... parameters) {
 		return getList(entityClass, null, null, orderOn, additionalCondition, fullInfo, includesNonActive, parameters);
 	}

  public <E extends BaseEntity> List<E> getList(Class<E> entityClass, Integer offset, Integer count, boolean fullInfo, Object... parameters) {
 		return getList(entityClass, offset, count, "id", fullInfo, false, null, parameters);
 	}

  public <E extends BaseEntity> List<E> getList(Class<E> entityClass, Integer offset, Integer count, String orderOn, boolean fullInfo, Object... parameters) {
 		return getList(entityClass, offset, count, orderOn, fullInfo, false, null, parameters);
 	}*/

  public <E extends BaseEntity> List<E> getList(Class<E> entityClass, boolean fullInfo) {
 		return getList(entityClass, null, null, "id", null, fullInfo, false);
 	}

	public <E extends BaseEntity> List<E> getList(Class<E> entityClass, Integer offset, Integer count, String orderOn, String additionalCondition, boolean fullInfo, boolean includesNonActive, Object... parameters) {
		ArrayList<E> result = new ArrayList<E>();
		String url = serviceUri + getEntityListSubPath(entityClass, offset, count, orderOn, fullInfo, includesNonActive, additionalCondition);
    if(url.charAt(url.length()-1)=='&') url = url.substring(0, url.length()-1);
		log.debug("Getting list of " + entityClass + " URL: " + url);
		E[] ids = getForObject(url, getEntityArrayClass(entityClass), parameters);
    result.addAll(Arrays.asList(ids));
		return result;
	}

  public List<Sector> getSubSectors(Long parent_id){
    String url = serviceUri + "subSectors/"+ parent_id;
    List<Sector> sectorList = new ArrayList<Sector>();
    return getForObject(url,sectorList.getClass());
  }

	private <E extends BaseEntity> List<E> detailList(Integer offset, Integer count, Class<E> entityClass, E[] ids) {
		ArrayList<E> result = new ArrayList<E>();
		if (offset != null && count != null && ids.length > count) {
			for (int i = 0; i < count; i++) {
				result.add(get(ids[i + offset].getId(), entityClass));
			}
		} else {
			for (E id : ids) {
				result.add(get(id.getId(), entityClass));
			}
		}
		return result;
	}

	public <E extends BaseEntity> E get(Long id, Class<E> clazz) {
		String url = serviceUri + getEntitySubPath(clazz) + "/{id}";
		logger.debug("Getting " + clazz + " URL: " + url);
		return getForObject(url, clazz, id);
	}

	public <E extends BaseEntity> void save(E entity) {
    entity.setActive(true);
		if (entity.getId() == null) {
			postForObject(serviceUri + getEntitySubPath(entity.getClass()), entity, entity.getClass());
		} else {
			postForObject(serviceUri + getEntitySubPath(entity.getClass()) + "/{id}", entity, entity.getClass(), entity.getId());
		}
	}

	public <E extends BaseEntity> void delete(E entity) {
		delete(serviceUri + getEntitySubPath(entity.getClass()) + "/{id}", entity.getId());
	}

	public String login(String username, String password) {
		HttpPost loginPost = new HttpPost(serviceUri + "login");
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[] { new BasicNameValuePair("username", username),
					new BasicNameValuePair("password", password) }));
			loginPost.setEntity(entity);
			HttpResponse response = httpClient.execute(loginPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        String auth = IOUtils.toString(response.getEntity().getContent());
				return auth;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private String getEntitySubPath(Class<? extends BaseEntity> entityClass) {
		if (entityClass.getAnnotation(ItemResourcePath.class) != null) {
			return entityClass.getAnnotation(ItemResourcePath.class).value();
		}
		return StringUtils.uncapitalize(entityClass.getSimpleName());
	}

	private String getEntityListSubPath(Class<? extends BaseEntity> entityClass, Integer offset, Integer count, String orderOn, boolean fullInfo, boolean includesNonActive, String additionalCondition) {
		StringBuilder result = new StringBuilder();
		if (entityClass.getAnnotation(ListResourcePath.class) != null) {
			result.append(entityClass.getAnnotation(ListResourcePath.class).value());
		} else {
			result.append(StringUtils.uncapitalize(entityClass.getSimpleName())).append("s");
		}
		result.append("?");
		if (offset != null)
			result.append("from=").append(offset).append("&");
		if (count != null)
			result.append("quantity=").append(count).append("&");
		if (orderOn != null)
			result.append("orderOn=").append(orderOn).append("&");
    if (fullInfo)
      result.append("fullInfo=true").append("&");
    if (includesNonActive)
      result.append("includesNonActive=true").append("&");
    if(StringUtils.isNotBlank(additionalCondition))
      result.append(additionalCondition);
		return result.toString();
	}

	@SuppressWarnings("unchecked")
	private <E extends BaseEntity> Class<E[]> getEntityArrayClass(Class<E> entityClass) {
		try {
			return (Class<E[]>) Class.forName("[L" + entityClass.getName() + ";");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to create array class for " + entityClass.getName(), e);
		}
	}

}
