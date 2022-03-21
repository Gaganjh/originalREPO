/**
 * @author Mabel Au
 * $Id: howto.js
 */

/**
 * key = howto layout page ContentId.
 * ind = f or F for form; r or R for report.
 * ind is used to distinguish "how to use form" and "how to read report" page.
 */

function doHowTo(key,ind) {
   location.href = "/do/contentpages/howTo?contentKey=" + key + "&ind=" + ind;
}


