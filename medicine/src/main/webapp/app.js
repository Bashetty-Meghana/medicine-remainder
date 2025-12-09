// API Base URL
const API_BASE = '/medicine-reminder';

// Global state
let medicines = [];
let reminders = [];
let notificationCheckInterval = null;  // NEW: Store interval ID for notification checking

// Initialize dashboard on page load
document.addEventListener('DOMContentLoaded', () => {
    loadMedicines();
    loadReminders();
    setupEventListeners();
    setTodayDate();
    // NEW: Initialize notification system
    initializeNotifications();
});

// Set today's date as default for reminder date input
function setTodayDate() {
    const dateInput = document.getElementById('reminderDate');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.value = today;
    }
}

// Setup event listeners
function setupEventListeners() {
    // Add medicine form
    const addMedicineForm = document.getElementById('addMedicineForm');
    if (addMedicineForm) {
        addMedicineForm.addEventListener('submit', handleAddMedicine);
    }

    // Add reminder form
    const addReminderForm = document.getElementById('addReminderForm');
    if (addReminderForm) {
        addReminderForm.addEventListener('submit', handleAddReminder);
    }
}

// Show message
function showMessage(text, isError = false) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.textContent = text;
        messageDiv.className = 'message ' + (isError ? 'error' : 'success');
        messageDiv.style.display = 'block';
        
        // Auto-hide after 3 seconds
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 3000);
    }
}

// Logout function
function logout() {
    window.location.href = 'index.html';
}

// ==================== MEDICINE FUNCTIONS ====================

// Load all medicines
async function loadMedicines() {
    try {
        const response = await fetch(`${API_BASE}/medicines`);
        const data = await response.json();

        if (data.success) {
            medicines = data.medicines;
            displayMedicines(medicines);
            updateMedicineDropdown(medicines);
        } else {
            console.error('Failed to load medicines:', data.message);
            document.getElementById('medicinesList').innerHTML = 
                '<p class="error">Failed to load medicines</p>';
        }
    } catch (error) {
        console.error('Error loading medicines:', error);
        document.getElementById('medicinesList').innerHTML = 
            '<p class="error">Error loading medicines</p>';
    }
}

// Display medicines in the list
function displayMedicines(medicineList) {
    const container = document.getElementById('medicinesList');
    
    if (!medicineList || medicineList.length === 0) {
        container.innerHTML = '<p class="empty-state">No medicines added yet. Add your first medicine above!</p>';
        return;
    }

    container.innerHTML = medicineList.map(medicine => `
        <div class="medicine-item">
            <div class="medicine-info">
                <h4>${escapeHtml(medicine.name)}</h4>
                ${medicine.dosage ? `<p><strong>Dosage:</strong> ${escapeHtml(medicine.dosage)}</p>` : ''}
                ${medicine.notes ? `<p><strong>Notes:</strong> ${escapeHtml(medicine.notes)}</p>` : ''}
            </div>
            <div>
                <button onclick="deleteMedicine(${medicine.id})" class="btn btn-danger">Delete</button>
            </div>
        </div>
    `).join('');
}

// Update medicine dropdown in reminder form
function updateMedicineDropdown(medicineList) {
    const dropdown = document.getElementById('reminderMedicine');
    if (!dropdown) return;

    dropdown.innerHTML = '<option value="">-- Select Medicine --</option>';
    
    medicineList.forEach(medicine => {
        const option = document.createElement('option');
        option.value = medicine.id;
        option.textContent = `${medicine.name}${medicine.dosage ? ' (' + medicine.dosage + ')' : ''}`;
        dropdown.appendChild(option);
    });
}

// Handle add medicine form submission
async function handleAddMedicine(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const params = new URLSearchParams(formData);

    try {
        const response = await fetch(`${API_BASE}/medicines`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        });

        const data = await response.json();

        if (data.success) {
            showMessage('Medicine added successfully!', false);
            e.target.reset();
            loadMedicines(); // Reload the list
        } else {
            showMessage(data.message || 'Failed to add medicine', true);
        }
    } catch (error) {
        showMessage('Error: ' + error.message, true);
    }
}

// Delete a medicine
async function deleteMedicine(medicineId) {
    if (!confirm('Are you sure you want to delete this medicine? This will also delete all associated reminders.')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/medicines/delete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `id=${medicineId}`
        });

        const data = await response.json();

        if (data.success) {
            showMessage('Medicine deleted successfully!', false);
            loadMedicines(); // Reload the list
            loadReminders(); // Reload reminders as they might be affected
        } else {
            showMessage(data.message || 'Failed to delete medicine', true);
        }
    } catch (error) {
        showMessage('Error: ' + error.message, true);
    }
}

// ==================== REMINDER FUNCTIONS ====================

// Load today's reminders
async function loadReminders() {
    try {
        const response = await fetch(`${API_BASE}/reminders`);
        const data = await response.json();

        if (data.success) {
            reminders = data.reminders;
            displayReminders(reminders);
        } else {
            console.error('Failed to load reminders:', data.message);
            document.getElementById('remindersList').innerHTML = 
                '<p class="error">Failed to load reminders</p>';
        }
    } catch (error) {
        console.error('Error loading reminders:', error);
        document.getElementById('remindersList').innerHTML = 
            '<p class="error">Error loading reminders</p>';
    }
}

// Display reminders in the list
function displayReminders(reminderList) {
    const container = document.getElementById('remindersList');
    
    if (!reminderList || reminderList.length === 0) {
        container.innerHTML = '<p class="empty-state">No reminders for today. Add a reminder above!</p>';
        return;
    }

    container.innerHTML = reminderList.map(reminder => {
        const timeStr = reminder.reminderTime.substring(0, 5); // Format HH:MM
        const takenClass = reminder.taken ? 'taken' : '';
        const statusClass = reminder.taken ? 'taken' : 'pending';
        const statusText = reminder.taken ? 'Taken' : 'Pending';

        return `
            <div class="reminder-item ${takenClass}">
                <div class="reminder-info">
                    <h4>${escapeHtml(reminder.medicineName)}</h4>
                    <p><strong>Time:</strong> ${timeStr}</p>
                    <p><span class="reminder-status ${statusClass}">${statusText}</span></p>
                </div>
                <div>
                    ${!reminder.taken ? 
                        `<button onclick="markReminderTaken(${reminder.id})" class="btn btn-success">Mark as Taken</button>` 
                        : '<span style="color: #4CAF50; font-weight: bold;">✓ Completed</span>'}
                </div>
            </div>
        `;
    }).join('');
}

// Handle add reminder form submission
async function handleAddReminder(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const params = new URLSearchParams(formData);

    try {
        const response = await fetch(`${API_BASE}/reminders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        });

        const data = await response.json();

        if (data.success) {
            showMessage('Reminder added successfully!', false);
            e.target.reset();
            setTodayDate(); // Reset date to today
            loadReminders(); // Reload the list
        } else {
            showMessage(data.message || 'Failed to add reminder', true);
        }
    } catch (error) {
        showMessage('Error: ' + error.message, true);
    }
}

// Mark a reminder as taken
async function markReminderTaken(reminderId) {
    try {
        const response = await fetch(`${API_BASE}/reminders/markTaken`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `id=${reminderId}`
        });

        const data = await response.json();

        if (data.success) {
            showMessage('Reminder marked as taken!', false);
            loadReminders(); // Reload the list
        } else {
            showMessage(data.message || 'Failed to mark reminder as taken', true);
        }
    } catch (error) {
        showMessage('Error: ' + error.message, true);
    }
}

// ==================== UTILITY FUNCTIONS ====================

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== NEW: NOTIFICATION SYSTEM ====================

/**
 * Initialize the notification system
 * - Requests browser notification permission
 * - Starts periodic checking for due reminders
 */
function initializeNotifications() {
    // Request notification permission if supported
    if ('Notification' in window && Notification.permission === 'default') {
        Notification.requestPermission().then(permission => {
            console.log('Notification permission:', permission);
        });
    }

    // Start checking for due reminders every 60 seconds (1 minute)
    // This interval runs while the dashboard page is open
    notificationCheckInterval = setInterval(checkDueReminders, 60000);
    
    // Also check immediately on page load
    setTimeout(checkDueReminders, 3000); // Wait 3 seconds after page load
}

/**
 * Check for due reminders and show notifications
 * This function:
 * 1. Calls the backend /reminders/due endpoint
 * 2. For each due reminder, shows browser notification + on-page toast
 * 3. Backend automatically marks them as notified to prevent duplicates
 */
async function checkDueReminders() {
    try {
        const response = await fetch(`${API_BASE}/reminders/due`);
        const data = await response.json();

        if (data.success && data.reminders && data.reminders.length > 0) {
            // We have due reminders - show notifications
            data.reminders.forEach(reminder => {
                showReminderNotification(reminder);
            });
            
            // Reload today's reminders list to show updated status
            loadReminders();
        }
    } catch (error) {
        console.error('Error checking due reminders:', error);
    }
}

/**
 * Show notification for a single reminder
 * - Shows browser notification if permission granted
 * - Always shows on-page toast (fallback if browser notification not available)
 */
function showReminderNotification(reminder) {
    const timeStr = reminder.reminderTime.substring(0, 5); // Format: HH:MM
    const title = 'Medicine Reminder';
    const body = `Time to take: ${reminder.medicineName} at ${timeStr}`;

    // Try to show browser notification
    if ('Notification' in window && Notification.permission === 'granted') {
        const notification = new Notification(title, {
            body: body,
            icon: '/medicine-reminder/favicon.ico', // Optional: add an icon
            tag: `reminder-${reminder.id}`, // Prevent duplicate notifications
            requireInteraction: false // Auto-close after a few seconds
        });

        // Optional: Handle notification click
        notification.onclick = function() {
            window.focus();
            notification.close();
        };
    }

    // Always show on-page toast notification (works even if browser notifications disabled)
    showToastNotification(reminder.medicineName, timeStr);
}

/**
 * Show on-page toast notification
 * Creates a temporary banner at the top of the page
 */
function showToastNotification(medicineName, time) {
    // Create toast element
    const toast = document.createElement('div');
    toast.className = 'notification-toast';
    toast.innerHTML = `
        <span class="notification-icon">💊</span>
        <span class="notification-text">
            <strong>Medicine Reminder!</strong><br>
            Time to take: ${escapeHtml(medicineName)} at ${time}
        </span>
        <button class="notification-close" onclick="this.parentElement.remove()">&times;</button>
    `;

    // Add to page
    document.body.appendChild(toast);

    // Auto-remove after 10 seconds
    setTimeout(() => {
        if (toast.parentElement) {
            toast.classList.add('notification-fade-out');
            setTimeout(() => toast.remove(), 500);
        }
    }, 10000);
}
