package com.flamexandr.phonebook;

import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.repository.ContactRepository;
import com.flamexandr.phonebook.util.SchemaInitializer;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        SchemaInitializer.executeSchema();

        ContactRepository contactRepository = new ContactRepository();

        try {
            Contact contact = new Contact(
                    0,
                    "Петров",
                    "Петр"
            );

            contactRepository.addContact(contact);

            List<Contact> contacts = contactRepository.getAllContacts();
            contacts.forEach(c -> System.out.println(c.getFirstName() + " " + c.getLastName()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}