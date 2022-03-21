var navObjs= new Array();

var numNavObjs = 4;

var curNav = null;

var defaultNav = 9999999999;

var showTimer = null;

var waitingNav = null;

var defaultTimer = null;

function init(sec, subsec)
{
	
	if (getLayerById("n1") == null) {
		return;
	}
	
	if ((navigator.appName=="Netscape")&&(parseInt(navigator.appVersion)>=5)) 
	{
		getLayerById("n1").style.top="105"
	}

	//this is a hack to fix a problem on NN when the init method is called from the contactUs and 
	//guides page
	if ((sec) && (sec == 9)) {
		if ((navigator.appName=="Netscape")&&(parseInt(navigator.appVersion)>=5)) {
			return;
		} else {
			sec = null;
		}
	}


	for (var i = 0; i < numNavObjs; i++)
	{
		navObjs[i] = new Object();
		
		navObjs[i].pipes = getPipes(i);
		
		navObjs[i].subnav = getLayerById("n2Sec"+(i+1));
				
		navObjs[i].link = getLinksInLayer(getLayerById("n1"))[i];

		assignMouseOverHandler(navObjs[i].subnav, new Function("cancelNavHide(" + i + ");"));			
		assignMouseOutHandler(navObjs[i].subnav, new Function("navOff(" + i + ");"));
		
	}

	
	if ((sec) && (sec > 0))
	{
		defaultNav = parseInt(sec) - 1;

		if (subsec)
		{
			var sslnk = getLinksInLayer(navObjs[defaultNav].subnav)[parseInt(subsec) - 1];
			
			setStyleAtt(sslnk,"color","#FFFFFF");
			setStyleAtt(sslnk,"fontWeight","bold");
		}
		
		setDefault();
		
	}
	
}

function navOn(n)
{
	cancelNavHide(n);	

	//highlight(n);
	
	if (curNav != n)
	{
	
		if (curNav == null)
		{		
			showNav(n);
			
		}
		else
		{
			showTimer = setTimeout("recheck(" + n + ");", 300);
			waitingNav = n;
		}
	}
	
}



function recheck(n)
{
	if (curNav == null)
	{	
		killTimer(defaultTimer);
		showNav(n);
	}
	
	waitingNav = null;
	killTimer(showTimer);
}

function showNav(n)
{
	hideAllNavs();
		
	showLayer(navObjs[n].subnav);

	highlight(n);
	
	curNav = n;
	
	setDefault();
}

function navOff(n)
{
	navObjs[n].hideTimer = setTimeout("killNav(" + n + ");", 200);
	
	if (waitingNav == n)
	{
		killTimer(showTimer);
	}
}

function hideNav(n)
{
	unhighlight(n);
	hideLayer(navObjs[n].subnav);
}

function killNav(n)
{
	
	if (n != defaultNav)
	{
		hideNav(n);
	}
	
	if (curNav == n)
	{
		curNav = null;
	}
	
	killTimer(navObjs[n].hideTimer);	
	
	//if (timer == null)
	if (true)
	{
		if (defaultNav < numNavObjs)
		{
			defaultTimer = setTimeout("setDefault();", 200);
		}		
	}
	
	
}

function hideAllNavs()
{
	for (var i = 0; i < numNavObjs; i++)
	{
		hideNav(i);
		unhighlight(i);
	}
}

function cancelNavHide(n)
{
	if (typeof (navObjs[n].hideTimer) != "undefined" && navObjs[n].hideTimer != null)
	{
		killTimer(navObjs[n].hideTimer);
	}
	
	if (showTimer != null)
	{
		killTimer(showTimer);
	}
	
	if (defaultTimer != null)
	{
		killTimer(defaultTimer);
	}
}

function killTimer(t)
{
	clearTimeout(t);
	t = null;
}


function highlight(n)
{
	if (n != defaultNav)
	{
		hidePipes(n);
		var el = navObjs[n].link;
	
		setStyleAtt(el, "color", "#FFFFFF");
		setStyleAtt(el, "background", "#7C97AB");
	}

}
function unhighlight(n)
{

	if (n != defaultNav)
	{
		if (n == curNav)
		{	
			showPipes(n);
		}

		var el = navObjs[n].link;
	
		setStyleAtt(el, "color", "#FFFFFF");
		setStyleAtt(el, "background", "#4E9EB8");		
	}
}

function setDefault()
{
	if (defaultNav < numNavObjs)
	{
		hidePipes(defaultNav);
		
		var el = navObjs[defaultNav].link;
		var l = navObjs[defaultNav].pipes.length;
		var pipes = navObjs[defaultNav].pipes;
		
		if ((curNav == null) || (curNav == defaultNav))
		{
			setStyleAtt(el, "color", "#FFFFFF");
			setStyleAtt(el, "background", "#7C97AB");

			for (var i = 0; i < l; i++)
			{
				el = pipes[i].parentNode;
				
				if (el != null)
				{
					setStyleAtt(el, "background", "#7C97AB");
				}
			}

			showLayer(navObjs[defaultNav].subnav);
		}
		else
		{
			setStyleAtt(el, "color", "#FFFFFF");
			setStyleAtt(el, "background", "#4E9EB8");
			for (var i = 0; i < l; i++)
			{
				el = pipes[i].parentNode;
				if (el != null)
				{
					setStyleAtt(el, "background", "#4E9EB8");
				}
			}
			hideNav(defaultNav);
		}
	}
}

function setStyleAtt(el, att, val)
{
	if (typeof el != 'undefined') {
		if (el.style)
		{
			s = el.style;
			eval("el.style." + att + "='" + val + "';");
		}
	}
}

function getPipes(n)
{

	var pipes = new Array();
	var layr = getLayerById("n1");
	var img; 

	for (var i = n; i <= n+1; i++)
	{
		img = getImgInLayerByName(layr, "pipe_img"+i);
				
		if (img != null)
		{
			pipes[pipes.length] = img;
		}
	}
	
	return pipes;
}

function hidePipes(n)
{
	var pipes = navObjs[n].pipes;
	var l = pipes.length;
	
	for (var i = 0; i < l; i++)
	{
		pipes[i].src = "/assets/unmanaged/images/s.gif";
	}
}

function showPipes(n)
{
	var pipes = navObjs[n].pipes;
	var l = pipes.length;
	
	for (var i = 0; i < l; i++)
	{
		if (!ctt(pipes[i], defaultNav))
		{
			pipes[i].src = "/assets/unmanaged/images/white.gif";
		}
	}
}

function ctt(pipe, n)
{
	if ((n != null) && (navObjs[n] != null))
	{
	
		var pipes = navObjs[n].pipes;
		var l = pipes.length;
		
		for (var i = 0; i < l; i++)
		{
			if (pipes[i] == pipe)
			{
				return true;
			}
		}
	}
	return false;
}
