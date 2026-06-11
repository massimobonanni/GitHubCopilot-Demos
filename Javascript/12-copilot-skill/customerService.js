'use strict';

class Customer {
  constructor(id, name, email, phone, createdAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone ?? null;
    this.createdAt = createdAt;
  }
}

class CustomerService {
  #store = new Map();
  #nextId = 1;

  async create(request) {
    if (!request?.name?.trim()) {
      throw new TypeError('Name is required.');
    }
    if (!request?.email?.includes('@')) {
      throw new TypeError('A valid email address is required.');
    }

    const customer = new Customer(
      this.#nextId++,
      request.name.trim(),
      request.email.trim().toLowerCase(),
      request.phone?.trim() ?? null,
      new Date()
    );

    this.#store.set(customer.id, customer);
    return customer;
  }

  async getById(customerId) {
    if (!Number.isInteger(customerId) || customerId <= 0) {
      throw new RangeError('customerId must be a positive integer.');
    }
    return this.#store.get(customerId) ?? null;
  }

  async search(query, page = 1, pageSize = 20) {
    if (!query?.trim()) {
      throw new TypeError('Search query cannot be empty.');
    }
    if (!Number.isInteger(page) || page < 1) {
      throw new RangeError('page must be a positive integer.');
    }
    if (!Number.isInteger(pageSize) || pageSize < 1 || pageSize > 100) {
      throw new RangeError('pageSize must be between 1 and 100.');
    }

    const lower = query.toLowerCase();
    const matches = [...this.#store.values()]
      .filter(c => c.name.toLowerCase().includes(lower) || c.email.toLowerCase().includes(lower))
      .sort((a, b) => a.name.localeCompare(b.name));

    return matches.slice((page - 1) * pageSize, page * pageSize);
  }

  async update(customerId, request) {
    if (!Number.isInteger(customerId) || customerId <= 0) {
      throw new RangeError('customerId must be a positive integer.');
    }
    const existing = this.#store.get(customerId);
    if (!existing) {
      throw new Error(`No customer with ID ${customerId} was found.`);
    }

    const updated = new Customer(
      existing.id,
      request.name?.trim() ?? existing.name,
      request.email?.trim().toLowerCase() ?? existing.email,
      request.phone !== undefined ? (request.phone?.trim() ?? null) : existing.phone,
      existing.createdAt
    );

    this.#store.set(customerId, updated);
    return updated;
  }

  async delete(customerId) {
    if (!Number.isInteger(customerId) || customerId <= 0) {
      throw new RangeError('customerId must be a positive integer.');
    }
    return this.#store.delete(customerId);
  }

  get count() {
    return this.#store.size;
  }
}

module.exports = { Customer, CustomerService };
