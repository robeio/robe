//@ sourceURL=AlertDialog.js

function showToast(type, message) {
    $().toastmessage('showToast', {
        text: message,
        sticky: false,
        type: type,
        position: 'top-right'
    });
};