function allArithmeticSelected(){
  for (skill of arithmeticSkills){
    if (!skills[skill] && ($.inArray(skill, disabledSkills)<0)){
      return false;
    }
  }
  return true;
}
function allArithmeticSelectedWithoutExclusion(){
  for (skill of arithmeticSkills){
    if (!skills[skill]){
      return false;
    }
  }
  return true;
}


function anyArithmeticSelected(){
  for (skill of arithmeticSkills){
    if (skills[skill]){
      return true;
    }
  }
  return false;
}

function updateArithmeticGroupSelectUnselectAll(){
  if (!anyArithmeticSelected()){
    $("#arithmetic").css('background-color', unselectedColor);
  }
  else{
    $("#arithmetic").css('background-color', selectedColor);
  }
}

function initArithmetic(){

  $("#arithmetic").click(function() {
    if (!allArithmeticSelected()) {
      for (skillId of arithmeticSkills){
        
        if (!skills[skillId]){
          $("#"+skillId).trigger('click');
        }
        
      }
      $("#arithmetic").css('background-color', selectedColor);
    }
    else {
       for (skillId of arithmeticSkills){
         if (skills[skillId]){
           $("#"+skillId).trigger('click');
         }
        }
        $("#arithmetic").css('background-color', unselectedColor);
    }
    showHideGeometry();
    showHide3DGeometry();
    updateLink(baseUrl);
  });
}
function initArithmeticClickItem(id){
  $("#"+arithmeticSkills[id]).click(function() {
    skills[arithmeticSkills[id]] = defineLeafOnclick(this.id, skills[arithmeticSkills[id]]);
    updateArithmeticGroupSelectUnselectAll();
    showHideGeometry();
    showHide3DGeometry();
    updateLink(baseUrl);
  });
}