$(document).ready(function() {
  aleYeah.init();
});

var aleYeah = {
  url: {
    getBeers:"/get-beers"
  },
  init:function () {
    aleYeah.events();
    aleYeah.styling();
  },

  events: function () {

  },

  styling: function () {

  },

  getBeerReview: function () {
    $.ajax({
      method: 'GET',
      url: aleYeah.url.getBeers,
      success: function(beerData) {
        console.log("RECEIVED BEERS", beerData);
        window.glob = beerData;
        aleYeah.addBeerToDom(JSON.parse(beerData));
      },
      error: function(err) {
        console.log('oh shit', err);
      }
    });
  },

  addBeertoDom: function(){
    $('.lecturers').html("");
   var tmpl = _.template(templates.reviews);
   lecturers.forEach(function(beer) {
     $('.lecturers').append(tmpl(beer));
   });
  },

  addBeerReviewtoDom: function(){

  },
};
