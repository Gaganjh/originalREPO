if (ie4)
{
	setLayerBg = new Function("layr", "bg", "layr.style.background = bg");
	writeToLayer = new Function("layr", "txt", "layr.innerHTML = txt;");
	showLayer = new Function("layr", "layr.style.visibility = 'visible';");
	hideLayer = new Function("layr", "layr.style.visibility = 'hidden';");
	moveX = new Function("layr", "x", "layr.style.posLeft = x;")	
	moveY = new Function("layr", "y", "layr.style.posTop = y;")	
	setHeight = new Function("layr", "h", "layr.style.pixelHeight = h;");
	getHeight = new Function("layr", "return layr.offsetHeight;");
	getWidth = new Function("layr", "return layr.offsetWidth;");
	getX = new Function("layr", "return layr.style.posLeft;");
	getY = new Function("layr", "return layr.style.posTop;");
	getLayerById = new Function("id", "return document.all(id);");
	getImgInLayerByName = new Function("layr", "name", "return layr.all.tags('img')[name]");
	setZ = new Function("layr", "z", "layr.style.zIndex = z");
	
	assignClickHandler = new Function("layr", "func", "layr.onclick = func;");
	assignMouseOverHandler = new Function("layr", "func", "layr.onmouseover = func;");
	assignMouseOutHandler = new Function("layr", "func", "layr.onmouseout = func;");

	createLayer = createLayerIE;
	getPagePosition = getPagePositionIE;
	
	if (document.getElementById)
	{
		createLayer = createLayerNS6;
	}
	
	adjustDocHeight = new Function(""); // only ns4 needs this function
	getFormInLayer = new Function("layr", "name", "if (name != null) { return layr.all.tags('form')[name]; } else { return layr.all.tags('form')[0]; }");
	getLinksInLayer = new Function ("layr", "return layr.all.tags('a')");
	getChildElements = new Function("layr", "return layr.all;")
}
else if (ns4)
{
	setLayerBg = new Function("layr", "bg", "layr.bgColor = bg");
	writeToLayer = new Function("layr", "txt", "layr.document.open(); layr.document.write(txt); layr.document.close();");
	showLayer = new Function("layr", "layr.visibility = 'visible';");
	hideLayer = new Function("layr", "layr.visibility = 'hidden';");
	moveX = new Function("layr", "x", "layr.left = x;")
	moveY = new Function("layr", "y", "layr.top = y;")	
	setHeight = new Function("layr", "h", "layr.clip.height = h;");
	getHeight = new Function("layr", "return layr.clip.height;");
	getWidth = new Function("layr", "return layr.clip.width;");
	getX = new Function("layr", "return layr.left;");
	getY = new Function("layr", "return layr.top;");
	getLayerById = getLayerByIdNS4;
	getImgInLayerByName = new Function("layr", "name", "return layr.document.images[name]");
	setZ = new Function("layr", "z", "layr.zIndex = z");
	
	assignClickHandler = new Function("layr", "func", "layr.captureEvents(Event.MOUSEDOWN); layr.onmousedown = func;");
	assignMouseOverHandler = new Function("layr", "func", "layr.onmouseover = func;");
	assignMouseOutHandler = new Function("layr", "func", "layr.onmouseout =  func;");
	
	initWidth = window.innerWidth;
	initHeight = window.innerHeight;
	window.onresize = new Function("if ((window.innerWidth != initWidth) || (window.innerHeight != initHeight)) { window.location.href=window.location.href }");
	

	createLayer = createLayerNS4;
	getPagePosition = getPagePositionNS4;
	getFormInLayer = new Function("layr", "name", "if (name != null) { return layr.document.forms[name]; } else { return layr.document.forms[0]; }");
	getLinksInLayer = new Function ("layr", "return layr.document.links");
	
	getChildElements = new Function("layr", "return null;")
}
else if (ns6) 
{
	setLayerBg = new Function("layr", "bg", "layr.style.background = bg");	
	writeToLayer = new Function("layr", "txt", "layr.innerHTML = txt;");
	showLayer = new Function("layr", "layr.style.visibility = 'visible';");
	hideLayer = new Function("layr", "layr.style.visibility = 'hidden';");
	moveX = new Function("layr", "x", "layr.style.left = x;")	
	moveY = new Function("layr", "y", "layr.style.top = y;")
	setHeight = new Function("layr", "h", "layr.style.height = h;");
	getHeight = new Function("layr", "return layr.offsetHeight;");	
	getWidth = new Function("layr", "layr.style.width = 'auto'; return layr.offsetWidth;");	
	getX = new Function("layr", "return parseInt(layr.style.left);");
	getY = new Function("layr", "return parseInt(layr.style.top);");	
	getLayerById = new Function("id", "return document.getElementById(id);");
	getImgInLayerByName = new Function("layr", "name", "return null");
	setZ = new Function("layr", "z", "layr.style.zIndex = z");
	
	assignClickHandler = new Function("layr", "func", "layr.addEventListener('click', func, true);");	
	assignMouseOverHandler = new Function("layr", "func", "layr.addEventListener('mouseover', func, true);");
	assignMouseOutHandler = new Function("layr", "func", "layr.addEventListener('mouseout', func, true);");

	createLayer = createLayerNS6;
	getPagePosition = getPagePositionNS6;	
	
	adjustDocHeight = new Function(""); // only ns4 needs this function
	getFormInLayer = new Function("layr", "name", "if (name != null) { return layr.getElementsByTagName('form')[name]; } else { return layr.getElementsByTagName('form')[0]; }");
	getLinksInLayer = new Function ("layr", "return layr.getElementsByTagName('a')");
	getChildElements = new Function("layr", "return layr.childNodes;")
}

function createLayerIE(id, container)
{
	id += new Date().getTime(); //makes sure id is unique on page
	var layr = '<div id="' + id + '" style="position:absolute"></div>'	
	if (container == null)
	{
		container = document.body;
	}	
	container.insertAdjacentHTML("afterBegin", layr)
	
	return container.all(id); 
}

function createLayerNS6(id, container)
{
	var layr = document.createElement("div");
	layr.style.position = "absolute";
	layr.style.visibility = "hidden";
	if (container == null)
	{
		container = document.body;
	}
	container.appendChild(layr);
	return layr;
}

function createLayerNS4(id, container)
{
	var layr;
	
	
	if (container == null) 
	{ 
	
		layr = new Layer(); 
		
	}
	else
	{
		layr = new Layer(192, container);	
	}
	
	
	return layr;
}



function getLayerByIdNS4(id, layrs)
{
	var layr;

	if (layrs == null)
	{
		layrs = document.layers;
	}
	
	var l = layrs.length;
	
	for (var i = 0; i < l; i++)
	{
		if (layrs[i].id == id)
		{
			layr = layrs[i];
		}
		else if(layrs[i].document.layers.length > 0)
		{
			layr = getLayerByIdNS4(id, layrs[i].document.layers);
		}
		if (layr != null)
		{
			return layr;
		}		
	}
	
	return layr;
}

function getPagePositionIE(el, n, axis)
{
	if (axis == "x")
	{
    		n += el.offsetLeft;	
	}
	else if (axis == "y")
	{
    		n += el.offsetTop;
	}
    	if (el != document.body)
    	{
    	    if (isMac)
    	    {
    	        parentalUnit = el.parentElement;
    	    } else
    	    {
    	        parentalUnit = el.offsetParent;
    	    }
	
    	    return getPagePositionIE(parentalUnit, n, axis);
    	}
    	return n;
}

function getPagePositionNS4(el, n, axis)
{
	if (axis == "x")
	{
		return el.pageX;
	}
	else
	{
		return el.pageY;
	}
}

function getPagePositionNS6(el, n, axis)
{
	if (axis == "x")
	{
		n += el.offsetLeft;
	}
	else
	{
		n += el.offsetTop;
	}
	
	if (el.offsetParent != null)
	{
		return getPagePosition(el.offsetParent, n, axis);
	}
	else
	{
		return n;
	}
}

function adjustDocHeight()
{
	var l = document.layers.length;
	for (var i = 0; i < l; i++)
	{
		if (document.layers[i].top + document.layers[i].clip.height > document.height)
		{
			document.height = document.layers[i].top + document.layers[i].clip.height;
		}
	}
}

/***********************************************
* Contractible Headers script-  Dynamic Drive (www.dynamicdrive.com)
* This notice must stay intact for legal use. Last updated Mar 23rd, 2004.
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

var collapseprevious="no" //Collapse previously open content when opening present? (yes/no)

if (document.getElementById){
document.write('<style type="text/css">')
document.write('.switchcontent{display:none;}')
document.write('</style>')
}

function getElementbyClass(classname){
ccollect=new Array()
var inc=0
var alltags=document.all? document.all : document.getElementsByTagName("*")
for (i=0; i<alltags.length; i++){
if (alltags[i].className==classname)
ccollect[inc++]=alltags[i]
}
}

function contractcontent(omit){
var inc=0
while (ccollect[inc]){
if (ccollect[inc].id!=omit)
ccollect[inc].style.display="none"
inc++
}
}

function contractAllSections(){
	getElementbyClass("switchcontent")
	var inc=0
	while (ccollect[inc]){
		ccollect[inc].style.display="none"
	inc++
}
}

function expandAllSections(){
	getElementbyClass("switchcontent")
	var inc=0
	while (ccollect[inc]){
		ccollect[inc].style.display="block"
	inc++
}
}

function expandcontent(cid){
if (typeof ccollect!="undefined"){
if (collapseprevious=="yes")
contractcontent(cid)
document.getElementById(cid).style.display=(document.getElementById(cid).style.display!="block")? "block" : "none"
}
}

function do_onload(){
getElementbyClass("switchcontent")
}


if (window.addEventListener)
window.addEventListener("load", do_onload, false)
else if (window.attachEvent)
window.attachEvent("onload", do_onload)
else if (document.getElementById)
window.onload=do_onload

