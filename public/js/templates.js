var templates = {};

templates.dashboard = [
  '<div class="beerBox" data-id="',
    '<%= id %>',
    '">',
    '<div class="beerBoxInner">',
    '<button type="button" name="Delete" class="delete"><i class="fa fa-trash"></i></button>',
    '<h3>',
    '<%= beerName %>',
    '</h3>',
    '<div class ="beerWords">',
    '<h6>style: ',
    '<%= beerType %>',
    '</h6>',
    '<h6>abv: ',
    '<%= alcoholContent %>',
    '</h6>',
    '<h6>like it? ',
    '<%= good %>',
    '</h6>',
    '</div>',
    '</div>',
    '</div>'
].join("");
