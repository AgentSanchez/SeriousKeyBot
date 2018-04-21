package net.adamsanchez.discord.seriouskeybot.data;

import net.adamsanchez.discord.seriouskeybot.util.CC;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.adamsanchez.discord.seriouskeybot.util.U;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Created by adam_ on 01/22/17.
 */
public class Database {
    private String host = "localhost";
    private String port = "3306";
    private String username = "root";
    private String password = "ohokay";
    private String dbname = "KeyBot";
    private String dbType = "mysql";
    private String table_prefix = "KB_";
    private String userTableName = "players";
    private String url = "jdbc:mysql://test.com:3306/testdata?useSSL=false";
    private String timezoneFix = "&useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    Properties props;
    HikariConfig config = new HikariConfig();
    HikariDataSource ds;




    public Database(){
        props = loadProperties(new Properties());
        this.host = props.getProperty("hostname","127.0.0.1");
        this.port = props.getProperty("port", "3306");
        this.dbname = props.getProperty("databaseName", "KeyBot");
        this.table_prefix = props.getProperty("tablePrefix", "KB_");
        this.username = props.getProperty("username", "root");
        this.password = props.getProperty("password", password);
        userTableName = table_prefix + "transactions";

        url = "jdbc:mysql://"+ host + ":" + port + "/" + dbname + "?useSSL=false";

        config.setJdbcUrl(url + timezoneFix);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(20);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(1770000);
        config.setPoolName("KeyBot-SQL");

        //Instantiate Pool
        ds = new HikariDataSource(config);
        U.info(CC.BLUE_BRIGHT + "Ready for connections");
    }

    private Properties loadProperties(Properties props){

        if (Files.notExists(Paths.get("database.conf"))) makeProperties();
        InputStream input = null;
        try {
            input = new FileInputStream("database.conf");
            props.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try{
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  props;
    }

    private void makeProperties(){
        Properties props = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("database.conf");

            props.setProperty("hostname", "127.0.0.1");
            props.setProperty("port", "3306");
            props.setProperty("databaseName", "KeyBot");
            props.setProperty("tablePrefix", "KB_");
            props.setProperty("username", "root");
            props.setProperty("password", password);

            props.store(output, null);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    U.error(CC.RED + "Could close file handle!");
                    e.printStackTrace();
                }
            }
        }


    }


    public Database(String url, String username, String password){
        config.setJdbcUrl(url + timezoneFix);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(20);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(1770000);
        config.setPoolName("KeyBot-SQL");

        ds = new HikariDataSource(config);
        U.info("Ready for connections");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////


    public void shutdown(){
        ds.close();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        return ds.getConnection();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public Statement statement(){
        Statement statement = null;
        try{
            Connection con = getConnection();
            statement = con.createStatement();

        } catch (SQLException e) {
            U.error("Unable to connect --- ", e);
        }
        return statement;
    }

    public PreparedStatement preparedStatement(String string){
        PreparedStatement statement = null;
        try{
            statement = getConnection().prepareStatement(string);

        } catch (SQLException e) {
            U.error("Error in DB Connection");
        }
        return statement;
    }

    private ResultSet genericQuery(String query){
        ResultSet results = null;
        try {
            results = statement().executeQuery(query);
        } catch (SQLException e) {
            U.error("Error running query!");
                    e.printStackTrace();
        }
        return results;
    }


    public ResultSet genericSelectQuery(String table, String field, String value){
        String initial = "SELECT * FROM %s WHERE %s='%s'";
        U.debug("+ Running query \"" + String.format(initial,table,field,value) + "\"" );
        ResultSet results = genericQuery(String.format(initial,table,field,value));
        return  results;
    }



    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public TransactionRecord getTransaction(String playerID){
        ResultSet results = genericSelectQuery(userTableName, "playerID", playerID);
        try {
            if(results.first()){
                String steamKey = results.getString("steamKey");
                results.getStatement().getConnection().close();
                return new TransactionRecord(playerID, steamKey);
            }
        } catch (SQLException e) {
            U.error("Trouble getting information from the database");
            e.printStackTrace();
        }
        return null;
    }

    public void postTransaction(TransactionRecord tr){
        U.info(CC.BLUE_BRIGHT + "Posting Transaction For: " + CC.YELLOW + tr.getPlayerID());
        String initial = "REPLACE INTO %s(playerID, steamKey) VALUES(?,?)";
        try(PreparedStatement statement = preparedStatement(String.format(initial,userTableName))){
            statement.setString(1, tr.getPlayerID());
            statement.setString(2, tr.getSteamKey());
            statement.execute();
            statement.getConnection().close();
        } catch (SQLException e) {
            U.error(CC.RED + "Error in trying to update player vote record!");
        }
    }


    public void createUserTable(){
        String table = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                "playerID	    VarChar(36) PRIMARY KEY," +
                "steamKey		VarChar(36)" +
                ")", userTableName);

        try {
            statement().executeUpdate(table);
            statement().getConnection().close();
        } catch (SQLException e) {
            U.error("Error Creating SQL TABLE-- CHECK YOUR DATA CONFIG", e);
        }

    }

    public boolean testDB(){
        try {
            Connection con = ds.getConnection();
            U.info(CC.GREEN + "SQL Connection SUCCESS");
            return true;
        } catch (SQLException e) {
            U.error(CC.RED + "SQL Connection ERROR");
            e.printStackTrace();
        }
        return false;
    }
}