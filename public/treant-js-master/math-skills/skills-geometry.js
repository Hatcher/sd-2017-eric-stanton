function allGeometrySelected(){
  for (skill of geometrySkills){
    if (!skills[skill] && ($.inArray(skill, disabledSkills)<0)){
      return false;
    }
  }
  return true;
}

function allGeometrySelectedWithoutExclusion(){
  for (skill of geometrySkills){
    if (!skills[skill]){
      return false;
    }
  }
  return true;
}

function anyGeometrySelected(){
  for (skill of geometrySkills){
    if (skills[skill]){
      return true;
    }
  }
  return false;
}

function updateGeometryGroupSelectUnselectAll(){
  if (!anyGeometrySelected()){
    $("#geometry").css('background-color', unselectedColor);
  }
  else{
    $("#geometry").css('background-color', selectedColor);
  }
}

function showHideGeometry(){
  if (allArithmeticSelectedWithoutExclusion()){
    $("#collapsable-geometry").show();
  }
  else{
    for (skill of geometrySkills){
      if (skills[skill]){
        $("#"+skill).trigger('click');
      }
    }
    $("#collapsable-geometry").hide();
  }
}

function initGeometry(){
  $("#geometry").click(function() {
    // unselect all
    if (!allGeometrySelected()) {
      for (skillId of geometrySkills){
        if (!skills[skillId]){
          $("#"+skillId).trigger('click');
        }
        
      }
      $("#geometry").css('background-color', selectedColor);
    }
    else {
       for (skillId of geometrySkills){
         if (skills[skillId]){
           $("#"+skillId).trigger('click');
         }
         
        }
        $("#geometry").css('background-color', unselectedColor);
    }
    showHide3DGeometry();
    updateLink(baseUrl);
  });
} 
function initGeometryClickItem(id){
  $("#"+geometrySkills[id]).click(function() {
    skills[geometrySkills[id]] = defineLeafOnclick(this.id, skills[geometrySkills[id]]);
    updateGeometryGroupSelectUnselectAll();
    showHide3DGeometry();
    updateLink(baseUrl);
  });
}
