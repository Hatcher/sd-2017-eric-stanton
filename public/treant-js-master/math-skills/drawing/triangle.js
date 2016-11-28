function drawTriangle(id) {
  console.log(id);
  var triangle_canvas = document.getElementById("canvas" + id);
  var context = triangle_canvas.getContext("2d");
  context.beginPath();
  context.moveTo(125, 125);
  context.lineTo(125, 45);
  context.lineTo(45, 125);
  context.closePath();
  context.stroke();
}

