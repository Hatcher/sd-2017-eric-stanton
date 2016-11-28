function disableUnlearned() {
  for (skill in skills) {
    if (getUrlParameter(skill) != 1) {
      skills[skill] = false;
      $("#" + skill).fadeTo(0, 0.5);
      $("#" + skill).css('background-color', disabledColor);
      $("#" + skill).unbind();
      disabledSkills.push(skill);
    }
  }
}

$(document).ready(function() {
  baseUrl="/math"
  initArithmetic();
  for (id in arithmeticSkills) {
    initArithmeticClickItem(id);
  }

  initGeometry();
  for (id in geometrySkills) {
    initGeometryClickItem(id);
  }

  init3DGeometry();
  for (id in threeDGeometrySkills) {
    init3DGeometryClickItem(id);
  }

  disableUnlearned();
  clickAll();
  if (!any3DGeometrySelected()){
    $('#3dgeometry').hide();
    if (!anyGeometrySelected()){
      $('#geometry').hide();
    }
  }
});
