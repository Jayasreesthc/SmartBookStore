package com.bookStore.service;

import com.bookStore.entity.Client;
import com.bookStore.entity.User;
import com.bookStore.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService {
    @Autowired
    private ClientRepository crepo;

    public Client validateUser(String username, String password) {
        Client user = crepo.findByUsernameAndPassword(username, password);
        if (user != null) {
            //user.setLoginTime(LocalDateTime.now());
            return crepo.save(user);

        }
        else {
            return null;
        }
    }

    public Client save(Client client) {
        return crepo.save(client);
    }
}

