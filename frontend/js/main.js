// Base URL for API
const API_BASE_URL = 'http://localhost:8080/api';

console.log('✅ main.js loaded successfully!');
console.log('Current page:', window.location.pathname);

// ========== AUTHENTICATION ==========

// Login form handler
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM fully loaded');
    
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        console.log('Login form found');
        loginForm.addEventListener('submit', handleLogin);
    }
    
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        console.log('Register form found');
        registerForm.addEventListener('submit', handleRegister);
    }
});

async function handleLogin(e) {
    e.preventDefault();
    console.log('Login form submitted');
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    console.log('Login attempt for username:', username);
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        console.log('Login response:', data);
        
        if (response.ok) {
            // Store user info
            sessionStorage.setItem('isLoggedIn', 'true');
            sessionStorage.setItem('userId', data.userId || data.id);
            sessionStorage.setItem('username', data.username);
            sessionStorage.setItem('userRole', data.role || 'MEMBER');
            
            showSuccess('Login successful! Redirecting...');
            
            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1000);
        } else {
            showError(data.error || data.message || 'Login failed');
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('Network error. Is the backend running at ' + API_BASE_URL + '?');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    console.log('Register form submitted');
    
    // Get form data
    const fullName = document.getElementById('fullName')?.value;
    const username = document.getElementById('username')?.value;
    const email = document.getElementById('email')?.value;
    const password = document.getElementById('password')?.value;
    const confirmPassword = document.getElementById('confirmPassword')?.value;
    const phone = document.getElementById('phone')?.value;
    const address = document.getElementById('address')?.value;
    
    // Validate passwords match
    if (password !== confirmPassword) {
        showError('Passwords do not match!');
        return;
    }
    
    // Prepare user data
    const userData = {
        fullName: fullName,
        username: username,
        email: email,
        password: password,
        phone: phone || null,
        address: address || null,
        role: 'MEMBER'
    };
    
    console.log('Sending registration data:', userData);
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        
        const data = await response.json();
        console.log('Registration response:', data);
        
        if (response.ok) {
            showSuccess('Registration successful! Redirecting to login...');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            showError(data.error || data.message || 'Registration failed');
        }
    } catch (error) {
        console.error('Registration error:', error);
        showError('Network error. Is the backend running at ' + API_BASE_URL + '?');
    }
}

// ========== HELPER FUNCTIONS ==========

function showError(message) {
    console.log('Showing error:', message);
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
        
        // Hide after 5 seconds
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    } else {
        alert('Error: ' + message);
    }
}

function showSuccess(message) {
    console.log('Showing success:', message);
    const successDiv = document.getElementById('successMessage');
    if (successDiv) {
        successDiv.textContent = message;
        successDiv.style.display = 'block';
        
        // Hide after 5 seconds
        setTimeout(() => {
            successDiv.style.display = 'none';
        }, 5000);
    } else {
        alert('Success: ' + message);
    }
}

// Check authentication
function checkAuth() {
    const isLoggedIn = sessionStorage.getItem('isLoggedIn');
    const currentPage = window.location.pathname.split('/').pop();
    
    // Pages that don't require authentication
    const publicPages = ['login.html', 'register.html', 'index.html', ''];
    
    if (!isLoggedIn && !publicPages.includes(currentPage)) {
        console.log('Not authenticated, redirecting to login');
        window.location.href = 'login.html';
    }
}

// Logout function
function logout() {
    sessionStorage.clear();
    window.location.href = '../index.html';
}

// Run check on every page
document.addEventListener('DOMContentLoaded', checkAuth);