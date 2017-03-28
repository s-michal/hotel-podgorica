package hotel;

import hotel.database.Persister;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerManagerImpl implements CustomerManager
{

    private DataSource dataSource;

    private Logger logger;

    private Persister<Customer> persister;

    public CustomerManagerImpl(DataSource dataSource, Logger logger)
    {
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
        this.logger = logger;
        this.persister = new Persister<>("customer", dataSource, logger);
    }

    public void create(Customer customer) throws ApplicationException
    {
        Objects.requireNonNull(customer);

        if(customer.getId() != null) {
            throw new IllegalArgumentException("User already stored");
        }

        long id = persister.insert(customer);
        customer.setId(id);
    }

    public void update(Customer customer) throws ApplicationException
    {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(customer.getId());
        persister.update(customer, customer.getId());
    }

    public Customer find(Long id)
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\" WHERE \"id\" = ?");

            statement.setLong(1, id);

            return executeQueryForSingleRow(statement);
        } catch (SQLException e) {
            log(e, "Find was unsuccessful");
        }

        return null;
    }

    public List<Customer> findByName(String name)
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\" WHERE \"name\" = ?");

            statement.setString(1, name);

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            log(e, "Find by name was unsuccessful");
        }

        return null;
    }

    public List<Customer> findAll()
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\"");

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            log(e, "Find all was unsuccessful");
        }

        return null;
    }

    public void delete(Customer customer)
    {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(customer.getId());

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM \"customer\" WHERE \"id\" = ?");

            statement.setLong(1, customer.getId());

            statement.execute();
        } catch (SQLException e) {
            log(e, "User couldn't be deleted");
        }
    }

    private Customer rowToCustomer(ResultSet result) throws SQLException
    {
        Customer customer = new Customer(
                result.getString("name"),
                result.getString("address"),
                toLocalDate(result.getDate("birth_date"))
        );
        customer.setId(result.getLong("id"));

        return customer;
    }

    private Customer executeQueryForSingleRow(PreparedStatement statement) throws SQLException
    {
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rowToCustomer(rs);
        }
        return null;
    }

    private List<Customer> executeQueryForMultipleRows(PreparedStatement statement) throws SQLException
    {
        ResultSet rs = statement.executeQuery();

        List<Customer> list = new ArrayList<>();

        while(rs.next()) {
            list.add(rowToCustomer(rs));
        }

        return list;
    }

    private LocalDate toLocalDate(Date date)
    {
        return date == null ? null : date.toLocalDate();
    }

    private void log(Throwable e, String message)
    {
        if(logger == null) {
            return;
        }
        logger.log(Level.SEVERE, message, e);
    }
}
