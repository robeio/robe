var User = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: false,
            type: "string"
        },
        lastUpdated: {
            editable: true,
            nullable: false,
            type: "string"
        },
        deleted: {
            editable: false,
            nullable: true,
            type: "string"
        },
        name: {
            editable: true,
            nullable: false,
            type: "string"
        },
        surname: {
            editable: true,
            nullable: false,
            type: "string"
        },
        email: {
            editable: true,
            nullable: false,
            type: "string"
        },
        active: {
            type: "boolean"
        },
        roleOid: {
            editable: true,
            nullable: false,
        },
        role: {
        },
        password: {
            editable: true,
            nullable: false,
            hidden: true,
            type: "string"
        },
        firmOid: {
            editable: true,
            nullable: false,
            type: "string"
        },
        firm:{

        }

    }
});

var Accountant = kendo.data.Model.define({

    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: false
        },
        lastUpdated: {
            editable: true,
            nullable: false
        },
        deleted: {
            editable: false,
            nullable: true
        },
        title: {
            "editable": true
        },
        name: {
            "editable": true
        },
        surname: {
            "editable": true
        },
        phoneDescription: {
            "editable": true
        },
        phone: {
            "editable": true
        },
        fax: {
            "editable": true
        },
        email: {
            "editable": true
        },
        agreementType: {
            "editable": true
        },
        agreementNo: {
            "editable": true
        },
        agreementDate: {
            "editable": true
        },
        country: {
            "editable": true
        },
        city: {
            "editable": true
        },
        street: {
            "editable": true
        },
        address: {
            "editable": true
        },
        buildingNumber: {
            "editable": true
        },
        doorNumber: {
            "editable": true
        },
        postalCode: {
            "editable": true
        }
    }

});

var Role = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: false
        },
        lastUpdated: {
            editable: true,
            nullable: false
        },
        deleted: {
            editable: false,
            nullable: true
        },
        name: {
            editable: true,
            nullable: false
        },
        code: {
            editable: true,
            nullable: false
        }
    }
});

var Firm = kendo.data.Model.define({
    id: "oid",
    fields: {
        "oid": {
            editable: false,
            nullable: false
        },
        "lastUpdated": {
            editable: true,
            nullable: false
        },
        "deleted": {
            editable: false,
            nullable: true
        },
        "taxIdentifier": {
            "editable": true
        },
        "phone": {
            "editable": true
        },
        "phoneDescription": {
            "editable": true
        },
        "fax": {
            "editable": true
        },
        "email": {
            "editable": true
        },
        "organizationDescription": {
            "editable": true
        },
        "organizationIdentifier": {
            "editable": true
        },
        "buildingNumber": {
            "editable": true
        },
        "street": {
            "editable": true
        },
        "addressDetail": {
            "editable": true
        },
        "city": {
            "editable": true
        },
        "country": {
            "editable": true
        },
        "website": {
            "editable": true
        },
        "businessDescription": {
            "editable": true
        },
        "fiscalYearStart": {
            "editable": true,
            type: "date"
        },
        "fiscalYearEnd": {
            "editable": true,
            type: "date"
        },
        "postalCode": {
            "editable": true
        },
        "active": {
            type: "boolean"
        }
    }
});

var Service = kendo.data.Model.define({
   id: "oid",
   fields: {
	   oid: {
		   editable: false,
		   nullable: false
	   },
	   lastUpdated: {
		   editable: true,
		   nullable: false
	   },
	   path: {
		   editable: true,
		   nullable: true
	   },
	   method: {
		   editable: true,
		   nullable: false
	   }
   }
});