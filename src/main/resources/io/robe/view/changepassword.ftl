<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Robe Change Password</title>
    <link rel="stylesheet" href="/bootstrap-2.0.2/css/bootstrap.css"/>
    <link rel="stylesheet" href="/awesome-1.0.0/css/font-awesome.css"/>
    <script type="text/javascript" src="/js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="/bootstrap-2.0.2/js/bootstrap.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="span8">

            <form class="form-horizontal" id="registerHere" method="post"
                  action="/robe/rest/changepassword">

                <fieldset>
                    <legend>Change Password</legend>
                    <div class="control-group">
                        <label class="control-label">New Password</label>

                        <div class="controls">
                            <input type="password" name="newpassword" class="span3">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">New Password (Confirm)</label>

                        <div class="controls">
                            <input type="password" name="newpassword2" class="span3">
                        </div>
                    </div>
                </fieldset>
                <div class="control-group">
                    <label class="control-label"></label>

                    <div class="controls">
                        <button type="submit" class="btn btn-success">Change It!</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</div>

</body>
</html>