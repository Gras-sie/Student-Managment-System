<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - BC Student Wellness</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%@ include file="includes/header.jsp" %> 

    <div class="form-container">
        <div class="login-box">
            <h2>Student Login</h2>

            <%-- Display error messages if any --%>
            <div class="error-message" id="errorMessage" style="display: none;"></div>
            <script>
                // Check for an error message in URL parameters
                let urlParams = new URLSearchParams(window.location.search);
                let errorMsg = urlParams.get('error');
                if (errorMsg) {
                    document.getElementById('errorMessage').textContent = decodeURIComponent(errorMsg);
                    document.getElementById('errorMessage').setAttribute('style', 'display: block;');
                }
            </script>

            <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                <div class="form-group">
                    <label for="username">Student Number or Email:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit" class="submit-button">Login</button>
            </form>
            <div class="register-link-container">
                Don't have an account? <a href="register.jsp">Register here</a>
            </div>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %> 
</body>
</html>
