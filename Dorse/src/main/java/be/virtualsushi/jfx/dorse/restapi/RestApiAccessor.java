package be.virtualsushi.jfx.dorse.restapi;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import be.virtualsushi.jfx.dorse.model.ServerResponse;
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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
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

  @Value("${sql.date.format}")
  private String sqlDateFormat;

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

  /*public <E extends BaseEntity> List<E> getList(Class<E> entityClass, boolean fullInfo) {
 		return getList(entityClass, null, null, "id", null, fullInfo, false);
 	}

	public <E extends BaseEntity> List<E> getList(Class<E> entityClass, Integer offset, Integer count, String orderOn, String additionalCondition, boolean fullInfo, boolean includesNonActive) {
		ArrayList<E> result = new ArrayList<E>();
		String url = serviceUri + getEntityListSubPath(entityClass);
    Map<String,?> urlVariables = getUrlVariables(offset, count, orderOn, fullInfo, includesNonActive, additionalCondition);
    if(url.charAt(url.length()-1)=='&') url = url.substring(0, url.length()-1);
		log.debug("Getting list of " + entityClass + " URL: " + url);
    try{
		  E[] ids = getForObject(url, getEntityArrayClass(entityClass), urlVariables);
      result.addAll(Arrays.asList(ids));
    } catch (Exception e){
      e.printStackTrace();
    }
		return result;
	}*/

  public <E extends BaseEntity> ServerResponse getResponse(Class<E> entityClass, boolean fullInfo) {
 		return getResponse(entityClass, null, null, null, "id", null, fullInfo, false, true);
 	}

  public <E extends BaseEntity> ServerResponse getResponse(BaseEntity entity) {
 		return getResponse(entity.getClass(), entity, null, null, "id", null, true, false, true);
 	}

	public <E extends BaseEntity> ServerResponse getResponse(Class<E> entityClass, BaseEntity entity, Integer offset, Integer count, String orderOn, String additionalCondition, boolean fullInfo, boolean includesNonActive, boolean asc) {
    ServerResponse response = null;
		ArrayList<E> data = new ArrayList<E>();
		String url = serviceUri + getEntityListSubPath(entityClass);
    Map<String,?> urlVariables = getUrlVariables(entity, offset, count, orderOn, fullInfo, includesNonActive, asc, additionalCondition);
    if(url.charAt(url.length()-1)=='&') url = url.substring(0, url.length()-1);
		log.debug("Getting list of " + entityClass + " URL: " + url);
    try{
      System.out.println("results in = " + getResponseSubtypeClass(entityClass));
		  response = getForObject(url, getResponseSubtypeClass(entityClass), urlVariables);
      //result.addAll(Arrays.asList(ids));
    } catch (Exception e){
      e.printStackTrace();
    }
		return response;
	}

  /*public List<Sector> getSubSectors(Long parent_id){
    String url = serviceUri + "subSectors/"+ parent_id;
    List<Sector> sectorList = new ArrayList<Sector>();
    return getForObject(url,sectorList.getClass());
  }*/

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

	public <E extends BaseEntity> Long save(E entity) {
    entity.setActive(true);
		if (entity.getId() == null || entity.getId()==0) {
      BaseEntity e = postForObject(serviceUri + getEntitySubPath(entity.getClass()), entity, entity.getClass());
      if(e!=null)
        return e.getId();
      else return null;
		} else {
			postForObject(serviceUri + getEntitySubPath(entity.getClass()) + "/{id}", entity, entity.getClass(), entity.getId());
      return entity.getId();
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

  private Map getUrlVariables(BaseEntity entity, Integer offset, Integer count, String orderOn, boolean fullInfo, boolean includesNonActive, boolean asc, String additionalCondition){
    Map<String, String> urlVariables = new HashMap<String, String>();
    if (offset != null)
      urlVariables.put("from", ""+offset);
    else
      urlVariables.put("from", "");
    if (count != null)
      urlVariables.put("quantity", ""+count);
    else
      urlVariables.put("quantity", "");
    if (fullInfo)
      urlVariables.put("fullInfo", "true");
    else
      urlVariables.put("fullInfo", "false");
    if (includesNonActive)
      urlVariables.put("includesNonActive", "true");
    else
      urlVariables.put("includesNonActive", "false");
    if(entity!=null){
      String filter = getFilterCondition(entity);
      if(StringUtils.isNotBlank(additionalCondition)){
        if(StringUtils.isNotBlank(filter))
          urlVariables.put("additionalCondition", additionalCondition+" and "+filter);
        else
          urlVariables.put("additionalCondition", additionalCondition);
      }
      else {
        if(StringUtils.isNotBlank(filter))
          urlVariables.put("additionalCondition", filter);
        else
          urlVariables.put("additionalCondition", "");
      }
      if(StringUtils.isNotBlank(entity.getColumnName())){
        urlVariables.put("orderOn", entity.getColumnName());
        if (StringUtils.isNotBlank(entity.getSortType()))
          urlVariables.put("asc", entity.getSortType());
        else
          urlVariables.put("asc", "true");
      }
      else if(StringUtils.isNotBlank(orderOn)){
        urlVariables.put("orderOn", orderOn);
        if (asc)
          urlVariables.put("asc", "true");
        else
          urlVariables.put("asc", "false");
      }
      else{
        urlVariables.put("orderOn", "");
        urlVariables.put("asc", "");
      }
    }
    else{//entity == null
      if(StringUtils.isNotBlank(additionalCondition))
        urlVariables.put("additionalCondition", additionalCondition);
      else
        urlVariables.put("additionalCondition", "");
      urlVariables.put("asc", "");
      urlVariables.put("orderOn", "");
    }
    urlVariables.put("count", "true");
    return urlVariables;
  }

	private String getEntityListSubPath(Class<? extends BaseEntity> entityClass) {
		StringBuilder result = new StringBuilder();
		if (entityClass.getAnnotation(ListResourcePath.class) != null) {
			result.append(entityClass.getAnnotation(ListResourcePath.class).value());
		} else {
			result.append(StringUtils.uncapitalize(entityClass.getSimpleName())).append("s");
		}
		result.append("?").append("from={from}&")
        .append("quantity={quantity}&")
        .append("orderOn={orderOn}&")
        .append("fullInfo={fullInfo}&")
        .append("includesNonActive={includesNonActive}&")
        .append("additionalCondition={additionalCondition}&")
        .append("asc={asc}&")
        .append("count={count}");
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

  @SuppressWarnings("unchecked")
 	private <E extends BaseEntity> Class<ServerResponse> getResponseSubtypeClass(Class<E> entityClass) {
 		try {
 			return (Class<ServerResponse>) Class.forName(entityClass.getName() + "Response");
 		} catch (ClassNotFoundException e) {
 			throw new RuntimeException("Unable to create array class for " + entityClass.getName(), e);
 		}
 	}

  private String getFilterCondition(BaseEntity entity) {
    StringBuffer filter = new StringBuffer();
    BeanInfo info = null;
    try {
      info = Introspector.getBeanInfo(entity.getClass(), Object.class);
      PropertyDescriptor[] props = info.getPropertyDescriptors();
      for (int i = 0; i < props.length; i++) {
        System.out.println("i = " + i);
        PropertyDescriptor pd = props[i];
        String name = pd.getName();
        System.out.println("name = " + name);
        JsonIgnore jsonIgnore = pd.getReadMethod().getAnnotation(JsonIgnore.class);
        if(jsonIgnore!=null) continue;
        JsonProperty jsonProperty = pd.getReadMethod().getAnnotation(JsonProperty.class);
        if(jsonProperty!=null)
          name = jsonProperty.value();

        Class clazz = pd.getPropertyType();
        Object value = pd.getReadMethod().invoke(entity);


        if(value!=null && !name.equals("new") && !name.equals("printName") && !name.equals("columnName") && !name.equals("sortType")){
          if(clazz.equals(String.class)){
            if(StringUtils.isNotBlank((String)value))
              value = " like '%"+value+"%'";
            else continue;
          }
          else if(clazz.equals(Date.class)) value = " > '"+ new SimpleDateFormat(sqlDateFormat).format(value)+"'";
          else if(BaseEntity.class.isAssignableFrom(clazz)){
            if(((BaseEntity)value).getId()>=0)
              value = " = "+(((BaseEntity)value).getId());
            else continue;
          }
          else if(clazz.equals(Long.class) || clazz.equals(Integer.class) || clazz.equals(Float.class)){
            long test = ((Number)value).longValue();
            if(((Number)value).longValue()>0)
              value = " = "+value;
            else continue;
          }
          else value = " = "+value;
          value = name+value+" and ";
          filter.append(value);
        }
      }
      if(filter.length()>5)
        return filter.substring(0, filter.length()-5);
      else
        return "";
    } catch (IntrospectionException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      return "";
    } catch (InvocationTargetException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      return "";
    } catch (IllegalAccessException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      return "";
    }
  }

}
