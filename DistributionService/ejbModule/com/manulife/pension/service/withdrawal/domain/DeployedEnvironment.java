package com.manulife.pension.service.withdrawal.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Provides functionality to add and retrieve values from the deployed environment. The deployed
 * environment is initialized with the names and valued from the java:comp/env context.
 * 
 * @author Chris Shin
 * 
 */
public final class DeployedEnvironment {
    public static final String COMPLETED_WD_THRESHOLD_NAME = "completedApolloWDThreshold";

    public static final String PENDING_WD_THRESHOLD_NAME = "pendingApolloWDThreshold";

    private static Logger logger = Logger.getLogger(DeployedEnvironment.class);

    private static final String ENV_JNDI_NAME = "java:comp/env";

    private static DeployedEnvironment instance = new DeployedEnvironment();

    private Map env = Collections.synchronizedMap(new HashMap());

    /**
     * Instance operation of the DeployedEnvironment singleton.
     * 
     * @return DeployedEnvironment The DeployedEnvironment singleton.
     * 
     */
    public static DeployedEnvironment instance() {
        return instance;
    }

    /**
     * Returns the value of the environment variable associated with the given name.
     * 
     * @param name String
     * @return Object
     */
    public Object getEnvironmentVariable(final String name) {
        Object value = env.get(name);

        if (value == null) {
            try {
                Context envCtx = (Context) (new InitialContext()).lookup(ENV_JNDI_NAME);
                value = envCtx.lookup(name);
                this.env.put(name, value);
            } catch (NamingException e) {
                StringBuffer buffer = new StringBuffer("Failed to find value for the name[");
                buffer.append(name);
                buffer.append("] in the deployed environment. ");
                buffer.append(e.toString());

                String msg = buffer.toString();

                logger.error(msg);
            }

        }
        return value;
    }

    /**
     * Adds the given value to the deployed environment for the given lookup name. Note: If the
     * given name already exists in the deployed environment, the associated value is replaced by
     * the given value.
     * 
     * @param name String
     * 
     * @param value Object
     */
    public void setEnvironmentVariable(final String name, final Object value) {
        this.env.put(name, value);
    }

    /**
     * Constucts a deployed environment. Initializes the environment with the names and values from
     * the java:comp/env context.
     * 
     */
    private DeployedEnvironment() {
        NamingEnumeration bindingList = null;

        try {
            Context envCtx = (Context) (new InitialContext()).lookup(ENV_JNDI_NAME);
            bindingList = envCtx.listBindings("");

            while (bindingList.hasMore()) {
                Binding binding = (Binding) bindingList.next();
                this.env.put(binding.getName(), binding.getObject());
            }
        } catch (NamingException e) {
            logger.warn(e.toString());
        } finally {
            try {
                if (bindingList != null) {
                    bindingList.close();
                } // fi
            } catch (NamingException e) {
                logger.warn("Failed to close NamingEnumeration. " + e.toString());
            } // end try/catch
        } // end try/catch/finally
    }

}
