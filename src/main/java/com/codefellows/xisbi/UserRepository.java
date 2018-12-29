package com.codefellows.xisbi;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<XisbiUser, Long> {
    public XisbiUser findByUsername(String username);
}
