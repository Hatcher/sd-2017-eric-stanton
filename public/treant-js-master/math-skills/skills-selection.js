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
  // data analysis
  chart_bounds : false,
  chart_difference : false,
  average : false,
  mode : false,
  chart_addition : false
}
var disabledSkills = [];

var chart_config_arithmetic = {
  chart : {
    container : "#collapsable-arithmetic",
  },
  padding : "0px",
  nodeAlign : "TOP",
  nodeStructure : {
    HTMLid : "arithmetic",
    text : {
      name : "ARITHMETIC"
    },
    children : [ {
      HTMLid : "add",
      image : "/assets/treant-js-master/math-skills/img/add.png"

    }, {
      HTMLid : "subtract",
      image : "/assets/treant-js-master/math-skills/img/subtract.png"
    }, {
      HTMLid : "multiply",
      image : "/assets/treant-js-master/math-skills/img/multiply.png"
    }, {
      HTMLid : "divide",
      image : "/assets/treant-js-master/math-skills/img/divide.png"
    } ]
  }
};

var chart_config_geometry = {
  chart : {
    container : "#collapsable-geometry"
  },
  nodeStructure : {
    HTMLid : "geometry",
    text : {
      name : "GEOMETRY"
    },
    collapsable : false,
    children : [ {
      image : "/assets/treant-js-master/math-skills/img/circle.png",
      HTMLid : "circle",
      collapsable : false
    }, {
      image : "/assets/treant-js-master/math-skills/img/rectangle.png",
      HTMLid : "rectangle",
      collapsable : false
    }, {
      image : "/assets/treant-js-master/math-skills/img/triangle.png",
      HTMLid : "triangle",
      collapsable : false
    }, ]
  }
};

var selectedColor = "#88FF88";
var unselectedColor = "#FF8888";
var disabledColor = "#BBBBBB";

function updateGeometryGroupSelectUnselectAll() {
  if (!skills.rectangle && !skills.triangle && !skills.circle) {
    $("#geometry").css('background-color', unselectedColor);
  } else {
    if (!(arrayContains(disabledSkills, "circle") && arrayContains(disabledSkills, "triangle") && arrayContains(disabledSkills, "rectangle"))) {
      $("#geometry").css('background-color', selectedColor);
    }
  }
}

function allGeometrySelected() {
  return skills.rectangle && skills.triangle && skills.circle;
}

function updateArithmeticGroupSelectUnselectAll() {
  if (!skills.add && !skills.subtract && !skills.multiply && !skills.divide) {
    $("#arithmetic").css('background-color', unselectedColor);
  } else {
    $("#arithmetic").css('background-color', selectedColor);
  }
}

function allArithmeticSelected() {
  return skills.add && skills.subtract && skills.multiply && skills.divide;
}

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

function getUrlFromSkills() {
  var responseString = "/math?";
  for (skill in skills) {
    if (skills[skill]) {
      responseString += skill + "=1&";
    }
  }
  return responseString;
}
function updateLink() {
  var temp = $("a#next");
  temp.attr("href", getUrlFromSkills());

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

function arrayContains(haystack, needle) {
  for (index = 0; index < haystack.length; ++index) {
    if (haystack[index] == needle) {
      return true;
    }
  }
  return false;
}

function hideDisabledItems() {
  for (skill in skills) {
    // turns the item green and sets it to true
    if (getUrlParameter(skill) == 1) {
      $("#" + skill).trigger("click");
    } else {

      for (child in chart_config_geometry.nodeStructure.children) {
        if (chart_config_geometry.nodeStructure.children[child].HTMLid == skill) {
          $("#" + skill).fadeTo(0, 0.5);
          $("#" + skill).css('background-color', disabledColor);
          $("#" + skill).unbind();
          disabledSkills.push(skill);
        }

      }

      for (child in chart_config_arithmetic.nodeStructure.children) {
        if (chart_config_arithmetic.nodeStructure.children[child].HTMLid == skill) {
          $("#" + skill).fadeTo(0, 0.5);
          $("#" + skill).css('background-color', disabledColor);
          $("#" + skill).unbind();
          disabledSkills.push(skill);
        }

      }

    }
  }
}

$(document).ready(
    function() {
      // only showing arithmetic

      // arithmetic
      $("#arithmetic").click(
          function() {
            // unselect all
            if ((skills.add || arrayContains(disabledSkills, "add")) && (skills.multiply || arrayContains(disabledSkills, "multiply"))
                && (skills.divide || arrayContains(disabledSkills, "divide")) && (skills.subtract || arrayContains(disabledSkills, "subtract"))) {
              if (!arrayContains(disabledSkills, "subtract")) {
                skills.subtract = false;
                $("#subtract").css('background-color', unselectedColor);
              }
              if (!arrayContains(disabledSkills, "add")) {
                skills.add = false;
                $("#add").css('background-color', unselectedColor);
              }
              if (!arrayContains(disabledSkills, "multiply")) {
                skills.multiply = false;
                $("#multiply").css('background-color', unselectedColor);
              }
              if (!arrayContains(disabledSkills, "divide")) {
                skills.divide = false;
                $("#divide").css('background-color', unselectedColor);
              }
              $("#arithmetic").css('background-color', unselectedColor);
            }
            // select all
            else {
              if (!arrayContains(disabledSkills, "subtract")) {
                skills.subtract = true;
                $("#subtract").css('background-color', selectedColor);
              }
              if (!arrayContains(disabledSkills, "add")) {
                skills.add = true;
                $("#add").css('background-color', selectedColor);
              }
              if (!arrayContains(disabledSkills, "multiply")) {
                skills.multiply = true;
                $("#multiply").css('background-color', selectedColor);
              }
              if (!arrayContains(disabledSkills, "divide")) {
                skills.divide = true;
                $("#divide").css('background-color', selectedColor);
              }
              $("#arithmetic").css('background-color', selectedColor);
            }
            updateLink();
          });

      $("#multiply").click(function() {
        skills.multiply = defineLeafOnclick(this.id, skills.multiply);
        updateArithmeticGroupSelectUnselectAll();
        updateLink();
      });
      $("#divide").click(function() {
        skills.divide = defineLeafOnclick(this.id, skills.divide);
        updateArithmeticGroupSelectUnselectAll();

        updateLink();
      });
      $("#add").click(function() {
        skills.add = defineLeafOnclick(this.id, skills.add);
        updateArithmeticGroupSelectUnselectAll();

        updateLink();
      });
      $("#subtract").click(function() {
        skills.subtract = defineLeafOnclick(this.id, skills.subtract);
        updateArithmeticGroupSelectUnselectAll();

        updateLink();
      });

      // geometry
      $("#geometry").click(function() {
        // unselect all
        if (skills.circle && skills.rectangle && skills.triangle) {
          if (!arrayContains(disabledSkills, "circle")) {
            skills.circle = false;
            $("#circle").css('background-color', unselectedColor);
          }
          if (!arrayContains(disabledSkills, "triangle")) {
            skills.triangle = false;
            $("#triangle").css('background-color', unselectedColor);
          }
          if (!arrayContains(disabledSkills, "rectangle")) {
            skills.rectangle = false;
            $("#rectangle").css('background-color', unselectedColor);
          }
          $("#geometry").css('background-color', unselectedColor);
        }
        // select all
        else {
          if (!arrayContains(disabledSkills, "circle")) {
            skills.circle = true;
            $("#circle").css('background-color', selectedColor);
          }
          if (!arrayContains(disabledSkills, "triangle")) {
            skills.triangle = true;
            $("#triangle").css('background-color', selectedColor);
          }
          if (!arrayContains(disabledSkills, "rectangle")) {
            skills.rectangle = true;
            $("#rectangle").css('background-color', selectedColor);
          }
          if (!(arrayContains(disabledSkills, "circle") && arrayContains(disabledSkills, "triangle") && arrayContains(disabledSkills, "rectangle"))) {
            $("#geometry").css('background-color', selectedColor);
          }
        }
        updateLink();
      });

      $("#circle").click(function() {
        skills.circle = defineLeafOnclick(this.id, skills.circle);
        updateGeometryGroupSelectUnselectAll();
        updateLink();
      });
      $("#rectangle").click(function() {
        skills.rectangle = defineLeafOnclick(this.id, skills.rectangle);
        updateGeometryGroupSelectUnselectAll();
        updateLink();
      });
      $("#triangle").click(function() {
        skills.triangle = defineLeafOnclick(this.id, skills.triangle);
        updateGeometryGroupSelectUnselectAll();
        updateLink();
      });

      hideDisabledItems();

    });
