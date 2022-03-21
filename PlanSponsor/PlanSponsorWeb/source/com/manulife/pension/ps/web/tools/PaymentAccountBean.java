package com.manulife.pension.ps.web.tools;

import java.io.Serializable;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

	
public class PaymentAccountBean extends LabelValueBean implements Serializable {
		
		protected String name;
		protected String type;
		
		PaymentAccountBean(String name, String label, String type, String value){
			super(label, value);
			this.name = name;
			this.type = type;
		}

		/**
		 * @return
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param string
		 */
		public void setName(String string) {
			name = string;
		}

		/**
		 * @return
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * @param string
		 */
		public void setType(String string) {
			type = string;
		}	

	}

