<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - BC Student Wellness</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%@ include file="includes/header.jsp" %> 

    <div class="form-container">
        <div class="registration-box">
            <h2>New Student Registration</h2>
            <form action="RegisterServlet" method="post">
                <div class="form-group">
                    <label for="studentNumber">Student Number:</label>
                    <input type="text" id="studentNumber" name="studentNumber" required>
                </div>
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="surname">Surname:</label>
                    <input type="text" id="surname" name="surname" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone Number:</label>
                    <input type="tel" id="phone" name="phone" placeholder="e.g., 0821234567" pattern="[0-9]{10}" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>
                <button type="submit" class="submit-button">Register Account</button>
            </form>
            <div class="login-link-container">
                Already have an account? <a href="login.jsp">Login here</a>
            </div>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %> 
</body>
</html>