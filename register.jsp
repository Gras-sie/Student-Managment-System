<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - BC Student Wellness</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        function validateForm() {
            // Get form elements
            const passwordElement = document.getElementById("password");
            const confirmPasswordElement = document.getElementById("confirmPassword");

            if (!passwordElement || !confirmPasswordElement) {
                console.error("Password or confirm password field not found");
                return false;
            }

            // Cast elements to HTMLInputElement to access a value property
            const passwordInput = /** @type {HTMLInputElement} */ (passwordElement);
            const confirmPasswordInput = /** @type {HTMLInputElement} */ (confirmPasswordElement);

            const password = passwordInput.value || '';
            const confirmPassword = confirmPasswordInput.value || '';

            if (password !== confirmPassword) {
                alert("Passwords do not match!");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <%@ include file="includes/header.jsp" %> 

    <div class="form-container">
        <div class="registration-box">
            <h2>New Student Registration</h2>

            <%-- Display error messages if any --%>
            <div class="error-message" id="errorMessage" style="display: none;"></div>
            <script>
                // Check for an error message in URL parameters
                document.addEventListener('DOMContentLoaded', function() {
                    const urlParams = new URLSearchParams(window.location.search);
                    const errorMsg = urlParams.get('error');
                    if (errorMsg) {
                        const errorElement = document.getElementById('errorMessage');
                        if (errorElement) {
                            errorElement.textContent = decodeURIComponent(errorMsg);
                            errorElement.setAttribute('style', 'display: block');
                        }
                    }
                });
            </script>

            <form action="${pageContext.request.contextPath}/RegisterServlet" method="post" onsubmit="return validateForm();">
                <div class="form-group">
                    <label for="studentNumber">Student Number:</label>
                    <input type="text" id="studentNumber" name="studentNumber" pattern="^[a-zA-Z0-9]+$" title="Student number should contain only letters and numbers" required>
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
                    <input type="email" id="email" name="email" pattern="^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$" title="Please enter a valid email address" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone Number:</label>
                    <input type="tel" id="phone" name="phone" placeholder="e.g., 0821234567" pattern="[0-9]{10}" title="Phone number must be 10 digits" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$" title="Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit" required>
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
