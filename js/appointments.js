// Modal handling
function showModal() {
    document.getElementById('appointmentDetailsModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('appointmentDetailsModal').style.display = 'none';
}

// Appointment Details
function showAppointmentDetails(appointmentId) {
    showModal();
    
    // Fetch appointment details from server
    fetch(`/AppointmentDetailsServlet?id=${appointmentId}`)
        .then(response => response.json())
        .then(data => {
            const detailsDiv = document.getElementById('appointmentDetails');
            detailsDiv.innerHTML = `
                <p><strong>Date & Time:</strong> ${data.appointmentTime}</p>
                <p><strong>Counselor:</strong> ${data.counselorName}</p>
                <p><strong>Status:</strong> ${data.status}</p>
                <p><strong>Notes:</strong> ${data.notes || 'No notes'}</p>
            `;
        })
        .catch(error => console.error('Error:', error));
}

// Cancel Appointment
function cancelAppointment(appointmentId) {
    if (confirm('Are you sure you want to cancel this appointment?')) {
        fetch(`/CancelAppointmentServlet?id=${appointmentId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Appointment cancelled successfully');
                // Refresh the page to show updated list
                location.reload();
            } else {
                alert('Failed to cancel appointment');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

// New Appointment Form
function showNewAppointmentForm() {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <h2>Schedule New Appointment</h2>
            <form id="newAppointmentForm">
                <div class="form-group">
                    <label for="counselor">Select Counselor:</label>
                    <select id="counselor" name="counselor" required>
                        <option value="">Select a counselor</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="appointmentTime">Appointment Time:</label>
                    <input type="datetime-local" id="appointmentTime" name="appointmentTime" required>
                </div>
                <div class="form-group">
                    <label for="notes">Notes:</label>
                    <textarea id="notes" name="notes"></textarea>
                </div>
                <button type="submit" class="submit-button">Schedule Appointment</button>
                <button type="button" onclick="closeModal()" class="cancel-button">Cancel</button>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
    
    // Fetch counselors list
    fetch('/GetCounselorsServlet')
        .then(response => response.json())
        .then(data => {
            const counselorSelect = document.getElementById('counselor');
            data.forEach(counselor => {
                const option = document.createElement('option');
                option.value = counselor.id;
                option.textContent = counselor.name;
                counselorSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error:', error));

    // Form submission
    document.getElementById('newAppointmentForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(this);
        
        fetch('/CreateAppointmentServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Appointment scheduled successfully');
                modal.remove();
                location.reload();
            } else {
                alert('Failed to schedule appointment');
            }
        })
        .catch(error => console.error('Error:', error));
    });
}
