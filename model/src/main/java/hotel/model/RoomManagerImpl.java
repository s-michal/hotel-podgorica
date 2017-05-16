package hotel.model;

import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.RoomHasReservationException;
import hotel.model.exceptions.DuplicateRoomNumberException;
import hotel.model.exceptions.RoomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomManagerImpl implements RoomManager
{
    private DataSource dataSource;
    private static Logger logger = LoggerFactory.getLogger(RoomManagerImpl.class);
    private Persister<Room> persister;
    private Hydrator<Room> hydrator;

    public RoomManagerImpl(DataSource dataSource, Persister<Room> persister, Hydrator<Room> hydrator)
    {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(persister);
        Objects.requireNonNull(hydrator);

        this.dataSource = dataSource;
        this.persister = persister;
        this.hydrator = hydrator;
        this.hydrator = new Hydrator<>(Room.class);
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

    public Room find(Long id) throws RoomNotFoundException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    getQuery() + " WHERE \"id\" = ?");

            statement.setLong(1, id);

            return executeQueryForSingleRow(statement);
        } catch (SQLException e) {
            logger.error("Find was unsuccessful", e);
        } catch(ApplicationException e) {
            throw new RuntimeException(e);
        }

        throw new RoomNotFoundException("There is no room with id " + id);
    }

    public List<Room> findAll()
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getQuery());

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            logger.error("Find all was unsuccessful", e);
        } catch(ApplicationException e) {
            throw new RuntimeException();
        }

        return new ArrayList<>();
    }

    public List<Room> findAllByFloor(int floor) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getQuery() + " WHERE \"floor\" = ?");

            statement.setInt(1, floor);

            return executeQueryForMultipleRows(statement);
        } catch (SQLException e) {
            logger.error("Find all was unsuccessful", e);
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
            logger.error(message, e);
            throw new ApplicationException(message, e);
        }
    }

    public void delete(Room room) throws RoomHasReservationException
    {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getId());

        if(hasReservations(room.getId())) {
            throw new RoomHasReservationException("Room with reservations can't be removed");
        }

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM \"room\" WHERE \"id\" = ?");

            statement.setLong(1, room.getId());

            statement.execute();
        } catch (SQLException e) {
            logger.error("User couldn't be deleted", e);
        }
    }


    private boolean hasReservations(long id)
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM \"reservation\" WHERE \"room_id\" = ?");

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return result.getInt(1) != 0;
            }
        } catch (SQLException e) {
            logger.error("Room couldn't be deleted", e);
        }

        return false;
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

        while (rs.next()) {
            list.add(hydrator.hydrate(rs));
        }

        return list;
    }

    private void checkDuplicateNumber(long number) throws DuplicateRoomNumberException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT COUNT(*) AS \"count\" FROM \"room\" WHERE \"number\" = ?"
            );
            statement.setLong(1, number);
            statement.execute();
            ResultSet result = statement.getResultSet();
            result.next();
            if (result.getInt("count") > 0) {
                throw new DuplicateRoomNumberException();
            }
        } catch (SQLException e) {
            // Ssh... it's ok
        }
    }

    private String getQuery()
    {
        return "SELECT * FROM \"room\"";
    }

}
