// script.js
document.addEventListener('DOMContentLoaded', function() {
    // Khởi tạo tooltip Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Xử lý sự kiện cho các nút đăng nhập/đăng ký
    const loginButtons = document.querySelectorAll('a[href="login.html"]');
    const registerButtons = document.querySelectorAll('a[href="register.html"]');
    
    loginButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            // Xử lý chuyển hướng hoặc hiển thị modal đăng nhập
            console.log('Chuyển đến trang đăng nhập');
            window.location.href = 'login.html';
        });
    });
    
    registerButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            // Xử lý chuyển hướng hoặc hiển thị modal đăng ký
            console.log('Chuyển đến trang đăng ký');
            window.location.href = 'register.html';
        });
    });

    // Hiệu ứng scroll cho các section
    window.addEventListener('scroll', function() {
        const scrollPosition = window.scrollY;
        const featureCards = document.querySelectorAll('.feature-card');
        
        featureCards.forEach((card, index) => {
            const cardPosition = card.getBoundingClientRect().top;
            if (cardPosition < window.innerHeight - 100) {
                card.style.transitionDelay = `${index * 0.1}s`;
                card.classList.add('animate__animated', 'animate__fadeInUp');
            }
        });
    });

    // Kích hoạt animation khi trang được tải
    setTimeout(() => {
        const featureCards = document.querySelectorAll('.feature-card');
        featureCards.forEach((card, index) => {
            card.style.transitionDelay = `${index * 0.1}s`;
            card.classList.add('animate__animated', 'animate__fadeInUp');
        });
    }, 500);

    // Xử lý responsive menu (nếu có)
    const navbarToggler = document.querySelector('.navbar-toggler');
    if (navbarToggler) {
        navbarToggler.addEventListener('click', function() {
            const navbarCollapse = document.querySelector('.navbar-collapse');
            navbarCollapse.classList.toggle('show');
        });
    }
});

// Hàm kiểm tra xem người dùng đã đăng nhập chưa
function checkLoginStatus() {
    // Kiểm tra trong localStorage hoặc sessionStorage
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    if (isLoggedIn) {
        // Cập nhật giao diện nếu đã đăng nhập
        const loginButtons = document.querySelectorAll('a[href="login.html"]');
        loginButtons.forEach(button => {
            button.textContent = 'Tài khoản';
            button.href = 'profile.html';
        });
    }
}

// Gọi hàm kiểm tra khi trang tải
checkLoginStatus();