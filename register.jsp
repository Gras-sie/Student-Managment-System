<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - BC Student Wellness</title>
    <style>
        /* Global Styles (Consistent with index.jsp) */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #1a1a1a; /* Dark grey/black background */
            color: #f0f0f0; /* Light text color for contrast */
            line-height: 1.6;
        }

        /* Header Styles (Consistent with index.jsp) */
        header {
            background-color: #2c2c2c; /* Slightly lighter dark grey for header */
            color: #4CAF50; /* Green for the title */
            padding: 25px 0;
            text-align: center;
            font-size: 2.2em;
            font-weight: 700;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.4);
            letter-spacing: 1px;
        }

        /* Form Container */
        .form-container {
            flex-grow: 1; /* Allows container to take up available space */
            display: flex;
            flex-direction: column;
            justify-content: center; /* Center content vertically */
            align-items: center; /* Center content horizontally */
            padding: 40px 20px;
        }

        .registration-box {
            background-color: #2c2c2c; /* Darker background for the form box */
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5); /* Stronger shadow for depth */
            width: 100%;
            max-width: 500px; /* Max width for the form */
            box-sizing: border-box; /* Include padding in width */
            border: 1px solid #4CAF50; /* Green border accent */
        }

        h2 {
            font-size: 2.2em;
            color: #4CAF50; /* Green heading */
            margin-bottom: 30px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
            text-align: left; /* Align labels to the left */
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 1.1em;
            color: #f0f0f0; /* Light label text */
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="tel"] { /* Added type="tel" for phone number */
            width: 100%;
            padding: 12px;
            border: 1px solid #555; /* Darker border for inputs */
            border-radius: 5px;
            background-color: #3a3a3a; /* Darker input background */
            color: #f0f0f0; /* Light input text */
            font-size: 1em;
            box-sizing: border-box; /* Include padding in width */
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus,
        input[type="tel"]:focus {
            border-color: #4CAF50; /* Green border on focus */
            outline: none;
            box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.3); /* Green glow on focus */
        }

        .submit-button {
            width: 100%;
            padding: 15px;
            background-color: #4CAF50; /* Green submit button */
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1.2em;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .submit-button:hover {
            background-color: #388E3C; /* Darker green on hover */
            transform: translateY(-2px);
        }

        .login-link-container {
            margin-top: 20px;
            font-size: 0.95em;
            text-align: center;
        }

        .login-link-container a {
            color: #4CAF50; /* Green link */
            text-decoration: none;
            font-weight: 600;
        }

        .login-link-container a:hover {
            text-decoration: underline;
        }

        /* Footer Styles (Consistent with index.jsp) */
        footer {
            background-color: #2c2c2c; /* Same as header background */
            color: #b0b0b0; /* Lighter grey for footer text */
            padding: 20px 0;
            text-align: center;
            font-size: 0.9em;
            box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.4);
            margin-top: auto; /* Pushes footer to the bottom */
        }
    </style>
</head>
<body>
    <header>
        BC Student Wellness
    </header>

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

    <footer>
        &copy; 2025 Certified BC Student Wellness
    </footer>
</body>
</html>