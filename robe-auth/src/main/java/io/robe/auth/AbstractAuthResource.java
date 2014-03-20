package io.robe.auth;

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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An Abstract class with necessary implementations of common used methods (Compatible with ESAPI Authenticator)
 * {@inheritDoc}
 * @param <T>
 */
public abstract class AbstractAuthResource<T> implements Authenticator<T, UserEntry> {

    private static final Logger LOGGER = Logger.getLogger(AbstractAuthResource.class);
    private static final Pattern PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


    UserStore userStore;
    ServiceStore serviceStore;

    public AbstractAuthResource(UserStore userStore, ServiceStore serviceStore) {
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
    public boolean verifyPassword(User user, String password) {
        Optional<UserEntry> entry = (Optional<UserEntry>) userStore.findByUsername(user.getAccountName());
        if (entry.isPresent())
            return entry.get().getPassword().equals(Hashing.sha256().hashString(password).toString());
        else
            return false;
    }

    /**
     * Generate a strong password. Implementations should use a large character set that does not
     * include confusing characters, such as i I 1 l 0 o and O.  There are many algorithms to
     * generate strong memorable passwords that have been studied in the past.
     *
     * @return a password with strong password strength
     */
    public String generateStrongPassword() {
        List<CharacterRule> rules = new ArrayList<CharacterRule>();
        rules.add(new DigitCharacterRule(1));
        rules.add(new UppercaseCharacterRule(1));
        rules.add(new LowercaseCharacterRule(1));
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(8, rules);
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
    public String generateStrongPassword(User user, String oldPassword) {
        String newPassword;
        do {
            newPassword = generateStrongPassword();
            // Continue until new password does not contain user info or same with old password
        }
        while (newPassword.contains(user.getAccountName()) || newPassword.contains(user.getName()) || oldPassword.equals(Hashing.sha256().hashString(newPassword).toString()));
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
    public void changePassword(@NotNull User user, @NotNull String currentPassword, @NotNull String newPassword, @NotNull String newPassword2) throws AuthenticationException {

        verifyPassword(user, currentPassword);

        if (!newPassword.equals(newPassword2))
            throw new AuthenticationException("New password and re-type password must be same", user.getAccountName() + ": New password and re-type password must be same");
        if (newPassword.equals(currentPassword))
            throw new AuthenticationException("New password and old password must be different", user.getAccountName() + ": New password and old password must be different");

        verifyPasswordStrength(currentPassword, newPassword, user);

        Optional<? extends UserEntry> optional = userStore.changePassword(user.getAccountName(), newPassword);
        if (!optional.isPresent())
            throw new AuthenticationException("Can't update User Password", user.getAccountName() + ": Can't update User Password");
    }

    /**
     * Returns the User matching the provided accountName.  If the accoundId is not found, an Anonymous
     * User or null may be returned.
     *
     * @param accountName the account name
     * @return the matching User object, or the Anonymous User if no match exists
     */
    public User getUser(String accountName) {
        Optional<? extends UserEntry> optional = userStore.findByUsername(accountName);
        if (optional.isPresent())
            return (User) optional.get();
        else
            return User.ANONYMOUS;
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
    public String hashPassword(String password, String accountName) throws EncryptionException {
        return Hashing.sha256().hashString(password).toString();
    }


    /**
     * Ensures that the account name passes site-specific complexity requirements, like minimum length.
     *
     * @param accountName the account name
     * @throws org.owasp.esapi.errors.AuthenticationException if account name does not meet complexity requirements
     */
    public void verifyAccountNameStrength(String accountName) throws AuthenticationException {
        Matcher matcher = PATTERN.matcher(accountName);
        if (!matcher.matches())
            throw new AuthenticationException("Account name must be a valid email address", accountName + " is not a valid email");
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
        if (!result.isValid()) {
            StringBuilder messages = new StringBuilder();
            for (String msg : validator.getMessages(result)) {
                messages.append(msg).append("\n");
            }
            throw new AuthenticationException(messages.toString(), messages.toString());
        }


    }

    /**
     * Given a set of user-provided credentials, return an optional principal.
     * <p/>
     * <p>If the credentials are valid and map to a principal, returns an {@code Optional.of(p)}.</p>
     * <p/>
     * <p>If the credentials are invalid, returns an {@code Optional.absent()}.</p>
     *
     * @param  t necessary object for login
     * @return either an authenticated principal or an absent optional
     * @throws com.yammer.dropwizard.auth.AuthenticationException if the credentials cannot be authenticated due to an
     *                                                            underlying error
     */
    @Override
    public Optional<UserEntry> authenticate(T t) throws com.yammer.dropwizard.auth.AuthenticationException {
        UserEntry user = null;
        try {
            user = login(t);
        } catch (Exception e) {
            throw new com.yammer.dropwizard.auth.AuthenticationException(e);
        }
        return Optional.fromNullable(user);
    }

    /**
     * An abstract method for login operation.
     * @param t desired object to login
     * @return User object
     */
    protected abstract UserEntry login(T t);
}
