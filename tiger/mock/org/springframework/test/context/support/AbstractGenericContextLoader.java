/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.test.context.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Abstract, generic extension of {@link AbstractContextLoader} which loads a
 * {@link GenericApplicationContext} from the <em>locations</em> provided to
 * {@link #loadContext(String...)}.
 * </p>
 * <p>
 * Concrete subclasses must provide an appropriate
 * {@link #createBeanDefinitionReader(GenericApplicationContext) BeanDefinitionReader}.
 * </p>
 *
 * @author Sam Brannen
 * @since 2.5
 * @see #loadContext(String...)
 */
public abstract class AbstractGenericContextLoader extends AbstractContextLoader {

	protected static final Log logger = LogFactory.getLog(AbstractGenericContextLoader.class);


	/**
	 * <p>
	 * Loads a Spring ApplicationContext from the supplied
	 * <code>locations</code>.
	 * </p>
	 * <p>
	 * Implementation details:
	 * </p>
	 * <ul>
	 * <li>Creates a standard {@link GenericApplicationContext} instance.</li>
	 * <li>Populates it from the specified config locations through a
	 * {@link #createBeanDefinitionReader(GenericApplicationContext) BeanDefinitionReader}.</li>
	 * <li>Calls {@link #customizeBeanFactory(DefaultListableBeanFactory)} to
	 * allow for customizing the context's DefaultListableBeanFactory.</li>
	 * <li>Delegates to {@link AnnotationConfigUtils} for
	 * {@link AnnotationConfigUtils#registerAnnotationConfigProcessors(org.springframework.beans.factory.support.BeanDefinitionRegistry) registering}
	 * annotation configuration processors.</li>
	 * <li>Calls {@link #customizeContext(GenericApplicationContext)} to allow
	 * for customizing the context before it is refreshed.</li>
	 * <li>{@link ConfigurableApplicationContext#refresh() Refreshes} the
	 * context and registers a JVM shutdown hook for it.</li>
	 * </p>
	 * </ul>
	 * <p>
	 * Subclasses must provide an appropriate implementation of
	 * {@link #createBeanDefinitionReader(GenericApplicationContext)}.
	 * </p>
	 *
	 * @return a new application context
	 * @see org.springframework.test.context.ContextLoader#loadContext
	 * @see GenericApplicationContext
	 * @see #customizeBeanFactory(DefaultListableBeanFactory)
	 * @see #createBeanDefinitionReader(GenericApplicationContext)
	 * @see BeanDefinitionReader
	 */
	public final ConfigurableApplicationContext loadContext(final String... locations) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Loading ApplicationContext for locations ["
					+ StringUtils.arrayToCommaDelimitedString(locations) + "].");
		}

		final GenericApplicationContext context = new GenericApplicationContext();
		customizeBeanFactory(context.getDefaultListableBeanFactory());
		createBeanDefinitionReader(context).loadBeanDefinitions(locations);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		customizeContext(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}

	/**
	 * <p>
	 * Customize the internal bean factory of the ApplicationContext created by
	 * this ContextLoader.
	 * </p>
	 * <p>
	 * The default implementation is empty but can be overridden in subclasses
	 * to customize DefaultListableBeanFactory's standard settings.
	 * </p>
	 *
	 * @param beanFactory the bean factory created by this ContextLoader
	 * @see #loadContext(String...)
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#setAllowBeanDefinitionOverriding(boolean)
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#setAllowEagerClassLoading(boolean)
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#setAllowCircularReferences(boolean)
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#setAllowRawInjectionDespiteWrapping(boolean)
	 */
	protected void customizeBeanFactory(final DefaultListableBeanFactory beanFactory) {
		/* no-op */
	}

	/**
	 * Factory method for creating new {@link BeanDefinitionReader}s for
	 * loading bean definitions into the supplied
	 * {@link GenericApplicationContext context}.
	 *
	 * @param context The context for which the BeanDefinitionReader should be
	 *        created.
	 * @return A BeanDefinitionReader for the supplied context.
	 * @see #loadContext(String...)
	 * @see BeanDefinitionReader
	 */
	protected abstract BeanDefinitionReader createBeanDefinitionReader(final GenericApplicationContext context);

	/**
	 * <p>
	 * Customize the {@link GenericApplicationContext} created by this
	 * ContextLoader after bean definitions have been loaded into the context
	 * but before the context is refreshed.
	 * </p>
	 * <p>
	 * The default implementation is empty but can be overridden in subclasses
	 * to customize the application context.
	 * </p>
	 *
	 * @param context the newly created application context
	 * @see #loadContext(String...)
	 */
	protected void customizeContext(final GenericApplicationContext context) {
		/* no-op */
	}

}
