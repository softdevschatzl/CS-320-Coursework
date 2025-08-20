package test;
import contact.Contact;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContactTest {

	@Test
    void testContactConstructorValidData() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertEquals("ID1234567", contact.getContactId());
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main St", contact.getAddress());
    }
	
	@Test
    void testContactConstructorInvalidContactId() {
        assertThrows(IllegalArgumentException.class, () -> new Contact(null, "John", "Doe", "1234567890", "123 Main St"));
    }

    @Test
    void testContactConstructorLongContactId() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("ID1234567890", "John", "Doe", "1234567890", "123 Main St"));
    }

    @Test
    void testSetFirstNameNull() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(null));
    }

    @Test
    void testSetFirstNameTooLong() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setFirstName("Johnathan"));
    }

    @Test
    void testSetLastNameNull() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setLastName(null));
    }

    @Test
    void testSetLastNameTooLong() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setLastName("DoeJohnson"));
    }

    @Test
    void testSetPhoneNull() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setPhone(null));
    }

    @Test
    void testSetPhoneInvalidLength() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setPhone("123456789"));
    }

    @Test
    void testSetAddressNull() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setAddress(null));
    }

    @Test
    void testSetAddressTooLong() {
        Contact contact = new Contact("ID1234567", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contact.setAddress("123 Main Street, Springfield, Anytown, USA"));
    }
}
