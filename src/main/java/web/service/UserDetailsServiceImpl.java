package web.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.dto.UserEdited;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(UserEdited userEdited) {
        return userRepository.save(toUser(userEdited));
    }

    @Transactional
    @Override
    public void registerUser(User user) {
        User userForAdd = new User(user);
        userRepository.save(userForAdd);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void editUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User editUser(UserEdited userEdited) {
        return userRepository.save(toUser(userEdited));
    }

    private User toUser(UserEdited userEdited) {
        User user = new User();
        user.setId(userEdited.getId());
        user.setUsername(userEdited.getUsername());
        user.setPassword(userEdited.getPassword());
        user.setFirstName(userEdited.getFirstName());
        user.setLastName(userEdited.getLastName());
        user.setAge(userEdited.getAge());
        Set<String> listRoles = userEdited.getRoles();
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles.stream().filter(role -> listRoles.contains(role.getRole())).collect(Collectors.toSet()));
        return user;
    }

    @Transactional
    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("This user - '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
