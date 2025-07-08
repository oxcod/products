// Product Management Application Scripts

// WebAwesome Delete Confirmation Dialog
let deleteProductData = {};

function showDeleteConfirm(button) {
    // Get product data from button attributes
    deleteProductData = {
        id: button.getAttribute('data-product-id'),
        title: button.getAttribute('data-product-title'),
        deleteUrl: button.getAttribute('data-delete-url')

    };
    
    // Update dialog message with product title
    const messageElement = document.getElementById('delete-message');
    messageElement.textContent = `Are you sure you want to delete "${deleteProductData.title}"? This action cannot be undone.`;
    
    // Show the dialog
    document.getElementById('delete-confirm-dialog').open = true;
}

function confirmDelete() {
    // Close dialog first
    document.getElementById('delete-confirm-dialog').open = false;
    
    // Trigger HTMX delete request
    htmx.ajax('DELETE', deleteProductData.deleteUrl, {
        target: '#product-table-fragment',
        swap: 'outerHTML'
    });
}

// HTMX Event Listeners
document.addEventListener('DOMContentLoaded', function() {
    // Listen for HTMX events to open dialog after content loads
    document.body.addEventListener('htmx:afterSwap', function(event) {
        if (event.target.id === 'dialog-content') {
            document.getElementById('edit-dialog').open = true;
        }
    });
});