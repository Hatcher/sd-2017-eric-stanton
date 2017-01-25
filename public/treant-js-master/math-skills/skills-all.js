function initAll(){
  console.log(chart_config_all);
  
  initItem(chart_config_all.nodeStructure);
  
  console.log("complete!");
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