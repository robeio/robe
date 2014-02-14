/**
 * Created by kaanalkim on 14/02/14.
 */

function showToast(type, message) {
    $().toastmessage('showToast', {
        text: message,
        sticky: false,
        type: type,
        position: 'top-right'
    });
};