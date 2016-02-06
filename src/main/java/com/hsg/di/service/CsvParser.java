package com.hsg.di.service;

import com.google.common.util.concurrent.RateLimiter;
import com.hsg.di.model.Coordinate;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jeff on 11/18/2015.
 */
@Service
public class CsvParser {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	GeocodingService geocodingService;
	
	@Value("${com.hsg.di.geocoder.limit:" + Integer.MAX_VALUE + "}")
	int geocodeLimit;

	String city = "Washington, D.C.";

	static final int PARSE_STATUS_MOD = 50;

	final RateLimiter rateLimiter = RateLimiter.create(7.0);

	public void openCsvParse(String filename, int addressColumn, int zipColumn) {
		log.info("openCsvParse '" + filename + "'...");
		try {
			String[] nameSplit = filename.split("/");
			String name = nameSplit[nameSplit.length - 1];
			String[] split = name.split("\\.");
			CSVWriter writer = new CSVWriter(new FileWriter(split[0] + "-out." + filename.split("\\.")[1]), ',');
			CSVReader reader = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename)));
			List<String[]> entries = reader.readAll();
			log.info("Read " + entries.size() + " entries.");
			int recordsGeocoded = 0;
			int recordsParsed = 0;
			long waitTimeMillis = 0;
			if (entries.size() > 0) {
	//			Arrays.asList(entries.get(0)).forEach(col -> {System.out.print(col + ",");});
	//			List<String[]> withoutHeaders = entries.subList(1, entries.size());
	//			withoutHeaders.forEach(writer::writeNext);
//				Executor executor = Executors.newCachedThreadPool();
				for (int i = 0; i < entries.size(); i++) {
					List<String> row = new ArrayList<>(Arrays.asList(entries.get(i)));
					if (i % PARSE_STATUS_MOD == 0) log.info("Parsing record " + i + " through " + (i + PARSE_STATUS_MOD));
					// Add Header columns for lat/lng and don't geocode this line
					if (i == 0) {
						row.add("Latitude");
						row.add("Longitude");
					}
					else {
						if (recordsParsed >= geocodeLimit) break;
						recordsParsed++;
						String address = row.get(addressColumn);
						String zipcode = zipColumn > 0 ? row.get(zipColumn) : null;

						long start = System.currentTimeMillis();
						rateLimiter.acquire();
						long stop = System.currentTimeMillis();
						waitTimeMillis += (stop - start);
						Coordinate coordinate = null;
						try {
							coordinate = geocodingService.geocode(address, city, zipcode);
						} catch (Exception e) {
							String msg = "Could not get geocode for address " + address;
							log.warn(msg);
							log.trace(msg, e);
						}
						if (coordinate != null) {
							row.add("" + coordinate.lat);
							row.add("" + coordinate.lng);
							recordsGeocoded++;
						}
						else {
							row.add("NO_DATA");
							row.add("NO_DATA");
						}
					}
					writer.writeNext(row.toArray(new String[row.size()]));
				}
				writer.close();
				double percentage = (100.0 * (double) recordsGeocoded) / (Math.min((double) entries.size(), (double)
						recordsParsed));
				log.info("Geocoded " + recordsGeocoded + " (" + String.format("%.0f%%",percentage) + ") records.");
				log.info("Throttled a total of " + waitTimeMillis + " ms.");
			}
		} catch (IOException e) {
			log.error("An error occurred while importing filename " + filename + ".", e);
		}
	}

//	public void commonsCsvParse(String filename) throws IOException {
//		log.info("commonsCsvParse '" + filename + "'...");
//		Reader in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename));
//		Iterable<CSVRecord> records = CSVFormat.EXCEL.
//				withHeader().
//				withEscape('\\').
//				parse(in);
//
//		FileWriter writer = new FileWriter("out.csv");
//		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
//		records.forEach(record -> {
//			try {
//				printer.print(record);
//			} catch (IOException e) {
//				log.error("Error printing record.", e);
//			}
//		});
//	}

}
