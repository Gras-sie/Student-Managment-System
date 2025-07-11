<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BC Student Wellness</title>
    <style>
        /* Global Styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh; /* Ensure full viewport height */
            background-color: #1a1a1a; /* Dark grey/black background */
            color: #f0f0f0; /* Light text color for contrast */
            line-height: 1.6;
        }

        /* Header Styles */
        header {
            background-color: #2c2c2c; /* Slightly lighter dark grey for header */
            color: #4CAF50; /* Green for the title */
            padding: 25px 0;
            text-align: center;
            font-size: 2.2em;
            font-weight: 700;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.4); /* Subtle shadow */
            letter-spacing: 1px;
        }

        /* Main Content Container */
        .container {
            flex-grow: 1; /* Allows container to take up available space */
            display: flex;
            flex-direction: column;
            justify-content: center; /* Center content vertically */
            align-items: center; /* Center content horizontally */
            padding: 40px 20px;
            text-align: center;
        }

        h1 {
            font-size: 3.5em; /* Large welcome text */
            color: #4CAF50; /* Green for the main heading */
            margin-bottom: 20px;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
        }

        p {
            max-width: 700px;
            font-size: 1.2em;
            margin-bottom: 40px;
            opacity: 0.9; /* Slightly transparent for depth */
        }

        /* Button Group */
        .button-group {
            display: flex;
            gap: 25px; /* Space between buttons */
            margin-top: 20px;
        }

        .button-link {
            display: inline-block;
            padding: 15px 35px;
            text-decoration: none;
            border-radius: 8px; /* Slightly rounded corners */
            font-size: 1.1em;
            font-weight: 600;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .button-link.login {
            background-color: #3a3a3a; /* Dark grey for Login */
            color: #f0f0f0;
            border: 1px solid #555;
        }

        .button-link.login:hover {
            background-color: #555;
            transform: translateY(-2px);
        }

        .button-link.register {
            background-color: #4CAF50; /* Green for Register */
            color: white;
            border: 1px solid #388E3C;
        }

        .button-link.register:hover {
            background-color: #388E3C;
            transform: translateY(-2px);
        }

        /* Footer Styles */
        footer {
            background-color: #2c2c2c; /* Same as header background */
            color: #b0b0b0; /* Lighter grey for footer text */
            padding: 20px 0;
            text-align: center;
            font-size: 0.9em;
            box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.4); /* Subtle shadow */
            margin-top: auto; /* Pushes footer to the bottom */
        }
    </style>
</head>
<body>
    <header>
        BC Student Wellness
    </header>

    <div class="container">
        <h1>Welcome to Your Wellness Hub!</h1>
        <p>
            This platform is dedicated to supporting Belgium Campus students in managing their well-being.
            Access essential services, connect with counselors, and provide valuable feedback.
        </p>
        <div class="button-group">
            <a href="login.jsp" class="button-link login">Login</a>
            <a href="register.jsp" class="button-link register">Register</a>
        </div>
    </div>

    <footer>
        &copy; 2025 Certified BC Student Wellness
    </footer>
</body>
</html>