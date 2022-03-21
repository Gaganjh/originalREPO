/**
 * 
 */
package com.manulife.pension.service.withdrawal.log;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ReflectionToLogBuilder
 *
 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder
 * @author test user
 */
@PrepareForTest({ Field.class, ReflectionToLogBuilder.class })
@RunWith(PowerMockRunner.class)
public class ReflectionToLogBuilderTest {

	/**
	 * Parasoft Jtest UTA: Test for ReflectionToLogBuilder(object, style)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder(object, style)
	 * @author test user
	 */
	@Test
	public void testReflectionToLogBuilder() throws Throwable
	{
		Object object = new Object(); 
		ToStringStyle style=new StandardToStringStyle();
		ReflectionToLogBuilder builder=new ReflectionToLogBuilder(object, style);
	}
	/**
	 * Parasoft Jtest UTA: Test for ReflectionToLogBuilder(object, style,buffer)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder(object, style,buffer)
	 * @author test user
	 */
	@Test
	public void testReflectionToLogBuilder_1() throws Throwable
	{
		Object object = new Object(); 
		ToStringStyle style=new StandardToStringStyle();
		StringBuffer buffer=new StringBuffer();
		ReflectionToLogBuilder builder=new ReflectionToLogBuilder(object, style, buffer);
	}
	/**
	 * Parasoft Jtest UTA: Test for ReflectionToLogBuilder(object, style, buffer, reflectUpToClass, outputTransients, outputStatics)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder(object, style, buffer, reflectUpToClass, outputTransients, outputStatics)
	 * @author test user
	 */
	@Test
	public void testReflectionToLogBuilder_2() throws Throwable
	{
		Object object = new Object(); 
		ToStringStyle style=new StandardToStringStyle();
		StringBuffer buffer=new StringBuffer();
		boolean outputStatics=true;
		boolean outputTransients=true;
		Class reflectUpToClass=Class.forName("java.lang.Object");
		ReflectionToLogBuilder builder=new ReflectionToLogBuilder(object, style, buffer, reflectUpToClass, outputTransients, outputStatics);
	}
	/**
	 * Parasoft Jtest UTA: Test for getValue(Field)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder#getValue(Field)
	 * @author test user
	 */
	@Test
	public void testGetValue() throws Throwable {
		// Given
		Object object = new Object(); // UTA: default value
		ReflectionToLogBuilder underTest = new ReflectionToLogBuilder(object);

		// When
		//BaseWithdrawal bw = new Address();
		Class aClass = Class.forName("com.manulife.pension.service.withdrawal.valueobject.Address");
		Field[] field = aClass.getFields();
		Object result = underTest.getValue(field[0]);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for toLogExclude(Object, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder#toLogExclude(Object, Collection)
	 * @author test user
	 */
	@Test
	public void testToLogExclude() throws Throwable {
		// When
		Object object = new Object();
		Collection<String> excludeFieldNames = new ArrayList<String>();
		String item = "IT44";
		excludeFieldNames.add(item);
		String result = ReflectionToLogBuilder.toLogExclude(object, excludeFieldNames);
	}
}