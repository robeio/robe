@args List<io.robe.crud.helper.Model> models,String entity
define([
    'kendo/kendo.data.min', 'robe/Validations'
], function() {

    var @entity=kendo.data.Model.define({
        id: "oid",
        fields: {
            oid: {
                editable: false,
                nullable: true,
                type: "string"
            },@for (io.robe.crud.helper.Model model: models) {
            {
                editable : true,
                nullable: @model.isNullable(),
                validation: getValidations("@model.getName()","@model.getName()",@if(model.isNullable()){false}else{true}, false, 0, @model.getLength(),""),
                type:"@model.getType()"
            }@if(model_index<models.size()){,}
        }
    });
    return @entity;
});