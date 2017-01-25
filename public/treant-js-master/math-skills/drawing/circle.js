function drawCircle(id, radius, diameter) {
  var circle_canvas = document.getElementById("canvas" + id);
  var context = circle_canvas.getContext("2d");

  context.beginPath();
  context.arc(75, 75, 55, 0, 2 * Math.PI);
  context.stroke();
  context.setLineDash([3, 0, 3])
  context.font = "16px Arial";
  
  var label;
  var x;
  if (radius != ""){
    context.lineTo(75, 75);// radius
    label = radius;
    x=88;
  }
  else{
    context.lineTo(20, 75);
    label = diameter;
    x=72;
  }
  context.stroke();
  context.fillText(label, x, 68);
}