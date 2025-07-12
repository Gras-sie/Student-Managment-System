<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - BC Student Wellness</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%@ include file="includes/header.jsp" %> 

    <div class="dashboard-container">
        <div class="dashboard-box">
            <h2 class="welcome-message">Welcome, <%= (request.getSession().getAttribute("userName") != null) ? request.getSession().getAttribute("userName") : "Student" %>!</h2>
            <p>
                You have successfully logged into the BC Student Wellness Management System.
                Here you can manage your appointments, interact with counselors, and submit feedback.
            </p>
            <p>
                Explore the features designed to support your well-being journey.
            </p>

            <%-- The logout button will eventually point to your LogoutServlet --%>
            <a href="LogoutServlet" class="logout-button">Logout</a>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %> 
</body>
</html>