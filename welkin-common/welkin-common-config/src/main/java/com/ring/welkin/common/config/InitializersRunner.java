package com.ring.welkin.common.config;

import com.ring.welkin.common.core.Initializer;
import com.ring.welkin.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Order(1)
public class InitializersRunner implements CommandLineRunner, ApplicationContextAware {

	private final List<Initializer> initializers = new ArrayList<Initializer>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		Map<String, Initializer> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				Initializer.class, true, false);
		if (beans != null && !beans.isEmpty()) {
			this.initializers.addAll(beans.values());
			AnnotationAwareOrderComparator.sort(this.initializers);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			log.debug("start execute the initializers...");
			if (ICollections.hasElements(initializers)) {
				for (Initializer initializer : initializers) {
					initializer.init();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

}
