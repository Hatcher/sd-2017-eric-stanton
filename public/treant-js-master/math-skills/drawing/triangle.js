function drawTriangle(id, base, height, hypoteneuse) {
  var triangle_canvas = document.getElementById("canvas" + id);
  var context = triangle_canvas.getContext("2d");
  context.beginPath();
  context.moveTo(125, 125);
  context.lineTo(125, 45);
  context.lineTo(45, 125);
  context.closePath();
  context.stroke();

  context.font = "16px Arial";
  context.fillText(base, 128, 90);
  context.fillText(height, 90, 140);
  context.fillText(hypoteneuse, 75, 80);
}
