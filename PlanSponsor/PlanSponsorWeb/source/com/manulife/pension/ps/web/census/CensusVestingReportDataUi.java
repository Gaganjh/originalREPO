package com.manulife.pension.ps.web.census;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;

/**
 * This is the UI value object for the {@link CensusVestingReportData}. It wraps up logic that is
 * used in the UI.
 * 
 * @author glennpa
 */
public class CensusVestingReportDataUi extends BaseSerializableCloneableObject {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    public static final int PERCENTAGE_SCALE = 3;

    private CensusVestingReportData censusVestingReportData;

    private List<List<BigDecimal>> participantPercentages;

    private boolean displayEditButton;

    private boolean displaySaveButton;

    private boolean displayCancelButton;

    private List<CensusVestingReportDataRowUi> rows;

    /**
     * Default Constructor.
     */
    public CensusVestingReportDataUi() {
    }

    /**
     * Default Constructor with the given {@link CensusVestingReportData}.
     * 
     * @param censusVestingReportData The census vesting report data this UI is driven with.
     */
    public CensusVestingReportDataUi(final CensusVestingReportData censusVestingReportData) {
        this.censusVestingReportData = censusVestingReportData;
    }

    /**
     * @return CensusVestingReportData - The censusVestingReportData.
     */
    public CensusVestingReportData getCensusVestingReportData() {
        return censusVestingReportData;
    }

    /**
     * @param censusVestingReportData - The censusVestingReportData to set.
     */
    public void setCensusVestingReportData(final CensusVestingReportData censusVestingReportData) {
        this.censusVestingReportData = censusVestingReportData;

        populateParticipantPercentagesFromCensusVestingReportData();

    }

    /**
     * Determines if the edit button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplayEditButton() {
        return displayEditButton;
    }

    /**
     * Determines if the save button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplaySaveButton() {
        return displaySaveButton;
    }

    /**
     * Determines if the cancel button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplayCancelButton() {
        return displayCancelButton;
    }

    /**
     * @param displayEditButton - The displayEditButton to set.
     */
    public void setDisplayEditButton(final boolean displayEditButton) {
        this.displayEditButton = displayEditButton;
    }

    /**
     * @param displaySaveButton - The displaySaveButton to set.
     */
    public void setDisplaySaveButton(final boolean displaySaveButton) {
        this.displaySaveButton = displaySaveButton;
    }

    /**
     * @param displayCancelButton - The displayCancelButton to set.
     */
    public void setDisplayCancelButton(final boolean displayCancelButton) {
        this.displayCancelButton = displayCancelButton;
    }

    public void populateParticipantPercentagesFromCensusVestingReportData() {

        final CensusVestingReportData censusVestingReportData = getCensusVestingReportData();

        if (censusVestingReportData != null) {
            final List<MoneyTypeVO> moneyTypes = censusVestingReportData.getMoneyTypes();

            final Collection participantDetails = censusVestingReportData.getDetails();

            final ArrayList<List<BigDecimal>> newParticipantPercentages = new ArrayList<List<BigDecimal>>(
                    participantDetails.size());

            for (CensusVestingDetails censusVestingDetails : (Collection<CensusVestingDetails>) participantDetails) {
                final Map<String, MoneyTypeVestingPercentage> percentagesByMoneyType = censusVestingDetails
                        .getPercentages();

                final List<BigDecimal> row = new ArrayList<BigDecimal>(moneyTypes.size());

                newParticipantPercentages.add(row);

                for (MoneyTypeVO moneyTypeVO : moneyTypes) {
                    final MoneyTypeVestingPercentage moneyTypeVestingPercentage = percentagesByMoneyType
                            .get(moneyTypeVO.getId());
                    // if (!(StringUtils.equals(MoneyTypeVO.FULLY_VESTED_VALUE_YES,
                    // moneyTypeVO.getFullyVested()))) {
                    row.add(moneyTypeVestingPercentage.getPercentage());
                    // } // fi
                } // end for - Money Types
            } // end for - Participant Data (each row).

            this.participantPercentages = newParticipantPercentages;

//            final Logger logger = Logger.getLogger(CensusVestingReportForm.class);
//            logger.debug("\nparticipantPercentages:\n" + participantPercentages);

        } // fi

        loadRows();
    }

    /**
     * @return List<List<BigDecimal>> - The participantPercentages.
     */
    public List<List<BigDecimal>> getParticipantPercentages() {
        return participantPercentages;
    }

    /**
     * @return List<Row> - The rows.
     */
    public List<CensusVestingReportDataRowUi> getRows() {
        return rows;
    }

    /**
     * @param rows - The rows to set.
     */
    public void setRows(List<CensusVestingReportDataRowUi> rows) {
        this.rows = rows;
    }

    public void loadRows() {
        List<CensusVestingReportDataRowUi> result = new ArrayList<CensusVestingReportDataRowUi>(
                participantPercentages.size());
        for (List<BigDecimal> rows : participantPercentages) {
            CensusVestingReportDataRowUi row = new CensusVestingReportDataRowUi();
            result.add(row);
            List<CensusVestingReportDataRowValueUi> moneyTypePercentages = new ArrayList<CensusVestingReportDataRowValueUi>(
                    rows.size());
            row.setMoneyTypeValues(moneyTypePercentages);
            for (BigDecimal bigDecimal : rows) {
                moneyTypePercentages.add(new CensusVestingReportDataRowValueUi(
                        formatPercentage(bigDecimal), bigDecimal));
            }
        }
        setRows(result);
    }

    private String formatPercentage(final BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } // fi

        return NumberFormat.getNumberInstance().format(
                bigDecimal.setScale(PERCENTAGE_SCALE, BigDecimal.ROUND_HALF_DOWN));
    }

    private BigDecimal parsePercentage(final String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        } // fi

        try {
            return new BigDecimal(NumberFormat.getNumberInstance().parse(string).floatValue())
                    .setScale(PERCENTAGE_SCALE, BigDecimal.ROUND_HALF_DOWN);
        } catch (ParseException parseException) {
            throw new RuntimeException(parseException);
        } // end try/catch
    }

    public class CensusVestingReportDataRowUi extends BaseSerializableCloneableObject {
        private List<CensusVestingReportDataRowValueUi> moneyTypeValues;

        /**
         * @return String - The value.
         */
        public List<CensusVestingReportDataRowValueUi> getMoneyTypeValues() {
            return moneyTypeValues;
        }

        /**
         * @param value - The value to set.
         */
        public void setMoneyTypeValues(final List<CensusVestingReportDataRowValueUi> moneyTypeValues) {
            this.moneyTypeValues = moneyTypeValues;
        }
    }

    public class CensusVestingReportDataRowValueUi extends BaseSerializableCloneableObject {
        private String value;

        private BigDecimal originalValue;

        /**
         * Default Constructor.
         * 
         * @param value
         */
        private CensusVestingReportDataRowValueUi(final String value, final BigDecimal originalValue) {
            super();
            this.value = value;
            this.originalValue = originalValue;
        }

        /**
         * @return String - The value.
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value - The value to set.
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return BigDecimal - The originalValue.
         */
        public BigDecimal getOriginalValue() {
            return originalValue;
        }

        /**
         * @param originalValue - The originalValue to set.
         */
        public void setOriginalValue(BigDecimal originalValue) {
            this.originalValue = originalValue;
        }

        public BigDecimal getBigDecimalValue() {
            return parsePercentage(getValue());
        }
    }
}