package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Date;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbitCon {

    public static void main(String[] args) {
        Properties config = new Properties();
        Connection cn;
        try (InputStream in = AlertRabbitCon.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        }  catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connect", cn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Date currentDate = new Date();
            System.out.println(currentDate);
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("connect");
            try (PreparedStatement ps = cn.prepareStatement("insert into rabbit(created_date) values (?)")) {
                ps.setString(1, currentDate.toString());
                ps.executeUpdate();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}