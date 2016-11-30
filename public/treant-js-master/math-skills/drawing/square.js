function drawSquare(id, side) {
  var rectangle_canvas = document.getElementById("canvas" + id);
  var context = rectangle_canvas.getContext("2d");
  context.strokeRect(20, 20, 50, 50);
  context.font = "16px Arial";
  //context.fillText("a", 8, 50);
  context.fillText(side, 42, 88);
}