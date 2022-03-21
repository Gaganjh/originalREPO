package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;

import org.apache.commons.collections.Factory;
/**
 * 
 * @author Baburaj Ramasamy
 *
 */

public class BacFactory implements Factory, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public BasicAssetChargeLine create() { return new BasicAssetChargeLine(); }

}
