<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <title>Robe.io</title>
    <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#save").bind("click", function () {
                var name = $("#name").val();
                var surname = $("#surname").val();
                var email = $("#email").val();
                var password = $("#password").val();
                var rePassword = $("#rePassword").val();
                var ticket = $("#ticket").val();


                var alert = $("#alert");

                var message = "";
                var error = false;

                if (!name) {
                    message += "Name i empty " + "<br/>";
                    error = true
                }
                if (!surname) {
                    message += "Surname is empty" + "<br/>";
                    error = true
                }

                if (!email) {
                    message += "Email is empty" + "<br/>";
                    error = true
                }

                if (!validateEmail(email)) {
                    message += "EMail is not valid" + "<br/>";
                    error = true;
                }

                if (!password) {
                    message += "Password is empty" + "<br/>";
                    error = true
                }

                if (!rePassword) {
                    message += "Re-password is empty" + "<br/>";
                    error = true;
                }

                if (password != rePassword) {
                    message += "Password and re-password must be equal" + "<br/>";
                    error = true
                }

                if (!ticket) {
                    message += "Ticket is empty" + "<br/>";
                    error = true
                }

                if (error) {
                    alert.html(message);
                    alert.show();
                    return;
                }

                $.ajax({
                    type: "POST",
                    url: ${url.value}+"user/registerByMail",
                    'contentType': 'application/json',
                    data: '{"email":"' + email + '","username":"' + email + '","name":"' + name + '","surname":"' + surname + '","ticket":"' + ticket + '","newPassword":"' + password + '"}',
                    'dataType': 'json',
                    success: function (response) {
                        var alert = $("#alert");
                        alert.removeClass("alert-danger");
                        alert.addClass("alert-success");
                        alert.html("Record was successfully added");
                        alert.show();
                    },
                    error: function (request) {
                        var response = JSON.parse(request.responseText);
                        var alert = $("#alert");
                        alert.addClass("alert-danger");
                        alert.removeClass("alert-success");
                        alert.html(response.name + " " + response.value);
                        alert.show();
                    }
                });
            });
            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
        });
    </script>

</head>
<body>
<div class="container">
    <div class="row text-center pad-top ">
        <div class="col-md-12">
            <h2>Robe.io</h2>

            <h2>Register Form</h2>
        </div>
    </div>
    <div class="row  pad-top">

        <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">
            <div class="panel panel-default">
                <div class="panel-heading" style="text-align: center;">
                    <strong> Please fill in all fields</strong>
                </div>
                <div class="panel-body">
                    <form role="form">
                        <br/>

                        <div class="form-group input-group">
                            <span class="input-group-addon">Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <input id="name" type="text" class="form-control"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">Surnanme</span>
                            <input id="surname" type="text" class="form-control"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">E-Mail&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <input id="email" type="text" class="form-control" readonly value="${mail.value}"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">Password&nbsp;&nbsp;&nbsp;</span>
                            <input id="password" type="password" class="form-control"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">Again</span>
                            <input id="rePassword" type="password" class="form-control"/>
                        </div>

                        <div class="form-group input-group">
                            <span class="input-group-addon">Ticket:</span>
                            <input id="ticket" type="text" class="form-control" readonly value="${ticket.value}"/>
                        </div>
                        <div class="alert alert-danger" role="alert" id="alert" hidden>
                            Enter a valid email address
                        </div>
                        <a href="#" id="save" style="float: right" class="btn btn-success ">Kayıt Ol</a>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>