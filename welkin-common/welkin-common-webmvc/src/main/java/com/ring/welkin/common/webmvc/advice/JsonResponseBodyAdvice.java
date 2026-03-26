package com.ring.welkin.common.webmvc.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ring.welkin.common.config.serialize.jackson.fieldFilter.FilterFieldsJsonSerializer;
import com.ring.welkin.common.core.annotation.JsonBodys;
import com.ring.welkin.common.core.result.ErrType;
import com.ring.welkin.common.core.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 说明：处理包装同时使用@ResponseBody和@JsonBody注解的方法以满足json字段过滤等需求. <br>
 *
 * @author cloud
 * @date 2017年12月18日 下午6:12:38
 */
@Slf4j
@ControllerAdvice
@ConditionalOnBean(ObjectMapper.class)
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	private final FilterFieldsJsonSerializer jsonSerializer;

	public JsonResponseBodyAdvice(@Autowired final ObjectMapper objectMapper) {
		this.jsonSerializer = new FilterFieldsJsonSerializer(objectMapper);
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 该Controller类上面存在@RestController注解或者该方法上面存在@ResponseBody注解，并且该方法上面存在@JsonBody注解(过滤所有，暂时不需要判断是否存在@JsonBody注解)
		Class<?> declaringClass = returnType.getMethod().getDeclaringClass();
		return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType)
			&& (declaringClass.isAnnotationPresent(RestController.class)
			|| returnType.hasMethodAnnotation(ResponseBody.class))
			&& returnType.hasMethodAnnotation(JsonBodys.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
								  ServerHttpResponse response) {

		// 返回报文统一进行处理，最外面统一加上一层包装
		if (!(Response.class.isAssignableFrom(body.getClass()))) {
			body = Response.ok(body);
		}

		// 如果没有@JsonBody注解则不需要过滤字段直接返回
		if (!returnType.hasMethodAnnotation(JsonBodys.class)) {
			return body;
		}

		// 处理json字段过滤逻辑
		Annotation[] annos = returnType.getMethodAnnotations();
		// 如果是@JsonBody就循环调用
		Arrays.asList(annos).forEach(a -> {
			if (a instanceof JsonBodys) {
				JsonBodys jsonBody = (JsonBodys) a;
				Arrays.asList(jsonBody.value()).forEach(jsonAnno -> {
					jsonSerializer.filter(jsonAnno);
				});
			}
		});

		// 转换成json返回
		try {
			jsonSerializer.toJson(response.getBody(), body);
		} catch (Exception e) {
			log.error("result serialize failed", e);
			return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getCause().toString());
		}
		return null;
	}
}
