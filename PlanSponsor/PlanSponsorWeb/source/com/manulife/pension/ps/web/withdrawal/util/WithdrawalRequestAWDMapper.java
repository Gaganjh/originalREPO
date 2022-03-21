package com.manulife.pension.ps.web.withdrawal.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import com.google.gson.Gson;
import com.manulife.esb.xsd.jh.workmanagement.Attachment;
import com.manulife.esb.xsd.jh.workmanagement.AttachmentList;
import com.manulife.esb.xsd.jh.workmanagement.CreateAWDInstance;
import com.manulife.esb.xsd.jh.workmanagement.CreateObjectRequest;
import com.manulife.esb.xsd.jh.workmanagement.CreateObjects;
import com.manulife.esb.xsd.jh.workmanagement.CreateSourceInstance;
import com.manulife.esb.xsd.jh.workmanagement.CreateWorkInstance;
import com.manulife.esb.xsd.jh.workmanagement.FieldValue;
import com.manulife.esb.xsd.jh.workmanagement.FieldValues;
import com.manulife.esb.xsd.jh.workmanagement.RelateObjects;
import com.manulife.esb.xsd.jh.workmanagement.Relationships;



/**
 * This Class maps the withdrawal request items to the request object for AWD Workmanagement API call 
 * 
 * 
 */
public class WithdrawalRequestAWDMapper {
	public static final String AWD_RECV_DATE_PATTERN = "yyyy-MM-dd-HH.mm.ss.mmmmmm";
	
	 /**This method will maps the withdrawal request items to the JSON request object for AWD Workmanagement API call
     *  @param String ssn
     *  @param String contractNumber
     *  @param String transactionNumber
     *  @param String submissionId
     *  @param String file
     *  @return String 
     */
	public static String mapAWDServiceRequest(String ssn, String contractNumber, String transactionNumber, String submissionId, String file) throws IOException{
		List<CreateSourceInstance> createSourceInstanceList = new ArrayList<CreateSourceInstance>();
		StringBuffer sourceInstanceBuffer = new StringBuffer();
		Gson gson = new Gson();
		CreateObjects createObjects= new CreateObjects();
		CreateObjectRequest createObjectRequest = new CreateObjectRequest(); 
		//Work Instance for the entire submission
		CreateWorkInstance createWorkInstance = new CreateWorkInstance(); 
		CreateAWDInstance createAWDWorkInstance = new CreateAWDInstance();
		
		FieldValues fieldValues = new FieldValues();
		fieldValues=getFieldValues(ssn,contractNumber, transactionNumber, file,fieldValues);
		createAWDWorkInstance.setFieldValues(fieldValues);
		createAWDWorkInstance.setBusinessArea("GLOBAL_RPS");
		createAWDWorkInstance.setType("INTEGRATE");
		createAWDWorkInstance.setRelationshipId("1");
		createWorkInstance.setCreateAWDInstance(createAWDWorkInstance);
		createWorkInstance.setStatus("CREATED");
		createObjectRequest.getCreateFolderInstanceOrCreateSourceInstanceOrCreateWorkInstance().add(createWorkInstance);
		Relationships relationships = new Relationships();	
			RelateObjects relateObjects = new RelateObjects();
			relateObjects.setParentRelationshipId("1");
			CreateSourceInstance  createSourceInstance = new CreateSourceInstance();
			CreateAWDInstance createAWDSourceInstance = new CreateAWDInstance();
			FieldValues fieldValuesSourceInstance = new FieldValues();
			fieldValuesSourceInstance = getFieldValues(ssn,contractNumber, transactionNumber, file,fieldValuesSourceInstance);
			
			/*adding file name*/	
			FieldValue fieldValueFileName = new FieldValue();
			fieldValueFileName.setName("FLNM");
			fieldValueFileName.setValue(submissionId);
			fieldValuesSourceInstance.getFieldValue().add(fieldValueFileName);
			/* adding file extension*/
			FieldValue fieldValueFileExtension = new FieldValue();
			fieldValueFileExtension.setName("EXT");
			fieldValueFileExtension.setValue(".html");
			fieldValuesSourceInstance.getFieldValue().add(fieldValueFileExtension);
			
			createAWDSourceInstance.setFieldValues(fieldValuesSourceInstance);
			createAWDSourceInstance.setBusinessArea("GLOBAL_RPS");
			createAWDSourceInstance.setType("FILE");
			String relationShipId = String.valueOf(2);
			createAWDSourceInstance.setRelationshipId(relationShipId);
			relateObjects.setChildRelationshipId(relationShipId);
			createSourceInstance.setCreateAWDInstance(createAWDSourceInstance);
			createSourceInstance.setContentId(submissionId);//need to change this
			AttachmentList attachmentList = new AttachmentList();
			Attachment attachment = new Attachment();
			if(null!=file){
				String fileData = new String( 
						Base64.encodeBase64(file.getBytes())); 
				attachment.setBinaryData(fileData);
				attachment.setSequence(1);
			}
			attachmentList.getAttachment().add(attachment);
			createSourceInstance.setAttachmentList(attachmentList);
			
			createObjectRequest.getCreateFolderInstanceOrCreateSourceInstanceOrCreateWorkInstance().add(createSourceInstance);
			sourceInstanceBuffer.append( "{\"createSourceInstance\": "+gson.toJson(createSourceInstance)+"}").append(",");
			createSourceInstanceList.add(createSourceInstance);
			relationships.getRelateObjects().add(relateObjects);
		
		createObjectRequest.setRelationships(relationships);
		createObjects.setCreateObjectRequest(createObjectRequest);
		createObjects.toString();
		String prefix = "{\n" + 
				"    \"createObjectRequest\": {\n" + 
				"        \"createFolderInstanceOrCreateSourceInstanceOrCreateWorkInstance\": [";
		
		
		String suffix="  }\n" + 
				"}\n";
		
		String relationshipJson="\"relationships\": "+gson.toJson(relationships);
		String createWorkInstanceJson=  "{\"createWorkInstance\": "+gson.toJson(createWorkInstance)+"}";
		String createSourceInstanceJson = sourceInstanceBuffer.substring(0, sourceInstanceBuffer.lastIndexOf(","))+"]\n";
		//"{\"createSourceInstance\": "+gson.toJson(createSourceInstanceList)+"}";
		return prefix+createWorkInstanceJson+","+createSourceInstanceJson+","+relationshipJson+suffix;
		
	}
    /**This method will create the LOB field and values in the JSON request to be sent to Workmanagement API 
     *  @param String ssn
     *  @param String contractNumber
     *  @param String transactionNumber
     *  @param String submissionId
     *  @param String file
     *  FieldValues fieldWorkValues
     *  @return String 
     */
	private static FieldValues getFieldValues(String ssn, String contractNumber, String transactionNumber, String file, FieldValues fieldWorkValues) {

		FieldValue fieldWorkValueWKT2 = new FieldValue();
		fieldWorkValueWKT2.setName("RFWT");
		fieldWorkValueWKT2.setValue("WD_STP");
		fieldWorkValues.getFieldValue().add(fieldWorkValueWKT2);

		FieldValue fieldWorkValueCNNO = new FieldValue();
		fieldWorkValueCNNO.setName("CNNO");
		fieldWorkValueCNNO.setValue(contractNumber);
		fieldWorkValues.getFieldValue().add(fieldWorkValueCNNO);

		FieldValue fieldWorkValueSSNO = new FieldValue();
		fieldWorkValueSSNO.setName("SSNO");
		fieldWorkValueSSNO.setValue(ssn);
		fieldWorkValues.getFieldValue().add(fieldWorkValueSSNO);

		FieldValue fieldWorkValueAFMC = new FieldValue();
		fieldWorkValueAFMC.setName("TRXN");
		fieldWorkValueAFMC.setValue(transactionNumber);
		fieldWorkValues.getFieldValue().add(fieldWorkValueAFMC);

		return fieldWorkValues;
	}



}
