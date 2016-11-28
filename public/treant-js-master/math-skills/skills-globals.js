var skills = {
  // arithmetic
  add : false,
  subtract : false,
  multiply : false,
  divide : false,
  // geometry
  triangle : false,
  rectangle : false,
  circle : false,

  cone : false,
  cylinder : false,
  pyramid : false,
  rectangularPrism : false,
  rightPrism : false,
  sphere : false,

  // data analysis
  chart_bounds : false,
  chart_difference : false,
  average : false,
  mode : false,
  chart_addition : false
}
var disabledSkills = [];

var arithmeticSkills = [ "add", "subtract", "multiply", "divide" ];
var geometrySkills = [ "triangle", "rectangle", "circle" ];
var threeDGeometrySkills = [ "cone", "cylinder", "pyramid", "rectangularPrism", "rightPrism", "sphere" ];

var selectedColor = "#88FF88";
var unselectedColor = "#FF8888";
var disabledColor = "#BBBBBB";
var baseUrl = "/math/skills/choices";

function defineLeafOnclick(id, typeValue) {
  if (!typeValue) {
    typeValue = true;
    $("#" + id).css('background-color', selectedColor);
  } else {
    typeValue = false;
    $("#" + id).css('background-color', unselectedColor);
  }
  return typeValue;
}

function getUrlFromSkills(baseUrl) {
  var responseString = baseUrl+"?";
  for (skill in skills) {
    if (skills[skill]) {
      responseString += skill + "=1&";
    }
  }
  return responseString;
}

function updateLink(baseUrl) {
  var temp = $("a#next");
  temp.attr("href", getUrlFromSkills(baseUrl));
}

function clickAll() {
  $('#arithmetic').trigger('click');
  $('#geometry').trigger('click');
  $('#3dgeometry').trigger('click');
  
}

function getUrlParameter(sParam) {
  var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'), sParameterName, i;

  for (i = 0; i < sURLVariables.length; i++) {
    sParameterName = sURLVariables[i].split('=');

    if (sParameterName[0] === sParam) {
      return sParameterName[1] === undefined ? true : sParameterName[1];
    }
  }
}