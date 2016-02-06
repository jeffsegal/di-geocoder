package com.hsg.di;

import com.hsg.di.service.CsvPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Jeff on 11/18/2015.
 */
@Configuration
@EnableAutoConfiguration
public class CsvPopulatorRunner {

	static Logger log = LoggerFactory.getLogger(CsvPopulatorRunner.class);

	public static void main(String[] args) throws IOException {
		log.info("Initializing '" + DiConfig.class + "'...");
		ApplicationContext ctx = SpringApplication.run(DiConfig.class, args);
		CsvPopulator csvPopulator = (CsvPopulator) ctx.getBean("csvPopulator");

		// Geocode home sales
//		csvPopulator.populate(Arrays.asList(
//				"2013.csv",
//				"2014.csv",
//				"2015.csv"
//		));

		// Geocode home sales
//		csvPopulator.populateMetroStations(Arrays.asList(
//				"Metro_Drink_300m.csv"
//		));

//		csvPopulator.populateMLSHomeSales(Arrays.asList(
//				"JanuarySalesDC.csv"
//		));

		csvPopulator.populate(Arrays.asList(
				"data/2013.csv",
				"data/2014.csv",
				"data/2015.csv"
		), 0, 1);

		csvPopulator.populate(Arrays.asList(
				"data/metro/Metro_Drink_300m.csv"
		), 2, -1);

		csvPopulator.populate(Arrays.asList(
				"data/JanuarySalesDC.csv"
		), 5, 13);

		log.info("CSV geocoding complete.");
	}

}
