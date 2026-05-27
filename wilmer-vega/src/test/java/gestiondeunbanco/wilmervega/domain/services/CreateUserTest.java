package gestiondeunbanco.wilmervega.domain.services;

import gestiondeunbanco.wilmervega.domain.exceptions.DuplicateUsernameException;
import gestiondeunbanco.wilmervega.domain.exceptions.InvalidUserException;
import gestiondeunbanco.wilmervega.domain.exceptions.MissingRelatedClientException;
import gestiondeunbanco.wilmervega.domain.models.NaturalClient;
import gestiondeunbanco.wilmervega.domain.models.SystemRole;
import gestiondeunbanco.wilmervega.domain.models.User;
import gestiondeunbanco.wilmervega.domain.models.UserStatus;
import gestiondeunbanco.wilmervega.domain.ports.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock
    private UserPort userPort;

    private CreateUser createUser;

    @BeforeEach
    void setUp() {
        createUser = new CreateUser(userPort);
    }

    @Test
    void saveShouldThrowWhenUserIsNull() {
        assertThrows(InvalidUserException.class, () -> {
            createUser.save(null);
        });
    }

    @Test
    void saveShouldThrowWhenUsernameIsNull() {
        User user = new User();
        user.setUsername(null);

        assertThrows(InvalidUserException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldThrowWhenPasswordIsNull() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(null);

        assertThrows(InvalidUserException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldThrowWhenPasswordIsTooShort() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("12345");

        assertThrows(InvalidUserException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldThrowWhenUsernameAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setSystemRole(SystemRole.TELLER_EMPLOYEE);

        when(userPort.existsByUsername("testuser")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldThrowWhenSystemRoleIsNull() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setSystemRole(null);

        when(userPort.existsByUsername("testuser")).thenReturn(false);

        assertThrows(InvalidUserException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldThrowWhenRelatedClientRequiredButMissing() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setSystemRole(SystemRole.NATURAL_CLIENT);
        user.setRelatedClient(null);

        when(userPort.existsByUsername("testuser")).thenReturn(false);

        assertThrows(MissingRelatedClientException.class, () -> {
            createUser.save(user);
        });
    }

    @Test
    void saveShouldSuccessfullyCreateValidTellerUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setSystemRole(SystemRole.TELLER_EMPLOYEE);

        when(userPort.existsByUsername("testuser")).thenReturn(false);
        when(userPort.save(any(User.class))).thenReturn(user);

        User result = createUser.save(user);
        assert result != null;
        assert result.getUsername().equals("testuser");
        assert result.getUserStatus() == UserStatus.ACTIVE;
    }

    @Test
    void saveShouldSuccessfullyCreateValidClientUser() {
        User user = new User();
        user.setUsername("testclient");
        user.setPassword("password123");
        user.setSystemRole(SystemRole.NATURAL_CLIENT);

        NaturalClient client = new NaturalClient();
        client.setId(1L);
        user.setRelatedClient(client);

        when(userPort.existsByUsername("testclient")).thenReturn(false);
        when(userPort.save(any(User.class))).thenReturn(user);

        User result = createUser.save(user);
        assert result != null;
        assert result.getUsername().equals("testclient");
    }
}
