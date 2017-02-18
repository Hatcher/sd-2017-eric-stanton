var skills = {}
var disabledSkills = [];

var unmappedSkillGroups = [];
var unmappedSkills = [];

var selectedColor = "#88FF88";
var unselectedColor = "#FF8888";
var disabledColor = "#BBBBBB";
var baseUrl = "/math/demo";



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

function getUrlParameter(sParam) {
  var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'), sParameterName, i;

  for (i = 0; i < sURLVariables.length; i++) {
    sParameterName = sURLVariables[i].split('=');

    if (sParameterName[0] === sParam) {
      return sParameterName[1] === undefined ? true : sParameterName[1];
    }
  }
}