package com.hsg.di;

import com.hsg.di.model.Coordinate;
import com.hsg.di.service.GeocodingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Jeff on 11/18/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DiConfig.class})
public class GeocodingServiceTest {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	GeocodingService geocodingService;

	@Test
	public void test() throws IOException, URISyntaxException {
		Coordinate coordinate = geocodingService.geocode("206 P ST NW #1", "Washington, D.C.", "20001");
		log.info("" + coordinate);
	}

}
