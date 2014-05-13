${modelName}  = kendo.data.Model.define({
        id: "oid",
        fields: {
                oid: {
                        editable: false,
                        nullable: true,
                        type: "string"
                    },
 			<#list fields as field>
    			${field.name} :{
    					editable : ${field.editable?string},
    					nullable : ${field.nullable?string}<#if field.validation>,    					
    					validation :validation: getValidations("${field.name}", "${field.name}", true, false, 2, 20, "[A-Za-z]+")</#if>
    			}<#if field_has_next>,</#if>
		</#list>
        }
});

