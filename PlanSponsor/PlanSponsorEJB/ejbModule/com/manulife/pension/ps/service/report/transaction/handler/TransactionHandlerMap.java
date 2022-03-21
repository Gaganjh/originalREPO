package com.manulife.pension.ps.service.report.transaction.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A specialized map that stored the mapping between a TransactionHandlerKey and
 * the TransactionHandler.
 * 
 * @author Charles Chan
 */
public class TransactionHandlerMap {

	/**
	 * The internal map.
	 */
	private Map map;

	/**
	 * Constructor.
	 */
	public TransactionHandlerMap() {
		super();
		map = new HashMap();
	}

	/**
	 * Associates a TransactionHandler with a TransactionHandlerKey.
	 * 
	 * @param key
	 *            The key to use.
	 * @param handler
	 *            The handler to associate with.
	 * @throws IllegalStateException
	 *             if the key is already associated with another
	 *             TransactionHandler.
	 */
	public void putTransactionHandler(TransactionHandlerKey key,
			TransactionHandler handler) {

		if (map.get(key) != null) {
			throw new IllegalStateException("Cannot reset handler [key:" + key
					+ "]");
		}

		map.put(key, handler);
	}

	/**
	 * Obtains the TransactionHandler using the given TransactionHandlerKey. If
	 * none is found, it returns null. The lookup algorithm tries to find the
	 * best match for the given key. It works as follows:
	 * <ol>
	 * <li>Looks for an exact match</li>
	 * <li>If an exact match is not found, set the reasonCodeExcessWD in the key to null
     * and search again. This way, we find a handler that handles the given
     * transaction type and the given reason code and report regardless of the 2nd reasonCode i.e. reasonCodeExcessWD.
     * </li>
     * <li>Looks for an exact match</li>
	 * <li>If an exact match is not found, set the reportId in the key to null
	 * and search again. This way, we find a handler that handles the given
	 * transaction type and the given reason code regardless of the report.
	 * </li>
	 * <li>If it is still not found, set the reasonCode in the key to null and
	 * search again. This way, we find a handler that handles the given
	 * transaction type regardless of the reason code and the report.</li>
	 * <li>If all of the above fails, it returns null</li>
	 * </ol>
	 * 
	 * @param key
	 *            The key to use.
	 * @return The TransactionHandler associated with the key.
	 */
	public TransactionHandler getTransactionHandler(TransactionHandlerKey key) {
		Object obj = map.get(key);
		TransactionHandlerKey newKey;
		if (obj == null) {
		  if (key.getReportId()!= null) {//reqd only for key from CashAccountReportHandler, but not from TransactionHistoryReportHandler 
            newKey = new TransactionHandlerKey(key
                    .getTransactionType(), key.getReasonCode(), key.getReportId());
            obj = map.get(newKey);
            }
    		if (obj == null) {
    			newKey = new TransactionHandlerKey(key
    					.getTransactionType(), key.getReasonCode());
    			obj = map.get(newKey);
    			if (obj == null) {
    				newKey = new TransactionHandlerKey(key.getTransactionType());
    				obj = map.get(newKey);
    				if (obj == null) {
    					throw new IllegalArgumentException(
    							"Invalid Transaction Handler Key");
    				}
    			}
    		}
		}
		return (TransactionHandler) obj;
	}
}