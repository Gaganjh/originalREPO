package com.manulife.pension.ps.web.ipitool;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.fee.valueobject.DiscontinuaceFee;


enum DiScaleRetrievalViewTransformer {
    
    INSTANCE;
    
    List<String> transform(List<DiscontinuaceFee> diScale) {
    	List<String> list = new ArrayList<String>();
    	
    for(DiscontinuaceFee discontinuaceFee: diScale){
    	list.add(discontinuaceFee.getAmountValue().toString());
    }
    
    return list ;
    }
  }

      
