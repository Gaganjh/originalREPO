g_DisplayManager = new DisplayManager();
var minus_icon = "/assets/unmanaged/images/minus_icon.gif";
var plus_icon = "/assets/unmanaged/images/plus_icon.gif";

function DisplayManager()
{
	this.registeredDefs = Array();
}
DisplayManager.prototype.registerDefinition = function (obj)
{
	this.registeredDefs[obj.getId()] = obj;
}
DisplayManager.prototype.show = function (objId)
{
	var def = this.registeredDefs[objId];
	if (def == null ) return;

	for ( var i = 0 ; i < def.getChildren().length ; i++ )
	{
		var child = def.getChildren()[i];
		if ( !isValid(child) )
			continue;
		if ( child.customShow != null )
			child.customShow();
		else
			getDOMObject(child).style.display = "";
			
	}
	
}
DisplayManager.prototype.hide = function (objId)
{
	var def = this.registeredDefs[objId];
	if (def == null ) return;

	for ( var i = 0 ; i < def .getChildren().length ; i++ )
	{
		var child = def .getChildren()[i];
		if ( !isValid(child) )
			continue;
		if ( child.customHide != null )
			child.customHide();
		else
			getDOMObject(child).style.display = "none";
			
	}
	
}
DisplayManager.prototype.isHidden = function (objId)
{
	var o;
	var def = this.registeredDefs[objId];
	if ( def == null || def.getChildren().length < 1 ) return false;
	var child = def.getChildren()[0];
	if ( isValid(child) )
	{
		o = getDOMObject(child);
		if (o.style.display == "none")
			return true;
		else
			return false;
	}
	return false;
	
}
DisplayManager.prototype.isVisible = function (objId)
{
	return !this.isHidden(objId);
	
}
DisplayManager.prototype.toggle = function (objId)
{
	this.isHidden(objId) ? this.show(objId) : this.hide(objId)
}

DisplayManager.prototype.toggleTable = function (objId, imgId)
{
	this.toggle(objId);
	if ( isValid(imgId) )
		getDOMObject(imgId).src = this.isHidden(objId) ? plus_icon : minus_icon;	
	
}
function DisplayDef (myId)
{
	this.myId = myId;
	this.children = Array();
	for ( var i = 1; i < arguments.length ; i++ ) this.children.push(arguments[i]);		
}

DisplayDef.prototype.getChildren = function ()
{
	return this.children;
}
DisplayDef.prototype.addDOMObjectId = function (objId)
{
	this.children.push(objId);
}
DisplayDef.prototype.getId = function ()
{
	return this.myId;
}

function isValid(objectId)
{
	var o = document.getElementById(objectId)
	return o != null && typeof(o) != "undefined";
}
function getDOMObject(objectId)
{
	if ( isValid(objectId) )
		return document.getElementById(objectId);
}


// define LabelValueBean class
function LabelValueBean(label, value){
  // initialize the member variables for this instance
  this.label = label;
  this.value = value;

  LabelValueBean.prototype.getLabel = function () {

	  return this.label;

  }
  
   LabelValueBean.prototype.getValue = function () {

	  return this.value;

  }



}

