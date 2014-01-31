$.ajaxSetup({
  dataType: "json",
  statusCode: {
    401: function() {
      $('#dialogMessage').load("../html/Login.html", function() {
        showDialog(null, "Giriş");
        eval("initializeLogin();");
      });
    },
    422: function(xhr) {
      var errors = JSON.parse(xhr.responseText);
      var msg = "<span style='float:right;'>";
      $.each(errors, function(index, error) {
        if (error.name != "") {
          msg += "<br> <b>" + error.name
          if (error.message != "")
            msg += ":";
          msg += "</b> "
        }
        if (error.message != "") {
          msg += error.message;
        }
      });
      showDialog("<img style='float:left; margin-right:10px;' src='../icon/close.png'/>" + msg + "</span>", "Hata");
    }
  },
  beforeSend: function(xhr) {

  },
  complete: function(xhr, status) {
    console.log(status + " : " + xhr);
  }
});

$(document).ready(function() {

  loadConfig();
  $("#progressBar").kendoProgressBar({
    min: 0,
    max: 1,
    type: "value",
    showStatus: false,
    animation: {
      duration: 200
    }
  });
  kendo.culture("tr-TR");
  $("#logout").kendoButton({
    click: function() {
      $.cookie.destroy("MedyAuthToken");
      location.reload();
    },
    imageUrl: "../icon/disconnect.png"
  });

  $('#dialog').kendoWindow({
    actions: ["Close"],
    modal: true,
    visible: false
  });

  $('#dialogMessage').load("../html/Login.html", function() {
    showDialog(null, "Giriş");
    eval("initializeLogin();");
  });

  $(document).ajaxStart(function() {
    showIndicator(true);
  });
  $(document).ajaxStop(function() {
    showIndicator(false);
  });
});

function loadMenu() {
  $.ajax({
    type: "GET",
    url: getBackendURL() + "menu/user",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function(response) {
      addIcons(response[0]);
      $('#menu').kendoPanelBar({
        dataSource: response[0].items,
        select: onSelect,
      });

    }
  });
}

function addIcons(menu) {
  if (menu.hasOwnProperty("items")) {
    for (var i = 0; i < menu.items.length; i++) {
      addIcons(menu.items[i]);
    }
  }
  menu.imageUrl = "../icon/menu/" + menu.cssClass.substring(8) + ".png";
}

function onSelect(e) {

  var selection = "k-";
  for (var i = 0; i < e.item.classList.length; i++) {
    var css = e.item.classList[i];
    if (css.indexOf("command:") == 0) {
      selection = css.substring(8);
      break;
    }
  }
  openMenuItem(selection);
}
//
function openMenuItem(menuitem) {

  kendo.destroy($('#container'));
  $('#container').html('');

  if (menuitem.indexOf("k-") == 0)
    return;

  $('#container').load("../html/" + menuitem + ".html", function() {
    try {
      eval("initialize" + menuitem + "();");

    } catch (e) {
      console.log(menuitem + " : " + e);
    }
    kendo.fx($("#container")).fade("in").play();
  });
}
//
function showIndicator(show) {
  if (show)
    $("#progressBar").data("kendoProgressBar").value(0);
  else
    $("#progressBar").data("kendoProgressBar").value(1);

}

function showDialog(message, title) {
  if (message != null)
    $('#dialogMessage').html(message);
  if (title == null)
    title = "";
  $('#dialog').data("kendoWindow").title(title);
  $('#dialog').data("kendoWindow").center();
  $('#dialog').data("kendoWindow").open();
}

function loadConfig() {
  $.ajax({
    dataType: "json",
    url: "../config.json",
    success: function(response) {
      backendURL = response.backendURL;
      adminURL = response.adminURL;
    }
  });
}
var backendURL = "";
var adminURL = "";

function getBackendURL() {
  return backendURL;
}

function getAdminURL() {
  return adminURL;
}