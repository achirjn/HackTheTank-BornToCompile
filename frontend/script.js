// Handle login form submission
function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email')?.value;
    const password = document.getElementById('password')?.value;
    const rememberMe = document.querySelector('.remember-me input')?.checked;
    
    if (email && password) {
        // Store user data in localStorage
        localStorage.setItem('userEmail', email);
        localStorage.setItem('userName', email.split('@')[0]);
        
        if (rememberMe) {
            localStorage.setItem('rememberMe', 'true');
        }
        
        // Redirect to home page
        window.location.href = 'home.html';
    }
}

// Handle logout
function handleLogout() {
    localStorage.clear();
    window.location.href = 'index.html';
}

// Check if user is logged in on home page
function checkAuthentication() {
    const currentPage = window.location.pathname;
    
    if (currentPage.includes('home.html')) {
        const userEmail = localStorage.getItem('userEmail');
        
        if (!userEmail) {
            // Redirect to login if not authenticated
            window.location.href = 'index.html';
        } else {
            // Display user information
            document.getElementById('userEmail').textContent = userEmail;
            document.getElementById('userName').textContent = localStorage.getItem('userName');
        }
    } else if (currentPage.includes('index.html') || currentPage === '/') {
        // If already logged in, redirect to home
        const userEmail = localStorage.getItem('userEmail');
        if (userEmail) {
            window.location.href = 'home.html';
        }
    }
}

// Run authentication check when page loads
document.addEventListener('DOMContentLoaded', checkAuthentication);
