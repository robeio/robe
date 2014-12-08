package io.robe.auth;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface Token extends Credentials {


    /**
     * Returns owner username of the  token
     * @return username
     */
    public String  getUsername();

    /**
     * Returns the status of token expiration.
     * @return true if token expired.
     */
    public boolean      isExpired();

    /**
     * Sets the expirationdate by adding duration to current time
     * @param durationInSeconds
     */
    public void         setExpiration(int durationInSeconds);

    /**
     * Returns the expiration date for token
     * @return expiration date
     */
    public Date         getExpirationDate();

    /**
     * Generates the attibuteHash for the token. All necessary calculations will be done and hash will be updated.
     * @param
     */
    public void         updateAttributeHash(Map<String, String> map);

    /**
     * Returns the calculated hash of attributes.
     * @return
     */
    public String       getAttributesHash();

    public String       getTokenString() throws Exception;

    public int          getMaxAge();

    public void setPermissions(Set<String> permissions );

    public Set<String> getPermissions();
}
