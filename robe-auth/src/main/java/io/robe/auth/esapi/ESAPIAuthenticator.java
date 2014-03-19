package io.robe.auth.esapi;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import com.yammer.dropwizard.auth.Authenticator;
import edu.vt.middleware.password.*;
import io.robe.auth.data.entry.UserEntry;
import io.robe.auth.data.store.ServiceStore;
import io.robe.auth.data.store.UserStore;
import org.apache.log4j.Logger;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.reference.AbstractAuthenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ESAPIAuthenticator extends AbstractAuthenticator implements Authenticator<Object[], User> {

    private static final Logger LOGGER = Logger.getLogger(ESAPIAuthenticator.class);
    private static final Pattern PATTERN =  Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


    UserStore userStore;
    ServiceStore serviceStore;

    public ESAPIAuthenticator(UserStore userStore, ServiceStore serviceStore) {
        this.userStore = userStore;
        this.serviceStore = serviceStore;
    }

    /**
     * Verify that the supplied password matches the password for this user. Password should
     * be stored as a hash. It is recommended you use the hashPassword(password, accountName) method
     * in this class.
     * This method is typically used for "reauthentication" for the most sensitive functions, such
     * as transactions, changing email address, and changing other account information.
     *
     * @param user     the user who requires verification
     * @param password the hashed user-supplied password
     * @return true, if the password is correct for the specified user
     */
    @Override
    public boolean verifyPassword(User user, String password) {
        Optional<UserEntry> entry = (Optional<UserEntry>) userStore.findByUsername(user.getAccountName());
        if (entry.isPresent())
            return entry.get().getPassword().equals(Hashing.sha256().hashString(password).toString());
        else
            return false;
    }

    /**
     * !!! Anonymous User will be returned.
     * This method will not be implemented because of limitations  at create user at esapi
     *
     * @param accountName the account name of the new user
     * @param password1   the password of the new user
     * @param password2   the password of the new user.  This field is to encourage user interface designers to include two password fields in their forms.
     * @return the User that has been created
     * @throws org.owasp.esapi.errors.AuthenticationException if user creation fails due to any of the qualifications listed in this method's description
     */
    @Override
    public User createUser(String accountName, String password1, String password2) throws AuthenticationException {
        throw new AuthenticationException("This method will not be implemented because of limitations  at create user at esapi",accountName +": This method will not be implemented because of limitations  at create user at esapi");
    }

    /**
     * Generate a strong password. Implementations should use a large character set that does not
     * include confusing characters, such as i I 1 l 0 o and O.  There are many algorithms to
     * generate strong memorable passwords that have been studied in the past.
     *
     * @return a password with strong password strength
     */
    @Override
    public String generateStrongPassword() {
        List<CharacterRule> rules = new ArrayList<CharacterRule>();
        rules.add(new DigitCharacterRule(1));
        rules.add(new UppercaseCharacterRule(1));
        rules.add(new LowercaseCharacterRule(1));
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(8,rules);
    }


    /**
     * Generate strong password that takes into account the user's information and old password. Implementations
     * should verify that the new password does not include information such as the username, fragments of the
     * old password, and other information that could be used to weaken the strength of the password.
     *
     * @param user        the user whose information to use when generating password
     * @param oldPassword the old password to use when verifying strength of new password.  The new password may be checked for fragments of oldPassword.
     * @return a password with strong password strength
     */
    @Override
    public String generateStrongPassword(User user, String oldPassword) {
        String newPassword;
        do {
            newPassword = generateStrongPassword();
            // Continue until new password does not contain user info or same with old password
        } while (newPassword.contains(user.getAccountName()) || newPassword.contains(user.getName()) || oldPassword.equals(Hashing.sha256().hashString(newPassword).toString()));
        return newPassword;
    }

    /**
     * Changes the password for the specified user. This requires the current password, as well as
     * the password to replace it with. The new password should be checked against old hashes to be sure the new password does not closely resemble or equal any recent passwords for that User.
     * Password strength should also be verified.  This new password must be repeated to ensure that the user has typed it in correctly.
     *
     * @param user            the user to change the password for
     * @param currentPassword the current password for the specified user
     * @param newPassword     the new password to use
     * @param newPassword2    a verification copy of the new password
     * @throws org.owasp.esapi.errors.AuthenticationException if any errors occur
     */
    @Override
    public void changePassword( @NotNull  User user,  @NotNull String currentPassword, @NotNull String newPassword,  @NotNull String newPassword2) throws AuthenticationException {

        verifyPassword(user,currentPassword);

        if(!newPassword.equals(newPassword2))
            throw new AuthenticationException("New password and re-type password must be same",user.getAccountName() +": New password and re-type password must be same");
        if(newPassword.equals(currentPassword))
            throw new AuthenticationException("New password and old password must be different",user.getAccountName() +": New password and old password must be different");

        verifyPasswordStrength(currentPassword,newPassword,user);

        Optional<? extends UserEntry > optional = userStore.changePassword(user.getAccountName(),newPassword);
        if(!optional.isPresent())
            throw new AuthenticationException("Can't update User Password",user.getAccountName() +": Can't update User Password");
    }

    /**
     * !!! Anonymous User will be returned.
     * This method will not be implemented because of id type limitation of esapi
     * @param accountId the account id
     * @return The Anonymous User.
     */
    @Override
    public User getUser(long accountId) {
        return User.ANONYMOUS;
    }

    /**
     * Returns the User matching the provided accountName.  If the accoundId is not found, an Anonymous
     * User or null may be returned.
     *
     * @param accountName the account name
     * @return the matching User object, or the Anonymous User if no match exists
     */
    @Override
    public User getUser(String accountName) {
        Optional<? extends UserEntry> optional = userStore.findByUsername(accountName);
        if(optional.isPresent())
            return (User) optional.get();
        else
            return User.ANONYMOUS;
    }

    /**
     * Gets a collection containing all the existing user names.
     *
     * @return a set of all user names
     */
    @Override
    public Set getUserNames() {
        LOGGER.warn("This method will not be implemented");
        throw new RuntimeException("This method will not be implemented");
    }

    /**
     * Returns a string representation of the hashed password, using the
     * accountName as the salt. The salt helps to prevent against "rainbow"
     * table attacks where the attacker pre-calculates hashes for known strings.
     * This method specifies the use of the user's account name as the "salt"
     * value. The Encryptor.hash method can be used if a different salt is
     * required.
     *
     * @param password    the password to hash
     * @param accountName the account name to use as the salt
     * @return the hashed password
     * @throws org.owasp.esapi.errors.EncryptionException
     */
    @Override
    public String hashPassword(String password, String accountName) throws EncryptionException {
        return Hashing.sha256().hashString(password).toString();
    }

    /**
     * Removes the account of the specified accountName.
     *
     * @param accountName the account name to remove
     * @throws org.owasp.esapi.errors.AuthenticationException the authentication exception if user does not exist
     */
    @Override
    public void removeUser(String accountName) throws AuthenticationException {
        LOGGER.warn("This method will not be implemented");
        throw new RuntimeException("This method will not be implemented");
    }

    /**
     * Ensures that the account name passes site-specific complexity requirements, like minimum length.
     *
     * @param accountName the account name
     * @throws org.owasp.esapi.errors.AuthenticationException if account name does not meet complexity requirements
     */
    @Override
    public void verifyAccountNameStrength(String accountName) throws AuthenticationException {
        Matcher matcher = PATTERN.matcher(accountName);
        if(!matcher.matches())
            throw new AuthenticationException("Account name must be a valid email address",accountName + " is not a valid email");

    }



    /**
     * Ensures that the password meets site-specific complexity requirements, like length or number
     * of character sets. This method takes the old password so that the algorithm can analyze the
     * new password to see if it is too similar to the old password. Note that this has to be
     * invoked when the user has entered the old password, as the list of old
     * credentials stored by ESAPI is all hashed.
     * Additionally, the user object is taken in order to verify the password and account name differ.
     *
     * @param oldPassword the old password
     * @param newPassword the new password
     * @param user        the user
     * @throws org.owasp.esapi.errors.AuthenticationException if newPassword is too similar to oldPassword or if newPassword does not meet complexity requirements
     */
    @Override
    public void verifyPasswordStrength(String oldPassword, String newPassword, User user) throws AuthenticationException {
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(new DigitCharacterRule(1));
        rules.add(new UppercaseCharacterRule(1));
        rules.add(new LowercaseCharacterRule(1));
        rules.add(new LengthRule(8, 16));
        rules.add(new WhitespaceRule());
        PasswordValidator validator = new PasswordValidator(rules);
        PasswordData passwordData = new PasswordData(new Password(newPassword));
        RuleResult result = validator.validate(passwordData);
        if (!result.isValid()){
            StringBuilder messages = new StringBuilder();
            for (String msg : validator.getMessages(result)) {
                messages.append(msg).append("\n");
            }
            throw new AuthenticationException(messages.toString(),messages.toString());
        }


    }

    /**
     * Given a set of user-provided credentials, return an optional principal.
     * <p/>
     * <p>If the credentials are valid and map to a principal, returns an {@code Optional.of(p)}.</p>
     * <p/>
     * <p>If the credentials are invalid, returns an {@code Optional.absent()}.</p>
     *
     * @param requestResponse request and response as array
     * @return either an authenticated principal or an absent optional
     * @throws com.yammer.dropwizard.auth.AuthenticationException if the credentials cannot be authenticated due to an
     *                                 underlying error
     */
    @Override
    public Optional<User> authenticate(Object[] requestResponse) throws com.yammer.dropwizard.auth.AuthenticationException {
        User user = null;
        try {
            // If array has 2 objects and they are at correct types
            if (requestResponse.length == 2 || requestResponse[0] instanceof HttpServletRequest &&requestResponse[1] instanceof HttpServletResponse)
                user = login((HttpServletRequest)requestResponse[0], (HttpServletResponse)requestResponse[1]);
            else
                throw new RuntimeException("Parameter requestResponse array is not suitable for this method. Use as Object[HttpServletRequest,HttpServletResponse]");
        } catch (AuthenticationException e) {
            throw new com.yammer.dropwizard.auth.AuthenticationException(e);
        }
        return Optional.fromNullable(user);
    }
}
