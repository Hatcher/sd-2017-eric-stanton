function drawCube(id, height, width, depth) {
  var rectangle_canvas = document.getElementById("canvas" + id);
  var context = rectangle_canvas.getContext("2d");
  
  context.strokeRect(80, 40, 110, 80);
  context.strokeRect(40, 80, 110, 80);
  
  context.moveTo(80, 40);
  context.lineTo(40, 80);
  
  
  context.moveTo(190, 40);
  context.lineTo(150, 80);

  context.moveTo(80, 120);
  context.lineTo(40, 160);
  
  context.moveTo(190, 120);
  context.lineTo(150, 160);
  
  
  context.stroke();
  
  context.font = "16px Arial";
  
  
  
  
  
  context.fillText(depth, 45, 65);
  context.fillText(width, 120, 35);
  context.fillText(height, 45, 120);
}