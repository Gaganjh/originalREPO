/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for DeployedEnvironment
 *
 * @see com.manulife.pension.service.withdrawal.domain.DeployedEnvironment
 * @author patelpo
 */
@PrepareForTest({ DeployedEnvironment.class })
@RunWith(PowerMockRunner.class)
public class DeployedEnvironmentTest {

	/**
	 * Parasoft Jtest UTA: Test for instance()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeployedEnvironment#instance()
	 * @author patelpo
	 */
	@Test
	public void testInstance() throws Throwable {
		// When
		DeployedEnvironment result = DeployedEnvironment.instance();

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEnvironmentVariable(String, Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeployedEnvironment#setEnvironmentVariable(String, Object)
	 * @author patelpo
	 */
	@Test
	public void testSetEnvironmentVariable() throws Throwable {
		// Given
		DeployedEnvironment underTest = DeployedEnvironment.instance();

		// When
		String name = "A"; // UTA: default value
		Object value = new Object(); // UTA: default value
		value = "VAL";
		underTest.setEnvironmentVariable(name, value);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEnvironmentVariable(String)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeployedEnvironment#getEnvironmentVariable(String)
	 * @author test user
	 */
	@Test
	public void testGetEnvironmentVariable() throws Throwable {
		// Given
		DeployedEnvironment underTest = DeployedEnvironment.instance();
		
		// When
		String name = "STR"; // UTA: default value
		Object result = underTest.getEnvironmentVariable(name);

		// Then
		assertNull(result);
	}
	@Test
	public void testGetEnvironmentVariable_1() throws Throwable {
		// Given
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		Context contextObj=mock(Context.class);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(contextObj);
		when(contextObj.lookup(nullable(String.class))).thenReturn("KEY");
		NamingEnumeration bindingList=mock(NamingEnumeration.class);
		when(contextObj.listBindings(nullable(String.class))).thenReturn(bindingList);
		when(bindingList.hasMore()).thenReturn(true,false);
		Binding binding=new Binding("DD", "EE", true);
		when(bindingList.next()).thenReturn(binding);
		DeployedEnvironment underTest = DeployedEnvironment.instance();
		
		// When
		String name = "STR"; // UTA: default value
		Object result = underTest.getEnvironmentVariable(name);

		// Then
		assertNotNull(result);
	}
}