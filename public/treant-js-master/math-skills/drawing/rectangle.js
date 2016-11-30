function drawRectangle(id, length, width) {
  var rectangle_canvas = document.getElementById("canvas" + id);
  var context = rectangle_canvas.getContext("2d");
  context.strokeRect(20, 20, 100, 50);
  context.font = "16px Arial";
  context.fillText(length, 8, 50);
  context.fillText(width, 65, 90);
}