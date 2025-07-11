<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - BC Student Wellness</title>
    <style>
        /* Global Styles (Consistent with index.jsp, register.jsp, login.jsp) */
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

        /* Header Styles (Consistent with other pages) */
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

        /* Main Content Container */
        .dashboard-container {
            flex-grow: 1; /* Allows container to take up available space */
            display: flex;
            flex-direction: column;
            justify-content: center; /* Center content vertically */
            align-items: center; /* Center content horizontally */
            padding: 40px 20px;
            text-align: center;
        }

        .dashboard-box {
            background-color: #2c2c2c; /* Darker background for the dashboard content box */
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5); /* Stronger shadow for depth */
            width: 100%;
            max-width: 700px; /* Max width for dashboard content */
            box-sizing: border-box; /* Include padding in width */
            border: 1px solid #4CAF50; /* Green border accent */
        }

        h2 {
            font-size: 2.5em;
            color: #4CAF50; /* Green heading */
            margin-bottom: 20px;
            text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.2);
        }

        p {
            font-size: 1.15em;
            margin-bottom: 30px;
            opacity: 0.9;
        }

        .welcome-message {
            font-size: 1.8em;
            font-weight: 600;
            color: #f0f0f0;
            margin-bottom: 20px;
        }

        .logout-button {
            display: inline-block;
            padding: 12px 25px;
            background-color: #ff6347; /* A red color for logout/danger action */
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 1.05em;
            font-weight: 600;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .logout-button:hover {
            background-color: #e5533d; /* Darker red on hover */
            transform: translateY(-2px);
        }

        /* Footer Styles (Consistent with other pages) */
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

    <footer>
        &copy; 2025 Certified BC Student Wellness
    </footer>
</body>
</html>