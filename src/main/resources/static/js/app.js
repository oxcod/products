// Product Management Application Scripts

// Modal Functions
function openEditModal() {
    document.getElementById('edit-modal').style.display = 'block';
}

function closeEditModal() {
    document.getElementById('edit-modal').style.display = 'none';
}

function closeEditModalOnOverlay(event) {
    if (event.target === event.currentTarget) {
        closeEditModal();
    }
}

// Delete Button Functionality
function handleDeleteClick(event, element) {
    event.preventDefault();
    
    if (element.classList.contains('delete-confirm')) {
        // Second click - trigger HTMX delete
        htmx.trigger(element, 'confirmed');
    } else {
        // First click - show confirmation
        element.classList.add('delete-confirm');
        element.innerHTML = '<span class="checkmark">✓</span>Sure?';
        
        // Reset after 3 seconds if not clicked
        setTimeout(function() {
            if (element.classList.contains('delete-confirm')) {
                element.classList.remove('delete-confirm');
                element.innerHTML = 'Delete';
            }
        }, 3000);
    }
    
    return false;
}

// HTMX Event Listeners
document.addEventListener('DOMContentLoaded', function() {
    // Listen for HTMX events to open modal after content loads
    document.body.addEventListener('htmx:afterSwap', function(event) {
        if (event.target.id === 'modal-content') {
            openEditModal();
        }
    });
});