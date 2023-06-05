
jQuery(document).ready(function ($) {

	//open the lateral panel

	$('.cd-btn-left').on('click', function (event) {
		event.preventDefault();
		$('.cd-panel-left').addClass('is-visible');
	});

	$('.cd-btn-right').on('click', function (event) {
		event.preventDefault();
		$('.cd-panel-right').addClass('is-visible');
	});



	//close the lateral panel
	$('.cd-panel-right, .cd-panel-left').on('click', function (event)
	{		
		if( $(event.target).is('.cd-panel') || $(event.target).is('.cd-panel-close') ) { 
			$('.cd-panel').removeClass('is-visible');
			event.preventDefault();
		}
	});
});