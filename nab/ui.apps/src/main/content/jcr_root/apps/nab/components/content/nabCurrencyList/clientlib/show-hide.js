(function (document, $) {
  "use strict";

  // listen for dialog injection
  $(document).on("foundation-contentloaded", function (e) {
    $(".showhide").each(function () {
      showHide($(this));
    });
  });

  // listen for toggle change
  $(document).on("change", ".showhide", function (e) {
   showHide($(this));
  });

  // show/hide our target depending on toggle state
  function showHide(el) {

  setTimeout(function() { 
   if(el.prop("checked")){
	   		el.closest('.coral-Form-fieldset').find('.text-show').closest('.coral-Form-fieldwrapper').hide();
			el.closest('.coral-Form-fieldset').find('.rich-text-show').closest('.coral-Form-fieldwrapper').show();

      }else{
           	el.closest('.coral-Form-fieldset').find('.text-show').closest('.coral-Form-fieldwrapper').show();
          	el.closest('.coral-Form-fieldset').find('.rich-text-show').closest('.coral-Form-fieldwrapper').hide();

      }
       },600);

  } 
 
})(document, Granite.$);