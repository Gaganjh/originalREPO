function AdobeWindow() 
{
	options="resizable,left=10,top=10,width=700,height=450,toolbar,status,menubar,scrollbars,location";
	newwindow=window.open("http://www.adobe.com/products/acrobat/readstep.html", "Adobe", options);
	if(navigator.appName=="Netscape") newwindow.focus();
}
