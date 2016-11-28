function all3DGeometrySelected(){
  for (skill of threeDGeometrySkills){
    if (!skills[skill] && ($.inArray(skill, disabledSkills)<0)){
      return false;
    }
  }
  return true;
}
function all3DGeometrySelectedWithoutExclusion(){
  for (skill of threeDGeometrySkills){
    if (!skills[skill]){
      return false;
    }
  }
  return true;
}

function any3DGeometrySelected(){
  for (skill of threeDGeometrySkills){
    if (skills[skill]){
      return true;
    }
  }
  return false;
}



function updateThreeDGeometryGroupSelectUnselectAll(){
  if (!any3DGeometrySelected()){
    $("#3dgeometry").css('background-color', unselectedColor);
  }
  else{
    $("#3dgeometry").css('background-color', selectedColor);
  }
}



function showHide3DGeometry(){
  if (allArithmeticSelectedWithoutExclusion() && allGeometrySelectedWithoutExclusion()){
    $("#3d-geometry").show();
  }
  else{
    for (skill of threeDGeometrySkills){
      if (skills[skill]){
        $("#"+skill).trigger('click');
      }
    }
    $("#3d-geometry").hide();
  }
}
function init3DGeometry(){
  $("#3dgeometry").click(function() {
    // unselect all
    if (!all3DGeometrySelected()) {
      for (skillId of threeDGeometrySkills){
        if (!skills[skillId]){
          $("#"+skillId).trigger('click');
        }
      }
      $("#3dgeometry").css('background-color', selectedColor);
    }
    else {
       for (skillId of threeDGeometrySkills){
         if (!skills[skillId]){
           $("#"+skillId).trigger('click');
         }
        }
        $("#3dgeometry").css('background-color', unselectedColor);
    }
    updateLink(baseUrl);
  });
}

function init3DGeometryClickItem(id){
  $("#"+threeDGeometrySkills[id]).click(function() {
    skills[threeDGeometrySkills[id]] = defineLeafOnclick(this.id, skills[threeDGeometrySkills[id]]);
    updateThreeDGeometryGroupSelectUnselectAll();
    updateLink(baseUrl);
  });
}