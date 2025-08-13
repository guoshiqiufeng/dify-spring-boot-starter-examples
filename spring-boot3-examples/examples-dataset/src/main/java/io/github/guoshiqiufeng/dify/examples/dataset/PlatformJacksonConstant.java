package io.github.guoshiqiufeng.dify.examples.dataset;

import java.time.format.DateTimeFormatter;

/**
 * @author yanghq
 * @version 1.0
 * @since 2021-11-08 15:52
 */
public interface PlatformJacksonConstant {

	/**
	 * 禁用
	 */
	String DISABLE = "false";

	/**
	 * 日期时间 格式
	 */
	String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期 格式
	 */
	String PATTERN_DATE = "yyyy-MM-dd";

	/**
	 * 时间格式
	 */
	String PATTERN_TIME = "HH:mm";

	String PATTERN_TIMES = "HH:mm:ss";

	int TIMES_LENGTH = 8;

	DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATE);

	DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_TIME);

}
