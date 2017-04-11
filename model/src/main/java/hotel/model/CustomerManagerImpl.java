package hotel.model;

import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerManagerImpl implements CustomerManager
{

    private DataSource dataSource;

    private static Logger logger = LoggerFactory.getLogger(CustomerManager.class);

    private Persister<Customer> persister;

    private Hydrator<Customer> hydrator;

    public CustomerManagerImpl(DataSource dataSource, Persister<Customer> persister, Hydrator<Customer> hydrator)
    {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(persister);
        Objects.requireNonNull(hydrator);

        this.dataSource = dataSource;
        this.persister = persister;
        this.hydrator = hydrator;
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

    public Customer find(Long id) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\" WHERE \"id\" = ?");

            statement.setLong(1, id);

            return executeQueryForSingleRow(statement);
        } catch (SQLException e) {
            logger.error("Find was unsuccessful", e);
        }

        return null;
    }

    public List<Customer> findByName(String name) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\" WHERE \"name\" = ?");

            statement.setString(1, name);

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            logger.error("Find by name was unsuccessful", e);
        }

        return null;
    }

    public List<Customer> findAll() throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT \"id\", \"name\", \"address\", \"birth_date\" FROM \"customer\"");

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            logger.error("Find all was unsuccessful", e);
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
            logger.error("User couldn't be deleted", e);
        }
    }

    private Customer executeQueryForSingleRow(PreparedStatement statement) throws SQLException, ApplicationException
    {
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return hydrator.hydrate(rs);
        }
        return null;
    }

    private List<Customer> executeQueryForMultipleRows(PreparedStatement statement) throws SQLException, ApplicationException
    {
        ResultSet rs = statement.executeQuery();

        List<Customer> list = new ArrayList<>();

        while(rs.next()) {
            list.add(hydrator.hydrate(rs));
        }

        return list;
    }

}
