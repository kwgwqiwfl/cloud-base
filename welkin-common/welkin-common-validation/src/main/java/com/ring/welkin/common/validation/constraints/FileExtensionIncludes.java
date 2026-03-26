package com.ring.welkin.common.validation.constraints;

import com.google.common.collect.Sets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 检验上传文件名后缀是否合规
 *
 * @author cloud
 * @date 2021年2月2日 上午11:10:32
 */
@Documented
@Constraint(validatedBy = {FileExtensionIncludes.FileExtensionIncludesValidatorForString.class,
        FileExtensionIncludes.FileExtensionIncludesValidatorForMultipartFile.class,
        FileExtensionIncludes.FileExtensionIncludesValidatorForFile.class,})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(FileExtensionIncludes.List.class)
public @interface FileExtensionIncludes {

	String message() default "{com.ring.welkin.common.validation.constraints.FileExtensionIncludes.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 支持的文件后缀名称
	 */
	String[] value() default {};

	/**
	 * Defines several {@code @IP} annotations on the same element.
	 */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
	@Documented
	public @interface List {

		FileExtensionIncludes[] value();
	}

	abstract class AbstractFileExtensionIncludesValidator<T> implements ConstraintValidator<FileExtensionIncludes, T> {
		private String[] extensions;

		@Override
		public void initialize(FileExtensionIncludes fileExtension) {
			this.extensions = fileExtension.value();
		}

		protected abstract String filename(T value);

		@Override
		public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
			// 空值不验证，交给空值验证注解
			if (value == null) {
				return true;
			}
			String filename = filename(value);
			String fileExt = FilenameUtils.getExtension(filename);
			if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
				constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
						.addMessageParameter("extensions", Arrays.toString(this.extensions))
						.addMessageParameter("filename", filename).addMessageParameter("fileExt", fileExt);
			}
            return StringUtils.isNotBlank(fileExt) && Sets.newHashSet(this.extensions).contains(fileExt.toLowerCase());
		}
	}

	class FileExtensionIncludesValidatorForString extends AbstractFileExtensionIncludesValidator<String> {
		@Override
		protected String filename(String value) {
			return value;
		}
	}

	class FileExtensionIncludesValidatorForMultipartFile extends AbstractFileExtensionIncludesValidator<MultipartFile> {
		@Override
		protected String filename(MultipartFile value) {
			return value.getOriginalFilename();
		}
	}

	class FileExtensionIncludesValidatorForFile extends AbstractFileExtensionIncludesValidator<File> {
		@Override
		protected String filename(File value) {
			return value.getName();
		}
	}
}
