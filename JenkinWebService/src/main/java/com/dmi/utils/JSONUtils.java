package com.dmi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.dmi.dto.models.OemModel;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.gson.Gson;

public class JSONUtils {
	
	private static final Logger LOG = Logger.getLogger(JSONUtils.class);

	public static String objectToJSON(final Object object)
			throws JsonGenerationException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();
		final StringWriter writer = new StringWriter();

		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setTimeZone(TimeZone.getDefault());

		mapper.writeValue(writer, object);

		return writer.toString();

	}

	@SuppressWarnings("unchecked")
	public static <T> T readInputStream(final InputStream stream, final Class<?> targetClass)
			throws JsonParseException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setTimeZone(TimeZone.getDefault());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return (T) mapper.readValue(stream, targetClass);

	}

	public static boolean isJSONValid(String jsonString) {

		try {
			OemModel dm = new Gson().fromJson(jsonString, OemModel.class);

			if ((dm.getOemId() != null && !dm.getOemId().isEmpty())
					&& (dm.getDeviceTypeId() != null && !dm.getDeviceTypeId().isEmpty())
					&& (dm.getMessageModelVersion() != null && !dm.getMessageModelVersion().isEmpty())
			/*
			 * && (dm.getAssetDataList() != null && !dm.getAssetDataList()
			 * .isEmpty())
			 */
			)
				return true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

}
