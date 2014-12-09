package io.robe.auth.tokenbased;

import io.robe.auth.Credentials;

import java.util.Date;
import java.util.Set;

public interface Token extends Credentials {


    /**
     * Returns owner username of the  token
     *
     * @return username
     */
    public String getUsername();

    /**
     * Returns the status of token expiration.
     *
     * @return true if token expired.
     */
    public boolean isExpired();

    /**
     * Sets the expirationdate by adding duration to current time
     *
     * @param durationInSeconds
     */
    public void setExpiration(int durationInSeconds);

    /**
     * Returns the expiration date for token
     *
     * @return expiration date
     */
    public Date getExpirationDate();

    /**
     * Returns the calculated hash of attributes.
     *
     * @return
     */
    public String getAttributesHash();

    /**
     * String representation of the token
     *
     * @return
     * @throws Exception
     */
    public String getTokenString() throws Exception;

    /**
     * Max age of the token
     *
     * @return
     */
    public int getMaxAge();

    /**
     * Set permissions to token
     *
     * @param permissions
     */
    public void setPermissions(Set<String> permissions);

    /**
     * Gets permissions
     *
     * @return
     */
    public Set<String> getPermissions();
}
