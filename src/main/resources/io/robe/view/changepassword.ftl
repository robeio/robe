<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Robe Change Password</title>
    <link href="../../admin-ui/js/bootstrap/bootstrap.orange.css" rel="stylesheet">

</head>
<body>
<!-- NAV START-->
<nav class="navbar navbar-default navbar-static-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <a class="navbar-brand">
            <span id="bannerTitle">robe.io</span>
        </a>
    </div>
</nav>
<!-- NAV END-->

<div class="container well well-sm">
    <div class="span8">
        <form class="form-horizontal" id="registerHere" method="POST" action="../../robe/ticket">
            <fieldset>
                <legend>Change Password</legend>
                <div class="control-group">
                    <label class="control-label">New Password</label>

                    <div class="controls">
                        <input id="newPassword" type="password" name="newPassword">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">New Password (Confirm)</label>

                    <div class="controls">
                        <input id="newPasswordConfirm" type="password" name="newPasswordConfirm">
                    </div>
                </div>
            </fieldset>
            <div class="control-group">
                <label class="control-label"></label>

                <div class="controls">
                    <button type="submit" class="btn btn-success">Change It!</button>
                </div>
            </div>
            <input type="hidden" name="ticketOid" value="${ticketOid}"/>
        </form>
    </div>
</div>
</div>
</body>
</html>