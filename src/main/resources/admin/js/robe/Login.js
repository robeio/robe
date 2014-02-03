function initializeLogin() {

	var token = $.cookie.read("auth-token");
	$("#username").val("admin@robe.io");
	$("#password").val("123123");
	if (token != null) {
		login(token);
	}

	$('#loginError').hide();
	$('#login-button').kendoButton({
		click: login,
		imageUrl:"../icon/checkmark.png"
	});
}

function login(token) {

	$.ajax({
		type: "POST",
		url: getBackendURL()+"authentication/login",
		data: JSON.stringify({username: $("#username").val(),password:CryptoJS.SHA256($("#password").val()).toString()}) ,
		contentType: "application/json; charset=utf-8",
		success: function(response) {
			$('#dialog').data("kendoWindow").close();
			loadMenu();
		},
		error:function( jqXHR ,  textStatus,  errorThrown ) {
		      console.log(errorThrown);
		},
		statusCode: {
            401: function() {
              $('#loginError').show();
            },
            422: function(xhr) {
              var errors = JSON.parse(xhr.responseText);
              var msg = "";
              $.each(errors, function(index, error) {
                msg += "<br> <b>" + error.name + ":</b> " + error.message;
              });
              showDialog(msg);
            }
          },
	});

}