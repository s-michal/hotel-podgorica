package hotel;

import hotel.database.Hydrator;
import hotel.database.Persister;
import hotel.exceptions.DuplicateRoomNumberException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomManagerImpl implements RoomManager
{
    private DataSource dataSource;
    private Logger logger;
    private Persister<Room> persister;
    private Hydrator<Room> hydrator;

    public RoomManagerImpl(DataSource dataSource, Persister<Room> persister, Hydrator<Room> hydrator, Logger logger)
    {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(persister);
        Objects.requireNonNull(hydrator);

        this.dataSource = dataSource;
        this.logger = logger;
        this.persister = persister;
        this.hydrator = hydrator;
        this.hydrator = new Hydrator<>(Room.class, logger);
    }

    public void create(Room room) throws ApplicationException, DuplicateRoomNumberException
    {
        Objects.requireNonNull(room);
        if (room.getId() != null) {
            throw new IllegalArgumentException("Room already stored");
        }

        checkDuplicateNumber(room.getNumber());

        long id = persister.insert(room);
        room.setId(id);
    }

    public void update(Room room) throws ApplicationException
    {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getId());

        persister.update(room, room.getId());
    }

    public Room find(Long id) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    getQuery() + " WHERE \"id\" = ?");

            statement.setLong(1, id);

            return executeQueryForSingleRow(statement);
        } catch (SQLException e) {
            log(e, "Find was unsuccessful");
        }

        return null;
    }

    public List<Room> findAll() throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getQuery());

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            log(e, "Find all was unsuccessful");
        }

        return null;
    }

    public List<Room> findAllByFloor(int floor) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getQuery() + " WHERE \"floor\" = ?");

            statement.setInt(1, floor);
            
            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            log(e, "Find all was unsuccessful");
        }

        return null;
    }

    public List<Room> findAllInPrice(BigDecimal price) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getQuery() + " WHERE \"price_per_day\" <= ?");

            statement.setBigDecimal(1, price);
            
            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            String message = "Find all was unsuccessful";
            log(e, message);
            throw new ApplicationException(message, e);
        }
    }

    public void delete(Room room)
    {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getId());

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM \"room\" WHERE \"id\" = ?");

            statement.setLong(1, room.getId());

            statement.execute();
        } catch (SQLException e) {
            log(e, "User couldn't be deleted");
        }
    }

    private Room executeQueryForSingleRow(PreparedStatement statement) throws SQLException, ApplicationException
    {
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return hydrator.hydrate(rs);
        }
        return null;
    }

    private List<Room> executeQueryForMultipleRows(PreparedStatement statement) throws SQLException, ApplicationException
    {
        ResultSet rs = statement.executeQuery();

        List<Room> list = new ArrayList<>();

        while(rs.next()) {
            list.add(hydrator.hydrate(rs));
        }

        return list;
    }

    private void log(Throwable e, String message)
    {
        if(logger == null) {
            return;
        }
        logger.log(Level.SEVERE, message, e);
    }

    private void checkDuplicateNumber(long number) throws DuplicateRoomNumberException
    {
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT COUNT(*) AS \"count\" FROM \"room\" WHERE \"number\" = ?"
            );
            statement.setLong(1, number);
            statement.execute();
            ResultSet result = statement.getResultSet();
            result.next();
            if(result.getInt("count") > 0) {
                throw new DuplicateRoomNumberException();
            }
        } catch(SQLException e) {
            // Ssh... it's ok
        }
    }

    private String getQuery()
    {
        return "SELECT * FROM \"room\"";
    }

}
