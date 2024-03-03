package test;
import contact.ContactService;

import contact.Contact;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class ContactServiceTest {

	private ContactService contactService;
	private Contact contact1;
	private Contact contact2;
	
	@Before
	public void setUp() {
		contactService = new ContactService();
		
        contact1 = new Contact("ID1", "John", "Doe", "1234567890", "123 First St");
        contact2 = new Contact("ID2", "Jane", "Smith", "0987654321", "456 Second St");
	}
	
	@Test
    public void testAddContact() {
        contactService.addContact(contact1);
        assertEquals(contact1, contactService.getContact("ID1"));
    }

	@Test
	public void testAddDuplicateContact() {
	    Contact contact = new Contact("ID1", "John", "Doe", "1234567890", "123 First St");
	    contactService.addContact(contact);
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	        contactService.addContact(contact);
	    });
	    assertEquals("Contact already exists or is invalid", exception.getMessage());
	}

    @Test
    public void testDeleteContact() {
        contactService.addContact(contact2);
        contactService.deleteContact("ID2");
        assertNull(contactService.getContact("ID2"));
    }

    @Test
    public void testUpdateContact() {
        contactService.addContact(contact1);
        contactService.updateContact("ID1", "John", "Doe Jr.", "1234567890", "123 First St");
        Contact updatedContact = contactService.getContact("ID1");
        assertNotNull(updatedContact);
        assertEquals("Doe Jr.", updatedContact.getLastName());
    }

    @Test
    public void testDeleteThenUpdateContact() {
        Contact contact = new Contact("ID2", "Jane", "Smith", "0987654321", "456 Second St");
        contactService.addContact(contact);
        contactService.deleteContact("ID2");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateContact("ID2", "Jane", "Doe", "1231231234", "789 Third St");
        });
        assertEquals("Contact does not exist", exception.getMessage());
    }
}
