function drawCircle(id) {
  var my_canvas = document.getElementById(id);
  var context = my_canvas.getContext("canvas-" + id);

  context.beginPath();
  context.arc(75, 75, 35, 0, 2 * Math.PI);
  context.stroke();
}