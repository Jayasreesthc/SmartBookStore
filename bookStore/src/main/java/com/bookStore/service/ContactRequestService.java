package com.bookStore.service;

import com.bookStore.entity.ContactRequest;
import com.bookStore.repository.ContactRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactRequestService
{
        @Autowired
        private ContactRequestRepository repo;

        public void save(ContactRequest request) {
            repo.save(request);
        }

        public List<ContactRequest> getAllRequests() {
            return repo.findAll();
        }


}
