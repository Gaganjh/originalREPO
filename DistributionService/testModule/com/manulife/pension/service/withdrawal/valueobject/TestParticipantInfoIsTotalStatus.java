package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests the participant has total status query.
 * 
 * @author dickand
 */
public class TestParticipantInfoIsTotalStatus {

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalDeath() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_DEATH_TOTAL);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalDisability() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_DISABILITY_TOTAL);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithPartialDeath() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_PARTIAL_DEATH);

        assertFalse("Status should be partial.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithPartialDisability() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_PARTIAL_DISABILITY);

        assertFalse("Status should be partial.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithPartialRetirement() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_PARTIAL_RETIREMENT);

        assertFalse("Status should be partial.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithPartialTermination() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_PARTIAL_TERMINATION);

        assertFalse("Status should be partial.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithPartialTerminationOfParticipation() {

        final ParticipantInfo info = new ParticipantInfo();
        info
                .setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_PARTIAL_TERMINATION_OF_PARTICIPATION);

        assertFalse("Status should be partial.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalRetirement() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_RETIRED_TOTAL);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalTermination() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_TERMINATED_TOTAL);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }

    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalTerminationOfParticipantPaidUp() {

        final ParticipantInfo info = new ParticipantInfo();
        info
                .setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_TERMINATION_OF_PARTICIPANT_PAID_UP);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }
    /**
     * Tests the participant has total status query.
     */
    @Test
    public void testWithTotalTerminationOfParticipantOptOut() {

        final ParticipantInfo info = new ParticipantInfo();
        info
                .setParticipantStatusCode(ParticipantInfo.PARTICIPANT_STATUS_OPTED_OUT);

        assertTrue("Status should be total.", info.isParticipantStatusTotal());
    }
}
