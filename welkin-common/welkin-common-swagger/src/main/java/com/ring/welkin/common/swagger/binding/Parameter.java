package com.ring.welkin.common.swagger.binding;

import lombok.*;
import org.springframework.core.Ordered;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.RequestParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true)
@ToString
public class Parameter implements Ordered {
    private String name;
    private String description;
    private String defaultValue;
	private boolean required;
	private boolean deprecated;
	private boolean allowMultiple;
	private ScalarTypeEnum modelRef = ScalarTypeEnum.STRING;
	private AllowableListValues allowableListValues;
    private AllowableRangeValues allowableRangeValues;
    private String paramType;
    private String paramAccess;
    private boolean hidden;
    private String pattern;
	private String collectionFormat;
	private int order;
	private Map<String, List<Example>> examples = new HashMap<String, List<Example>>();
	private boolean allowEmptyValue;

	public AllowableValues getAllowableValues() {
		return allowableListValues != null ? allowableListValues : allowableRangeValues;
	}

	public RequestParameter get() {
		return new RequestParameterBuilder()//
			.name(name)//
			.description(description)//
			.deprecated(deprecated)//
			.hidden(hidden)//
			.in(paramType)//
			.parameterIndex(order)//
			.precedence(order)//
			.query(param -> param.model(model -> model.scalarModel(modelRef.getScalarType())))//
			// .content(c->c.requestBody(false).representation(MediaType.APPLICATION_JSON).apply(t->t.))//
			.build();
	}

	@Getter
	@AllArgsConstructor
	public enum ScalarTypeEnum {
		INTEGER(ScalarType.INTEGER), //
		LONG(ScalarType.LONG), //
		DATE(ScalarType.DATE), //
		DATE_TIME(ScalarType.DATE_TIME), //
		STRING(ScalarType.STRING), //
		BYTE(ScalarType.BYTE), //
		BINARY(ScalarType.BINARY), //
		PASSWORD(ScalarType.PASSWORD), //
		BOOLEAN(ScalarType.BOOLEAN), //
		DOUBLE(ScalarType.DOUBLE), //
		FLOAT(ScalarType.FLOAT), //
		BIGINTEGER(ScalarType.BIGINTEGER), //
		BIGDECIMAL(ScalarType.BIGDECIMAL), //
		UUID(ScalarType.UUID), //
		EMAIL(ScalarType.EMAIL), //
		CURRENCY(ScalarType.CURRENCY), //
		URI(ScalarType.URI), //
		URL(ScalarType.URL), //
		OBJECT(ScalarType.OBJECT);//

		private final ScalarType scalarType;
	}
}
