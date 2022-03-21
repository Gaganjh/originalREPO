// This javascript file allows the "BDUser" cookie to be read in, and provides the cookieValues to the page
// They can be accessed using simple JavaScript calls to:  cookieValues.XXX where XXX is one of the cookie parameters like "FID", "PID" or "UR"

var cookieValues = [];
function readCookie(cookieName) {
    var theCookie=""+document.cookie;
    var ind=theCookie.indexOf(cookieName);
    if (ind==-1 || cookieName=="") return ""; 
    var ind1=theCookie.indexOf(';',ind);
    if (ind1==-1) ind1=theCookie.length; 
    var cookieString = unescape(theCookie.substring(ind+cookieName.length+1,ind1));
    var cookieStringArray = [];
    cookieStringArray = cookieString.split(",");
    for (i in cookieStringArray) {
        var keyValuePair=[];
        keyValuePair = cookieStringArray[i].split("_");        
        cookieValues[keyValuePair[0]] = keyValuePair[1];
    }

}
try {
    readCookie("BDUser");
} catch (e) {
}
