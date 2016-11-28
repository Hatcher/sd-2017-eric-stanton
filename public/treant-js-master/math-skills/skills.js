$(document).ready(function() {
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
  
  clickAll();
});
