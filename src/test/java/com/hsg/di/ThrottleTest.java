package com.hsg.di;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jeff on 11/18/2015.
 */
public class ThrottleTest {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	final RateLimiter rateLimiter = RateLimiter.create(5000.0);

	@Test
	public void test() {
		doTest(false);
		doTest(true);
	}

	private void doTest(boolean throttle) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			if (throttle) rateLimiter.acquire();
		}
		long stop = System.currentTimeMillis();

		log.info("Total time in ms: " + (stop - start));
	}

}
