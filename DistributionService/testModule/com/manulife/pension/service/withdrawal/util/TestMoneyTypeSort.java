package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.collections.ComparatorUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * Test class for MoneyTypeSort.
 * 
 * @author Kristin Kerr
 */
public class TestMoneyTypeSort {

    /**
     * Tests sorting of WithdrawalRequestMoneyTypes first by ascending money type category code,
     * then by ascending money type short name. The money type short name should be the custom short
     * name (moneyTypeAliasId) if available, otherwise the Manulife short name (moneyTypeId).
     * 
     */
    @Test
    public void testSortMoneyTypesArray() {

        WithdrawalRequestMoneyType first = new WithdrawalRequestMoneyType();
        first.setMoneyTypeCategoryCode("EE");
        first.setMoneyTypeAliasId("AAAAA");
        first.setMoneyTypeId(null);

        WithdrawalRequestMoneyType second = new WithdrawalRequestMoneyType();
        second.setMoneyTypeCategoryCode("EE");
        second.setMoneyTypeAliasId("BBBBB");
        second.setMoneyTypeId("FFFFF");

        WithdrawalRequestMoneyType third = new WithdrawalRequestMoneyType();
        third.setMoneyTypeCategoryCode("EE");
        third.setMoneyTypeAliasId("CCCCC");
        third.setMoneyTypeId("EEEEE");

        WithdrawalRequestMoneyType forth = new WithdrawalRequestMoneyType();
        forth.setMoneyTypeCategoryCode("EE");
        forth.setMoneyTypeAliasId(null);
        forth.setMoneyTypeId("DDDDD");

        WithdrawalRequestMoneyType fifth = new WithdrawalRequestMoneyType();
        fifth.setMoneyTypeCategoryCode("ER");
        fifth.setMoneyTypeAliasId("AAAAA");
        fifth.setMoneyTypeId(null);

        WithdrawalRequestMoneyType sixth = new WithdrawalRequestMoneyType();
        sixth.setMoneyTypeCategoryCode("ER");
        sixth.setMoneyTypeAliasId("BBBBB");
        sixth.setMoneyTypeId("FFFFF");

        WithdrawalRequestMoneyType seventh = new WithdrawalRequestMoneyType();
        seventh.setMoneyTypeCategoryCode("ER");
        seventh.setMoneyTypeAliasId("CCCCC");
        seventh.setMoneyTypeId("EEEEE");

        WithdrawalRequestMoneyType eighth = new WithdrawalRequestMoneyType();
        eighth.setMoneyTypeCategoryCode("ER");
        eighth.setMoneyTypeAliasId(null);
        eighth.setMoneyTypeId("DDDDD");

        WithdrawalRequestMoneyType[] moneyTypes = { third, eighth, first, sixth, second, seventh,
                forth, fifth };

        Arrays.sort(moneyTypes, ComparatorUtils.nullLowComparator(new MoneyTypeComparator()));

        assertEquals("First money type", first, moneyTypes[0]);
        assertEquals("Second money type", second, moneyTypes[1]);
        assertEquals("Third money type", third, moneyTypes[2]);
        assertEquals("Forth money type", forth, moneyTypes[3]);
        assertEquals("Fifth money type", fifth, moneyTypes[4]);
        assertEquals("Sixth money type", sixth, moneyTypes[5]);
        assertEquals("Seventh money type", seventh, moneyTypes[6]);
        assertEquals("Eighth money type", eighth, moneyTypes[7]);

    }

    /**
     * Tests sorting of WithdrawalRequestMoneyTypes first by ascending money type category code,
     * then by ascending money type short name. The money type short name should be the custom short
     * name (moneyTypeAliasId) if available, otherwise the Manulife short name (moneyTypeId).
     * 
     */
    @Test
    public void testSortMoneyTypesArrayWithNullValues() {

        WithdrawalRequestMoneyType beforeFirst = null;

        WithdrawalRequestMoneyType first = new WithdrawalRequestMoneyType();
        first.setMoneyTypeCategoryCode(null);
        first.setMoneyTypeAliasId(null);
        first.setMoneyTypeId(null);

        WithdrawalRequestMoneyType second = new WithdrawalRequestMoneyType();
        second.setMoneyTypeCategoryCode(null);
        second.setMoneyTypeAliasId("BBBBB");
        second.setMoneyTypeId("FFFFF");

        WithdrawalRequestMoneyType third = new WithdrawalRequestMoneyType();
        third.setMoneyTypeCategoryCode("EE");
        third.setMoneyTypeAliasId("CCCCC");
        third.setMoneyTypeId("EEEEE");

        WithdrawalRequestMoneyType forth = new WithdrawalRequestMoneyType();
        forth.setMoneyTypeCategoryCode("EE");
        forth.setMoneyTypeAliasId(null);
        forth.setMoneyTypeId("DDDDD");

        WithdrawalRequestMoneyType fifth = new WithdrawalRequestMoneyType();
        fifth.setMoneyTypeCategoryCode("ER");
        fifth.setMoneyTypeAliasId(null);
        fifth.setMoneyTypeId(null);

        WithdrawalRequestMoneyType sixth = new WithdrawalRequestMoneyType();
        sixth.setMoneyTypeCategoryCode("ER");
        sixth.setMoneyTypeAliasId("BBBBB");
        sixth.setMoneyTypeId("FFFFF");

        WithdrawalRequestMoneyType seventh = new WithdrawalRequestMoneyType();
        seventh.setMoneyTypeCategoryCode("ER");
        seventh.setMoneyTypeAliasId("CCCCC");
        seventh.setMoneyTypeId("EEEEE");

        WithdrawalRequestMoneyType eighth = new WithdrawalRequestMoneyType();
        eighth.setMoneyTypeCategoryCode("ER");
        eighth.setMoneyTypeAliasId(null);
        eighth.setMoneyTypeId("DDDDD");

        WithdrawalRequestMoneyType[] moneyTypes = { third, eighth, first, sixth, beforeFirst,
                second, seventh, forth, fifth };

        Arrays.sort(moneyTypes, ComparatorUtils.nullLowComparator(new MoneyTypeComparator()));

        int i = 0;
        assertEquals("Before First money type", beforeFirst, moneyTypes[i++]);
        assertEquals("First money type", first, moneyTypes[i++]);
        assertEquals("Second money type", second, moneyTypes[i++]);
        assertEquals("Third money type", third, moneyTypes[i++]);
        assertEquals("Forth money type", forth, moneyTypes[i++]);
        assertEquals("Fifth money type", fifth, moneyTypes[i++]);
        assertEquals("Sixth money type", sixth, moneyTypes[i++]);
        assertEquals("Seventh money type", seventh, moneyTypes[i++]);
        assertEquals("Eighth money type", eighth, moneyTypes[i++]);

    }
}
