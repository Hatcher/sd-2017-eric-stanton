var skillItems = [];

function initAllSkillNames(){
  initSkills(chart_config_all.nodeStructure);
}
function initSkills(item){
  if (item.children != null){
    for (child of item.children){
      initSkills(child);
    }
  }
  else{
    skillItems.push(item.HTMLid)

  }
  
}