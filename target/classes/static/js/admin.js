// Toggle the side navigation
document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.querySelector('#sidebarToggleTop');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function(e) {
            e.preventDefault();
            document.querySelector('body').classList.toggle('sidebar-toggled');
            document.querySelector('.sidebar').classList.toggle('toggled');
        });
    }

    // Close any open menu accordions when window is resized
    window.addEventListener('resize', function() {
        const vw = Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0);
        
        if (vw < 768) {
            document.querySelector('.sidebar .collapse').classList.remove('show');
        }
    });

    // Scroll to top button appear
    document.addEventListener('scroll', function() {
        const scrollToTop = document.querySelector('.scroll-to-top');
        
        if (document.documentElement.scrollTop > 100) {
            scrollToTop.style.display = 'block';
        } else {
            scrollToTop.style.display = 'none';
        }
    });

    // Smooth scrolling
    document.querySelectorAll('a.scroll-to-top').forEach(function(element) {
        element.addEventListener('click', function(e) {
            e.preventDefault();
            document.querySelector('html, body').animate(
                { scrollTop: 0 }, 
                'slow'
            );
        });
    });

    // Add active state to sidebar nav links
    const path = window.location.pathname;
    document.querySelectorAll('.sidebar .nav-link').forEach(function(element) {
        if (element.getAttribute('href') === path) {
            element.parentElement.classList.add('active');
        }
    });

    // Format currency inputs
    document.querySelectorAll('.currency-input').forEach(function(input) {
        input.addEventListener('input', function(e) {
            let value = this.value.replace(/\D/g, '');
            value = new Intl.NumberFormat('vi-VN').format(value);
            this.value = value;
        });
    });

    // Format date inputs to Vietnamese format
    document.querySelectorAll('.date-input').forEach(function(input) {
        input.addEventListener('change', function(e) {
            const date = new Date(this.value);
            const formattedDate = new Intl.DateTimeFormat('vi-VN').format(date);
            this.dataset.formattedDate = formattedDate;
        });
    });

    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Toggle password visibility
    document.querySelectorAll('.toggle-password').forEach(function(button) {
        button.addEventListener('click', function(e) {
            const input = this.previousElementSibling;
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);
            this.querySelector('i').classList.toggle('fa-eye');
            this.querySelector('i').classList.toggle('fa-eye-slash');
        });
    });

    // File input preview
    document.querySelectorAll('.custom-file-input').forEach(function(input) {
        input.addEventListener('change', function(e) {
            const fileName = this.files[0].name;
            const nextSibling = this.nextElementSibling;
            nextSibling.innerText = fileName;
        });
    });

    // Confirm delete
    document.querySelectorAll('.confirm-delete').forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm('Bạn có chắc chắn muốn xóa?')) {
                e.preventDefault();
            }
        });
    });

    // Search functionality
    const searchInput = document.querySelector('#searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const searchTerm = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('tbody tr');
            
            tableRows.forEach(function(row) {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
    }

    // Sort tables
    document.querySelectorAll('th.sortable').forEach(function(header) {
        header.addEventListener('click', function() {
            const table = header.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            const index = Array.from(header.parentElement.children).indexOf(header);
            const direction = header.classList.contains('asc') ? -1 : 1;
            
            // Clear all sort classes
            header.parentElement.querySelectorAll('th').forEach(th => {
                th.classList.remove('asc', 'desc');
            });
            
            // Add sort class
            header.classList.add(direction === 1 ? 'asc' : 'desc');
            
            // Sort rows
            rows.sort((a, b) => {
                const aValue = a.children[index].textContent;
                const bValue = b.children[index].textContent;
                return aValue.localeCompare(bValue) * direction;
            });
            
            // Reorder rows
            rows.forEach(row => tbody.appendChild(row));
        });
    });
});

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Format date
function formatDate(dateString) {
    const options = { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    };
    return new Date(dateString).toLocaleDateString('vi-VN', options);
}

// DataTable initialization
$(document).ready(function() {
    if ($.fn.DataTable) {
        $('.datatable').DataTable({
            language: {
                url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json'
            },
            pageLength: 10,
            responsive: true
        });
    }
});

// Confirm delete
function confirmDelete(message) {
    return confirm(message || 'Bạn có chắc chắn muốn xóa không?');
}

// Toast notification
function showToast(message, type = 'success') {
    if ($.toast) {
        $.toast({
            heading: type === 'success' ? 'Thành công' : 'Lỗi',
            text: message,
            showHideTransition: 'slide',
            icon: type,
            position: 'top-right',
            hideAfter: 3000
        });
    }
}

// Form validation
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;

    let isValid = true;
    const requiredFields = form.querySelectorAll('[required]');

    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            field.classList.add('is-invalid');
            isValid = false;
        } else {
            field.classList.remove('is-invalid');
        }
    });

    return isValid;
}

// Image preview
function previewImage(input, previewId) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById(previewId).src = e.target.result;
        }
        reader.readAsDataURL(input.files[0]);
    }
}

// Active menu item
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const menuItems = document.querySelectorAll('.list-group-item');
    
    menuItems.forEach(item => {
        if (currentPath.includes(item.getAttribute('href'))) {
            item.classList.add('active');
        }
    });
}); 