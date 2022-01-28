package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getById(id);
    }

    @Transactional
    @Override
    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public Set<Role> getSetRoles(List<String> roles) {
        Set<Role> setRoles = new HashSet<>();
        for (String id : roles) {
            setRoles.add(getRoleById(Long.parseLong(id)));
        }
        return setRoles;
    }
}
