package hei.school.demo.repository.mapper;

import hei.school.demo.entity.AppUser;
import hei.school.demo.repository.model.JAppUser;

public class AppUserMapper {
    
    public AppUser toDomain(JAppUser toMap) {
        AppUser user = new AppUser();
        user.setEmail(toMap.getEmail());
        user.setId(toMap.getId());
        user.setPasswordHash(toMap.getPasswordHash());
        return user;
    }

    public JAppUser toJpa(AppUser toMap){
        JAppUser appUser = new JAppUser();
        appUser.setEmail(toMap.getEmail());
        appUser.setId(toMap.getId());
        appUser.setPasswordHash(toMap.getPasswordHash());
        return appUser;
    }
    
}
