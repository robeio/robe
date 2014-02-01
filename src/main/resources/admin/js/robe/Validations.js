function getValidations (field,label, isRequired, isEmail, minlength, maxlength,regexp) {
		var validations =  {};

		if (isRequired)
			validations["required"] = {
				message: label + " alanı gerekli."
			};
		if (isEmail)
			validations["email"] = {
				message: label + " yazımı hatalı."
			};
		if (minlength)
			validations["minlength"] =  function(input){
				if((input.is("[data-bind='value:"+field+"']") && input.val().length<minlength)){
                    input.attr("data-minlength-msg", label + " alanı en az " + minlength + " karakter olmalı.");
					return false;
				}
				return true;

			};
		if (maxlength)
            validations["maxlength"] =  function(input){
				if((input.is("[data-bind='value:"+field+"']") && input.val().length>maxlength)){
					input.attr("data-maxlength-msg", label + " alanı en fazla " + maxlength + " karakter olmalı.");
					return false;
				}
				return true;
			};
		if (regexp)
			validations["regexp"] =  function(input){
				if((input.is("[data-bind='value:"+field+"']") && !(new RegExp("A-Z").test(input.val())))){
					input.attr("data-regexp-msg", label + " alanı " + regexp + " formatına uymalı.");
					return false;
				}
				return true;
			};
		return validations;
}