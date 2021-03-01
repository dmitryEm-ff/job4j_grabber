package ru.job4j.grabber;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cnn = DriverManager.getConnection(
                cfg.getProperty("url"),
                cfg.getProperty("username"),
                cfg.getProperty("password"));
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = cnn.prepareStatement(
                "insert into post(name, text, link, created) VALUES (?, ?, ?, ?) on conflict do nothing",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, post.getUrl());
            ps.setString(4, post.getDate());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement("select * from post")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getString("link"),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getString("created")
                        );
                post.setId(resultSet.getInt("id"));
                posts.add(post);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        try (PreparedStatement ps = cnn.prepareStatement("select * from post where id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                post.setUrl(resultSet.getString("link"));
                post.setTitle(resultSet.getString("name"));
                post.setText(resultSet.getString("text"));
                post.setDate(resultSet.getString("created"));
                post.setId(Integer.parseInt(id));
                return post;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Could not find by this id");
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        Properties cfg = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("grabber.properties")) {
            cfg.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        PsqlStore psqlStore = new PsqlStore(cfg);
        Post testOne = new Post("http1", "Title1", "Here some text1", "26/02/2021");
        Post testTwo = new Post("http2", "Title2", "Here some text2", "27/02/2021");
        Post testThree = new Post("http3", "Title3", "Here some text3", "28/02/2021");
        Post testFour = new Post("http4", "Title4", "Here some text4", "29/02/2021");
        Post testFive = new Post("http5", "Title5", "Here some text5", "30/02/2021");

        psqlStore.save(testOne);
        psqlStore.save(testTwo);
        psqlStore.save(testThree);
        psqlStore.save(testFour);
        psqlStore.save(testFive);

        for (Post p : psqlStore.getAll()) {
            System.out.println(p);
        }

        System.out.println(System.lineSeparator());

        System.out.println(psqlStore.findById("3"));
    }
}