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
            <h2 class="welcome-message">Welcome, ${sessionScope.name != null ? sessionScope.name : "Student"}!</h2>
            <p>
                You have successfully logged into the BC Student Wellness Management System.
                Here you can manage your appointments, interact with counselors, and submit feedback.
            </p>
            <p>
                Explore the features designed to support your well-being journey.
            </p>

            <%-- The logout button points to the LogoutServlet URL mapping --%>
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-button">Logout</a>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %> 
</body>
</html>
