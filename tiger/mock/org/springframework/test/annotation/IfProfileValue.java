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

package org.springframework.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Test annotation to indicate that a test is enabled for a specific testing
 * profile or environment. If the configured {@link ProfileValueSource} returns
 * a matching {@link #value() value} for the provided {@link #name() name}, the
 * test will be enabled.
 * </p>
 * <p>
 * Note: {@link IfProfileValue @IfProfileValue} can be applied at either the
 * class or method level.
 * </p>
 * <p>
 * Examples: when using {@link SystemProfileValueSource} as the
 * {@link ProfileValueSource} implementation, you can configure a test method to
 * run only on Java VMs from Sun Microsystems as follows:
 * </p>
 *
 * <pre class="code">
 * {@link IfProfileValue @IfProfileValue}(name=&quot;java.vendor&quot;, value=&quot;Sun Microsystems Inc.&quot;)
 * testSomething() {
 *     // ...
 * }
 * </pre>
 *
 * <p>
 * You can alternatively configure {@link IfProfileValue @IfProfileValue} with
 * <em>OR</em> semantics for multiple {@link #values() values} as follows
 * (assuming a {@link ProfileValueSource} has been appropriately configured for
 * the &quot;test-groups&quot; name):
 * </p>
 *
 * <pre class="code">
 * {@link IfProfileValue @IfProfileValue}(name=&quot;test-groups&quot;, values={&quot;unit-tests&quot;, &quot;integration-tests&quot;})
 *  public void testWhichRunsForUnitOrIntegrationTestGroups() {
 *      // ...
 *  }
 * </pre>
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @since 2.0
 * @see ProfileValueSource
 * @see ProfileValueSourceConfiguration
 * @see ProfileValueUtils
 * @see AbstractAnnotationAwareTransactionalTests
 * @see org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests
 * @see org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests
 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner
 */
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IfProfileValue {

	/**
	 * <p>
	 * The <code>name</code> of the <em>profile value</em> against which to
	 * test.
	 * </p>
	 */
	String name();

	/**
	 * <p>
	 * A single, permissible <code>value</code> of the <em>profile value</em>
	 * for the given {@link #name() name}.
	 * </p>
	 * <p>
	 * Note: assigning values to both {@link #value()} and {@link #values()}
	 * will lead to a configuration conflict.
	 * </p>
	 */
	String value() default "";

	/**
	 * <p>
	 * A list of all permissible <code>values</code> of the
	 * <em>profile value</em> for the given {@link #name() name}.
	 * </p>
	 * <p>
	 * Note: assigning values to both {@link #value()} and {@link #values()}
	 * will lead to a configuration conflict.
	 * </p>
	 */
	String[] values() default {};

}