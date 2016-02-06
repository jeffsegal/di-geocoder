package com.hsg.di.service;

import com.hsg.di.model.Coordinate;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Jeff on 11/18/2015.
 */
@Service
public class GeocodingService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected final Logger geocodeResponse = LoggerFactory.getLogger("geocodeResponse");

	CloseableHttpClient httpClient;
	
	@Value("${com.hsg.di.geocoder.ws.google.geocode.scheme}")
	String geocodeScheme;

	@Value("${com.hsg.di.geocoder.ws.google.geocode.host}")
	String geocodeHost;

	@Value("${com.hsg.di.geocoder.ws.google.geocode.path}")
	String geocodePath;

	@Value("${com.hsg.di.geocoder.ws.google.apiKey}")
	String apiKey;

	@PostConstruct
	public void init() {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		httpClient = HttpClients.custom()
		            .setConnectionManager(connManager)
		            .build();
	}

	public Coordinate geocode(String streetAddress, String city, String zipcode) throws URISyntaxException, IOException {
		return geocode(buildAddress(streetAddress, city, zipcode));
	}

	public Coordinate geocode(String address) throws URISyntaxException, IOException {
		String url = new URIBuilder()
				.setScheme(geocodeScheme)
				.setHost(geocodeHost)
				.setPath(geocodePath)
				.addParameter("key", apiKey)
				.addParameter("address", address)
				.build().toString();
		log.debug("Issuing request to '" + url + "'");
		HttpGet httpget = new HttpGet(url);
		String response = EntityUtils.toString(httpClient.execute(httpget).getEntity());
		geocodeResponse.debug(response);
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		return parseLocation(document);
	}

	public Coordinate parseLocation(Object document) throws IOException {
		try {
			double lng = JsonPath.read(document, "$.results[0].geometry.location.lng");
			double lat = JsonPath.read(document, "$.results[0].geometry.location.lat");
			return new Coordinate(lng, lat);
		} catch (Exception e) {
			log.trace("Error parsing document.", e);
			return null;
		}
	}

	public String buildAddress(String streetAddress, String city, String zipcode) {
		if (city == null || zipcode == null) return streetAddress;
		else return streetAddress + ',' + city + "," + zipcode;
	}

}
