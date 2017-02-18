var chart_config_all =
{
  chart :
  {
    container : "#collapsable-all"
  },
  padding : "0px",
  nodeAlign : "TOP",
  nodeStructure :
  {
    HTMLid : "group_counting",
    text :
    {
      name : "CORE SKILLS"
    },
    collapsed : false,
    children : [
    {
      HTMLid : "counting",
      text :
      {
        name : "COUNTING"
      }
    },
      {
      HTMLid : "skipTo_arithmetic",
      pseudo : true,
      children : [
        {
  
        HTMLid : "group_arithmetic",
        text :
        {
          name : "ARITHMETIC"
        },
        collapsed : false,
        children : [
        {
          HTMLid : "add",
          image : "/assets/treant-js-master/math-skills/img/add.png"
  
        },
        {
          HTMLid : "subtract",
          image : "/assets/treant-js-master/math-skills/img/subtract.png"
        },
        {
  
          HTMLid : "skipTo_geometry",
          pseudo : true,
          children : [
          {
            HTMLid : "group_geometry",
            text :
            {
              name : "GEOMETRY"
            },
            collapsed : false,
            children : [
            {
              image : "/assets/treant-js-master/math-skills/img/circle.png",
              HTMLid : "circle"
            },
            {
              HTMLid : "skipTo_3DGeometry",
              pseudo : true,
              children : [
              {
                HTMLid : "group_3dgeometry",
                text :
                {
                  name : "3DGEOMETRY"
                },
                collapsed : false,
                children : [
                {
                  image : "/assets/treant-js-master/math-skills/img/cone.png",
                  HTMLid : "cone"
                },
                {
                  image : "/assets/treant-js-master/math-skills/img/cylinder.png",
                  HTMLid : "cylinder"
                },
                {
                  image : "/assets/treant-js-master/math-skills/img/pyramid.png",
                  HTMLid : "pyramid"
                },
                {
                  image : "/assets/treant-js-master/math-skills/img/cube.png",
                  HTMLid : "rectangularPrism"
                },
                {
                  image : "/assets/treant-js-master/math-skills/img/rightPrism.png",
                  HTMLid : "rightPrism"
                },
                {
                  image : "/assets/treant-js-master/math-skills/img/sphere.png",
                  HTMLid : "sphere"
                } ]
              } ]
            },
            {
              image : "/assets/treant-js-master/math-skills/img/rectangle.png",
              HTMLid : "rectangle",
            },
            {
              image : "/assets/treant-js-master/math-skills/img/triangle.png",
              HTMLid : "triangle",
            }, ]
  
          } ]
        },
        {
          HTMLid : "multiply",
          image : "/assets/treant-js-master/math-skills/img/multiply.png"
        },
        {
          HTMLid : "divide",
          image : "/assets/treant-js-master/math-skills/img/divide.png"
        }
  
        ]
  
      }
  ]}
    ]

  }
};