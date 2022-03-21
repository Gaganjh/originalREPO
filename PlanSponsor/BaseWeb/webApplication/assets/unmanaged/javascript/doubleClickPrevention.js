/**
 * This file contains Double Click prevention functionality.
 *
 * @author Andrew Dick
 */
var submitted = false;

/**
 * Checks if a submission is in progress:
 * If a submission is not in progress, the submission flag is updated to show a 
 * submission in progress and the submission is allowed to proceed.
 * If a submission is in progress, the status message is updated and the submission 
 * is prevented from proceeding. 
 */
function isSubmitInProgress() {
  if (!submitted) {
    submitted = true;
    return false;
  } else {
    window.status = "Transaction already in progress.  Please wait.";
    return true;
  }
}

/**
 * Resets the submission flag and removes the status message.
 */
function resetSubmitInProgress() {
  submitted = false;
  window.status = "";
} 