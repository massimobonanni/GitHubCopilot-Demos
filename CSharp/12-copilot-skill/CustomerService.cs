using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Demo.Services;

public record Customer(int Id, string Name, string Email, string? Phone, DateTime CreatedAt);

public record CreateCustomerRequest(string Name, string Email, string? Phone = null);

public record UpdateCustomerRequest(string? Name = null, string? Email = null, string? Phone = null);

public class CustomerService
{
    private readonly Dictionary<int, Customer> _store = new();
    private int _nextId = 1;

    public async Task<Customer> CreateAsync(
        CreateCustomerRequest request,
        CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(request);
        if (string.IsNullOrWhiteSpace(request.Name))
            throw new ArgumentException("Name cannot be empty.", nameof(request));
        if (string.IsNullOrWhiteSpace(request.Email) || !request.Email.Contains('@'))
            throw new ArgumentException("A valid email address is required.", nameof(request));

        await Task.Delay(0, cancellationToken);

        var customer = new Customer(
            _nextId++,
            request.Name.Trim(),
            request.Email.Trim().ToLowerInvariant(),
            request.Phone?.Trim(),
            DateTime.UtcNow);

        _store[customer.Id] = customer;
        return customer;
    }

    public async Task<Customer?> GetByIdAsync(
        int customerId,
        CancellationToken cancellationToken = default)
    {
        if (customerId <= 0)
            throw new ArgumentOutOfRangeException(nameof(customerId), "Customer ID must be a positive integer.");

        await Task.Delay(0, cancellationToken);
        _store.TryGetValue(customerId, out var customer);
        return customer;
    }

    public async Task<IReadOnlyList<Customer>> SearchAsync(
        string query,
        int page = 1,
        int pageSize = 20,
        CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(query))
            throw new ArgumentException("Search query cannot be empty.", nameof(query));
        if (page < 1)
            throw new ArgumentOutOfRangeException(nameof(page), "Page number must be at least 1.");
        if (pageSize is < 1 or > 100)
            throw new ArgumentOutOfRangeException(nameof(pageSize), "Page size must be between 1 and 100.");

        await Task.Delay(0, cancellationToken);

        return _store.Values
            .Where(c => c.Name.Contains(query, StringComparison.OrdinalIgnoreCase)
                     || c.Email.Contains(query, StringComparison.OrdinalIgnoreCase))
            .OrderBy(c => c.Name)
            .Skip((page - 1) * pageSize)
            .Take(pageSize)
            .ToList()
            .AsReadOnly();
    }

    public async Task<Customer> UpdateAsync(
        int customerId,
        UpdateCustomerRequest request,
        CancellationToken cancellationToken = default)
    {
        if (customerId <= 0)
            throw new ArgumentOutOfRangeException(nameof(customerId));
        ArgumentNullException.ThrowIfNull(request);

        await Task.Delay(0, cancellationToken);

        if (!_store.TryGetValue(customerId, out var existing))
            throw new KeyNotFoundException($"No customer with ID {customerId} was found.");

        var updated = existing with
        {
            Name  = string.IsNullOrWhiteSpace(request.Name)  ? existing.Name  : request.Name.Trim(),
            Email = string.IsNullOrWhiteSpace(request.Email) ? existing.Email : request.Email.Trim().ToLowerInvariant(),
            Phone = request.Phone is null ? existing.Phone : request.Phone.Trim()
        };

        _store[customerId] = updated;
        return updated;
    }

    public async Task<bool> DeleteAsync(int customerId, CancellationToken cancellationToken = default)
    {
        if (customerId <= 0)
            throw new ArgumentOutOfRangeException(nameof(customerId));

        await Task.Delay(0, cancellationToken);
        return _store.Remove(customerId);
    }

    public int Count => _store.Count;
}
