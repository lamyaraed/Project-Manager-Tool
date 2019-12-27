import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class getProjectChart extends JFrame {

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public getProjectChart(){
        super("Project Chart");
    }

    public void getCategoryDataset(Connection connection, int projectID) {

        TaskSeries PlannedTimePath = new TaskSeries("Estimated Date");
        TaskSeries ActualTimePath = new TaskSeries("Actual Date");

        String showSql = "SELECT MajorTaskID, startTime, dueDate, ActualWorkingHours \r\n" + "  FROM MajorTasks WHERE [ProjectID]= " + projectID;

        Date StartingDate = new Date();
        Date DueDate = new Date();
        ResultSet resultSet = null;
        TaskSeriesCollection dataset = new TaskSeriesCollection();

        try (PreparedStatement prepsInsertProduct = connection.prepareStatement(showSql, Statement.RETURN_GENERATED_KEYS)) {
            prepsInsertProduct.execute();
            resultSet = prepsInsertProduct.getResultSet();
            //System.out.println("ID" + " " + "StartTime" + " " + "dueDate" + " " + "ActualWorkingHours");
            while (resultSet.next()) {
                //Retrieve by column name
                String MajorTaskName = resultSet.getString("MajorTaskID");
                Date startDate = resultSet.getDate("startTime");
                Date dueDate = resultSet.getDate("dueDate");
                //System.out.println(startDate);
                String ActualWorking = resultSet.getString("ActualWorkingHours");

                //System.out.println(MajorTaskName + "        " + ActualWorkingHours);
                DueDate = getDate(startDate,Integer.parseInt(ActualWorking));
                //System.out.println(DueDate);
                PlannedTimePath.add(new Task(MajorTaskName, startDate, dueDate));
                ActualTimePath.add(new Task(MajorTaskName, startDate, DueDate));
            }

            dataset.add(PlannedTimePath);
            dataset.add(ActualTimePath);

            JFreeChart chart = ChartFactory.createGanttChart("Project Gantt Chart", "Project Tasks", "Timeline", dataset);

            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {

            setSize(800, 400);
            setLocationRelativeTo(null);
            setVisible(true);
        });

    }
    public void getEstimatedChart(Connection connection, int projectID) {

        TaskSeries PlannedTimePath = new TaskSeries("Estimated Date");

        String showSql = "SELECT MajorTaskID, startTime, dueDate \r\n" + "  FROM MajorTasks WHERE [ProjectID]= " + projectID;

        ResultSet resultSet = null;
        TaskSeriesCollection dataset = new TaskSeriesCollection();

        try (PreparedStatement prepsInsertProduct = connection.prepareStatement(showSql, Statement.RETURN_GENERATED_KEYS)) {
            prepsInsertProduct.execute();
            resultSet = prepsInsertProduct.getResultSet();
            while (resultSet.next()) {
                //Retrieve by column name
                String MajorTaskName = resultSet.getString("MajorTaskID");
                Date startDate = resultSet.getDate("startTime");
                Date dueDate = resultSet.getDate("dueDate");

                PlannedTimePath.add(new Task(MajorTaskName, startDate, dueDate));
            }

            dataset.add(PlannedTimePath);

            JFreeChart chart = ChartFactory.createGanttChart("Project Gantt Chart", "Project Tasks", "Timeline", dataset);

            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);
            SwingUtilities.invokeLater(() -> {

                setSize(800, 400);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setVisible(true);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Date getDate(Date StartDate, int hour) throws ParseException {
        Date StartingDate = StartDate;
        int Year = StartingDate.getYear()+1900;
        int Month = StartingDate.getMonth()+1;
        int Day = StartingDate.getDate();

        Date dueDate;
        int years = hour/8760;
        int months = (hour%8760)/730;
        hour = (hour%8760);
        int days = (hour%730)/24;
        String Stringyear = Integer.toString(Year+years);
        String StringMonth = Integer.toString(Month+months);
        String StringDay = Integer.toString(Day+days);
        String StringDate = Stringyear + "-" + StringMonth+ "-" + StringDay;
        dueDate = null;
        dueDate = format.parse(StringDate);
        //System.out.println(dueDate);
        return dueDate;
    }
}
