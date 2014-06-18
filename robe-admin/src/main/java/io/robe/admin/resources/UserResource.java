package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.dto.UserDTO;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.admin.hibernate.entity.User;
import io.robe.admin.util.ExceptionMessages;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import io.robe.mail.MailBundle;
import org.joda.time.DateTime;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    @Inject
    UserDao userDao;
    @Inject
    RoleDao roleDao;

    @Inject
    TicketDao ticketDao;

    @Path("all")
    @GET
    @UnitOfWork
    public List<UserDTO> getUsers(@Auth Credentials credentials) {
        List<User> entities = userDao.findAll(User.class);
        List<UserDTO> users = new LinkedList<UserDTO>();
        for (User entity : entities) {
            UserDTO user = new UserDTO(entity);
            users.add(user);
        }
        return users;
    }

    @GET
    @UnitOfWork
    @Path("{userId}")
    public UserDTO get(@Auth Credentials credentials, @PathParam("userId") String id) {
        return new UserDTO(userDao.findById(id));
    }

    @GET
    @UnitOfWork
    @Path("email/{email}")
    public UserDTO getByEmail(@Auth Credentials credentials, @PathParam("email") String email) {
        return new UserDTO(userDao.findByUsername(email).get());
    }

    @PUT
    @UnitOfWork
    public UserDTO create(@Auth Credentials credentials, @Valid UserDTO user) {
        Optional<User> checkUser = userDao.findByUsername(user.getEmail());
        if (checkUser.isPresent()) {
            throw new RobeRuntimeException("E-mail", user.getEmail() + " already used by another user. Please use different e-mail.");
        }
        User entity = new User();
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setSurname(user.getSurname());
        entity.setActive(user.isActive());
        Role role = roleDao.findById(user.getRoleOid());
        if (role == null) {
            throw new RobeRuntimeException("Role", user.getEmail() + ExceptionMessages.CANT_BE_NULL.toString());
        }
        entity.setRole(role);
        entity.setPassword(Hashing.sha256().hashString(user.getName()).toString());

//        sendActivationMail(entity);

        return new UserDTO(userDao.create(entity));

    }

    private void sendActivationMail(User entity) {
        if (MailBundle.isIsActive()) {
            Ticket ticket = new Ticket();
            ticket.setType(Ticket.Type.ACTIVATE);
            DateTime expire = DateTime.now().plusDays(1);
            ticket.setExpirationDate(expire.toDate());
            ticket.setUser(entity);
            ticketDao.create(ticket);
            try {
                //TODO: Template support will be used.
                MailBundle.getMailSender().sendMessage("Activation", ticket.getOid(), null, null, entity.getUsername());
            } catch (MessagingException e) {
                new RobeRuntimeException("send failed", e);
            }
        }
    }

    @POST
    @UnitOfWork
    public UserDTO update(@Auth Credentials credentials, @Valid UserDTO user) {
        // Get and check user
        User entity = userDao.findById(user.getOid());
        if (entity == null) {
            throw new RobeRuntimeException("User", user.getOid() + ExceptionMessages.NOT_EXISTS.toString());
        }
        //Get role and firm and check for null
        Role role = roleDao.findById(user.getRoleOid());
        if (role == null) {
            throw new RobeRuntimeException("Role", user.getEmail() + ExceptionMessages.CANT_BE_NULL.toString());
        }
        entity.setOid(user.getOid());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setSurname(user.getSurname());
        entity.setActive(user.isActive());
        entity.setRole(role);

        entity = userDao.update(entity);
        userDao.flush();

        return new UserDTO(entity);

    }

    @DELETE
    @UnitOfWork
    public UserDTO delete(@Auth Credentials credentials, UserDTO user) {
        User entity = userDao.findById(user.getOid());
        userDao.delete(entity);
        return user;
    }

}
