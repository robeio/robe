package io.robe.auth;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import com.yammer.dropwizard.auth.AuthenticationException;
import edu.vt.middleware.password.*;
import io.robe.auth.data.entry.UserEntry;
import io.robe.auth.data.store.UserStore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An Abstract class with necessary implementations of common used methods (Compatible with ESAPI Authenticator)
 * {@inheritDoc}
 * @param <T>
 */
public abstract class AbstractAuthResource<T extends UserEntry> {

    private static final Pattern PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


    private final UserStore userStore;

    public AbstractAuthResource(UserStore userStore) {
        this.userStore = userStore;
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
    public boolean verifyPassword(T user, String password) {
        Optional<T> entry;
        entry = (Optional<T>) userStore.findByUsername(user.getUsername());
        return entry.isPresent() && entry.get().getPassword().equals(Hashing.sha256().hashString(password).toString());
    }

    /**
     * Generate a strong password. Implementations should use a large character set that does not
     * include confusing characters, such as i I 1 l 0 o and O.  There are many algorithms to
     * generate strong memorable passwords that have been studied in the past.
     *
     * @return a password with strong password strength
     */
    public String generateStrongPassword() {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(8, getPasswordCharacterRules());
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
    public String generateStrongPassword(T user, String oldPassword) {
        String newPassword;
        do {
            newPassword = generateStrongPassword();
            // Continue until new password does not contain user info or same with old password
        } while (newPassword.contains(user.getUsername()) || oldPassword.equals(Hashing.sha256().hashString(newPassword).toString()));
        return newPassword;
    }

    /**
     * Changes the password for the specified user. This requires the current password, as well as
     * the password to replace it with. The new password should be checked against old hashes to be sure the new password does not closely resemble or equal any recent passwords for that UserEntry.
     * Password strength should also be verified.  This new password must be repeated to ensure that the user has typed it in correctly.
     *
     * @param user            the user to change the password for
     * @param currentPassword the current password for the specified user
     * @param newPassword     the new password to use
     * @param newPassword2    a verification copy of the new password
     * @throws com.yammer.dropwizard.auth.AuthenticationException if any errors occur
     */
    public void changePassword(T user, String currentPassword, String newPassword, String newPassword2) throws AuthenticationException {

        verifyPassword(user, currentPassword);

        if (!newPassword.equals(newPassword2)){
            throw new AuthenticationException( user.getUsername() + ": New password and re-type password must be same");
        }else if (newPassword.equals(currentPassword)){
            throw new AuthenticationException(user.getUsername() + ": New password and old password must be different");
        }
        verifyPasswordStrength(currentPassword, newPassword, user);

        Optional<? extends UserEntry> optional = userStore.changePassword(user.getUsername(), newPassword);
        if (!optional.isPresent()){
            throw new AuthenticationException(user.getUsername() + ": Can't update UserEntry Password");
        }
    }

    /**
     * Returns the UserEntry matching the provided accountName.  If the accoundId is not found, an Anonymous
     * UserEntry or null may be returned.
     *
     * @param accountName the account name
     * @return the matching UserEntry object, or the Null UserEntry if no match exists
     */
    public T getUser(String accountName) {
        Optional<T> optional = (Optional<T>) userStore.findByUsername(accountName);
        if (optional.isPresent()){
            return  optional.get();
        }else {
            return null;
        }
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
     */
    public String hashPassword(String password, String accountName) {
        return Hashing.sha256().hashString(password).toString();
    }


    /**
     * Ensures that the account name passes site-specific complexity requirements, like minimum length.
     *
     * @param accountName the account name
     * @throws com.yammer.dropwizard.auth.AuthenticationException if account name does not meet complexity requirements
     */
    public void verifyAccountNameStrength(String accountName) throws AuthenticationException {
        Matcher matcher = PATTERN.matcher(accountName);
        if (!matcher.matches()){
            throw new AuthenticationException(accountName + " is not a valid email");
        }
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
     * @throws com.yammer.dropwizard.auth.AuthenticationException if newPassword is too similar to oldPassword or if newPassword does not meet complexity requirements
     */
    public void verifyPasswordStrength(String oldPassword, String newPassword, T user) throws AuthenticationException {
        List<Rule> rules = getPasswordRules();
        PasswordValidator validator = new PasswordValidator(rules);
        PasswordData passwordData = new PasswordData(new Password(newPassword));
        RuleResult result = validator.validate(passwordData);
        if (!result.isValid()) {
            StringBuilder messages = new StringBuilder();
            for (String msg : validator.getMessages(result)) {
                messages.append(msg).append("\n");
            }
            throw new AuthenticationException(messages.toString());
        }


    }


    private static List<Rule> getPasswordRules() {
        // password must be between 8 and 20 chars long
        LengthRule lengthRule = new LengthRule(8, 20);

        // don't allow whitespace
        WhitespaceRule whitespaceRule = new WhitespaceRule();

        // control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
        charRule.getRules().addAll(getPasswordCharacterRules());
        // require at least 3 of the previous rules be met
        charRule.setNumberOfCharacteristics(3);

        // don't allow alphabetical sequences
        AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule(3, false);
        // don't allow numerical sequences of length 3
        NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3, false);
        // don't allow qwerty sequences
        QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
        // don't allow 3 repeat characters
        RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(3);

        // group all rules together in a List
        List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add(lengthRule);
        ruleList.add(whitespaceRule);
        ruleList.add(charRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(numSeqRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);
        return ruleList;
    }

    private static List<CharacterRule> getPasswordCharacterRules() {
        List<CharacterRule> rules = new ArrayList<CharacterRule>(3);
        // require at least 4 digit in passwords
        rules.add(new DigitCharacterRule(4));
        // require at least 1 upper case char
        rules.add(new UppercaseCharacterRule(1));
        // require at least 2 lower case char
        rules.add(new LowercaseCharacterRule(2));

        return rules;
    }

}
