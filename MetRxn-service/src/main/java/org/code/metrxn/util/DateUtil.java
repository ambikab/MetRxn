package org.code.metrxn.util;

/**
 * 
 * @author ambika_b
 *
 */
public class DateUtil {

	public static java.sql.Date getCurrentDatetime() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
}
