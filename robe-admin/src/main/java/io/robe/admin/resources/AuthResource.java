package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.metrics.annotation.Timed;
import edu.vt.middleware.password.*;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import io.robe.auth.TokenWrapper;
import org.owasp.esapi.ESAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Authentication Resource to provide standard Authentication services like login,change password....
 */
//TODO: Take it to auth module and convert to a common interface
@Path("authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource {

    @Inject
    UserDao userDao;


    @POST
    @UnitOfWork
    @Timed
    @Path("login")
    public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response, Map<String, String> credentials) throws Exception {

        Optional<User> user = userDao.findByUsername(credentials.get("username"));
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (user.get().getPassword().equals(credentials.get("password"))) {
            IsToken token = TokenWrapper.createToken(user.get().getEmail(), null);

            try {
                ESAPI.authenticator().login(request, response);
            } catch (Exception e) {
                throw new WebApplicationException(e,Response.Status.UNAUTHORIZED);
            }
            credentials.remove("password");
            return Response.ok().header("Set-Cookie", "auth-token" + "=" + token.getToken() + ";path=/;domain=" + request.getRemoteHost() + ";").entity(credentials).build();

        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

    }

    @POST
    @UnitOfWork
    @Path("changepassword")
    public Response changePassword(@Auth Credentials clientDetails, @FormParam("newpassword") String newPassword) {
        //TODO Change password.
        return Response.ok().build();
    }


    @POST
    @UnitOfWork
    @Path("forgotpassword")
    public User forgotPassword(@FormParam("email") String email, @FormParam("clientId") String clientId, @Context UriInfo uriInfo) {
        //TODO password request
        return null;
    }

    public User completeforgotPassword(String email, String clientId) {
        //TODO complete request with a mail
        return null;
    }


    /*
     * Password can't be sa with last 3 paswords
     * Cannot be same with birtdate
     * Cant be username
     * Cant be email
     * cant be sequence
     * cant be repeative numbers or chars
     * Cant be less then 6 alfanumeric
     */
    public static String checkPasswordPolicy(String newPassword) {
        List<Rule> ruleList = getPasswordRules();

        PasswordValidator validator = new PasswordValidator(ruleList);
        PasswordData passwordData = new PasswordData(new Password(newPassword));

        RuleResult result = validator.validate(passwordData);
        if (result.isValid()) {
            return "true";
        } else {
            StringBuilder builder = new StringBuilder();
            for (String msg : validator.getMessages(result)) {
                builder.append(msg).append("\n");
            }
            return builder.toString();
        }


    }


    private static List<Rule> getPasswordRules() {
        // password must be between 8 and 20 chars long
        LengthRule lengthRule = new LengthRule(8, 20);

        // don't allow whitespace
        WhitespaceRule whitespaceRule = new WhitespaceRule();

        // control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
        // require at least 4 digit in passwords
        charRule.getRules().add(new DigitCharacterRule(4));
        // require at least 1 upper case char
        charRule.getRules().add(new UppercaseCharacterRule(1));
        // require at least 2 lower case char
        charRule.getRules().add(new LowercaseCharacterRule(2));
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
}
