package com.ring.welkin.common.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class LongNumberSafeToStringSerializer extends JsonSerializer<Long> {
	public static final LongNumberSafeToStringSerializer INSTANCE = new LongNumberSafeToStringSerializer();

	/**
	 * 安全的长度，对于javascript是17位，超过17位会失去精度
	 */
	private int maxLength = 17;

	@Override
	public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
		throws IOException, JsonProcessingException {
		try {
			if (value != null) {
				String vStr = value.toString();
				if (vStr.length() > this.maxLength) {
					jgen.writeString(vStr);
					return;
				}
			}
			jgen.writeNumber(value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IOException(e);
		}
	}
}
