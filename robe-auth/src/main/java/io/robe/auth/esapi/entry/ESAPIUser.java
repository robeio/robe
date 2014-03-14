package io.robe.auth.esapi.entry;

import org.owasp.esapi.reference.DefaultUser;


public class ESAPIUser extends DefaultUser {

    public ESAPIUser(String accountName){
        super(accountName);
    }
}
