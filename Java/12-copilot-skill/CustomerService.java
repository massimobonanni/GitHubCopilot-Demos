import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Demo 12 — Copilot Skill
 * =========================
 * A Copilot Skill is a reusable, templated action for documentation and
 * code generation. Unlike Agents (interactive chat modes) or Prompt Files
 * (invokable templates), a Skill is designed to help you perform a specific
 * type of work — in this case, generating standardized Google-style docstrings
 * for your API.
 *
 * How it works:
 *   • Create a `<name>.skill.md` file with YAML frontmatter (name, description)
 *   • The markdown contains instructions for documentation generation
 *   • Users invoke the skill by asking in Chat: "Use the document-api skill"
 *     or "Add documentation following team standards"
 *   • The skill appears in the Chat mode picker and can also be triggered
 *     via slash commands (`/document-api`)
 *
 * This file contains a CustomerService API that intentionally lacks
 * comprehensive JavaDoc comments. Use this to practice the skill.
 *
 * Exercises:
 *   1. Copy SKILL.md to .vscode/skills/document-api.skill.md
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Open Copilot Chat → ask: "Use the document-api skill to document this file"
 *      → Copilot generates JavaDoc comments following the template
 *   4. Ask: "Are all public methods documented?"
 *   5. Select the skill in the mode picker and ask it questions about the API
 *   6. Bonus — edit SKILL.md to add additional rules
 *      (e.g., "include @since version tags"), then re-run the skill
 */

/**
 * Represents a customer record with immutable data.
 */
final class Customer {
    private final int id;
    private final String name;
    private final String email;
    private final String phone;
    private final LocalDateTime createdAt;

    public Customer(int id, String name, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

/**
 * Request object for creating a new customer.
 */
class CreateCustomerRequest {
    private String name;
    private String email;
    private String phone;

    public CreateCustomerRequest(String name, String email) {
        this(name, email, null);
    }

    public CreateCustomerRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}

/**
 * Request object for updating an existing customer.
 */
class UpdateCustomerRequest {
    private String name;
    private String email;
    private String phone;

    public UpdateCustomerRequest() {
    }

    public UpdateCustomerRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

/**
 * Service for managing customer data and operations.
 */
public class CustomerService {

    private Map<Integer, Customer> store;
    private int nextId;

    /**
     * Constructs a new CustomerService with an empty customer store.
     */
    public CustomerService() {
        this.store = new HashMap<>();
        this.nextId = 1;
    }

    /**
     * Create a new customer with the provided information.
     *
     * @param request the CreateCustomerRequest containing name, email, and optional phone
     * @return the newly created Customer
     * @throws IllegalArgumentException if name is empty or email is invalid
     */
    public Customer create(CreateCustomerRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new IllegalArgumentException("A valid email address is required.");
        }

        Customer customer = new Customer(
            nextId,
            request.getName().trim(),
            request.getEmail().trim().toLowerCase(),
            request.getPhone() != null ? request.getPhone().trim() : null,
            LocalDateTime.now(ZoneId.of("UTC"))
        );

        nextId++;
        store.put(customer.getId(), customer);
        return customer;
    }

    /**
     * Retrieve a customer by their unique identifier.
     *
     * @param customerId the unique ID of the customer. Must be a positive integer.
     * @return the Customer with the given ID, or null if not found
     * @throws IllegalArgumentException if customerId is not a positive integer
     */
    public Customer getById(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer_id must be a positive integer.");
        }
        return store.get(customerId);
    }

    /**
     * Search for customers by name or email.
     *
     * @param query the search query to match against customer names and emails
     * @param page the page number for pagination (must be at least 1)
     * @param pageSize the number of results per page (must be between 1 and 100)
     * @return a list of matching customers sorted by name
     * @throws IllegalArgumentException if query is empty, page is less than 1, or pageSize is out of range
     */
    public List<Customer> search(String query, int page, int pageSize) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty.");
        }
        if (page < 1) {
            throw new IllegalArgumentException("page must be at least 1.");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException("page_size must be between 1 and 100.");
        }

        String lowerQuery = query.toLowerCase();
        List<Customer> matches = store.values().stream()
            .filter(c -> c.getName().toLowerCase().contains(lowerQuery) ||
                        c.getEmail().toLowerCase().contains(lowerQuery))
            .sorted(Comparator.comparing(Customer::getName))
            .collect(Collectors.toList());

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, matches.size());

        return matches.subList(start, end);
    }

    /**
     * Search for customers with default pagination (page 1, page size 20).
     *
     * @param query the search query to match against customer names and emails
     * @return a list of matching customers sorted by name
     * @throws IllegalArgumentException if query is empty
     */
    public List<Customer> search(String query) {
        return search(query, 1, 20);
    }

    /**
     * Update an existing customer's information.
     *
     * @param customerId the unique ID of the customer to update
     * @param request the UpdateCustomerRequest containing fields to update
     * @return the updated Customer
     * @throws IllegalArgumentException if customerId is not a positive integer
     * @throws NoSuchElementException if no customer with the given ID exists
     */
    public Customer update(int customerId, UpdateCustomerRequest request) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer_id must be a positive integer.");
        }

        Customer existing = store.get(customerId);
        if (existing == null) {
            throw new NoSuchElementException(
                String.format("No customer with ID %d was found.", customerId)
            );
        }

        String updatedName = request.getName() != null && !request.getName().isEmpty()
            ? request.getName().trim()
            : existing.getName();

        String updatedEmail = request.getEmail() != null && !request.getEmail().isEmpty()
            ? request.getEmail().trim().toLowerCase()
            : existing.getEmail();

        String updatedPhone = request.getPhone() != null && !request.getPhone().isEmpty()
            ? request.getPhone().trim()
            : existing.getPhone();

        Customer updated = new Customer(
            existing.getId(),
            updatedName,
            updatedEmail,
            updatedPhone,
            existing.getCreatedAt()
        );

        store.put(customerId, updated);
        return updated;
    }

    /**
     * Delete a customer by their unique identifier.
     *
     * @param customerId the unique ID of the customer to delete
     * @return true if the customer was deleted, false if the customer was not found
     * @throws IllegalArgumentException if customerId is not a positive integer
     */
    public boolean delete(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer_id must be a positive integer.");
        }

        return store.remove(customerId) != null;
    }

    /**
     * Get the total number of customers in the store.
     *
     * @return the count of customers
     */
    public int getCount() {
        return store.size();
    }
}
