
function drawRectangle(id){
  console.log(id);
var rectangle_canvas = document.getElementById("canvas"+id);
var context = rectangle_canvas.getContext("2d");
context.strokeRect(20, 20, 100, 50);
}