var templates = {};

templates.dashboard = [
  '<div class="beerBox">',
    '<h3>',
    '<%= beerName %>',
    '</h3>',
    // '<div class="imgBox">'
    // '<img src="',
    // '<%= imgUrl %>',
    // '" alt="" /></div>',
    '<div class ="beerWords">',
    '<h6>',
    '<%= beerType =>',
    '</h6>',
    '<h6>',
    '<%= alcoholContent %>',
    '</h6>',
    '<span>',
    '<%= isGood %>',
    '</span>',
    '</div>',
    '</div>'
].join("");
