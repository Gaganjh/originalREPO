function showValues(seriesId,sColor, fColor,seriesDesc) {

	re=/;/;

	seriesIdList=seriesId.split(re);
	sColorList=sColor.split(re);
	fColorList=fColor.split(re);
	seriesDescList=seriesDesc.split(re);

	for(i=0;i<seriesIdList.length;i++) {
		var item=document.getElementById(seriesIdList[i]+'Color');
		item.setAttribute("bgcolor",sColorList[i]);
		item.style.backgroundColor=sColorList[i];
		item=document.getElementById(seriesIdList[i]+'Desc');
		item.childNodes[0].nodeValue=seriesDescList[i];
		item.style.color=fColorList[i];
	}
}
