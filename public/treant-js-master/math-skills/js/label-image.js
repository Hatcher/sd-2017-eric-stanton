var labelsQueue = [];

function queueLabel(imgId, overlayId, text, relativeX, relativeY){
	var labelToQueue = {};
	labelToQueue.imgId = imgId;
	labelToQueue.overlayId = overlayId;
	labelToQueue.text = text;
	labelToQueue.relativeX = relativeX;
	labelToQueue.relativeY = relativeY;
	labelsQueue.push(labelToQueue);
}

function overlayLabels(){
	for (var i = 0; i < labelsQueue.length; i++) {
		var previewImageOffset = $("#"+labelsQueue[i].imgId).offset();
		var fullX = previewImageOffset.top + labelsQueue[i].relativeY;
		var fullY = previewImageOffset.left + labelsQueue[i].relativeX;
		var tmpSpace = $("#tmp-space").append("<div id=\""+labelsQueue[i].overlayId+"\">"+labelsQueue[i].text+"</div>");
		$("#"+labelsQueue[i].overlayId).offset({ top: fullX, left: fullY });
	}
	
	
}

	