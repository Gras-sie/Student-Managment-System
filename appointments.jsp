<%@ page import="com.bcstudent.model.AppointmentModel" %>
<%@ page import="com.bcstudent.controller.AppointmentController" %>
<%@ page import="com.bcstudent.model.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Appointments - BC Student Wellness</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="js/appointments.js"></script>
</head>
<body>
    <%@ include file="includes/header.jsp" %> 

    <div class="container">
        <h1>My Appointments</h1>

        <!-- Appointment List -->
        <div class="appointments-list">
            <table class="appointment-table">
                <thead>
                    <tr>
                        <th>Date & Time</th>
                        <th>Counselor</th>
                        <th>Status</th>
                        <th>Notes</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    // Get appointments for the logged-in student
                    Student student = (Student) session.getAttribute("student");
                    if (student != null) {
                        AppointmentController controller = new AppointmentController();
                        List<AppointmentModel> appointments = controller.getAppointmentsByStudent(student);
                        
                        for (AppointmentModel appointment : appointments) {
                    %>
                    <tr>
                        <td><%= appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) %></td>
                        <td><%= appointment.getCounselor().getName() %></td>
                        <td><%= appointment.getStatus() %></td>
                        <td><%= appointment.getNotes() != null ? appointment.getNotes() : "" %></td>
                        <td>
                            <button onclick="showAppointmentDetails(<%= appointment.getAppointmentId() %>)" 
                                    class="details-button">Details</button>
                            <% if ("Scheduled".equals(appointment.getStatus())) { %>
                                <button onclick="cancelAppointment(<%= appointment.getAppointmentId() %>)" 
                                        class="cancel-button">Cancel</button>
                            <% } %>
                        </td>
                    </tr>
                    <%
                        }
                    }
                    %>
                </tbody>
            </table>
        </div>

        <!-- Appointment Details Modal -->
        <div id="appointmentDetailsModal" class="modal">
            <div class="modal-content">
                <h2>Appointment Details</h2>
                <div id="appointmentDetails"></div>
                <button onclick="closeModal()" class="close-button">Close</button>
            </div>
        </div>

        <!-- New Appointment Form -->
        <div class="new-appointment">
            <button onclick="showNewAppointmentForm()" class="new-appointment-button">
                Schedule New Appointment
            </button>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %> 
</body>
</html>
