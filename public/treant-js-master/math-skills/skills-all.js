function initAll(){
	$.getJSON("https://s3.amazonaws.com/remind/Math/Trees/CaringTree.json").success(function(data){
		  chart_config_all["nodeStructure"] = data["nodeStructure"];
		  // redraw the tree
		  tree_all = new Treant(chart_config_all);
		  initItem(chart_config_all.nodeStructure);
	  });  
}
function initItem(item){
  console.log(item);
  if (item.children != null){
    for (child of item.children){
      initItem(child);
    }
  }
  else{
    $("#"+item.HTMLid).click(function(){
    if (!skills[item.HTMLid]){
      console.log(skills);
      $("#"+item.HTMLid).css('background-color', selectedColor);  
      skills[item.HTMLid] = true;
    }
    else{
      $("#"+item.HTMLid).css('background-color', unselectedColor);
      skills[item.HTMLid] = false;
    }
    updateLink(baseUrl);
    });
  }
}


