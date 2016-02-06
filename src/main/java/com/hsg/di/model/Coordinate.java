package com.hsg.di.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Jeff on 11/18/2015.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"lng", "lat"})
public class Coordinate {

	public double lat;

	public double lng;

	public Coordinate() {
	}

	public Coordinate(double lng, double lat) {
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Coordinate{" +
				"lat=" + lat +
				", lng=" + lng +
				'}';
	}
}
