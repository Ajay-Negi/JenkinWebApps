
package com.dmi.dto.oemmodel.rawmessage;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(Include.NON_EMPTY)
public class AssetDataList implements Serializable {

	private static final long serialVersionUID = 1L;
	@SerializedName("key")
	@Expose
	private String key;
	@SerializedName("value")
	@Expose
	private double value;
	@SerializedName("timestamp")
	@Expose
	private String timestamp;

	@SerializedName("min")
	@Expose
	private double min;

	@SerializedName("max")
	@Expose
	private double max;

	@SerializedName("avg")
	@Expose
	private double avg;

	@SerializedName("sum")
	@Expose
	private double sum;

	@SerializedName("count")
	@Expose
	private long count;

	public AssetDataList() {

	}

	public AssetDataList(final String key, final double value, final String timestamp, final double min,
			final double max, final double avg, final double sum, final long count) {
		this.key = key;
		this.value = value;
		this.timestamp = timestamp;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.sum = sum;
		this.count = count;
	}

	/**
	 * 
	 * @return The key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 *            The key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return The value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * 
	 * @return The timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 *            The timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/*
	 * @Override public String toString() { //return
	 * ToStringBuilder.reflectionToString(this); }
	 */

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class AssetDataListBuilder {

		private String key;
		private double value;
		private String timestamp;
		private double min;
		private double max;
		private double avg;
		private double sum;
		private long count;

		public AssetDataListBuilder() {

		}

		public AssetDataListBuilder withKey(String key) {
			this.key = key;
			return this;
		}

		public AssetDataListBuilder withValue(double value) {
			this.value = value;
			return this;
		}

		public AssetDataListBuilder withTimeStamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public AssetDataListBuilder withMin(double min) {
			this.min = min;
			return this;
		}

		public AssetDataListBuilder withMax(double max) {
			this.max = max;
			return this;
		}

		public AssetDataListBuilder withAvg(double avg) {
			this.avg = avg;
			return this;
		}

		public AssetDataListBuilder withSum(double sum) {
			this.sum = sum;
			return this;
		}

		public AssetDataListBuilder withCount(long count) {
			this.count = count;
			return this;
		}

		public AssetDataList buildAssetDataList() {
			return new AssetDataList(key, value, timestamp, min, max, avg, sum, count);
		}

	}

}
