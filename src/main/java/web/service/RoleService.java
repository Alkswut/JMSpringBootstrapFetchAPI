package web.service;

import web.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role getRoleById(Long id);

    List<Role> getRolesList();

    Set<Role> getSetRoles(List<String> roles);
}
