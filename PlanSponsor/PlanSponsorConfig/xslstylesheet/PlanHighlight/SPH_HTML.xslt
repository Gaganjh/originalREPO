<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:preserve-space elements="*" />
<xsl:output method="xml" omit-xml-declaration="yes" />
<xsl:variable name="header" select="sph/header" />
<xsl:variable name="eligibilitySection" select="sph/eligibilitySection" />
<xsl:attribute-set name="section-title-style">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="font-size">30pt</xsl:attribute>
</xsl:attribute-set>
<xsl:template match="sph">
<table border="0" rules="cols" width="100%" cellpadding="10" cellspacing="0">
						<tbody>
							<!-- Introductory Paragraph -->
							<tr>
									<xsl:value-of select="header/introductory" />
							</tr>

							<!-- Eligibility Section -->
							<tr>
								<td>
										<xsl:value-of select="pageSectionText/eligibility" />
								</td>
								<td>
									<!--  <xsl:apply-templates select="eligibilitySection" /> -->
									<xsl:variable name="eligibilitySectionSpecified_var">
										<xsl:value-of select="eligibilitySection/eligibilitySectionSpecified" />
									</xsl:variable>
									<xsl:variable name="eligibilityExcludedEmployeeSpecified_var">
										<xsl:value-of
											select="eligibilitySection/eligibilityExcludedEmployeeSpecified" />
									</xsl:variable>
									<xsl:variable name="multipleEligibilityRulesForOneSingleMoneyType_var">
										<xsl:value-of select="eligibilitySection/multipleEligibilityRulesForOneSingleMoneyType" />
									</xsl:variable>
									
									<xsl:if test="$multipleEligibilityRulesForOneSingleMoneyType_var ='Y'">
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='multipleEligibilityRulesForOneSingleMoneyType_MSG'">
														<xsl:value-of select="*|text()" />
													</xsl:if>
												</xsl:for-each>
									</xsl:if>
									
									<xsl:if test="$eligibilityExcludedEmployeeSpecified_var='N' and $eligibilitySectionSpecified_var='N' and $multipleEligibilityRulesForOneSingleMoneyType_var ='N'">
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='eligibilitySectionSpecifiedMSG'">
														<xsl:value-of select="*|text()" />
													</xsl:if>
												</xsl:for-each>
									</xsl:if>
									<xsl:if test="$multipleEligibilityRulesForOneSingleMoneyType_var ='N'">
									 <xsl:if test="$eligibilitySectionSpecified_var='Y' or $eligibilityExcludedEmployeeSpecified_var='Y'">
																						
												<xsl:if test="$eligibilityExcludedEmployeeSpecified_var='Y'">
													<!-- eligibility exclusions Section -->
														<xsl:variable name="noExcludedEmployees_var">
																<xsl:value-of select="eligibilitySection/noExcludedEmployees"/>
														</xsl:variable>
														<xsl:variable name="showExcludedGrid_var">
																<xsl:value-of select="eligibilitySection/showExcludedGrid"/>
														</xsl:variable>
														<xsl:variable name="otherOption_var">
																	<xsl:value-of select="eligibilitySection/otherOption"/>
														</xsl:variable>
														
														<xsl:if test="$otherOption_var='Y'">
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilityExcludedEmployeesOtherMSG'">
																			<xsl:value-of select="*|text()"/>
																	</xsl:if>
																</xsl:for-each>
														</xsl:if>
														
														<xsl:if test="$noExcludedEmployees_var='N'">
															<xsl:if test="$showExcludedGrid_var='N'">
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilityExcludedEmployeesMSG'">
																			<xsl:value-of select="*|text()"/>
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>
					
														</xsl:if>
														
														<!--  eligibility exclusions Section Table display starts -->
														<xsl:if test="$showExcludedGrid_var='Y'">
														
																<xsl:variable name="showUnion_var">
																	<xsl:value-of select="eligibilitySection/showUnion"/>
																</xsl:variable>
																<xsl:variable name="showNonResidentAliens_var">
																	<xsl:value-of select="eligibilitySection/showNonResidentAliens"/>
																</xsl:variable>
																<xsl:variable name="showHighlyCompensated_var">
																	<xsl:value-of select="eligibilitySection/showHighlyCompensated"/>
																</xsl:variable>
																<xsl:variable name="showLeased_var">
																	<xsl:value-of select="eligibilitySection/showLeased"/>
																</xsl:variable>
																<xsl:variable name="showPartTimeOrTemporary_var">
																	<xsl:value-of select="eligibilitySection/showPartTimeOrTemporary"/>
																</xsl:variable>
																
																
																	<xsl:value-of select="tableHeaderSection/exclutionsAboveTableMSG"/>
																
																							
																<table border="1" frame="border" rules="groups" >
																		
																																			
																	<tbody>
																		<tr>
																			<td>
																					<xsl:value-of select="tableHeaderSection/moneyType" />
																			</td>
																			<td>
																				<xsl:if test="$showUnion_var='Y'">
																						<xsl:value-of select="tableHeaderSection/union" />
																				</xsl:if>
																			</td>
																			<td>
																				<xsl:if test="$showNonResidentAliens_var='Y'">
																						<xsl:value-of select="tableHeaderSection/nonresidentAliens" />
																				</xsl:if>
																			</td>
																				<td >
																				<xsl:if test="$showHighlyCompensated_var='Y'">
																						<xsl:value-of select="tableHeaderSection/highlyCompensated" />
																				</xsl:if>
																			</td>
																			<td>
																				<xsl:if test="$showLeased_var='Y'">
																					<xsl:value-of select="tableHeaderSection/leased" />	
																				</xsl:if>
																			</td>
																		</tr>
																		<!-- For loop starts-->
																		<xsl:for-each select="eligibilitySection/exclusionTable">
																			<tr>
																					<td>
																							<xsl:value-of select="./moneyType/text()" /> 
																					</td>
																					
																					<xsl:if test="$showUnion_var='Y'">
																						<td >
																								<xsl:value-of select="./union/text()" />
																						</td>
																					</xsl:if>
																					
																					<xsl:if test="$showNonResidentAliens_var='Y'">
																						<td >
																								<xsl:value-of select="./nonResidentAliens/text()" />
																						</td>
																					</xsl:if>
																					
																					
																					<xsl:if test="$showHighlyCompensated_var='Y'">
																						<td >
																								<xsl:value-of select="./highlyCompensated/text()" />
																						</td>
																					</xsl:if>
																						
																					<xsl:if test="$showLeased_var='Y'">
																						<td>
																								<xsl:value-of select="./leased/text()" />
																						</td>
																					</xsl:if>
																					<xsl:if test="$showPartTimeOrTemporary_var='Y'">
																						<td>
																								<xsl:value-of select="./partTimeOrTemporary/text()" />
																						</td>
																					</xsl:if>
																	
																					
													  						</tr>
																		</xsl:for-each>
																		<!-- for loop ends -->			
																		
							       									</tbody>
							       									
							     								</table>
																<!-- Added for PRODUCTION FIX on Nov 27 - 2009 -->
																	<xsl:variable name="otherOptionSelectedFoaAnyMT_var">
																		<xsl:value-of select="eligibilitySection/otherOptionSelectedAnyMoneyType"/>
																	</xsl:variable>		
																	<xsl:if test="$otherOptionSelectedFoaAnyMT_var='Y'">
																			<xsl:for-each select="messages/message">
																				<xsl:if test="@id='eligibilityExcludedEmployeeOtherSelected_MSG'">
																					<xsl:value-of select="*|text()" />
																				</xsl:if>
																			</xsl:for-each>
																	</xsl:if>
																
																
														</xsl:if>
														<!--  eligibility exclusions Section Table display Ends -->
													</xsl:if> <!--  End if for No Exculded Eligibilty Specified check -->
													<!-- Paragraph 2 -->
																<xsl:variable name="eligibilityExcludedEmployeeParagraph_2_1_var">
																	<xsl:value-of select="eligibilitySection/eligibilityExcludedEmployeeParagraph_2_1" />
																</xsl:variable>
																<xsl:if test="$eligibilityExcludedEmployeeParagraph_2_1_var='Y'">
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityExcludedEmployeeParagraph_2_1_MSG'">
																				<xsl:value-of select="*|text()" />&#160;
																			</xsl:if>
																		</xsl:for-each>
																</xsl:if>
													
													
															<!--  Message display starts -->
					
															<xsl:variable name="allIdentical_var">
																<xsl:value-of select="eligibilitySection/allIdentical" />
															</xsl:variable>
															<xsl:variable name="eligibilityParagraph_1_1_var">
																<xsl:value-of select="eligibilitySection/eligibilityParagraph_1_1" />
															</xsl:variable>
					
															
					
															<xsl:if test="$eligibilityParagraph_1_1_var!='N'">
																	<xsl:for-each select="messages/message">
																		<xsl:if test="@id='eligibilityParagraph_1_1'">
																			<xsl:value-of select="*|text()" />
																		</xsl:if>
																	</xsl:for-each>
																
															</xsl:if>
					
															<xsl:if test="$allIdentical_var='Y'">
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilitySection'">
																		<xsl:value-of select="*|text()" />
																	</xsl:if>
																</xsl:for-each>
																
															</xsl:if>
					
															<!--  Message display ends -->
					
															<xsl:if test="$eligibilitySectionSpecified_var='Y'">
					
																<!--  Table display starts -->
																<xsl:if test="$allIdentical_var='N'">
																	<!--  Getting value in variable starts -->
					
																	<xsl:variable name="showMinimumAge_var">
																		<xsl:value-of select="eligibilitySection/showMinimumAge" />
																	</xsl:variable>
					
																	<xsl:variable name="showHoursOfService_var">
																		<xsl:value-of select="eligibilitySection/showHoursOfService" />
																	</xsl:variable>
					
																	<xsl:variable name="showLengthOfService_var">
																		<xsl:value-of select="eligibilitySection/showLengthOfService" />
																	</xsl:variable>
																	<xsl:variable name="showEligibilty_var">
																		<xsl:value-of select="eligibilitySection/showEligibilty" />
																	</xsl:variable>
																	<xsl:variable name="colSpan_var">
																		<xsl:value-of select="eligibilitySection/colSpan" />
																	</xsl:variable>
					
					
																	<!--  Getting value in variable ends -->
					
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityRequiredLead'">
																				<xsl:value-of select="*|text()" />
																			</xsl:if>
																		</xsl:for-each>
					
					
																	<table border="1">
																		<th>
																		<xsl:if test="$showMinimumAge_var='Y'">
																			<td> </td>
																		</xsl:if>
																		<xsl:if test="$showHoursOfService_var='Y'">
																			<td> </td>
																		</xsl:if>
																		<xsl:if test="$showLengthOfService_var='Y'">
																			<td> </td>
																		</xsl:if>
																		<xsl:if test="$showMinimumAge_var!='Y'">
																			<td> </td>
																		</xsl:if>
																		</th>
					
																		<tbody>
					
																			<tr>
					
																				<td >
																						<xsl:value-of select="tableHeaderSection/moneyType" />
																				</td>
																				<xsl:if test="$showEligibilty_var='Y'">
																					<td >
																							<xsl:value-of select="tableHeaderSection/eligibility" />
																					</td>
																				</xsl:if>
																				<xsl:if test="$showMinimumAge_var='Y'">
																					<td >
																							<xsl:value-of select="tableHeaderSection/minimmumAge" />
																					</td>
																				</xsl:if>
																				<xsl:if test="$showHoursOfService_var='Y'">
																					<td >
																							<xsl:value-of select="tableHeaderSection/hoursOfService" />
																					</td>
																				</xsl:if>
																				<xsl:if test="$showLengthOfService_var='Y'">
																					<td >
																							<xsl:value-of select="tableHeaderSection/periodOfService" />
																					</td>
																				</xsl:if>
																			</tr>
																			<!-- For loop starts-->
					
																			<xsl:for-each select="eligibilitySection/listOfMoneyType">
					
																				<xsl:variable name="skipRow_var">
																					<xsl:value-of select="./skipRow/text()" />
																				</xsl:variable>
																				<xsl:if test="$skipRow_var='N'">
																					<tr>
					
																						<td>
																							<xsl:value-of select="./moneyType/text()" /> 
																					</td>
																					
																					<xsl:variable name="EligibilityChecked_var">
																						<xsl:value-of select="./EligibilityChecked/text()" />
																					</xsl:variable>
																				
																					
																					<xsl:if test="$showEligibilty_var='N'">
																					<xsl:if test="$EligibilityChecked_var='immediate'">
																						<xsl:if test="$colSpan_var='3'">
																							<td>																	
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																							</td>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='2'">
																							<td  >																	
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																							</td>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='1'">
																							<td>																	
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																							</td>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='0'">
																							<td  >																	
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																							</td>
																						</xsl:if>
																					</xsl:if>
																					</xsl:if>
																					
																					<xsl:if test="$showEligibilty_var='Y'">
																					<xsl:if test="$EligibilityChecked_var='immediate'"> 
																							<td >
																									<xsl:value-of select="./onlyEligibilityCheckedText/text()" />
																							</td>
																					</xsl:if>
																					</xsl:if>
																					 
																					<xsl:if test="$EligibilityChecked_var!='immediate'"> 
																						<xsl:if test="$showMinimumAge_var='Y'">
																							<td >
																									<xsl:value-of select="./minimumAge/text()" />
																							</td>
																						</xsl:if>
																						
																						<xsl:if test="$showHoursOfService_var='Y'">
																							<td  >
																									<xsl:value-of select="./hoursOfService/text()" />
																							</td>
																						</xsl:if>
																						
																						<xsl:if test="$showLengthOfService_var='Y'">
																							<td >
																									<xsl:value-of select="./lengthOfService/text()" />
																							</td>
																						</xsl:if>
																					</xsl:if>
													  						</tr>
													  						</xsl:if>
																		</xsl:for-each>
																		
																		<!-- for loop ends -->			
							       									</tbody>
							     								</table>
																
															</xsl:if>
														<!--  Table display ends -->
														<!-- 2.4.3.3	Paragraph 3 -->
														
														</xsl:if>
														<xsl:variable name="otherOption2_var">
																<xsl:value-of select="eligibilitySection/otherOptionSelectedAnyMoneyType"/>
														</xsl:variable>
														<xsl:variable name="isMoneyTypeMissingInTheTable_var">
																<xsl:value-of select="eligibilitySection/isMoneyTypeMissingInTheTable"/>
														</xsl:variable>
														<xsl:variable name="isMoneyTypeWihtHoursGreaterThanZero_var">
																<xsl:value-of select="eligibilitySection/isMoneyTypeWihtHoursGreaterThanZero"/>
														</xsl:variable>	
														
														<xsl:choose>
															<xsl:when test="$isMoneyTypeWihtHoursGreaterThanZero_var='Y'">
																	<xsl:for-each select="messages/message">
																		<xsl:if test="@id='eligibilityMoneyTypeHoursGreaterThanZero'">
																			<xsl:value-of select="*|text()"/>
																		</xsl:if>
																	</xsl:for-each>
															</xsl:when>
															<xsl:otherwise>
																<xsl:if test="$otherOption2_var='Y' or $isMoneyTypeMissingInTheTable_var='Y'">
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityExcludedEmployeesOtherMSG2'">
																				<xsl:value-of select="*|text()"/>
																			</xsl:if>
																		</xsl:for-each>
																</xsl:if>
															</xsl:otherwise>
														</xsl:choose>
								
									</xsl:if> <!--  multipleEligibilityRulesForOneSingleMoneyType_var checking if ends here -->
								  </xsl:if>
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>
								</td>
							</tr>	
							
							<!-- If the Plan Entry freq is empty , do not show this section -->
							<xsl:variable name="planEntryFrequency_chk_var">
											<xsl:value-of select="entryDatesSection/planEntryFrequency"/>
							</xsl:variable>
							<xsl:variable name="isPlanHasAutoEnrollment_var">
											<xsl:value-of select="entryDatesSection/isPlanHasAutoEnrollment"/>
							</xsl:variable>
							
							<xsl:if test="$planEntryFrequency_chk_var!='Empty'"> 
							
								<tr>
									<td >
											<xsl:value-of select="pageSectionText/entryDates" />
									</td>
									<td >
											<xsl:variable name="planEntryFrequency_var">
												<xsl:value-of select="entryDatesSection/planEntryFrequency"/>
											</xsl:variable>
											<xsl:variable name="entryDatesSpecifiedAnyMoneyType_var">
												<xsl:value-of select="entryDatesSection/entryDatesSpecifiedAnyMoneyType"/>
											</xsl:variable>
										
											<!-- <xsl:if test="$planEntryFrequency_var!='Complex'"> --> 
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='entryDatesSection'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
												</xsl:for-each>
											<!-- </xsl:if> -->
											
											<!-- *************Table else -->
										
											<xsl:if test="$planEntryFrequency_var='Complex'"> 	
												
													<!-- 3.1.4 --><!-- Once you become eligible and join the plan, contributions will begin on the next: -->
												<xsl:variable name="planEntryDatesTableLead_var">
														<xsl:value-of select="tableHeaderSection/planEntryDatesTableLead" />
												</xsl:variable>
												<xsl:if test="$planEntryDatesTableLead_var!=''">
														<xsl:value-of select="tableHeaderSection/planEntryDatesTableLead" />
												</xsl:if>
																									
												<table border="1">
			
													
														<tr>
																<td >
																		<xsl:value-of select="tableHeaderSection/moneyType" />
																</td>
																<td >
																		<xsl:value-of select="tableHeaderSection/planEntryDates" />
																</td>
														</tr>
														
														<!-- For loop -->
														<xsl:for-each select="entryDatesSection/typeAndEntry">
															<tr>
																	<td>
																			<xsl:value-of select="./moneyType/text()" /> 
																	</td>
									
																	<td >
																			<xsl:value-of select="./planEntryDates/text()" />
																	</td>
									  						</tr>
														</xsl:for-each>
														<!-- for loop ends -->
														
			       									
			     								</table>
			     								
			     								<xsl:if test="$entryDatesSpecifiedAnyMoneyType_var='Y'"> 
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='entryDatesSpecifiedAnyMoneyType'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
											    </xsl:if> 
			     								
											</xsl:if>
												<!-- ****************table else ends -->
																				
											<xsl:if test="$isPlanHasAutoEnrollment_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='automaticEnrollmentFeatureMSG'">
															<xsl:value-of select="*|text()" />
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
									</td>
								</tr>
							  </xsl:if>  <!--  End of Plan Entry frq empty check ends -->	
							  <!-- Your Contributions Specified or unspecified  -->
							<xsl:variable name="yourContributionsMTSpecified_var">
								<xsl:value-of select="yourContributionsSection/yourContributionsMTSpecified"/>
							</xsl:variable>
							<!-- all paragraph_ empty display  -->
							<xsl:variable name="allParagraphEmpty_var">
								<xsl:value-of select="yourContributionsSection/allParagraphEmpty"/>
							</xsl:variable>	
							<xsl:if test="$yourContributionsMTSpecified_var='Y'"> 
									<tr>
										<td>
												<xsl:value-of select="pageSectionText/yourContributions" />
										</td>
										<td >
											<xsl:variable name="paragraph_1_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_1"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_1_2_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_2"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_1_3_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_3"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_2_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_2_1"/>
											</xsl:variable>
											<xsl:variable name="paragraph_3_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_3_1"/>
											</xsl:variable>
											<xsl:variable name="paragraph_3_2_var">
													<xsl:value-of select="yourContributionsSection/paragraph_3_2"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_4_var">
													<xsl:value-of select="yourContributionsSection/paragraph_4"/>
											</xsl:variable>
											
											
																				
												<xsl:if test="$yourContributionsMTSpecified_var='N'"> 
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='yourContributionsMTSpecified_MSG'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												<!--  Specified = Y only we can display other data -->
												<xsl:if test="$yourContributionsMTSpecified_var='Y'">
														
													<xsl:if test="$allParagraphEmpty_var='Y'"> 
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourContributionsMTSpecified_MSG'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
													</xsl:if>  
													<!-- paragraph_1_1 display  -->
															<xsl:if test="$paragraph_1_1_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_1'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>
															<!-- paragraph_1_2 display  -->
															<xsl:if test="$paragraph_1_2_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_2'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>										
															<!-- paragraph_1_3 display  -->
															<xsl:if test="$paragraph_1_3_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_3'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>		
	
														<!-- paragraph_2_1 display  -->
														<xsl:if test="$paragraph_2_1_var='Y'">
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_2_1'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
													<!-- paragraph_3_1 display  -->
														<xsl:if test="$paragraph_3_1_var='Y'"> 
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_3_1'">
																	<xsl:value-of select="*|text()"/>&#160;
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
														<!-- paragraph_3_2 display  -->
														<xsl:if test="$paragraph_3_2_var='Y'"> 
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_3_2'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
													<!-- paragraph_4_1 display  -->
													
														<xsl:if test="$paragraph_4_var='Y'"> 
															<xsl:if test="$paragraph_1_2_var='Y' or $paragraph_1_1_var='Y' or $paragraph_1_3_var='Y' or $paragraph_2_1_var='Y' or $paragraph_3_1_var='Y' or $paragraph_3_2_var='Y'"> 
														     </xsl:if>
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_4'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>
												</xsl:if>
										</td>
									</tr>
							</xsl:if> <!-- Your Contribution Unspecified check ends -->
							<!--  Your Employer Contribution MT not specified And Rules are not specified skip this section -->
							 <xsl:variable name="isEmployerMoneyTypeSpecified_var">
								<xsl:value-of select="yourERContributionsSection/isEmployerMoneyTypeSpecified"/>
							</xsl:variable>
							<xsl:variable name="isContributionRulesAreSpecified_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRulesAreSpecified"/>
							</xsl:variable>
							<xsl:variable name="isContributionRuleNotSpecifiedForAnyMoneyType_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRuleNotSpecifiedForAnyMoneyType"/>
							</xsl:variable>
							<xsl:variable name="isContributionRuleNotSpecifiedForAllAnyMoneyType_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRuleNotSpecifiedForAllAnyMoneyType"/>
							</xsl:variable>
							
							<xsl:if test="$isEmployerMoneyTypeSpecified_var!='N'">
									<tr>
										<td>
												<xsl:value-of select="pageSectionText/yourERContributions" />
										</td>
										<td>
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRulesAreSpecified_var='N' and $isContributionRuleNotSpecifiedForAnyMoneyType_var='N' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='N'">
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourERContributionsNotSpecifiedAllMoneyType'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
												</xsl:if>
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='Y'">
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourERContributionsNotSpecifiedAllMoneyType'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
												</xsl:if>
												<!-- Table for MoneyType Employer Contribution -->
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRulesAreSpecified_var='Y'">
												
													<xsl:variable name="yourERContributionTableLead_var">
															<xsl:value-of select="tableHeaderSection/yourERContributionTableLead" /> <!-- 5.1.1 -->
													</xsl:variable>
													<xsl:if test="$yourERContributionTableLead_var!=''">
															<xsl:value-of select="tableHeaderSection/yourERContributionTableLead" /> <!-- 5.1.1 -->
													</xsl:if>
													<table border="1">
				
														<tbody>
															<tr>
																	<td>
																			<xsl:value-of select="tableHeaderSection/moneyType" />
																	</td>
																	<td>
																			<xsl:value-of select="tableHeaderSection/yourERContribution" />
																	</td>
															</tr>
															
															<!-- For loop -->
															<xsl:for-each select="yourERContributionsSection/employerContributions">
																<tr>
																		<td >
																				<xsl:value-of select="./moneyType/text()" /> 
																		</td>
										
																		<td>
																					<xsl:value-of select="./contributionDesc/text()" />
																		</td>
										  						</tr>
															</xsl:for-each>
															<!-- for loop ends -->
															
				       									</tbody>
				     								</table>
				     								
				     								<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRuleNotSpecifiedForAnyMoneyType_var='Y' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='N'">
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourERContributionsNotSpecifiedAnyMoneyType'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
													</xsl:if>
												</xsl:if>
													<!-- ****************table else ends -->
												
											
										</td>
									</tr>
								</xsl:if> <!-- Your Employer Contribution MT not specified and Rules are not specified IF ENDS-->
							<tr>
								<td >
										<xsl:value-of select="pageSectionText/vesting" />
								</td>
								<td>
										<xsl:variable name="vestingSectionSpecified_var">
											<xsl:value-of select="vestingSection/vestingSectionSpecified"/>
										</xsl:variable>
										<xsl:variable name="twoOrMoreVestingSchedulesForAnySingleMT_var">
											<xsl:value-of select="vestingSection/twoOrMoreVestingSchedulesForAnySingleMT"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_1_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_1"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_2_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_2"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_3_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_3"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_4_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_4"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_2_var">
											<xsl:value-of select="vestingSection/vestingParagraph_2"/>
										</xsl:variable>
										<xsl:variable name="isAllYearsOfServiceSpecified100Percent_var">
											<xsl:value-of select="vestingSection/isAllYearsOfServiceSpecified100Percent"/>
										</xsl:variable>
										<xsl:variable name="showZeroYr_var">
											<xsl:value-of select="vestingSection/showZeroYr"/>
										</xsl:variable>
										
										
										<xsl:variable name="isEmployerVestingSchedulesIdentical_var">
											<xsl:value-of select="vestingSection/isEmployerVestingSchedulesIdentical"/>
										</xsl:variable>
										
										
												<xsl:if test="$twoOrMoreVestingSchedulesForAnySingleMT_var='Y'">
													<xsl:for-each select="messages/message">
															<xsl:if test="@id='vestingSection_1_1'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
													</xsl:for-each>
																							
												</xsl:if>
												
										<xsl:if test="$twoOrMoreVestingSchedulesForAnySingleMT_var='N'">
										
												<!-- <xsl:if test="$vestingSectionSpecified_var='N'">
													<xsl:for-each select="messages/message">
															<xsl:if test="@id='vestingSection_1_1'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
													</xsl:for-each>
												</xsl:if> -->
												<xsl:if test="$vestingParagraph_1_1_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_1'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
									
												<xsl:if test="$vestingParagraph_1_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_2'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:if test="$vestingParagraph_1_3_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_3'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
																								
												</xsl:if>
												
												<xsl:if test="$vestingParagraph_1_4_var='Y' or $isAllYearsOfServiceSpecified100Percent_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_4'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
																						
												</xsl:if>
												<!-- <xsl:if test="$isAllYearsOfServiceSpecified100Percent_var='Y'">
														<xsl:value-of select="vestingSection/allYearsOfServiceSpecified100PercentMsg" />
												</xsl:if> -->
										
									<xsl:if test="$vestingSectionSpecified_var='Y'">
												
												<xsl:if test="$isEmployerVestingSchedulesIdentical_var='Y' and $isAllYearsOfServiceSpecified100Percent_var='N'">
														<xsl:value-of select="tableHeaderSection/vestingOneScheduleTableLead" />
													<table border="1">
														
														<tbody>
															<tr>
																	<td>
																			 <xsl:value-of select="tableHeaderSection/yearOfServiceForOneSchedule" />
																	</td>
																	<td>
																			<xsl:value-of select="tableHeaderSection/percentVestedForOneSchedule" />
																	</td>
															</tr>
															
															<xsl:for-each select="vestingSection/employerContributionsIdentical">
																<tr>
																		<td>
																				<xsl:value-of select="./yearOfService/text()" /> 
																		</td>										
																		<td>
																				<xsl:value-of select="./percentVested/text()" />
																		</td>
										  						</tr>
															</xsl:for-each>
														</tbody>
				     								</table>		
													
												
												</xsl:if>
												
												<xsl:if test="$isEmployerVestingSchedulesIdentical_var='N' and $isAllYearsOfServiceSpecified100Percent_var='N'">
														<xsl:value-of select="tableHeaderSection/vestingMultipleScheduleTableLead" />
													<table border="1">
															<!--<fo:table-column column-width="5.70cm"	xsl:use-attribute-sets="table-border" />
															<xsl:if test="$showZeroYr_var='Y'">
																<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															</xsl:if>
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" /> -->												
				
														<tbody>
															<tr>
																	<td rowspan="2">
																			<xsl:value-of select="tableHeaderSection/moneyType" />
																	</td>
																	<td colspan="7">
																	<xsl:if test="$showZeroYr_var='Y'">
																				<xsl:value-of select="tableHeaderSection/percentVestedForMultipleSchedule" />
																	</xsl:if>
																	<xsl:if test="$showZeroYr_var='N'">
																				<xsl:value-of select="tableHeaderSection/percentVestedForMultipleSchedule" />
																	</xsl:if>
																	</td>
															</tr>		
															<tr>
															<xsl:if test="$showZeroYr_var='Y'">
																<td> <b>0</b>
																			<!--<fo:block xsl:use-attribute-sets="table-header-style">
																				<fo:inline font-weight="bold">0</fo:inline>
																			</fo:block> -->
																</td>
															</xsl:if>
															<td> <b>1</b>
																		<!-- <fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">1</fo:inline>
																		</fo:block> -->
															</td>
															<td ><b>2</b>
																	<!-- 	<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">2</fo:inline>
																		</fo:block> -->
															</td>
															<td ><b>3</b>
																	<!-- 	<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">3</fo:inline>
																		</fo:block> -->
															</td>
															<td ><b>4</b>
																	<!-- 	<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">4</fo:inline>
																		</fo:block> -->
															</td>
															<td ><b>5</b>
																	<!-- 	<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">5</fo:inline>
																		</fo:block> -->
															</td>
															<td  ><b> 6+ </b>
																	<!-- 	<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">6+</fo:inline>
																		</fo:block> -->
															</td>
															
															
															
															</tr>										
															
															
															<xsl:for-each select="vestingSection/employerContributions">
																<tr>
																		<td>
																				<xsl:value-of select="./moneyType/text()" /> 
																		</td>										
																		<xsl:if test="$showZeroYr_var='Y'">
																			<td>
																					<xsl:value-of select="./percentVested0/text()" />
																			</td>
																		</xsl:if>
																		<td>
																				<xsl:value-of select="./percentVested1/text()" />
																		</td>
																		<td >
																				<xsl:value-of select="./percentVested2/text()" />
																		</td>
																		<td>
																				<xsl:value-of select="./percentVested3/text()" />
																		</td>
																		<td >
																				<xsl:value-of select="./percentVested4/text()" />
																		</td>
																		<td>
																				<xsl:value-of select="./percentVested5/text()" />
																		</td>
																		<td>
																				<xsl:value-of select="./percentVested6/text()" />
																		</td>
										  						</tr>
															</xsl:for-each>
														</tbody>
				     								</table>				
																			
												</xsl:if>								
										</xsl:if>
										
												<xsl:if test="$vestingParagraph_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_2'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
									</xsl:if> <!--  end if  twoOrMoreVestingSchedulesForAnySingleMT_Var checking -->		
								</td>
							</tr>
							<tr>
								<td >
										<xsl:value-of select="pageSectionText/loans" />
								</td>
								<td>
									
										<xsl:variable name="loanSpecified_var">
											<xsl:value-of select="loansSection/loanSpecified"/>
										</xsl:variable>
										
										<xsl:if test="$loanSpecified_var='N'">
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='loansSentence_0'">
																	<xsl:value-of select="*|text()"/>&#160;
															</xsl:if>
														</xsl:for-each>
										</xsl:if>
										<xsl:if test="$loanSpecified_var='U'">
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='loansSentence_0'">
																		<xsl:value-of select="*|text()"/>&#160;
																</xsl:if>
															</xsl:for-each>
										</xsl:if>
									
									<xsl:if test="$loanSpecified_var='Y'">
									
												<xsl:variable name="loansSentence_1_var">
													<xsl:value-of select="loansSection/loansSentence_1"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_1_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_1'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_2_var">
													<xsl:value-of select="loansSection/loansSentence_2"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_2'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_3_var">
													<xsl:value-of select="loansSection/loansSentence_3"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_3_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_3'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_4_var">
													<xsl:value-of select="loansSection/loansSentence_4"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_4_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_4'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_5_var">
													<xsl:value-of select="loansSection/loansSentence_5"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_5_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_5'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
										</xsl:if> <!-- loanSpecified - Y if ENDs -->
								</td>
							</tr>
							<tr>
								<td>
										<xsl:value-of select="pageSectionText/withdrawals" />
								</td>
								<td >
										<xsl:variable name="withdrawalSentence_1_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_1'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>

										<xsl:variable name="withdrawalSentence_1_2_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_2"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_2_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_2'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
										<xsl:variable name="withdrawalSentence_1_3_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_3"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_3_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_3'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									<!-- 2nd paragraph -->
										<xsl:variable name="withdrawalSentence_2_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_1'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
										<xsl:variable name="withdrawalSentence_2_2_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_2"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_2_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_2'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
								
										<xsl:variable name="withdrawalSentence_2_3_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_3"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_3_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_3'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
									
										<xsl:variable name="withdrawalSentence_3_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_3_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_3_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_3_1'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
								</td>
							</tr>
							<tr>
								<td>
										<xsl:value-of select="pageSectionText/investmentOptions" />
								</td>
								<td> 
										<xsl:for-each select="messages/message">
												<xsl:if test="@id='investmentParagraph'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										<xsl:for-each select="messages/message">
												<xsl:if test="@id='investmentParagraph_2'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
								</td>
							</tr>
							<tr>
								<td>
										<xsl:value-of select="pageSectionText/reportingAndChanges" />
								</td>
								<td>
											<xsl:for-each select="messages/message">
													<xsl:if test="@id='reportingParagraph_1'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
											</xsl:for-each>
											<xsl:for-each select="messages/message">
													<xsl:if test="@id='reportingParagraph_2'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
											</xsl:for-each>
								</td>
							</tr>
						</tbody>
					</table>
</xsl:template>
</xsl:stylesheet>