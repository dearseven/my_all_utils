

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 日历适配器
 * 
 * @author wangxu
 * 
 */
public class CalendarWheelAdapter implements WheelAdapter {

	/** 默认最大值，3年 */
	public static final int DEFAULT_MAX_VALUE = 1095;

	/** 默认最小值 */
	public static final int Default_MIN_VALUE = 0;

	private int minValue;
	private int maxValue;

	private String format;

	/**
	 * 日期格式
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E");

	/**
	 * 构造函数
	 */
	public CalendarWheelAdapter() {
		this(Default_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * 构造函数
	 */
	public CalendarWheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null);
	}

	/**
	 * 构造函数
	 */
	public CalendarWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, index);
			return dateFormat.format(c.getTime());
		}
		return index + "";
	}

	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}

}
