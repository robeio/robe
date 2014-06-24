function getValidations(field, label, isRequired, isEmail, minlength, maxlength, regexp) {
    var validations = {};

    if (isRequired)
        validations["required"] = {
            message: label + " alanı gerekli."
        };
    if (isEmail)
        validations["email"] = {
            message: label + " yazımı hatalı."
        };
    if (minlength)
        validations[field + "_minlength"] = function (input) {
            if ((input.is("[data-bind='value:" + field + "']") && input.val().length < minlength)) {
                input.attr("data-" + field + "_minlength-msg", label + " alanı en az " + minlength + " karakter olmalı.");
                return false;
            }
            return true;

        };
    if (maxlength) {
        validations[field + "_maxlength"] = function (input) {
            var isInputField = input.is('[data-bind=\"value:' + field + '\"]');
            if (isInputField && input.val().length > maxlength) {
                input.attr('data-' + field + '_maxlength-msg', label + ' alanı en fazla ' + maxlength + ' karakter olmalı.');
                return false;
            }
            return true
        }
    }

    if (regexp)
        validations[field + "_regexp"] = function (input) {
            if ((input.is("[data-bind='value:" + field + "']") && !(new RegExp(regexp).test(input.val())))) {
                input.attr("data-" + field + "_regexp-msg", label + " alanı " + regexp + " formatına uymalı.");
                return false;
            }
            return true;
        };
    return validations;
}