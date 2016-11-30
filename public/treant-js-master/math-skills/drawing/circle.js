function drawCircle(id) {
  var my_canvas = document.getElementById("canvas" + id);
  var context = my_canvas.getContext("2d");

  context.beginPath();
  context.arc(75, 75, 35, 0, 2 * Math.PI);
  context.stroke();
}