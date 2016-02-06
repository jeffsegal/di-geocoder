package com.hsg.di.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Jeff on 11/18/2015.
 */
@Component
public class CsvPopulator {

//	@Autowired
//	HomeSalesCsvParser homeSalesCsvParser;
//
//	@Autowired
//	MetroCsvParser metroCsvParser;
//
//	@Autowired
//	MLSHomeSalesCsvParser mlsHomeSalesCsvParser;

	@Autowired
	CsvParser csvParser;

//	public void populateHomeSales(List<String> files) {
//		files.forEach(file -> {
//			homeSalesCsvParser.openCsvParse(file);
//		});
//	}
//
//	public void populateMetroStations(List<String> files) {
//		files.forEach(file -> {
//			metroCsvParser.openCsvParse(file);
//		});
//	}
//
//	public void populateMLSHomeSales(List<String> files) {
//		files.forEach(file -> {
//			mlsHomeSalesCsvParser.openCsvParse(file);
//		});
//	}

	public void populate(List<String> files, int addressColumn, int zipColumn) {
		files.forEach(file -> {
			csvParser.openCsvParse(file, addressColumn, zipColumn);
		});
	}

}
