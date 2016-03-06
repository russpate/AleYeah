var templates = {};

templates.dashboard = [
  '<div class="beerBox">',
    '<h3>',
    '<%= beerName %>',
    '</h3>',
    '<div class ="beerWords">',
    '<h6>',
    '<%= beerType %>',
    '</h6>',
    '<h6>',
    '<%= alcoholContent %>',
    '</h6>',
    '<span>',
    '<%= _Good %>',
    '</span>',
    '</div>',
    '</div>'
].join("");
