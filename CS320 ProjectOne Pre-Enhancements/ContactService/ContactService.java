// Importing HashMap to serve as our in-memory data structure. (Who doesn't love HashMaps?)
package contact;

import java.util.HashMap;
import java.util.Map;

public class ContactService {
    private final Map<String, Contact> contacts = new HashMap<>();

    public void addContact(Contact contact) {
        if (contact == null || contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Contact already exists or is invalid");
        }
        contacts.put(contact.getContactId(), contact);
    }

    public void deleteContact(String contactId) {
        contacts.remove(contactId);
    }

    public void updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        Contact contact = contacts.get(contactId);
        if (contact != null) {
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setPhone(phone);
            contact.setAddress(address);
        } else {
            throw new IllegalArgumentException("Contact does not exist");
        }
    }

    public Contact getContactByName(String name) {
        for (Contact contact : contacts.values()) {
            if (contact.getFirstName().equals(name) || contact.getLastName().equals(name)) {
                return contact;
            }
        }
        throw new IllegalArgumentException("Contact does not exist");
    }

    // Added a helper function for testing.
    public Contact getContact(String contactId) {
    	return contacts.get(contactId);
    }
}
