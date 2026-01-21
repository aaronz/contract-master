package com.contract.master.identity.application;

import com.contract.master.identity.domain.model.User;
import com.contract.master.identity.domain.model.UserRoleRel;
import com.contract.master.identity.domain.repository.UserRepository;
import com.contract.master.identity.domain.repository.UserRoleRelRepository;
import com.contract.master.identity.domain.repository.RoleRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRelRepository userRoleRelRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void assignRoles(String userId, List<String> roleIds) {
        userRoleRelRepository.deleteByUserId(userId);
        
        for (String roleId : roleIds) {
            UserRoleRel rel = new UserRoleRel();
            rel.setUserId(userId);
            rel.setRoleId(roleId);
            userRoleRelRepository.save(rel);
        }
    }

    public List<String> getUserRoleIds(String userId) {
        return userRoleRelRepository.findByUserId(userId).stream()
                .map(UserRoleRel::getRoleId)
                .collect(Collectors.toList());
    }
}
