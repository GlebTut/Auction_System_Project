package com.DAO;

import com.Database.DBConnector;
import com.Entities.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on Item records.
 */
public class ItemDAO {

    /**
     * Creates a new item record in the database.
     *
     * <p>This method inserts a new record into the item table with details such as itemName,
     * itemDescription, itemStartingPrice, and itemImage. It retrieves the generated itemID
     * and sets it in the Item object.</p>
     *
     * @param item the Item object containing item details
     * @return the generated itemID
     * @throws SQLException if a database access error occurs during insertion
     */
    public static int createItem(Item item) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet generatedKeys = null;
        int itemID = 0;

        String sql = "INSERT INTO item (itemName, itemDescription, itemStartingPrice, itemImage) VALUES (?, ?, ?, ?)";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstat.setString(1, item.getItemName());
            pstat.setString(2, item.getItemDescription());
            pstat.setDouble(3, item.getItemStartingPrice());
            pstat.setBytes(4, item.getItemImage());

            int i = pstat.executeUpdate();
            System.out.println(i + " Item successfully added to the table");

            if (i > 0) {
                generatedKeys = pstat.getGeneratedKeys();
                if (generatedKeys.next()) {
                    itemID = generatedKeys.getInt(1);
                    item.setItemID(itemID);
                    System.out.println("Generated Item ID: " + itemID);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return itemID;
    }

    /**
     * Updates an existing item record in the database.
     *
     * <p>This method updates the item table with new details for the specified itemID.</p>
     *
     * @param item the Item object containing updated item details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateItem(Item item) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "UPDATE item SET itemName = ?, itemDescription = ?, itemStartingPrice = ?, itemImage = ? WHERE itemID = ?";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);

            pstat.setString(1, item.getItemName());
            pstat.setString(2, item.getItemDescription());
            pstat.setDouble(3, item.getItemStartingPrice());
            pstat.setBytes(4, item.getItemImage());
            pstat.setInt(5, item.getItemID());

            int i = pstat.executeUpdate();
            System.out.println(i + " Item successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves an item by its ID.
     *
     * <p>This method executes a SQL query to fetch an item's details using the provided itemID
     * and returns an Item object populated with those details.</p>
     *
     * @param itemID the unique identifier of the item
     * @return the Item object if found; null otherwise
     * @throws SQLException if a database access error occurs during the query
     */
    public static Item getItemByID(int itemID) throws SQLException {
        Item item = null;
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM item WHERE itemID = ?";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, itemID);

            rs = pstat.executeQuery();
            while (rs.next()) {
                item = new Item();
                item.setItemID(rs.getInt("itemID"));
                item.setItemName(rs.getString("itemName"));
                item.setItemDescription(rs.getString("itemDescription"));
                item.setItemStartingPrice(rs.getDouble("itemStartingPrice"));
                item.setItemImage(rs.getBytes("itemImage"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return item;
    }

    /**
     * Deletes an item record from the database by its ID.
     *
     * @param itemID the unique identifier of the item to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteItem(int itemID) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "DELETE FROM item WHERE itemID = ?";
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, itemID);

            int i = pstat.executeUpdate();
            System.out.println(i + " Item successfully deleted from the table.");
        } catch (SQLException e) {
            System.err.println("Error deleting item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves all item records from the database.
     *
     * <p>This method executes a SQL query to fetch all items from the item table and returns
     * them as a list of Item objects.</p>
     *
     * @return a List of Item objects containing details of all items
     * @throws SQLException if a database access error occurs during the query
     */
    public List<Item> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM item";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setItemID(rs.getInt("itemID"));
                item.setItemName(rs.getString("itemName"));
                item.setItemDescription(rs.getString("itemDescription"));
                item.setItemStartingPrice(rs.getDouble("itemStartingPrice"));
                item.setItemImage(rs.getBytes("itemImage"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return items;
    }
}