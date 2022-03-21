package com.manulife.pension.ps.service.submission.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;

/**
 * @author parkand
 *
 */
public class CensusSubmissionItemComparatorFactory {

	private static CensusSubmissionItemComparatorFactory instance = new CensusSubmissionItemComparatorFactory(); 
	private Map comparators;

	private static final String DESCENDING_INDICATOR = "DESC";
	
	private CensusSubmissionItemComparatorFactory() {
		super();
		comparators = new HashMap(6);
		comparators.put(CensusSubmissionReportData.SORT_RECORD_NUMBER, 
				new CensusSubmissionItemRecordNumberComparator());
		comparators.put(CensusSubmissionReportData.SORT_SSN,
				new CensusSubmissionItemSSNComparator());
        comparators.put(CensusSubmissionReportData.SORT_EMP_ID,
                new CensusSubmissionItemEmployeeIdComparator());
		comparators.put(CensusSubmissionReportData.SORT_CITY,
				new CensusSubmissionItemCityComparator());
		comparators.put(CensusSubmissionReportData.SORT_STATE,
				new CensusSubmissionItemStateComparator());
		comparators.put(CensusSubmissionReportData.SORT_STATUS,
				new CensusSubmissionItemStatusComparator());
	}

	public static CensusSubmissionItemComparatorFactory getInstance() {
		return instance;
	}

	private class CensusSubmissionItemStatusComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemStatusComparator() {
			super();
		}
		
		public CensusSubmissionItemStatusComparator(boolean ascending) {
			super(ascending);
		}

		public int compare(Object arg0, Object arg1) {

			Integer number0 = MIN_INTEGER;
			Integer number1 = MIN_INTEGER;

			if (arg0 != null) {
				number0 = new Integer(((CensusSubmissionItem)arg0).getStatusSortOder());
			}
			if (arg1 != null) {
				number1 = new Integer(((CensusSubmissionItem)arg1).getStatusSortOder());
			}
			
			int result = (isAscending() ? 1 : -1) * number0.compareTo(number1);
			
			// if they are equal, goto the secondary sort by record number descending always
			if (result == 0) {
				result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
			}
			return result;
		}
	}

	private class CensusSubmissionItemRecordNumberComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemRecordNumberComparator() {
			super();
		}
		
		public CensusSubmissionItemRecordNumberComparator(boolean ascending) {
			super(ascending);
		}
		
		public int compare(Object arg0, Object arg1) {

			Integer number0 = null;
			Integer number1 = null;
			
			if (arg0 != null) {
				number0 = ((CensusSubmissionItem)(arg0)).getSourceRecordNo();
			}
			if (arg1 != null) {
				number1 = ((CensusSubmissionItem)(arg1)).getSourceRecordNo();
			}
			
			if (number0 == null) {
				number0 = MIN_INTEGER;
			}
			if (number1 == null) {
				number1 = MIN_INTEGER;
			}

			return (isAscending() ? 1 : -1) * number0.compareTo(number1);
		}
	}

	private class CensusSubmissionItemFirstNameComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemFirstNameComparator() {
			super();
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((CensusSubmissionItem)(arg0)).getFirstName();
			}
			if (arg1 != null) {
				name1 = ((CensusSubmissionItem)(arg1)).getFirstName();
			}
			if (name0 == null) {
				name0 = EMPTY_STRING;
			}
			if (name1 == null) {
				name1 = EMPTY_STRING;
			}
			int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
			
			// if they are equal, goto the secondary sort by tracing (submission) number descending always
			if (result == 0) {
				result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
			}
			return result;
		}
	}
    
    
    private class CensusSubmissionItemLastNameComparator extends SubmissionHistoryItemComparator {

        public CensusSubmissionItemLastNameComparator() {
            super();
        }

        public int compare(Object arg0, Object arg1) {

            String name0 = null;
            String name1 = null;

            if (arg0 != null) {
                name0 = ((CensusSubmissionItem)(arg0)).getLastName();
            }
            if (arg1 != null) {
                name1 = ((CensusSubmissionItem)(arg1)).getLastName();
            }
            if (name0 == null) {
                name0 = EMPTY_STRING;
            }
            if (name1 == null) {
                name1 = EMPTY_STRING;
            }
            int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
            
            // if they are equal, goto the secondary sort by tracing (submission) number descending always
            if (result == 0) {
                result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
            }
            return result;
        }
    }
    
    private class CensusSubmissionItemNamePrefixComparator extends SubmissionHistoryItemComparator {

        public CensusSubmissionItemNamePrefixComparator() {
            super();
        }

        public int compare(Object arg0, Object arg1) {

            String name0 = null;
            String name1 = null;

            if (arg0 != null) {
                name0 = ((CensusSubmissionItem)(arg0)).getNamePrefix();
            }
            if (arg1 != null) {
                name1 = ((CensusSubmissionItem)(arg1)).getNamePrefix();
            }
            if (name0 == null) {
                name0 = EMPTY_STRING;
            }
            if (name1 == null) {
                name1 = EMPTY_STRING;
            }
            int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
            
            // if they are equal, goto the secondary sort by tracing (submission) number descending always
            if (result == 0) {
                result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
            }
            return result;
        }
    }

    private class CensusSubmissionItemMiddleInitialComparator extends SubmissionHistoryItemComparator {

        public CensusSubmissionItemMiddleInitialComparator() {
            super();
        }

        public int compare(Object arg0, Object arg1) {

            String name0 = null;
            String name1 = null;

            if (arg0 != null) {
                name0 = ((CensusSubmissionItem)(arg0)).getMiddleInitial();
            }
            if (arg1 != null) {
                name1 = ((CensusSubmissionItem)(arg1)).getMiddleInitial();
            }
            if (name0 == null) {
                name0 = EMPTY_STRING;
            }
            if (name1 == null) {
                name1 = EMPTY_STRING;
            }
            int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
            
            // if they are equal, goto the secondary sort by tracing (submission) number descending always
            if (result == 0) {
                result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
            }
            return result;
        }
    }
	private class CensusSubmissionItemEmployeeIdComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemEmployeeIdComparator() {
			super();
		}
		
		public CensusSubmissionItemEmployeeIdComparator(boolean ascending) {
			super(ascending);
		}
		
		public int compare(Object arg0, Object arg1) {

			String number0 = null;
			String number1 = null;
			
			if (arg0 != null) {
				number0 = ((CensusSubmissionItem)(arg0)).getEmployeeNumber();
			}
			if (arg1 != null) {
				number1 = ((CensusSubmissionItem)(arg1)).getEmployeeNumber();
			}
			
			if (number0 == null) {
				number0 = EMPTY_STRING;
			}
			if (number1 == null) {
				number1 = EMPTY_STRING;
			}

			return (isAscending() ? 1 : -1) * number0.compareTo(number1);
		}
	}
    
    private class CensusSubmissionItemSSNComparator extends SubmissionHistoryItemComparator {

        public CensusSubmissionItemSSNComparator() {
            super();
        }
        
        public CensusSubmissionItemSSNComparator(boolean ascending) {
            super(ascending);
        }
        
        public int compare(Object arg0, Object arg1) {

            String number0 = null;
            String number1 = null;
            
            if (arg0 != null) {
                number0 = ((CensusSubmissionItem)(arg0)).getSsn();
            }
            if (arg1 != null) {
                number1 = ((CensusSubmissionItem)(arg1)).getSsn();
            }
            
            if (number0 == null) {
                number0 = EMPTY_STRING;
            }
            if (number1 == null) {
                number1 = EMPTY_STRING;
            }

            return (isAscending() ? 1 : -1) * number0.compareTo(number1);
        }
    }

	
	private class CensusSubmissionItemCityComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemCityComparator() {
			super();
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((CensusSubmissionItem)(arg0)).getCity();
			}
			if (arg1 != null) {
				name1 = ((CensusSubmissionItem)(arg1)).getCity();
			}
			if (name0 == null) {
				name0 = EMPTY_STRING;
			}
			if (name1 == null) {
				name1 = EMPTY_STRING;
			}
			int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
			
			// if they are equal, goto the secondary sort by tracing (submission) number descending always
			if (result == 0) {
				result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
			}
			return result;
		}
	}
	
	private class CensusSubmissionItemStateComparator extends SubmissionHistoryItemComparator {

		public CensusSubmissionItemStateComparator() {
			super();
		}

		public int compare(Object arg0, Object arg1) {

			String name0 = null;
			String name1 = null;

			if (arg0 != null) {
				name0 = ((CensusSubmissionItem)(arg0)).getStateCode();
			}
			if (arg1 != null) {
				name1 = ((CensusSubmissionItem)(arg1)).getStateCode();
			}
			if (name0 == null) {
				name0 = EMPTY_STRING;
			}
			if (name1 == null) {
				name1 = EMPTY_STRING;
			}
			int result = (isAscending() ? 1 : -1) * name0.compareToIgnoreCase(name1);
			
			// if they are equal, goto the secondary sort by tracing (submission) number descending always
			if (result == 0) {
				result = new CensusSubmissionItemRecordNumberComparator(true).compare(arg0, arg1);
			}
			return result;
		}
	}
	
	public Comparator getComparator(String sortField, String sortDirection) {
		SubmissionHistoryItemComparator comparator = 
			comparators.containsKey(sortField) ? (SubmissionHistoryItemComparator) comparators.get(sortField) : 
												 (SubmissionHistoryItemComparator) comparators.get(SubmissionHistoryReportData.SORT_SUBMISSION_DATE);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection)); 	
		return comparator;
	}
	

}
