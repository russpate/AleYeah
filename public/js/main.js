$(document).ready(function() {
  aleYeah.init();
});

var aleYeah = {
  url: {
    getBeers:"/get-beers",
    createReview: "/get-beers"
  },

  init:function () {
    aleYeah.events();
    aleYeah.styling();
  },

  events: function () {
    $('.submitLogin').on('click', function(event){
      event.preventDefault();
      console.log("you clicked login");
      $('.loginScreen').removeClass('active');
      $('.dashboardScreen').addClass('active');
    });
    $('.dashboardScreen').on('click', 'button', function(event){
      event.preventDefault();
      console.log("you clicked create a new review");
      $('.dashboardScreen').removeClass('active');
      $('.reviewScreen').addClass('active');
    });
    $('.reviewScreen').on('click', '.submitReview', function(event){
      event.preventDefault();
      console.log("you clicked submit a new review");
      $('.reviewScreen').removeClass('active');
      $('.confirmationScreen').addClass('active');
      var rating = aleYeah.getBeerInfo();
      console.log(rating);

      aleYeah.createBeerReview(rating);
    });
    $('.confirmationScreen').on('click', 'button', function(event){
      event.preventDefault();
      console.log("you clicked back to dashboard");
      $('.confirmationScreen').removeClass('active');
      $('.dashboardScreen').addClass('active');
    });
  //   $('.reviewScreen').on('click','input[type="submit"]',function(event) {
  //    event.preventDefault();
  //    var rating = aleYeah.getBeerInfo();
  //    console.log(rating);
  //
  //    aleYeah.createBeerReview(rating);
  //  });
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
        console.log('dint work', err);
      }
    });
  },

  createBeerReview: function(){
    $.ajax({
      method:'POST',
      url: aleYeah.url.createReview,
      data: review,
      success: function(newReview){
        console.log("YOU DID IT", newReview);
      },
      error: function(err){
        console.log("dint work", err);
      }
    });
  },

  getBeerInfo: function(){
    var beerName = $('input[name="beerName"]').val();
    var imgUrl = $('input[name="imgUrl"]').val();
    var beerType = $('input[name="beerType"]').val();
    var abv = $('input[name="abv"]').val();
    return{
      beerName: beerName,
      imgUrl: imgUrl,
      beerType: beerType,
      abv: abv
    };
  },

  addBeertoDom: function(){
    $('.dashboard').html("");
   var tmpl = _.template(templates.dashboard);
   lecturers.forEach(function(beer) {
     $('.dashboard').append(tmpl(beer));
   });
  }
}; // end of aleyeah array
