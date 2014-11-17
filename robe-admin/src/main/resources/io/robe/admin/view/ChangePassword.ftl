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
    <!-- BOOTSTRAP CORE STYLE CSS -->
    <link href="../admin-ui/lib/bootstrap/bootstrap.orange.css" rel="stylesheet"/>
    <!-- FONTAWESOME STYLE CSS -->
    <!-- CUSTOM STYLE CSS -->
    <!-- GOOGLE FONT -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'/>
    <script src="../admin-ui/lib/jquery/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#save").bind("click", function () {
                var email = $("#email").val();
                var password = $("#password").val();
                var rePassword = $("#rePassword").val();
                var ticket = $("#ticket").val();

                var alert = $("#alert");

                var message = "";
                var error = false;

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
                    url: ${url.value}+"user/registerPassword",
                    'contentType': 'application/json',
                    data: '{"email":"' + email + '","ticket":"' + ticket + '","newPassword":"' + password + '","username":"' + email + '"}',
                    'dataType': 'json',
                    success: function (response) {
                        alert.removeClass("alert-danger");
                        alert.addClass("alert-success");
                        alert.html("Record was successfully added");
                    },
                    error: function (request) {
                        var response = JSON.parse(request.responseText);
                        alert.addClass("alert-danger");
                        alert.removeClass("alert-success");
                        alert.html(response.name + " " + response.value);
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

            <h2>Şifre Kayıt Formu</h2>
        </div>
    </div>
    <div class="row  pad-top">

        <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">
            <div class="panel panel-default">
                <div class="panel-heading" style="text-align: center;">
                    <strong>Lütfen Tüm Alanları Doldrurunuz</strong>
                </div>
                <div class="panel-body">
                    <form role="form">
                        <br/>

                        <div class="form-group input-group">
                            <span class="input-group-addon">Mail&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <input id="email" type="text" class="form-control" readonly value="${mail.value}"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">Şifre&nbsp;&nbsp;&nbsp;</span>
                            <input id="password" type="password" class="form-control"/>
                        </div>
                        <div class="form-group input-group">
                            <span class="input-group-addon">Tekrar</span>
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