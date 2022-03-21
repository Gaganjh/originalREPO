var n1SelectedlinkID=null;
var n2SelectedlinkID=null;

function n1Highlight(n1link) { 
 if ( n1link.id != n1SelectedlinkID){
	MM_showHideLayers("n2" + n1SelectedlinkID,'','hide')
	if (n1SelectedlinkID != null) {
		var selLink = MM_findObj(n1SelectedlinkID)
		selLink.style.color = '#7C97AB'
		selLink.style.background = '#FBF2DD'
	}
 } 
	nav2 = "n2" + n1link.id;
	n1link.style.color = '#7C97AB'
	n1link.style.background = '#4E9EB8'
	MM_showHideLayers(nav2,'','show')
}

function n1Normal(n1link) { 

 if ( n1link.id != n1SelectedlinkID){
	nav2 = "n2" + n1link.id;

	if (n1SelectedlinkID != null) {
		var selLink = MM_findObj(n1SelectedlinkID)
		selLink.style.color = '#7C97AB'
		selLink.style.background = '#4E9EB8'
	}
	n1link.style.color = '#FFFFFF'
	n1link.style.background = '#4E9EB8'
	MM_showHideLayers(nav2,'','hide')
	MM_showHideLayers("n2" + n1SelectedlinkID,'','show')
 }

}

function n2Highlight(n2link){
n2link.style.color= '#FFFFFF';
n2link.style.fontWeight = 'bold';
}

function pageSelected(section,subsection){
	if ((navigator.appName=="Netscape")&&(parseInt(navigator.appVersion)>=5)) {
		MM_findObj("n1",'').style.top="105"
	}
	
	defaultNav = section-1;
	
	if (section!=null && section!="0"){
	n1SelectedlinkID="Sec"+section;
	n1Highlight(MM_findObj(n1SelectedlinkID,''));
	}
	if (subsection!=null && subsection!="0"){
	n2SelectedlinkID="Sub"+ subsection +"Sec"+section;
	n2Highlight(MM_findObj(n2SelectedlinkID,''));
	}
}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i>d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v3.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}

function MM_jumpMenu(targ,selObj,restore){ //v3.0
if(selObj.options[selObj.selectedIndex].value != "#" && selObj.options[selObj.selectedIndex].value != ""){
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
  }
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

